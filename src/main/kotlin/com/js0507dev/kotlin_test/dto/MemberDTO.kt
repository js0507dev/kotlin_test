package com.js0507dev.kotlin_test.dto

import com.js0507dev.kotlin_test.entity.Member

data class MemberDTO(
    val id: Long,
    val email: String,
    val name: String,
) {
    constructor(entity: Member) : this(entity.id!!, entity.email, entity.name)
}
