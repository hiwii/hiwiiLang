package net.hiwii.prop;

import java.util.List;

import net.hiwii.cognition.Expression;

/**
 * sign±Ì æsignature
 * @author hiwii
 *
 */
public class VariableStore {
	private String type;
	private List<Expression> limits;
	private char valueType;
//	private String sign;
	private String value;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Expression> getLimits() {
		return limits;
	}
	public void setLimits(List<Expression> limits) {
		this.limits = limits;
	}
	
	public char getValueType() {
		return valueType;
	}
	public void setValueType(char valueType) {
		this.valueType = valueType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
