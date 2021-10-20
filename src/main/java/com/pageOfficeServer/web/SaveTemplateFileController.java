package com.pageOfficeServer.web;


import com.pageOfficeServer.util.FileUtil;
import com.zhuozhengsoft.pageoffice.FileSaver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Controller
public class SaveTemplateFileController extends HttpServlet {
	private static final long serialVersionUID = -758686623642845302L;
	@RequestMapping("savefile")
	 public  void  savefile(HttpServletRequest request, HttpServletResponse response){
		FileSaver fs = new FileSaver(request,response);
		String templateNo=request.getParameter("templateNo");
		String rootPath = request.getServletContext().getRealPath("")+"/template/"+templateNo;
		String fileName=FileUtil.getFileName(rootPath);
		String path=request.getSession().getServletContext().getRealPath("template")  + "/"+templateNo+"/"+ fileName;
		fs.saveToFile(path);
		fs.close();
	}

	@RequestMapping("saveContractfile")
	public  void  saveContractfile(HttpServletRequest request, HttpServletResponse response){
		FileSaver fs = new FileSaver(request,response);
		String contractNo=request.getParameter("contractNo");

		//合同文件以合同名称命名文件
		File contractFile=new File(request.getSession().getServletContext().getRealPath("") +"/contract");
		if(!contractFile.exists()){
			contractFile.mkdir();
		}

		//合同文件以合同名称命名文件
		File targetFile=new File(request.getSession().getServletContext().getRealPath("contract")+ "/"+ contractNo+".pdf");
		if(!targetFile.exists()){//4.判断真实路径下是否存在filename文件
			try {
				targetFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		String path=request.getSession().getServletContext().getRealPath("contract")  + "/"+ contractNo+".pdf";
		fs.saveToFile(path);
		fs.close();
	}

	@RequestMapping("savepdf")
	public  void  savepdf(HttpServletRequest request, HttpServletResponse response){
		FileSaver fs = new FileSaver(request, response);
		String fileName = "\\maker" + fs.getFileExtName();
		System.out.print(request.getSession().getServletContext().getRealPath("doc") +"/"+ fileName);
		fs.saveToFile(request.getSession().getServletContext().getRealPath("doc") +"/"+ fileName);
		fs.close();
	}

}
