package cn.duheng.persistent.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*************************
 @author: 杜衡
 @Date: 2020/6/26
 @Describe:
    Queue 的持久化代码
 *************************/
public class ProducerPersistent {
    private final static  String ACTIVEMQ_URL = "tcp://192.168.246.133:61616";
    private final static  String QUEUE_NAME = "FOO";

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);
        //创建一个Map类型的消息
        MapMessage mapMessage = session.createMapMessage();
        mapMessage.setString("HELLO","你好");
        mapMessage.setBoolean("FLAG",false);
        MessageProducer producer = session.createProducer(queue);
        //持久化，默认就是持久化的
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        producer.send(mapMessage);
        connection.start();
        //close resources
        producer.close();
        session.close();
        connection.close();
    }
}
