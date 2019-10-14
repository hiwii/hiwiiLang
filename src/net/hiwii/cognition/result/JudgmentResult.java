package net.hiwii.cognition.result;

import net.hiwii.cognition.Expression;

/**
 * 也用于表示decide[]结束。
 * 也是Expression entity一员
 * @author Administrator
 *
 */
public class JudgmentResult extends Expression {
	private boolean result;

	public JudgmentResult() {
	}
	public JudgmentResult(boolean result) {
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
