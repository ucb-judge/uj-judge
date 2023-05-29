package ucb.judge.ujjudge.amqp.producer

import org.springframework.amqp.core.AmqpTemplate
import org.springframework.stereotype.Service
import ucb.judge.ujjudge.dto.VerdictDto

@Service
class VerdictProducer constructor(
    private val amqpTemplate: AmqpTemplate
) {
    fun sendVerdict(verdict: VerdictDto): Boolean {
        amqpTemplate.convertAndSend("verdict2Exchange", "verdict2RoutingKey", verdict)
        return true
    }
}