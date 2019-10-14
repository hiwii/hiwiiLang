package net.hiwii.reg.match;

import java.util.List;

import net.hiwii.reg.RegularExpression;
import net.hiwii.view.Entity;

/**
 * MatchGroup�����ж���������������£�
 * 1������
 * 2��identifier
 * 3��identifier[����]
 * 2,3����һ��ֻ�����ڵ�һ������λ�ã���ʾ�������á�
 * �������Ϳ������κ�λ�ó��֡�
 * ���������ʾ�༶���á�
 * @author Administrator
 *
 */
public class MatchGroup extends RegularExpression {
	private List<Entity> arguments;

	public List<Entity> getArguments() {
		return arguments;
	}

	public void setArguments(List<Entity> arguments) {
		this.arguments = arguments;
	}
}
