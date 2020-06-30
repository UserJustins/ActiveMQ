package cn.duheng.helloworld.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*************************
 @author: 杜衡
 @Date: 2020/6/24
 @Describe:
    创建消费者
 *************************/
public class ProductDemo {
    /**
     * activeMQ url
     */
    private final static String ACTIVEMQ_URL = "tcp://192.168.246.132:61616";


    public static void main(String[] args){
        try {
            //create a connectionfactory
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
            //create a connection
            Connection connection = factory.createConnection();
            connection.start();
            //create a session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //create a destination (Topic or Queue)
            Destination queue = session.createQueue("TEST.FOO");
            //create MessageProducer from session to Queue
            MessageProducer producer = session.createProducer(queue);
            //队列的非持久化
            //producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            //队列的持久化，默认就是持久化
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            for (int i = 0; i < 3; i++) {

                //create message and tell the producer to send the message
                TextMessage message = session.createTextMessage("HELLO WORLD"+i);
                producer.send(message);
            }

            //close resource
            producer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
