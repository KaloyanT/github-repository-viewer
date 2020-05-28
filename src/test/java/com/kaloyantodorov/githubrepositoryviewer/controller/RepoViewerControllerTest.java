package com.kaloyantodorov.githubrepositoryviewer.controller;

import com.kaloyantodorov.githubrepositoryviewer.service.GitHubService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RepoViewerController.class)
public class RepoViewerControllerTest {

    @MockBean
    private GitHubService gitHubService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testValidInputReturnsHttp200() throws Exception {
        // given

        // when

        // then
        this.mockMvc.perform(
                get("/repositories")
                .queryParam("count", "10")
                .queryParam("creationDate", LocalDate.now().toString())
                .queryParam("language", "Java"))
                .andExpect(status().isOk());
    }

    @Test
    public void testInvalidCountParameterReturnsHttp400() throws Exception {
        // given

        // when

        // then
        this.mockMvc.perform(
                get("/repositories")
                        .queryParam("count", "-10")
                        .queryParam("creationDate", LocalDate.now().toString())
                        .queryParam("language", "Java"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMissingCreationDateAndLanguageParametersReturnsHttp400() throws Exception {
        // given

        // when

        // then
        this.mockMvc.perform(
                get("/repositories")
                        .queryParam("count", "-10"))
                .andExpect(status().isBadRequest());
    }
}