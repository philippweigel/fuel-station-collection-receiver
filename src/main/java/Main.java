import com.rabbitmq.client.Channel;
import config.RabbitMQConfig;
import service.RabbitMQService;
import util.MessageHandler;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {

    private static final int WAIT_DURATION_FOR_JOB_START_NOTIFICATIONS  = 5000;

    public static void main(String[] args) {

        RabbitMQService rabbitMQService = new RabbitMQService();
        MessageHandler messageHandler = new MessageHandler();
        Channel channel;

        try {
            channel = RabbitMQConfig.setupRabbitMQChannel();
            messageHandler.consumeJobStartNotifications(channel, rabbitMQService);
            Thread.sleep(WAIT_DURATION_FOR_JOB_START_NOTIFICATIONS);
            messageHandler.consumeCustomerChargeNotifications(channel, rabbitMQService);
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }





}
