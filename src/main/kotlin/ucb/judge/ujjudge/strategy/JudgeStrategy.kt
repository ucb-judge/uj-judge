package ucb.judge.ujjudge.strategy

import ucb.judge.ujjudge.dto.CodeRunnerResDto

interface JudgeStrategy {
    fun run(sourceCode: String, input: String, timeLimit: Double, memoryLimit: Int): CodeRunnerResDto
}
