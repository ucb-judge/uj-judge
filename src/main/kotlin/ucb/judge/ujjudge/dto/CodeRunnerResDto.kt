package ucb.judge.ujjudge.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CodeRunnerResDto(
    @JsonProperty("output")
    var output: String = "",
    @JsonProperty("memory_usage")
    var memoryUsage: Double = 0.0,
    @JsonProperty("execution_time")
    var executionTime: Double = 0.0,
)
