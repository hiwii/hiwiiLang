package net.hiwii.protocol;

import java.util.HashMap;
import java.util.Map;

public class HiwiiMessage {
	private String tag;
	private Map<String,String> headers;
	private String content;
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public void putHeader(String key, String value){
		if(headers == null){
			headers = new HashMap<String,String>();
		}
		headers.put(key, value);
	}
	
	public String getHeader(String key){
		if(headers == null){
			return null;
		}
		if(headers.containsKey(key)){
			return headers.get(key);
		}
		return null;
	}
}
