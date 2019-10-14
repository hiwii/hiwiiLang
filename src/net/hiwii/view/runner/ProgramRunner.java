package net.hiwii.view.runner;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.view.Runner;

public class ProgramRunner extends Runner {
	private List<Expression> actions;
	private int atLine;


	public ProgramRunner() {
		atLine = 0;
	}

	public List<Expression> getActions() {
		return actions;
	}

	public void setActions(List<Expression> actions) {
		this.actions = actions;
	}
	
	
	public int getAtLine() {
		return atLine;
	}

	public void setAtLine(int atLine) {
		this.atLine = atLine;
	}

	@Override
	public void run(){
		
	}
	
	public void doProgram(){
		for(Expression expr:actions){
//			Expresson result = 
		}
	}
}
