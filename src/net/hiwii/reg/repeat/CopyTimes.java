package net.hiwii.reg.repeat;

import net.hiwii.reg.RegularExpression;
import net.hiwii.system.exception.ApplicationException;


public class CopyTimes extends RepeatRegular {
	private int times;
	public CopyTimes(RegularExpression regular, int times) {
		super(regular);
		this.times = times;
	}
	
	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	/**
	 * 从string参数中获取标准字符串。
	 * @param str
	 * @return
	 */
//	public String stand(String str){
//		if(str.length() == 0){
//	}
	@Override
	public boolean match(String str) {
		// TODO Auto-generated method stub
		return super.match(str);
	}

	/**
	 * forward;
	 */
	@Override
	public int count(String str, int pos, boolean forward, boolean greedy) {
		if(!getRegular().guide(str, pos, forward)){
			return 0;
		}
		String std = null;
		try {
			std = getMaxLen(str, pos, forward);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(times == 0){
			return 0;
		}
		
		int cnt = 0;
		int len = std.length();
		int start = pos;

		if(len == 0){
			return 0;
		}
		while(true){
			if(str.substring(pos, pos + len).equals(std)){
				cnt++;
				if(cnt == times){
					return times * len;
				}
				if(forward){
					start = start + len;
					if(start >= str.length()){
						break;
					}
				}else{
					start = start - len;
					if(start < 0){
						break;
					}
				}
			}else{
				break;
			}
		}
		return 0;
	}
	
	@Override
	public boolean guide(String str, int pos, boolean forward) {
		if(times == 0){
			return true;
		}
		if(!getRegular().guide(str, pos, forward)){
			return false;
		}
		
		String std = null;
		try {
			std = getMaxLen(str, pos, forward);
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int cnt = 0;
		int len = std.length();
		int start = pos;

		if(len == 0){
			return true;
		}
		while(true){
			if(str.substring(pos, pos + len).equals(std)){
				cnt++;
				if(cnt == times){
					return true;
				}
				if(forward){
					start = start + len;
					if(start >= str.length()){
						break;
					}
				}else{
					start = start - len;
					if(start < 0){
						break;
					}
				}
			}else{
				break;
			}
		}
		
		return false;
	}

}
