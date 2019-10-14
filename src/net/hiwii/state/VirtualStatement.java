package net.hiwii.state;

import net.hiwii.view.Entity;

public class VirtualStatement extends Entity {
	private boolean judge;

	public boolean isJudge() {
		return judge;
	}
	public void setJudge(boolean judge) {
		this.judge = judge;
	}
}
