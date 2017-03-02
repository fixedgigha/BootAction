package readinglist

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import spock.lang.Specification

@SpringBootTest(classes=ReadingListApplication,
        properties = ['spring.datasource.platform=h2'],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringSpockRestTest extends Specification {

    @LocalServerPort
    private int port

    @Autowired
    TestRestTemplate rest

    String url

    def setup() {
        url = "http://localhost:$port/api"
    }

    def 'Test REST API'() {
        given:
            def author = getClass().name
            def book = new Book(title: 'Volume 1', author: author, isbn: '65432123', description: 'WHAT' )
        when:
            rest.postForObject(url, book, Void)
            def books = rest.getForObject(url, List)
        then:
            def expected = books.find {it.author == author && it.isbn == '65432123' }
            expected.title == book.title
            expected.author == book.author
            expected.isbn == book.isbn
            expected.description == book.description
    }
}
