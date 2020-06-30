package cn.duheng.persistent.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*************************
 @author: 杜衡
 @Date: 2020/6/26
 @Describe:
    消息的消费者
 *************************/
public class ConsumerPersistent {
    private final static  String ACTIVEMQ_URL = "tcp://192.168.246.133:61616";
    private final static  String QUEUE_NAME = "FOO";

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);
        MessageConsumer consumer = session.createConsumer(queue);
        connection.start();
        //阻塞等待消息
        Message message = consumer.receive();
        //接受到消息进入循环
        while(message!=null){
            if (message instanceof MapMessage) {
                MapMessage mapMessage = (MapMessage)message;
                String v1 = mapMessage.getString("HELLO");
                String v2 = mapMessage.getString("FLAG");
                System.out.println("接受的消息-->"+v1+"===="+v2);

            }
            //消费完消息，再次阻塞等待，知道接受到消息接着循环
            message = consumer.receive();
        }
        consumer.close();
        session.close();
        connection.close();
    }
}
