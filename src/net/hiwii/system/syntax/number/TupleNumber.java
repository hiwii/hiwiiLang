package net.hiwii.system.syntax.number;

/**
 * 为了精确记录运算结果，设计这个类定义。
 * 比如：1e30 + 1,如果精确表示，浪费资源较多
 * @author ha-wangzhenhai
 *
 */
public class TupleNumber extends RealNumber {
	private String operator;
	private RealNumber number1;
	private RealNumber number2;
	
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public RealNumber getNumber1() {
		return number1;
	}
	public void setNumber1(RealNumber number1) {
		this.number1 = number1;
	}
	public RealNumber getNumber2() {
		return number2;
	}
	public void setNumber2(RealNumber number2) {
		this.number2 = number2;
	}
}
