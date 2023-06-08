package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import model.CompleteData;
import model.CustomerCharge;
import model.Job;
import service.ObjectMapperService;
import service.RabbitMQService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static service.RabbitMQService.CUSTOMER_CHARGE_QUEUE_NAME;
import static service.RabbitMQService.JOB_START_QUEUE_NAME;

public class MessageHandler {
    private final ObjectMapperService objectMapperService = new ObjectMapperService();
    private final Map<String, Job> jobsMap = new HashMap<>();
    private final Map<String, List<CustomerCharge>> customerChargesMap = new HashMap<>();

    public Map<String, Double> groupCustomerChargesByUid(List<CustomerCharge> customerCharges) {
        return customerCharges.stream()
                .collect(Collectors.groupingBy(CustomerCharge::getUid,
                        Collectors.summingDouble(CustomerCharge::getSumKwh)));
    }

    public CompleteData mapToCompleteData(List<CustomerCharge> customerCharges, Map<String, Double> groupedData) {
        String uid = customerCharges.get(0).getUid();  // Assuming the list contains only one CustomerCharge object
        double totalKwh = groupedData.get(uid);
        int customerId = getCustomerId(customerCharges, uid);
        return new CompleteData(uid, customerId, totalKwh);
    }

    public int getCustomerId(List<CustomerCharge> customerCharges, String uid) {
        return customerCharges.stream()
                .filter(charge -> charge.getUid().equals(uid))
                .findFirst()
                .map(CustomerCharge::getCustomerId)
                .orElse(-1);  // use a default value if not found
    }

    public void sendMessage(Channel channel, RabbitMQService rabbitMQService, CompleteData completeData) {
        try {
            String message = new ObjectMapper().writeValueAsString(completeData);
            rabbitMQService.sendMessage(channel, RabbitMQService.COMPLETE_DATA_QUEUE_NAME, message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void consumeJobStartNotifications(Channel channel, RabbitMQService rabbitMQService) {
        rabbitMQService.consumeMessage(channel, JOB_START_QUEUE_NAME, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
            Job job = objectMapperService.readValue(message, Job.class);
            if (job != null) {
                jobsMap.put(job.getUid(), job);
            }
        });
    }

    public void consumeCustomerChargeNotifications(Channel channel, RabbitMQService rabbitMQService) {
        rabbitMQService.consumeMessage(channel, CUSTOMER_CHARGE_QUEUE_NAME, (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
            CustomerCharge customerCharge = objectMapperService.readValue(message, CustomerCharge.class);
            if (customerCharge != null) {
                handleCustomerCharge(channel, rabbitMQService, customerCharge);
            }
        });
    }

    private void handleCustomerCharge(Channel channel, RabbitMQService rabbitMQService, CustomerCharge customerCharge) {
        String jobId = customerCharge.getUid();
        customerChargesMap.computeIfAbsent(jobId, k-> new ArrayList<>()).add(customerCharge);
        Optional.ofNullable(jobsMap.get(jobId)).ifPresent(job -> {
            if (job.getExpectedMessageCount() == customerChargesMap.get(jobId).size()) {
                try {
                    sendCompleteData(channel, rabbitMQService, customerCharge);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void sendCompleteData(Channel channel, RabbitMQService rabbitMQService, CustomerCharge customerCharge) throws JsonProcessingException {
        Map<String, Double> groupedData = groupCustomerChargesByUid(Collections.singletonList(customerCharge));
        CompleteData completeData = mapToCompleteData(Collections.singletonList(customerCharge), groupedData);
        sendMessage(channel, rabbitMQService, completeData);
    }
}
