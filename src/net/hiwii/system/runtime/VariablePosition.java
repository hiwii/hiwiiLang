package net.hiwii.system.runtime;

public class VariablePosition {
	private String name;
	private int position;
	
	public VariablePosition() {
	}
	public VariablePosition(String name, int position) {
		this.name = name;
		this.position = position;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
}
