package com.example.JustGetStartedBackEnd.Member.Repository;

import com.example.JustGetStartedBackEnd.Domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email); //중복 가입 확인
}
