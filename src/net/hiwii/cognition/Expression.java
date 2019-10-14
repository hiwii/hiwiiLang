package net.hiwii.cognition;

import java.util.List;

import net.hiwii.context.RuntimeLadder;
import net.hiwii.view.Entity;

public class Expression extends Entity{
	@Override
	public String getGene() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Expression getEnhancedArgument(List<Expression> args){
		return this;
	}
	
	public Expression calculateExpression(RuntimeLadder ladder){
		return this;
	}

	@Override
	public boolean equals(Object expr) {
		if(!(expr instanceof Expression)){
			return false;
		}
		if(this.toString().equals(expr.toString())){
			return true;
		}
		return false;
	}
}
