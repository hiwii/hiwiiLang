package net.hiwii.system.syntax.sent;

import java.util.List;

import net.hiwii.cognition.Expression;

/**
 * create 定义名(参数)[{使用材料对象}]
 * @author ha-wangzhenhai
 *
 */
public class CreateSentence extends Expression {
	private String name;
	private List<Expression> arguments;
	private List<Expression> items;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Expression> getArguments() {
		return arguments;
	}
	public void setArguments(List<Expression> arguments) {
		this.arguments = arguments;
	}
	public List<Expression> getItems() {
		return items;
	}
	public void setItems(List<Expression> items) {
		this.items = items;
	}
}
