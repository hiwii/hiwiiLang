package net.hiwii.system.syntax.number;


public class ComplexNumber extends NumberExpression {
	private RealNumber real;
	private RealNumber imaginary;
	public RealNumber getReal() {
		return real;
	}
	public void setReal(RealNumber real) {
		this.real = real;
	}
	public RealNumber getImaginary() {
		return imaginary;
	}
	public void setImaginary(RealNumber imaginary) {
		this.imaginary = imaginary;
	}
}
