package readinglist

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jms.core.JmsTemplate
import spock.lang.Specification

@SpringBootTest(classes=ReadingListApplication,
        properties = ['spring.datasource.platform=h2'],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringSpockMessageTest extends Specification {

    @Autowired
    JmsTemplate jmsTmpl

    def 'Test JMS infrastructure'() {
        given:
            def payload = new Book(title: 'Title',  author: 'Moby Dick')
        when:
            jmsTmpl.convertAndSend(MessageReceiver.BOOK_QUEUE, payload)
        then:
            sleep 1000L
            true
    }
}
