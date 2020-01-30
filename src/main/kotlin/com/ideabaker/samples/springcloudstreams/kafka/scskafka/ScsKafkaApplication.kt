package com.ideabaker.samples.springcloudstreams.kafka.scskafka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.util.function.Function

@SpringBootApplication
class ScsKafkaApplication {
	@Bean
	fun toUpperCase(): Function<String, String> = Function { it.toUpperCase() }
}

fun main(args: Array<String>) {
	runApplication<ScsKafkaApplication>(*args)
}
