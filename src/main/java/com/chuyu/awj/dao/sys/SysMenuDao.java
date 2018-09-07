package com.chuyu.awj.dao.sys;

import com.chuyu.awj.config.DbConf;
import com.chuyu.awj.model.sys.SysMenu;
import com.chuyu.utils.bean.CamelProcessor;
import com.chuyu.utils.common.DaoException;
import com.chuyu.utils.common.RecordMap;
import com.chuyu.utils.data.DaoUtil;
import com.chuyu.utils.data.QueryParams;
import com.chuyu.utils.data.SqlUtil;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by Administrator on 2017-7-7.
 */
@Repository
public class SysMenuDao
{
    private String table="sys_menu";
    private String dbId = DbConf.ATTENDANCE_MASTER_ID;
    public List<SysMenu> queryListByParentId(Integer... parentId) throws SQLException
    {
        if(parentId.length==0)
        {
            BeanListHandler<SysMenu> h= new BeanListHandler<SysMenu>(SysMenu.class,new BasicRowProcessor(new CamelProcessor()));
            return DaoUtil.query(dbId,"select * from "+ table +" order by orderNo asc",h);
        }
        if(parentId.length==1)
        {
            RecordMap map = new RecordMap();
            map.putObj("parentId",parentId[0]);
            BeanListHandler<SysMenu> h= new BeanListHandler<SysMenu>(SysMenu.class,new BasicRowProcessor(new CamelProcessor()));
            String sql = SqlUtil.makeSelectAllSqlWithOrder(table,map,"order by orderNo asc");
            System.out.println(sql);
            return DaoUtil.query(dbId, SqlUtil.makeSelectAllSqlWithOrder(table,map,"order by orderNo asc"),h);
        }
        else
        {
            Map<String,List<Object>> map = new HashMap <>();
            List<Object> list = new ArrayList<>();
            list.addAll(Arrays.asList(parentId));
            map.put("parentId",list);
            BeanListHandler<SysMenu> h= new BeanListHandler<SysMenu>(SysMenu.class,new BasicRowProcessor(new CamelProcessor()));
            return DaoUtil.query(dbId,SqlUtil.makeSelectAllInSql(table,map),h);
        }
    }
    public List<SysMenu> queryListWithParentName(Integer parentId) throws SQLException
    {
        RecordMap map = new RecordMap();
        String sql ="select m.*,(select p.name from sys_menu p where p.menuId = m.parentId) as parentName from sys_menu m ";
        if(parentId!=null)
        {
            sql +="where parentId="+SqlUtil.sqlValue(parentId);
        }
        sql+=" order by m.orderNo asc";
        BeanListHandler<SysMenu> h= new BeanListHandler<SysMenu>(SysMenu.class,new BasicRowProcessor(new CamelProcessor()));
        return DaoUtil.query(dbId,sql,h);
    }
    public List<SysMenu> queryListWithParentName(QueryParams params) throws SQLException
    {
        StringBuilder sql =new StringBuilder();
        int ws = params.getWhereMap().size();
        int ins = params.getLikeMap().size();
        sql.append("select m.*,(select p.name from sys_menu p where p.menuId = m.parentId) as parentName from sys_menu m ");
        if(ws>0)
        {
            sql.append(" where ");
            int index =0;
            for(String key : params.getWhereMap().keySet())
            {
                sql.append(key).append("=").append(SqlUtil.sqlValue(params.getWhereMap().get(key)));
                index++;
                if(index<ws)
                {
                    sql.append(" and ");
                }
            }
        }
        if(ins>0)
        {
            if(ws==0){
                sql.append(" where ");
            }
            else
            {
                sql.append(" and ");
            }
            int index =0;
            for(String key : params.getLikeMap().keySet())
            {
                sql.append(key).append(" like '%").append(SqlUtil.sqlLikeValue(params.getLikeMap().get(key))).append("%' ");
                index++;
                if(index<ins)
                {
                    sql.append(" and ");
                }
            }
        }
        sql.append(" order by m.parentId asc, m.orderNo asc");
        sql.append(" limit ").append((params.getPage()-1)*params.getPageSize()).append(",").append(params.getPageSize());
        BeanListHandler<SysMenu> h= new BeanListHandler<SysMenu>(SysMenu.class,new BasicRowProcessor(new CamelProcessor()));
        return DaoUtil.query(dbId,sql.toString(),h);
    }

    public Integer queryTotal(QueryParams params) throws SQLException
    {
        return Integer.valueOf(DaoUtil.query4Object(dbId,SqlUtil.makeCountWithLikeSql(table,params.getWhereMap(),params.getLikeMap())).toString());
    }

    public SysMenu queryMenuById(String menuId) throws SQLException
    {
        StringBuilder sql =new StringBuilder();
        sql.append("select m.*,(select p.name from sys_menu p where p.menuId = m.parentId) as parentName from sys_menu m ");
        sql.append(" where menuId = ").append(SqlUtil.sqlValue(menuId));
        BeanHandler<SysMenu> h= new BeanHandler<>(SysMenu.class,new BasicRowProcessor(new CamelProcessor()));
        return DaoUtil.query(dbId,sql.toString(),h);
    }

    public List<SysMenu> queryAllUserMenu(String userId) throws SQLException
    {
        String sql ="select m.* from sys_role_user ur LEFT JOIN sys_role_menu rm on ur.roleId = rm.roleId and rm.menuId>0 LEFT JOIN sys_menu m on rm.menuId = m.menuId  where ur.userId = "+SqlUtil.sqlValue(userId);
        BeanListHandler<SysMenu> h= new BeanListHandler<>(SysMenu.class,new BasicRowProcessor(new CamelProcessor()));
        return DaoUtil.query(dbId,sql,h);
    }

    public Integer queryLastMenuOrderByParentId(String parentId) throws SQLException
    {
        RecordMap map = new RecordMap();
        map.putObj("menuId",parentId);
        String sql = "select orderNo from sys_menu where parentId = "+SqlUtil.sqlValue(parentId)+" order by orderNo desc limit 1";
        Object obj =DaoUtil.query4Object(dbId,sql);
        if(obj==null)
        {
            return 0;
        }
        return Integer.valueOf(obj.toString());
    }

    public int insert(SysMenu menu) throws SQLException
    {
        RecordMap map = new RecordMap();
        map.put("parentId",menu.getParentId());
        map.putObj("name",menu.getName());
        map.putObj("url",menu.getUrl());
        map.putObj("target",menu.getTarget());
        map.putObj("perms",menu.getPerms());
        map.putObj("type",menu.getType());
        map.putObj("icon",menu.getIcon());
        map.putObj("orderNo",menu.getOrderNo());
        return DaoUtil.update(dbId,SqlUtil.makeInsertSql(table,map));
    }

    public int update(SysMenu menu) throws SQLException
    {
        RecordMap setMap = new RecordMap();
        setMap.putObj("parentId",menu.getParentId());
        setMap.putObj("name",menu.getName());
        setMap.putObj("url",menu.getUrl());
        setMap.putObj("target",menu.getTarget());
        setMap.putObj("perms",menu.getPerms());
        setMap.putObj("type",menu.getType());
        setMap.putObj("icon",menu.getIcon());
        setMap.putObj("orderNo",menu.getOrderNo());
        RecordMap whereMap = new RecordMap();
        whereMap.put("menuId",menu.getMenuId());
        return DaoUtil.update(dbId,SqlUtil.makeUpdateSql(table,setMap,whereMap));
    }

    public int delete(Integer... menuId) throws DaoException
    {
        if(menuId.length==0)
        {
            return 0;
        }
        List<String> sqlList = new ArrayList <>();
        if(menuId.length==1)
        {
            RecordMap map = new RecordMap();
            map.putObj("menuId",menuId[0]);
            sqlList.add(SqlUtil.makeDeleteSql("sys_role_menu",map));
            sqlList.add(SqlUtil.makeDeleteSql(table,map));
            return DaoUtil.updateBatch(dbId,sqlList);
        }
        List<Object> ids = new ArrayList<>();
        ids.addAll(Arrays.asList(menuId));
        Map<String,List<Object>> map = new HashMap<>();
        map.put("menuId",ids);
        sqlList.add(SqlUtil.makeDeleteInSql("sys_role_menu",map));
        sqlList.add(SqlUtil.makeDeleteInSql(table,map));
        return DaoUtil.updateBatch(dbId,sqlList);
    }

}
