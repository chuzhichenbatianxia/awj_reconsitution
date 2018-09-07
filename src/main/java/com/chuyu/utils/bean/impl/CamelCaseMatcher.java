package com.chuyu.utils.bean.impl;

import com.chuyu.utils.bean.Matcher;
import com.chuyu.utils.data.StringTools;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 驼峰转换的匹配器
 */
public class CamelCaseMatcher implements Matcher {

    private static Logger logger = LoggerFactory.getLogger(CamelCaseMatcher.class);
	@Override
	public boolean match(String columnName, String propertyName) {
		boolean flag=false;
		if(StringUtils.isBlank(columnName)){
			flag=false;
		}
		String camelColumnName= StringTools.getCamelCase(columnName);
		camelColumnName= StringUtils.trim(camelColumnName);
		if(camelColumnName.equals(propertyName)){
			flag=true;
		}else{
			logger.error(" camelColumnName   "+camelColumnName+" propertyName "+propertyName);	
		}
		
		return flag;
	}

}
