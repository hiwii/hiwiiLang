package net.hiwii.obj.decl;

import net.hiwii.view.Entity;

/**
 * flag初始化后即拥有状态。
 * new(Switch[{true|false}:name]
 * @author Administrator
 *
 */
public class SwitchObject extends Entity {
	private boolean flag;

	public SwitchObject() {
		flag = true;
	}
	
	public SwitchObject(boolean flag) {
		this.flag = flag;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public void negate(){
		if(flag){
			flag = false;
		}else{
			flag = true;
		}
	}
	
}
