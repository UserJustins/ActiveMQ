package cn.duheng.helloworld.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*************************
 @author: 杜衡
 @Date: 2020/6/25
 @Describe:
    订阅消息
 *************************/
public class ConsumerTopic {
    private final static String ACTIVEMQ_URL = "tcp://192.168.246.132:61616";
    private final static String TOPIC_NAME = "TOPIC.FOO";

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);
        MessageConsumer consumer = session.createConsumer(topic);
        //监听获取消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message != null && message instanceof TextMessage) {
                    TextMessage text = (TextMessage)message;
                    String value = null;
                    try {
                        value = text.getText();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                    System.out.println(value);
                }
            }
        });

        System.in.read();
        consumer.close();
        session.close();
        connection.close();
    }
}
