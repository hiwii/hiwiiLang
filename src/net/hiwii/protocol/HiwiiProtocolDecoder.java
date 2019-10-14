package net.hiwii.protocol;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HiwiiProtocolDecoder {
	public static String encode(HiwiiMessage message){
//		ByteBuffer buffer = 
		String ret = "hiwii1.0\r\n";
		Map<String,String> keys = message.getHeaders();
		for(Entry<String,String> entry:keys.entrySet()){
			ret = ret + entry.getKey() + ":" + entry.getValue() + "\r\n";
		}
		return ret;
	}
	
	public static HiwiiMessage decode(ByteBuffer buffer, int len){
		int sep = 0;
		String tag = null;
		int header0 = 0;
		for(int i=0;i<len;i++){
			byte ch = buffer.get(i);
			if(ch == '\r'){
				if(i<len - 1 && buffer.get(i+1) == '\n'){
					byte[] array = new byte[i];
					buffer.get(array);
					tag = new String(array).trim().toLowerCase();
					header0 = i + 2;
					break;
				}
			}
		}
		
		buffer.position(header0);
//		if(!tag.equals("hiwii1.0")){
//			return null;
//		}
		
		//search for double line separator
		boolean found = false;
		for(int i=header0;i<len;i++){
			byte ch = buffer.get(i);
			if(ch == '\r' && i < len - 4){
				if(buffer.get(i+1) == '\n' && buffer.get(i+2) == '\r' && buffer.get(i+3) == '\n'){
					sep = i;
					found = true;
				}
			}
		}
		if(!found){
			return null;
		}
		
		Map<String,String> map = new HashMap<String,String>();
		int start = header0, coma = 0;
		found = false;
		for(int i=header0;i<sep + 1;i++){
			if(buffer.get(i) == '\r'){
				if(i<sep + 1  && buffer.get(i+1) == '\n'){
					for(int j=start;j<i;j++){
						if(buffer.get(j) == ':'){
							coma = j;
							byte[] array0 = new byte[coma - start];
							byte[] array1 = new byte[i - coma -1];
							buffer.get(array0);
							String key = new String(array0);
							buffer.get();  //skip :
							buffer.get(array1);
							String value = new String(array1);
							//TOBE value是一个对象，这里简化为string，待优化
							map.put(key, value);
							buffer.get();
							buffer.get(); //skip \r\n
							start = i + 2;
						}
					}
				}
			}
		}
		
		buffer.get();
		buffer.get(); //skip \r\n
		HiwiiMessage message = new HiwiiMessage();
		message.setTag(tag);
		byte[] array = new byte[len - sep - 4];
		buffer.get(array);
		message.setContent(new String(array));

		message.setHeaders(map);
		return message;
	}
	public static HiwiiMessage decode(byte[] buffer){
		int sep = 0;
		String tag = null;
		int header0 = 0;
		for(int i=0;i<buffer.length;i++){
			byte ch = buffer[i];
			if(ch == '\r'){
				if(i<buffer.length - 1 && buffer[i+1] == '\n'){
					tag = new String(buffer, 0, i).toLowerCase();
					header0 = i + 2;
					break;
				}
			}
		}
		
		if(!tag.equals("hiwii1.0")){
			return null;
		}
		
		//search for double line separator
		boolean found = false;
		for(int i=header0;i<buffer.length;i++){
			byte ch = buffer[i];
			if(ch == '\r' && i < buffer.length - 4){
				if(buffer[i+1] == '\n' && buffer[i+2] == '\r' && buffer[i+3] == '\n'){
					sep = i;
					found = true;
				}
			}
		}
		if(!found){
			return null;
		}
		
		Map<String,String> map = new HashMap<String,String>();
		int start = header0, coma = 0;
		found = false;
		for(int i=header0;i<sep + 1;i++){
			if(buffer[i] == '\r'){
				if(i<sep + 1  && buffer[i+1] == '\n'){
					for(int j=start;j<i;j++){
						if(buffer[j] == ':'){
							coma = j;
							String key = new String(buffer, start, coma - start);
							String value = new String(buffer, coma + 1, i - coma -1);
							//TOBE value是一个对象，这里简化为string，待优化
							map.put(key, value);
							start = i + 2;
						}
					}
				}
			}
		}
		
		HiwiiMessage message = new HiwiiMessage();
		message.setTag(tag);
		message.setContent(new String(buffer, sep + 4, buffer.length - sep - 4));

		message.setHeaders(map);
		return message;
	}
	
	public static void encode(ByteBuffer buffer, String src, String head){
		buffer.clear();
		buffer.put("hiwii1.0\r\n".getBytes());
		HiwiiMessage message = new HiwiiMessage();
		message.putHeader("method", head);
//		message.putHeader(, value);
		for(Entry<String,String> entry:message.getHeaders().entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();
			buffer.put((key + ":" + value + "\r\n").getBytes());
		}
		buffer.put("\r\n".getBytes());
		buffer.put(src.getBytes());
		
		buffer.flip();
	}
}
