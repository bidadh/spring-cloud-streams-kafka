package com.ideabaker.samples.springcloudstreams.kafka.scskafka

import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.TimeWindows
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.time.Duration
import java.util.*
import java.util.function.Function

@SpringBootApplication
class ScsKafkaApplication {
  @Bean
  fun toUpperCase(): Function<KStream<Any, String>, KStream<Any, String>> = Function {
        it.mapValues { value -> value.toUpperCase() }
  }

  @Bean
  fun process(): Function<KStream<Any, String>, KStream<Any?, WordCount>> {
    return Function {
      it
          .flatMapValues { value -> value.toLowerCase().split("\\W+") }
          .map{ _, value -> KeyValue(value, value) }
          .groupByKey()
          .windowedBy(TimeWindows.of(Duration.ofSeconds(WINDOW_SIZE_SECONDS)))
          .count()
          .toStream()
          .map { key, value ->
            val wordCount = WordCount(key.key() as String,
                value,
                Date(key.window().start()),
                Date(key.window().end()))
            KeyValue(null, wordCount)
          }
    }
  }

  companion object {
    const val WINDOW_SIZE_SECONDS = 30L
  }
}

data class WordCount(val key: String, val value: Long, val start: Date, val end: Date)

fun main(args: Array<String>) {
  runApplication<ScsKafkaApplication>(*args)
}
