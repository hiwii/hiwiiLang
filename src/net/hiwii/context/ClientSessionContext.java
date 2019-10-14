package net.hiwii.context;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.NormalEnd;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.MappingExpression;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.net.HostObject;
import net.hiwii.system.util.StringUtil;
import net.hiwii.view.Entity;

public class ClientSessionContext extends SessionContext {
	private HostObject host;
	
	public HostObject getHost() {
		return host;
	}

	public void setHost(HostObject host) {
		this.host = host;
	}

	public Expression doAction(Expression expr){
		if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			if(me.getArguments().size() != 1){
				return new HiwiiException();
			}
			if(me.getName().equals("ask")){
				String ret = host.doRemoteCalculation(me.getArguments().get(0));
				getSession().getTerminal().doResponse(ret);
				return new NormalEnd();
			}else if(me.getName().equals("whether")){
				String ret = host.doRemoteDecision(me.getArguments().get(0));
				getSession().getTerminal().doResponse(ret);
				return new NormalEnd();
			}
		}else if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			if(ie.getName().equals("exit")){
				getSession().popContext();
				return new NormalEnd();
			}
		}
		String ret = host.doRemoteAction(expr);
//		Entity ent = doCalculation(StringUtil.parseString(ret));
		getSession().getTerminal().doResponse(ret);
		return new NormalEnd();
	}
}
