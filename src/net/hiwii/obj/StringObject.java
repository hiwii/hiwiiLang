package net.hiwii.obj;

import net.hiwii.expr.StringExpression;

/**
 * StringObject��StringExpression��ͬ���ڣ�
 * StringObject�����text
 * StringExpression������ַ�����
 * @author hiwii
 *
 */
public class StringObject extends StringExpression {
	@Override
	public String toString() {
		return getValue();
	}
}
