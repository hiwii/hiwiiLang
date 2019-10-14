package net.hiwii.msg.from;

import net.hiwii.view.Entity;

/**
 * 
 * @author hiwii
 * when userId is anonymous or null,userId = anonymous,
 * when ipaddress is "local" or null, fromChannel = local 
 *
 */
public class MessageFrom extends Entity {
	private String userId;
	private String ipaddress;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
}
