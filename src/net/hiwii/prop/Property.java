package net.hiwii.prop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sleepycat.je.DatabaseException;

import net.hiwii.arg.Argument;
import net.hiwii.cognition.Expression;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.view.Entity;

/**
 * new[Property@host:(type{}->name)
 * ���������ԣ����Կɸ�ֵ��
 * ϵͳ������������Ժ�ֵ��һ��
 * name, definition, limits, value
 * hiwiiContext�����������������localHost ������entity
 * refer.host�����������localHost��Entity �� abstraction
 * unique
 * number:-1��ʾ���̶�������1����ʾһ��һ��n��ʾ�̶�����n��0��ʾ�������С�
 * ÿ���ع���ʹ80%�Ĵ��벻�����С�
 * @author Administrator
 *
 */
public class Property extends Argument {
//	private String name;
	private boolean option;  //option or required
	private boolean unique;
	private String type;
	private int number;
	private List<Expression> limits;	
	
	public Property() {
		limits = new ArrayList<Expression>();
		option = true;
		unique = false;
		number = 1;
	}

	public boolean isOption() {
		return option;
	}
	public void setOption(boolean option) {
		this.option = option;
	}
	public boolean isUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public List<Expression> getLimits() {
		return limits;
	}
	public void setLimits(List<Expression> limits) {
		this.limits = limits;
	}
//	public Entity getValue() {
//		return value;
//	}
//	public void setValue(Entity value) {
//		this.value = value;
//	}
	
	public boolean doAccept(Entity ent) 
			throws DatabaseException, IOException, ApplicationException, Exception{
		if(type == null) {
			return true;
		}
		if(EntityUtil.judgeDefinitionIsAnother(ent.getClassName(), type)){
			return true;
		}
		return false;
	}
}
