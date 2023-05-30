package ucb.judge.ujjudge.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ResponseDto<T>(
    val data: T? = null,
    val message: String = "",
    val successful: Boolean = false
);
