package readinglist

import org.springframework.test.web.servlet.MockMvc
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*
import spock.lang.Specification
import static org.mockito.Mockito.*

class BootSpockSpec extends Specification {

    MockMvc mockMvc
    List<Book> expectedBooks

    def setup() {
        expectedBooks = [
            new Book(
                    id: 1,
                    reader: 'Craig',
                    isbn: '12345678',
                    title: 'Total Junk',
                    author: 'Jim',
                    description: 'Hideous rubbish'
            )
        ]
        def mockRepo = mock(ReadingListRepository)
        when(mockRepo.findByReader('Craig')).thenReturn(expectedBooks)
        def controller = new ReadingListController(readingListRepository: mockRepo)
        mockMvc = standaloneSetup(controller).build()
    }

    def "Should get list of books when requesed"() {
        when:
            def response = mockMvc.perform(get('/'))
        then:
            response.andExpect(view().name('readingList')).andExpect(model().attribute('books', expectedBooks))
    }

}
