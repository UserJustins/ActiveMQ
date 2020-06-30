package cn.duheng.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/*************************
 @author: 杜衡
 @Date: 2020/6/28
 @Describe:
    Queue或Topic的监听程序，具体监听谁是在配置文件中配置的
 *************************/
public class MessageListenerImpl implements MessageListener {

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textmessage = (TextMessage) message;
            try {
                String value = textmessage.getText();
                System.out.println("监听接受到的消息---->"+value);
            } catch (JMSException e) {
                e.printStackTrace();
            }

        }
    }
}
