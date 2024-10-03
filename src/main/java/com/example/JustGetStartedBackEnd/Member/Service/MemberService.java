package com.example.JustGetStartedBackEnd.Member.Service;

import com.example.JustGetStartedBackEnd.Exception.BusinessLogicException;
import com.example.JustGetStartedBackEnd.Member.DTO.MemberDTO;
import com.example.JustGetStartedBackEnd.Member.DTO.MemberListDTO;
import com.example.JustGetStartedBackEnd.Member.Entity.Member;
import com.example.JustGetStartedBackEnd.Member.ExceptionType.MemberExceptionType;
import com.example.JustGetStartedBackEnd.Member.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberListDTO getMemberList(int page, int size, String keyword){
        Pageable pageable = PageRequest.of(page, size);
        Page<Member> memberPage;
        if (keyword == null || keyword.isEmpty()) {
            memberPage = memberRepository.findAll(pageable);
        } else {
            memberPage = memberRepository.findByNameAndEmail(keyword, pageable);
        }
        List<MemberDTO> memberDTOS = memberPage.getContent().stream()
                .map(Member::toMemberDTO)
                .collect(Collectors.toList());

        MemberListDTO memberListDTO = new MemberListDTO();
        memberListDTO.setMemberDTOList(memberDTOS);
        memberListDTO.setPageNo(memberPage.getNumber());
        memberListDTO.setPageSize(memberPage.getSize());
        memberListDTO.setTotalElements(memberPage.getTotalElements());
        memberListDTO.setTotalPages(memberPage.getTotalPages());
        memberListDTO.setLast(memberPage.isLast());

        return memberListDTO;
    }

    @Transactional(readOnly = true)
    public MemberDTO findById(Long id){
        Optional<Member> member = memberRepository.findById(id);
        if(member.isPresent()){
            return member.map(Member::toMemberDTO).orElse(null);
        } else{
            log.warn("Member Not Found : {}", id);
            throw new BusinessLogicException(MemberExceptionType.MEMBER_NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public Member findByIdReturnEntity(Long id){
        Optional<Member> member = memberRepository.findById(id);
        if(member.isPresent()){
            return member.get();
        } else{
            log.warn("Member Not Found : {}", id);
            throw new BusinessLogicException(MemberExceptionType.MEMBER_NOT_FOUND);
        }
    }
}
