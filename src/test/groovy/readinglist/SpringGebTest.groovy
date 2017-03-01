package readinglist

import geb.Page
import geb.spock.GebSpec
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

class ReadListPage extends Page {
    static at = { $('h2').text() == 'Your Reading List' }

    static url = '/'

    static content = {
        submitButton(to: ReadListPage) { $('input', type: 'submit') }
        bookTitle { $('input', type: 'text', name: 'title') }
        author { $('input', type: 'text', name: 'author') }
        isbn { $('input', type: 'text', name: 'isbn') }
        description { $('textarea', name: 'description') }
        listedBook {x -> $('div').$('dl').$('dt.bookHeadline', x) }
    }
}

@SpringBootTest(classes=ReadingListApplication,
        properties = ['spring.datasource.platform=h2'],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringGebTest extends GebSpec {


    @Value('${local.server.port}')
    private int port

    def setup() {
        browser.baseUrl = "http://localhost:${port}/"
    }

    def 'Submit a book and verify displayed' () {
        given:
            to ReadListPage
            assert at(ReadListPage)
            bookTitle.value('Total Garbage')
            author.value('Jim Bob')
            isbn.value('987654321')
            description.value('Do not buy this\nUnder any circumstances')
        when:
            submitButton.click()
        then:
            at(ReadListPage)
            listedBook(0).text() == 'Total Garbage by Jim Bob (ISBN: 987654321)'
    }
}

