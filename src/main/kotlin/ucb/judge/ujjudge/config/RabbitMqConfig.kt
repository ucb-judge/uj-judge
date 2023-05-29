package ucb.judge.ujjudge.config

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMqConfig {
    @Bean
    fun verdict2Exchange(): DirectExchange {
        return DirectExchange("verdict2Exchange")
    }

    @Bean
    fun verdict2Queue(): Queue {
        return QueueBuilder.durable("verdict2Queue").build()
    }

    @Bean
    fun verdict2Binding(verdict2Queue: Queue, verdict2Exchange: DirectExchange): Binding {
        return BindingBuilder
            .bind(verdict2Queue)
            .to(verdict2Exchange)
            .with("verdict2RoutingKey")
    }

    @Bean
    fun converter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun amqpTemplate(connectionFactory: ConnectionFactory): AmqpTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = converter()
        return rabbitTemplate
    }
}
