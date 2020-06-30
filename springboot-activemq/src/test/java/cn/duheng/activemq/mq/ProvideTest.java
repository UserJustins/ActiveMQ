package cn.duheng.activemq.mq;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProvideTest {
    @Autowired
    private Provide provide;

    /**
     * 发送消息
     */
    @Test
    public void sendMessage() {
        provide.sendMessage("springBoot send message");
    }
}