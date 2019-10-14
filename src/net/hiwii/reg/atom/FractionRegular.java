package net.hiwii.reg.atom;

import net.hiwii.reg.RegularExpression;

public class FractionRegular extends RegularExpression {
	@Override
	public boolean match(String str) {
		int pos = str.indexOf('/');
		if(pos <= 0){
			return false;
		}

		for(int i=0;i<pos;i++){
			char ch = str.charAt(i);
			if(!Character.isDigit(ch)){
				return false;
			}
		}
		
		for(int i = pos + 1;i < str.length();i++){
			char ch = str.charAt(i);
			if(!Character.isDigit(ch)){
				return false;
			}
		}
		return true;
	}
}
