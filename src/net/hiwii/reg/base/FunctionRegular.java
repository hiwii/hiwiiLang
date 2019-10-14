package net.hiwii.reg.base;

import net.hiwii.reg.RegularExpression;
import net.hiwii.reg.atom.IdentifierRegular;
import net.hiwii.reg.comb.SingleExpressionRegular;
import net.hiwii.system.SystemCharacter;

public class FunctionRegular extends RegularExpression {
	@Override
	public boolean match(String str) {
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
		int count = new IdentifierRegular().count(str, start, true, true);
		if(count <= 0){
			return false;
		}
		int pos = start + count;
		while(pos < str.length()){
			char ch = str.charAt(pos);
			pos++;
			if(SystemCharacter.isInvisiable(ch)){				
				continue;
			}else if(ch == '('){
				break;
			}else{
				return false;
			}
		}

		int end = str.length() - 1;
		if(end <= pos){
			return false;
		}
		while(end > pos){
			char ch = str.charAt(pos);
			end--;
			if(SystemCharacter.isInvisiable(ch)){				
				continue;
			}else if(ch == ')'){
				break;
			}else{
				return false;
			}
		}
		
		if(pos + 1 == end){
			return true;
		}
		String args = str.substring(pos + 1, end);
		if(new SingleExpressionRegular().match(args)){
			return true;
		}
		if(new SingleExpressionRegular().match(args)){
			return true;
		}
		return false;
	}
}
