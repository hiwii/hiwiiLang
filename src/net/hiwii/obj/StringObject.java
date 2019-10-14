package net.hiwii.obj;

import net.hiwii.expr.StringExpression;

/**
 * StringObject与StringExpression不同在于，
 * StringObject输出是text
 * StringExpression输出是字符串。
 * @author hiwii
 *
 */
public class StringObject extends StringExpression {
	@Override
	public String toString() {
		return getValue();
	}
}
