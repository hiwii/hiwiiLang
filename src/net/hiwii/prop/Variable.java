package net.hiwii.prop;

import net.hiwii.view.Entity;

/**
 * variable��property��ϵͳ���ԡ�
 * property�Ķ���͸�ֵ���롣��variableͳһ��һ��
 * @author hiwii
 *
 */
public class Variable extends Property {
	private Entity value;
	
	public Entity getValue() {
		return value;
	}
	public void setValue(Entity value) {
		this.value = value;
	}
}
