package i.qst

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class QstApplication

fun main(args: Array<String>) {
    runApplication<QstApplication>(*args)
}
