package net.hiwii.def;

public class ExtendedDefinition extends Definition {
	private String parent;
	private String parentKey;

//	public String getParent() {
//		return parent;
//	}
//	public void setParent(String parent) {
//		this.parent = parent;
//	}

	public String getParentKey() {
		return parentKey;
	}
	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}
	
	@Override
	public String takeSignature() {
		return parentKey + "." + getName();
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
