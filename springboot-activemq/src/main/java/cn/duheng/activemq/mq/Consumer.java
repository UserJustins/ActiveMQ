package cn.duheng.activemq.mq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


/*************************
 @author: 杜衡
 @Date: 2020/6/28
 @Describe:
    springboot 的监听程序，就是这么的简单,
    这个是偷懒了消费者应该再重新建一个工程的。
    放在这里就懒得启动了，生产者单元测试一运行
    立马拿到消息
 *************************/
@Component
public class Consumer {
    /**
     *  @JmsListener(destination = "${destination-queue-name}")
     *  注解@JmsListener:监听哪个destination
     * @param message 队列中的消息
     */
    @JmsListener(destination = "${destination-queue-name}")
    public void messageHandle(String message){
        System.out.println("springBoot接受的消息--->>"+message);
    }
}
