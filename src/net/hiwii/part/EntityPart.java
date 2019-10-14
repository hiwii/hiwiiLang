package net.hiwii.part;

import net.hiwii.view.Entity;

/**
 * 一个对象由多个part组成。不同的part属于不同的Definition。
 * 属于一个Definition的part个数是number。当number=-1，表示有无限多个。
 * @author hiwii
 *
 */
public class EntityPart extends Entity {
	private int number;
	private String type;
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public boolean satisfy(int count){
		if(number == -1){
			return true;
		}else if(number == 0){
			return false;
		}else{
			return number >= count;
		}
	}
}
