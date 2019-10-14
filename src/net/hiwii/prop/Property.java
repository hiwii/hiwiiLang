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
 * 对象有属性，属性可赋值。
 * 系统是特殊对象，属性和值在一起。
 * name, definition, limits, value
 * hiwiiContext分两种情况，主语是localHost 和其它entity
 * refer.host则有三种主语：localHost，Entity 和 abstraction
 * unique
 * number:-1表示不固定数量，1，表示一对一，n表示固定数量n。0表示不允许有。
 * 每次重构都使80%的代码不能运行。
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
