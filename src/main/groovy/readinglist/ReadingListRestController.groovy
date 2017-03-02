package readinglist

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping('/api')
class ReadingListRestController {

    Logger logger = LoggerFactory.getLogger(ReadingListController)

    String reader = "Craig"

    @Autowired
    ReadingListRepository readingListRepository

    @RequestMapping(method=RequestMethod.GET)
    List<Book> getReadingList() {
        readingListRepository.findByReader(reader)
    }

    @RequestMapping(method=RequestMethod.POST)
    void addToReadingLost(@RequestBody Book book) {
        logger.info 'Received book {}', book
        book.reader = reader
        readingListRepository.save(book)
    }
}
