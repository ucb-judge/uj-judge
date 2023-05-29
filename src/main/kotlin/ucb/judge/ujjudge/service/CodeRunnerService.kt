package ucb.judge.ujjudge.service

import org.springframework.cloud.openfeign.FeignClient

@FeignClient(name = "aws-code-runner", url = "https://tib7qgxq19.execute-api.us-west-1.amazonaws.com/dev/code-runner")
interface CodeRunnerService {
}