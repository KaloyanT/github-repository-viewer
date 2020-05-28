package com.kaloyantodorov.githubrepositoryviewer.model.response;

import java.util.List;
import java.util.Objects;

public class RepoResponse {

    private int repositoriesCount;

    private boolean incompleteResults;

    private List<RepoResponseItem> repositories;

    public int getRepositoriesCount() {
        return repositoriesCount;
    }

    public void setRepositoriesCount(int repositoriesCount) {
        this.repositoriesCount = repositoriesCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public List<RepoResponseItem> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<RepoResponseItem> repositories) {
        this.repositories = repositories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepoResponse that = (RepoResponse) o;
        return repositoriesCount == that.repositoriesCount &&
                incompleteResults == that.incompleteResults &&
                Objects.equals(repositories, that.repositories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repositoriesCount, incompleteResults, repositories);
    }
}
