package net.hiwii.system.net.format;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.hiwii.system.util.PackageUtil;

public class PackageHeader implements Iterable<Map.Entry<String,String>>{
//	private List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>();
	private Map<String,String> map = new HashMap<String,String>();

	public int size() {
		return map.size();
	}
	public boolean isEmpty() {
		return map.isEmpty();
	}
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}
	
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}
	
	public String get(Object key) {
		return map.get(key);
	}
	
	public String put(String key, String value) {
		return map.put(key, value);
	}
	
	public String remove(Object key) {
		return map.remove(key);
	}
	
	public void putAll(Map<? extends String, ? extends String> m) {
		map.putAll(m);
	}
	
	public void clear() {
		map.clear();
	}
	
	public Set<String> keySet() {
		return map.keySet();
	}
	
	public Collection<String> values() {
		return map.values();
	}
	
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return map.entrySet();
	}
	@Override
	public Iterator<Entry<String, String>> iterator() {
		return map.entrySet().iterator();
	}
	@Override
	public String toString() {
		return PackageUtil.makeHeader(this);
	}
	
}
