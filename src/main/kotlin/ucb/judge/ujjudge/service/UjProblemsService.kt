package ucb.judge.ujjudge.service

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import ucb.judge.ujjudge.dto.ProblemLimitsDto
import ucb.judge.ujjudge.dto.ResponseDto
import ucb.judge.ujjudge.dto.TestcaseDto

@FeignClient(name = "uj-problems")
interface UjProblemsService {

    @GetMapping("/api/v1/problems/{problemId}/testcases")
    fun getTestcases(@PathVariable problemId: Long, @RequestHeader("Authorization") token: String): ResponseDto<List<TestcaseDto>>

    @GetMapping("/api/v1/problems/{problemId}/limits")
    fun getLimits(@PathVariable problemId: Long, @RequestHeader("Authorization") token: String): ResponseDto<ProblemLimitsDto>

}