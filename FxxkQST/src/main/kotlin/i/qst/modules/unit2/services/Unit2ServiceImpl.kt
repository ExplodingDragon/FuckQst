package i.qst.modules.unit2.services

import i.qst.modules.unit2.models.dto.Unit2LoginDto
import i.qst.modules.unit2.models.dto.Unit2RegisterDto
import i.qst.modules.unit2.models.dto.Unit2StatusDto
import i.qst.modules.unit2.models.dto.Unit2TableDto
import i.qst.modules.unit2.models.dto.Unit2TablesDto
import i.qst.modules.unit2.models.dto.Unit2UserDto
import i.qst.modules.unit2.models.po.Unit2DanZiEntity
import i.qst.modules.unit2.models.po.Unit2UserEntity
import i.qst.modules.unit2.repository.Unit2DanZiEntityRepository
import i.qst.modules.unit2.repository.Unit2DeptEntityRepository
import i.qst.modules.unit2.repository.Unit2UserEntityRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Base64

@Service
class Unit2ServiceImpl(
    private val unit2UserEntityRepository: Unit2UserEntityRepository,
    private val unit2DanZiEntityRepository: Unit2DanZiEntityRepository,
    private val unit2DeptEntityRepository: Unit2DeptEntityRepository
) : Unit2Service {
    val format: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    override fun login(user: Unit2LoginDto): Unit2StatusDto {

        val userData = unit2UserEntityRepository.findTopByUserName(user.userName)
        return if (userData.isEmpty || userData.get().passowrd != user.password) {
            Unit2StatusDto(false, "用户名或密码错误.")
        } else {
            Unit2StatusDto(true, Base64.getEncoder().encodeToString("<id>${userData.get().userId}</id>".toByteArray()))
        }
    }

    override fun getAllDepartment(): List<String> {
        return unit2DeptEntityRepository.findAll().map { it.name }
    }

    override fun register(user: Unit2RegisterDto): Unit2StatusDto {
        if (unit2UserEntityRepository.existsByUserName(user.userName)) {
            return Unit2StatusDto(false, "此用户已被注册。")
        }

        val dept = unit2DeptEntityRepository.findTopByName(user.deptName)
        if (dept.isEmpty) {
            return Unit2StatusDto(false, "没有此部门。")
        }
        val unit2UserEntity = Unit2UserEntity(
            userName = user.userName,
            passowrd = user.password,
            photo = user.urlId
        )
        unit2UserEntity.depts.add(dept.get())
        unit2UserEntityRepository.save(unit2UserEntity)
        return Unit2StatusDto(true, "注册成功！")
    }

    override fun updateTable(token: String, data: Unit2TableDto, create: Boolean): Unit2StatusDto {
        val userIdByToken = getUserIdByToken(token)
        if (create) {
            val unit2DanZiEntity = Unit2DanZiEntity(
                content = data.content,
                bxDate = LocalDate.parse(data.dateTime, format).atStartOfDay(),
                money = data.money
            )
            unit2DanZiEntity.run {
                linkedUser = unit2UserEntityRepository.getById(userIdByToken)
                linkedDept = unit2DeptEntityRepository.findTopByName(data.groupName).get()
            }
            unit2DanZiEntityRepository.save(
                unit2DanZiEntity
            )
        } else {
            val findById = unit2DanZiEntityRepository.findById(data.id)
            if (findById.isEmpty) {
                return Unit2StatusDto(false, "未找到！")
            }
            val get = findById.get()
            if (userIdByToken != get.linkedUser.userId) {
                return Unit2StatusDto(false, "非本人，无权限！")
            }
            get.run {
                this.content = data.content
                this.bxDate = LocalDate.parse(data.dateTime, format).atStartOfDay()
                this.linkedUser = unit2UserEntityRepository.getById(userIdByToken)
                this.linkedDept = unit2DeptEntityRepository.findTopByName(data.groupName).get()
                this.money = data.money
            }
            unit2DanZiEntityRepository.save(get)
        }
        return Unit2StatusDto(true, "成功！")
    }

    override fun getAllTables(token: String, of: PageRequest): Unit2TablesDto {
        val map = unit2DanZiEntityRepository.findAll(of).map {
            Unit2TableDto(
                id = it.danId,
                userName = it.linkedUser.userName,
                content = it.content, groupName = it.linkedDept.name,
                dateTime = it.bxDate.format(format), money = it.money,
                canEdit = getUserIdByToken(token) == it.linkedUser.userId
            )
        }
        return Unit2TablesDto(map.content, of.pageNumber, unit2DanZiEntityRepository.count())
    }

    override fun getUserInfo(token: String): Unit2UserDto {
        val userIdByToken = getUserIdByToken(token)
        return unit2UserEntityRepository.findById(userIdByToken).get().let {
            Unit2UserDto(it.userName, it.photo)
        }
    }

    override fun searchTable(token: String, name: String, dept: String, pages: PageRequest): Unit2TablesDto {
        val map = unit2DanZiEntityRepository.findAllByLinkedDeptNameAndLinkedUserUserNameLike(dept, "%$name%", pages).map {
            Unit2TableDto(
                id = it.danId,
                userName = it.linkedUser.userName,
                content = it.content, groupName = it.linkedDept.name,
                dateTime = it.bxDate.format(format), money = it.money,
                canEdit = getUserIdByToken(token) == it.linkedUser.userId
            )
        }
        val size = unit2DanZiEntityRepository
            .countAllByLinkedDeptNameAndLinkedUserUserNameLike(dept, "%$name%")
        return Unit2TablesDto(map, pages.pageNumber, size)
    }

    override fun getTableById(token: String, id: Int): Unit2TableDto {
        return unit2DanZiEntityRepository.findById(id).get().let {
            Unit2TableDto(
                id = it.danId,
                userName = it.linkedUser.userName,
                content = it.content, groupName = it.linkedDept.name,
                dateTime = it.bxDate.format(format), money = it.money,
                canEdit = getUserIdByToken(token) == it.linkedUser.userId
            )
        }
    }

    private fun getUserIdByToken(token: String): Int {
        return Base64.getDecoder().decode(token).toString(Charset.defaultCharset())
            .replace("<id>", "")
            .replace("</id>", "")
            .toInt()
    }

    override fun deleteTable(token: String, id: Int): Unit2StatusDto {
        val userIdByToken = getUserIdByToken(token)
        val findById = unit2DanZiEntityRepository.findById(id)
        if (findById.isEmpty) {
            return Unit2StatusDto(false, "未找到！")
        }
        val get = findById.get()
        if (userIdByToken != get.linkedUser.userId) {
            return Unit2StatusDto(false, "非本人，无权限！")
        }
        unit2DanZiEntityRepository.delete(get)
        return Unit2StatusDto(true, "删除成功！")
    }
}
