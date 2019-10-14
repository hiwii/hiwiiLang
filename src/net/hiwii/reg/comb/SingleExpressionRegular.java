package net.hiwii.reg.comb;

import net.hiwii.reg.RegularExpression;
import net.hiwii.reg.atom.IdentifierRegular;
import net.hiwii.reg.atom.StringRegular;
import net.hiwii.reg.base.FunctionRegular;
import net.hiwii.reg.base.MappingRegular;

/**
 * SingleExpression���Զ���ʹ�ã��ڼ���ʹ��ʱ�������ö��ŵȷָ����ֿ���
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
