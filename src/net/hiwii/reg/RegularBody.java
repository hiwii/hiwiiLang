package net.hiwii.reg;

public class RegularBody extends RegularExpression {
	private RegularExpression body;

	public RegularExpression getBody() {
		return body;
	}

	public void setBody(RegularExpression wrapper) {
		this.body = wrapper;
	}

	@Override
	public boolean match(String str) {
		return body.match(str);
	}

	@Override
	public int locate(String str, int pos, boolean forward) {
		return body.locate(str, pos, forward);
	}

	@Override
	public int count(String str, int pos, boolean forward, boolean greedy) {
		return body.count(str, pos, forward, true);
	}

	@Override
	public boolean guide(String str, int pos, boolean forward) {
		return body.guide(str, pos, forward);
	}
}
