package net.hiwii.reg.match;

import java.util.List;

import net.hiwii.reg.RegularExpression;
import net.hiwii.view.Entity;

/**
 * MatchGroup参数有多个，参数类型如下：
 * 1，整数
 * 2，identifier
 * 3，identifier[整数]
 * 2,3类型一般只出现在第一个参数位置，表示命名引用。
 * 整数类型可以在任何位置出现。
 * 多个参数表示多级引用。
 * @author Administrator
 *
 */
public class MatchGroup extends RegularExpression {
	private List<Entity> arguments;

	public List<Entity> getArguments() {
		return arguments;
	}

	public void setArguments(List<Entity> arguments) {
		this.arguments = arguments;
	}
}
