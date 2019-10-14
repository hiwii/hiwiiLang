package net.hiwii.reg.base.charactor;



public class DigitRegular extends CharRegular {
	@Override
	public boolean match(String str) {
		if(str.length() == 1){
			char ch = str.charAt(0);
			return Character.isDigit(ch);
		}else{
			return false;
		}
	}

}
