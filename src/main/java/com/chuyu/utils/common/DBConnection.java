package com.chuyu.utils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈俊昊 on 2016/7/7
 */
public class DBConnection {


    private static Logger logger = LoggerFactory.getLogger(DBConnection.class);
    private static Map<String,DataSource> dataSourceMap = new HashMap<>();


    /**
     * 得到conn
     * @param dbID
     * @return
     */
    public static Connection getConnPrxxool(String dbID) {
        Connection conn =null;
        try {
            Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
            conn= DriverManager.getConnection(dbID);
        } catch (ClassNotFoundException e) {
            System.out.println("proxool 连接方式出错"+e);
            logger.error("proxool 连接方式出错",e);
            return null;
        } catch (SQLException e) {
            System.out.println("proxool 连接方式出错"+e);
            logger.error("proxool 连接方式出错",e);
            return null;
        }
        return conn;
    }

    /**
     * 得到conn
     * @param dbID
     * @return
     */
    public static Connection getConn(String dbID) {

        synchronized(DBConnection.class)
        {
            if(!dataSourceMap.containsKey(dbID))
            {
                ApplicationContext ctx = new ClassPathXmlApplicationContext("dataSource.xml");
                dataSourceMap.put(dbID,ctx.getBean(dbID,DataSource.class));
            }
        }
        Connection conn= null;
        try {
            conn = dataSourceMap.get(dbID).getConnection();
        } catch (SQLException e) {
            logger.error("[连接数据库出错]"+e.getMessage());
            e.printStackTrace();
        }

        return conn;
    }
}
