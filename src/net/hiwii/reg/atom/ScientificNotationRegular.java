package net.hiwii.reg.atom;

import java.util.regex.Pattern;

import net.hiwii.reg.RegularExpression;

public class ScientificNotationRegular extends RegularExpression {
	@Override
	public boolean match(String str) {
		Pattern pattern = Pattern.compile("( [1-9] (.([0-9])+ )? )[eE] ([+,-])? ([0-9])+");
		return true;
	}
}
