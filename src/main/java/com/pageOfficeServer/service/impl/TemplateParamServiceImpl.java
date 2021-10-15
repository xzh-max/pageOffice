package com.pageOfficeServer.service.impl;

import com.pageOfficeServer.mapper.TemplateParamMapper;
import com.pageOfficeServer.model.*;
import com.pageOfficeServer.service.TemplateParamService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateParamServiceImpl implements TemplateParamService {

    @Autowired
    private TemplateParamMapper templateParamMapper;

    @Override
    public void addParam(String fid,TemplateParamEntryModel templateParamEntryModel, List<TemplateParamOptionModel> templateParamOptionModels,List<String> booksMarks) {
        templateParamEntryModel.setFid(fid);
        templateParamMapper.saveParamEntry(templateParamEntryModel);

        if(templateParamOptionModels!=null&&templateParamOptionModels.size()!=0){
            for(TemplateParamOptionModel templateParamOptionModel: templateParamOptionModels){
                templateParamOptionModel.setFentryId(templateParamEntryModel.getFentryId());
            }
            //批量保存下拉选项信息
            templateParamMapper.saveParamOptionInfos(templateParamOptionModels);
        }

        //复制母模版的自定义书签列表
        if(booksMarks!=null&&booksMarks.size()!=0){
            List<ParamBookMarkModel> booKMarkModels=new ArrayList<>();
            for(String bookMark:booksMarks){
                ParamBookMarkModel booKMarkModel=new ParamBookMarkModel();
                booKMarkModel.setBookMark(bookMark);
                booKMarkModel.setType("1");//自定义变量
                booKMarkModel.setFid(Integer.valueOf(fid));
                booKMarkModel.setFparamId(templateParamEntryModel.getFentryId());
                booKMarkModels.add(booKMarkModel);
            }
            templateParamMapper.saveParamBookMarkModels(booKMarkModels);
        }
    }

    @Override
    public void addPO(String fentryId, List<TemplateParamMarkInfoModel> templateParamMarkInfoModels) {
        if(templateParamMarkInfoModels!=null&&templateParamMarkInfoModels.size()!=0){
            for(TemplateParamMarkInfoModel templateParamMarkInfoModel:templateParamMarkInfoModels){
                templateParamMarkInfoModel.setFentryId(Integer.valueOf(fentryId));
            }
            templateParamMapper.saveParamMarkInfos(templateParamMarkInfoModels);
        }

    }

    @Override
    public void addPO1(TemplateParamMarkInfoModel templateParamMarkInfoModel) {
        templateParamMapper.saveParamMarkInfo(templateParamMarkInfoModel);
    }

    @Override
    public void removePO(String fid) {
        templateParamMapper.removePO(fid);
    }

    @Override
    public TemplateParOptionDto queryEntryOptionInfoById(int fentryId) {
        TemplateParamEntryModel templateParamEntryModel=templateParamMapper.getByFentryId(Integer.valueOf(fentryId));
        TemplateParOptionDto templateParOptionDto=new TemplateParOptionDto();
        if(templateParamEntryModel!=null){
            templateParOptionDto.setFentryId(fentryId);
            templateParOptionDto.setParamType(templateParamEntryModel.getParamType());
            templateParOptionDto.setName(templateParamEntryModel.getName());
        }
        List<TemplateParamOptionModel> templateParamOptionModels= templateParamMapper.getOptions(fentryId);
        if(templateParamOptionModels!=null&&templateParamOptionModels.size()!=0){
            List<String> values=new ArrayList<>();
            for(TemplateParamOptionModel templateParamOptionModel:templateParamOptionModels){
                values.add(templateParamOptionModel.getFoptionValue());
            }
            templateParOptionDto.setValue(values);
        }

        return templateParOptionDto;
    }

    @Override
    public List<ArticleInfoDto> queryAtricleDtoByFid(String fid) {
        List<TemplateArticleModel> articleModels=templateParamMapper.queryTemplateArticleByFid(fid);
        List<ArticleInfoDto> articleInfoDtos=new ArrayList<>();
        if(articleModels!=null&&articleModels.size()!=0){
            for(TemplateArticleModel templateArticleModel:articleModels){
                ArticleInfoDto articleInfoDto=new ArticleInfoDto();
                articleInfoDto.setFarticleId(templateArticleModel.getFarticleId());
                articleInfoDto.setName(templateArticleModel.getName());
                List<ArticleContentModel> articleContentModels=templateParamMapper.queryContentsById(templateArticleModel.getFarticleId());
                if(articleContentModels!=null && articleContentModels.size()!=0){
                    List<ArticleContentDto> articleContentDtos=new ArrayList<>();
                    for(ArticleContentModel articleContentModel:articleContentModels){
                        ArticleContentDto articleContentDto=new ArticleContentDto();
                        articleContentDto.setArticle(articleContentModel.getArticle());
                        articleContentDto.setFarticleContentId(articleContentModel.getFarticleContentId());
                        List<ArticleEntryRelationModel> articleEntryRelationModels=templateParamMapper.queryArticleEntryRelationByContentId(articleContentModel.getFarticleContentId());
                        if(articleEntryRelationModels!=null && articleEntryRelationModels.size()!=0){
                            int[] fentryIds=new int[articleEntryRelationModels.size()];
                            for(int i=0;i<articleEntryRelationModels.size();i++){
                                fentryIds[i]=articleEntryRelationModels.get(i).getFentryId();
                            }
                            articleContentDto.setFentryIds(fentryIds);
                        }
                        articleContentDtos.add(articleContentDto);
                    }
                    articleInfoDto.setArticleContentDtoList(articleContentDtos);
                }
                articleInfoDtos.add(articleInfoDto);
            }
        }
        return articleInfoDtos;
    }

    public void addAtricleDtosByFid(String fid,List<ArticleInfoDto> articleInfoDtos) {
        for(ArticleInfoDto articleInfoDto:articleInfoDtos){
            TemplateArticleModel templateArticleModel=new TemplateArticleModel();
            templateArticleModel.setFid(Integer.valueOf(fid));
            templateArticleModel.setName(articleInfoDto.getName());
            //查询条款的插入书签信息
            List<String> bookMarks=templateParamMapper.queryParamBookMarkBy("2",articleInfoDto.getFarticleId());
            templateParamMapper.saveArticle(templateArticleModel);

            //保存一份新的书签信息
            List<ParamBookMarkModel> paramBookMarkModels=new ArrayList<>();
            if(bookMarks.size()!=0){
                for(String bookMark:bookMarks){
                    ParamBookMarkModel paramBookMarkModel=new ParamBookMarkModel();
                    paramBookMarkModel.setFparamId(templateArticleModel.getFarticleId());
                    paramBookMarkModel.setBookMark(bookMark);
                    paramBookMarkModel.setType("2");
                    paramBookMarkModel.setFid(Integer.valueOf(fid));
                    paramBookMarkModels.add(paramBookMarkModel);
                }
                templateParamMapper.saveParamBookMarkModels(paramBookMarkModels);
            }

            if(articleInfoDto.getArticleContentDtoList()!=null&&articleInfoDto.getArticleContentDtoList().size()!=0){
                for(ArticleContentDto articleContentDto:articleInfoDto.getArticleContentDtoList()){
                    ArticleContentModel articleContentModel=new ArticleContentModel();
                    articleContentModel.setFarticleId(templateArticleModel.getFarticleId());
                    articleContentModel.setArticle(articleContentDto.getArticle());
                    templateParamMapper.saveArticleContent(articleContentModel);
                    if(articleContentDto.getFentryIds()!=null&&articleContentDto.getFentryIds().length!=0){
                        List<ArticleEntryRelationModel> articleEntryRelationModels=new ArrayList<>();
                        for(int fentryId:articleContentDto.getFentryIds()){
                            ArticleEntryRelationModel articleEntryRelationModel=new ArticleEntryRelationModel();
                            articleEntryRelationModel.setFentryId(fentryId);
                            articleEntryRelationModel.setFarticleContentId(articleContentDto.getFarticleContentId());
                            articleEntryRelationModels.add(articleEntryRelationModel);
                        }
                        templateParamMapper.saveArticleRelations(articleEntryRelationModels);
                    }
                }
            }


        }
    }

    @Override
    public void addTemplateFile(String fileId, String filePath) {
        TemplateFileModel templateFileModel=new TemplateFileModel();
        templateFileModel.setFileId(fileId);
        templateFileModel.setPath(filePath);
        templateParamMapper.addTemplateFile(templateFileModel);
    }

    @Override
    public String getTemplateFileById(String fileId) {
        if(templateParamMapper.queryTemplateFileById(fileId)!=null){
            return templateParamMapper.queryTemplateFileById(fileId).getPath();
        }
        return null;
    }

    @Override
    @Transactional
    public void modifyParamInfo(String optionValues, String type, String name, String fentryId,String length,String paramType) {
        if(paramType.equals("3")){
            //下拉选项修改
            String[] optionValuess=optionValues.split(",");
            templateParamMapper.removeParamEntryOptionsById(Integer.valueOf(fentryId));
            List<TemplateParamOptionModel> templateParamOptionModels=new ArrayList<>();
            for(String op:optionValuess){
                TemplateParamOptionModel templateParamOptionModel=new TemplateParamOptionModel();
                templateParamOptionModel.setFentryId(Integer.valueOf(fentryId));
                templateParamOptionModel.setFoptionValue(op);
                templateParamOptionModels.add(templateParamOptionModel);
            }
            templateParamMapper.saveParamOptionInfos(templateParamOptionModels);
        }else {
            //日期，文字，数字修改
            TemplateParamEntryModel templateParamEntryModel=templateParamMapper.getByFentryId(Integer.valueOf(fentryId));
            if("3".equals(templateParamEntryModel.getParamType())){
                templateParamMapper.removeParamEntryOptionsById(Integer.valueOf(fentryId));
            }
        }
        if(StringUtils.isEmpty(length)){
            templateParamMapper.modifyParamInfo(name,type,fentryId,Integer.valueOf("0"),paramType,optionValues);
        }else {
            templateParamMapper.modifyParamInfo(name,type,fentryId,Integer.valueOf(length),paramType,optionValues);
        }

    }


    @Override
    @Transactional
    public void addTemplateAndSysParam(String templateNo, List<SysParamInfoDto> sysParams) {
        TemplateParamModel templateParamModel=new TemplateParamModel();
        templateParamModel.setTemplateId(templateNo);
        templateParamMapper.addTemplateInfo(templateParamModel);
        if(templateParamModel.getFid()!=0){
            List<TemplateParamEntryModel> templateParamEntryModels=new ArrayList<>();
//            Set<String> keyset=sysParams.keySet();
            for(SysParamInfoDto sysParamInfoDto:sysParams){
                TemplateParamEntryModel templateParamEntryModel=new TemplateParamEntryModel();
                templateParamEntryModel.setName("{"+sysParamInfoDto.getSysName()+"}");
                templateParamEntryModel.setSyskey(sysParamInfoDto.getSysKey());
                templateParamEntryModel.setFid(String.valueOf(templateParamModel.getFid()));
                templateParamEntryModel.setParamType("1");
                templateParamEntryModels.add(templateParamEntryModel);
            }
            templateParamMapper.saveParamEntrys(templateParamEntryModels);
        }
    }

    public void deleteTemplateParam(int fid){
        templateParamMapper.removeTemplateParam(fid);
        templateParamMapper.removeSysParamEntrys(fid,"1");
    }

    public static void main(String[] args){
        String type="下拉选项";
        System.out.println("下拉选项".equals(type));
    }
}
