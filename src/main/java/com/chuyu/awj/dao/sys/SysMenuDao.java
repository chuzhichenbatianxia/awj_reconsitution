package com.chuyu.awj.dao.sys;

import com.chuyu.awj.config.DbConf;
import com.chuyu.awj.model.sys.SysMenu;
import com.chuyu.utils.common.RecordMap;
import com.chuyu.utils.data.DaoUtil;
import com.chuyu.utils.data.SqlUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by Administrator on 2018/9/3.
 */
@Repository
public class SysMenuDao {

    private String table = "sys_menu";
    private String dbId = DbConf.ATTENDANCE_MASTER_ID;

    public List<SysMenu> queryListParentId(Integer... parentId) throws SQLException {
        if (parentId.length==0){
            BeanListHandler<SysMenu> h = new BeanListHandler<>(SysMenu.class,new BasicRowProcessor());
            return DaoUtil.query(dbId,"select *from "+table+ " order by orderNo asc",h);
        }else if (parentId.length==1){
            RecordMap recordMap = new RecordMap();
            recordMap.putObj("parentId",parentId);
            BeanListHandler<SysMenu> h = new BeanListHandler<SysMenu>(SysMenu.class,new BasicRowProcessor());
            return DaoUtil.query(dbId, SqlUtil.makeSelectAllSqlWithOrder(table,recordMap,"order by orderNo asc"),h);
        }else {
            Map<String,List<Object>> map = new HashMap<>();
            List<Object> list = new ArrayList<>();
            list.addAll(Arrays.asList(parentId));
            map.put("parentId",list);
            BeanListHandler<SysMenu> h = new BeanListHandler<SysMenu>(SysMenu.class,new BasicRowProcessor());
            return DaoUtil.query(dbId,SqlUtil.makeSelectAllInSql(table,map),h);
        }
    }

}
