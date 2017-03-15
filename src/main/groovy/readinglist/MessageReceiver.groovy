package readinglist

import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageType

import javax.jms.ConnectionFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.annotation.JmsListener
import org.springframework.jms.listener.SimpleMessageListenerContainer
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.stereotype.Component
import com.rabbitmq.jms.admin.RMQConnectionFactory

@Configuration
@EnableJms
class MessageReceiver {
    @Component
    static class Receiver {

        @JmsListener(destination = MessageReceiver.BOOK_QUEUE)
        void onMessage(Book book) {
            logger.info("Received <" + book + ">")
        }

    }
    static Logger logger = LoggerFactory.getLogger(MessageReceiver)

    static final String BOOK_QUEUE = 'bookListUpdates'

    @Bean
    @Profile('heroku')
    ConnectionFactory connectionFactory() {
        String uri = System.getenv("CLOUDAMQP_URL")
        if (uri == null) uri = "amqp://guest:guest@localhost"
        logger.info('Rabbit URI {}', uri)
        def match = uri =~ /amqp:\/\/(\w+):(\w+)@([^:\/]+)(:\d+)?(\/\w+)?/
        if (!match.matches()) throw new IllegalArgumentException(uri)
        def factory = new RMQConnectionFactory()
        factory.setUsername(match.group(1))
        factory.setPassword(match.group(2))
        if (match.group(5)) factory.setVirtualHost(match.group(5).substring(1))
        factory.setHost(match.group(3))
        if (match.group(4)) factory.setPort(Integer.parseInt(match.group(4).substring(1)))
        factory
    }

    @Bean // Serialize message content to json using TextMessage
    MessageConverter jacksonMessageConverter() {
        logger.info('Creating converter <<<<')
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter()
        converter.typeIdPropertyName = '_type'
        converter.targetType = MessageType.TEXT
        converter
    }

}
