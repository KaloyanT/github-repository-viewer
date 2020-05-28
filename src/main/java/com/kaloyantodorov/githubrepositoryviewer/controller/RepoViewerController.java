package com.kaloyantodorov.githubrepositoryviewer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaloyantodorov.githubrepositoryviewer.model.response.RepoResponse;
import com.kaloyantodorov.githubrepositoryviewer.service.GitHubService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@RestController
@Validated
public class RepoViewerController {

    private GitHubService gitHubService;

    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        this.objectMapper = new ObjectMapper();
    }

    public RepoViewerController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/repositories")
    public RepoResponse getRepositories(@RequestParam(required = false) @Min(1) @Max(100) Integer count,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate creationDate,
                                        @RequestParam(required = false) String language) {
        return this.gitHubService.getGitHubRepositories(count, creationDate, language);
    }

    // An Exception handler to return an appropriate message, when the count parameter exceeds the the defined range of [1,..,100]
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        var responseBody = this.objectMapper.createObjectNode();
        responseBody.put("errorCode", HttpStatus.BAD_REQUEST.value());
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        var responseBody = this.objectMapper.createObjectNode();
        responseBody.put("errorCode", HttpStatus.BAD_REQUEST.value());
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }
}
