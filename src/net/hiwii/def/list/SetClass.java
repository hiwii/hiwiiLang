package net.hiwii.def.list;

import net.hiwii.collection.EnumSet;
import net.hiwii.view.Entity;

/**
 * List��ʾ�������е�һ��������������Ӧ����Ȼ���ڲ�����ֱ���б��������κ��������С�
 * ��Ӧ��΢���Զ��ԣ�List��ʾ�������С�������������һ��������˺�΢�����У�Set��List�����ࡣ
 * @author hiwii
 *
 */
public class SetClass extends ListClass {
//	private String type;
//	private List<Expression> limits;
	public SetClass() {
//		super();
		setSignature("L.L.S");
	}
//	public String getType() {
//		return type;
//	}
//	public void setType(String type) {
//		this.type = type;
//	}
//	public List<Expression> getLimits() {
//		return limits;
//	}
//	public void setLimits(List<Expression> limits) {
//		this.limits = limits;
//	}
	
	@Override
	public Entity doIdentifierCalculation(String name) {
		if(name.equals("new")){
			EnumSet list = new EnumSet();
			list.setListType(this);
			return list;
		}
		return null;
	}
	
}
