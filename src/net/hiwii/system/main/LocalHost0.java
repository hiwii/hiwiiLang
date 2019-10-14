package net.hiwii.system.main;

import java.util.ArrayList;
import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.FunctionExpression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.BraceExpression;
import net.hiwii.system.runtime.RuntimePool;
import net.hiwii.view.Entity;

/**
 * 
 * @author ha-wangzhenhai
 * 
 * Entity doAction(Entity active, Expression statement, RuntimePool pool);
 * 
 * Entity doRefer(Entity active, Expression expr, RuntimePool pool);
 * 
 * boolean doJudge(Entity active, Expression expr, RuntimePool pool)
 *
 * 
 * 以上是5个基本运算
 * doOperation通过name和Entity definition获得用户定义。
 * 
 * boolean exist(Entity active, Entity passive, Expression status, RuntimePool pool)
 * boolean exist(Entity active, Expression status, RuntimePool pool);
 * boolean notExist(Entity active, Entity passive, Expression status, RuntimePool pool)
 * boolean notExist(Entity active, Expression status, RuntimePool pool);
 * 
 * boolean exist(List<Entity> group, Expression status, RuntimePool pool);
 * boolean notExist(List<Entity> group, Expression status, RuntimePool pool);
 * 
 * Entity make(Entity passive, Expression status);
 * 
 * Entity doAction(Entity active, Expression statement);
 * Entity doAction(Entity active, Entity passive, Expression statement);
 * Entity doCognize(Entity active, Expression statement);
 * Entity doOperation(String fname, List<Expression> args);
 * boolean doJudge(Entity active, Entity passive, Expression jexp)
 * boolean doJudge(Entity active, Expression exp, RuntimePool pool);
 * Entity refer(Expression expr);//Tobe delete,raplace by doOperation
 *
 */
public class LocalHost0 {
	/**
	 * action hub
	 * @param active
	 * @param statement
	 * @param pool
	 * @return
	 */
//	public Entity doAction(Entity active, Expression statement, RuntimePool pool){
//		if(statement instanceof IdentifierExpression){
//			IdentifierExpression ie = (IdentifierExpression) statement;
//			return active.toDo(ie.getName(), pool);
//		}else if(statement instanceof FunctionExpression){
//			FunctionExpression fe = (FunctionExpression) statement;
//			List<Entity> ents = new ArrayList<Entity>();
//			for(Expression exp:fe.getArguments()){
//				Entity ent = doRefer(active, exp, pool);
//				ents.add(ent);
//			}
//			return active.toCognize(fe.getName(), pool);
//		}else if(statement instanceof Program){
//			
//		}
//		return null;
//	}
	
	/**
	 * service for getObject
	 * @param active
	 * @param expr
	 * @param pool
	 * @return
	 */
//	Entity doRefer(Entity active, Expression expr, RuntimePool pool){
//		if(expr instanceof IdentifierExpression){
//			IdentifierExpression ie = (IdentifierExpression) expr;
//			return active.toRefer(ie.getName(), pool);
//		}else if(expr instanceof FunctionExpression){
//			FunctionExpression fe = (FunctionExpression) expr;
//			List<Entity> ents = new ArrayList<Entity>();
//			for(Expression exp:fe.getArguments()){
//				Entity ent = active.getObject(exp, pool);
//				ents.add(ent);
//			}
//			return active.toRefer(fe.getName(), ents, pool);
//		}
//	}
	
	/**
	 * service for judge
	 * @param active
	 * @param expr
	 * @param pool
	 * @return
	 */
	public boolean doJudge(Entity active, Expression expr, RuntimePool pool){
		return false;
	}
	/**
	 * Operation hub
	 * @param fname
	 * @param args
	 * @param pool
	 * @return
	 */
	public Entity doOperation(String fname, List<Entity> args, RuntimePool pool){
		return null;
	}
}
