package com.example.JustGetStartedBackEnd.API.Member.Repository;

import com.example.JustGetStartedBackEnd.API.Member.Entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email); //중복 가입 확인

    @Query("SELECT m FROM Member m WHERE m.name LIKE %:keyword% OR m.email LIKE %:keyword%")
    Page<Member> findByNameAndEmail(@Param("keyword") String keyword, Pageable pageable);
}
