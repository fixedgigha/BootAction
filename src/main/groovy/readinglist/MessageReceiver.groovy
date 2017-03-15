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
      new RMQConnectionFactory()
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
