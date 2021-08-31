package i.qst.modules.unit2.models.dto

data class Unit2LoginDto(
    val userName: String,
    val password: String
)

data class Unit2StatusDto(
    val status: Boolean,
    val message: String
)

data class Unit2RegisterDto(
    val userName: String,
    val password: String,
    val deptName: String,
    val urlId: String
)

data class Unit2TableDto(
    var id: Int = 0,
    val userName: String,
    val content: String,
    val groupName: String,
    val dateTime: String,
    val money: Double,
    val canEdit: Boolean = false
)

data class Unit2TablesDto(
    val items: List<Unit2TableDto>,
    val index: Int,
    val size: Long
)

data class Unit2UserDto(
    val userName: String,
    val imageId: String
)
