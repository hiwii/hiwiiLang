package net.hiwii.system.util;

import java.util.HashMap;
import java.util.Map;

import net.hiwii.system.exception.ApplicationException;

/**
 * ���ڼ�¼��������
 * ��������ÿ���ڵ��ʾһ�����壬�����ӽڵ����Ӷ��塣
 * ÿ���Ӷ����id�����и��������Ϣ��
 * @author ha-wangzhenhai
 *
 */
public class DefinitionTree {
	private static Map<String,String> defs;
	static{
		defs = new HashMap<String,String>();
		defs.put("Entity", "0");
		
		defs.put("Object", "0.0");
		defs.put("Cognition", "0.1");
		
		defs.put("LocalHost", "0.0.1");
		defs.put("Set", "0.0.0");
		
		defs.put("Set", "0.1.0");
		defs.put("List", "0.1.1");
		defs.put("String", "0.1.2");
		defs.put("Number", "0.1.3");
		
		defs.put("Integer", "0.1.3.0");
		defs.put("Float", "0.1.3.1");
	}

	public static String getIdentifier(String defName) throws ApplicationException{
		if(defs.containsKey(defName)){
			return defs.get(defName);
		}else{
			throw new ApplicationException("err");
		}
	}
	
	public static boolean isIdentified(String defName){
		if(defs.containsKey(defName)){
			return true;
		}else{
			return false;
		}
	}
}
