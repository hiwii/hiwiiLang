package net.hiwii.term;

import net.hiwii.cognition.Expression;
import net.hiwii.context.SessionContext;
import net.hiwii.expr.StringExpression;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.view.Entity;

/**
 * Ϊ��java��hiwii�������.
 * javaͨ��hiwiiTeminal���hiwii���Եĵ��á�
 * hiwii����javaTerminal���java���á�
 * @author Administrator
 *
 */
public class HiwiiTerminal extends Entity {
	private SessionContext context;
	
	public HiwiiTerminal() {
		context = new SessionContext();
	}
	public SessionContext getContext() {
		return context;
	}
	public void setContext(SessionContext context) {
		this.context = context;
	}
	
	/**
	 * ����֧��ask/whether/whetherNot,��doCalculation/doPositive/doNegative���
	 * ��ʹ�䣺doAction
	 * @param arg
	 * @throws Exception
	 */
	public void doAction(String arg) throws Exception{
		StringExpression se = new StringExpression(arg);
		Entity ret = se.doIdentifierCalculation("toExpression");
		if(ret instanceof HiwiiException){
			HiwiiException he = (HiwiiException) ret;
			throw new Exception(he.toString());
		}
		Expression exp = (Expression) ret;
		Entity res = context.doAction(exp);
		if(res instanceof HiwiiException){
			HiwiiException he = (HiwiiException) ret;
			throw new Exception(he.toString());
		}
		return;		
	}
	
	public String doCalculation(String arg) throws Exception{
		StringExpression se = new StringExpression(arg);
		Entity ret = se.doIdentifierCalculation("toExpression");
		if(ret instanceof HiwiiException){
			HiwiiException he = (HiwiiException) ret;
			throw new Exception(he.toString());
		}
		Expression exp = (Expression) ret;
		Entity res = context.doCalculation(exp);
		if(res instanceof HiwiiException){
			HiwiiException he = (HiwiiException) ret;
			throw new Exception(he.toString());
		}
		return res.toString();		
	}
	
	public boolean doPositive(String arg) throws Exception{
		StringExpression se = new StringExpression(arg);
		Entity ret = se.doIdentifierCalculation("toExpression");
		if(ret instanceof HiwiiException){
			HiwiiException he = (HiwiiException) ret;
			throw new Exception(he.toString());
		}
		Expression exp = (Expression) ret;
		Expression res = context.doDecision(exp);
		if(res instanceof HiwiiException){
			HiwiiException he = (HiwiiException) ret;
			throw new Exception(he.toString());
		}
		return EntityUtil.judge(res);
	}
	
//	public boolean doNegative(String arg) throws Exception{
//		StringExpression se = new StringExpression(arg);
//		Entity ret = se.doIdentifierCalculation("toExpression", null);
//		if(ret instanceof HiwiiException){
//			HiwiiException he = (HiwiiException) ret;
//			throw new Exception(he.toString());
//		}
//		Expression exp = (Expression) ret;
//		Expression res = context.doLadderNegative(exp, null);
//		if(res instanceof HiwiiException){
//			HiwiiException he = (HiwiiException) ret;
//			throw new Exception(he.toString());
//		}
//		return EntityUtil.judge(res);
//	}
	
}
