package net.hiwii.def.list;

import net.hiwii.collection.EnumSet;
import net.hiwii.view.Entity;

/**
 * List表示有序排列的一组对象。这种有序对应与自然存在并不是直线列表，可以是任何有序排列。
 * 对应海微语言而言，List表示有序排列。而无序是另外一种有序。因此海微语言中，Set是List的子类。
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
