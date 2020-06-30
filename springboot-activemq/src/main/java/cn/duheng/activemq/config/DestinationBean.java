package cn.duheng.activemq.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;


/*************************
 @author: 杜衡
 @Date: 2020/6/28
 @Describe:
    获取配置文件中的队列或topic并创建
 *************************/
@Configuration
public class DestinationBean {


    @Value("${destination-queue-name}")
    private String queueName;

    /**
     * 创建队列
     * @return
     */
    @Bean
    public Queue queue(){
        return new ActiveMQQueue(queueName);
    }

}
