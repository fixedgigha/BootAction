package readinglist

import spock.lang.Specification

class TestSpec1  extends Specification {

    void "Testo numero uno" () {
        given:
            def x = 1
        when:
            def y = x + 5
        then:
            y == 6
    }
}
