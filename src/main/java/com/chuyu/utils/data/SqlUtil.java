package com.chuyu.utils.data;

import org.apache.commons.beanutils.BeanUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQL 工具类
 * @author
 * @teme 2010-6-1 下午12:08:49
 */
public final class SqlUtil {

	/**
	 * date format
	 */
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 设置date format
	 * @param format -- date format
	 */
	public static void setDateFormat(String format) {
		sdf =  new SimpleDateFormat(format);
	}

	/**
	 * 拼接 select * SQL语句
	 * @param tableName -- db 表名称
	 * @param where -- where 子句map
	 * @return -- SQL语句
	 */
	public static String makeSelectAllSql(String tableName, Map<String, Object> where) {

		int ws = (null != where) ? where.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ws * 32);
		sql.append("\n select * from ").append(tableName);
		if(ws>0){
			sql.append("\n where ");// 拼接where子句
		}
		int index = 0;
		for (String key : where.keySet()) {
			Object v = where.get(key);
			sql.append(key).append("=").append(sqlValue(v));
			index++;
			if (index < ws) {
				sql.append("\n   and ");
			}
		}
		return sql.toString();
	}

	/**
	 * 拼接 insert SQL语句
	 * @param tableName -- db 表名称
	 * @param columns -- 列集合map
	 * @return -- SQL语句
	 */
	public static String makeInsertSql(String tableName, Map<String, Object> columns) {

		int columnSize = columns.size();
		StringBuilder sql = new StringBuilder(64 + columnSize * 32);
		sql.append("\n insert into ").append(tableName);
		sql.append(" ( ");
		int index = 0;
		for (String item : columns.keySet()) {
			sql.append(item);
			index++;
			if (index != columnSize) {
				sql.append(",");
			}
		}
		sql.append(" )\n");
		sql.append(" values ( ");
		index = 0;
		for (String item : columns.keySet()) {
			Object value = columns.get(item);
			sql.append(sqlValue(value));
			index++;
			if (index != columnSize) {
				sql.append(",");
			}
		}
		sql.append(" )");
		return sql.toString();
	}

	public static String makeBatchInsertSql(String tableName, List<Map<String,Object>> recordMaps) {
		int recordMapSize = recordMaps.size();
		if(recordMapSize<=0)
		{
			return "";
		}
		Map<String,Object> columns = recordMaps.get(0);
		int columnSize = columns.size();
		StringBuilder sql = new StringBuilder(64 + recordMapSize * 32);
		sql.append("\n insert into ").append(tableName);
		sql.append(" ( ");
		int index = 0;
		for (String item :columns.keySet()) {
			sql.append(item);
			index++;
			if (index != columnSize) {
				sql.append(",");
			}
		}
		sql.append(" ) values ");
		int recordIndex =0;
		for(Map<String,Object> map : recordMaps)
		{
			recordIndex++;
			sql.append(" (");
			index = 0;
			for (String item : map.keySet()) {
				Object value = map.get(item);
				sql.append(sqlValue(value));
				index++;
				if (index != columnSize) {
					sql.append(",");
				}
			}
			sql.append(" )");
			if(recordIndex !=recordMapSize)
			{
				sql.append(",");
			}
		}
		return sql.toString();
	}


	/**
	 * 拼接update SQL语句
	 * @param tableName -- db表名称
	 * @param set -- update子句map
	 * @param where -- where 子句map
	 * @return -- SQL语句
	 */
	public static String makeUpdateSql(String tableName, Map<String, Object> set, Map<String, Object> where) {

		if (null == set) {
			throw new IllegalArgumentException("update的字段集合不能为null");
		}
		int ss = set.size();
		int ws = (null != where) ? where.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ss * 32 + ws * 32);
		// 拼接set子句
		sql.append("\n update ").append(tableName).append("\n set ");
		int index = 0;
		for (String key : set.keySet()) {
			Object v = set.get(key);
			sql.append("\t").append(key).append("=").append(sqlValue(v));
			index++;
			if (index < ss) {
				sql.append(",\n");
			}
		}
		// 没有where子句
		if (ws == 0) {
			return sql.toString();
		}
		// 拼接where子句
		sql.append("\n where ");
		index = 0;
		for (String key : where.keySet()) {
			Object v = where.get(key);
			sql.append(key).append("=").append(sqlValue(v));
			index++;
			if (index < ws) {
				sql.append("\n   and ");
			}
		}
		return sql.toString();
	}

	/**
	 * update set where and in
	 * @param tableName
	 * @param setMap
	 * @param whereMap
	 * @param inMap
	 * @return
	 */
	public static String makeUpdateWithInSql(String tableName, Map<String, Object> setMap, Map<String, Object> whereMap , Map<String,List<Object>> inMap) {

		if (null == setMap) {
			throw new IllegalArgumentException("update的字段集合不能为null");
		}
		int ss = setMap.size();
		int ws = (null != whereMap) ? whereMap.size() : 0;
		int ins = (null != inMap) ? inMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ss * 32 + ws * 32 + ins * 32);
		// 拼接set子句
		sql.append("\n update ").append(tableName).append("\n set ");
		int index = 0;
		for (String key : setMap.keySet()) {
			Object v = setMap.get(key);
			sql.append("\t").append(key).append("=").append(sqlValue(v));
			index++;
			if (index < ss) {
				sql.append(",\n");
			}
		}
		// 没有where子句
		if (ws > 0) {
			// 拼接where子句
			sql.append("\n where ");
			index = 0;
			for (String key : whereMap.keySet()) {
				Object v = whereMap.get(key);
				sql.append(key).append("=").append(sqlValue(v));
				index++;
				if (index < ws) {
					sql.append("\n   and ");
				}
			}
		}
		if(ins>0)
		{
			if(ws==0)
			{
				// 拼接where子句
				sql.append("\n where ");
			}
			else
			{
				// 拼接and子句
				sql.append("\n and ");
			}
			index = 0;
			for (String key : inMap.keySet()) {
				List<Object> v = inMap.get(key);
				int vsize = v.size();
				if(v !=null && !v.isEmpty())
				{
					sql.append(key).append(" in(");
					int vindex =0;
					for(Object obj : v)
					{
						sql.append(sqlValue(obj));
						vindex++;
						if(vindex<vsize)
						{
							sql.append(",");
						}
					}
					sql.append(")");
					index++;
					if (index < ins) {
						sql.append("\n   and ");
					}
				}
			}
		}
		return sql.toString();
	}

	/**
	 * delete from table where .. and ..in
	 * @param tableName
	 * @param whereMap
	 * @param inMap
	 * @return
	 */
	public static String makeDeleteWithInSql(String tableName, Map<String, Object> whereMap , Map<String,List<Object>> inMap) {
		int ws = (null != whereMap) ? whereMap.size() : 0;
		int ins = (null != inMap) ? inMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ws * 32 + ins * 32);
		// 拼接set子句
		sql.append("\n delete from ").append(tableName);
		int index = 0;
		if (ws > 0) {
			// 拼接where子句
			sql.append("\n where ");
			for (String key : whereMap.keySet()) {
				Object v = whereMap.get(key);
				sql.append(key).append("=").append(sqlValue(v));
				index++;
				if (index < ws) {
					sql.append("\n   and ");
				}
			}
		}
		if(ins>0)
		{
			if(ws==0)
			{
				// 拼接where子句
				sql.append("\n where ");
			}
			else
			{
				// 拼接and子句
				sql.append("\n and ");
			}
			index = 0;
			for (String key : inMap.keySet()) {
				List<Object> v = inMap.get(key);
				int vsize = v.size();
				if(v !=null && !v.isEmpty())
				{
					sql.append(key).append(" in(");
					int vindex =0;
					for(Object obj : v)
					{
						sql.append(sqlValue(obj));
						vindex++;
						if(vindex<vsize)
						{
							sql.append(",");
						}
					}
					sql.append(")");
					index++;
					if (index < ins) {
						sql.append("\n   and ");
					}
				}
			}
		}
		return sql.toString();
	}

	/**
	 * update table set ... where ... and ..in
	 * @param tableName
	 * @param setMap
	 * @param inMap
	 * @return
	 */
	public static String makeUpdateInSql(String tableName, Map<String, Object> setMap, Map<String,List<Object>> inMap) {

		if (null == setMap) {
			throw new IllegalArgumentException("update的字段集合不能为null");
		}
		int ss = setMap.size();
		int ins = (null != inMap) ? inMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ss * 32 + ins * 32);
		// 拼接set子句
		sql.append("\n update ").append(tableName).append("\n set ");
		int index = 0;
		for (String key : setMap.keySet()) {
			Object v = setMap.get(key);
			sql.append("\t").append(key).append("=").append(sqlValue(v));
			index++;
			if (index < ss) {
				sql.append(",\n");
			}
		}
		if(ins>0)
		{
			// 拼接where子句
			sql.append("\n where ");
			index = 0;
			for (String key : inMap.keySet()) {
				List<Object> v = inMap.get(key);
				int vsize = v.size();
				if(v !=null && !v.isEmpty())
				{
					sql.append(key).append(" in(");
					int vindex =0;
					for(Object obj : v)
					{
						sql.append(sqlValue(obj));
						vindex++;
						if(vindex<vsize)
						{
							sql.append(",");
						}
					}
					sql.append(")");
					index++;
					if (index < ins) {
						sql.append("\n   and ");
					}
				}
			}
		}
		return sql.toString();
	}

	/**
	 * delete from table where ..in
	 * @param tableName
	 * @param inMap
	 * @return
	 */
	public static String makeDeleteInSql(String tableName, Map<String,List<Object>> inMap) {
		int ins = (null != inMap) ? inMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ins * 32);
		// 拼接set子句
		sql.append("\n delete from ").append(tableName).append("\n ");
		int index = 0;
		if(ins>0)
		{
			// 拼接where子句
			sql.append("\n where ");
			index = 0;
			for (String key : inMap.keySet()) {
				List<Object> v = inMap.get(key);
				int vsize = v.size();
				if(v !=null && !v.isEmpty())
				{
					sql.append(key).append(" in(");
					int vindex =0;
					for(Object obj : v)
					{
						sql.append(sqlValue(obj));
						vindex++;
						if(vindex<vsize)
						{
							sql.append(",");
						}
					}
					sql.append(")");
					index++;
					if (index < ins) {
						sql.append("\n   and ");
					}
				}
			}
		}
		return sql.toString();
	}

	/**
	 * 拼接delete SQL语句
	 * @param tableName -- db表名称
	 * @param where -- where子句map
	 * @return -- SQL语句
	 */
	public static String makeDeleteSql(String tableName, Map<String, Object> where) {

		int ws = (null != where) ? where.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ws * 32);
		sql.append("\n delete from ").append(tableName);
		if (0 == ws) {
			return sql.toString();
		}
		sql.append("\n where ");
		int index = 0;
		for (String key : where.keySet()) {
			Object v = where.get(key);
			sql.append(key).append("=").append(sqlValue(v));
			index++;
			if (index < ws) {
				sql.append("\n   and ");
			}
		}
		return sql.toString();
	}



    /*
    **
            * 拼接 动态SQL
    * @param dynamicSql
    * @param params
    * @return
            * @see
    */
    @SuppressWarnings("unchecked")
    public static String makeDynamicSql(String dynamicSql, Object params) {

        Map<String, Object> b = null;
        if (params instanceof Map) {
            b = (Map<String, Object>)params;
        } else {
            try {
                b = BeanUtils.describe(params);
            } catch (Exception ex) {
                throw new IllegalArgumentException("BeanUtils.describe(bean)异常", ex);
            }
        }
        return makeDynamicSql(dynamicSql, b);
    }

    /**
     * 拼接 动态SQL
     * <pre>
     *    String dynamicSql = "select * from article where {id = #id } {and val = $val } {vas in($vas )} {and creation > $createTime}";
     *    <1>使用map作为参数
     *    Map<String, Object> params = new HashMap<String, Object>();
     *    params.put("id", 20060601);
     *    params.put("val", "demo_value");
     *    String sql = makeDynamicSql(dynamicSql, params);
     *    // sql --> "select * from article where id = 20060601 and val = 'demo_value'"
     *
     *
     *     <2>使用java bean作为参数
     *     public static class MyParamBean {	 *
     *  		private String id = null;
     *  		private String val = null;	 *
     *  		public String getId() {
     *  			return id;
     *  		}
     *  		public void setId(String id) {
     *  			this.id = id;
     *  		}
     *  		public String getVal() {
     *  			return val;
     *  		}
     *  		public void setVal(String val) {
     *  			this.val = val;
     *  		}
     *  	}	 *
     *     MyParamBean param = new MyParamBean();
     *		param.setId("demo_id");
     *		param.setVal("demo_value");
     *		sql = SqlUtil.makeDynamicSql(dynamicSql, param.setVal);
     *     //  sql --> "select * from article where id = 'demo_id' and val = 'demo_value'"
     * </pre>
     * @param dynamicSql -- 动态SQL语句
     * @param  -- 替换数据源
     * @return -- SQL语句
     */
    public static String makeDynamicSql(String dynamicSql, Map<String, Object> params) {

        int ps = dynamicSql.length();
        StringBuilder sql = new StringBuilder(128 + ps * 2);
        StringBuilder item = new StringBuilder(128);

        boolean isDynamicStart = false;
        char c = 0;
        for (int i = 0; i < ps; i++) {
            c = dynamicSql.charAt(i);
            if (isDynamicStart) {
                if ('}' == c) {
                    isDynamicStart = false;
                    String ii = item.toString();
                    item.setLength(0);
                    sql.append(makeDynamicItem(ii, params));
                } else {
                    item.append(c);
                }
            } else {
                if ('{' == c) {
                    isDynamicStart = true;
                } else {
                    sql.append(c);
                }
            }
        }
        if (item.length() > 0) {
            String ii = item.toString();
            sql.append(makeDynamicItem(ii, params));
        }
        return sql.toString();
    }




	/**
	 * 拼接动态SQL中的一个动态项
	 * <br>备注: 动态SQL中 在一对'{'和'}'之间的字符串就是一个动态性
	 * @param dynamicItem -- 动态项字符串
	 * @param params -- 替换变量的参数
	 * @return -- SQL语句片段
	 */
	private static String makeDynamicItem(String dynamicItem, Map<String, Object> params) {

		int ps = dynamicItem.length();
		StringBuilder sqlItem = new StringBuilder(64 + ps * 2);
		StringBuilder param = new StringBuilder(64);

		boolean isParamStart = false;
		char c = 0;
		char flag = 0;
		for (int i = 0; i < ps; i++) {
			c = dynamicItem.charAt(i);
			if (isParamStart) {
				if (' ' == c || '\n' == c || '\t' == c || '\r' == c) {
					isParamStart = false;
					String p = param.toString();
					param.setLength(0);
					Object v = params.get(p);
					if ('$' == flag) {
						if (null == v) {
							return "";
						}
						sqlItem.append(sqlValue(v));
					}  else if ('#' == flag) {
						sqlItem.append(sqlValue(v));
					} else if ('&' == flag) {
						if (null == v) {
							return "";
						}
						sqlItem.append(v);
					}
				} else {
					param.append(c);
				}
			} else {
				if ('$' == c || '#' == c || '&' == c || '?' == c || '@' == c) {
					flag = c;
					isParamStart = true;
				} else {
					sqlItem.append(c);
				}
			}
		}
		if (param.length() > 0) {
			String p = param.toString();
			Object v = params.get(p);
			if ('$' == flag) {
				if (null == v) {
					return "";
				}
				sqlItem.append(sqlValue(v));
			} else if ('#' == flag) {
				sqlItem.append(sqlValue(v));
			}
		}
		return sqlItem.toString();
	}



	/**
	 * 拼接SQL语法的字段字符串值
	 * @param value -- 数据
	 * @return -- SQL片段字符串
	 */
	public static String sqlValue(int value) {
		return Integer.toString(value);
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * @param value -- 数据
	 * @return -- SQL片段字符串
	 */
	public static String sqlValue(long value) {
		return Long.toString(value);
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * @param value -- 数据
	 * @return -- SQL片段字符串
	 */
	public static String sqlValue(short value) {
		return Short.toString(value);
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * @param value -- 数据
	 * @return -- SQL片段字符串
	 */
	public static String sqlValue(float value) {
		return Float.toString(value);
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * @param value -- 数据
	 * @return -- SQL片段字符串
	 */
	public static String sqlValue(double value) {
		return Double.toString(value);
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * @param value -- 数据
	 * @return -- SQL片段字符串
	 */
	public static String sqlValue(String value) {

		//return "'" + value.trim().replaceAll("\'", "\'\'").replace("\\", "\\\\") + "'"; // 防止sql注入
		//下面的代码等价于上面一行的代码,
		//目的: 性能优化, 测试数据显示性能可以提高一,两个数量级(100w次调用时间只用上面代码的1/100 ~ 1/20)
		if (null == value) {
			return "''";
		}
		String v = value.trim();
		int vs = v.length();
		StringBuilder sb = new StringBuilder(2 + vs * 2);
		char c = 0;
		sb.append('\'');
		// 防止sql注入
		for (int i = 0; i < vs; i++) {
			c = v.charAt(i);
			if ('\'' == c) {
				sb.append('\'');
				sb.append('\'');
			} else if ('\\' == c) {
				sb.append('\\');
				sb.append('\\');
			} else {
				sb.append(c);
			}
		}
		sb.append('\'');
		return sb.toString();
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * @param value -- 数据
	 * @return -- SQL片段字符串
	 */
	@SuppressWarnings("unchecked")
	public static String sqlValue(Object value) {

		if (null == value) {
			return "null";
		} else if (value instanceof String) {
			String v = (String)value;
			return sqlValue(v);
		} else if (value instanceof Integer) {
			Integer v = (Integer)value;
			return  v.toString();
		} else if (value instanceof Date) {
			Date v = (Date)value;
			return  "'" + sdf.format(v) + "'";
		}  else if (value instanceof Timestamp) {
			Timestamp v = (Timestamp)value;
			return "'" + v + "'";
		}  else if (value instanceof List) {
			List v = (List)value;
			return sqlValue(v);
		}  else if (value.getClass().isArray()) { // 数组类型
			Class ct = value.getClass().getComponentType();
			if (ct == String.class) { // 是否是String数组
				String[] va = (String[])value;
				return sqlValue(va);
			} else if (ct.isPrimitive()) { // 是否是原始类型数组
				if (ct == int.class) {
					int[] va = (int[])value;
					return sqlValue(va);
				} else if (ct == long.class) {
					long[] va = (long[])value;
					return sqlValue(va);
				} else if (ct == short.class) {
					short[] va = (short[])value;
					return sqlValue(va);
				} else if (ct == float.class) {
					float[] va = (float[])value;
					return sqlValue(va);
				} else if (ct == double.class) {
					double[] va = (double[])value;
					return sqlValue(va);
				}
			}
			Object[] v = (Object[])value; // 默认,转成Object对象数组
			return sqlValue(v);
		} else if (value instanceof Long || value instanceof Short || value instanceof Float || value instanceof Double) {
			return  value.toString();
		}  else {
			return  "'" + value.toString() + "'";
		}
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * @param value -- 数据
	 * @return -- SQL片段字符串
	 */
	public static String sqlValue(Object[] value) {

		if (null == value) {
			return "''";
		}
		StringBuilder sql = new StringBuilder(64 + value.length * 32);
		for (int i = 0; i < value.length; i++) {
			sql.append(sqlValue(value[i]));
			if(i < value.length - 1) {
				sql.append(",");
			}
		}
		return sql.toString();
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * @param value -- 数据
	 * @return -- SQL片段字符串
	 */
	public static String sqlValue(String[] value) {

		if (null == value) {
			return "''";
		}
		StringBuilder sql = new StringBuilder(64 + value.length * 32);
		for (int i = 0; i < value.length; i++) {
			sql.append(sqlValue(value[i]));
			if(i < value.length - 1) {
				sql.append(",");
			}
		}
		return sql.toString();
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * @param value -- 数据
	 * @return -- SQL片段字符串
	 */
	public static String sqlValue(List<Object> value) {

		if (null == value) {
			return "''";
		}
		int size = value.size();
		StringBuilder sql = new StringBuilder(64 + size * 32);
		for (int i = 0; i < size; i++) {
			sql.append(sqlValue(value.get(i)));
			if(i < size - 1) {
				sql.append(",");
			}
		}
		return sql.toString();
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * @param value -- 数据
	 * @return -- SQL片段字符串
	 */
	public static String sqlValue(int[] value) {

		if (null == value) {
			return "''";
		}
		StringBuilder sql = new StringBuilder(64 + value.length * 32);
		for (int i = 0; i < value.length; i++) {
			sql.append(sqlValue(value[i]));
			if(i < value.length - 1) {
				sql.append(",");
			}
		}
		return sql.toString();
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * @param value -- 数据
	 * @return -- SQL片段字符串
	 */
	public static String sqlValue(short[] value) {

		if (null == value) {
			return "''";
		}
		StringBuilder sql = new StringBuilder(64 + value.length * 32);
		for (int i = 0; i < value.length; i++) {
			sql.append(sqlValue(value[i]));
			if(i < value.length - 1) {
				sql.append(",");
			}
		}
		return sql.toString();
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * @param value -- 数据
	 * @return -- SQL片段字符串
	 */
	public static String sqlValue(long[] value) {

		if (null == value) {
			return "''";
		}
		StringBuilder sql = new StringBuilder(64 + value.length * 32);
		for (int i = 0; i < value.length; i++) {
			sql.append(sqlValue(value[i]));
			if(i < value.length - 1) {
				sql.append(",");
			}
		}
		return sql.toString();
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * @param value -- 数据
	 * @return -- SQL片段字符串
	 */
	public static String sqlValue(float[] value) {

		if (null == value) {
			return "''";
		}
		StringBuilder sql = new StringBuilder(64 + value.length * 32);
		for (int i = 0; i < value.length; i++) {
			sql.append(sqlValue(value[i]));
			if(i < value.length - 1) {
				sql.append(",");
			}
		}
		return sql.toString();
	}

	/**
	 * 拼接SQL语法的字段字符串值
	 * @param value -- 数据
	 * @return -- SQL片段字符串
	 */
	public static String sqlValue(double[] value) {

		if (null == value) {
			return "''";
		}
		StringBuilder sql = new StringBuilder(64 + value.length * 32);
		for (int i = 0; i < value.length; i++) {
			sql.append(sqlValue(value[i]));
			if(i < value.length - 1) {
				sql.append(",");
			}
		}
		return sql.toString();
	}
	public static String sqlLikeValue(Object value)
	{
		if (null == value) {
			return "";
		}
		String v = String.valueOf(value);
		int vs = v.length();
		StringBuilder sb = new StringBuilder(vs * 2);
		char c = 0;
		for (int i = 0; i < vs; i++) { // 防止sql注入
			c = v.charAt(i);
			if ('\'' == c) {
				sb.append('\'');
				sb.append('\'');
			} else if ('\\' == c) {
				sb.append('\\');
				sb.append('\\');
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	/**
	 * sql =select sum(user_id) from xx_table where game_id=1;
	 * @param whereMap
	 * @param sumColumn
	 * @param table
	 * @return
	 */
	public static String makeSumSql(String table, Map<String, Object> whereMap , String sumColumn)
	{
		int ws = (null != whereMap) ? whereMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ws * 32);
		sql.append("\n select sum(").append(sumColumn).append(")  from ").append(table);
		if(ws!=0)
		{
			sql.append("\n where ");// 拼接where子句
			int index = 0;
			for (String key : whereMap.keySet()) {
				Object v = whereMap.get(key);
				sql.append(key).append("=").append(SqlUtil.sqlValue(v));
				index++;
				if (index < ws) {
					sql.append("\n   and ");
				}
			}
		}
		return sql.toString();
	}



	/**
	 * 拼接where子句
	 * @param whereMap
	 * @return
	 */
	public static String dealMap(Map<String,Object> whereMap)
	{
		int ws = (null != whereMap) ? whereMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ws * 32);
		if (ws != 0) {
			sql.append("\n where ");// 拼接where子句

			int index = 0;
			for (String key : whereMap.keySet()) {
				Object v = whereMap.get(key);
				sql.append(key).append("=").append(SqlUtil.sqlValue(v));
				index++;
				if (index < ws) {
					sql.append("\n   and ");
				}
			}
		}
		return sql.toString();
	}

	public static String dealInMap(Map<String,List<Object>> inMap)
	{
		int ws = (null != inMap) ? inMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ws * 32);
		if(ws>0)
		{
			// 拼接where子句
			sql.append("\n where ");
		}
		int index = 0;
		for (String key : inMap.keySet()) {
			List<Object> v = inMap.get(key);
			int vsize = v.size();
			if(v !=null && !v.isEmpty())
			{
				sql.append(key).append(" in(");
				int vindex =0;
				for(Object obj : v)
				{
					sql.append(sqlValue(obj));
					vindex++;
					if(vindex<vsize)
					{
						sql.append(",");
					}
				}
				sql.append(")");
				index++;
				if (index < ws) {
					sql.append("\n   and ");
				}
			}
		}
		return sql.toString();
	}
	public static String dealInMapNoWhere(Map<String,List<Object>> inMap)
	{
		int ws = (null != inMap) ? inMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ws * 32);
		int index = 0;
		for (String key : inMap.keySet()) {
			List<Object> v = inMap.get(key);
			int vsize = v.size();
			if(v !=null && !v.isEmpty())
			{
				sql.append(key).append(" in(");
				int vindex =0;
				for(Object obj : v)
				{
					sql.append(sqlValue(obj));
					vindex++;
					if(vindex<vsize)
					{
						sql.append(",");
					}
				}
				sql.append(")");
				index++;
				if (index < ws) {
					sql.append("\n   and ");
				}
			}
		}
		return sql.toString();
	}

	/**
	 * 拼接where like子句
	 * @param likeMap
	 * @return
	 */
	public static String dealLikeMap(Map<String,Object> likeMap)
	{
		int ws = (null != likeMap) ? likeMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ws * 32);
		if (ws != 0) {
			sql.append("\n where ");// 拼接where子句
			int index = 0;
			for (String key : likeMap.keySet()) {
				Object v = likeMap.get(key);
				sql.append(key).append("'%").append(SqlUtil.sqlLikeValue(v)).append("%'");
				index++;
				if (index < ws) {
					sql.append("\n   and ");
				}
			}
		}
		return sql.toString();
	}

	public static String dealLikeMapNoWhere(Map<String,Object> likeMap)
	{
		int ws = (null != likeMap) ? likeMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ws * 32);
		if (ws != 0) {
			int index = 0;
			for (String key : likeMap.keySet()) {
				Object v = likeMap.get(key);
				sql.append(key).append("'%").append(SqlUtil.sqlLikeValue(v)).append("%'");
				index++;
				if (index < ws) {
					sql.append("\n   and ");
				}
			}
		}
		return sql.toString();
	}

	/**
	 * 拼接where and like语句
	 * @param likeMap
	 * @return
	 */
	public static String dealWithLikeMap(Map<String,Object> whereMap, Map<String,Object> likeMap)
	{
		int ws = (null != whereMap) ? whereMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ws * 32);
		if (ws != 0) {
			sql.append("\n where ");// 拼接where子句
			int index = 0;
			for (String key : whereMap.keySet()) {
				Object v = whereMap.get(key);
				sql.append(key).append("=").append(SqlUtil.sqlValue(v));
				index++;
				if (index < ws) {
					sql.append("\n   and ");
				}
			}
		}

		int wsLike = (null != likeMap) ? likeMap.size() : 0;
		if(wsLike>0)
		{
			if(ws==0)
			{
				sql.append("\n where ");// 拼接where子句
			}
			else
			{
				sql.append("\n and ");// 拼接and子句
			}
			int index = 0;
			for (String key : likeMap.keySet()) {
				Object v = likeMap.get(key);
				sql.append(key).append(" like '%").append(sqlLikeValue(v)).append("%'");
				index++;
				if (index < wsLike) {
					sql.append("\n   and ");
				}
			}
		}
		return sql.toString();
	}

	public static String dealWithLikeMapNoWhere(Map<String,Object> whereMap, Map<String,Object> likeMap)
	{
		int ws = (null != whereMap) ? whereMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ws * 32);
		if (ws != 0) {
			int index = 0;
			for (String key : whereMap.keySet()) {
				Object v = whereMap.get(key);
				sql.append(key).append("=").append(SqlUtil.sqlValue(v));
				index++;
				if (index < ws) {
					sql.append("\n   and ");
				}
			}
		}

		int wsLike = (null != likeMap) ? likeMap.size() : 0;
		if(wsLike>0)
		{
			if(ws>0)
			{
				sql.append("\n and ");// 拼接and子句
			}
			int index = 0;
			for (String key : likeMap.keySet()) {
				Object v = likeMap.get(key);
				sql.append(key).append(" like '%").append(sqlLikeValue(v)).append("%'");
				index++;
				if (index < wsLike) {
					sql.append("\n   and ");
				}
			}
		}
		return sql.toString();
	}


	/**
	 * 拼接where子句
	 * @param whereMap
	 * @return
	 */
	public static String dealMapNoWhere(Map<String,Object> whereMap)
	{
		int ws = (null != whereMap) ? whereMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ws * 32);
		if (ws != 0) {

			int index = 0;
			for (String key : whereMap.keySet()) {
				Object v = whereMap.get(key);
				sql.append(key).append("=").append(SqlUtil.sqlValue(v));
				index++;
				if (index < ws) {
					sql.append("\n   and ");
				}
			}
		}
		return sql.toString();
	}
	
	
	/**
	 * sql 类型与java类形的映射;
	 * @param sqlType
	 * @return
	 */
	public  static String sqlType2JavaType(String sqlType) {


		Map<String,String> map =new HashMap<String,String>();

		map.put("tinyint", "int");
		map.put("integer", "int");
		map.put("int", "int");
		map.put("int unsigned", "int");
		map.put("integer unsigned", "int");
		map.put("bigint", "long");
		map.put("float", "float");
		map.put("decimal", "BigDecimal");
		map.put("numeric", "double");
		map.put("real", "BigDecimal");
		map.put("money", "BigDecimal");
		map.put("smallmoney", "BigDecimal");
		map.put("smallmoney", "BigDecimal");


		map.put("bit","bool");
		map.put("smallint", "int");
		map.put("varchar", "String");
		map.put("mediumblob", "String");
		map.put("char", "String");
		map.put("nvarchar", "String");
		map.put("nchar", "String");

		map.put("datetime", "Date");
		map.put("date", "Date");
		map.put("timestamp", "String");
		map.put("image", "String");
		map.put("text", "String");

		return map.get(sqlType.toLowerCase());
	}

	

	/**
	 * 按条件分页查找,拼成sql.条件放到whereMap里
	 *
	 * @param whereMap
	 *            条件
	 * @param curPageNo
	 *            当前页
	 * @param pageSize
	 *            每页多少条
	 * @return
	 */
	public static String makeSelectAllSql(String table, Map<String, Object> whereMap, int curPageNo,
                                          int pageSize) {
        String sql = "";
        if (null != whereMap && whereMap.size() > 0) {
            sql = SqlUtil.makeSelectAllSql(table, whereMap) + " limit "
                    + (curPageNo-1)*pageSize+","+pageSize;
        } else {
            sql = "select * from " + table + " limit " + (curPageNo-1)*pageSize+","+pageSize;
        }
        return sql;
    }

	/**
	 * 按条件分页模糊查找,拼成sql.条件放到whereLikeMap里
	 * @param table
	 * @param whereLikeMap
	 * @param curPageNo
	 * @param pageSize
	 * @return
	 */
	public static String makeSelectAllLikeSql(String table, Map<String, Object> whereLikeMap, int curPageNo,
                                              int pageSize) {
		String sql = "";
		if (null != whereLikeMap && whereLikeMap.size() > 0) {
			sql = SqlUtil.makeSelectAllLikeSql(table, whereLikeMap) + " limit "
					+ (curPageNo-1)*pageSize+","+pageSize;
		} else {
			sql = "select * from " + table + " limit " + (curPageNo-1)*pageSize+","+pageSize;
		}
		return sql;
	}

	/**
	 * 按条件分页精确+模糊查找,拼成sql.条件放到whereMap和likeMap里
	 * @param table
	 * @param whereMap
	 * @param likeMap
	 * @param curPageNo
	 * @param pageSize
	 * @return
	 */
	public static String makeSelectAllWithLikeSql(String table, Map<String, Object> whereMap, Map<String, Object> likeMap, int curPageNo,
                                                  int pageSize) {
		String sql = "";
		if ((null != whereMap && whereMap.size() > 0) || (null != likeMap && likeMap.size() > 0)) {
			sql = SqlUtil.makeSelectAllWithLikeSql(table, whereMap,likeMap) + " limit "
					+ (curPageNo-1)*pageSize+","+pageSize;
		} else {
			sql = "select * from " + table + " limit " + (curPageNo-1)*pageSize+","+pageSize;
		}
		return sql;
	}

	public static String makeSelectAllWithLikeSqlWithOrder(String table, Map<String, Object> whereMap, Map<String, Object> likeMap, int curPageNo,
                                                           int pageSize, String order) {
		String sql = "";
		if ((null != whereMap && whereMap.size() > 0) || (null != likeMap && likeMap.size() > 0)) {
			sql = SqlUtil.makeSelectAllWithLikeSql(table, whereMap,likeMap);
			if(order!=null)
			{
				sql+=" "+order;
			}
			sql+= " limit " + (curPageNo-1)*pageSize+","+pageSize;
		} else {
			sql = "select * from " + table;
			if(order!=null)
			{
				sql+=" "+order;
			}
			sql+=" limit " + (curPageNo-1)*pageSize+","+pageSize;
		}
		return sql;
	}


    /**
     * 按条件查询1条记录
     * @param whereMap
     *    whereMap=null的时候。 用select * from this.table limit 1;查询
     * @param table
     * @return
     */
    public static String makeSelectOneSql(String table, Map<String, Object> whereMap)  {
        String sql = "";
        if (null != whereMap && whereMap.size() > 0) {
            sql = SqlUtil.makeSelectAllSql(table, whereMap) + " limit 1";
        }
        else{
            sql="select * from "+table+" limit 1";
        }
        return sql;
    }
	

	/**
	 * 按条件分页查找,拼成sql.条件放到whereMap里，可以排序
	 * @param whereMap
	 * @param table
	 * @param orderBy
	 * @param curPageNo
	 * @param pageSize
	 * @return
	 */
	public static String makeSelectAllSqlWithOrder(String table, Map<String, Object> whereMap,
                                                   String orderBy, int curPageNo, int pageSize) {
		String sql = "";
		if (null != whereMap && whereMap.size() > 0) {
			sql = SqlUtil.makeSelectAllSql(table, whereMap) +" "+orderBy+ " limit "
					+  (curPageNo-1)*pageSize+","+pageSize;
		} else {
			sql = "select * from " + table +" "+orderBy+ " limit " + (curPageNo-1)*pageSize+","+pageSize;
		}
		return sql;
	}

	public static String makeSelectAllSqlWithOrder(String table, Map<String, Object> whereMap, String orderBy) {
		String sql = "";
		if (null != whereMap && whereMap.size() > 0) {
			sql = SqlUtil.makeSelectAllSql(table, whereMap) +" "+orderBy;
		} else {
			sql = "select * from " + table +" "+orderBy;
		}
		return sql;
	}
	
	/**
	 * 生成count语句
	 * @param whereMap
	 * @return
	 */
	public static String makeCountSql(String table, Map<String, Object> whereMap) {
		StringBuffer sb =new StringBuffer();
		sb.append(" select count(*)  from ").append(table);
		sb.append(dealMap( whereMap));
		return sb.toString();
	}

	/**
	 * 生成count like语句
	 * @param table
	 * @param likeMap
	 * @return
	 */
	public static String makeCountLikeSql(String table, Map<String, Object> likeMap) {
		StringBuffer sb =new StringBuffer();
		sb.append(" select count(*)  from ").append(table);
		sb.append(dealLikeMap( likeMap));
		return sb.toString();
	}

	/**
	 * 生成count where and like语句
	 * @param table
	 * @param whereMap
	 * @param likeMap
	 * @return
	 */
	public static String makeCountWithLikeSql(String table, Map<String, Object> whereMap, Map<String, Object> likeMap) {
		StringBuffer sb =new StringBuffer();
		sb.append(" select count(*)  from ").append(table);
		sb.append(dealWithLikeMap(whereMap,likeMap));
		return sb.toString();
	}


	/**
	 * 拼接 select * SQL where ... like语句进行模糊查找
	 * @param tableName
	 * @param likeMap
	 * @return
	 */
	public static String makeSelectAllLikeSql(String tableName, Map<String, Object> likeMap) {

		int ws = (null != likeMap) ? likeMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ws * 32);
		sql.append("\n select * from ").append(tableName);
		if(ws>0){
			sql.append("\n where ");// 拼接where子句
		}
		int index = 0;
		for (String key : likeMap.keySet()) {
			Object v = likeMap.get(key);
			sql.append(key).append(" like '%").append(sqlLikeValue(v)).append("%'");
			index++;
			if (index < ws) {
				sql.append("\n   and ");
			}
		}
		return sql.toString();
	}
	public static String makeSelectAllInSql(String tableName, Map<String,List<Object>> inMap) {
		int ins = (null != inMap) ? inMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ins * 32);
		// 拼接set子句
		sql.append("\n select * from ").append(tableName).append("\n ");
		int index = 0;
		if(ins>0)
		{
			// 拼接where子句
			sql.append("\n where ");
			index = 0;
			for (String key : inMap.keySet()) {
				List<Object> v = inMap.get(key);
				int vsize = v.size();
				if(v !=null && !v.isEmpty())
				{
					sql.append(key).append(" in(");
					int vindex =0;
					for(Object obj : v)
					{
						sql.append(sqlValue(obj));
						vindex++;
						if(vindex<vsize)
						{
							sql.append(",");
						}
					}
					sql.append(")");
					index++;
					if (index < ins) {
						sql.append("\n   and ");
					}
				}
			}
		}
		return sql.toString();
	}
	public static String makeCountInSql(String tableName, Map<String,List<Object>> inMap) {
		int ins = (null != inMap) ? inMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ins * 32);
		// 拼接set子句
		sql.append("\n select count(*) from ").append(tableName).append("\n ");
		int index = 0;
		if(ins>0)
		{
			// 拼接where子句
			sql.append("\n where ");
			index = 0;
			for (String key : inMap.keySet()) {
				List<Object> v = inMap.get(key);
				int vsize = v.size();
				if(v !=null && !v.isEmpty())
				{
					sql.append(key).append(" in(");
					int vindex =0;
					for(Object obj : v)
					{
						sql.append(sqlValue(obj));
						vindex++;
						if(vindex<vsize)
						{
							sql.append(",");
						}
					}
					sql.append(")");
					index++;
					if (index < ins) {
						sql.append("\n   and ");
					}
				}
			}
		}
		return sql.toString();
	}

	public static String makeSelectAllWithInSql(String tableName, Map<String,Object> whereMap, Map<String,List<Object>> inMap) {
		int ins = (null != inMap) ? inMap.size() : 0;
		int ws = (null != whereMap) ? whereMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ins * 32 + ws*32);
		// 拼接set子句
		sql.append("\n select * from ").append(tableName).append("\n ");
		int index = 0;
		if(ins>0)
		{
			// 拼接where子句
			sql.append("\n where ");
			index = 0;
			for (String key : inMap.keySet()) {
				List<Object> v = inMap.get(key);
				int vsize = v.size();
				if(v !=null && !v.isEmpty())
				{
					sql.append(key).append(" in(");
					int vindex =0;
					for(Object obj : v)
					{
						sql.append(sqlValue(obj));
						vindex++;
						if(vindex<vsize)
						{
							sql.append(",");
						}
					}
					sql.append(")");
					index++;
					if (index < ins) {
						sql.append("\n   and ");
					}
				}
			}
		}
		if(ws>0)
		{
			if(ins>0)
			{
				sql.append("\n and ");
			}
			else
			{
				sql.append("\n where ");
			}
			index=0;
			for (String key : whereMap.keySet()) {
				Object v = whereMap.get(key);
				sql.append(key).append("=").append(SqlUtil.sqlValue(v));
				index++;
				if (index < ws) {
					sql.append("\n   and ");
				}
			}
		}
		return sql.toString();
	}

	public static String makeCountWithInSql(String tableName, Map<String,Object> whereMap, Map<String,List<Object>> inMap) {
		int ins = (null != inMap) ? inMap.size() : 0;
		int ws = (null != whereMap) ? whereMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ins * 32 + ws*32);
		// 拼接set子句
		sql.append("\n select * from ").append(tableName).append("\n ");
		int index = 0;
		if(ins>0)
		{
			// 拼接where子句
			sql.append("\n where ");
			index = 0;
			for (String key : inMap.keySet()) {
				List<Object> v = inMap.get(key);
				int vsize = v.size();
				if(v !=null && !v.isEmpty())
				{
					sql.append(key).append(" in(");
					int vindex =0;
					for(Object obj : v)
					{
						sql.append(sqlValue(obj));
						vindex++;
						if(vindex<vsize)
						{
							sql.append(",");
						}
					}
					sql.append(")");
					index++;
					if (index < ins) {
						sql.append("\n   and ");
					}
				}
			}
		}
		if(ws>0)
		{
			if(ins>0)
			{
				sql.append("\n and ");
			}
			else
			{
				sql.append("\n where ");
			}
			index=0;
			for (String key : whereMap.keySet()) {
				Object v = whereMap.get(key);
				sql.append(key).append("=").append(SqlUtil.sqlValue(v));
				index++;
				if (index < ws) {
					sql.append("\n   and ");
				}
			}
		}
		return sql.toString();
	}

	/**
	 * 拼接 select * SQL where ... like语句进行精确+模糊查找
	 * @param tableName
	 * @param whereMap
	 * @param likeMap
	 * @return
	 */
	public static String makeSelectAllWithLikeSql(String tableName, Map<String, Object> whereMap, Map<String, Object> likeMap) {

		int ws = (null != whereMap) ? whereMap.size() : 0;
		StringBuilder sql = new StringBuilder(64 + ws * 32);
		sql.append("\n select * from ").append(tableName);
		if(ws>0){
			sql.append("\n where ");// 拼接where子句
		}
		int index = 0;
		for (String key : whereMap.keySet()) {
			Object v = whereMap.get(key);
			sql.append(key).append("=").append(sqlValue(v));
			index++;
			if (index < ws) {
				sql.append("\n   and ");
			}
		}
		int wsLike = (null != likeMap) ? likeMap.size() : 0;
		if(wsLike>0)
		{
			if(ws==0)
			{
				sql.append("\n where ");// 拼接where子句
			}
			else
			{
				sql.append("\n and ");// 拼接where子句
			}
			index = 0;
			for (String key : likeMap.keySet()) {
				Object v = likeMap.get(key);
				sql.append(key).append(" like '%").append(sqlLikeValue(v)).append("%'");
				index++;
				if (index < wsLike) {
					sql.append("\n   and ");
				}
			}
		}
		return sql.toString();
	}

	
	/**
	 * 防止非法实例化
	 */
	private SqlUtil() { }
}
