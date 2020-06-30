package cn.duheng.helloworld.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*************************
 @author: 杜衡
 @Date: 2020/6/25
 @Describe:

 *************************/
public class Customer {

    private final static String ACTIVEMQ_URL = "tcp://192.168.246.132:61616";
    private final static String ACTIVEMQ_QUEUE_NAME = "TEST.FOO";

    public static void main(String[] args) {
        //create a connectionfactory
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;
        try {
            //create a connection from connectionfactory
            connection = factory.createConnection();
            connection.start();
            //create a session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //create a destination Queue
            Destination queue = session.createQueue(ACTIVEMQ_QUEUE_NAME);
            consumer = session.createConsumer(queue);

            while (true){
                //Wait a message
                Message message = consumer.receive();
                if(message instanceof TextMessage){
                    TextMessage text = (TextMessage)message;
                    String value = text.getText();
                    System.out.println(value);
                }
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            try {
                if (consumer != null) {
                    consumer.close();
                }
                if (session != null) {
                    session.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

    }
}
