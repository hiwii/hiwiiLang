package net.hiwii.def;

import java.util.List;

import net.hiwii.cognition.Expression;

public class DefinitionLimitView extends Definition {
	private List<Expression> limits;
	private List<Expression> fields;
	public List<Expression> getLimits() {
		return limits;
	}
	public void setLimits(List<Expression> limits) {
		this.limits = limits;
	}
	public List<Expression> getFields() {
		return fields;
	}
	public void setFields(List<Expression> fields) {
		this.fields = fields;
	}
	
	@Override
	public String toString() {
		String ret = this.getName();
		ret = ret + ".view[";
		int i = 1;
		for(Expression expr:fields) {
			if(i == fields.size()) {
				ret = ret + expr.toString();
			}else {
				ret = ret + expr.toString() + ",";
			}
			i++;
		}		
		ret = ret + "].limit[";
		i = 1;
		for(Expression expr:limits) {
			if(i == limits.size()) {
				ret = ret + expr.toString();
			}else {
				ret = ret + expr.toString() + ",";
			}
			i++;
		}
		ret = ret + "]";
		return ret;
	}
}
