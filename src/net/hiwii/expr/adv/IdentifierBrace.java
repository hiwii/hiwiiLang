package net.hiwii.expr.adv;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.sent.ConditionExpression;

public class IdentifierBrace extends ConditionExpression{	
	private String name;;
	
	public IdentifierBrace() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Expression getBody() {
		return new IdentifierExpression(name);
	}
	
	@Override
	public String toString() {
		String str = name;
		str = str + "{";
		int i = getConditions().size() - 1;
		
		for(Expression exp:getConditions()){
			if(i != 0 ) {
				str = str + exp.toString()  + ",";
			}else {
				str = str + exp.toString();
			}
			i--;
		}
		str = str + "}";
		return str;
	}
}
