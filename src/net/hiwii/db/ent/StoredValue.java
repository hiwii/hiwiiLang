package net.hiwii.db.ent;

/**
 * signΪ�˱�֤�ܹ�����signature����
 * @author hiwii
 *
 */
public class StoredValue {
	private char type;
	private String sign;
	private String value;
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
