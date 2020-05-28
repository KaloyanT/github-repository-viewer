package com.kaloyantodorov.githubrepositoryviewer.model.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubResponse {

    @JsonProperty("total_count")
    private int totalCount;

    private boolean incompleteResults;

    private List<GitHubResponseItem> items;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public List<GitHubResponseItem> getItems() {
        return items;
    }

    public void setItems(List<GitHubResponseItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitHubResponse that = (GitHubResponse) o;
        return totalCount == that.totalCount &&
                incompleteResults == that.incompleteResults &&
                Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalCount, incompleteResults, items);
    }

    @Override
    public String toString() {
        return "GitHubResponse{" +
                "totalItems=" + totalCount +
                ", incompleteResults=" + incompleteResults +
                ", gitHubResponseItems=" + items +
                '}';
    }
}
