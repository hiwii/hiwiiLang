package net.hiwii.def;

import net.hiwii.cognition.Expression;
import net.hiwii.view.Entity;

/**
 * key��ʽ��
 * �����޲��������������----name
 * �����޲��������������----name@subject
 * �����в��������������----name#integer%serial ;serial����ͬһfunction�����������ڡ�������function�����
 * �����в��������������----name#integer@subject
 * ���������������
 * definition-----signature
 * entity-------definition's signature + % + entityId
 * entity include:
 * native cognition----number,string(��¼�м���)
 * perception-------
 * 
 * �µ��������ǣ�
 * calculation(����)
 * decision(�ж�)
 * action(����)
 * type='c','d','a'
 * @author Administrator
 *
 */
public class Declaration extends Entity {
	private String name;
	private Expression statement;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Expression getStatement() {
		return statement;
	}
	public void setStatement(Expression statement) {
		this.statement = statement;
	}
}
