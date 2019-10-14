package net.hiwii.system.syntax.def;

import net.hiwii.cognition.Expression;

/**
 * format:fname(ObjectDefinition...)
 * ObjectDefinition:type identifier[judgment]
 * ��ͬ�������Ĳ������Ϳ��������ʾ���������ö��塣
 * ��һ��ʹ�����ʾ������ͬ��ʽ�ĺ��������Բ����ݵ����ʾ����ֹ���ֶ������Ĳ����ݡ�
 * ��Ϊ�����������Ȼ���ݵġ�
 * @author ha-wangzhenhai
 *
 */
public class ObjectDefinition extends Expression{
	private String type;//����������������
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
