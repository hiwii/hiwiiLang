package net.hiwii.runner;

import net.hiwii.cognition.Expression;
import net.hiwii.context.RuntimeLadder;
import net.hiwii.view.Entity;

/**
 * 
 * @author Administrator
 * 20150602
 * Runner是一个Entity创建的对象，用于记录执行状态和执行指令表达式。
 * Runner的核心是一个Expression。像数学中的等式计算，目标表达式可以简化和代替。
 * invoker是Runner的调用者，用于输出runner的执行结果。
 * 很多情况下，invoker是terminal。localHost是一个特殊的invoker。
 *
 */
public class ExpressionRunner extends SystemRunner {
	private RuntimeLadder ladder;
	private Expression target;
	private Entity invoker;
	private ExpressionRunner parent;
	
	public RuntimeLadder getLadder() {
		return ladder;
	}
	public void setLadder(RuntimeLadder ladder) {
		this.ladder = ladder;
	}
	public Expression getTarget() {
		return target;
	}
	public void setTarget(Expression target) {
		this.target = target;
	}
	public Entity getInvoker() {
		return invoker;
	}
	public void setInvoker(Entity invoker) {
		this.invoker = invoker;
	}
	
	public ExpressionRunner getParent() {
		return parent;
	}
	public void setParent(ExpressionRunner parent) {
		this.parent = parent;
	}
	public void run(){
		return;
	}
	
	public void goon(){
		if(parent != null){
			parent.run();
		}
	}
}
