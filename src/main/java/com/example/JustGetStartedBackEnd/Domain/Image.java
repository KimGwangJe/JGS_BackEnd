package com.example.JustGetStartedBackEnd.Domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="image")
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private int image_id;

    @NotBlank
    @Column(name = "image_name")
    private String imageName;

    @NotBlank
    @Column(name = "image_url")
    private String imageUrl;

    @JoinColumn(name = "community_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Community community;

    @Builder
    Image(String imageName, String imageUrl, Community community) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.community = community;
    }
}
