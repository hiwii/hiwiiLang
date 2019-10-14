package net.hiwii.reg.match;

import net.hiwii.reg.RegularExpression;
import net.hiwii.view.Entity;

public class Matcher extends Entity {
	private String source;
	private RegularExpression regular;
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public RegularExpression getRegular() {
		return regular;
	}
	public void setRegular(RegularExpression regular) {
		this.regular = regular;
	}
}
