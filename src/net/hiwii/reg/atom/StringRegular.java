package net.hiwii.reg.atom;

import net.hiwii.reg.RegularExpression;
import net.hiwii.system.SystemCharacter;

public class StringRegular extends RegularExpression {

	@Override
	public boolean match(String str) {
		int len = str.length();
		if(len < 2){ //at least ""
			return false;
		}
		if((str.charAt(0) != '\"') || (str.charAt(len - 1) != '\"')){
			return false;
		}
		if(len == 2){
			return true; //empty string
		}
		boolean ret = true;
		int pos = 1;
		while(pos <= len - 2){
			char ch = str.charAt(pos);
			if(ch == '\\'){
				if(pos == len - 2){
					return false; //last transfer char, should follow by n,r
				}
				pos++;
				char next = str.charAt(pos);
				if(!(next == 'r' || next == 'n'  || next == 'f' || next == 'b'
						 || next == 't'|| next == '\\')){
					return false;
				}
				continue;
			}
			int code = str.codePointAt(pos);
			if(SystemCharacter.isChar(code)){
				ret = false;
				break;
			}
			pos = pos + Character.charCount(code);
		}
		return ret;
	}

	
}
