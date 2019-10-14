package net.hiwii.reg.base.charactor;

import net.hiwii.system.SystemCharacter;

public class SpecialRegular extends CharRegular {
	@Override
	public boolean match(String str) {
		return SystemCharacter.isSpecial(str);
	}
}
