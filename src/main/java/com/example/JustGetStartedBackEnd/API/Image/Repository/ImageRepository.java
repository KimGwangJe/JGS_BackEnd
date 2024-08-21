package com.example.JustGetStartedBackEnd.API.Image.Repository;

import com.example.JustGetStartedBackEnd.API.Image.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
