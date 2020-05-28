package com.kaloyantodorov.githubrepositoryviewer.service;

import com.kaloyantodorov.githubrepositoryviewer.model.github.GitHubResponse;
import com.kaloyantodorov.githubrepositoryviewer.model.github.GitHubResponseItem;
import com.kaloyantodorov.githubrepositoryviewer.model.response.RepoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class GitHubServiceTest {

    private static final String GITHUB_API_URL = "https://api.github.com";

    private static final String GITHUB_SEARCH_REPOSITORIES_ENDPOINT = "/search/repositories";

    @Mock
    private RestTemplate gitHubRestTemplate;

    @Spy
    @InjectMocks
    private GitHubService gitHubService;

    @BeforeEach
    public void setUp() {
        this.gitHubService = new GitHubService(this.gitHubRestTemplate);
        ReflectionTestUtils.setField(this.gitHubService, "gitHubApiUrl", GITHUB_API_URL);
        ReflectionTestUtils.setField(this.gitHubService, "searchRepositoriesEndpoint", GITHUB_SEARCH_REPOSITORIES_ENDPOINT);
    }

    @Test
    public void testGetGitHubRepositoriesThrowsIllegalArgumentExceptionWhenCalledWithoutCreationDateAndLanguage() {
        // given

        // when

        // then
       assertThrows(IllegalArgumentException.class, () -> {
            this.gitHubService.getGitHubRepositories(null, null, null);
        });
    }

    @ParameterizedTest
    @MethodSource("createCountCreationDateAndLanguageParameters")
    public void testGetGitHubRepositoriesCallsEndpointCorrectly(Integer count, LocalDate creationDate, String language, String expectedUrl) {
        // given

        // when
        doReturn(Mockito.mock(GitHubResponse.class)).when(this.gitHubRestTemplate).getForObject(expectedUrl, GitHubResponse.class);
        this.gitHubService.getGitHubRepositories(count, creationDate, language);

        // then
        Mockito.verify(this.gitHubRestTemplate, times(1)).getForObject(expectedUrl, GitHubResponse.class);
    }

    private static Stream<Arguments> createCountCreationDateAndLanguageParameters() {
        var testDate = LocalDate.of(2020, 01, 01);
        var language = "Java";
        var sortParameters = "&sort=stars&order=desc";
        return Stream.of(
                Arguments.of(42, testDate, language, GITHUB_API_URL + GITHUB_SEARCH_REPOSITORIES_ENDPOINT + "?q=created:>=" + testDate.toString() + "+language:" + language + sortParameters + "&per_page=42"),
                Arguments.of(null, testDate, language, GITHUB_API_URL + GITHUB_SEARCH_REPOSITORIES_ENDPOINT + "?q=created:>=" + testDate.toString() + "+language:" + language + sortParameters),
                Arguments.of(42, null, language, GITHUB_API_URL + GITHUB_SEARCH_REPOSITORIES_ENDPOINT + "?q=language:" + language + sortParameters + "&per_page=42"),
                Arguments.of(null, null, language, GITHUB_API_URL + GITHUB_SEARCH_REPOSITORIES_ENDPOINT + "?q=language:" + language + sortParameters),
                Arguments.of(null, testDate, null, GITHUB_API_URL + GITHUB_SEARCH_REPOSITORIES_ENDPOINT + "?q=created:>=" + testDate.toString() + sortParameters)
        );
    }

    @Test
    public void testGetGitHubRepositoriesMapsGitHubResponseCorrectlyToRepoResponse() {
        // given
        GitHubResponse gitHubResponse = this.createGitHubResponse();

        // when
        doReturn(gitHubResponse).when(this.gitHubRestTemplate).getForObject(Mockito.anyString(), Mockito.any());
        RepoResponse repoResponse = this.gitHubService.getGitHubRepositories(10, LocalDate.now(), "Java");

        // then
        assertEquals(gitHubResponse.getTotalCount(), repoResponse.getRepositoriesCount());
        assertEquals(gitHubResponse.isIncompleteResults(), repoResponse.isIncompleteResults());
        assertEquals(gitHubResponse.getItems().size(), repoResponse.getRepositories().size());

        var gitHubResponseItem = gitHubResponse.getItems().get(0);
        var repoResponseItem = repoResponse.getRepositories().get(0);
        assertEquals(gitHubResponseItem.getId(), repoResponseItem.getId());
        assertEquals(gitHubResponseItem.getName(), repoResponseItem.getName());
        assertEquals(gitHubResponseItem.getFullName(), repoResponseItem.getFullName());
        assertEquals(gitHubResponseItem.isPrivateRepository(), repoResponseItem.isPrivateRepository());
        assertEquals(gitHubResponseItem.getDescription(), repoResponseItem.getDescription());
        assertEquals(gitHubResponseItem.getUrl(), repoResponseItem.getUrl());
        assertEquals(gitHubResponseItem.getCloneUrl(), repoResponseItem.getCloneUrl());
        assertEquals(gitHubResponseItem.getLanguage(), repoResponseItem.getLanguage());
        assertEquals(gitHubResponseItem.getCreatedAt(), repoResponseItem.getCreatedAt());
        assertEquals(gitHubResponseItem.getScore(), repoResponseItem.getScore());
        assertEquals(gitHubResponseItem.getStargazersCount(), repoResponseItem.getStargazersCount());

    }

    private GitHubResponse createGitHubResponse() {
        var gitHubResponse = new GitHubResponse();
        gitHubResponse.setTotalCount(2);
        gitHubResponse.setIncompleteResults(false);

        var gitHubResponseItem = new GitHubResponseItem();
        gitHubResponseItem.setId(1L);
        gitHubResponseItem.setName("repo1");
        gitHubResponseItem.setFullName("group1/repo1");
        gitHubResponseItem.setPrivateRepository(false);
        gitHubResponseItem.setDescription("Description");
        gitHubResponseItem.setUrl("URL");
        gitHubResponseItem.setCloneUrl("CloneURL");
        gitHubResponseItem.setLanguage("Java");
        gitHubResponseItem.setCreatedAt(LocalDateTime.now());
        gitHubResponseItem.setScore(10.0);
        gitHubResponseItem.setStargazersCount(1337);

        gitHubResponse.setItems(List.of(gitHubResponseItem));
        return gitHubResponse;
    }
}