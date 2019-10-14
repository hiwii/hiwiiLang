package net.hiwii.msg;

import net.hiwii.view.Entity;

public class Message extends Entity {
	private String content;
	private String time;
	private boolean input;

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public boolean isInput() {
		return input;
	}
	public void setInput(boolean input) {
		this.input = input;
	}
}
