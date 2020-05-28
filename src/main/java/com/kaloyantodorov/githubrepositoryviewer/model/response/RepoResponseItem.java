package com.kaloyantodorov.githubrepositoryviewer.model.response;

import java.time.LocalDateTime;
import java.util.Objects;

public class RepoResponseItem {

    private Long id;

    private String name;

    private String fullName;

    private boolean privateRepository;

    private String description;

    private String url;

    private String cloneUrl;

    private String language;

    private LocalDateTime createdAt;

    private Double score;

    private Integer stargazersCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isPrivateRepository() {
        return privateRepository;
    }

    public void setPrivateRepository(boolean privateRepository) {
        this.privateRepository = privateRepository;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(Integer stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepoResponseItem that = (RepoResponseItem) o;
        return privateRepository == that.privateRepository &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(fullName, that.fullName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(url, that.url) &&
                Objects.equals(cloneUrl, that.cloneUrl) &&
                Objects.equals(language, that.language) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(score, that.score) &&
                Objects.equals(stargazersCount, that.stargazersCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, fullName, privateRepository, description, url, cloneUrl, language, createdAt, score, stargazersCount);
    }
}
