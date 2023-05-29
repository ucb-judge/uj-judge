package ucb.judge.ujjudge.dto

data class ResponseDto<T>(
    val data: T? = null,
    val message: String = "",
    val successful: Boolean = false
);
