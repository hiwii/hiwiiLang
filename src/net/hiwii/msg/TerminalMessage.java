package net.hiwii.msg;

/**
 * 
 * @author hiwii
 * ���û�δ��¼��user��anonymous��
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
