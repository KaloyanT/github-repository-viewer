package com.kaloyantodorov.githubrepositoryviewer.service;

import com.kaloyantodorov.githubrepositoryviewer.model.github.GitHubResponseItem;
import com.kaloyantodorov.githubrepositoryviewer.model.response.RepoResponse;
import com.kaloyantodorov.githubrepositoryviewer.model.github.GitHubResponse;
import com.kaloyantodorov.githubrepositoryviewer.model.response.RepoResponseItem;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * This service is used to interact with the GitHub API. It can make requests to different GitHub API Endpoints and parse the Response to the Response Object returned
 * by the {@link com.kaloyantodorov.githubrepositoryviewer.controller.RepoViewerController}
 */
@Component
public class GitHubService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubService.class);

    private static final String GITHUB_API_QUERY_SEPARATOR = "+";

    @Value("${github.api.url}")
    private String gitHubApiUrl;

    @Value("${github.api.endpoints.searchRepositories}")
    private String searchRepositoriesEndpoint;

    private RestTemplate gitHubRestTemplate;

    public GitHubService(RestTemplate gitHubRestTemplate) {
        this.gitHubRestTemplate = gitHubRestTemplate;
    }

    /**
     * This method returns a given number of ("count") GitHub Repositories for the specified "creationDate" and "language" sorted descending by the number of stars.
     * @param count The number of repositories to be returned. Min 1, Max 100
     * @param creationDate The creation date of the repositories. If specified, only repositories created after this date will be returned
     * @param language The programming language used in the repositories
     * @return The GitHub repositories for these search criteria
     */
    public RepoResponse getGitHubRepositories(final Integer count, final LocalDate creationDate, final String language) {
        GitHubResponse gitHubResponse = this.callGitHubApi(this.searchRepositoriesEndpoint, count, creationDate, language);
        return this.mapGitHubResponseToRepoResponse(gitHubResponse);
    }

    private GitHubResponse callGitHubApi(final String endpoint, final Integer count, final LocalDate creationDate, final String language) {
        var url = this.buildGitHubRequestUrl(endpoint, count, creationDate, language);
        GitHubResponse gitHubResponse = null;
        try {
            gitHubResponse = this.gitHubRestTemplate.getForObject(url, GitHubResponse.class);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while making a call to the GitHub API Endpoint {}. Error Message: {}", e.getMessage());
        }

        return gitHubResponse;
    }

    private String buildGitHubRequestUrl(final String endpoint, final Integer count, final LocalDate creationDate, final String language) {
        var uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(this.gitHubApiUrl + endpoint);
        var gitHubQuery = new StringBuilder();

        if (creationDate != null) {
            gitHubQuery.append("created:>=");
            gitHubQuery.append(creationDate.toString());
        }

        if (StringUtils.isNotBlank(language)) {
            // The separator is required only if we have other parameters before that
            gitHubQuery.append(creationDate != null ? GITHUB_API_QUERY_SEPARATOR : "");
            gitHubQuery.append("language:");
            gitHubQuery.append(language);
        }

        if (StringUtils.isNotBlank(gitHubQuery.toString())) {
            uriComponentsBuilder.queryParam("q", gitHubQuery.toString());
        } else {
            // The GitHub API requires a query parameter. Depending on the requirements for the API, the "creationDate" or the "language" parameters
            // can be made mandatory or a workaround for this requirement can be added. For example, if both the "creationDate" and the "language" parameters
            // are empty, a parameter (is:) to return only public parameters can be added in order to always get a response from the GitHub API
            throw new IllegalArgumentException("At least one of the parameters creationDate or language has to be specified");
        }

        uriComponentsBuilder.queryParam("sort", "stars");
        uriComponentsBuilder.queryParam("order", "desc");

        // A pagination strategy with the help of the "page" and "per_page" parameters can be implemented. If we have a Front-End,
        // which can set the parameter for the current page and the results per page, we can extend the controller and this method. For this example,
        // keep everything simple and simply set the number of results with the help of the "per_page" parameter.
        if (count != null) {
            uriComponentsBuilder.queryParam("per_page", count);
        }

        return uriComponentsBuilder.build().toUriString();
    }

    /**
     * This method maps the GitHubResponse to our internal RepoResponse format.
     * For this example project this mapping is absolutely unnecessary, but in a real-world project,
     * where a service has to make a request to an API, custom logic/mapping has to be often applied
     * to the response, before it can be returned by the controller
     * @param gitHubResponse The GitHubResponse Object to be mapped
     * @return A Mapped RepoResponse
     */
    private RepoResponse mapGitHubResponseToRepoResponse(final GitHubResponse gitHubResponse) {
        var repoResponse = new RepoResponse();
        repoResponse.setRepositoriesCount(gitHubResponse.getTotalCount());
        repoResponse.setIncompleteResults(gitHubResponse.isIncompleteResults());
        repoResponse.setRepositories(
                gitHubResponse.getItems().stream()
                .map(this::mapGitHubResponseItemToRepoResponseItem)
                .collect(Collectors.toList())
        );
        return  repoResponse;
    }

    private RepoResponseItem mapGitHubResponseItemToRepoResponseItem(final GitHubResponseItem gitHubResponseItem) {
        var repoResponseItem = new RepoResponseItem();
        repoResponseItem.setId(gitHubResponseItem.getId());
        repoResponseItem.setName(gitHubResponseItem.getName());
        repoResponseItem.setFullName(gitHubResponseItem.getFullName());
        repoResponseItem.setPrivateRepository(gitHubResponseItem.isPrivateRepository());
        repoResponseItem.setDescription(gitHubResponseItem.getDescription());
        repoResponseItem.setUrl(gitHubResponseItem.getUrl());
        repoResponseItem.setCloneUrl(gitHubResponseItem.getCloneUrl());
        repoResponseItem.setLanguage(gitHubResponseItem.getLanguage());
        repoResponseItem.setCreatedAt(gitHubResponseItem.getCreatedAt());
        repoResponseItem.setScore(gitHubResponseItem.getScore());
        repoResponseItem.setStargazersCount(gitHubResponseItem.getStargazersCount());
        return repoResponseItem;
    }
}
