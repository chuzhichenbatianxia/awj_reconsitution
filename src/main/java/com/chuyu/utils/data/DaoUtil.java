package com.chuyu.utils.data;

import com.chuyu.utils.common.DBConnection;
import com.chuyu.utils.common.DaoException;
import com.chuyu.utils.common.StringHelper;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DaoUtil {

	
	//日志工具
	private static Logger logger = LoggerFactory.getLogger(DaoUtil.class);
	
	//DB操作类
	private static QueryRunner queryRunner = new QueryRunner();

	
	public DaoUtil() {
	}
	/**
	 * 读操作，对从库操作，完成后关闭连接，出现操作异常后写LOG
	 * 可通过传入合适的ResultSetHandler来定制输出的结果,请参考apache Common中的dbUtil包的API文档
	 * @param <T> 泛型，可自定义，也可返回通用的Map,Array,List<Map>,List<Array>,Bean,List<Bean>
	 * @param dbId 在DaShangDBConst中DBID定义
	 * @param sql sql字串
	 * @param rsh ResultSetHandler，请参考apache的dbUtil相关API文档
	 * @param params 个数不定的参数列表
	 * @return 查询结果,失败时返回null
	 */
	public static <T> T queryQuietly(String dbId, String sql, ResultSetHandler<T> rsh, Object... params){
		Connection conn = DBConnection.getConn(dbId);
		if(conn==null){
			return null;
		}
		try {
			return queryRunner.query(conn, sql, rsh, params);
		} catch (Exception e) {
			logger.error("DB读出错,dbId="+dbId+",sql="+sql+",params="+getParamsStr(params),e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		
		return null;
	}
	
	/**
	 * 读操作，对从库操作，完成后关闭连接，由调用进者处理操作异常
	 * 可通过传入合适的ResultSetHandler来定制输出的结果,请参考apache Common中的dbUtil包的API文档
	 * @param <T> 泛型，可自定义，也可返回通用的Map,Array,List<Map>,List<Array>,Bean,List<Bean>
	 * @param dbId 在DaShangDBConst中DBID定义
	 * @param sql sql字串
	 * @param rsh ResultSetHandler，请参考apache的dbUtil相关API文档
	 * @param params 个数不定的参数列表
	 * @return 查询结果
	 * @throws SQLException
	 */
	public static <T> T query(String dbId, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
		Connection conn = DBConnection.getConn(dbId);
		if(conn==null)
		{
			return null;		
		}
		try {
			return queryRunner.query(conn, sql, rsh, params);
		}
		finally {
			DbUtils.closeQuietly(conn);
		}
	}
	
	/**
	 * 读操作，对从库操作，完成后关闭连接，出现操作异常后写LOG
	 * 适用于只返回一行且只有一个值的查询
	 * @param dbId 在DaShangDBConst中DBID定义
	 * @param sql sql字串
	 * @param rsh ResultSetHandler，请参考apache的dbUtil相关API文档
	 * @param params 个数不定的参数列表
	 * @return 查询结果，特别说明：返回null时有可能是失败或DB查询结果为null
	 */
	public static Object query4ObjectQuietly(String dbId, String sql, Object... params){
		Connection conn = DBConnection.getConn(dbId);
		if(conn==null){
			return null;
		}
		try {
			return queryRunner.query(conn, sql, new ScalarHandler(), params);
		} catch (Exception e) {
			logger.error("DB读出错,dbId="+dbId+",sql="+sql+",params="+getParamsStr(params),e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		
		return null;
	}
	
	/**
	 * 读操作，对从库操作，完成后关闭连接，由调用进者处理操作异常
	 * 适用于只返回一行且只有一个值的查询
	 * @param dbId 在DaShangDBConst中DBID定义
	 * @param sql sql字串
	 * @param rsh ResultSetHandler，请参考apache的dbUtil相关API文档
	 * @param params 个数不定的参数列表
	 * @return 查询结果
	 * @throws SQLException
	 */
	public static Object query4Object(String dbId, String sql, Object... params) throws SQLException {
		Connection conn = DBConnection.getConn(dbId);
		if(conn==null){
			return null;
		}
		try {
			return queryRunner.query(conn, sql, new ScalarHandler(), params);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}
	
	/**
	 * 增删改等写操作，对主库操作，完成后关闭连接，出现操作异常后写LOG
	 * @param dbId 在DaShangDBConst中DBID定义
	 * @param sql sql字串
	 * @param params 个数不定的参数列表
	 * @return 更新结果，失败时返回-1
	 */
	public static int updateQuietly(String dbId, String sql, Object... params){
		Connection conn = DBConnection.getConn(dbId);
		try {
			return queryRunner.update(conn, sql, params);
		} catch (Exception e) {
			logger.error("DB写出错,dbId="+dbId+",sql="+sql+",params="+getParamsStr(params), e);
		}finally{
			DbUtils.closeQuietly(conn);
		}
		return -1;
	}
	
	
	/**
	 * 增删改等写操作，对主库操作，完成后关闭连接，由调用进者处理操作异常
	 * @param dbId 在UserDBConst中DBID定义
	 * @param sql sql字串
	 * @param params 个数不定的参数列表
	 * @return 更新结果
	 * @throws SQLException
	 */
	public static int update(String dbId, String sql, Object... params) throws SQLException {
		Connection conn = DBConnection.getConn(dbId);
		try {
			return queryRunner.update(conn, sql, params);
		}finally{
			DbUtils.closeQuietly(conn);
		}
	}
	
	public static void free(ResultSet rs, Statement st, Connection conn)
			throws SQLException {
		try {
			if (rs != null){
				rs.close();
			}
		} catch (SQLException e) {
			throw new SQLException();
		} finally {
			try {
				if (st != null){
					st.close();
				}
			} catch (SQLException e) {
				throw new SQLException();
			} finally {
				if (conn != null){
					try {
						conn.close();
					} catch (Exception e) {
						throw new SQLException();
					}
				}
			}
		}
	}
	
	
	public static int updateBatch(String dbId, List<String> sqlList) throws DaoException
	{
		
		Connection conn = null;
		Statement ps = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DBConnection.getConn(dbId);
			conn.setAutoCommit(false);
			ps = conn.createStatement();
			
			for (String sqlStr : sqlList) {
				ps.addBatch(sqlStr);
			}
			count = ps.executeBatch().length;
			conn.commit();
		} catch (SQLException e) {
			try {
				count=-1;
				conn.rollback();
				logger.error("批量更新失败:sql="+ StringHelper.listToString(sqlList,'#'));
				e.printStackTrace();
				throw new DaoException("批量更新失败:"+e.toString()+",count:"+sqlList.size());
			} catch (Exception ex) {
				throw new DaoException(ex.getMessage());
			}
		
		} finally {
			try {
				free(rs, ps, conn);
			} catch (SQLException e) {
				throw new DaoException("关闭连接失败"+e.getMessage());
			}
			return count;
		}
	}
	
	
		
	/**
	 * 把Object...的输入参数转成字串输出
	 * @param params 输入参数。。。
	 * @return 豆号隔开的字串
	 */
   private static String getParamsStr(Object... params){
   	if(params==null){
		return "";
	}
   	StringBuffer buff=new StringBuffer();
   	for(int i=0;i<params.length;i++){
   		if(i>0){
			buff.append(",");
		}
   		buff.append(params[i]);
   	}
   	return buff.toString();
   } 

}
