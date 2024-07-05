package com.js0507dev.kotlin_test.controller

import com.js0507dev.kotlin_test.dto.CreateMemberReqDTO
import com.js0507dev.kotlin_test.dto.MemberDTO
import com.js0507dev.kotlin_test.dto.UpdateMemberReqDTO
import com.js0507dev.kotlin_test.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/members")
class MemberController @Autowired constructor(val memberService: MemberService) {
    @GetMapping("")
    fun getMembers(): List<MemberDTO> = memberService.findAll().map { MemberDTO(it) }

    @GetMapping("/{id}")
    fun getMember(@PathVariable id: Long): MemberDTO = MemberDTO(memberService.getById(id))

    @PostMapping("")
    fun createMember(@RequestBody dto: CreateMemberReqDTO): MemberDTO = MemberDTO(memberService.createMember(dto))
    @PatchMapping("/{id}")
    fun updateMember(@PathVariable id: Long, @RequestBody dto: UpdateMemberReqDTO): MemberDTO = MemberDTO(
        memberService
            .updateMember(id, dto)
    )
    @DeleteMapping("/{id}")
    fun deleteMember(@PathVariable id: Long): Unit = memberService.deleteMember(id)
}