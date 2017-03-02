package readinglist

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.Canonical

@Canonical
@JsonIgnoreProperties(ignoreUnknown = true)
class Book {
    Long id 
    String reader 
    String isbn 
    String title 
    String author 
    String description 
}
