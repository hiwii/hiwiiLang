package net.hiwii.system.syntax.cog;

import net.hiwii.cognition.Expression;

/**
 * format:define name {type name|judgment expression}|
 * 		{f(type1 arg1,type2 arg2...)|judgment expression}
 * 		{(type1 arg1,type2 arg2...){}|judgment expression}
 * @author Administrator
 *
 */
public class DefineSentence extends CognitionAction {
	private Expression parent;//定义名、函数定义
	private String name;
	private Expression judgment;

	public Expression getParent() {
		return parent;
	}
	public void setParent(Expression parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Expression getJudgment() {
		return judgment;
	}
	public void setJudgment(Expression judgment) {
		this.judgment = judgment;
	}
}
