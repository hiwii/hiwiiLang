package net.hiwii.reg.repeat;

import net.hiwii.reg.NotRegular;
import net.hiwii.reg.RegularExpression;
import net.hiwii.reg.SimpleRegular;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.EntityUtil;

public class RepeatTimes extends RepeatRegular {

	private int times;
	public RepeatTimes(RegularExpression regular, int times) {
		super(regular);
		this.times = times;
	}
	
	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	@Override
	public boolean match(String str) {
		boolean guide = getRegular().guide(str, 0, true);
		if(!guide){
			return false;
		}
		int matchlen = 0;
		int len = str.length();
		RegularExpression not = new NotRegular(getRegular());
		if(len == 0){ //""是唯一匹配
			if(getRegular().match("")){
				return true;
			}else{
				return false;
			}
		}
		
		//以下len > 0, 因此Regular匹配必须大于0
		int start = 0, end = 2;
		

		while(end <= len){
			String item = str.substring(0, end);//从itemLen == 1开始，因此end=2

//			boolean test = true; //item不匹配
			if(len / item.length() < times){
				return false;
			}
			if(getRegular().match(item)){
				if(len / item.length() == times && len % item.length() == 0){
					return true;
				}
			}
			end++;
		}
		return false;

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
		
		int cnt = 0;
		int len = 0;
		int start = pos;

		if(len == 0){
			return true;
		}
		while(true){
			if(getRegular().guide(str, start, forward)){
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
