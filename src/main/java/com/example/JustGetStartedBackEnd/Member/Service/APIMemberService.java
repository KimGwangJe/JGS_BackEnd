package com.example.JustGetStartedBackEnd.Member.Service;

import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.Member.DTO.UpdateMemberDTO;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.Member.ExceptionType.MemberExceptionType;
import com.example.JustGetStartedBackEnd.Member.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class APIMemberService {
    private final MemberRepository memberRepository;

    @Transactional(rollbackFor = Exception.class)
    public void updateMember(Long memberId, UpdateMemberDTO updateMemberDTO){
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new BusinessLogicException(MemberExceptionType.MEMBER_NOT_FOUND)
        );
        member.update(updateMemberDTO.getName(), updateMemberDTO.getProfileImage(), updateMemberDTO.getIntroduce());
    }
}
