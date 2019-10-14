package net.hiwii.def;

/**
 * hiwii type依赖于一个定义。区别于类不依赖定义
 * type必然有一些修饰，如果没有则用定义表示
 * @author ha-wangzhenhai
 *
 */
public class HiwiiType extends Abstraction {
	private String from;//from definition
//	private Expression limit;
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
//	public Expression getLimit() {
//		return limit;
//	}
//	public void setLimit(Expression limit) {
//		this.limit = limit;
//	}
}
