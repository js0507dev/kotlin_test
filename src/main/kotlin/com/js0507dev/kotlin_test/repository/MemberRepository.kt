package com.js0507dev.kotlin_test.repository

import com.js0507dev.kotlin_test.entity.Member
import org.springframework.data.repository.CrudRepository

interface MemberRepository: CrudRepository<Member, Long> {
}