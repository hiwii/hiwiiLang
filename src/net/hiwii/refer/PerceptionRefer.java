package net.hiwii.refer;

import net.hiwii.context.RuntimeContext;
import net.hiwii.view.Entity;

/**
 * TODO contextÉ¾³ý
 * @author ha-wangzhenhai
 *
 */
public class PerceptionRefer extends Entity{
	private boolean memory;
	private String signature;
	private RuntimeContext context;
	
	public PerceptionRefer(boolean memory, String signature) {
		this.memory = memory;
		this.signature = signature;
	}
	public PerceptionRefer(boolean memory, String signature,
			RuntimeContext context) {
		this.memory = memory;
		this.signature = signature;
		this.context = context;
	}
	public boolean isMemory() {//false for persist
		return memory;
	}
	public void setMemory(boolean memory) {
		this.memory = memory;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public RuntimeContext getContext() {
		return context;
	}
	public void setContext(RuntimeContext context) {
		this.context = context;
	}
	@Override
	public String toString() {
		return signature;
	}
	@Override
	public String getClassName() {
		return "";//.getDefine()
	}
}
