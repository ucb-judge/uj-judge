package ucb.judge.ujjudge.utils

import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile

class FileUtils {
    companion object {
        @JvmStatic
        fun createFileWithExtension(text: String, filename: String, extension: String): MultipartFile {
            val bytes = text.toByteArray()
            return MockMultipartFile("${filename}.${extension}", "${filename}.${extension}", "text/plain", bytes)
        }
    }
}