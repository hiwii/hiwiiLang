package net.hiwii.user;

import net.hiwii.view.Entity;

/**
 * groupId符合id规则，不能包含特殊字符。
 * name是全名，以字符串表示，可以包含任意字符。
 * @author hiwii
 *
 */
public class Group extends Entity {
	private String groupId;
	private String name;
	private String note;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@Override
	public String toString() {
		return "Group [groupId=" + groupId + ", name=" + name + ", note=" + note + "]";
	}
}
