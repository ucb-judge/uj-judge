package ucb.judge.ujjudge.amqp.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ucb.judge.ujjudge.amqp.producer.VerdictProducer
import ucb.judge.ujjudge.bl.KeycloakBl
import ucb.judge.ujjudge.dto.SubmissionInfoDto
import ucb.judge.ujjudge.dto.VerdictDto
import ucb.judge.ujjudge.service.UjProblemsService

@Component
class SubmissionConsumer constructor(
    private val ujProblemsService: UjProblemsService,
    private val verdictProducer: VerdictProducer,
    private val keycloakBl: KeycloakBl
) {
    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(SubmissionConsumer::class.java)
    }

    @RabbitListener(queues = ["submission2Queue"])
    fun receiveSubmission(message: Message) {
        logger.info("Received submission with message id ${message.messageProperties.messageId}")
        val sourceCode = String(message.body)
        val submissionInfo = message.messageProperties.headers["submission"]
        val objectMapper = ObjectMapper()
        val submissionInfoDto =  objectMapper.readValue(submissionInfo.toString(), SubmissionInfoDto::class.java)
        logger.info("Submission id: ${submissionInfoDto.submissionId}")
        // get testcases by problemId
        logger.info("Retrieving testcases for problem ${submissionInfoDto.problemId}")
        val token = "Bearer ${keycloakBl.getToken()}";
        val testcases = ujProblemsService.getTestcases(submissionInfoDto.problemId, token).data
        logger.info("Testcases retrieved: ${testcases!!.size}")
        for ((i, testcase) in testcases.withIndex()) {
            // send submission to judge
            // judgeService.sendSubmission(sourceCode, submissionInfoDto, testcase)
            // compare output

            val verdictDto = VerdictDto()
            verdictDto.submissionId = submissionInfoDto.submissionId
            verdictDto.testcaseId = testcase.testcaseId
            verdictDto.verdictId = 1
            verdictDto.executionTime = 0
            verdictDto.executionMemory = 0
            if (i == testcases.size - 1) {
                verdictDto.isLast = true
            }
            // send verdict to verdict queue
            logger.info("Sending verdict for testcase #${testcase.testcaseNumber}")
            verdictProducer.sendVerdict(verdictDto)
        }
    }
}