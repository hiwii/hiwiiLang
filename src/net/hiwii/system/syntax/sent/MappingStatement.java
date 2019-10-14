package net.hiwii.system.syntax.sent;

import net.hiwii.cognition.Expression;
import net.hiwii.def.Cognition;

/**
 * 
 * @author ha-wangzhenhai
 * map Judgment/Action/Operation [@localhost/definition/class/object] id/function=
 * object is object/structure/object.operation
 * for example:
 * map Action id0=action expression;//@localhostÊ¡ÂÔ
 * map Action fname(type0 arg0,type1 arg1...)=action expression;
 * map Action @String fname(type0 arg0,type1 arg1...)=action expression;
 *
 */
public class MappingStatement extends Expression {
	private char type;//j:judge,a:action,o:operation
	//c:cognition,e:expression²»Òª
	private Expression from;
	private Expression to;
//	private Cognition cognition;//identifier for definition or class, List/identifier for List<id>
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}
	public Expression getFrom() {
		return from;
	}
	public void setFrom(Expression from) {
		this.from = from;
	}
	public Expression getTo() {
		return to;
	}
	public void setTo(Expression to) {
		this.to = to;
	}
	
}
