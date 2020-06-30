package cn.duheng.persistent.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*************************
 @author: 杜衡
 @Date: 2020/6/26
 @Describe:
    Topic中订阅者方的持久化、事务、签收
 *************************/
public class SubscriberPersistentTopic {
    private final static  String ACTIVEMQ_URL = "tcp://192.168.246.134:61616";
    private final static  String TOPIC_NAME = "FOO";

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        //设置订阅者ID
        connection.setClientID("AD");
        connection.start();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);
        TopicSubscriber subscriber = session.createDurableSubscriber(topic,"remark...");
        Message message = subscriber.receive();
        //接受到消息进入循环
        while(message!=null){
            if (message instanceof MapMessage) {
                MapMessage mapMessage = (MapMessage)message;
                String v1 = mapMessage.getString("HELLO");
                String v2 = mapMessage.getString("FLAG");
                System.out.println("接受的消息-->"+v1+"===="+v2);
                //手动签收应答
                message.acknowledge();
            }
            //消费完消息，再次阻塞等待，知道接受到消息接着循环
            message = subscriber.receive();
        }
        subscriber.close();
        //session.commit();
        session.close();
        connection.close();
    }
}
