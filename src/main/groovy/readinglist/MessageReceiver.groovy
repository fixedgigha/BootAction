package readinglist

import org.springframework.jms.support.converter.MappingJackson2MessageConverter

import javax.jms.ConnectionFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.annotation.JmsListener
import org.springframework.jms.listener.SimpleMessageListenerContainer
import org.springframework.jms.listener.adapter.MessageListenerAdapter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.stereotype.Component

import javax.jms.Queue

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

    @Bean // Serialize message content to json using TextMessage
    MessageConverter jacksonMessageConverter() {
        logger.info('Creating converter <<<<')
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter()
        converter.typeIdMappings = ['book' : Book]
        converter
    }


    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter adapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer()
        container.setConnectionFactory(connectionFactory)
        container.setDestinationName(BOOK_QUEUE)
        container.setMessageListener(adapter)
        container
    }

}

