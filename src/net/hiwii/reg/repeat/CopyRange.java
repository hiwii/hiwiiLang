package net.hiwii.reg.repeat;

import net.hiwii.reg.RegularExpression;

/**
 * max±ØÐë´óÓÚmin
 * @author Administrator
 *
 */
public class CopyRange extends CopyAtLeast {
	private int max;
	
	public CopyRange(RegularExpression regular, int min, int max) {
		super(regular, min);
		this.max = max;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * less than max
	 * @param times
	 * @return
	 */
	public boolean ltMax(int times){
		if(times <= max){
			return true;
		}else{
			return false;
		}
	}

	
	@Override
	public boolean match(String str) {
		int start = 0, len = 0;
		int cnt = 0;
		while(guide(str, start, true)){
			cnt++;
			len = count(str, start, true, true);
			start = start + len;
			if(start >= str.length()){
				break;
			}
		}
		if(start > str.length()){
			return false;
		}
		if(inRange(cnt)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * interval
	 * @param times
	 * @return
	 */
	public boolean inRange(int times){
		if(times < getMin()){
			return false;
		}
		if(times > max){
			return false;
		}
		return true;
	}
	
	@Override
	public int count(String str, int pos, boolean forward, boolean greedy) {
		int start = pos;
		int cnt = 0;
		int len = 0;
		int sum = 0;
		
		while(true){
			if(getRegular().guide(str, pos, forward)){
				len = getRegular().count(str, start, forward, true);
				sum = sum + len;
				cnt++;
				if(cnt == max){
					return sum;
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
		
		if(inRange(cnt)){
			return sum;
		}else{
			return 0;
		}
	}
	
}
