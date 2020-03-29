package net.hiwii.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sleepycat.je.DatabaseException;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.BreakReturn;
import net.hiwii.cognition.result.ExitEnd;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.cognition.result.NormalEnd;
import net.hiwii.cognition.result.ReturnEnd;
import net.hiwii.cognition.result.ReturnResult;
import net.hiwii.cognition.result.SkipReturn;
import net.hiwii.collection.EntityList;
import net.hiwii.context.HiwiiContext;
import net.hiwii.context.HostContext;
import net.hiwii.context.RuntimeContext;
import net.hiwii.context.RuntimeLadder;
import net.hiwii.def.Definition;
import net.hiwii.expr.ActionAtSubject;
import net.hiwii.expr.BinaryOperation;
import net.hiwii.expr.BraceExpression;
import net.hiwii.expr.BracketExpression;
import net.hiwii.expr.CharExpression;
import net.hiwii.expr.FunctionExpression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.MappingExpression;
import net.hiwii.expr.NullExpression;
import net.hiwii.expr.ParenExpression;
import net.hiwii.expr.Parentheses;
import net.hiwii.expr.StringExpression;
import net.hiwii.expr.SubjectOperation;
import net.hiwii.expr.SubjectStatus;
import net.hiwii.expr.SubjectVerb;
import net.hiwii.expr.UnaryOperation;
import net.hiwii.expr.adv.IdentifierBrace;
import net.hiwii.expr.sent.ConditionExpression;
import net.hiwii.lambda.ArgumentedLambda;
import net.hiwii.lambda.EntityLambda;
import net.hiwii.lambda.LambdaMapping;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.SystemOperators;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.syntax.bin.BinaryFormat;
import net.hiwii.system.syntax.bin.HexFormat;
import net.hiwii.system.syntax.bin.OctalFormat;
import net.hiwii.system.syntax.number.DecimalNumber;
import net.hiwii.system.syntax.number.FractionNumber;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.system.syntax.number.ScientificNotation;
import net.hiwii.system.util.EntityUtil;

/**
 * 
 * @author ha-wangzhenhai
 * 
 * 对象的哲学：
 * 1，世界由对象构成，对象由子对象构成。
 * 2，对象有状态。
 * 3，对象能进行动作。
 * 4，对象在某种状态下就会执行某个动作。
 * 5，对象和其它对象有属性关系。
 * 6、对象接收消息后，执行动作。
 * 对象能够进行哪些动作，接受哪些消息
 * 1，接受计算、判定、动作。
 * 2，hiwii语言是本系统中一切对象的标准信息。
 * hiwii语言的语法，根据语法格式可以对信息进行分类
 * 1、identifier
 * 2、function
 * 3、mapping
 * 4、subject@Action
 * 5、program
 * 6、以上语法的副词。
 * 一级函数
 * doCalculation(expression, context)
 * doDecision(expression, context)
 * doAction(expression, context)
 * 
 * action\decide\calculation是原子型态。
 * process是运算形式的混合形态。process和action相似，一个时刻只能执行一个action。
 * 不同在于，process必须返回一个值。
 * identifier/function/mapping/subjectVerb/
 * functionMapping同时传递表达式参数和对象参数
 * +adverb
 * doCalculation/doDecision/doAction
 *
 */
public class Entity {
	public static final char CALCULATION = 'c';
	public static final char DECISION = 'd';
	public static final char ACTION = 'a';
//	public static final String STREAMHEAD = "!@#$%^&*";
	private Entity handle;
	private String className = "Object";
	private Entity container;
	
	public Entity doCalculation(Expression expr){
		if(expr instanceof LambdaMapping){
			return expr;
		}
		if(expr instanceof ArgumentedLambda){
			return expr;
		}
		if(expr instanceof EntityLambda){
			return expr;
		}
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
			Entity ret = doProgramCalculation(prg);
			if(ret instanceof ReturnResult){
				ReturnResult or = (ReturnResult) ret;
				return or.getResult();
			}else{
				return new HiwiiException();
			}
		}else if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			String name = ie.getName();
			return doIdentifierCalculation(name);
		}else if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			String name = fe.getName();
			List<Entity> list = new ArrayList<Entity>();
			for(Expression arg:fe.getArguments()){
				Entity ent = doCalculation(arg);
				if(ent instanceof HiwiiException){
					return ent;
				}
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
			if(subject == null){
				return new HiwiiException();
			}
//			Expression arg = doInstance(sv.getAction());
//			return subject.doCalculation(arg);
			return subject.doCalculation(sv.getAction());
		}else if(expr instanceof ActionAtSubject){
			ActionAtSubject sv = (ActionAtSubject) expr;
			Entity subject = doCalculation(sv.getSubject());
			if(subject == null){
				return new HiwiiException();
			}
//			return doCalculation(subject, sv.getAction());
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
			BracketExpression be = (BracketExpression) expr;
			List<Entity> list = new ArrayList<Entity>();
			for(Expression item:be.getArray()){
				Entity ent = doCalculation(item);
				if(ent instanceof HiwiiException){
					return ent;
				}
				list.add(ent);
			}
			EntityList el = new EntityList();
			el.setItems(list);
			return el;
		}else if(expr instanceof ParenExpression){
			ParenExpression pe = (ParenExpression) expr;
			return doCalculation(pe.getExpression());
		}else if(expr instanceof Parentheses){
			Parentheses pe = (Parentheses) expr;
			return pe;
		}else if(expr instanceof IdentifierBrace){
			IdentifierBrace ib = (IdentifierBrace) expr;
//			return doIdentifierCalculation(ib.getName(), ib.getConditions());
		}
		return null;
	}
	
	public Expression doAction(Expression expr){
		if(expr instanceof BraceExpression){
			BraceExpression prg = (BraceExpression) expr;
			return doProgramAction(prg);
		}else if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			String name = ie.getName();
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
		}else if(expr instanceof ActionAtSubject){
			ActionAtSubject sv = (ActionAtSubject) expr;
			Expression result = doAction(sv.getAction());
			if(result != null){
				return result;
			}
			Entity subject = doCalculation(sv.getSubject());
//			return doAction(subject, sv.getAction());
		}else if(expr instanceof SubjectVerb){
			SubjectVerb sv = (SubjectVerb) expr;
			Entity subject = doCalculation(sv.getSubject());
			return subject.doAction(sv.getAction());        	
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
		}else if(expr instanceof SubjectStatus){
			//statement sentence
			SubjectStatus ss = (SubjectStatus) expr;
		}
		return null;
	}
	
	
	public final Entity doCalculation(Expression expr, HiwiiContext context){
		if(expr instanceof LambdaMapping){
			return expr;
		}
		if(expr instanceof ArgumentedLambda){
			return expr;
		}
		if(expr instanceof EntityLambda){
			return expr;
		}
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
			Entity ret = doProgramCalculation(prg);
			if(ret instanceof ReturnResult){
				ReturnResult or = (ReturnResult) ret;
				return or.getResult();
			}else{
				return new HiwiiException();
			}
		}else if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			String name = ie.getName();
			return doIdentifierCalculation(name, context);
		}else if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			String name = fe.getName();
			List<Entity> list = new ArrayList<Entity>();
			for(Expression arg:fe.getArguments()){
				Entity ent = doCalculation(arg);
				if(ent instanceof HiwiiException){
					return ent;
				}
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
			if(subject == null){
				return new HiwiiException();
			}
//			Expression arg = doInstance(sv.getAction());
//			return subject.doCalculation(arg);
			return subject.doCalculation(sv.getAction());
		}else if(expr instanceof ActionAtSubject){
			ActionAtSubject sv = (ActionAtSubject) expr;
			Entity subject = doCalculation(sv.getSubject());
			if(subject == null){
				return new HiwiiException();
			}
//			return doCalculation(subject, sv.getAction());
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
			BracketExpression be = (BracketExpression) expr;
			List<Entity> list = new ArrayList<Entity>();
			for(Expression item:be.getArray()){
				Entity ent = doCalculation(item);
				if(ent instanceof HiwiiException){
					return ent;
				}
				list.add(ent);
			}
			EntityList el = new EntityList();
			el.setItems(list);
			return el;
		}else if(expr instanceof ParenExpression){
			ParenExpression pe = (ParenExpression) expr;
			return doCalculation(pe.getExpression());
		}else if(expr instanceof Parentheses){
			Parentheses pe = (Parentheses) expr;
			return pe;
		}else if(expr instanceof IdentifierBrace){
			IdentifierBrace ib = (IdentifierBrace) expr;
//			return doIdentifierCalculation(ib.getName(), ib.getConditions());
		}
		return null;
	}
	
	
	public Expression doDecision(Expression expr, HiwiiContext context){
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
		}else if(expr instanceof ActionAtSubject){
			//statement sentence
			SubjectStatus ss = (SubjectStatus) expr;
			Entity subject = doCalculation(ss.getSubject());
			boolean right = ss.isRight();
			Expression ret = null;//doDecision(subject, ss.getAction());
			if(!(ret instanceof JudgmentResult)){
				return ret;
			}
			if(EntityUtil.judge(ret)){
				if(right){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else{
				if(right){
					return EntityUtil.decide(false);
				}else{
					return EntityUtil.decide(true);
				}
			}
		}else if(expr instanceof SubjectStatus){
			//statement sentence
			SubjectStatus ss = (SubjectStatus) expr;
			Entity subject = doCalculation(ss.getSubject());
			boolean right = ss.isRight();
			Expression ret =  null;//doDecision(subject, ss.getAction());
			if(!(ret instanceof JudgmentResult)){
				return ret;
			}
			if(EntityUtil.judge(ret)){
				if(right){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else{
				if(right){
					return EntityUtil.decide(false);
				}else{
					return EntityUtil.decide(true);
				}
			}
		}else if(expr instanceof UnaryOperation){
			UnaryOperation uo = (UnaryOperation) expr;
			Expression result = unaryTranslate(uo);
			if(result == null){
				return new HiwiiException();//operator not recognized
			}
			return doDecision(result);  //mapping or function
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
		}else if(expr instanceof ActionAtSubject){
			//statement sentence
			SubjectStatus ss = (SubjectStatus) expr;
			Entity subject = doCalculation(ss.getSubject());
			boolean right = ss.isRight();
			Expression ret = null;//doDecision(subject, ss.getAction());
			if(!(ret instanceof JudgmentResult)){
				return ret;
			}
			if(EntityUtil.judge(ret)){
				if(right){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else{
				if(right){
					return EntityUtil.decide(false);
				}else{
					return EntityUtil.decide(true);
				}
			}
		}else if(expr instanceof SubjectStatus){
			//statement sentence
			SubjectStatus ss = (SubjectStatus) expr;
			Entity subject = doCalculation(ss.getSubject());
			boolean right = ss.isRight();
			Expression ret =  null;//doDecision(subject, ss.getAction());
			if(!(ret instanceof JudgmentResult)){
				return ret;
			}
			if(EntityUtil.judge(ret)){
				if(right){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else{
				if(right){
					return EntityUtil.decide(false);
				}else{
					return EntityUtil.decide(true);
				}
			}
		}else if(expr instanceof UnaryOperation){
			UnaryOperation uo = (UnaryOperation) expr;
			Expression result = unaryTranslate(uo);
			if(result == null){
				return new HiwiiException();//operator not recognized
			}
			return doDecision(result);  //mapping or function
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
			
		}
		return null;
	}
	
	public Expression onReceiveMessage(Entity from, Expression message){
		return null;
	}
	
	
	public Expression doActionProgram(BraceExpression prg){
//		RuntimeContext rc = LocalHost.getInstance().newSessionContext();
		List<Expression> sents = prg.getArray();
		Expression result = null;
		for(Expression expr:sents){
			result = doAction(expr);

			if(result instanceof SkipReturn){
				break; //consume skip identifier
			}else if(result instanceof BreakReturn){
				return result;//will be consumed in loop action.if there is no loop will throw exception
			}else if(result instanceof ExitEnd){
				return result;
			}else if(result instanceof ReturnEnd){
				return result;
			}else if(result instanceof JudgmentResult){
				return result;
			}else if(result instanceof ReturnResult){
				return result;
			}

			if(result instanceof HiwiiException){
				//				System.out.println("some exception happened!");
				return result;
			}
		}
		return new NormalEnd();
	}
	
	
	
	public Entity doIdentifierCalculation(String name, HiwiiContext context){
		Expression expr = null;//getInnerIdentifierAction(inst, name);
		if(expr != null) {
//			inst.doInnerAction(expr)
		}
		return null;
	}
	/**
	 * 为保证对象在同一时刻只执行一个action，identifierAction和FunctionAction又分为syncAction和普通Action。
	 * 系统调用syncAction。系统内部的复合执行程序也是一个syncAction与atomAction并列，复合执行程序只是调用普通Action。
	 * 
	 * @param name
	 * @return
	 */
	public Entity doIdentifierCalculation(String name){
		return null;
	}
	public Expression doIdentifierAction(String name){return null;}
	public Expression doIdentifierDecision(String name){return null;}
	
	public Entity doFunctionCalculation(String name, List<Entity> args){return null;}	
	public Expression doFunctionAction(String name, List<Entity> args){return null;}
	public Expression doFunctionDecision(String name, List<Entity> args){return null;}
	
	public Entity doMappingCalculation(String name, List<Expression> args){return null;}	
	public Expression doMappingAction(String name, List<Expression> args){
		return null;
	}
	public Expression doMappingDecision(String name, List<Expression> args){
		return null;
	}
	
	//added begin.at 20190810
	public Entity doIdentifierCalculation(Entity subject, String name){
		return null;
	}
	public Expression doIdentifierAction(Entity subject, String name){return null;}
	public Expression doIdentifierDecision(Entity subject, String name){return null;}
	
	public Entity doFunctionCalculation(Entity subject, String name, List<Entity> args){return null;}	
	public Expression doFunctionAction(Entity subject, String name, List<Entity> args){return null;}
	public Expression doFunctionDecision(Entity subject, String name, List<Entity> args){return null;}
	
	public Entity doMappingCalculation(Entity subject, String name, List<Expression> args){return null;}	
	public Expression doMappingAction(Entity subject, String name, List<Expression> args){
		return null;
	}
	public Expression doMappingDecision(Entity subject, String name, List<Expression> args){
		return null;
	}
	/*****/
	public Entity doIdentifierCalculation(String name, List<Expression> adverb){
		return null;
	}
	public Expression doIdentifierAction(String name, List<Expression> adverb){return null;}
	public Expression doIdentifierDecision(String name, List<Expression> adverb){return null;}
	
	public Entity doFunctionCalculation(String name, List<Entity> args, List<Expression> adverb){return null;}	
	public Expression doFunctionAction(String name, List<Entity> args, List<Expression> adverb){return null;}
	public Expression doFunctionDecision(String name, List<Entity> args, List<Expression> adverb){return null;}
	
	public Entity doMappingCalculation(String name, List<Expression> args, List<Expression> adverb){return null;}	
	public Expression doMappingAction(String name, List<Expression> args, List<Expression> adverb){
		return null;
	}
	public Expression doMappingDecision(String name, List<Expression> args, List<Expression> adverb){
		return null;
	}
	public Entity doFunctionCalculation(String name, List<Entity> args, List<Expression> margs, List<Expression> states){return null;}	
	public Expression doFunctionAction(String name, List<Entity> args, List<Expression> margs, List<Expression> states){return null;}
	public Expression doFunctionDecision(String name, List<Entity> args, List<Expression> margs, List<Expression> states){return null;}
	/*****/
	public Entity doIdentifierCalculation(Entity subject, String name, List<Expression> adverb){
		return null;
	}
	public Expression doIdentifierAction(Entity subject, String name, List<Expression> adverb){return null;}
	public Expression doIdentifierDecision(Entity subject, String name, List<Expression> adverb){return null;}
	
	public Entity doFunctionCalculation(Entity subject, String name, List<Entity> args, List<Expression> adverb){return null;}	
	public Expression doFunctionAction(Entity subject, String name, List<Entity> args, List<Expression> adverb){return null;}
	public Expression doFunctionDecision(Entity subject, String name, List<Entity> args, List<Expression> adverb){return null;}
	
	public Entity doMappingCalculation(Entity subject, String name, List<Expression> args, List<Expression> adverb){return null;}	
	public Expression doMappingAction(Entity subject, String name, List<Expression> args, List<Expression> adverb){
		return null;
	}
	public Expression doMappingDecision(Entity subject, String name, List<Expression> args, List<Expression> adverb){
		return null;
	}
	//added end
	
	public Expression doIdentifierAction(String name, HiwiiContext context){return null;}
	public Expression doIdentifierDecision(String name, HiwiiContext context){return null;}
	
	public Entity doFunctionCalculation(String name, List<Entity> args, HiwiiContext context){return null;}	
	public Expression doFunctionAction(String name, List<Entity> args, HiwiiContext context){return null;}
	public Expression doFunctionDecision(String name, List<Entity> args, HiwiiContext context){return null;}
	
	public Entity doMappingCalculation(String name, List<Expression> args, HiwiiContext context){return null;}	
	public Expression doMappingAction(String name, List<Expression> args, HiwiiContext context){
		Expression ret = context.proxyMappingAction(name, args);
		if(ret != null) {
			return ret;
		}
		if(name.equals("return")) {
			if(args.size() != 1){
				return new HiwiiException();
			}
			ReturnResult ret0 = new ReturnResult();
			Entity res = doCalculation(args.get(0));
			if(res instanceof HiwiiException){
				return (Expression) res;
			}
			ret0.setResult(res);
			return ret0;
		}
		return null;
	}
	public Expression doMappingDecision(String name, List<Expression> args, HiwiiContext context){
		return null;
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Entity getContainer() {
		return container;
	}

	public void setContainer(Entity container) {
		this.container = container;
	}

	/*************************/
	public RuntimeContext newRuntimeContext(char type){
		RuntimeContext rc = new RuntimeContext();
		rc.setType(type);
		rc.setLoop(false);
		RuntimeLadder rl = new RuntimeLadder();		
		List<RuntimeContext> ch = new ArrayList<RuntimeContext>();
		ch.add(rc);
		rl.setChains(ch);
		rc.setLadder(rl);
		return rc;
	}
	
	
	public Expression doProgramAction(BraceExpression prg){
		RuntimeContext rc = newRuntimeContext('c'); //LocalHost.getInstance().;
		List<Expression> sents = prg.getArray();
		Expression result = null;
		for(Expression expr:sents){
			result = rc.doAction(expr);

			if(result instanceof SkipReturn){
				break; //consume skip identifier
			}else if(result instanceof BreakReturn){
				return result;//will be consumed in loop action.if there is no loop will throw exception
			}else if(result instanceof ExitEnd){
				return result;
			}else if(result instanceof ReturnEnd){
				return result;
			}else if(result instanceof JudgmentResult){
				return result;
			}else if(result instanceof ReturnResult){
				return result;
			}

			if(result instanceof HiwiiException){
				//				System.out.println("some exception happened!");
				return result;
			}
		}
		return new NormalEnd();
	}
	/************************/
//	public boolean hasIdentifierOperation(String name){return false;}	
//	public boolean hasIdentifierAction(String name){return false;}
//	public boolean hasIdentifierJudgment(String name){return false;}
//	
//	public boolean hasFunctionOperation(String name, List<Entity> args){return false;}	
//	public boolean hasFunctionAction(String name, List<Entity> args){return false;}
//	public boolean hasFunctionPositive(String name, List<Entity> args){return false;}

	/*******************non entity interface************/
	private boolean busy = false;
	
	public String getGene(){return null;}
	
	public synchronized boolean isBusy(){
		return busy;
	}
	public synchronized void setBusy(boolean busy){
		this.busy = busy;
	}
	
	/**
	 * actionId不忙用empty表示
	 * 通过program产生的actionId可以继承ProgramId
	 */
	private String actionId = "";
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	/**
	 * actionId和Entity有关，不同的entity产生不同的Id，相比LocalHost产生Id，可以提高效率。
	 * @return
	 */
	public String generateActionId(){
		return "";
	}
	public boolean isDefinition(String def){
		return false;
	}
	
	public String format(String arg) throws ApplicationException{
		return toString();
	}
	
	public synchronized Entity getHandle(){
		if(handle == null){
			this.handle = new Entity();
		}
		return handle;
	}
	public synchronized void setHandle(Entity handle){
		this.handle = handle;
	}
	
	public Definition getDefinition(){
		return null;
	}
	
	public Expression doAssign(Expression expr, Entity value){
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			return doAssign(ie.getName(), value);
		}
		return null;
	}
	public Expression doAssign(String name, Entity value){
		return null;
	}
	
	public Expression doJudge(Expression expr, JudgmentResult value){
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			return doJudge(ie.getName(), value);
		}
		return null;
	}
	public Expression doJudge(String name, JudgmentResult value){
		return null;
	}
	
	public Expression doIfAction(List<Expression> args){
		boolean doelse = true;
		Expression expr = args.get(0);
		Expression decide = doDecision(expr);
		if(decide instanceof JudgmentResult){
			JudgmentResult jr = (JudgmentResult) decide;
			if(jr.isResult()){
				doelse = false;
				Expression ret = doAction(args.get(1));				
				return ret;
			}
		}else{
			return decide;
		}

		if(doelse && args.size() == 3){
			Expression ret = doAction(args.get(2));
			return ret;
		}
		return new NormalEnd();
	}
	
	public Expression doIfAction(List<Expression> args, HiwiiContext context){
		boolean doelse = true;
		Expression expr = args.get(0);
		Expression decide = context.doDecision(expr);
		if(decide instanceof JudgmentResult){
			JudgmentResult jr = (JudgmentResult) decide;
			if(jr.isResult()){
				doelse = false;
				Expression ret = context.doAction(args.get(1));				
				return ret;
			}
		}else{
			return decide;
		}

		if(doelse && args.size() == 3){
			Expression ret = context.doAction(args.get(2));
			return ret;
		}
		return new NormalEnd();
	}
	
	public Expression doSilentIfAction(List<Expression> args){
//		boolean doelse = true;
//		Expression expr = args.get(0);
//		Expression decide = contextDecision(expr);
//		if(decide instanceof JudgmentResult){
//			JudgmentResult jr = (JudgmentResult) decide;
//			if(jr.isResult()){
//				doelse = false;
//				Expression ret = doSilentAction(args.get(1));				
//				return ret;
//			}
//		}else{
//			return decide;
//		}
//
//		if(doelse && args.size() == 3){
//			Expression ret = doSilentAction(args.get(2));
//			return ret;
//		}
		return new NormalEnd();
	}
	
	/***********************************************/
	public Expression doWhileLoop(Expression cond, Expression stm, HiwiiContext context){
//		RuntimeContext rc = context.getLadder().newRuntimeContext('a');
//		rc.setLoop(true);
		//		boolean old = this.isLoop();
		//		this.setLoop(true);
		//new runtimeContext
		//		this.setLoop(old);
		int temp = 0;
		while(true){
			Expression jud = null;
			jud = context.doDecision(cond);
			if(jud instanceof JudgmentResult){
				JudgmentResult jr = (JudgmentResult) jud;
				if(jr.isResult()){
					Expression result = null;
					result = context.doAction(stm);

					if(result instanceof BreakReturn){
						break; //consume break
					}else if(result instanceof ReturnEnd){
						return result;
					}else if(result instanceof JudgmentResult){
						return result;
					}else if(result instanceof ReturnResult){
						return result;
					}

					if(result instanceof HiwiiException){
						return result;
					}
				}else{
					return new NormalEnd();
				}
			}else{
				return jud;
			}
			temp++;
			if(temp >= 10){
				break;  //为防止测试环境压力大，每次测试最多允许10次循环。
			}
		}
		return new NormalEnd();
	}
	
	public Expression doSilentAction(Expression expr, HiwiiContext context){
		if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			String name = me.getName();
			List<Expression> args = me.getArguments();
			if(name.equals("decide")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				Expression res = doDecision(args.get(0));
				return res;//res is Exception or JudgmentResult
			}else if(name.equals("throw")){
				return new HiwiiException();
			}else if(name.equals("return")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				ReturnResult ret = new ReturnResult();
				Entity res = doCalculation(args.get(0));
				if(res instanceof HiwiiException){
					return (Expression) res;
				}
				ret.setResult(res);
				return ret;
			}
			if(name.equals("if")){
				return doSilentIfAction(args);
			}else if(name.equals("choose")){

			}else if(name.equals("each")){

			}else if(name.equals("for")){
				//for[x:=int0,condition,post,statement]
				if(args.size() == 2){
					//dowhileLoop
					return doWhileLoop(args.get(0), args.get(1), context);
				}
				if(args.size() != 4){
					return new HiwiiException();
				}
				//for[initial,condition,post,statement]
//				return doForLoop(args.get(0), args.get(1), args.get(2), args.get(3));
			}else if(name.equals("while")){
				if(args.size() != 2){
					return new HiwiiException();
				}			
				return doWhileLoop(args.get(0), args.get(1), context);
			}else if(name.equals("assign")){
				if(args.size() != 2){
					return new HiwiiException();
				}
				context.doAssignSilent(args.get(0), args.get(1));
			}else if(name.equals("turn")){
				if(args.size() != 2){
					return new HiwiiException();
				}
				return context.turnJudge(args.get(0), args.get(1));
			}else if(name.equals("var") || name.equals("variable")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				return context.newVariable(args.get(0));
			}else if(name.equals("vars") || name.equals("variables")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				return context.newVariable(args.get(0));
			}else if(name.equals("boolean")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				return context.newBoolean(args.get(0));
			}else if(name.equals("expression")){
				if(args.size() != 1){
					return new HiwiiException();
				}
//				return newExpression(args.get(0));
			}else{
				return new HiwiiException();
			}
//			else if(name.equals("expressions")){
//				//s表示静态
//				if(args.size() != 1){
//					return new HiwiiException();
//				}
//				return newExpressionST(args.get(0));
//			}
		}else if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			String name = ie.getName();
			if(name.equals("skip")){
				return new SkipReturn();
			}else if(name.equals("break")){
				return new BreakReturn();
			}else if(name.equals("return")){
				return new ReturnEnd();
			}else{
				return new HiwiiException();
			}
		}else if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			String name = fe.getName();
			if(name.equals("return")){
				if(fe.getArguments().size() != 1){
					return new HiwiiException();
				}			
				ReturnResult rr = new ReturnResult();
				Entity ent = doCalculation(fe.getArguments().get(0));
				rr.setResult(ent);
				return rr;
			}			
		}
		return null;
	}
	
	public Entity doProgramCalculation(BraceExpression prg, HiwiiContext context){
		RuntimeContext rc = newHostContext();
		List<Expression> sents = prg.getArray();
		Expression result = null;
		for(Expression expr:sents){
			//只允许context操作
			result = rc.doSilentAction(expr);
			
			if(result instanceof SkipReturn){
				break; //consume skip identifier
			}else if(result instanceof BreakReturn){
				return result;//will be consumed in loop action.if there is no loop will throw exception
			}else if(result instanceof ExitEnd){
				return result;
			}else if(result instanceof ReturnEnd){
				return result;
			}else if(result instanceof JudgmentResult){
				return result;
			}else if(result instanceof ReturnResult){
//				ReturnResult or = (ReturnResult) result;
//				return or.getResult();
				return result;
			}

			if(result instanceof HiwiiException){
				//				System.out.println("some exception happened!");
				return result;
			}
		}
		return new HiwiiException();
	}
	
	public Entity doProgramCalculation(BraceExpression prg){
		RuntimeContext rc = newHostContext();
		List<Expression> sents = prg.getArray();
		Expression result = null;
		for(Expression expr:sents){
			//只允许context操作
			result = rc.doSilentAction(expr);
			
			if(result instanceof SkipReturn){
				break; //consume skip identifier
			}else if(result instanceof BreakReturn){
				return result;//will be consumed in loop action.if there is no loop will throw exception
			}else if(result instanceof ExitEnd){
				return result;
			}else if(result instanceof ReturnEnd){
				return result;
			}else if(result instanceof JudgmentResult){
				return result;
			}else if(result instanceof ReturnResult){
//				ReturnResult or = (ReturnResult) result;
//				return or.getResult();
				return result;
			}

			if(result instanceof HiwiiException){
				//				System.out.println("some exception happened!");
				return result;
			}
		}
		return new HiwiiException();
	}
	public Entity doProgramCalculation(Entity subject, BraceExpression prg){
		RuntimeContext rc = newHostContext();
		List<Expression> sents = prg.getArray();
		Expression result = null;
		for(Expression expr:sents){
			result = rc.doSilentAction(expr);
			if(result == null){
				result = rc.doSilentAction(subject, expr);
			}

			if(result instanceof SkipReturn){
				break; //consume skip identifier
			}else if(result instanceof BreakReturn){
				return result;//will be consumed in loop action.if there is no loop will throw exception
			}else if(result instanceof ExitEnd){
				return result;
			}else if(result instanceof ReturnEnd){
				return result;
			}else if(result instanceof JudgmentResult){
				return result;
			}else if(result instanceof ReturnResult){
				return result;
			}

			if(result instanceof HiwiiException){
				//				System.out.println("some exception happened!");
				return result;
			}
		}
		return new NormalEnd();
	}
	
	public Expression doDefine(Expression source, Expression expr){
		if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			if(bo.getOperator().equals(":")){
				Expression left = bo.getLeft();
				String cogn = null;

				if(left instanceof IdentifierExpression){
					IdentifierExpression ie = (IdentifierExpression) left;
					cogn = ie.getName();
				}else{
					return new HiwiiException();
				}

				Expression right = bo.getRight();
				if(cogn.equals("Calculation") || cogn.equals("Decision") || cogn.equals("Action")){
					char tp = 0;
					if(cogn.equals("Action")){
						tp = 'a';
					}else if(cogn.equals("Calculation")){
						tp = 'c';
					}else{
						tp = 'd';
					}
//					return doDeclare(tp, source, right);
				}else if(cogn.equals("Process")) {
					
				}else{
//					return newDefinition(source, right);
				}
			}else{
//				return newDefinition(source, expr);
			}
		}
		
		return new NormalEnd();
	}
	
	public Expression unaryTranslate(UnaryOperation expr){
		String op = expr.getOperator();
		if(op.equals("!")){
			return new MappingExpression("negate",  Arrays.asList(expr.getOperand()));
		}else{
			try {
				String name = SystemOperators.getOperationName(op);
				FunctionExpression fe = new FunctionExpression(name, Arrays.asList(expr.getOperand()));
				return fe;
			} catch (ApplicationException e) {
				return null;
			}
		}
		//    	return null;
	}

	public Expression binaryTranslate(BinaryOperation expr){
		String op = expr.getOperator();
		List<Expression> args =  Arrays.asList(expr.getLeft(), expr.getRight());
		if(op.equals("&")){
			return new MappingExpression("and", args);
		}
		if(op.equals("!")){
			return new MappingExpression("or", args);
		}
		if(op.equals(":")){
			return new MappingExpression("describe", args);
		}
		if(op.equals("@")){
			return new MappingExpression("", args);
		}
		if(op.equals(":=")){
			return new MappingExpression("assign", args);
		}
		if(op.equals("::")){
			return new MappingExpression("judge", args);
		}
		Expression left = expr.getLeft();
		Expression right = expr.getRight();
		if(op.equals("->")){			
			
		}
		if(op.equals("=>")){
			LambdaMapping le = new LambdaMapping();
			List<String> keys = new ArrayList<String>();
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				keys.add(ie.getName());
			}else if(left instanceof BracketExpression){
				BracketExpression be = (BracketExpression) left;
				for(Expression exp:be.getArray()){
					if(left instanceof IdentifierExpression){
						IdentifierExpression ie = (IdentifierExpression) exp;
						keys.add(ie.getName());
					}else{
						return new HiwiiException();
					}
				}
			}else{
				return new HiwiiException();
			}
			le.setKeys(keys);
			le.setStatement(right);
			return le;
		}
		else{
			try {
				String name = SystemOperators.getOperationName(op);
				FunctionExpression fe = new FunctionExpression(name, args);
				return fe;
			} catch (ApplicationException e) {
				return new HiwiiException();
			}
		}
		//    	return null;
	}
	
	public Expression belongToDecision(Expression left, Expression right) {
		if(!(right instanceof IdentifierExpression)){
			return new HiwiiException();  //format err!
		}
		IdentifierExpression ie = (IdentifierExpression) right;
		Definition def = null;
		try {
			def = EntityUtil.proxyGetDefinition(ie.getName());
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}
		if(def == null){
			return new HiwiiException();
		}
		Entity ent = doCalculation(left);
		if(ent == null){
			return new HiwiiException();
		}
		if(ent instanceof HiwiiException){
			return (HiwiiException) ent;
		}
		boolean res = EntityUtil.judgeEntityIsDefinition(ent, def);
		return EntityUtil.decide(res);
	}
	
	public HostContext newHostContext(){
		return new HostContext(this);
	}
	
	public Expression sendMessage(Entity target, Expression expr){
		return target.doAction(expr);
	}
	
	public Expression getMessage(Entity from, Expression expr){
		return null;
	}
}
