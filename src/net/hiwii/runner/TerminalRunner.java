package net.hiwii.runner;

import java.util.List;
import java.util.Map;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.MappingExpression;
import net.hiwii.term.Terminal;

public class TerminalRunner {
	private Map<Terminal, ExpressionRunner> questions;
	
	public void run(Terminal term, Expression expr){
		if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			if(me.getName().equals("answer")){
				
			}
		}
		
	}
	
	public void getAnswer(Terminal term, List<Expression> args){
		if(args.size() == 1){
			Expression ans = args.get(0);
			if(questions.containsKey(term)){
				ExpressionRunner er = questions.get(term);
				questions.remove(term);
			}else{
//				term.sendMessage("no need answer")
//				term.messageReceived(host, "why answer, no need answer.");
			}
		}
	}
}
