package com.pageOfficeServer.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pageOfficeServer.mapper.TemplateParamMapper;
import com.pageOfficeServer.model.*;
import com.pageOfficeServer.service.TemplateParamService;
import com.pageOfficeServer.util.FileUtil;
import com.pageOfficeServer.util.PingYingUtils;
import com.pageOfficeServer.util.SysUtil;
import com.pageOfficeServer.util.WaterMarkUtil;
import com.zhuozhengsoft.pageoffice.BorderStyleType;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import com.zhuozhengsoft.pageoffice.ThemeType;
import com.zhuozhengsoft.pageoffice.wordwriter.DataRegion;
import com.zhuozhengsoft.pageoffice.wordwriter.WordDocument;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Controller
public class Template extends HttpServlet {
	private static final long serialVersionUID = -758686623642845302L;

	@Autowired
	private TemplateParamMapper templateParamMapper;

	@Autowired
	private TemplateParamService templateParamService;

	@RequestMapping("templateEdit")
	@ResponseBody
	public ModelAndView openword(HttpServletRequest request, HttpServletResponse response){
		String templateNo=request.getParameter("templateNo");

		TemplateParamModel templateParamModel=templateParamMapper.getTemplateByTemplateNo(templateNo);
		if(templateParamModel==null){
			return null;
		}
		List<TemplateParamEntryModel> templateParamEntryModels=templateParamMapper.getParamEntrys(templateParamModel.getFid());

		//系统变量
		List<String> tags=new ArrayList<>();
		//自定义变量
		List<TemplateParMarkDto> templateParMarkDtos=new ArrayList<>();
		for(TemplateParamEntryModel templateParamEntryModel:templateParamEntryModels){
			p:
			switch (templateParamEntryModel.getParamType()){
				//系统变量
				case "1":
					tags.add(templateParamEntryModel.getName());
					//自定义变量
					break p;
				case "2":
					//用户还未使用的自定义变量
					TemplateParMarkDto templateParMarkDto=new TemplateParMarkDto();
					templateParMarkDto.setName(templateParamEntryModel.getName());
					templateParMarkDto.setLength(templateParamEntryModel.getLength());
					templateParMarkDto.setType(templateParamEntryModel.getType());
					templateParMarkDto.setFentryId(templateParamEntryModel.getFentryId());
					templateParMarkDtos.add(templateParMarkDto);
					break p;
				//下拉选项
				case "3":
					//获取下拉选项信息
					TemplateParMarkDto templateParMarkDtoo=new TemplateParMarkDto();
					templateParMarkDtoo.setName(templateParamEntryModel.getName());
					templateParMarkDtoo.setFentryId(templateParamEntryModel.getFentryId());

					//获取值信息
					List<TemplateParamOptionModel> templateParamOptionModels=templateParamMapper.getOptions(templateParamEntryModel.getFentryId());
					if(templateParamOptionModels!=null && templateParamOptionModels.size()!=0){

						List<String> options=new ArrayList<>();
						for(TemplateParamOptionModel templateParamOptionModel:templateParamOptionModels){
							options.add(templateParamOptionModel.getFoptionValue());//添加选项值
						}
						templateParMarkDtoo.setOptionValues(options);
					}
					templateParMarkDtos.add(templateParMarkDtoo);
					break p;
			}
		}

		List<ArticleInfoDto> articleInfoDtos = templateParamService.queryAtricleDtoByFid(String.valueOf(templateParamModel.getFid()));
		ModelAndView modelAndView = new ModelAndView("templateEdit");
		modelAndView.addObject("tags",tags);
		modelAndView.addObject("fid",templateParamModel.getFid());
		modelAndView.addObject("templateParMarkDtos",templateParMarkDtos);//自定义变量
		modelAndView.addObject("articleInfoDtos",articleInfoDtos);//条款信息

		//定义系统变量，使用dataTag
		WordDocument doc = new WordDocument();
		//定义系统变量，使用dataTag
		if(tags!=null){
			for(String pa:tags){
				doc.getTemplate().defineDataTag(pa);
			}
		}

		//定义自定义变量数据区域
		if(templateParMarkDtos!=null && templateParMarkDtos.size()!=0){
			for(TemplateParMarkDto templateParMarkDto:templateParMarkDtos){
//				String poName="PO_"+ PingYingUtils.getPinYinHeadChar(templateParMarkDto.getName());
//				//定义数据区域
//				doc.getTemplate().defineDataRegion(poName, templateParMarkDto.getName());
//				//打开数据区域
//				DataRegion dataRegion =doc.openDataRegion(poName);
//				dataRegion.setEditing(false);
//				dataRegion.getShading().setBackgroundPatternColor(Color.yellow);
				List<String> bookMarks=templateParamMapper.queryParamBookMarkBy("1",templateParMarkDto.getFentryId());
				for(String bookMark:bookMarks){
					doc.getTemplate().defineDataRegion(bookMark, templateParMarkDto.getName());
					DataRegion dataRegion =doc.openDataRegion(bookMark);
					dataRegion.setEditing(false);
					dataRegion.getShading().setBackgroundPatternColor(Color.yellow);
				}
				templateParMarkDto.setBookMarks(bookMarks);
			}
		}

		//定义条款数据区域
		if(articleInfoDtos!=null && articleInfoDtos.size()!=0 ){
			for(ArticleInfoDto articleInfoDto:articleInfoDtos){
//				String poName="PO_"+ PingYingUtils.getPinYinHeadChar(articleInfoDto.getName());
//				articleInfoDto.setMarkName(poName);
//				doc.getTemplate().defineDataRegion(poName, articleInfoDto.getName());
//				DataRegion dataRegion =doc.openDataRegion(poName);
//				dataRegion.setEditing(false);
//				dataRegion.getShading().setBackgroundPatternColor(Color.yellow);

				List<String> bookMarks=templateParamMapper.queryParamBookMarkBy("2",articleInfoDto.getFarticleId());
				for(String bookMark:bookMarks){
					doc.getTemplate().defineDataRegion(bookMark, articleInfoDto.getName());
					DataRegion dataRegion =doc.openDataRegion(bookMark);
					dataRegion.setEditing(false);
					dataRegion.getShading().setBackgroundPatternColor(Color.yellow);
				}
				articleInfoDto.setBookMarks(bookMarks);
			}
		}

		DataRegion dataRegion =doc.openDataRegion("PO_biaoge");
		dataRegion.setEditing(true);

		PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
		poCtrl.addCustomToolButton("保存", "Save()", 1);
		poCtrl.addCustomToolButton("保存并退出", "SaveAndExit()", 2);
		poCtrl.addCustomToolButton("插入子模板可编辑区域", "insertEditRegion()", 3);
		poCtrl.addCustomToolButton("删除子模板可编辑区域", "deleteEditRegion()", 4);
		poCtrl.setServerPage(request.getContextPath()+"/poserver.zz");
        poCtrl.setJsFunction_AfterDocumentOpened( "AfterDocumentOpened()");

//		doc.getWaterMark().setText("WENS");
		poCtrl.setTheme(ThemeType.Office2007);
		poCtrl.setBorderStyle(BorderStyleType.BorderThin);
		poCtrl.setWriter(doc);


//		poCtrl.setJsFunction_OnWordDataRegionClick("OnWordDataRegionClick()");

		//  获取文件
//		String rootPath = request.getServletContext().getRealPath("")+"/template/";
		String rootPath = request.getSession().getServletContext().getRealPath("template/");
		String fileName1 = FileUtil.getFileName(rootPath+templateNo);
		if(StringUtils.isBlank(fileName1)){
			return null;
		}
		poCtrl.setSaveFilePage("savefile.do?templateNo="+templateNo);
		if(SysUtil.IsLinux()){
			String linuxFilePath="file://"+rootPath+templateNo+"/"+fileName1;
			System.out.print("templateEdit.do,编辑模板，模板路径为-------："+linuxFilePath);
			poCtrl.webOpen(linuxFilePath, OpenModeType.docNormalEdit, "zhangsan");
		}else {
			poCtrl.webOpen(request.getSession().getServletContext().getRealPath("template/")+templateNo+"/"+fileName1, OpenModeType.docNormalEdit, "zhangsan");
		}

		request.setAttribute("poCtrl",poCtrl);
		return modelAndView;
	}

	@RequestMapping("quotetemplateEdit")
	public ModelAndView quoteTemplate(HttpServletRequest request, HttpServletResponse response){
		String quoteTemplateNo=request.getParameter("templateNo");
		String templateNo = request.getParameter("quoteTemplateNo");

		TemplateParamModel templateParamModel=templateParamMapper.getTemplateByTemplateNo(quoteTemplateNo);
		if(templateParamModel==null){
			return null;
		}
		List<TemplateParamEntryModel> templateParamEntryModels=templateParamMapper.getParamEntrys(templateParamModel.getFid());

		//自定义变量
		List<TemplateParMarkDto> templateParMarkDtos=new ArrayList<>();
		for(TemplateParamEntryModel templateParamEntryModel:templateParamEntryModels){
			p:
			switch (templateParamEntryModel.getParamType()){
				case "2":
					//用户还未使用的自定义变量
					TemplateParMarkDto templateParMarkDto=new TemplateParMarkDto();
					templateParMarkDto.setName(templateParamEntryModel.getName());
					templateParMarkDto.setFentryId(templateParamEntryModel.getFentryId());
					templateParMarkDtos.add(templateParMarkDto);
					break p;
				//下拉选项
				case "3":
					//获取下拉选项信息
					TemplateParMarkDto templateParOptionDto=new TemplateParMarkDto();
					templateParOptionDto.setName(templateParamEntryModel.getName());
					templateParOptionDto.setFentryId(templateParamEntryModel.getFentryId());
					templateParMarkDtos.add(templateParOptionDto);
					break p;
			}
		}
		List<ArticleInfoDto> articleInfoDtos = templateParamService.queryAtricleDtoByFid(String.valueOf(templateParamModel.getFid()));
		ModelAndView modelAndView = new ModelAndView("templateQuote");

		//定义系统变量，使用dataTag
		WordDocument doc = new WordDocument();

		List<String> paramBooks=new ArrayList<>();
		//定义自定义变量数据区域
		if(templateParMarkDtos!=null && templateParMarkDtos.size()!=0){
			for(TemplateParMarkDto templateParMarkDto:templateParMarkDtos){

				List<String> bookMarks=templateParamMapper.queryParamBookMarkBy("1",templateParMarkDto.getFentryId());
				for(String bookMark:bookMarks){
					DataRegion dataRegion =doc.openDataRegion(bookMark);
					dataRegion.getShading().setBackgroundPatternColor(Color.YELLOW);
					dataRegion.setEditing(false);
					paramBooks.add(bookMark);
				}
				templateParMarkDto.setBookMarks(bookMarks);
			}
		}
//
		if(articleInfoDtos!=null && articleInfoDtos.size()!=0 ){
			for(ArticleInfoDto articleInfoDto:articleInfoDtos){
				List<String> bookMarks=templateParamMapper.queryParamBookMarkBy("2",articleInfoDto.getFarticleId());
				for(String bookMark:bookMarks){
					DataRegion dataRegion =doc.openDataRegion(bookMark);
					dataRegion.getShading().setBackgroundPatternColor(Color.YELLOW);
					dataRegion.setEditing(false);
					paramBooks.add(bookMark);
				}
				articleInfoDto.setBookMarks(bookMarks);
			}
		}

		PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
		poCtrl.addCustomToolButton("保存", "Save()", 1);
		poCtrl.addCustomToolButton("保存并退出", "SaveAndExit()", 2);
		poCtrl.setServerPage(request.getContextPath()+"/poserver.zz");

		TemplateParamModel templateParamModell=templateParamMapper.getTemplateByTemplateNo(templateNo);
		if(templateParamModel==null){
			return null;
		}

		//设置表格可编辑
		List<BooKMarkModel> booKMarkModels=templateParamMapper.queryBkByFid(templateParamModell.getFid());
		for(BooKMarkModel booKMarkModel:booKMarkModels){
			DataRegion dataRegion =doc.openDataRegion(booKMarkModel.getBookMark());
			if(!paramBooks.contains(booKMarkModel.getBookMark())){
				dataRegion.setEditing(true);
				dataRegion.getShading().setBackgroundPatternColor(Color.white);
			}
		}

//		doc.getWaterMark().setText("WENS");
//		poCtrl.setTheme(ThemeType.Office2007);
//		poCtrl.setBorderStyle(BorderStyleType.BorderThin);
		poCtrl.setWriter(doc);

		poCtrl.setJsFunction_OnWordDataRegionClick("OnWordDataRegionClick()");
		//  获取文件
		String rootPath = request.getServletContext().getRealPath("")+"/template/";
		String fileName1 = FileUtil.getFileName(rootPath+templateNo);
		if(StringUtils.isBlank(fileName1)){
			return null;
		}
		poCtrl.setSaveFilePage("savefile.do?templateNo="+templateNo);
		if(SysUtil.IsLinux()){
			String linuxFilePath="file://"+rootPath+templateNo+"/"+fileName1;
			System.out.print("templateEdit.do,编辑模板，模板路径为-------："+linuxFilePath);
			poCtrl.webOpen(linuxFilePath, OpenModeType.docSubmitForm, "zhangsan");
		}else {
			poCtrl.webOpen("template/"+templateNo+"/"+fileName1, OpenModeType.docSubmitForm, "zhangsan");
		}

		poCtrl.setOfficeToolbars(false);
		poCtrl.setTagId("PageOfficeCtrl1");
		request.setAttribute("poCtrl",poCtrl);
		modelAndView.addObject("parentFid",templateParamModel.getFid());
		modelAndView.addObject("fid",templateParamModell.getFid());
		return modelAndView;
	}



	@RequestMapping("openData")
	public void openData(HttpServletRequest request, HttpServletResponse response,String mark){
		PageOfficeCtrl pageOfficeCtrl= (PageOfficeCtrl) request.getAttribute("poCtrl");
//		doc.getTemplate().defineDataRegion("PO_"+ PingYingUtils.getPinYinHeadChar(mark), mark);
//		doc.openDataRegion("PO_"+ PingYingUtils.getPinYinHeadChar(mark));
		pageOfficeCtrl.getHtmlCode("PageOfficeCtrl1");
	}


	@RequestMapping(value = "templateGet" , method = RequestMethod.POST)
	@ResponseBody
	public JSONObject templateGet(HttpServletRequest request, HttpServletResponse response){
		String fid=request.getParameter("fid");
		List<TemplateParamEntryModel> templateParamEntryModels=templateParamMapper.getParamEntrys(Integer.valueOf(fid));

		//系统变量
		List<String> tags=new ArrayList<>();
		//自定义变量
		List<TemplateParMarkDto> templateParMarkDtos=new ArrayList<>();
		for(TemplateParamEntryModel templateParamEntryModel:templateParamEntryModels){
			p:
			switch (templateParamEntryModel.getParamType()){
				//系统变量
				case "1":
					tags.add(templateParamEntryModel.getName());
					//自定义变量
					break p;
				case "2":
					//用户还未使用的自定义变量
					TemplateParMarkDto templateParMarkDto=new TemplateParMarkDto();
					templateParMarkDto.setName(templateParamEntryModel.getName());
					templateParMarkDto.setLength(templateParamEntryModel.getLength());
					templateParMarkDto.setType(templateParamEntryModel.getType());
					templateParMarkDto.setFentryId(templateParamEntryModel.getFentryId());
					templateParMarkDto.setParamType(templateParamEntryModel.getParamType());

					templateParMarkDtos.add(templateParMarkDto);
					break p;
				//下拉选项
				case "3":
					//获取下拉选项信息
					TemplateParMarkDto templateParMarkDtoo=new TemplateParMarkDto();
					templateParMarkDtoo.setName(templateParamEntryModel.getName());
					templateParMarkDtoo.setType(templateParamEntryModel.getType());
					templateParMarkDtoo.setParamType(templateParamEntryModel.getParamType());
					templateParMarkDtoo.setFentryId(templateParamEntryModel.getFentryId());

					//获取值信息
					List<TemplateParamOptionModel> templateParamOptionModels=templateParamMapper.getOptions(templateParamEntryModel.getFentryId());
					if(templateParamOptionModels!=null && templateParamOptionModels.size()!=0){

						List<String> options=new ArrayList<>();
						for(TemplateParamOptionModel templateParamOptionModel:templateParamOptionModels){
							options.add(templateParamOptionModel.getFoptionValue());//添加选项值
						}
						templateParMarkDtoo.setOptionValues(options);
					}
					templateParMarkDtos.add(templateParMarkDtoo);
					break p;
			}
		}

		List<ArticleInfoDto> articleInfoDtos = templateParamService.queryAtricleDtoByFid(fid);
		JSONObject modelAndView = new JSONObject();
		modelAndView.put("tags",tags);
		modelAndView.put("fid",fid);
		modelAndView.put("templateParMarkDtos",templateParMarkDtos);//自定义变量
		modelAndView.put("articleInfoDtos",articleInfoDtos);//条款


		if(templateParMarkDtos!=null && templateParMarkDtos.size()!=0){
			for(TemplateParMarkDto templateParMarkDto:templateParMarkDtos){
				List<String> bookMarks=templateParamMapper.queryParamBookMarkBy("1",templateParMarkDto.getFentryId());
				templateParMarkDto.setBookMarks(bookMarks);
//				templateParMarkDto.setMarkName("PO_"+ PingYingUtils.getPinYinHeadChar(templateParMarkDto.getName()));
			}
		}

		if(articleInfoDtos!=null && articleInfoDtos.size()!=0){
			for(ArticleInfoDto articleInfoDto:articleInfoDtos){
				List<String> bookMarks=templateParamMapper.queryParamBookMarkBy("2",articleInfoDto.getFarticleId());
				articleInfoDto.setBookMarks(bookMarks);
//				articleInfoDto.setMarkName("PO_"+ PingYingUtils.getPinYinHeadChar(articleInfoDto.getName()));
			}
		}
		return modelAndView;
	}

	//添加变量，req 需要传入模板表对应fid，entry需要包含变量类型
	@RequestMapping("addParam")
	public @ResponseBody String addParam(HttpServletRequest request, HttpServletResponse response, @RequestBody String req){
		JSONObject jsonObject=(JSONObject) JSONObject.parse(req);
		String fid =jsonObject.getString("fid");
		TemplateParamModel templateParamModel=templateParamMapper.getModelById(fid);
		if(templateParamModel==null){
			return "fail";
		}
		JSONObject objperson = jsonObject.getJSONObject("entry");
		TemplateParamEntryModel templateParamEntryModel= JSON.toJavaObject(objperson, TemplateParamEntryModel.class);

		List<TemplateParamOptionModel> templateParamOptionModels=new ArrayList<>();
		//获取下拉信息
		if(jsonObject.getJSONArray("options")!=null){
			templateParamOptionModels= jsonObject.getJSONArray("options").toJavaList(TemplateParamOptionModel.class);
		}
		List<String> bookMark=new ArrayList<>();
		templateParamService.addParam(fid,templateParamEntryModel,templateParamOptionModels,bookMark);
		return "success";
	}

	//删除变量 req 需要传入模板表对应fid，entry需要包含变量类型
	@RequestMapping("removeParam")
	public @ResponseBody String removeParam(HttpServletRequest request, HttpServletResponse response,@RequestParam String fentryId){
		TemplateParamEntryModel templateParamEntryModel=templateParamMapper.getByFentryId(Integer.valueOf(fentryId));
		if(templateParamEntryModel==null){
			return "fail";
		}
		if("3".equals(templateParamEntryModel.getParamType())){
			templateParamMapper.removeParamEntryOptionsById(templateParamEntryModel.getFentryId());
		}
		templateParamMapper.removeParamEntryById(templateParamEntryModel.getFentryId());
		return "success";
	}


	//添加变量，req 需要传入模板表对应fid，entry需要包含变量类型
	@RequestMapping("addArticle")
	public @ResponseBody String addArticle(HttpServletRequest request, HttpServletResponse response, @RequestBody String req){
		JSONObject jsonObject=(JSONObject) JSONObject.parse(req);
		String fid =jsonObject.getString("fid");
		TemplateParamModel templateParamModel=templateParamMapper.getModelById(fid);
		if(templateParamModel==null){
			return "fail";
		}
		JSONObject articleInfo = jsonObject.getJSONObject("entry");

		TemplateArticleModel templateArticleModel=new TemplateArticleModel();
		templateArticleModel.setName(articleInfo.getString("name"));
		templateArticleModel.setFid(Integer.valueOf(fid));

		//保存条款信息
		templateParamMapper.saveArticle(templateArticleModel);
		JSONArray articles=articleInfo.getJSONArray("articles");
		List<ArticleEntryRelationModel> articleEntryRelationModels=new ArrayList<>();
		if(articles!=null&&articles.size()!=0){
			for(Object o:articles){
				ArticleContentModel articleContentModel=new ArticleContentModel();
				JSONObject articleContent=(JSONObject)o;
				//设置条款信息
				articleContentModel.setArticle(articleContent.getString("content"));
				articleContentModel.setFarticleId(templateArticleModel.getFarticleId());
				//保存条款选项信息
				templateParamMapper.saveArticleContent(articleContentModel);
				JSONArray fentryIds=(JSONArray) articleContent.get("fentryIds");

				//如果fentryIds变量不为空，则保存条款和变量信息
				if(fentryIds!=null && fentryIds.size()!=0){
					for(int i=0;i< fentryIds.size();i++){
						ArticleEntryRelationModel articleEntryRelationModel=new ArticleEntryRelationModel();
						articleEntryRelationModel.setFentryId(Integer.valueOf((String) fentryIds.get(i)));
						articleEntryRelationModel.setFarticleContentId(templateArticleModel.getFarticleId());
						articleEntryRelationModels.add(articleEntryRelationModel);
					}

				}
			}
			List<ArticleEntryRelationModel> result = new LinkedList<>();
//			for(ArticleEntryRelationModel articleEntryRelationModel:articleEntryRelationModels){
//				if(result.size()==0){
//					result.add(articleEntryRelationModel);
//				}else {
//					for(ArticleEntryRelationModel articleEntryRelationModel1:result){
//						if(articleEntryRelationModel.getFentryId()!=articleEntryRelationModel1.getFentryId()
//						&&articleEntryRelationModel.getFarticleContentId()!=articleEntryRelationModel1.getFarticleContentId()){
//							result.add(articleEntryRelationModel1);
//						}
//					}
//				}
//			}

			if(articleEntryRelationModels.size()!=0){
				templateParamMapper.saveArticleRelations(articleEntryRelationModels);
			}
		}

		return "success";
	}

	//添加变量，req 需要传入模板表对应fid，entry需要包含变量类型
	@RequestMapping("removeArticle")
	public void removeArticle(HttpServletRequest request, HttpServletResponse response, @RequestParam String farticleId){
		TemplateArticleModel templateArticleModel=templateParamMapper.queryArticleById(Integer.valueOf(farticleId));
		if(templateArticleModel==null){
			return;
		}

		List<ArticleContentModel>  articleContentModels= templateParamMapper.queryContentsById(templateArticleModel.getFarticleId());
		if(articleContentModels!=null&&articleContentModels.size()!=0){
			for(ArticleContentModel articleContentModel:articleContentModels){
				List<ArticleEntryRelationModel> articleEntryRelationModels=templateParamMapper.queryArticleContentsById(articleContentModel.getFarticleContentId());
				if(articleEntryRelationModels!=null&&articleEntryRelationModels.size()!=0){
					templateParamMapper.removeArticleContentsByIds(articleContentModel.getFarticleContentId());
				}
			}
			templateParamMapper.removeContentsById(articleContentModels.get(0).getFarticleId());
		}
		templateParamMapper.removeArticleById(templateArticleModel.getFarticleId());
	}

	//添加单个书签信息，并且返回fid
	@RequestMapping("addPO")
	public String addPO(HttpServletRequest request, HttpServletResponse response, @RequestBody String req){
		JSONObject jsonObject=(JSONObject) JSONObject.parse(req);
		String fentryId =jsonObject.getString("fentryId");

		JSONObject object = jsonObject.getJSONObject("entry");
		//获取标签信息
		TemplateParamMarkInfoModel templateParamMarkInfoModel= JSON.toJavaObject(object, TemplateParamMarkInfoModel.class);
		templateParamMarkInfoModel.setFentryId(Integer.valueOf(fentryId));
		templateParamService.addPO1(templateParamMarkInfoModel);
		return "success";
	}

	//批量添加书签信息
	@RequestMapping("addPOs")
	public String addPOs(HttpServletRequest request, HttpServletResponse response, @RequestBody String req){
		JSONObject jsonObject=(JSONObject) JSONObject.parse(req);
		String fentryId =jsonObject.getString("fentryId");
		//获取标签信息
		List<TemplateParamMarkInfoModel> templateParamMarkInfoModels= jsonObject.getJSONArray("markInfos").toJavaList(TemplateParamMarkInfoModel.class);
		templateParamService.addPO(fentryId,templateParamMarkInfoModels);
		return "success";
	}

	@RequestMapping("removePO")
	public String delPO(HttpServletRequest request, HttpServletResponse response, @RequestBody String fid){
		//获取标签信息
		templateParamService.removePO(fid);
		return "success";
	}

	@RequestMapping("pdf")
	public ModelAndView pdf(HttpServletRequest request, HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView("/FileMakerPDF");
		return modelAndView;
	}


	//文本变量信息变量查询接口，传入fentryId
	@RequestMapping("paramEntry")
	@ResponseBody
	public  JSONObject paramEntry(HttpServletRequest request, HttpServletResponse response){
		String fentryId=request.getParameter("fentryId");
		TemplateParamEntryModel templateParamEntryModel=templateParamMapper.getByFentryId(Integer.valueOf(fentryId));
		JSONObject modelAndView=new JSONObject();
		modelAndView.put("templateParamEntryModel",templateParamEntryModel);
		return modelAndView;
	}

	//    //下拉变量信息变量查询接口，传入fentryId
	@RequestMapping("paramEntryOption")
	@ResponseBody
	public JSONObject paramEntryOption(HttpServletRequest request, HttpServletResponse response){
		String fentryId=request.getParameter("fentryId");
		TemplateParOptionDto templateParOptionDto=templateParamService.queryEntryOptionInfoById(Integer.valueOf(fentryId));
		JSONObject modelAndView=new JSONObject();
		modelAndView.put("templateParOptionDto",templateParOptionDto);
		return modelAndView;
	}

	@RequestMapping("compare")
	public ModelAndView compare(HttpServletRequest request, HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView("/Compare");
		return modelAndView;
	}


	// {"回收价格","品种信息","物种"}
	@RequestMapping("queryByNameAndFid")
	@ResponseBody
	public JSONObject queryByNameAndFid(HttpServletRequest request, HttpServletResponse response){
		JSONObject jsonObject=new JSONObject();
		String fid=request.getParameter("fid");
		String names = request.getParameter("name");
		List<TemplateParamEntryModel> templateParamEntryModels=templateParamMapper.getParamEntrys(Integer.valueOf(fid));
		for(TemplateParamEntryModel templateParamEntryModel:templateParamEntryModels){
			if(names.equals(templateParamEntryModel.getName())){
				if("2".equals(templateParamEntryModel.getParamType())){
					jsonObject.put("templateParamEntryModel",templateParamEntryModel);
					jsonObject.put("paramType","2");
					return jsonObject;
				}
				if("3".equals(templateParamEntryModel.getParamType())){
					//获取值信息
					List<TemplateParamOptionModel> templateParamOptionModels=templateParamMapper.getOptions(templateParamEntryModel.getFentryId());
					if(templateParamOptionModels!=null && templateParamOptionModels.size()!=0){

						List<String> options=new ArrayList<>();
						for(TemplateParamOptionModel templateParamOptionModel:templateParamOptionModels){
							options.add(templateParamOptionModel.getFoptionValue());//添加选项值
						}
						jsonObject.put("paramType","3");
						jsonObject.put("names",templateParamEntryModel.getNames());
						jsonObject.put("fentryId",templateParamEntryModel.getFentryId());
						jsonObject.put("options",options);
						return jsonObject;
					}
				}
			}
		}

		List<TemplateArticleModel> templateArticleModels=templateParamMapper.queryArticleByFidAndName(fid,names);
		if(templateArticleModels!=null||templateArticleModels.size()!=0){
			jsonObject.put("templateArticleModels",templateArticleModels);
		}
		return jsonObject;
	}

	// {"回收价格","品种信息","物种"}
	@RequestMapping("queryByNamesAndFid")
	@ResponseBody
	public JSONObject queryByNamesAndFid(HttpServletRequest request, HttpServletResponse response){
		JSONObject jsonObject=new JSONObject();
		String fid=request.getParameter("fid");
		String nameDto = request.getParameter("names");
		String[] names=nameDto.split(",");
		List<TemplateParamEntryModel> templateParamEntryModels=templateParamMapper.getParamEntrys(Integer.valueOf(fid));
		List<TemplateparamMarkAndOptionModel> templateparamMarkAndOptionModels = new ArrayList<>();
//		List<TemplateParOptionDto> templateParOptionDtos=new ArrayList<>();
//		List<TemplateParMarkDto> templateParMarkDtos=new ArrayList<>();
		for(String name:names){
			for(TemplateParamEntryModel templateParamEntryModel:templateParamEntryModels) {
				if (templateParamEntryModel.getName().equals(name)) {
					TemplateparamMarkAndOptionModel templateparamMarkAndOptionModel = new TemplateparamMarkAndOptionModel();
					templateparamMarkAndOptionModel.setName(templateParamEntryModel.getName());
					templateparamMarkAndOptionModel.setType(templateParamEntryModel.getType());
					templateparamMarkAndOptionModel.setParamType(templateParamEntryModel.getParamType());
					templateparamMarkAndOptionModel.setLength(templateParamEntryModel.getLength());
					templateparamMarkAndOptionModel.setFentryId(templateParamEntryModel.getFentryId());
					List<TemplateParamOptionModel> templateParamOptionModels = templateParamMapper.getOptions(templateParamEntryModel.getFentryId());
					if (templateParamOptionModels != null && templateParamOptionModels.size() != 0) {
						List<String> options = new ArrayList<>();
						for (TemplateParamOptionModel templateParamOptionModel : templateParamOptionModels) {
							options.add(templateParamOptionModel.getFoptionValue());//添加选项值
						}
						templateparamMarkAndOptionModel.setValue(options);
					}
					templateparamMarkAndOptionModels.add(templateparamMarkAndOptionModel);
				}
			}
		}
		jsonObject.put("templateparamMarkAndOptionModels",templateparamMarkAndOptionModels);
		return jsonObject;
	}

	@RequestMapping("queryByArticleId")
	@ResponseBody
	public JSONObject   queryByArticleId(HttpServletRequest request, HttpServletResponse response){
		JSONObject jsonObject=new JSONObject();
		String articleId=request.getParameter("articleId");
		if(StringUtils.isEmpty(articleId)){
			return null;
		}
		TemplateArticleModel templateArticleModel = templateParamMapper.queryArticleById(Integer.valueOf(articleId));

		if(templateArticleModel==null){
			return null;
		}

		List<ArticleContentModel> articleContentModels=templateParamMapper.queryContentsById(Integer.valueOf(articleId));

		List<String> contents=new ArrayList<>();
		for(ArticleContentModel articleContentModel:articleContentModels){
			contents.add(articleContentModel.getArticle());
		}
		jsonObject.put("name",templateArticleModel.getName());
		jsonObject.put("contents",contents);
		return jsonObject;
	}

	@RequestMapping("addBookMark")
	public void addBookMark(HttpServletRequest request){
		String bookMarks = request.getParameter("bookMarks");
		String fid=request.getParameter("fid");
		String[] bookMark=bookMarks.split(",");
		List<TemplateParamEntryModel> templateParamEntryModels=templateParamMapper.getParamEntrys(Integer.valueOf(fid));
		List<ArticleInfoDto> articleInfoDtos = templateParamService.queryAtricleDtoByFid(fid);
		List<String> marks=new ArrayList<>();
		for(ArticleInfoDto articleInfoDto:articleInfoDtos){
			String poName="PO_"+ PingYingUtils.getPinYinHeadChar(articleInfoDto.getName());
			marks.add(poName);
		}
		for(TemplateParamEntryModel templateParamEntryModel:templateParamEntryModels){
			String poName="PO_"+ PingYingUtils.getPinYinHeadChar(templateParamEntryModel.getName());
			marks.add(poName);
		}

		List<BooKMarkModel> bks=new ArrayList<>();
		for(String bk:bookMark){
			if(!marks.contains(bk)){
				BooKMarkModel booKMarkModel=new BooKMarkModel();
				booKMarkModel.setBookMark(bk);
				booKMarkModel.setFid(fid);
				bks.add(booKMarkModel);
			}
		}

		templateParamMapper.removeBkByFid(fid);
		templateParamMapper.addBookMarks(bks);


	}

	@RequestMapping("docRevisionOnly")
	@ResponseBody
	public ModelAndView docRevisionOnly(HttpServletRequest request ,HttpServletResponse response){
		String templateNo=request.getParameter("templateNo");

		TemplateParamModel templateParamModel=templateParamMapper.getTemplateByTemplateNo(templateNo);
		if(templateParamModel==null){
			return null;
		}
		List<TemplateParamEntryModel> templateParamEntryModels=templateParamMapper.getParamEntrys(templateParamModel.getFid());

		//系统变量
		String name = null;
		//自定义变量
		for(TemplateParamEntryModel templateParamEntryModel:templateParamEntryModels){
			name = templateParamEntryModel.getName();
		}


		ModelAndView modelAndView = new ModelAndView("docRevisionOnly");
		PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
		poCtrl.setServerPage(request.getContextPath()+"/poserver.zz"); //此行必须
		poCtrl.addCustomToolButton("保存", "Save()", 1);
		poCtrl.addCustomToolButton("保存并关闭", "SaveAndExit()", 2);
		poCtrl.addCustomToolButton("显示/隐藏痕迹", "Show_HidRevisions", 5);
		poCtrl.addCustomMenuItem("接受所有修订", "AcceptAllRevisions", true);
		poCtrl.setOfficeToolbars(false);//隐藏office工具栏
		//打开文件
		String rootPath = request.getServletContext().getRealPath("")+"/template/";
		String fileName1 = FileUtil.getFileName(rootPath+templateNo);
		poCtrl.setSaveFilePage("savefile.do?templateNo="+templateNo);
		if(SysUtil.IsLinux()){
			String linuxFilePath="file://"+rootPath+templateNo+"/"+fileName1;
//			System.out.print("templateEdit.do,编辑模板，模板路径为-------："+linuxFilePath);
			poCtrl.webOpen(linuxFilePath,OpenModeType.docRevisionOnly, name);
		}else {
			poCtrl.webOpen("template/"+templateNo+"/"+fileName1,OpenModeType.docRevisionOnly,name);
		}
		request.setAttribute("poCtrl",poCtrl);
		return modelAndView;
	}

	@RequestMapping("addParamBookMarks")
	@ResponseBody
	public void addParamBookMarks(HttpServletRequest request ){
		String bookMark=request.getParameter("bookMark");
		Integer fparamId=Integer.valueOf(request.getParameter("fparamId"));
		Integer fid=Integer.valueOf(request.getParameter("fid"));
		String type=request.getParameter("type");
		ParamBookMarkModel paramBookMarkModel=new ParamBookMarkModel();
		List<ParamBookMarkModel> paramBookMarkModels=new ArrayList<>();
		paramBookMarkModel.setBookMark(bookMark);
		paramBookMarkModel.setFparamId(fparamId);
		paramBookMarkModel.setType(type);
		paramBookMarkModel.setFid(fid);
		paramBookMarkModels.add(paramBookMarkModel);
		templateParamMapper.saveParamBookMarkModels(paramBookMarkModels);
	}

	@RequestMapping("searchParam")
	@ResponseBody
	public List<String>  searchParam(HttpServletRequest request ){
		String fparamId=request.getParameter("fparamId");
		String type=request.getParameter("type");
		List<String> bks=templateParamMapper.queryParamBookMarkBy(type,Integer.valueOf(fparamId));
		return bks;
	}

	@RequestMapping("searchParamByFid")
	@ResponseBody
	public List<String>  searchParamByFid(HttpServletRequest request ){
		String fid=request.getParameter("fid");
		List<String> bks=templateParamMapper.queryParamBookMarkByFid(Integer.valueOf(fid));
		return bks;
	}

	@RequestMapping("updateTemplateStatus")
	@ResponseBody
	public void templateStatus(HttpServletRequest request){
		String templateNo=request.getParameter("templateNo");
		String status=request.getParameter("status");
		templateParamMapper.updateTemplateStatus(templateNo,status);
	}

	@RequestMapping("queryTemplateStatus")
	@ResponseBody
	public String queryTemplateStatus(HttpServletRequest request){
		String fid=request.getParameter("fid");
		TemplateParamModel templateParamModel=templateParamMapper.getModelById(fid);
		return templateParamModel.getTemplateType();
	}

	@RequestMapping("deleteBkByFid")
	@ResponseBody
	public void deleteBkByFid(HttpServletRequest request){
		String bookMarks = request.getParameter("bookMarks");
		String fid=request.getParameter("fid");
		String[] bookMarkss=bookMarks.split(",");
		templateParamMapper.batchdeleteBks(bookMarkss,Integer.valueOf(fid));
	}

	@RequestMapping("queryOpenStatus")
	@ResponseBody
	public String queryOpenStatus(HttpServletRequest request){
		String templateNo=request.getParameter("templateNo");
		return templateParamMapper.queryOpenStatus(templateNo);
	}

	@RequestMapping("updateOpenStatus")
	@ResponseBody
	public void updateOpenStatus(HttpServletRequest request){
		String openStatus = request.getParameter("openStatus");
		String fid=request.getParameter("fid");
		templateParamMapper.updateOpenStatus(openStatus,fid);
	}

	@RequestMapping("updateParamInfo")
	@ResponseBody
	public void updateParamInfo(HttpServletRequest request,@RequestBody String req){
		JSONObject jsonObject=(JSONObject) JSONObject.parse(req);
		String optionValues = jsonObject.getString("optionValues");
		String type=jsonObject.getString("type");
		String name=jsonObject.getString("name");
		String fentryId=jsonObject.getString("fentryId");
		String length=jsonObject.getString("length");
		String paramType=jsonObject.getString("paramType");
		templateParamService.modifyParamInfo(optionValues,type,name,fentryId,length,paramType);
	}

	@RequestMapping("editOrNot")
	@ResponseBody
	public Boolean editOrNot(HttpServletRequest request){
		String fentryId=request.getParameter("fentryId");
		List<ArticleEntryRelationModel> articleEntryRelationModels=templateParamMapper.queryTemplateArticleContent(fentryId);
		if(articleEntryRelationModels!=null&&articleEntryRelationModels.size()!=0){
			return false;
		}
		return true;
	}

	@RequestMapping("removeTemplateWM")
	@ResponseBody
	public Boolean removeTemplateWaterMark(HttpServletRequest request){
		String ctxPath = request.getSession().getServletContext().getRealPath("/")+"template/";

		List<String> files=getFiles(ctxPath);
		for(String path:files){
			WaterMarkUtil.removeWM(path);
		}
		return true;
	}

	/**
	 * @Author：
	 * @Description：获取某个目录下所有直接下级文件，不包括目录下的子目录的下的文件，所以不用递归获取
	 * @Date：
	 */
	public static List<String> getFiles(String path) {
		List<String> files = new ArrayList<String>();
		File file = new File(path);
		File[] tempList = file.listFiles();

		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				files.add(tempList[i].toString());
				//文件名，不包含路径
				//String fileName = tempList[i].getName();
			}
			if (tempList[i].isDirectory()) {
				//这里就不递归了，
				String path1 = tempList[i].getPath();
				files.addAll(getFiles(path1));
			}
		}
		return files;
	}

	public static  void  main(String[] args){
		System.out.println("{".equals("{"));
	}
}
