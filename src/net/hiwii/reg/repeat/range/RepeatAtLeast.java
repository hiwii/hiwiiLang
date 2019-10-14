package net.hiwii.reg.repeat.range;

import net.hiwii.reg.RegularExpression;
import net.hiwii.reg.repeat.RepeatRegular;
import net.hiwii.system.exception.ApplicationException;

public class RepeatAtLeast extends RepeatRegular {

	private int min;
	public RepeatAtLeast(RegularExpression regular, int min) {
		super(regular);
		this.min = min;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	/**
	 * great than max
	 * @param times
	 * @return
	 */
	public boolean gtMin(int times){
		if(times >= min){
			return true;
		}else{
			return false;
		}
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
				
		int cnt = 0;
		int len = std.length();
		int start = pos;

		if(len == 0){
			return 0;
		}
		
		if(min == 0 && !greedy){
			return 0;
		}
		while(true){
			if(str.substring(pos, pos + len).equals(std)){
				cnt++;
				if(cnt == min && !greedy){
					return min * len;
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
		
		if(cnt < min){
			return 0;
		}else{
			return cnt * len;
		}
	}
	
	@Override
	public boolean guide(String str, int pos, boolean forward) {
		if(min == 0){
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
				if(cnt == min){
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
