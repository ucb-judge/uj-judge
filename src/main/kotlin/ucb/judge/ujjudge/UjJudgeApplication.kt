package ucb.judge.ujjudge

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
class UjJudgeApplication

fun main(args: Array<String>) {
	runApplication<UjJudgeApplication>(*args)
}
