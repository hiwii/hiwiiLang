package net.hiwii.def;

import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.view.Entity;

public class Judgment extends Entity {
	private String name;
	private JudgmentResult positive;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public JudgmentResult getPositive() {
		return positive;
	}
	public void setPositive(JudgmentResult positive) {
		this.positive = positive;
	}
	
	
//	public void negate(){
//		if(positive){
//			positive = false;
//		}else{
//			positive = true;
//		}
//	}
}
