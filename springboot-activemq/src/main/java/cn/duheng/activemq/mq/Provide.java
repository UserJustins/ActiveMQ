package cn.duheng.activemq.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import java.util.UUID;

/*************************
 @author: 杜衡
 @Date: 2020/6/28
 @Describe:
 *************************/
@Component
public class Provide {
    /**
     * destination
     */
    @Autowired
    private Queue queue;

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * activemq发送消息，调用一次发送一次，建议走单元测试（已编写）
     * @param message 消息
     */
    public void sendMessage(String message){
        jmsTemplate.convertAndSend(queue,message);
    }
    /**
     * activemq定时发送消息
     *  建议直接启动主程序
     *
     */
    @Scheduled(fixedDelay = 3000L)
    public void sendMessageScheduled(){
        String message = UUID.randomUUID().toString().substring(0,6);
        jmsTemplate.convertAndSend(queue, message);
        System.out.println("定时投递的消息->"+message);
    }
}
