package net.hiwii.arg;

import java.util.List;

import net.hiwii.cognition.Expression;

/**
 * 
 * @author wzh
 * function format
 * f(argument...)
 * argument as follow:
 * 1,identifier.i.e. x,y
 * 2,decorated.i.e. x|x->Integer. separated by "|"
 * decorated expression can be program format. i.e. {x->Integer,x>0,x<10}
 *
 */
public class DecoratedArgument extends Argument {
	private List<Expression> states;

	public List<Expression> getStates() {
		return states;
	}

	public void setStates(List<Expression> states) {
		this.states = states;
	}
}
