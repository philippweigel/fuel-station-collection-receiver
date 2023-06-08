package service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RabbitMQService {

    public final static String JOB_START_QUEUE_NAME = "job-start-notifications";
    public final static String CUSTOMER_CHARGE_QUEUE_NAME = "customer-charge";
    public final static String COMPLETE_DATA_QUEUE_NAME = "complete-data";

    public void consumeMessage(Channel channel, String queueName, DeliverCallback deliverCallback) {
        try {
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {

            });
        } catch (IOException e) {
            System.err.println("Failed to consume message from queue " + queueName);
            e.printStackTrace();
        }
    }

    public void sendMessage(Channel channel, String queueName, String message) throws IOException {
        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println(" [x] Sent '" + message + "'");
    }

}
