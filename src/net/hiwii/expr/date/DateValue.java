package net.hiwii.expr.date;

import net.hiwii.view.Entity;

/**
 * ͨ��date("20151011")����
 * @author Administrator
 *
 */
public class DateValue extends Entity {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
	
}
