package com.yukinagato.api

import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.MalformedURLException
import java.net.URL

@RestController
class PageviewController {
    val urlCounter = HashMap<String, Long>()
    val hostCounter = HashMap<String, Long>()

    @GetMapping("/pageview")
    fun query(@RequestParam(value="url", required = false) paramUrl: String?, @RequestParam(value="basis", defaultValue = "url") basis: String, @RequestHeader headers: HttpHeaders): Pageview {
        if(basis!="url" && basis!="host") {
            throw ParamInvalidException("Parameter \"basis\" invalid. Must be either\"url\" or \"host\".")
        }
        var url = paramUrl
        if(url==null) {
            url = headers["referer"]?.get(0)
        }
        if(url==null) {
            throw ParamInvalidException("Parameter \"url\" missing. There must be either a \"url\" parameter in URL query part, or a \"Referer\" HTTP header.")
        }
        val urlObj: URL
        try {
            urlObj = URL(url)
        }
        catch (e: MalformedURLException) {
            throw ParamInvalidException("Parameter \"url\" invalid. Must be a valid URL string.")
        }
        return if(basis=="url") {
            val rst = urlCounter.getOrDefault(urlObj.toString(), 0)+1
            urlCounter[urlObj.toString()] = rst
            Pageview(basis, url, rst)
        }
        else {
            val host = String.format("%s://%s:%d", urlObj.protocol, urlObj.host, urlObj.port)
            val rst = hostCounter.getOrDefault(host, 0)+1
            hostCounter[host] = rst
            Pageview(basis, host, rst)
        }
    }
}

data class Pageview(val basis: String, val url: String, val count: Long)