package com.pageOfficeServer.loginkingdee;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * access_token监听器
 * 
 * @author LiangMingHui
 *
 */
public class AccessTokenListener implements ServletContextListener {


	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
//		AuthUtil.refreshCommonAccessToken();
	}
}
