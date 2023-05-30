package ucb.judge.ujjudge.strategy

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ucb.judge.ujjudge.dto.CodeRunnerResDto
import ucb.judge.ujjudge.service.CodeRunnerService
import ucb.judge.ujjudge.utils.FileUtils

@Component
class CppStrategy constructor(
    private val codeRunnerService: CodeRunnerService
): JudgeStrategy {
    @Value("\${aws.code-runner.key}")
    private lateinit var apiKey: String

    override fun run(sourceCode: String, input: String, timeLimit: Double, memoryLimit: Int): CodeRunnerResDto {
        val sourceCodeFile = FileUtils.createFileWithExtension(sourceCode, "code", "cpp")
        val inputFile = FileUtils.createFileWithExtension(input, "input", "txt")
        return codeRunnerService.runCpp(sourceCodeFile, inputFile, timeLimit, memoryLimit, apiKey).data
            ?: throw Exception("Error running code")
    }
}
