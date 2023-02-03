package com.flap.flap

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FlapApplication

fun main(args: Array<String>) {
	runApplication<FlapApplication>(*args)
}
