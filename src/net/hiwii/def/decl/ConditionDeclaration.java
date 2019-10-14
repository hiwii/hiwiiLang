package net.hiwii.def.decl;

import java.util.List;

import net.hiwii.cognition.Expression;

/**
 * function(x,y...)+condition
 * @author ha-wangzhenhai
 *
 */
public class ConditionDeclaration extends FunctionDeclaration {
	private List<Expression> conditions;

	public List<Expression> getConditions() {
		return conditions;
	}

	public void setConditions(List<Expression> conditions) {
		this.conditions = conditions;
	}

}
