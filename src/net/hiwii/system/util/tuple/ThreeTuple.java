package net.hiwii.system.util.tuple;

public class ThreeTuple<A, B, C> extends TwoTuple<A, B> {
	private C c;
	
	public ThreeTuple(){
		
	}
	public ThreeTuple(A a, B b, C c) {
		super(a, b);
		this.c = c;
	}

	public C getC() {
		return c;
	}

	public void setC(C c) {
		this.c = c;
	}
}
