package net.hiwii.obj.time;

import net.hiwii.view.Entity;

public class HiwiiAction extends HiwiiEvent {
	private String verb;//TODO function verb
	private Entity subject;
	private boolean start;//true for start,false for stop
	
	public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		this.verb = verb;
	}
	public Entity getSubject() {
		return subject;
	}
	public void setSubject(Entity subject) {
		this.subject = subject;
	}
	public boolean isStart() {
		return start;
	}
	public void setStart(boolean start) {
		this.start = start;
	}
	
}
