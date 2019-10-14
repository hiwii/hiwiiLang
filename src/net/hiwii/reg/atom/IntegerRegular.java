package net.hiwii.reg.atom;

import net.hiwii.reg.RegularExpression;

public class IntegerRegular extends RegularExpression {

	@Override
	public boolean match(String str) {
		if(str.length() == 0){
			return false;
		}
		boolean ret = true;
		for(int i=0;i<str.length();i++){
			char ch = str.charAt(i);
			if(!Character.isDigit(ch)){
				ret = false;
				break;
			}
		}
		return ret;
	}

}
