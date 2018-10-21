package com.yukinagato.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

data class DoYouLikeMe(val success: Boolean, val like: Long)

@RestController
class DoYouLikeMeController {
    var like = 0L
    @GetMapping("/do-you-like-me")
    fun doYouLikeMe(@RequestParam(value="action") action: String): DoYouLikeMe {
        if(action=="get") {
            return DoYouLikeMe(true, like)
        }
        else if(action=="add") {
            return DoYouLikeMe(true, ++like)
        }
        throw ParamInvalidException("Must have parameter \"action\", and be either \"get\" or \"add\".")
    }
}