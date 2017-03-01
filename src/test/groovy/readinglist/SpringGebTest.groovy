package readinglist

import geb.Browser
import geb.Page
import geb.spock.GebSpec
import org.junit.Test
import org.junit.runner.RunWith
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

class ReadListPage extends Page {
    static at = { $('h2').text() == 'Your Reading List' }

    static url = '/'

    static content = {
        submitButton(to: ReadListPage) { $('input', type: 'submit') }
        bookTitle { $('input', type: 'text', name: 'title') }
        author { $('input', type: 'text', name: 'author') }
        isbn { $('input', type: 'text', name: 'isbn') }
        description { $('textarea', name: 'description') }
    }
}

@RunWith(SpringRunner)
@SpringBootTest(classes=ReadingListApplication,
        properties = ['spring.datasource.platform=h2'],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringGebTest {


    @Value('${local.server.port}')
    private int port

    @Test
    void testStartApplication() {
        Browser.drive(driver: new HtmlUnitDriver(), baseUrl: "http://localhost:${port}/") {
            to ReadListPage
            assert at(ReadListPage)
            bookTitle.value('Total Garbage')
            author.value('Jim Bob')
            isbn.value('987654321')
            description.value('Do not buy this\nUnder any circumstances')
            submitButton.click()
            assert at(ReadListPage)
            assert $('div').$('dl').$('dt.bookHeadline').text() == 'Total Garbage by Jim Bob (ISBN: 987654321)'
        }
    }
}

