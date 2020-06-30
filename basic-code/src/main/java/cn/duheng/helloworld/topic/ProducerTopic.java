package cn.duheng.helloworld.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*************************
 @author: 杜衡
 @Date: 2020/6/25
 @Describe:
    订阅模式发布消息
 *************************/
public class ProducerTopic {
    private final static String ACTIVEMQ_URL = "tcp://192.168.246.132:61616";
    private final static String TOPIC_NAME = "TOPIC.FOO";
    private final static String TOPIC_MESSAGE = "HELLO";

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);
        MessageProducer producer = session.createProducer(topic);
        for (int i = 0; i < 3; i++) {
            TextMessage message = session.createTextMessage(TOPIC_MESSAGE+i);
            producer.send(message);
        }

        producer.close();
        session.close();
        connection.close();
        System.out.println("消费发布完毕");
    }
}
