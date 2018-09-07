package com.chuyu.utils.bean;


import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 扩展org.apache.commons.beanutils.BeanUtils<br>
 * 
 * @author Wesley<br>
 * 
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(BeanUtils.class);
    /**
     * 日志头
     */
    private static String logPre = "[]";
    
	/**
	 * 将源对象中的值覆盖到目标对象中，仅覆盖源对象中不为NULL值的属性
	 * 
	 * @param dest
	 *            目标对象，标准的JavaBean
	 * @param orig
	 *            源对象，可为Map、标准的JavaBean
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public static void applyIf(Object dest, Object orig) throws Exception {
		try {
			if (orig instanceof Map) {
				Iterator names = ((Map) orig).keySet().iterator();
				while (names.hasNext()) {
					String name = (String) names.next();
					if (PropertyUtils.isWriteable(dest, name)) {
						Object value = ((Map) orig).get(name);
						if (value != null) {
							PropertyUtils.setSimpleProperty(dest, name, value);
						}
					}
				}
			} else {
				java.lang.reflect.Field[] fields = orig.getClass().getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					String name = fields[i].getName();
					if (PropertyUtils.isReadable(orig, name) && PropertyUtils.isWriteable(dest, name)) {
						Object value = PropertyUtils.getSimpleProperty(orig, name);
						if (value != null) {
							PropertyUtils.setSimpleProperty(dest, name, value);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("将源对象中的值覆盖到目标对象中，仅覆盖源对象中不为NULL值的属性", e);
		}
	}

	/**
	 * 将源对象中的值覆盖到目标对象中，仅覆盖源对象中不为NULL值的属性
	 * 
	 * @param orig
	 *            源对象，标准的JavaBean
	 * @param dest
	 *            排除检查的属性，Map
	 * 
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public static boolean checkObjProperty(Object orig, Map dest) throws Exception {
		try {
			java.lang.reflect.Field[] fields = orig.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				String name = fields[i].getName();
				if (!dest.containsKey(name)) {
					if (PropertyUtils.isReadable(orig, name)) {
						Object value = PropertyUtils.getSimpleProperty(orig, name);
						if (value == null) {
							return true;
						}
					}
				}
			}
			return false;
		} catch (Exception e) {
			throw new Exception("将源对象中的值覆盖到目标对象中，仅覆盖源对象中不为NULL值的属性", e);
		}
	}
	
	
	 /**
     * 转换时对map中的key里的字符串会做忽略处理的正则串
     */
    private static final String OMIT_REG = "_";
 
    /**
     * 将map集合转换成Bean集合，Bean的属性名与map的key值对应时不区分大小写，并对map中key做忽略OMIT_REG正则处理
     * 
     * @param <E>
     * @param cla
     * @param mapList
     * @return
     */
    public static <E> List<E> toBeanList(Class<E> cla,
                                         List<Map<String, Object>> mapList) {
 
        List<E> list = new ArrayList<E>(mapList.size());
 
        for (Map<String, Object> map : mapList) {
            E obj = toBean(cla, map);
            list.add(obj);
        }
 
        return list;
    }
    
    
    /**
     * 将map转换成Bean，Bean的属性名与map的key值对应时不区分大小写，并对map中key做忽略OMIT_REG正则处理
     * 
     * @param <E>
     * @param cla
     * @param map
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    public static <E> E toBean(Class<E> cla, Map<String, Object> map) {
 
        // 创建对象
        E obj = null;
        try {
            obj = cla.newInstance();
            if (obj == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            logger.error("类型实例化对象失败,类型:" + cla);
            return null;
        }
 
        // 处理map的key
        Map<String, Object> newmap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> en : map.entrySet()) {
            newmap.put(
                    "set"
                            + en.getKey().trim().replaceAll(OMIT_REG, "")
                                    .toLowerCase(), en.getValue());
        }
 
        // 进行值装入
        Method[] ms = cla.getMethods();
        for (Method method : ms) {
            String mname = method.getName().toLowerCase();
            if (mname.startsWith("set")) {
 
                Class[] clas = method.getParameterTypes();
                Object v = newmap.get(mname);
 
                if (v != null && clas.length == 1) {
                    try {
                        method.invoke(obj, v);
                    } catch (Exception e) {
                        logger.error("属性值装入失败,装入方法：" + cla + "."
                                + method.getName() + ".可装入类型" + clas[0]
                                + ";欲装入值的类型:" + v.getClass());
                    }
                }
            }
        }
 
        return obj;
    }
}
