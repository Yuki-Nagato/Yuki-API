package com.yukinagato.api

class ParamInvalidException(message: String?) : Throwable(message)

data class ParamInvalidExceptionResponse(val message: String?)