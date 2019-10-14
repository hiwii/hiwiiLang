package net.hiwii.reg.base.sub;

import net.hiwii.reg.RegularExpression;
import net.hiwii.system.SystemCharacter;

public class EmptyRegular extends RegularExpression {

	@Override
	public boolean match(String str) {
		if(str.length() == 0 ){
			return true;
		}
		for(int i=0; i < str.length(); i++){
			char ch = str.charAt(i);
			if(!SystemCharacter.isInvisiable(ch)){
				return false;
			}
		}
		return true;
	}

}
