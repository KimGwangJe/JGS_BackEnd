package com.example.JustGetStartedBackEnd.API.Image.Controller;

import com.example.JustGetStartedBackEnd.API.Image.Service.APIImageService;
import com.example.JustGetStartedBackEnd.TestCustomOAuth2User.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@WebMvcTest(APIImageController.class)
class APIImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private APIImageService apiImageService;

    @DisplayName("이미지 업로드 성공")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void saveImage() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", "test image content".getBytes());

        URL mockUrl = new URL("http://localhost:8080/test-image.jpg");

        when(apiImageService.saveImage(any(MultipartFile.class))).thenReturn(mockUrl);

        mockMvc.perform(multipart("/api/image")
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("\"" + mockUrl.toString() + "\""));
    }

    @DisplayName("이미지 업로드 실패 - 이미지 비어 있음")
    @WithMockCustomUser(id = 1L, role = "ADMIN")
    @Test
    void saveImage_whenImageIsEmpty() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "", "image/jpeg", new byte[0]);

        mockMvc.perform(multipart("/api/image")
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
