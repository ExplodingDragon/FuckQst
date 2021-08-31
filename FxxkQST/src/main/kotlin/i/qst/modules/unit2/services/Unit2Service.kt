package i.qst.modules.unit2.services

import i.qst.modules.unit2.models.dto.Unit2LoginDto
import i.qst.modules.unit2.models.dto.Unit2RegisterDto
import i.qst.modules.unit2.models.dto.Unit2StatusDto
import i.qst.modules.unit2.models.dto.Unit2TableDto
import i.qst.modules.unit2.models.dto.Unit2TablesDto
import i.qst.modules.unit2.models.dto.Unit2UserDto
import org.springframework.data.domain.PageRequest

interface Unit2Service {
    fun login(user: Unit2LoginDto): Unit2StatusDto
    fun register(user: Unit2RegisterDto): Unit2StatusDto
    fun getAllDepartment(): List<String>
    fun getAllTables(token: String, of: PageRequest): Unit2TablesDto
    fun updateTable(token: String, data: Unit2TableDto, create: Boolean): Unit2StatusDto
    fun deleteTable(token: String, id: Int): Unit2StatusDto
    fun getTableById(token: String, id: Int): Unit2TableDto
    fun getUserInfo(token: String): Unit2UserDto
    fun searchTable(token: String, name: String, dept: String, pages: PageRequest): Unit2TablesDto
}
