package net.hiwii.db.ent;

import java.util.List;

public class FunctionHead {
	private String type;
	private List<String> argumentType;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getArgumentType() {
		return argumentType;
	}
	public void setArgumentType(List<String> argumentType) {
		this.argumentType = argumentType;
	}
}
