package com.charge.autorization.internalas.consumer;

import com.charge.autorization.internalas.model.MessageDto;
import com.charge.autorization.internalas.service.ProcessService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MessageConsumer {

    private ProcessService processService;

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void receiveMessage(MessageDto message) {
        log.info("Message Received::: Station Id = {} ::: Driver Id = {} ::: CallBack url = {}",
                message.getStationId(), message.getDriverId(), message.getCallback());
        try {
            processService.processMessage(message);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
    }

}
