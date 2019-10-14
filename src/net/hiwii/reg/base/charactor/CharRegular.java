package net.hiwii.reg.base.charactor;

import net.hiwii.reg.RegularExpression;
import net.hiwii.system.SystemCharacter;

/**
 * 字符分为如下类：
 * 1，letter
 * 2，digit
 * 3，special symbol
 * 4，invisible
 * 5，Others
 * @author Administrator
 *
 */
public class CharRegular extends RegularExpression {

	@Override
	public boolean match(String str) {
		return SystemCharacter.isChar(str);
	}

	@Override
	public int count(String str, int pos, boolean forward, boolean greedy) {
		if(!guide(str, pos, forward)){
			return 0;
		}
		return 1;
	}

	@Override
	public boolean guide(String str, int pos, boolean forward) {
		if((pos >= 0 && pos < str.length()) && str.length() > 0){
			return true;
		}else{
			return false;
		}
	}

	public boolean isChar(int ch) {
		return true;
	}
}
