package com.yukinagato.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URL
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

data class DoYouLikeMe(val success: Boolean, val like: Long)

@RestController
class DoYouLikeMeController {
    @Autowired
    lateinit var repository: DoYouLikeMeRepository

    @GetMapping("/do-you-like-me")
    fun doYouLikeMe(@RequestParam(value="action") action: String, @RequestHeader headers: HttpHeaders): DoYouLikeMe {
        val urlObject = URL(headers["referer"]!![0])
        val hostname = urlObject.host
        val queryResult = repository.findById(hostname).orElse(DoYouLikeMeEntity(hostname))

        if(action=="get") {
            return DoYouLikeMe(true, queryResult.count)
        }
        else if(action=="add") {
            queryResult.count++
            repository.save(queryResult)
            return DoYouLikeMe(true, queryResult.count)
        }

        throw ParamInvalidException("Must have parameter \"action\", and be either \"get\" or \"add\".")
    }
}

@Entity
@Table(name="do_you_like_me")
data class DoYouLikeMeEntity(@Id val hostname: String="", var count: Long=0)

interface DoYouLikeMeRepository : JpaRepository<DoYouLikeMeEntity, String>