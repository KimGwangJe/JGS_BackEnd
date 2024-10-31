package com.example.JustGetStartedBackEnd.API.Image.Repository.QueryDSL;

import com.example.JustGetStartedBackEnd.API.Image.Entity.Image;

import java.util.List;

public interface ImageQueryDSL {

    List<Image> findByCommunityId(Long communityId);

    List<String> findByCommunityIsNull();

    Image findByImageUrl(String imageUrl);

    void deleteImagesWhereCommunityIdIsNull();
}
