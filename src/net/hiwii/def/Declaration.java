package net.hiwii.def;

import net.hiwii.cognition.Expression;
import net.hiwii.view.Entity;

/**
 * key格式：
 * 对于无参数、无主语情况----name
 * 对于无参数、有主语情况----name@subject
 * 对于有参数、无主语情况----name#integer%serial ;serial允许同一function，多条件存在。无条件function在最后
 * 对于有参数、有主语情况----name#integer@subject
 * 主语有两种情况：
 * definition-----signature
 * entity-------definition's signature + % + entityId
 * entity include:
 * native cognition----number,string(记录中间结果)
 * perception-------
 * 
 * 新的三个词是：
 * calculation(计算)
 * decision(判定)
 * action(操作)
 * type='c','d','a'
 * @author Administrator
 *
 */
public class Declaration extends Entity {
	private String name;
	private Expression statement;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Expression getStatement() {
		return statement;
	}
	public void setStatement(Expression statement) {
		this.statement = statement;
	}
}
