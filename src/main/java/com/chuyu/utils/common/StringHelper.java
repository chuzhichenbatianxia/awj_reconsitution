package com.chuyu.utils.common;



import java.util.ArrayList;
import java.util.List;

public class StringHelper {

	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(Object str) {
		return str==null || str.toString().trim().length()==0;
	}

	/**
	 * 判断字符串是否不为null或者空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(Object str)
	{
		return !isNullOrEmpty(str);
	}

	/**
	 * 将列表转换为带分隔符的字符串
	 * @param list
	 * @param separator
	 * @return
	 */
	public static String listToString(List list, char separator)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++)
		{
			sb.append(list.get(i)).append(separator);
		}
		String s = sb.toString();
		return s.substring(0,s.length()-1);
	}

	public static boolean IsAllNullOrEmpty(Object... str)
	{
		for(Object obj : str)
		{
			if(!StringHelper.isNullOrEmpty(str))
			{
				return false;
			}
		}
		return true;
	}

	public static boolean existNullOrEmpty(Object... str)
	{
		for(Object obj : str)
		{
			if(StringHelper.isNullOrEmpty(str))
			{
				return true;
			}
		}
		return false;
	}

	public static List<String> convertStringToList(String str, String seperator)
	{
		ArrayList<String> list = new ArrayList<>();
		if(str!=null)
		{
			String[] strs = str.split(seperator);
			for(String s : strs)
			{
				if(!isNullOrEmpty(s))
				{
					list.add(s);
				}
			}
		}
		return list;
	}


	public static List<Integer> convertStringToIntegerList(String str, String seperator)
	{
		ArrayList<Integer> list = new ArrayList<>();
		if(str!=null)
		{
			String[] strs = str.split(seperator);
			for(String s : strs)
			{
				if(!isNullOrEmpty(s))
				{
					list.add(Integer.valueOf(s));
				}
			}
		}
		return list;
	}

	public static String[] convertStringToArray(String str, String seperator)
	{
		if(str==null)
		{
			return null;
		}
		if(str.endsWith(seperator))
		{
			str = str.substring(0,str.lastIndexOf(seperator));
		}
		return str.split(seperator);
	}

	public static Integer[] convertStringToIntegerArray(String str, String seperator)
	{
		String[] strTemp = convertStringToArray(str,seperator);
		Integer[] array = new Integer[strTemp.length];
		for(int i =0 ; i<strTemp.length;i++)
		{
			array[i] = Integer.valueOf(strTemp[i]);
		}
		return array;
	}

	public static String trimEnd(String str, String seperator)
	{
		if(str==null || !str.endsWith(seperator))
		{
			return str;
		}
		return str.substring(0,str.lastIndexOf(seperator));
	}

	/**
	 * 补空格
	 * @param dataString 传入数据
	 * @param length	格式长度
	 * @return
	 */
	public static String fillBlankformatLength(String dataString, int length) {
		String fill="";
		for(int i =0;i<(length-dataString.length());i++)
		{
			fill+=" ";
		}
		return dataString+fill;
	}
}
