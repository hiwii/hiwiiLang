package net.hiwii.def;

import java.util.List;

import net.hiwii.cognition.Expression;

/**
 * 
 * @author Wangzhenhai
 * format:definition1.property
 * or:definition1.property1.property2..
 * Èç£ºÂíÌã¡¢Â¹½Ç
 *
 */
class PropertyDefinition extends Definition {
	private List<Expression> express;

	public List<Expression> getExpress() {
		return express;
	}

	public void setExpress(List<Expression> express) {
		this.express = express;
	} 
	
}
