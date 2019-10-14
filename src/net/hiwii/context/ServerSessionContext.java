package net.hiwii.context;

import java.util.ArrayList;
import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.cognition.result.NormalEnd;
import net.hiwii.expr.BinaryOperation;
import net.hiwii.expr.BracketExpression;
import net.hiwii.expr.CharExpression;
import net.hiwii.expr.FunctionExpression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.MappingExpression;
import net.hiwii.expr.NullExpression;
import net.hiwii.expr.ParenExpression;
import net.hiwii.expr.BraceExpression;
import net.hiwii.expr.StringExpression;
import net.hiwii.expr.SubjectOperation;
import net.hiwii.expr.SubjectVerb;
import net.hiwii.expr.UnaryOperation;
import net.hiwii.expr.adv.IdentifierBrace;
import net.hiwii.expr.sent.ConditionExpression;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.syntax.bin.BinaryFormat;
import net.hiwii.system.syntax.bin.HexFormat;
import net.hiwii.system.syntax.bin.OctalFormat;
import net.hiwii.system.syntax.number.DecimalNumber;
import net.hiwii.system.syntax.number.FractionNumber;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.system.syntax.number.ScientificNotation;
import net.hiwii.view.Entity;

/**
 * øÿ÷∆‘∂≥Ã∑√Œ 
 * @author hiwii
 *
 */
public class ServerSessionContext extends SessionContext {
	public Expression doAction(Expression expr){
        if(expr instanceof BraceExpression){
            BraceExpression prg = (BraceExpression) expr;
            return doProgramAction(prg);
        }else if(expr instanceof IdentifierExpression){
            IdentifierExpression ie = (IdentifierExpression) expr;
            String name = ie.getName();
//            try {
//				if(!permitIdentifierAction(name)){
//					System.out.println("not permitted");
//					return new HiwiiException();
//				}
//			} catch (ApplicationException e) {
//				return new HiwiiException();
//			}
            return doIdentifierAction(name);
        }else if(expr instanceof FunctionExpression){
            FunctionExpression fe = (FunctionExpression) expr;
            String name = fe.getName();
            List<Entity> list = new ArrayList<Entity>();
            for(Expression arg:fe.getArguments()){
                Entity ent = doCalculation(arg);
                list.add(ent);
            }
            return doFunctionAction(name, list);
        }else if(expr instanceof MappingExpression){
            MappingExpression me = (MappingExpression) expr;
            String name = me.getName();
            return doMappingAction(name, me.getArguments());
        }else if(expr instanceof SubjectVerb){
        	SubjectVerb sv = (SubjectVerb) expr;
        	Entity subject = doCalculation(sv.getSubject());
        	return doAction(subject, sv.getAction());        	
        }else if(expr instanceof UnaryOperation){
            UnaryOperation uo = (UnaryOperation) expr;
            Expression result = unaryTranslate(uo);
            if(result == null){
            	return new HiwiiException();//operator not recognized
            }
            return doAction(result);  //mapping or function
        }else if(expr instanceof BinaryOperation){
            BinaryOperation bo = (BinaryOperation) expr;
            Expression result = binaryTranslate(bo);
            if(result == null){
            	return new HiwiiException();//operator not recognized
            }
            return doAction(result);  //mapping or function
        }else if(expr instanceof BracketExpression){
            BracketExpression be = (BracketExpression) expr;
            boolean err = false;
            for(Expression comm:be.getArray()){
                Expression ret = doAction(comm);
                if(ret instanceof HiwiiException){
                    err = true;
                }
            }
            if(err){
            	return new HiwiiException();
            }else{
            	return new NormalEnd();
            }
        }else if(expr instanceof ConditionExpression){
            ConditionExpression ce = new ConditionExpression();
            Expression body = ce.getBody();
            List<Expression> cons = ce.getConditions();
            RuntimeContext context = null;
            context.doAction(body);
            if(body instanceof IdentifierExpression){
            	try {
					context = makeEnvironment(cons);
					return context.doAction(body);
				} catch (ApplicationException e) {
					return new HiwiiException();
				}
            }else if(body instanceof FunctionExpression){
            	FunctionExpression fe = (FunctionExpression) body;
            	List<Entity> list = new ArrayList<Entity>();
                for(Expression arg:fe.getArguments()){
                    Entity ent = doCalculation(arg);
                    list.add(ent);
                }
            	context = makeEnvironment(list, cons);
            }
            if(body instanceof SubjectVerb){
            	SubjectVerb sv = (SubjectVerb) expr;
            	Entity subject = doCalculation(sv.getSubject());
            	Expression verb = sv.getAction();
            	if(verb instanceof IdentifierExpression){
            		context = makeEnvironment(subject, cons);
            	}else if(verb instanceof FunctionExpression){
            		FunctionExpression fe = (FunctionExpression) verb;
            		List<Entity> list = new ArrayList<Entity>();
                    for(Expression arg:fe.getArguments()){
                        Entity ent = doCalculation(arg);
                        list.add(ent);
                    }
            		context = makeEnvironment(subject, cons);
            	}else{
            		context = makeEnvironment(subject, cons);
            	}
            }else{
            	try {
					context = makeEnvironment(cons);
				} catch (ApplicationException e) {
					return new HiwiiException();
				}
            }
            return context.doAction(body);
        }
        return null;
    }

    public Entity doCalculation(Expression expr){
    	if(expr instanceof CharExpression){
			return expr;
		}else if(expr instanceof StringExpression){
			return expr;
		}else if(expr instanceof FractionNumber){
			FractionNumber fn = (FractionNumber) expr;
			return fn.simply();
		}else if(expr instanceof ScientificNotation){
			return expr;
		}else if(expr instanceof IntegerNumber){//Number Cognition
			return expr;
		}else if(expr instanceof DecimalNumber){//Number Cognition
			return expr;
		}else if(expr instanceof HexFormat){//Number Cognition
			return expr;
		}else if(expr instanceof OctalFormat){//Number Cognition
			return expr;
		}else if(expr instanceof BinaryFormat){//Number Cognition
			return expr;
		}else if(expr instanceof NullExpression){
			return expr;
		}

		
    	if(expr instanceof BraceExpression){
            BraceExpression prg = (BraceExpression) expr;
            return doProgramAction(prg);
        }else if(expr instanceof IdentifierExpression){
            IdentifierExpression ie = (IdentifierExpression) expr;
            String name = ie.getName();
            try {
            	if(!permitIdentifierAction(name)){
					System.out.println("not permitted");
					return new HiwiiException();
				}
			} catch (ApplicationException e) {
				return new HiwiiException();
			}
            return doIdentifierCalculation(name);
        }else if(expr instanceof FunctionExpression){
            FunctionExpression fe = (FunctionExpression) expr;
            String name = fe.getName();
            List<Entity> list = new ArrayList<Entity>();
            for(Expression arg:fe.getArguments()){
                Entity ent = doCalculation(arg);
                list.add(ent);
            }
            return doFunctionCalculation(name, list);
        }else if(expr instanceof MappingExpression){
            MappingExpression me = (MappingExpression) expr;
            String name = me.getName();
            return doMappingCalculation(name, me.getArguments());
        }else if(expr instanceof SubjectOperation){
        	SubjectOperation sv = (SubjectOperation) expr;
        	Entity subject = doCalculation(sv.getSubject());
            return doCalculation(subject, sv.getAction());
        }else if(expr instanceof UnaryOperation){
            UnaryOperation uo = (UnaryOperation) expr;
            Expression result = unaryTranslate(uo);
            if(result == null){
            	return new HiwiiException();//operator not recognized
            }
            return doCalculation(result);  //mapping or function
        }else if(expr instanceof BinaryOperation){
            BinaryOperation bo = (BinaryOperation) expr;
            Expression result = binaryTranslate(bo);
            if(result == null){
            	return new HiwiiException();//operator not recognized
            }
            return doCalculation(result);  //mapping or function
        }else if(expr instanceof BracketExpression){
//            BracketExpression be = (BracketExpression) expr;
//            for(Expression comm:be.getArray()){
//                Expression ret = doContextAction(comm, adverbs);
//                if(ret instanceof HiwiiException){
//                    return ret;
//                }
//            }
            return new NormalEnd();
        }else if(expr instanceof ParenExpression){
        	ParenExpression pe = (ParenExpression) expr;
        	return doCalculation(pe.getExpression());
        }else if(expr instanceof IdentifierBrace){
        	IdentifierBrace ib = (IdentifierBrace) expr;
        	return doIdentifierCalculation(ib.getName(), ib.getConditions());
        }
		return null;
    }
    
    public Expression doDecision(Expression expr){
    	if(expr instanceof JudgmentResult){
    		return expr;
    	}
    	if(expr instanceof BraceExpression){
            BraceExpression prg = (BraceExpression) expr;
            return doProgramAction(prg);
        }else if(expr instanceof IdentifierExpression){
            IdentifierExpression ie = (IdentifierExpression) expr;
            String name = ie.getName();
            return doIdentifierDecision(name);
        }else if(expr instanceof FunctionExpression){
            FunctionExpression fe = (FunctionExpression) expr;
            String name = fe.getName();
            List<Entity> list = new ArrayList<Entity>();
            for(Expression arg:fe.getArguments()){
                Entity ent = doCalculation(arg);
                list.add(ent);
            }
            return doFunctionDecision(name, list);
        }else if(expr instanceof MappingExpression){
            MappingExpression me = (MappingExpression) expr;
            String name = me.getName();
            return doMappingDecision(name, me.getArguments());
        }else if(expr instanceof SubjectVerb){
        	SubjectVerb sv = (SubjectVerb) expr;
        	Entity subject = doCalculation(sv.getSubject());
            return doAction(subject, sv.getAction());
        }else if(expr instanceof UnaryOperation){
            UnaryOperation uo = (UnaryOperation) expr;
            Expression result = unaryTranslate(uo);
            if(result == null){
            	return new HiwiiException();//operator not recognized
            }
            return doAction(result);  //mapping or function
        }else if(expr instanceof BinaryOperation){
            BinaryOperation bo = (BinaryOperation) expr;
            Expression result = binaryTranslate(bo);
            if(result == null){
            	return new HiwiiException();//operator not recognized
            }
            return doDecision(result);  //mapping or function
        }else if(expr instanceof BracketExpression){
//            BracketExpression be = (BracketExpression) expr;
//            for(Expression comm:be.getArray()){
//                Expression ret = doContextAction(comm, adverbs);
//                if(ret instanceof HiwiiException){
//                    return ret;
//                }
//            }
            return new NormalEnd();
        }else if(expr instanceof ConditionExpression){
            ConditionExpression ce = new ConditionExpression();
            Expression body = ce.getBody();
            List<Expression> cons = ce.getConditions();
            RuntimeContext context = null;
            context.doAction(body);
            if(body instanceof IdentifierExpression){
            	try {
					context = makeEnvironment(cons);
					return context.doAction(body);
				} catch (ApplicationException e) {
					return new HiwiiException();
				}
            }else if(body instanceof FunctionExpression){
            	FunctionExpression fe = (FunctionExpression) body;
            	List<Entity> list = new ArrayList<Entity>();
                for(Expression arg:fe.getArguments()){
                    Entity ent = doCalculation(arg);
                    list.add(ent);
                }
            	context = makeEnvironment(list, cons);
            }
            if(body instanceof SubjectVerb){
            	SubjectVerb sv = (SubjectVerb) expr;
            	Entity subject = doCalculation(sv.getSubject());
            	Expression verb = sv.getAction();
            	if(verb instanceof IdentifierExpression){
            		context = makeEnvironment(subject, cons);
            	}else if(verb instanceof FunctionExpression){
            		FunctionExpression fe = (FunctionExpression) verb;
            		List<Entity> list = new ArrayList<Entity>();
                    for(Expression arg:fe.getArguments()){
                        Entity ent = doCalculation(arg);
                        list.add(ent);
                    }
            		context = makeEnvironment(subject, cons);
            	}else{
            		context = makeEnvironment(subject, cons);
            	}
            }else{
            	try {
					context = makeEnvironment(cons);
				} catch (ApplicationException e) {
					return new HiwiiException();
				}
            }
            return context.doAction(body);
        }
		return null;
    }
}
