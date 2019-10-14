package net.hiwii.cognition.result;

import net.hiwii.cognition.Expression;

public class ReturnDecision extends Expression{
	private boolean result;

	public ReturnDecision() {
	}
	public ReturnDecision(boolean result) {
		this.result = result;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}
	
	public void negate(){
		if(result){
			result = false;
		}else{
			result = true;
		}
	}
	@Override
	public String toString() {
		if(result){
			return "true";
		}else{
			return "false";
		}
	}
}
