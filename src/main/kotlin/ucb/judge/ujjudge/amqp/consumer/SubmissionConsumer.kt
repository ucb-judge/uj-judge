package ucb.judge.ujjudge.amqp.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import ucb.judge.ujjudge.amqp.producer.VerdictProducer
import ucb.judge.ujjudge.bl.KeycloakBl
import ucb.judge.ujjudge.dto.CodeRunnerResDto
import ucb.judge.ujjudge.dto.SubmissionInfoDto
import ucb.judge.ujjudge.dto.VerdictDto
import ucb.judge.ujjudge.service.CodeRunnerService
import ucb.judge.ujjudge.service.UjProblemsService
import ucb.judge.ujjudge.strategy.CppStrategy
import ucb.judge.ujjudge.strategy.JavaStrategy
import ucb.judge.ujjudge.strategy.JudgeStrategy
import ucb.judge.ujjudge.strategy.PythonStrategy

@Component
class SubmissionConsumer constructor(
    private val ujProblemsService: UjProblemsService,
    private val verdictProducer: VerdictProducer,
    private val keycloakBl: KeycloakBl,
    private var applicationContext: ApplicationContext
) {
    private var judgeStrategy: JudgeStrategy? = null;

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

        // get memory limit and time limit by problemId
        val token = "Bearer ${keycloakBl.getToken()}";
        logger.info("Retrieving memory limit and time limit for problem ${submissionInfoDto.problemId}")
        val limits = ujProblemsService.getLimits(submissionInfoDto.problemId, token).data
        // get testcases by problemId
        logger.info("Retrieving testcases for problem ${submissionInfoDto.problemId}")
        val testcases = ujProblemsService.getTestcases(submissionInfoDto.problemId, token).data
        logger.info("Testcases retrieved: ${testcases!!.size}")

        for ((i, testcase) in testcases.withIndex()) {
            var codeRunnerResDto: CodeRunnerResDto
            try {
                // send submission to judge
                judgeStrategy = when(submissionInfoDto.languageId) {
                    1L -> applicationContext.getBean(CppStrategy::class.java)
                    2L -> applicationContext.getBean(JavaStrategy::class.java)
                    3L -> applicationContext.getBean(PythonStrategy::class.java)
                    else -> null
                }
                if(judgeStrategy == null) {
                    logger.error("Language not supported")
                    throw Exception("Language not supported")
                }
                logger.info("Running testcase #${testcase.testcaseNumber}")
                codeRunnerResDto = judgeStrategy!!.run(sourceCode, testcase.input, limits!!.timeLimit, limits.memoryLimit)
            } catch (e: Exception) {
                e.printStackTrace()
                logger.error("Error running code")
                val verdictDto = VerdictDto()
                verdictDto.submissionId = submissionInfoDto.submissionId
                verdictDto.testcaseId = testcase.testcaseId
                verdictDto.verdictId = 4
                verdictDto.isLast = true
                logger.info("Sending verdict for testcase #${testcase.testcaseNumber}")
                verdictProducer.sendVerdict(verdictDto)
                return
            }
            // create verdictDto
            val verdictDto = VerdictDto()
            verdictDto.submissionId = submissionInfoDto.submissionId
            verdictDto.testcaseId = testcase.testcaseId
            verdictDto.output = codeRunnerResDto.output
            verdictDto.executionTime = codeRunnerResDto.executionTime
            verdictDto.executionMemory = codeRunnerResDto.memoryUsage.toLong()
            // compare output with expected output
            logger.info("Comparing output with expected output")
            if (codeRunnerResDto.output != testcase.expectedOutput) {
                logger.info("Verdict is: WA")
                verdictDto.verdictId = 2
                verdictDto.isLast = true
                logger.info("Sending verdict for testcase #${testcase.testcaseNumber}")
                verdictProducer.sendVerdict(verdictDto)
                return
            }

            logger.info("Verdict is: AC")
            verdictDto.verdictId = 1
            verdictDto.isLast = false
            if (i == testcases.size - 1) {
                verdictDto.isLast = true
            }

            // send verdict to verdict queue
            logger.info("Sending verdict for testcase #${testcase.testcaseNumber}")
            verdictProducer.sendVerdict(verdictDto)
        }
    }
}