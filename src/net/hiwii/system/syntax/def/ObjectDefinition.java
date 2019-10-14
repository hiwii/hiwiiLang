package net.hiwii.system.syntax.def;

import net.hiwii.cognition.Expression;

/**
 * format:fname(ObjectDefinition...)
 * ObjectDefinition:type identifier[judgment]
 * 相同函数名的参数类型可以用类表示，而不是用定义。
 * 但一担使用类表示，则相同格式的函数必须以不相容的类表示，防止出现定义和类的不相容。
 * 因为定义和类是天然相容的。
 * @author ha-wangzhenhai
 *
 */
public class ObjectDefinition extends Expression{
	private String type;//定义名、函数定义
	private Expression judgment;
	private String identifier;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Expression getJudgment() {
		return judgment;
	}
	public void setJudgment(Expression judgment) {
		this.judgment = judgment;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
