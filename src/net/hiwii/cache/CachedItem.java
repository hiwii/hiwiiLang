package net.hiwii.cache;

public class CachedItem implements Comparable<CachedItem> {
	String key;
	public CachedItem(String key) {
		this.key = key;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@Override
	public int compareTo(CachedItem o) {		
		return this.key.compareTo(o.getKey());
	}
	@Override
	public String toString() {
		return key;
	}

}
