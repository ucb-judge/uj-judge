package ucb.judge.ujjudge.dto

data class VerdictDto(
    var submissionId: Long = 0,
    var testcaseId: Long = 0,
    var verdictId: Long = 0,
    var executionTime: Long = 0,
    var executionMemory: Long = 0,
    var isLast: Boolean = false
)