package com.example.JustGetStartedBackEnd.TestCustomOAuth2User;

import com.example.JustGetStartedBackEnd.API.Member.DTO.MemberDTO;
import com.example.JustGetStartedBackEnd.OAuth2.UserDetails.CustomOAuth2User;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // Create a mock MemberDTO
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(customUser.id());
        memberDTO.setRole(customUser.role());
        memberDTO.setEmail("test@example.com");  // Set default or mock email
        memberDTO.setName("Test User");          // Set default or mock name
        memberDTO.setProfileImage("default.png");// Set default or mock profile image

        // Create a CustomOAuth2User with the mock MemberDTO
        OAuth2User oAuth2User = new CustomOAuth2User(memberDTO);

        // Create an OAuth2AuthenticationToken with the mock OAuth2User
        OAuth2AuthenticationToken auth = new OAuth2AuthenticationToken(
                oAuth2User,
                oAuth2User.getAuthorities(),
                "github" // Specify the registration ID (could be any identifier for your OAuth2 setup)
        );

        context.setAuthentication(auth);
        return context;
    }
}
