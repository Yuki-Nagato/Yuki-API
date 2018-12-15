package com.yukinagato.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

data class UploadResponse(val success: Boolean, val id: UUID)

@RestController
class UploadController {
    @Autowired
    lateinit var repository: UploadRepository

    @PostMapping(value = "/upload")
    fun uploadReceived(@RequestParam("file")file: MultipartFile): UploadResponse {
        val toInsert = UploadEntity(UUID.randomUUID(), file.originalFilename, file.bytes, file.contentType, Timestamp(System.currentTimeMillis()))
        repository.save(toInsert)
        return UploadResponse(true, toInsert.id!!)
    }
}

@Entity
@Table(name="upload")
data class UploadEntity(@Id val id: UUID? =null, val name: String?=null, val file: ByteArray? =null, val content_type: String?=null, val upload_time: Timestamp? =null)

interface UploadRepository : JpaRepository<UploadEntity, UUID>

@Controller
class UploadDownloadController {
    @Autowired
    lateinit var repository: UploadRepository

    @GetMapping("/upload/download", params = ["id"])
    fun uploadDownload(request: HttpServletRequest, response: HttpServletResponse) {
        val id = request.getParameter("id")
        val queryResult = repository.findById(UUID.fromString(id))
        response.contentType = queryResult.get().content_type
        response.setHeader("Content-Disposition", "attachment; filename=${queryResult.get().name}")
        response.outputStream.write(queryResult.get().file)
    }

    @GetMapping("/upload/download")
    fun uploadDownloadList(request: HttpServletRequest, response: HttpServletResponse) {
        response.contentType = "text/html; charset=utf-8"
        val queryResult = repository.findAll()
        val writer = response.outputStream.bufferedWriter(Charsets.UTF_8)
        writer.write("<!DOCTYPE html>\n")
        writer.write("<html><head></head><body><ul>")
        for(entity in queryResult) {
            writer.write("<li><a href=\"download?id=${entity.id}\">${entity.name}</a></li>")
        }
        writer.write("</ul></body></html>")
        writer.flush()
    }
}