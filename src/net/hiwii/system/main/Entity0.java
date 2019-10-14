package net.hiwii.system.main;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.FunctionExpression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.system.runtime.RuntimePool;

/**
 * 
 * @author ha-wangzhenhai
 * toDo(string name, RuntimePool);expression is id or function
 * toDo(string name, List<Entity> args, RuntimePool);expression is id or function
 * 
 * toRefer(string name, RuntimePool);expression is id or function
 * toRefer(string name, List<Entity> args, RuntimePool);expression is id or function
 * 
 * toJudge(string name, RuntimePool);object is adjective
 * toJudge(string name, List<Entity> args, RuntimePool); judgeName(object.arg1, object.arg2)
 * 
 * Expression getDefinition(); return definition name or collection of definition.
 * boolean isClass(String name)
 * boolean inStructure(structure name)
 * boolean inCollection(Collection name)
 *
 */
public class Entity0 {
//	public Entity refer(Expression exp, RuntimePool pool){
//		if(exp instanceof IdentifierExpression){
//			IdentifierExpression ie = (IdentifierExpression) exp;
//		}else if(exp instanceof FunctionExpression){
//			FunctionExpression fe = (FunctionExpression) exp;
//		}else{
//			LocalHost host = LocalHost.getInstance();
//			return host.doRefer(this, exp, pool);
//		}
//		return null;
//	}
	
	/**
	 * todo invoke getObject(String name)
	 * @param name
	 * @param pool
	 * @return
	 */
//	public Entity refer(String name, RuntimePool pool){
//		return null;
//	}
//	
//	public Entity refer(String name, List<Entity> args, RuntimePool pool){
//		return null;
//	}
//	
//	public boolean judge(Expression exp, RuntimePool pool){
//		if(exp instanceof IdentifierExpression){
//			IdentifierExpression ie = (IdentifierExpression) exp;
//		}else if(exp instanceof FunctionExpression){
//			FunctionExpression fe = (FunctionExpression) exp;
//		}else{
//			LocalHost host = LocalHost.getInstance();
//			return host.doJudge(this, exp, pool);
//		}
//		return false;
//	}
//	
//	public boolean judge(String name, RuntimePool pool){
//		return false;
//	}
//	
//	public boolean judge(String name, List<Entity> args, RuntimePool pool){
//		return false;
//	}
//	
//	public Entity does(Expression exp, RuntimePool pool){
//		if(exp instanceof IdentifierExpression){
//			IdentifierExpression ie = (IdentifierExpression) exp;
//		}else if(exp instanceof FunctionExpression){
//			FunctionExpression fe = (FunctionExpression) exp;
//		}else{
//			LocalHost host = LocalHost.getInstance();
//			return host.doAction(this, exp, pool);
//		}
//	}
}
