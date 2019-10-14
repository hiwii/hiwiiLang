package net.hiwii.view;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.runtime.RuntimePool;
import net.hiwii.user.Peer;

/**
 * 
 * @author ha-wangzhenhai
 * LocalHost:
 * doOperation(String name, List<Entity> args, RuntimePool pool);
 * 
 * boolean isObjectClass(object name, definition/class name)
 * StringToExpression
 * StringToAction
 * StringToJudgment
 * newDefinition
 * newClass
 * newStructure
 * 
 * Host object:
 * host.doRefer()
 * host #doAction()
 * host ?doJudge()
 *
 */
public class Host extends Peer {

//	Entity doAction(Entity active, Expression statement, RuntimePool pool);
//	Entity doAction(Entity active, Entity passive, Expression statement, RuntimePool pool);
//	
//	Entity doCognize(Entity active, Expression statement, RuntimePool pool);
//	
//	Entity doOperation(String fname, List<Expression> args, RuntimePool pool);
//	
//	boolean doJudge(Entity active, Entity passive, Expression jexp, RuntimePool pool) throws ApplicationException;
//	boolean doJudge(Entity active, Expression exp, RuntimePool pool);
//
//	Entity refer(Expression expr, RuntimePool pool);//Tobe delete,raplace by doOperation
//	
//	boolean identify(Entity entity, Expression cog, RuntimePool pool);
//	Expression doIdentify(Entity entity);
}
