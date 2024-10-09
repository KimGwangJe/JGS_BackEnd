package com.example.JustGetStartedBackEnd.API.Member.Controller;

import com.example.JustGetStartedBackEnd.API.Member.DTO.MemberDTO;
import com.example.JustGetStartedBackEnd.API.Member.DTO.MemberListDTO;
import com.example.JustGetStartedBackEnd.API.Member.Service.MemberService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Validated
public class MemberController {
    private final MemberService memberService;
    private final int SIZE = 30;

    @GetMapping
    public ResponseEntity<MemberListDTO> getMemberList(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(required = false) String keyword){
        MemberListDTO memberListDTO = memberService.getMemberList(page, SIZE, keyword);
        return ResponseEntity.ok(memberListDTO);
    }

    @GetMapping("/info/{memberId}")
    public ResponseEntity<MemberDTO> getMember(
            @NotNull @Min(value=1, message="Member ID는 1 이상이어야 됩니다.")
            @PathVariable("memberId") Long memberId){
        return ResponseEntity.status(HttpStatus.OK).body(memberService.findById(memberId));
    }
}
