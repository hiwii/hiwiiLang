package net.hiwii.reg.comb;

import net.hiwii.reg.RegularExpression;
import net.hiwii.reg.atom.IdentifierRegular;
import net.hiwii.reg.atom.StringRegular;
import net.hiwii.reg.base.FunctionRegular;
import net.hiwii.reg.base.MappingRegular;

/**
 * SingleExpression可以独立使用，在集合使用时，必须用逗号等分隔符分开。
 * identifier/string/number/binaryNumbe
 * function/mapping
 * binaryExpression/unary/
 * brace/parenExpression/bracket
 * subjectVerb/subjectStatus/subjectNoun
 * numberUnit
 * @author Administrator
 *
 */
public class SingleExpressionRegular extends RegularExpression {

	@Override
	public boolean match(String str) {
		if(str.length() == 0){
			return true; //empty string
		}else if(new IdentifierRegular().match(str)){
			return true;
		}else if(new StringRegular().match(str)){
			return true;
		}else if(new NumberRegular().match(str)){
			return true;
		}else if(new FunctionRegular().match(str)){
			return true;
		}else if(new MappingRegular().match(str)){
			return true;
		}
		return false;
	}

}
