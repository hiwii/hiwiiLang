package net.hiwii.message;

import net.hiwii.cognition.Expression;

public class HiwiiException extends Expression {
	private String message;

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public HiwiiException() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public HiwiiException(String message) {
		this.message = message;
	}

	public HiwiiException(String name, String message) {
//		super(name, message);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		if(message == null){
			return "Exception happened!";
		}else{
			return message;
		}
	}

}
