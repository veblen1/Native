package com.sunbell.nicepricemap.repository

import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException

abstract class BaseRepository {

    var lastError: String? = null

    protected fun handleNetworkException(e: Exception? = null, response: Response<*>? = null): String {
        return when {
            e is ConnectException || e is UnknownHostException -> "인터넷 연결 실패"
            response != null && !response.isSuccessful -> {
                when(response.code()) {
                    400 -> "잘못된 요청입니다."
                    401 -> "권한이 없습니다. 로그인 후 다시 시도해주세요."
                    403 -> "접근 권한이 제한되었습니다."
                    404 -> "요청하신 페이지를 찾을 수 없습니다."
                    405 -> "허용되지 않는 메서드입니다."
                    406 -> "허용되지 않는 요청입니다."
                    408 -> "요청 시간이 초과되었습니다."
                    413 -> "요청이 너무 큽니다."
                    415 -> "지원되지 않는 형식입니다."
                    429 -> "요청이 너무 많습니다. 잠시 후 다시 시도해주세요."
                    500 -> "서버 내부 오류가 발생했습니다."
                    502 -> "인터넷 연결이 원활하지 않습니다. 잠시 후 다시 시도해주세요."
                    503 -> "서비스를 사용할 수 없습니다. 잠시 후 다시 시도해주세요."
                    504 -> "인터넷 연결을 확인해주세요"
                    507 -> "저장 공간이 부족합니다."
                    else -> response.errorBody()?.string() ?: "알 수 없는 에러"
                }
            }
            else -> e?.localizedMessage ?: "알 수 없는 에러"
        }
    }
}
