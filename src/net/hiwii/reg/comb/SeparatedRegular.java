package net.hiwii.reg.comb;

import net.hiwii.reg.RegularExpression;
import net.hiwii.system.SystemCharacter;

/**
 * 至少有一个","
 * @author Administrator
 *
 */
public class SeparatedRegular extends RegularExpression {
	@Override
	public boolean match(String str) {
//		int pos = str.indexOf(',');
//		if(pos < 0){
//			return false;
//		}
		
		int start = 0;
		while(start < str.length()){
			char ch = str.charAt(start);
			if(SystemCharacter.isInvisiable(ch)){
				start++;
				continue;
			}else{
				break;
			}
		}

		int end = str.length() - 1;
		if(end <= start){
			return false;//empty string
		}

		while(end > start){
			char ch = str.charAt(start);
			end--;
			if(SystemCharacter.isInvisiable(ch)){				
				continue;
			}else if(ch == ')'){
				break;
			}else{
				return false;
			}
		}

		return false;
	}
}
