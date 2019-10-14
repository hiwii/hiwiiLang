package net.hiwii.state;

import net.hiwii.def.Definition;
import net.hiwii.view.HiwiiInstance;

public class ExistStatement extends VirtualStatement {
	private Definition define;
	private HiwiiInstance hiwii;
	public Definition getDefine() {
		return define;
	}
	public void setDefine(Definition define) {
		this.define = define;
	}
	public HiwiiInstance getHiwii() {
		return hiwii;
	}
	public void setHiwii(HiwiiInstance hiwii) {
		this.hiwii = hiwii;
	}
	
}
