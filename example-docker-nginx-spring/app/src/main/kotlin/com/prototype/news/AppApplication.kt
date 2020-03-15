package com.prototype.news

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AppApplication

@RestController
class HelloController {
	@RequestMapping("/")
	fun index(): String {
		return "Greetings from Spring Boot!"
	}
}


fun main(args: Array<String>) {
	runApplication<AppApplication>(*args)
}
