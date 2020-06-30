package cn.duheng.persistent.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*************************
 @author: 杜衡
 @Date: 2020/6/26
 @Describe:
    Topic的持久化、签收、事务
 *************************/
public class ProducerPersistentTopic {
    private final static  String ACTIVEMQ_URL = "tcp://192.168.246.134:61616";
    private final static  String TOPIC_NAME = "FOO";

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);
        //创建一个Map类型的消息
        MapMessage mapMessage = session.createMapMessage();
        mapMessage.setString("HELLO","WORLD1234");
        mapMessage.setBoolean("FLAG",false);
        MessageProducer producer = session.createProducer(topic);
        //持久化，默认就是持久化的
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        producer.send(mapMessage);

        //关闭资源
        producer.close();
        session.commit();
        session.close();
        connection.close();
    }
}
