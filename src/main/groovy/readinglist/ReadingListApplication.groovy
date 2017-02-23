package readinglist

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@SpringBootApplication
class ReadingListApplication extends WebMvcConfigurerAdapter {

    static void main(String[] args) {
        SpringApplication.run(ReadingListApplication.class, args)
    }
    
    @Override
    void addViewControllers(ViewControllerRegistry registry) {
      registry.addRedirectViewController('/', '/readingList')
    }
    
}
