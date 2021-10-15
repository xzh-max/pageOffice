package com.pageOfficeServer.service;

import com.pageOfficeServer.model.*;

import java.util.List;

public interface TemplateParamService {

    /**
     * 保存自定义变量以及下拉下拉选项信息
     * * @param fid 模板id
     * @param templateParamEntryModel 自定义变量
     * @param templateParamOptionModels 变量对应的下拉选项，如果为下拉选项不为空，如果为自定义变量为空
     */
    void addParam(String fid, TemplateParamEntryModel templateParamEntryModel, List<TemplateParamOptionModel> templateParamOptionModels, List<String> bookMarks);

    /**
     * 批量添加书签信息
     * @param fentryId 自定义变量id
     * @param templateParamMarkInfoModels 书签信息
     */
    void addPO(String fentryId, List<TemplateParamMarkInfoModel> templateParamMarkInfoModels);

    /**
     * 添加书签信息
     * @param templateParamMarkInfoModel
     */
    void addPO1(TemplateParamMarkInfoModel templateParamMarkInfoModel);

    /**
     * 删除标签信息
     * @param fid 标签fid
     */
    void removePO(String fid);

    /**
     * 通过变量id获取下拉选项信息
     * @param fentryId 变量id
     * @return
     */
    TemplateParOptionDto queryEntryOptionInfoById(int fentryId);

    /**
     * 通过fid获取条款信息
     * @param fid 模板fid
     * @return
     */
    List<ArticleInfoDto> queryAtricleDtoByFid(String fid);

    /**
     * 保存模板和系统变量
     * @param templateNo 模板编号
     * @param sysParams 系统变量
     */
    void addTemplateAndSysParam(String templateNo, List<SysParamInfoDto> sysParams);

    /**
     * 删除模板信息
     * @param fid
     */
    void deleteTemplateParam(int fid);

    /**
     * 复制新增条款信息
     * @param fid
     * @param articleInfoDtos
     */
    void addAtricleDtosByFid(String fid, List<ArticleInfoDto> articleInfoDtos);

    void addTemplateFile(String fileId, String filePath);

    String getTemplateFileById(String fileId);

    /**
     * 修改变量信息
     * @param optionValues
     * @param type
     * @param name
     * @param fentryId
     */
    void modifyParamInfo(String optionValues, String type, String name, String fentryId, String length, String paramType);
}
