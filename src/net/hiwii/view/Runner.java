package net.hiwii.view;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.BraceExpression;
import net.hiwii.term.Terminal;
import net.hiwii.view.runner.ProgramRunner;

public class Runner extends Entity {
	private Entity invoker;
	private Runner parent;
	private Expression command;
	//memory

	public Entity getInvoker() {
		return invoker;
	}

	public void setInvoker(Entity invoker) {
		this.invoker = invoker;
	}

	public Runner getParent() {
		return parent;
	}

	public void setParent(Runner parent) {
		this.parent = parent;
	}

	public Expression getCommand() {
		return command;
	}

	public void setCommand(Expression command) {
		this.command = command;
	}	
	
	//to be override
	public void run(){
		
	}
	public Expression doCommand(){
		if(command instanceof BraceExpression){
			BraceExpression prg = (BraceExpression) command;
			ProgramRunner child = new ProgramRunner();
			child.setParent(this);
			child.setActions(prg.getArray());
//			return child.run();
		}
		return null;
	}
	
	public Expression doAction(){
		if(getInvoker() == null){
			
		}else if(getInvoker() instanceof Terminal){
			
		}else{
			
		}
		return null;
	}
}
