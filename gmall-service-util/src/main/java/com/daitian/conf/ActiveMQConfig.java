package com.daitian.conf;

import com.daitian.util.ActiveMQPoolUtil;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

@Configuration
@ConfigurationProperties
public class ActiveMQConfig {

    @Value("${spring.activemq.broker-url}")
    String brokerUrl;

    @Bean
    public ActiveMQPoolUtil getActiveMQ(){
        if(StringUtils.isBlank(brokerUrl)){
            return null;
        }
        ActiveMQPoolUtil activeMQPoolUtil = new ActiveMQPoolUtil();
        activeMQPoolUtil.init(brokerUrl);
        return activeMQPoolUtil;
    }

    //定义一个消息监听器连接工厂，这里定义的是点对点模式的监听器连接工厂
    @Bean(name = "jmsQueueListener")
    public DefaultJmsListenerContainerFactory jmsQueueListenerContainerFactory(ActiveMQConnectionFactory activeMQConnectionFactory ) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        factory.setConnectionFactory(activeMQConnectionFactory);
        //设置并发数
        factory.setConcurrency("5");

        //重连间隔时间
        factory.setRecoveryInterval(5000L);
        factory.setSessionTransacted(false);
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);

        return factory;
    }
}
