package ucb.judge.ujjudge.service

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile
import ucb.judge.ujjudge.dto.CodeRunnerResDto
import ucb.judge.ujjudge.dto.ResponseDto

@FeignClient(name = "aws-code-runner", url = "\${aws.code-runner.url}")
interface CodeRunnerService {

    @PostMapping("/cpp", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun runCpp(
        @RequestPart("sourceCode") sourceCode: MultipartFile,
        @RequestPart("input") input: MultipartFile,
        @RequestPart("timeLimit") timeLimit: Double,
        @RequestPart("memoryLimit") memoryLimit: Int,
        @RequestHeader("x-api-key") apiKey: String
    ): ResponseDto<CodeRunnerResDto>

    @PostMapping("/java", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun runJava(
        @RequestPart("sourceCode") sourceCode: MultipartFile,
        @RequestPart("input") input: MultipartFile,
        @RequestPart("timeLimit") timeLimit: Double,
        @RequestPart("memoryLimit") memoryLimit: Int,
        @RequestHeader("x-api-key") apiKey: String
    ): ResponseDto<CodeRunnerResDto>

    @PostMapping("/python", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun runPython(
        @RequestPart("sourceCode") sourceCode: MultipartFile,
        @RequestPart("input") input: MultipartFile,
        @RequestPart("timeLimit") timeLimit: Double,
        @RequestPart("memoryLimit") memoryLimit: Int,
        @RequestHeader("x-api-key") apiKey: String
    ): ResponseDto<CodeRunnerResDto>
}