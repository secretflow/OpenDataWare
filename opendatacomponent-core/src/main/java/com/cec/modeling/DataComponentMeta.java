package com.cec.modeling;

import com.cec.comm.Notice;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 数据元件业务元数据包装类
 * @author koala
 */
public class DataComponentMeta {

    @Notice(name = "元件id", stage = "Definition")
    protected String componentId;

    @Notice(name = "元件名", stage = "Definition")
    protected String componentName;

    @Notice(name = "元件编码", stage = "Definition")
    protected String componentCode;

    @Notice(name = "元件英文名", stage = "Definition")
    protected String componentEName;

    @Notice(name = "元件描述", stage = "Definition")
    protected String description;

    @Notice(name = "关键词", description = "词之间用逗号隔开", stage = "Definition")
    protected String keyword;

    @Notice(name = "后台类目ID", stage = "Definition")
    protected String categoryId;

    @Notice(name = "后台类目名称", stage = "Definition")
    protected String categoryName;

    @Notice(name = "元件目名称", stage = "Definition")
    protected String categoryNames;

    @Notice(name = "根分类ID", stage = "Definition")
    protected String rootCategoryId;

    @Notice(name = "根分类名称", stage = "Definition")
    protected String rootCategoryName;

    @Notice(name = "数据来源", description = "多个， 数组格式", stage = "Definition")
    protected String datasource;

    @Notice(name = "数据提供方", description = "多个， 数组格式", stage = "Definition")
    protected String dataProvider;

    @Notice(name = "商品详情", stage = "Definition")
    protected String content;

    @Notice(name = "元件类型", description = "1:组态；2：模态； 3：向量; 4: 组合态", stage = "Definition")
    protected String componentForm;

    @Notice(name = "生产模式", description = "离线生产-nonRealTime，实时生产-realTime，触发生产-trigger，源端生产-source", stage = "Definition")
    protected String productionMode;

    @Notice(name = "生产频率", description = "1:秒;2:日;3:周;4:月;5:季;6:年;7:不更新;8:触发更新;9:时;10:分", stage = "Definition")
    protected String dataFrequency;

    @Notice(name = "生产频率展示名称", description = "1:秒;2:日;3:周;4:月;5:季;6:年;7:不更新", stage = "Definition")
    protected String dataFrequencyName;

    @Notice(name = "属性名称", description = "格式为：[{\"fieldNameEns\":[\"fieldNameEn1\",\"fieldNameEn2\"],\"mergedName\":\"mergedName\"}]", stage = "Definition")
    protected String propNames;

    @Notice(name = "元件状态")
    protected Integer status;

    @Notice(name = "元件状态msg")
    protected String statusMsg;

    @Notice(name = "URL服务地址")
    protected String serviceUrl;

    @Notice(name = "服务接口描述")
    protected String serviceDescription;

    @Notice(name = "交付接口method", description = "1:post;2:get;3:全部")
    protected Integer requestFashion;

    @Notice(name = "入参说明", stage = "Definition")
    protected String inParam;

    @Notice(name = "出参说明", stage = "Definition")
    protected String outParam;

    @Notice(name = "数据集信息", stage = "Definition")
    protected String datasetInfo;

    @Notice(name = "来源表", stage = "Definition")
    protected String sourceTable;

    @Notice(name = "元件商ID", stage = "Definition")
    protected Long merchantId;

    @Notice(name = "元件商", stage = "Definition")
    protected String merchantName;

    @Notice(name = "创建人ID", stage = "Definition")
    protected Long createId;

    @Notice(name = "创建人", stage = "Definition")
    protected String createName;

    @Notice(name = "创建时间", description = "yyyy-MM-dd HH:mm:ss", stage = "Definition")
    protected String createTime;

    @Notice(name = "修改人ID", stage = "Definition")
    protected Long updateId;

    @Notice(name = "修改人", stage = "Definition")
    protected String updateName;

    @Notice(name = "修改时间", description = "yyyy-MM-dd HH:mm:ss", stage = "Definition")
    protected String updateTime;

    @Notice(name = "数据源信息", stage = "Definition")
    protected List resourceList;

    @Notice(name = "需求ID", stage = "Definition")
    protected Long requirementId;

    @Notice(name = "需求名称", stage = "Definition")
    protected String requirementName;

    @Notice(name = "需求ID", stage = "Definition")
    protected String needId;

    @Notice(name = "开发器ID", stage = "Definition")
    protected Integer microDeveloperId;

    @Notice(name = "元件身份标识", description = "1没有标识 2个人主体 3企业主体 4其他主体", stage = "Definition")
    protected Integer identificationStatus;

    @Notice(name = "元件身份标识字段", stage = "Definition")
    protected String identificationCol;

    @Notice(name = "运营管理中心ID", stage = "Definition")
    protected String omcId;

    @Notice(name = "运营管理中心名称", stage = "Definition")
    protected String omcName;

    @Notice(name = "运营管理中心电话", stage = "Definition")
    protected String omcPhone;

    @Notice(name = "区域范围", stage = "Definition")
    protected String areaRange;

    @Notice(name = "时间范围", stage = "Definition")
    protected String timeRange;

    @Notice(name = "生存周期", description = "一年两年三年五年永久，默认永久", stage = "Definition")
    protected String termOfValidity;

    @Notice(name = "是否允许出境", description = "1是 2否， 默认否", stage = "Definition")
    protected Integer allowedToLeave;

    @Notice(name = "是否跨主体流动", description = "1是 2否", stage = "Definition")
    protected Integer accessAcross;

    @Notice(name = "元件版本", stage = "Definition")
    protected String version;

    @Notice(name = "元件开发单位统一社会信用代码", stage = "Definition")
    protected String unifiedSocialCreditCode;

    @Notice(name = "元件开发单位电话", stage = "Definition")
    protected String agentPhone;

    @Notice(name = "是否跨境", description = "是 1 否 0", stage = "Definition")
    protected Integer isCrossBorder;

    //生产后

    @Notice(name = "更新日期")
    protected String dataUpdateDate;

    @Notice(name = "更新时间", description = "HH:MM:ss")
    protected String dataUpdateTime;

    @Notice(name = "安全风险评估", description = "默认无风险")
    protected String assessSafety;

    @Notice(name = "评估机构")
    protected String evaluationAgency;

    @Notice(name = "评估时间", description = "yyyy-MM-dd HH:mm:ss")
    protected String evaluationTime;

    @Notice(name = "评估结论")
    protected String evaluationRes;

    @Notice(name = "整改措施")
    protected String correctiveMeasures;

    @Notice(name = "质量等级")
    protected String qualityLevel;

    @Notice(name = "价值等级")
    protected String nameLevel;

    @Notice(name = "安全等级")
    protected String safeLevel;

    @Notice(name = "元件信息项")
    protected String informationItems;

    @Notice(name = "入库时间", description = "yyyy-MM-dd HH:mm:ss")
    protected String storageDate;

    //交易-交付

    @Notice(name ="交付类型", description = "1:API调用；2：服务推送；3：数据集")
    protected String serviceType;

    @Notice(name = "本次评价积分")
    protected Long score;

    @Notice(name = "描述相符评分", description = "1,2,3,4,5")
    protected Long starDesciption;

    @Notice(name = "产品质量评分", description = "1,2,3,4,5")
    protected Long starProduction;

    @Notice(name = "服务态度评分", description = "1,2,3,4,5")
    protected Long starService;

    @Notice(name = "上架时间", description = "yyyy-MM-dd HH:mm:ss")
    protected String upDate;

    @Notice(name = "上架人ID")
    protected Long upPersonId;

    @Notice(name = "上架人")
    protected String upPerson;

    @Notice(name = "下架时间", description = "yyyy-MM-dd HH:mm:ss")
    protected String downDate;

    @Notice(name = "下架人ID")
    protected Long downPersonId;

    @Notice(name = "下架人")
    protected String downPerson;

    @Notice(name = "售后服务")
    protected String afterSaleservice;

    @Notice(name = "订购次数")
    protected  Integer orderTimes = (int)Math.round(1000 +Math.random()*1000);

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getComponentEName() {
        return componentEName;
    }

    public void setComponentEName(String componentEName) {
        this.componentEName = componentEName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(String categoryNames) {
        this.categoryNames = categoryNames;
    }

    public String getRootCategoryId() {
        return rootCategoryId;
    }

    public void setRootCategoryId(String rootCategoryId) {
        this.rootCategoryId = rootCategoryId;
    }

    public String getRootCategoryName() {
        return rootCategoryName;
    }

    public void setRootCategoryName(String rootCategoryName) {
        this.rootCategoryName = rootCategoryName;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(String dataProvider) {
        this.dataProvider = dataProvider;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComponentForm() {
        return componentForm;
    }

    public void setComponentForm(String componentForm) {
        this.componentForm = componentForm;
    }

    public String getProductionMode() {
        return productionMode;
    }

    public void setProductionMode(String productionMode) {
        this.productionMode = productionMode;
    }

    public String getDataFrequency() {
        return dataFrequency;
    }

    public void setDataFrequency(String dataFrequency) {
        this.dataFrequency = dataFrequency;
    }

    public String getDataFrequencyName() {
        return dataFrequencyName;
    }

    public void setDataFrequencyName(String dataFrequencyName) {
        this.dataFrequencyName = dataFrequencyName;
    }

    public String getDataUpdateDate() {
        return dataUpdateDate;
    }

    public void setDataUpdateDate(String dataUpdateDate) {
        this.dataUpdateDate = dataUpdateDate;
    }

    public String getDataUpdateTime() {
        return dataUpdateTime;
    }

    public void setDataUpdateTime(String dataUpdateTime) {
        this.dataUpdateTime = dataUpdateTime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPropNames() {
        return propNames;
    }

    public void setPropNames(String propNames) {
        this.propNames = propNames;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public Integer getRequestFashion() {
        return requestFashion;
    }

    public void setRequestFashion(Integer requestFashion) {
        this.requestFashion = requestFashion;
    }

    public String getInParam() {
        return inParam;
    }

    public void setInParam(String inParam) {
        this.inParam = inParam;
    }

    public String getOutParam() {
        return outParam;
    }

    public void setOutParam(String outParam) {
        this.outParam = outParam;
    }

    public String getDatasetInfo() {
        return datasetInfo;
    }

    public void setDatasetInfo(String datasetInfo) {
        this.datasetInfo = datasetInfo;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List getResourceList() {
        return resourceList;
    }

    public void setResourceList(List resourceList) {
        this.resourceList = resourceList;
    }

    public Long getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Long requirementId) {
        this.requirementId = requirementId;
    }

    public String getRequirementName() {
        return requirementName;
    }

    public void setRequirementName(String requirementName) {
        this.requirementName = requirementName;
    }

    public String getNeedId() {
        return needId;
    }

    public void setNeedId(String needId) {
        this.needId = needId;
    }

    public Integer getMicroDeveloperId() {
        return microDeveloperId;
    }

    public void setMicroDeveloperId(Integer microDeveloperId) {
        this.microDeveloperId = microDeveloperId;
    }

    public Integer getIdentificationStatus() {
        return identificationStatus;
    }

    public void setIdentificationStatus(Integer identificationStatus) {
        this.identificationStatus = identificationStatus;
    }

    public String getIdentificationCol() {
        return identificationCol;
    }

    public void setIdentificationCol(String identificationCol) {
        this.identificationCol = identificationCol;
    }

    public String getOmcId() {
        return omcId;
    }

    public void setOmcId(String omcId) {
        this.omcId = omcId;
    }

    public String getOmcName() {
        return omcName;
    }

    public void setOmcName(String omcName) {
        this.omcName = omcName;
    }

    public String getOmcPhone() {
        return omcPhone;
    }

    public void setOmcPhone(String omcPhone) {
        this.omcPhone = omcPhone;
    }

    public String getAreaRange() {
        return areaRange;
    }

    public void setAreaRange(String areaRange) {
        this.areaRange = areaRange;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public String getTermOfValidity() {
        return termOfValidity;
    }

    public void setTermOfValidity(String termOfValidity) {
        this.termOfValidity = termOfValidity;
    }

    public Integer getAllowedToLeave() {
        return allowedToLeave;
    }

    public void setAllowedToLeave(Integer allowedToLeave) {
        this.allowedToLeave = allowedToLeave;
    }

    public Integer getAccessAcross() {
        return accessAcross;
    }

    public void setAccessAcross(Integer accessAcross) {
        this.accessAcross = accessAcross;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUnifiedSocialCreditCode() {
        return unifiedSocialCreditCode;
    }

    public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) {
        this.unifiedSocialCreditCode = unifiedSocialCreditCode;
    }

    public String getAgentPhone() {
        return agentPhone;
    }

    public void setAgentPhone(String agentPhone) {
        this.agentPhone = agentPhone;
    }

    public Integer getIsCrossBorder() {
        return isCrossBorder;
    }

    public void setIsCrossBorder(Integer isCrossBorder) {
        this.isCrossBorder = isCrossBorder;
    }

    public String getAssessSafety() {
        return assessSafety;
    }

    public void setAssessSafety(String assessSafety) {
        this.assessSafety = assessSafety;
    }

    public String getEvaluationAgency() {
        return evaluationAgency;
    }

    public void setEvaluationAgency(String evaluationAgency) {
        this.evaluationAgency = evaluationAgency;
    }

    public String getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(String evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public String getEvaluationRes() {
        return evaluationRes;
    }

    public void setEvaluationRes(String evaluationRes) {
        this.evaluationRes = evaluationRes;
    }

    public String getCorrectiveMeasures() {
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(String correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }

    public String getQualityLevel() {
        return qualityLevel;
    }

    public void setQualityLevel(String qualityLevel) {
        this.qualityLevel = qualityLevel;
    }

    public String getNameLevel() {
        return nameLevel;
    }

    public void setNameLevel(String nameLevel) {
        this.nameLevel = nameLevel;
    }

    public String getSafeLevel() {
        return safeLevel;
    }

    public void setSafeLevel(String safeLevel) {
        this.safeLevel = safeLevel;
    }

    public String getInformationItems() {
        return informationItems;
    }

    public void setInformationItems(String informationItems) {
        this.informationItems = informationItems;
    }

    public String getStorageDate() {
        return storageDate;
    }

    public void setStorageDate(String storageDate) {
        this.storageDate = storageDate;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Long getStarDesciption() {
        return starDesciption;
    }

    public void setStarDesciption(Long starDesciption) {
        this.starDesciption = starDesciption;
    }

    public Long getStarProduction() {
        return starProduction;
    }

    public void setStarProduction(Long starProduction) {
        this.starProduction = starProduction;
    }

    public Long getStarService() {
        return starService;
    }

    public void setStarService(Long starService) {
        this.starService = starService;
    }

    public String getUpDate() {
        return upDate;
    }

    public void setUpDate(String upDate) {
        this.upDate = upDate;
    }

    public Long getUpPersonId() {
        return upPersonId;
    }

    public void setUpPersonId(Long upPersonId) {
        this.upPersonId = upPersonId;
    }

    public String getUpPerson() {
        return upPerson;
    }

    public void setUpPerson(String upPerson) {
        this.upPerson = upPerson;
    }

    public String getDownDate() {
        return downDate;
    }

    public void setDownDate(String downDate) {
        this.downDate = downDate;
    }

    public Long getDownPersonId() {
        return downPersonId;
    }

    public void setDownPersonId(Long downPersonId) {
        this.downPersonId = downPersonId;
    }

    public String getDownPerson() {
        return downPerson;
    }

    public void setDownPerson(String downPerson) {
        this.downPerson = downPerson;
    }

    public String getAfterSaleservice() {
        return afterSaleservice;
    }

    public void setAfterSaleservice(String afterSaleservice) {
        this.afterSaleservice = afterSaleservice;
    }

    public Integer getOrderTimes() {
        return orderTimes;
    }

    public void setOrderTimes(Integer orderTimes) {
        this.orderTimes = orderTimes;
    }

    public Double getRenewalRate() {
        return renewalRate;
    }

    public void setRenewalRate(Double renewalRate) {
        this.renewalRate = renewalRate;
    }

    @Notice(name = "续费率")
    protected  Double renewalRate = Math.random();

    public String getStrCreateTime(){
        SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(null != createTime){
            return sft.format(createTime);
        }
        return null;
    }

    public String getStrUpdateTime(){
        SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(null != updateTime){
            return sft.format(updateTime);
        }
        return null;
    }

    public String getStrCurrentTime(){
        SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sft.format(new Date());
    }
}