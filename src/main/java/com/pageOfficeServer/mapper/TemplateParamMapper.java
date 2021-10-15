package com.pageOfficeServer.mapper;

import com.pageOfficeServer.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TemplateParamMapper {

    //通过模板编号获取模板信息
    TemplateParamModel getTemplateByTemplateNo(@Param("templateNo")String templateNo);

    //获取变量列表
    List<TemplateParamEntryModel> getParamEntrys(@Param("fid")int fid);

    //通过fid获取模板信息
    TemplateParamModel getModelById(@Param("fid")String fid);

    //通过fentryId获取自定义
//    TemplateParamModel getModelById(@Param("fid")String fid);

    //通过fentryId找到变量信息
    TemplateParamEntryModel getByFentryId(@Param("fentryId")int fentryId);

    void removeParamEntryById(@Param("fentryId")int fentryId);

    void removeParamEntryOptionsById(@Param("fentryId")int fentryId);

    //获取下拉选项信息
    List<TemplateParamOptionModel> getOptions(@Param("fentryId")int fentryId);

    //获取标签信息
    List<TemplateParamMarkInfoModel> getMarkInfo(@Param("fentryId")int fentryId);

    //保存自定义变量，包括自定义变量和下拉变量
    int saveParamEntry(TemplateParamEntryModel entryModel);

    //保存变量标签信息
    void saveParamMarkInfo(TemplateParamMarkInfoModel templateParamMarkInfoModel);

    //批量保存变量标签信息
    void saveParamMarkInfos(List<TemplateParamMarkInfoModel> list);

    //保存下拉选项值
    void saveParamOptionInfo(TemplateParamOptionModel templateParamOptionModel);

    //批量保存下拉选项值
    void saveParamOptionInfos(List<TemplateParamOptionModel> list);

    //通过标签查询变量信息
    TemplateParamEntryModel queryByMarkInfo(@Param("fentryId")int fentryId);

    //通过标签查询变量信息
    TemplateParamMarkInfoModel queryByMark(@Param("fmark")String fmark);

    //删除标签信息
    void removePO(@Param("fid")String fid);

    //保存条款信息
    int saveArticle(TemplateArticleModel templateArticleModel);

    //保存条款内容信息
    int saveArticleContent(ArticleContentModel articleContentModel);

    //保存条款变量关系信息
    void saveArticleRelations(List<ArticleEntryRelationModel> articleEntryRelationModels);

    // 保存条款变量关系信息
    int saveArticleRelation(ArticleEntryRelationModel articleEntryRelationModel);

    //根据farticleId查询条款信息
    TemplateArticleModel queryArticleById(@Param("farticleId")int farticleId);

    //根据farticleId查询条款所有选项内容
    List<ArticleContentModel> queryContentsById(@Param("farticleId")int farticleId);

    //根据farticleContentId 查询选项对应的自定义变量
    List<ArticleEntryRelationModel> queryArticleContentsById(@Param("farticleContentId")int farticleContentId);

    //删除关系
    void removeArticleContentsByIds(@Param("farticleContentId")int farticleContentId);

    //删除条款选项
    void removeContentsById(@Param("farticleId")int farticleId);

    //删除条款
    void removeArticleById(@Param("farticleId")int farticleId);

    //通过模板fid查询所有条款变量
    List<TemplateArticleModel> queryTemplateArticleByFid(@Param("fid")String fid);

    //根据条款选项信息查询对应的变量信息
    List<ArticleEntryRelationModel> queryArticleEntryRelationByContentId(@Param("farticleContentId")int farticleContentId);

    //保存模板信息
    int addTemplateInfo(TemplateParamModel templateParamModel);

    void saveParamEntrys(List<TemplateParamEntryModel> list);

    //删除模板数据
    void removeTemplateParam(int fid);

    //删除模板系统变量信息
    void removeSysParamEntrys(@Param("fid")int fid,@Param("paramType")String paramType);

    void saveHandsSysInfo(List<HandSysInfoModel> handSysInfoModels);

    List<HandSysInfoModel> queryHandsSysInfo(@Param("contractNo")String contractNo);

    void deleteHandsSysInfo(@Param("contractNo")String contractNo);

    List<TemplateArticleModel> queryArticleByFidAndName(@Param("fid") String fid,@Param("name") String name);

    //查询消费失败日志信息
    List<ConsumerFailModel> queryConsumerFail();

    void addConsumerFail(ConsumerFailModel consumerFailModel);

    void removeConsumerFail(@Param("id") int id);

    //添加书签信息，控制表格可编辑
    void addBookMarks(List<BooKMarkModel> bks);

    List<BooKMarkModel> queryBkByFid(Integer fid);

    void removeBkByFid(@Param("fid")String fid);

    //添加书签信息
    void saveParamBookMarkModels(List<ParamBookMarkModel> paramBookMarkModels);

    List<String> queryParamBookMarkBy(@Param("type")String type,@Param("fparamId")Integer fparamId);

    void updateTemplateStatus(@Param("templateNo")String templateNo,@Param("status")String status);

    List<String> queryParamBookMarkByFid(@Param("fid")Integer fid);

    void batchdeleteBks(@Param("array") String[] array,@Param("fid")Integer fid);

    //查询关闭打开状态
    String queryOpenStatus(String templateNo);

    //更新关闭打开状态
    void updateOpenStatus(@Param("openStatus")String openStatus,@Param("fid")String fid);

    /**
     * 添加文件地址
     */
    void addTemplateFile(TemplateFileModel templateFileModel);

    /**
     * 根据文件Id或者文件path
     * @param fileId
     * @return
     */
    TemplateFileModel queryTemplateFileById(String fileId);

    /**
     * 更新自定义变量
     */
    void modifyParamInfo(@Param("name") String name, @Param("type") String type, @Param("fentryId") String fentryId, @Param("length") Integer length, @Param("paramType")String paramType ,@Param("optionValues")String optionValues);

    /**
     * 根据fentryId查询条款变量
     * @param fentryId
     */
    List<ArticleEntryRelationModel> queryTemplateArticleContent(@Param("fentryId") String fentryId);
}