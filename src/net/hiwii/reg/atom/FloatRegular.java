package net.hiwii.reg.atom;

import net.hiwii.reg.RegularExpression;

public class FloatRegular extends RegularExpression {
	@Override
	public boolean match(String str) {
		int pos = str.indexOf('.');
		if(pos <=0 || pos == str.length() - 1){
			return false;
		}
		String left = str.substring(0, pos);
		String right = str.substring(pos + 1);
		IntegerRegular ir = new IntegerRegular();
		if(ir.match(left) && ir.match(right)){
			return true;
		}
		return false;
	}
}
