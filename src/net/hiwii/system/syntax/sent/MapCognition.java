package net.hiwii.system.syntax.sent;

import net.hiwii.cognition.Expression;

/**
 * format:map Cognition [type] x.[id/function]=expression/block;
 * @author ha-wangzhenhai
 *
 */
public class MapCognition extends Expression {
	private String type;//if null,type="~"
	private String name;//host object name in expression/block
	private Expression cognition;
	private Expression content;//in x.id/x.f(argument) or bloc
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Expression getCognition() {
		return cognition;
	}
	public void setCognition(Expression cognition) {
		this.cognition = cognition;
	}
	public Expression getContent() {
		return content;
	}
	public void setContent(Expression content) {
		this.content = content;
	}
}
