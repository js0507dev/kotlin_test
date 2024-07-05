package com.js0507dev.kotlin_test.service

import com.js0507dev.kotlin_test.dto.CreateMemberReqDTO
import com.js0507dev.kotlin_test.dto.UpdateMemberReqDTO
import com.js0507dev.kotlin_test.entity.Member
import com.js0507dev.kotlin_test.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService @Autowired constructor(
    val memberRepository: MemberRepository,
    val passwordEncoder: PasswordEncoder,
) {
    @Transactional(readOnly = true)
    fun findAll(): List<Member> = memberRepository.findAll().toList()
    @Transactional(readOnly = true)
    fun findById(id: Long): Member? = memberRepository.findById(id).orElse(null)
    @Transactional(readOnly = true)
    fun getById(id: Long): Member = this.findById(id)?: throw NoSuchElementException("Member with id $id does not exist")
    @Transactional
    fun createMember(dto: CreateMemberReqDTO): Member {
        val member = Member(dto)
        member.password = passwordEncoder.encode(dto.password)
        return memberRepository.save(member)
    }
    @Transactional
    fun updateMember(id: Long, dto: UpdateMemberReqDTO): Member {
        val member = getById(id)
        member.name = dto.name ?: member.name
        member.password = passwordEncoder.encode(dto.password) ?: member.name
        return memberRepository.save(member)
    }
    @Transactional
    fun deleteMember(id: Long): Unit {
        val _member = getById(id)
        memberRepository.deleteById(id)
        return
    }
}