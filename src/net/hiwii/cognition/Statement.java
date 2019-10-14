package net.hiwii.cognition;


public class Statement extends Expression{
	private Expression sentence;

	public Expression getSentence() {
		return sentence;
	}

	public void setSentence(Expression sentence) {
		this.sentence = sentence;
	}
}
