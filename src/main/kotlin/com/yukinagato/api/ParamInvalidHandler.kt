package com.yukinagato.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@ControllerAdvice
@RestController
class ParamInvalidHandler {
    @ExceptionHandler(ParamInvalidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleParamInvalid(err: ParamInvalidException): ParamInvalidExceptionResponse {
        return ParamInvalidExceptionResponse(err.message)
    }
}