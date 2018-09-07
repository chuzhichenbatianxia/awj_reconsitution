package com.chuyu.utils.bean;


import com.chuyu.utils.bean.impl.CamelCaseMatcher;
import org.apache.commons.dbutils.BeanProcessor;

import java.beans.PropertyDescriptor;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;

public class CamelProcessor extends BeanProcessor {
	private Matcher matcher;

	public CamelProcessor() {
		matcher = new CamelCaseMatcher();
	}
	public CamelProcessor(Matcher matcher){
        this.matcher = matcher;
    }

	public Matcher getMatcher() {
		return matcher;
	}

	public void setMatcher(Matcher matcher) {
		this.matcher = matcher;
	}
	private String getPropertyName(String columnName){
	    StringBuilder sb = new StringBuilder();
	    boolean match = false;
	    for (int i=0; i<columnName.length(); i++){
	        char ch = columnName.charAt(i);
	        if (match && ch>=97 && ch<=122) {
				ch -= 32;
			}
	        if (ch!='_'){
	            match = false;
	            sb.append(ch);  
	        }else{              
	            match = true; 
	        }           
	    }
	    return sb.toString();
	}

	/**
	 * 重写BeanProcessor的实现,使用策略模式
	 */
	@Override
    protected int[] mapColumnsToProperties(ResultSetMetaData rsmd,
            PropertyDescriptor[] props) throws SQLException {
    	 int cols = rsmd.getColumnCount();
    	    int columnToProperty[] = new int[cols + 1];
    	    Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);
    	 
    	    for (int col = 1; col <= cols; col++) {
    	        String columnName = getPropertyName(rsmd.getColumnName(col));
    	        
    	        for (int i = 0; i < props.length; i++) {
    	            if (columnName.equalsIgnoreCase(props[i].getName())) {
    	                columnToProperty[col] = i;
    	                break;
    	            }
    	        }
    	    }
    	    
    	    return columnToProperty;
    }
}
