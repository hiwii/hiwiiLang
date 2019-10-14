package net.hiwii.msg;

/**
 * 
 * @author hiwii
 * 当用户未登录，user是anonymous。
 *
 */
public class TerminalMessage extends Message {
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
