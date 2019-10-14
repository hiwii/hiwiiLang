package net.hiwii.system.util.tuple;

public class DoubleString {
	private String first;
	private String second;
	
	public DoubleString() {
	}
	public DoubleString(String first, String second) {
		this.first = first;
		this.second = second;
	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getSecond() {
		return second;
	}
	public void setSecond(String second) {
		this.second = second;
	}
}
