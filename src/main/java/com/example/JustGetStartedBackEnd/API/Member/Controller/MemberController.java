package com.example.JustGetStartedBackEnd.API.Member.Controller;

import com.example.JustGetStartedBackEnd.API.Member.DTO.MemberDTO;
import com.example.JustGetStartedBackEnd.API.Member.DTO.MemberListDTO;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final int SIZE = 30;

    @GetMapping
    public ResponseEntity<MemberListDTO> getMemberList(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(required = false) String keyword){
        MemberListDTO memberListDTO = memberService.getMemberList(page, SIZE, keyword);
        return ResponseEntity.ok(memberListDTO);
    }

    @GetMapping("/info")
    public ResponseEntity<MemberDTO> getMember(@RequestParam("memberId") Long memberId){
        return ResponseEntity.status(HttpStatus.OK).body(memberService.findById(memberId));
    }
}
