package net.hiwii.view;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.system.runtime.RuntimePool;

public class Procedure extends Entity{
	private String programId;
	private boolean willStop;
	private boolean end;
	private String signature;
	private Entity host;
	private List<Expression> actions;
	private Entity result;
	
	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public boolean isEnd() {
		return end;
	}
	public void setEnd(boolean end) {
		this.end = end;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public Entity getHost() {
		return host;
	}
	public void setHost(Entity host) {
		this.host = host;
	}
	public boolean isWillStop() {
		return willStop;
	}
	public void setWillStop(boolean willStop) {
		this.willStop = willStop;
	}
	public List<Expression> getActions() {
		return actions;
	}
	public void setActions(List<Expression> actions) {
		this.actions = actions;
	}
	public Entity getResult() {
		return result;
	}
	public void setResult(Entity result) {
		this.result = result;
	}

	public Expression doAction(){
		for(Expression expr:actions){
			if(willStop){
				return null;
			}
//			host.doAction(expr);
		}
		//释放进程空间
		setEnd(true);
		return null;
	}
	
	public void stop(){
		//getChildProgram,stop them
		setWillStop(true);
	}
}
