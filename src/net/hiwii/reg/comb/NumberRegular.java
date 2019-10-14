package net.hiwii.reg.comb;

import net.hiwii.reg.RegularExpression;
import net.hiwii.reg.atom.FloatRegular;
import net.hiwii.reg.atom.FractionRegular;
import net.hiwii.reg.atom.IntegerRegular;
import net.hiwii.reg.atom.ScientificNotationRegular;

public class NumberRegular extends RegularExpression {
	@Override
	public boolean match(String str) {
		if(new IntegerRegular().match(str)){
			return true;
		}else if(new FloatRegular().match(str)){
			return true;
		}else if(new FractionRegular().match(str)){
			return true;
		}else if(new ScientificNotationRegular().match(str)){
			return true;
		}
		return false;
	}
}
