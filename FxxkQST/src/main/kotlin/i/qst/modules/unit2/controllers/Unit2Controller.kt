package i.qst.modules.unit2.controllers

import i.qst.modules.unit2.models.dto.Unit2LoginDto
import i.qst.modules.unit2.models.dto.Unit2RegisterDto
import i.qst.modules.unit2.models.dto.Unit2TableDto
import i.qst.modules.unit2.services.Unit2Service
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.util.UUID
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/unit2")
class Unit2Controller {
    @Resource
    private lateinit var u2: Unit2Service

    @PostMapping("/login")
    fun login(@RequestBody user: Unit2LoginDto) = u2.login(user)

    @PostMapping("/register")
    fun register(@RequestBody user: Unit2RegisterDto) = u2.register(user)

    @GetMapping("/department")
    fun department() = u2.getAllDepartment()

    @GetMapping("/{token}/tables/page/{index}")
    fun getTable(
        @PathVariable token: String,
        @PathVariable("index") index: Int,
    ) = u2.getAllTables(token, PageRequest.of(index, 10))

    @PostMapping("/{token}/tables")
    fun addTable(@PathVariable token: String, @RequestBody data: Unit2TableDto) = kotlin.run {
        data.id = -1
        u2.updateTable(token, data, true)
    }

    @GetMapping("/{token}/tables/{id}")
    fun getTableById(
        @PathVariable("token") token: String,
        @PathVariable("id") id: Int
    ) = u2.getTableById(token, id)

    @PostMapping("/{token}/tables/{id}")
    fun updateTable(
        @PathVariable("token") token: String,
        @PathVariable("id") id: Int,
        @RequestBody data: Unit2TableDto
    ) = kotlin.run {
        data.id = id
        u2.updateTable(token, data, false)
    }

    @GetMapping("/{token}/user")
    fun getUserInfo(@PathVariable("token") token: String) = u2.getUserInfo(token)

    @GetMapping("/{token}/tables/search/{name}/{dept}/{index}")
    fun searchTable(
        @PathVariable("index") index: Int,
        @PathVariable("token") token: String,
        @PathVariable("name") name: String,
        @PathVariable("dept") dept: String,
        request: HttpServletRequest
    ) = kotlin.run {
        u2.searchTable(token, name, dept, PageRequest.of(index, 10))
    }

    @GetMapping("/{token}/tables/{id}/delete")
    fun deleteTable(
        @PathVariable("token") token: String,
        @PathVariable("id") id: Int
    ) = u2.deleteTable(token, id)

    private val tempDir = File(System.getProperty("java.io.tmpdir"), "fxxk")

    @PostMapping("/upload")
    fun update(@RequestPart file: MultipartFile) = kotlin.run {
        tempDir.mkdir()
        val id = UUID.randomUUID().toString().replace("-", "")
        Files.copy(file.inputStream, File(tempDir, id).toPath())
        mapOf("id" to id)
    }

    @GetMapping("/static/{id}")
    fun download(@PathVariable id: String, response: HttpServletResponse) {
        val file = File(tempDir, id)
        if (file.isFile.not()) {
            response.status = 404
        } else {
            response.contentType = "image/jpg"
            file.inputStream().let { it1 ->
                response.outputStream.let { it2 ->
                    it1.transferTo(it2)
                }
            }
        }
    }
}
