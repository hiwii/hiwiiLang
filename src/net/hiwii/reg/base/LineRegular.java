package net.hiwii.reg.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.hiwii.reg.RegularExpression;

public class LineRegular extends RegularExpression {

	@Override
	public boolean match(String str) {
		String trim = str.trim();
		int pos = getLineEnd(trim);
		if(pos > 0){
			return false;//¶àÓàÒ»¸ölineEnd
		}else{
			return true;
		}
	}

	@Override
	public int count(String str, int pos, boolean forward, boolean greedy) {
		return super.count(str, pos, forward, greedy);
	}

	@Override
	public boolean guide(String str, int pos, boolean forward) {
		return true;
	}
	
	public int getLineEnd(String str){
		Pattern p = Pattern.compile("\r\n|\n|\r");
		Matcher m = p.matcher(str);
		boolean b = m.find();
		if(b){
			return m.start();
		}else{
			return -1;
		}
	}
}
