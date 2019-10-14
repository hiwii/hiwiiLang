package net.hiwii.reg.repeat.range;

import net.hiwii.reg.RegularExpression;

public class RepeatAtMost extends RepeatRange {

	public RepeatAtMost(RegularExpression regular, int max) {
		super(regular, 0, max);
	}
	
}
