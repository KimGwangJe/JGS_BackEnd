package com.example.JustGetStartedBackEnd.API.Community.Repository;

import com.example.JustGetStartedBackEnd.API.Community.Entity.Community;
import com.example.JustGetStartedBackEnd.API.Community.Repository.QueryDSL.CommunityQueryDSL;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long>, CommunityQueryDSL {
}
