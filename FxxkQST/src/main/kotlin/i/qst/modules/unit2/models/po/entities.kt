package i.qst.modules.unit2.models.po

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Table
@Entity(name = "t_unit2_user")
class Unit2UserEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val userId: Int = 0,
    var userName: String,
    var passowrd: String,
    var photo: String,

) {
    @ManyToMany
    val depts: MutableSet<Unit2DeptEntity> = mutableSetOf()

    @OneToMany
    val danZis: MutableSet<Unit2DanZiEntity> = mutableSetOf()
}

@Table
@Entity(name = "t_unit2_dept")
class Unit2DeptEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val deptId: Int = 0,
    var name: String,
) {
    @ManyToMany
    val linkedUser: MutableSet<Unit2UserEntity> = mutableSetOf()

    @OneToMany
    val danZis: MutableSet<Unit2DanZiEntity> = mutableSetOf()
}

@Table
@Entity(name = "t_unit2_dan_zi")
class Unit2DanZiEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val danId: Int = 0,
    var content: String,
    var bxDate: LocalDateTime,
    var money: Double

) {
    @ManyToOne
    lateinit var linkedUser: Unit2UserEntity

    @ManyToOne
    lateinit var linkedDept: Unit2DeptEntity
}
