package net.hiwii.system.util;

import java.util.Map;

import net.hiwii.system.net.format.PackageHeader;

public class PackageUtil {
	public static String makeHeader(PackageHeader headers){
		String ret = "";
		int n = 0;
		for(Map.Entry<String,String> entry:headers){
			if(n == 0){
				ret = entry.getKey() + ":" + entry.getValue() + "\r\n";
			}else{
				ret = ret + entry.getKey() + ":" + entry.getValue() + "\r\n";
			}
			n++;
		}
		
		ret = ret + "\r\n"; //two empty lines.
		return ret;
	}
	
	public static PackageHeader parseHeaders(String header){
		PackageHeader ret = new PackageHeader();
		String[] part = header.split("\r\n");
		for(String pair:part){
			int pos = pair.indexOf(':');
			if(pos > 0){
				ret.put(pair.substring(0, pos).trim(), pair.substring(pos + 1).trim());
			}
		}
		return ret;
	}
}
