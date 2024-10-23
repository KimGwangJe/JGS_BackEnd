package com.example.JustGetStartedBackEnd.API.MatchPost.Repository;

import com.example.JustGetStartedBackEnd.API.MatchPost.Entity.MatchPost;
import com.example.JustGetStartedBackEnd.API.MatchPost.Repository.QueryDSL.MatchPostQueryDSL;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchPostRepository extends JpaRepository<MatchPost, Long>, MatchPostQueryDSL {
}
