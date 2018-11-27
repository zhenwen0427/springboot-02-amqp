package com.atguigu.amqp;

import com.atguigu.amqp.bean.Book;
import com.rabbitmq.client.AMQP;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot02AmqpApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Test
    public void createExchange(){
        //mqpAdmin.declareExchange(new DirectExchange("amqpadmin.exchange"));
        amqpAdmin.declareQueue(new Queue("amqpadmin.queue",true));
        System.out.println("创建完成!");
    }

    /**
     * 1、单播:点对点
     */
    @Test
    public void contextLoads() {
        //Message需要自己构造一个;定义消息体内容和消息头
        //rabbitTemplate.send(exchage,routeKey,message);

        //只需要传入要发送的对象，自动序列化给rabbitmq;
        //rabbitTemplate.convertAndSend(exchage,routeKey,message);
        Map<String,Object> map = new HashMap<>();
        map.put("msg","这是第一条消息");
        map.put("data", Arrays.asList("helloworld",126,true));
        //对象被默认序列化以后发送出去
        rabbitTemplate.convertAndSend("exchange.direct","atguigu.news",map);
    }

    //对象数据，如何将数据自动的转为json发送出去
    @Test
    public void receive(){
        Object object = rabbitTemplate.receiveAndConvert("atguigu.news");
        System.out.println(object.getClass());
        System.out.println(object);
    }

    /**
     * 广播模式
     */
    @Test
    public void sendMsg(){
        rabbitTemplate.convertAndSend("exchange.fanout","",new Book("三国演义","罗贯中"));
    }

}
