package net.hiwii.system.util;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.entity.EntityWrapper;
import net.hiwii.expr.CharExpression;
import net.hiwii.expr.NullExpression;
import net.hiwii.expr.StringExpression;
import net.hiwii.system.syntax.bin.BinaryFormat;
import net.hiwii.system.syntax.bin.HexFormat;
import net.hiwii.system.syntax.bin.OctalFormat;
import net.hiwii.system.syntax.number.DecimalNumber;
import net.hiwii.system.syntax.number.FractionNumber;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.system.syntax.number.ScientificNotation;
import net.hiwii.view.Entity;

public class ExpressionUtil extends Entity {
	public static String mappingToString(String name, List<Expression> args){
		String ret = name + "[";
		for(Expression expr:args){
			ret = ret + expr.toString();
		}
		ret = ret + "]";
		return ret;
	}
	
	/**
	 * �Ƿ������ʽ��
	 * �������ʽ�Ķ����ǣ���������������Ȼ��ԭ���ı��ʽ��
	 * @param expr
	 * @return
	 */
	public static boolean isLiteral(Expression expr){
		if(expr instanceof CharExpression){
			return true;
		}else if(expr instanceof StringExpression){
			return true;
		}else if(expr instanceof FractionNumber){
			return true;
		}else if(expr instanceof ScientificNotation){
			return true;
		}else if(expr instanceof IntegerNumber){//Number Cognition
			return true;
		}else if(expr instanceof DecimalNumber){//Number Cognition
			return true;
		}else if(expr instanceof HexFormat){//Number Cognition
			return true;
		}else if(expr instanceof OctalFormat){//Number Cognition
			return true;
		}else if(expr instanceof BinaryFormat){//Number Cognition
			return true;
		}else if(expr instanceof NullExpression){
			return true;
		}else{
			return false;
		}
	}
	
	public static EntityWrapper wrap(Entity target){
		EntityWrapper ew = new EntityWrapper();
		ew.setContent(target);
		return ew;
	}
	/**
	 * ��ȡ���ʽ�е�������hostObject�ļ�����ʽ���жϱ��ʽ��������
	 * @return
	 */
	public static Expression extractHostExpresion(Expression expr){
		return null;
	}
}
