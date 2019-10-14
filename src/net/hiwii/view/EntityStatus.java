package net.hiwii.view;

import java.util.List;

import net.hiwii.cognition.Expression;

public class EntityStatus {
	private boolean busy;
	private String signature;
	private List<Expression> queue;
	public boolean isBusy() {
		return busy;
	}
	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public List<Expression> getQueue() {
		return queue;
	}
	public void setQueue(List<Expression> queue) {
		this.queue = queue;
	}
	
}
