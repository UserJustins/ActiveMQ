# 解耦、异步、削峰

![](img\2020-06-23_204427.png)

# 两种模式队列比较

![](img\2020-06-25_202024.png)

# 异常

## java.io.EOFException

如果消费者在监听期间，activeMQ的服务器关闭后就会出现以下异常信息

![](img\2020-06-26_230128.png)

## Setting clientID on a used Connection is not allowed

==**不允许在已使用的连接上设置clientID**==

该异常出现的原因就是设置clientID放在了start之后

![](img\2020-06-26_233324.png)

## java.lang.NoClassDefFoundError: org/springframework/core/ErrorCoded

# 一、Install

## Install JDK

activeMQ使用了java开发，因此安装前需要JDK的环境

### 1、Unload OpenJDK

通常情况下CentOS 7默认会安装OpenJDK ,此时安装JDK的话首先需要卸载掉OpenJDK

使用下面得命令检查Linux中是否默认安装了OpenJDK

```shell
java -version
```

![](img\2020-06-20_230011.png)

如果存在就删除OpenJDK软件包，使用 rpm -qa | grep java查询系统中所有的跟java相关的rpm包

![](img\2020-06-20_230448.png)

使用rpm -e --nodeps  "rpm包全名"   的方式删除

```shell
rpm -e --nodeps java-1.7.0-openjdk-1.7.0.111-2.6.7.8.el7.x86_64
rpm -e --nodeps java-1.7.0-openjdk-headless-1.7.0.111-2.6.7.8.el7.x86_64

#参数--nodeps:忽略rpm包之间的相互依赖
```

### 2、Install JDK

（1）上传压缩包并进行解压 **opt目录就是第三方软件的地方**   /opt/jdk/jdk1.8.0_251

上传JDK压缩包到/opt/jdk中，使用  tar -zxvf  压缩包名 进行解压

（2）配置环境变量

编辑/etc/profile文件，添加以下内容

```shell
export JAVA_HOME=/usr/local/jdk1.8.0_181  #jdk安装目录
 
export JRE_HOME=${JAVA_HOME}/jre
 
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib:$CLASSPATH
 
export JAVA_PATH=${JAVA_HOME}/bin:${JRE_HOME}/bin
 
export PATH=$PATH:${JAVA_PATH}
```

（3）刷新配置文件

```shell
source /etc/profile
```

（4）检测 java -version

## Install ActiveMQ

1、解压

2、启动/关闭

​	(1)普通启动 bin目录下    ./activemq start/stop

​	(2)日志启动 ./activemq start/stop > 日志文件路径

3、查看启动是否成功

activeMQ默认的端口号就是61616

(1) 方式一：查看进程号

ps -ef | grep activemq [| grep  -v  grep] 获取进程号

(2)方式二：查看端口号

netstat -anp|grep 61616

## 访问activeMQ 控制台

1、开启防火墙 

```shell
 systemctl start firewalld
```

2、开放指定端口

```shell
      firewall-cmd --zone=public --add-port=61616/tcp --permanent
```

 命令含义：
--zone #作用域
--add-port=1935/tcp  #添加端口，格式为：端口/通讯协议
--permanent  #永久生效，没有此参数重启后失效

3、重启防火墙

```shell
 firewall-cmd --reload
```

4、查看端口号
netstat -ntlp   //查看当前所有tcp端口·

```shell
netstat -ntulp |grep 61616   #查看所有1935端口使用情况·
```

**需要注意：控制台的端口为8161，记得要开放**

控制台地址：http://192.168.246.129:8161/admin/

账号密码：admin

![](img\2020-06-23_203833.png)

# 二、HelloWorld

maven

```maven
        <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-client</artifactId>
            <version>5.15.0</version>
        </dependency>
```

这是官方文档中对activeMQ的简单使用http://activemq.apache.org/version-5-hello-world



```java
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldConsumer(), false);
        Thread.sleep(1000);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
        Thread.sleep(1000);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldProducer(), false);
        Thread.sleep(1000);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldConsumer(), false);
        thread(new HelloWorldProducer(), false);
    }

    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    public static class HelloWorldProducer implements Runnable {
        public void run() {
            try {
                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();

                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue("TEST.FOO");

                // Create a MessageProducer from the Session to the Topic or Queue
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                // Create a messages
                String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
                TextMessage message = session.createTextMessage(text);

                // Tell the producer to send the message
                System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
                producer.send(message);

                // Clean up
                session.close();
                connection.close();
            }
            catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }
    }

    public static class HelloWorldConsumer implements Runnable, ExceptionListener {
        public void run() {
            try {

                // Create a ConnectionFactory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

                // Create a Connection
                Connection connection = connectionFactory.createConnection();
                connection.start();

                connection.setExceptionListener(this);

                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue("TEST.FOO");

                // Create a MessageConsumer from the Session to the Topic or Queue
                MessageConsumer consumer = session.createConsumer(destination);

                // Wait for a message
                Message message = consumer.receive(1000);

                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println("Received: " + text);
                } else {
                    System.out.println("Received: " + message);
                }

                consumer.close();
                session.close();
                connection.close();
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
        }

        public synchronized void onException(JMSException ex) {
            System.out.println("JMS Exception occured.  Shutting down client.");
        }
    }
}
```

# 三、消费的两种方式

![](img\2020-06-25_161450.png)

## Receive

receive方法存在重载，一个参数为空，一个参数为long的时间

receive（）：一直不停的接受，不见不散的那种。

receive（time）：超过规定的时间就不在接受，过时不候的那种。

```java
package cn.duheng.HelloWorld;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.xml.soap.Text;

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

```



## Listener

```java
package cn.duheng.HelloWorld;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

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

```



# 四、Queue

思考：1、生产者先生产消息，接着启动唯一的消费者；消息会被怎样的消费？

​			2、生产者先生产消息，接着依次启动多个的消费者；请问消息会被如何消费？

​			3、先启动唯一的消费者，接着启动生产者；消息给以何种方式被消费？

​			4、先依次启动多个消费者进行监听，接着生产者生产消息；请问消息又是如何被消费的？



对于问题1＆3:  如果只有一个消费者存在的情况，不论先启动生产者还是消费者，消费全都会被消费者消费殆尽；问题2：先到先得，后来的连汤都没有了；问题4：默认是一种轮询的方式（简单对比理解成负载均衡），平均分配。

根据上面的问题还有一个点需要理解的是，Queue与时间无关，不管谁先启动，只要生产者生产的消息都会被消费。这和Topic有着本质的区别，Topic一直强调先订阅然后才有消息。

# 五、Topic

订阅模式，一对多，强调的是先订阅后发布，所有的订阅者都会接受到自订阅之后发布者发布的消息

## 发布者

```java
package cn.duheng.topic;

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

```

## 订阅者

```java
package cn.duheng.topic;

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

```

# 六、消息类型

activeMQ中支持以下五种消息类型 ，常用的消息类型也就前两种

![](img\2020-06-25_205232.png)

# 七、消息的可靠性

消息的可靠性将从持久化、事务、签收、四个维度来进行学习

## 1、持久化

服务器宕机后，队列中没有被消费的消息在重启服务之后是否还存在

（1）、Queue队列的持久化相对来说比较简单，只需要在生产者中写入下面的一行代码即可

```java
//队列Queue的非持久化
producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
//队列Queue的持久化，默认就是持久化
producer.setDeliveryMode(DeliveryMode.PERSISTENT);
```

**生产者**

```java
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
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);
        //创建一个Map类型的消息
        MapMessage mapMessage = session.createMapMessage();
        mapMessage.setString("HELLO","你好");
        mapMessage.setBoolean("FLAG",false);
        MessageProducer producer = session.createProducer(queue);
        //******************************************************
        //持久化，默认就是持久化的
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        //******************************************************
        producer.send(mapMessage);
        //关闭资源
        producer.close();
        session.close();
        connection.close();
    }
}

```

演示：此时生产者已生产了两条消息到队列中，在消息没有被消费前将服务器宕机后重新启动

![](img\2020-06-26_230658.png)

**消费者**

```java
package cn.duheng.persistent.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*************************
 @author: 杜衡
 @Date: 2020/6/26
 @Describe:
    消息的消费者
 *************************/
public class ConsumerPersistent {
    private final static  String ACTIVEMQ_URL = "tcp://192.168.246.133:61616";
    private final static  String QUEUE_NAME = "FOO";

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);
        MessageConsumer consumer = session.createConsumer(queue);
        connection.start();
        //阻塞等待消息
        Message message = consumer.receive();
        //接受到消息进入循环
        while(message!=null){
            if (message instanceof MapMessage) {
                MapMessage mapMessage = (MapMessage)message;
                String v1 = mapMessage.getString("HELLO");
                String v2 = mapMessage.getString("FLAG");
                System.out.println("接受的消息-->"+v1+"===="+v2);

            }
            //消费完消息，再次阻塞等待，知道接受到消息接着循环
            message = consumer.receive();
        }
        consumer.close();
        session.close();
        connection.close();
    }
}

```

当服务器再次成功启动，消费者依旧可以从队列中接受到服务器宕机前的消息

![](img\2020-06-26_231520.png)

producer.setDeliveryMode(DeliveryMode.PERSISTENT);这样服务器宕机了，队列中的消息也是不会丢失的，当服务器再次启动的时候，监听的消费者依旧会接受到没有被消费过得消息。放心此种情况不会出现消息被重复消费。队列中的消息只会被消费一次。

（2）、Topic的持久化，发布者依旧只需要setDeliveryMode(DeliveryMode.PERSISTENT)即可

**发布者**

```java
package cn.duheng.persistent.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*************************
 @author: 杜衡
 @Date: 2020/6/26
 @Describe:
    Topic的持久化
 *************************/
public class ProducerPersistentTopic {
    private final static  String ACTIVEMQ_URL = "tcp://192.168.246.133:61616";
    private final static  String TOPIC_NAME = "FOO";

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);
        //创建一个Map类型的消息
        MapMessage mapMessage = session.createMapMessage();
        mapMessage.setString("HELLO","WORLD");
        mapMessage.setBoolean("FLAG",false);
        MessageProducer producer = session.createProducer(topic);
        //持久化，默认就是持久化的
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        producer.send(mapMessage);

        //关闭资源
        producer.close();
        session.close();
        connection.close();
    }
}

```

**订阅者**

```java
package cn.duheng.persistent.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/*************************
 @author: 杜衡
 @Date: 2020/6/26
 @Describe:
    Topic中订阅者方的持久化
 *************************/
public class SubscriberPersistentTopic {
    private final static  String ACTIVEMQ_URL = "tcp://192.168.246.133:61616";
    private final static  String TOPIC_NAME = "FOO";

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        //设置订阅者ID
        connection.setClientID("AD");
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
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

            }
            //消费完消息，再次阻塞等待，知道接受到消息接着循环
            message = subscriber.receive();
        }
        subscriber.close();
        session.close();
        connection.close();
    }
}

```

订阅者一方的持久化：首先需要自报家门设置setClientID(String id)，用于在发布者一方注册，以便告诉发布者你是谁；接着就不在是创建consumer而是subscribe订阅者createDurableSubscribe(Destination topic，String remark)，最后就是订阅者接受消息。

`注意：Topic的持久化是建立在订阅者已订阅的基础之上；订阅者一定先要进行订阅注册，订阅之后才能接受消费消息`

订阅者在成功订阅之后如果处于offline的状态期间发布者持续发送的消息，订阅者是不能够进行消费的，但是订阅者一旦从offline状态变成active状态，那么offline期间没有消费的消息都会被接受到。（这里的offline和active指的就是监听程序有没有在运行中），这也是视频中讲的那种持久化。但我使用代码验证觉得这只是持久化的表现之一。服务器宕机同样表现出了持久化的特性

补充说明：订阅者在离线状态下服务器成功发布了几条消息但是订阅者并没有进行消费（active状态是不存在这样的状况，因为总是能及时消费）此时如果服务器宕机了，重新启动后，虽然控制页面会显示有0条消息，可是当订阅者激活以后照样能接收到此前离线状态下未消费的消息。这也是持久化的表现。请跟着下面的演示走试试。

![](img\2020-06-26_235324.png)

**演示**：

第一、订阅者成功订阅并持续监听，接着发布者成功发布消息保证订阅者能成功接受（基础功能正常）

第二、将订阅者的状态由Active转为Offline，即关闭订阅者程序，接着让发布者发布消息后关闭服务器

第三、重新启动activeMQ服务器

第四、开启订阅者程序，订阅者由Offline转Active，如果成功接受到第二步发布者发布的消息则持久化成功

## 2、事务

事务在代码上相对来说就简单的多了，在创建session时候进行参数设置和手动提交，但是它和接下来要讲的签收有千丝万缕的联系。

**注意：事务在消费者一方控制不好会出现消息重复消费**

```java
connection.createSession(是否开启事务, 签收方式);
//开启了事务一定要手动的进行提交，不然会重复消费
```

事务在代码上没有那么的复杂，就如上只需设置参数的true或false，在session关闭之前手动的提交事务即可，**（在消费方如果开启了事务一定要记得提交，不然activeMQ会认为你从来就没有消费过消息；但你却真真切切的接受到了，意味着程序已经对消息进行了业务处理 。这样就糟糕透了，因为你一直在重复的消费消息）**

以Topic为例进行说明：

当生产者开启了事务但是忘记提交那么消息就不会存在于队列中

![](img\2020-06-27_104101.png)

同理订阅者在开启了事务却没有提交的话，消息会被重复的消费

![](img\2020-06-27_114616.png)



## 3、签收

事务和签收这两个参数的设置关系到消息是否被重复消费

1、非事务会话：如果设置了手动签收，那么一定要记得应答，否则就会出现消息重复消费

![](img\2020-06-27_123212.png)

2、事务会话：开启事务只要记得提交，不论你是何种签收模式，（就算你手动签收，忘记消息应答）都不会出现重复消费的问题

## 何时会出现重复消费

重复消费总是和事务以及签收脱不开关系，事务偏生产者，签收偏消费者。

1、消费者开启事务但没有提交事务

2、消费者不开启事务，签收方式为手动但消费者在接受消息后没有进行应答

# 八、Spring-activeMQ

## pom.xml

spring-jms包已经依赖导入了spring常用的核心包

![](img\2020-06-28_000551.png)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>cn.duheng</groupId>
    <artifactId>spring-activeMQ</artifactId>
    <version>1.0-SNAPSHOT</version>

     <properties>
        <spring-version>4.3.19.RELEASE</spring-version>
     </properties>

    <dependencies>
        <!-- https://mvnrepository.com/artifact/org.springframework/spring-jms -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jms</artifactId>
            <version>4.3.19.RELEASE</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.apache.activemq/activemq-pool -->
        <dependency>
            <groupId>org.apache.activemq</groupId>
            <artifactId>activemq-pool</artifactId>
            <version>5.14.5</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.springframework/spring-test -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>${spring-version}</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13</version>
        </dependency>
    </dependencies>
</project>
```

## SpringContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd ">


    <!--使用连接池配置连接-->
    <bean id="jmsFactory" class="org.apache.activemq.pool.PooledConnectionFactory">
        <property name="connectionFactory">
            <bean class="org.apache.activemq.ActiveMQConnectionFactory">
                <property name="brokerURL" value="tcp://192.168.246.135:61616"/>
            </bean>
        </property>
        <property name="maxConnections" value="100"/>
    </bean>
    <!--设置目的地 Queue-->
    <bean id="destinationQueue" class="org.apache.activemq.command.ActiveMQQueue">
        <constructor-arg index="0" value="QUEUE_NAME"/>
    </bean>
    <!--设置目的地 Topic-->
    <bean id="destinationTopic" class="org.apache.activemq.command.ActiveMQTopic">
        <constructor-arg index="0" value="TOPIC_NAME"/>
    </bean>
    <!--spring 提供JMSTemplate对消息进行操作-->
    <bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate">
        <property name="connectionFactory" ref="jmsFactory"/>
        <!--<property name="defaultDestination" ref="destinationQueue"/>-->
        <property name="defaultDestination" ref="destinationTopic"/>
        <!--消息自动转换-->
        <property name="messageConverter">
            <bean class="org.springframework.jms.support.converter.SimpleMessageConverter"/>
        </property>
    </bean>
    <!--activeMQ监听器-->
    <bean id="jmsContainer" class="org.springframework.jms.listener.DefaultMessageListenerContainer">
        <property name="connectionFactory" ref="jmsFactory"/>
        <property name="destination" ref="destinationTopic"/>
        <property name="messageListener" ref="messageListenerImpl"/>
    </bean>
    <!--监听程序-->
    <bean id="messageListenerImpl" class="cn.duheng.activemq.MessageListenerImpl"/>
</beans>
```

## MessageListenerimpl

可以使用receive同步阻塞一直接受消息；但是配置监听器程序，监听程序不用启动运行，只要生产者往队列中发消息，立马就能接受。

在spring中使用监听程序需要两步操作：

1、编写监听程序 	

```java
package cn.duheng.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/*************************
 @author: 杜衡
 @Date: 2020/6/27
 @Describe:
    监听程序:监听程序不用启动，生产者只要生产了它立马可以拿到
 *************************/
public class MessageListenerImpl implements MessageListener {

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage){
            TextMessage textMessage = (TextMessage)message;
            try {
                String value = textMessage.getText();
                System.out.println("监听程序接受到的消息--->"+value);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}

```

2、SpringContext.xml中配置监听程序

## 附带: 常规的生产和消费

如果没有配置监听器，程序代码是这样的

```java
package cn.duheng.activemq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * 不管是Queue还是Topic代码都是一样的 ,不用动就是下面这一套代码
 * 相应的destination是在xml中进行配置的，需要说明：代码中consumeMessage方法
 * 对Queue和Topic来说有点小差异
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:springContext.xml")
public class QueueOrTopicTest {
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 生产者生产消息
     */
    @Test
    public void createMessage(){
        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("spring-activeMQ-Message");
            }
        });
    }

    /**
     * 消费消息：
     * Queue：运行一次只会取队列中的一个消息而已
     * Topic：先启动消费者它会一直监听，但生产者
     * 只要放个消息在队列它拿起就跑，而且懒得跑第二次
     *	因此可以使用配置监听器的方式实现一直监听，并且不管是Queue或Topic，监听程序都不用启动
     	生产者生产完消息放入队列，监听程序都能立马接受到
     */
    @Test
    public void consumeMessage(){
        String  value =(String)jmsTemplate.receiveAndConvert();
        System.out.println(value);
    }


}
```

# 九、SpringBoot-activeMQ

## pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.1.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>cn.duheng</groupId>
    <artifactId>springboot-activemq</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>springboot-activemq</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>
	<!-- activemq-->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-activemq</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```



## application.yml

```yaml
spring:
  activemq:
    broker-url: tcp://192.168.246.135:61616
    user: admin
    password: admin
  #如果是点对点（queue），那么此处默认应该是false，如果发布订阅，那么一定设置为true
  jms:
    pub-sub-domain: false
#自定义属性配置：目的地的名称
destination-queue-name: TEST.FOO

```

## config

将配置文件中配置的destination读进程序中，并创建对应的Destination对象

```java
package cn.duheng.activemq.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;


/*************************
 @author: 杜衡
 @Date: 2020/6/28
 @Describe:
    获取配置文件中的队列或topic并创建
 *************************/
@Configuration
public class DestinationBean {


    @Value("${destination-queue-name}")
    private String queueName;

    /**
     * 创建队列
     * @return
     */
    @Bean
    public Queue queue(){
        return new ActiveMQQueue(queueName);
    }

}

```

## Provide

```java
package cn.duheng.activemq.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import java.util.UUID;

/*************************
 @author: 杜衡
 @Date: 2020/6/28
 @Describe:
 *************************/
@Component
public class Provide {
    /**
     * destination
     */
    @Autowired
    private Queue queue;

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * activemq发送消息，调用一次发送一次，建议走单元测试（已编写）
     * @param message 消息
     */
    public void sendMessage(String message){
        jmsTemplate.convertAndSend(queue,message);
    }
    /**
     * activemq定时发送消息
     *  建议直接启动主程序
     *
     */
    @Scheduled(fixedDelay = 3000L)
    public void sendMessageScheduled(){
        String message = UUID.randomUUID().toString().substring(0,6);
        jmsTemplate.convertAndSend(queue, message);
        System.out.println("定时投递的消息->"+message);
    }
}

```

生产消息就是这么的简单，注意关注程序中的备注。

## 主启动类

@EnableJms ：开启JMS功能

@EnableScheduling： 开启定时投递的功能

```java
package cn.duheng.activemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableJms
@EnableScheduling
public class SpringbootActivemqApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootActivemqApplication.class, args);
    }

}

```











































