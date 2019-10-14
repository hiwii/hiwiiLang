package net.hiwii.reg.join;

import net.hiwii.reg.RegularExpression;

public class JoinExpression extends JoinRegular {
	private RegularExpression pattern;

	public RegularExpression getPattern() {
		return pattern;
	}

	public void setPattern(RegularExpression pattern) {
		this.pattern = pattern;
	}
}
