package net.hiwii.web;

import java.util.ArrayList;
import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.context.SessionContext;
import net.hiwii.message.HiwiiException;
import net.hiwii.obj.Session;
import net.hiwii.system.util.StringUtil;
import net.hiwii.term.CommandTerminal;
import net.hiwii.view.Entity;

public class NetTerminal extends CommandTerminal {
	private List<String> results;
	
	public NetTerminal() {
		Session session = new Session();
		SessionContext context = new SessionContext();
//		String id = getNextChildId();
//		context.setContextId(id);
		context.setSession(session);
		session.setContext(context);
		session.setTerminal(this);
		this.setSession(session);
		
		results = new ArrayList<String>();
	}

	public List<String> getResults() {
		return results;
	}

	public void setResults(List<String> results) {
		this.results = results;
	}
	
	public String doCommand(String comm){
		results.clear();
		
		Expression exp = StringUtil.parseString(comm);
		if(exp instanceof HiwiiException){
			results.add("parsing error!");
			return "error";
		}
		Expression res = getSession().getContext().doAction(exp);
		if(res instanceof HiwiiException){
			results.add("Exception happened!");
			return "error";
		}
		String ret = res.toString();
		if(ret.length() == 0){
			return "OK";
		}
		return res.toString();
	}
	
	public String doAction(String comm){
		results.clear();
		
		Expression exp = StringUtil.parseString(comm);
		if(exp instanceof HiwiiException){
			results.add("parsing error!");
			return "error";
		}
		Expression res = getSession().getContext().doAction(exp);
		if(res instanceof HiwiiException){
			results.add("Exception happened!");
			return "error";
		}
		return res.toString();
	}

	public String doCalculation(String comm){
		results.clear();
		
		Expression exp = StringUtil.parseString(comm);
		if(exp instanceof HiwiiException){
			results.add("parsing error!");
			return "error";
		}
		Entity res = getSession().getContext().doCalculation(exp);

		if(res instanceof HiwiiException){
			results.add("Exception happened!");
			return "error";
		}
		return res.toString();
	}
	
	public String doDecision(String comm){
		results.clear();
		
		Expression exp = StringUtil.parseString(comm);
		if(exp instanceof HiwiiException){
			results.add("parsing error!");
			return "error";
		}
		Expression res = getSession().getContext().doDecision(exp);

		if(res instanceof HiwiiException){
			results.add("Exception happened!");
			return "error";
		}
		return res.toString();
	}
	
	@Override
	public String doResponse(String out) {
		if(out.length() > 0){
			results.add(out);
		}
		return out;
	}
	
	
}
