package com.js0507dev.kotlin_test.entity

import com.js0507dev.kotlin_test.dto.CreateMemberReqDTO
import jakarta.persistence.*

@Entity
@Table(name = "members", uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("email"))])
data class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val email: String,
    var name: String,
    var password: String,
) {
    constructor(createDTO: CreateMemberReqDTO) : this(null, createDTO.email, createDTO.name, createDTO.password)
    constructor(copy: Member) : this(copy.id, copy.email, copy.name, copy.password)
}
