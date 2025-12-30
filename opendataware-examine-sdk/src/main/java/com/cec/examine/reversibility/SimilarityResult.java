package com.cec.examine.reversibility;
/**
 * 相似性审核
 */
public class SimilarityResult extends ReversibilityResult {

    private String resultColumName;

    public String getResultColumName() {
        return resultColumName;
    }

    public void setResultColumName(String resultColumName) {
        this.resultColumName = resultColumName;
    }

    public String getResourceColumName() {
        return resourceColumName;
    }

    public void setResourceColumName(String resourceColumName) {
        this.resourceColumName = resourceColumName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    private String resourceColumName;
    private Integer score;
}
