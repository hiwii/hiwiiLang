package net.hiwii.reg.base.charactor;

import net.hiwii.system.SystemCharacter;

public class InvisiableChar extends CharRegular {
	@Override
	public boolean match(String str) {
		return SystemCharacter.isInvisiable(str);
	}
}
