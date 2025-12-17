package com.cec.production;

import java.util.Map;

/**
 * 元件生产模型类，目前生产模型仅限于单一SQL类型，并且不支持DAG编排。<p/>
 * 目前仅支持多源融合加工成一个结果，所以source多个，sink一个。<p/>
 * @author koala
 */
public class ProductionModel {
    /**
     * 模型ID
     */
    protected String modelId;
    /**
     * 模型名称
     */
    protected String modelName;
    /**
     * 开发者ID
     */
    protected String developUserId;
    /**
     * 开发者名称
     */
    protected String developUserName;
    /**
     * 元件ID
     */
    protected String dataComponentId;
    /**
     * 元件模型创建时间
     */
    protected String createTime;
    /**
     * 元件模型更新时间
     */
    protected String updateTime;
    /**
     * 元件模型类型
     */
    protected String modelType;
    /**
     * 元件模型资源ID
     */
    protected String modelResourceId;
    /**
     * 元件加工SQL
     */
    private String sql;
    /**
     * 元件资源表DDL语句列表
     */
    private Map<String,String> sourceTableCataLogs;
    /**
     * 元件结果表DDL语句
     */
    private String sinkTableCataLogs;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDevelopUserId() {
        return developUserId;
    }

    public void setDevelopUserId(String developUserId) {
        this.developUserId = developUserId;
    }

    public String getDevelopUserName() {
        return developUserName;
    }

    public void setDevelopUserName(String developUserName) {
        this.developUserName = developUserName;
    }

    public String getDataComponentId() {
        return dataComponentId;
    }

    public void setDataComponentId(String dataComponentId) {
        this.dataComponentId = dataComponentId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getModelResourceId() {
        return modelResourceId;
    }

    public void setModelResourceId(String modelResourceId) {
        this.modelResourceId = modelResourceId;
    }

    public String getSinkTableName() {
        return sinkTableName;
    }

    public void setSinkTableName(String sinkTableName) {
        this.sinkTableName = sinkTableName;
    }

    private String sinkTableName;

    public String getSinkTableCataLogs() {
        return sinkTableCataLogs;
    }

    public void setSinkTableCataLogs(String sinkTableCataLogs) {
        this.sinkTableCataLogs = sinkTableCataLogs;
    }

    public Map<String, String> getSourceTableCataLogs() {
        return sourceTableCataLogs;
    }
    public void setSourceTableCataLogs(Map<String, String> sourceTableCataLogs) {
        this.sourceTableCataLogs = sourceTableCataLogs;
    }
    public String getSql() {
        return sql;
    }
    public void setSql(String sql) {
        this.sql = sql;
    }
}
