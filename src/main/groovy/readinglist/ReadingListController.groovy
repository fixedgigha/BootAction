package readinglist 
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod


@Controller
@RequestMapping("/")
class ReadingListController {

  String reader = "Craig"

  @Autowired
  ReadingListRepository readingListRepository

  @RequestMapping(method=RequestMethod.GET)
  def readersBooks(Model model) {
    List<Book> readingList = 
        readingListRepository.findByReader(reader)

    if (readingList != null) {
      model.addAttribute("books", readingList)
    }

    "readingList"
  }

  @RequestMapping(method=RequestMethod.POST)
  def addToReadingList(Book book) {
    book.setReader(reader)
    readingListRepository.save(book)
    
    "redirect:/"
  }

}
