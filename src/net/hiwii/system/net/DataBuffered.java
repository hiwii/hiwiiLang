package net.hiwii.system.net;

import java.nio.charset.Charset;

import net.hiwii.system.exception.ApplicationException;
import net.hiwii.view.Entity;

public class DataBuffered extends Entity {
	private byte[] buffer = new byte[1024 * 1024];
	private int position;
	private int limit;
	private int count;
	
	public DataBuffered() {
		count = 0;		
	}
	public byte[] getBuffer() {
		return buffer;
	}
	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public int getPort() throws ApplicationException{
		if(count >= 6){
			String p = new String(buffer, 0, 6).trim();
			return Integer.parseInt(p);
		}
		throw new ApplicationException();
	}
	
	public String getType() throws ApplicationException{
		if(count >= 9){
			String p = new String(buffer, 6, 3).trim();
			return p;
		}
		throw new ApplicationException();
	}
	
	public String getLocalId() throws ApplicationException{
		if(count >= 45){
			String p = new String(buffer, 13, 32).trim();
			return p;
		}
		throw new ApplicationException();
	}
	
	public String getRomoteId() throws ApplicationException{
		if(count >= 77){
			String p = new String(buffer, 45, 32).trim();
			return p;
		}
		throw new ApplicationException();
	}
	
	public boolean keepAlive() throws ApplicationException{
		if(count >= 13){
			String p = new String(buffer, 9, 13).trim();
			if(p.equals("talk")){
				return false;
			}else if(p.equals("talk")){
				return true;
			}else{
				throw new ApplicationException();
			}
		}
		throw new ApplicationException();
	}
	
	public String getContent() throws ApplicationException{
		if(count > 77){
			String p = new String(buffer, 77, count + 1, Charset.forName("UTF-8")).trim();
			return p;
		}
		throw new ApplicationException();
	}
}
