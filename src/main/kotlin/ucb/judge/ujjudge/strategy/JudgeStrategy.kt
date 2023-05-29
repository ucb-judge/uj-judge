package ucb.judge.ujjudge.strategy

import org.springframework.web.multipart.MultipartFile

interface JudgeStrategy {
    fun getVerdictId(): Long

    fun convertToMultipartFile(sourceCode: String): MultipartFile
}