package com.example.JustGetStartedBackEnd.API.Image.Repository;

import com.example.JustGetStartedBackEnd.API.Image.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("DELETE FROM Image i WHERE i.community.communityId = :communityId")
    void deleteByCommunityId(Long communityId);

    @Query("SELECT i FROM Image i WHERE i.community.communityId = :communityId")
    List<Image> findByCommunityId(Long communityId);

    @Query("SELECT i FROM Image i WHERE i.imageUrl = :imageUrl")
    Image findByImageUrl(String imageUrl);
}
