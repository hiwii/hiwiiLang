package net.hiwii.system.syntax.number;

/**
 * Ϊ�˾�ȷ��¼���������������ඨ�塣
 * ���磺1e30 + 1,�����ȷ��ʾ���˷���Դ�϶�
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
