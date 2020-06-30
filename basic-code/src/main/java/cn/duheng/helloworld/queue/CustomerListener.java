package cn.duheng.helloworld.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*************************
 @author: 杜衡
 @Date: 2020/6/25
 @Describe:
    监听的方式来接受消息
 *************************/
public class CustomerListener {
    private static final String ACTIVEMQ_URL = "tcp://192.168.246.132:61616";
    private static final String QUEUE_NAME = "TEST.FOO";

    public static void main(String[] args) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        try {
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(QUEUE_NAME);
            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if (message != null && message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage)message;
                        String value = null;
                        try {
                            value = textMessage.getText();
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                        System.out.println(value);
                    }
                }
            });
            //让控制台一直阻塞，也就是说需要让程序一直跑着，
            //不然都来不及连接程序就关闭完了所有的连接资源
            System.in.read();
            consumer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
