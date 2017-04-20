/**
* Copyright (C) 2008 Happy Fish / YuQing
*
* FastDFS Java Client may be copied only under the terms of the GNU Lesser
* General Public License (LGPL).
* Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
**/

package org.csource.common;

import java.io.*;
import java.util.*;
import org.csource.common.*;

/**
* ini file reader / parser
* @author Happy Fish / YuQing
* @version Version 1.0
*/
public class IniFileReader
{
	private Hashtable paramTable;
	private String conf_filename;
	
/**
* @param conf_filename config filename
*/
	public IniFileReader(String conf_filename) throws FileNotFoundException, IOException
	{
		this.conf_filename = conf_filename;
		loadFromFile(conf_filename);
	}
	
/**
* get the config filename
* @return config filename
*/
	public String getConfFilename()
	{
		return this.conf_filename;
	}
	
/**
* get string value from config file
* @param name item name in config file
* @return string value
*/
	public String getStrValue(String name)
	{
		Object obj;
		obj = this.paramTable.get(name);
		if (obj == null)
		{
			return null;
		}
		
		if (obj instanceof String)
		{
			return (String)obj;
		}
		
		return (String)((ArrayList)obj).get(0);
	}

/**
* get int value from config file
* @param name item name in config file
* @param default_value the default value
* @return int value
*/
	public int getIntValue(String name, int default_value)
	{
		String szValue = this.getStrValue(name);
		if (szValue == null)
		{
			return default_value;
		}
		
		return Integer.parseInt(szValue);
	}

/**
* get boolean value from config file
* @param name item name in config file
* @param default_value the default value
* @return boolean value
*/
	public boolean getBoolValue(String name, boolean default_value)
	{
		String szValue = this.getStrValue(name);
		if (szValue == null)
		{
			return default_value;
		}
		
		return szValue.equalsIgnoreCase("yes") || szValue.equalsIgnoreCase("on") || 
					 szValue.equalsIgnoreCase("true") || szValue.equals("1");
	}
	
/**
* get all values from config file
* @param name item name in config file
* @return string values (array)
*/
	public String[] getValues(String name)
	{
		Object obj;
		String[] values;
		
		obj = this.paramTable.get(name);
		if (obj == null)
		{
			return null;
		}
		
		if (obj instanceof String)
		{
			values = new String[1];
			values[0] = (String)obj;
			return values;
		}
		
		Object[] objs = ((ArrayList)obj).toArray();
		values = new String[objs.length];
		System.arraycopy(objs, 0, values, 0, objs.length);
		return values;
	}
	
	private void loadFromFile(String conf_filename) throws FileNotFoundException, IOException
    {
        //问题说明 使用中发现原来客户端打jar包后，在另一个项目中引用，另一个项目打jar包后运行时找不到客户端配置文件
        String name;
        String value;
        Object obj;
        ArrayList valueList;
        InputStream is=null;
        this.paramTable = new Hashtable();

        try
        {
            Properties props;
            try {
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream(conf_filename);
                props = new Properties();
                props.load(is);
            } catch (Exception ex) {
                is = new FileInputStream(conf_filename);
                props = new Properties();
                props.load(is);
            }
            Iterator<Map.Entry<Object, Object>> it = props.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Object, Object> entry = it.next();
                name= entry.getKey().toString();
                value = entry.getValue().toString();
                obj = this.paramTable.get(name);
                if (obj == null)
                {
                    this.paramTable.put(name, value);
                }
                else if (obj instanceof String)
                {
                    valueList = new ArrayList();
                    valueList.add(obj);
                    valueList.add(value);
                    this.paramTable.put(name, valueList);
                }
                else
                {
                    valueList = (ArrayList)obj;
                    valueList.add(value);
                }
            }
        }
        finally
        {
            if (is != null) {
                is.close();
            }
        }
    }
}
