package com.example.JustGetStartedBackEnd.API.TeamReview.Repository;

import com.example.JustGetStartedBackEnd.API.TeamReview.Entity.TeamReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TeamReviewRepository extends JpaRepository<TeamReview, Long> {
}
