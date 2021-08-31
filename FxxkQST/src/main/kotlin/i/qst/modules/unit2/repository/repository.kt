package i.qst.modules.unit2.repository

import i.qst.modules.unit2.models.po.Unit2DanZiEntity
import i.qst.modules.unit2.models.po.Unit2DeptEntity
import i.qst.modules.unit2.models.po.Unit2UserEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface Unit2UserEntityRepository : JpaRepository<Unit2UserEntity, Int> {
    fun findTopByUserName(userName: String): Optional<Unit2UserEntity>
    fun existsByUserName(userName: String): Boolean
}

@Repository
interface Unit2DeptEntityRepository : JpaRepository<Unit2DeptEntity, Int> {
    fun findTopByName(deptName: String): Optional<Unit2DeptEntity>
}

@Repository
interface Unit2DanZiEntityRepository : JpaRepository<Unit2DanZiEntity, Int> {
    fun findAllByLinkedDeptNameAndLinkedUserUserNameLike(
        id: String,
        userName: String,
        page: Pageable
    ): List<Unit2DanZiEntity>

    fun countAllByLinkedDeptNameAndLinkedUserUserNameLike(
        id: String,
        userName: String,
    ): Long
}
