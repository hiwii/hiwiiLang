package net.hiwii.cache;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.NavigableMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import net.hiwii.def.Definition;

public class DefinitionPool {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
	private NavigableMap<String ,SortItem> nameIndex;
	private NavigableMap<String ,String> signIndex;
	private SortedSet<SortItem> totalIndex;
//	private NavigableMap<String ,String> timeIndex;
	private int limit;
	
	public DefinitionPool(int limit) {
		this.limit = limit;
		nameIndex = new TreeMap<String ,SortItem>();
		signIndex = new TreeMap<String ,String>();
		totalIndex = new TreeSet<SortItem>();
//		timeIndex = new TreeMap<String ,String>();
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public void put(Definition def){
		if(nameIndex.containsKey(def.getName())){
			return;
		}
		if(nameIndex.size() == limit){
			dropTail();
		}
		SortItem si = new SortItem();
		si.setLastTime(sdf.format(Calendar.getInstance().getTime()));
		si.setTotal(1);
		si.setItem(def);
		nameIndex.put(def.getName(), si);
		signIndex.put(def.getSignature(), def.getName());		
		totalIndex.add(si);
	}
	
	public void remove(String name){
		SortItem si = nameIndex.get(name);
		nameIndex.remove(si.getItem().getName());
		signIndex.remove(si.getItem().getSignature());
		totalIndex.remove(si);
	}
	public Definition getByName(String name){
		if(nameIndex.containsKey(name)){
			SortItem si = nameIndex.get(name);
			si.refresh();
			return si.getItem();
		}
		return null;
	}
	
	public Definition getBySignature(String sig){
		if(signIndex.containsKey(sig)){
			String name = signIndex.get(sig);
			SortItem si = nameIndex.get(name);
			si.refresh();
			return si.getItem();
		}
		return null;
	}
	
	public void dropTail(){
		SortItem si = totalIndex.first();
		nameIndex.remove(si.getItem().getName());
		signIndex.remove(si.getItem().getSignature());
		totalIndex.remove(si);
	}
	
	public class SortItem implements Comparable<SortItem>{
		private int total;
		private String lastTime;
		private Definition item;
		public int getTotal() {
			return total;
		}
		public void setTotal(int total) {
			this.total = total;
		}
		public Definition getItem() {
			return item;
		}
		public void setItem(Definition item) {
			this.item = item;
		}
		public String getLastTime() {
			return lastTime;
		}
		public void setLastTime(String lastTime) {
			this.lastTime = lastTime;
		}
		public void refresh(){			
			try {
				Date last = sdf.parse(lastTime);
				Calendar cal = Calendar.getInstance();
				lastTime = sdf.format(cal.getTime());
				
				cal.add(Calendar.HOUR_OF_DAY, -24);
				//为了更快的运算，不在毫秒级进行运算。
//				long intval = (now.getTime() - last.getTime())/1000*60*60;
//				if(intval > 24){
				//method 2 放弃，当跨年，计算失真。
//				int arg0 = Integer.parseInt(lastTime);
				
//				int arg1 = Integer.parseInt(lastTime);
//				int intval = arg1 - arg0;
				
				if(last.compareTo(cal.getTime()) > 0){
					//超过24小时
					total = 100;
				}else{
					if(total < 999999){
						total++;
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		public int compareTo(SortItem o) {
			if(this.total > o.getTotal()){
				return 1;
			}else if(this.total == o.getTotal()){
				return 0;
			}else{
				return -1;
			}
		}
	}
}
