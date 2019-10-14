package net.hiwii.part;

import net.hiwii.view.Entity;

/**
 * һ�������ɶ��part��ɡ���ͬ��part���ڲ�ͬ��Definition��
 * ����һ��Definition��part������number����number=-1����ʾ�����޶����
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
