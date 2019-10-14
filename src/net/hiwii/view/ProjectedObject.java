package net.hiwii.view;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.def.Assignment;
import net.hiwii.expr.IdentifierExpression;

public class ProjectedObject extends HiwiiInstance {
	private List<Expression> fields;
	private List<Entity> values;

	public List<Expression> getFields() {
		return fields;
	}

	public void setFields(List<Expression> fields) {
		this.fields = fields;
	}

	public List<Entity> getValues() {
		return values;
	}

	public void setValues(List<Entity> values) {
		this.values = values;
	}
	
	@Override
	public Entity doIdentifierCalculation(String name) {
		for(int i=0;i<fields.size();i++) {
			Expression expr = fields.get(i);
			String str = expr.toString();
			if((expr instanceof IdentifierExpression) && str.equals(name)) {
				return values.get(i);
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		String ret = "{";
		for(int i=0;i<values.size();i++){
			if(i == 0) {
				ret = ret + fields.get(i).toString() + ":" + values.get(i).toString();
			}else {
				ret =  ret + "," + fields.get(i).toString() + ":" + values.get(i).toString();
			}
		}
		
		ret = ret + "}";
		return ret;
	}
}
