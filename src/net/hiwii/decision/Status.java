package net.hiwii.decision;

import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.view.Entity;

/**
 * ��initial=null����ʼ״̬δ֪
 * @author Administrator
 *
 */
public class Status extends Entity {
	private String name;
	private JudgmentResult initial;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public JudgmentResult getInitial() {
		return initial;
	}
	public void setInitial(JudgmentResult initial) {
		this.initial = initial;
	}
}
