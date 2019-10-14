package net.hiwii.def.cog;

import net.hiwii.def.Judgment;
import net.hiwii.view.Entity;

public class JudgmentSubject extends Judgment {
	private Entity subject;
	
	public Entity getSubject() {
		return subject;
	}
	public void setSubject(Entity subject) {
		this.subject = subject;
	}
}
