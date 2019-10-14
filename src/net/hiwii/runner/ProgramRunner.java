package net.hiwii.runner;

import java.util.List;

import net.hiwii.cognition.Expression;
/**
 * 
 * @author hiwii
 *
 */
public class ProgramRunner extends ExpressionRunner {
	private List<Expression> commands;
	private Expression result;//current command execute result;
	//将执行的命令位置。初始为0
	private int position;
	public List<Expression> getCommands() {
		return commands;
	}
	public void setCommands(List<Expression> commands) {
		this.commands = commands;
	}
	public Expression getResult() {
		return result;
	}
	public void setResult(Expression result) {
		this.result = result;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	@Override
	public void run() {
		for(int i = position;i<commands.size();i++){
			
		}
	}
}
