package net.hiwii.context;

import java.util.ArrayList;
import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.def.Definition;
import net.hiwii.def.SimpleDefinition;
import net.hiwii.prop.Property;
import net.hiwii.system.SystemDefinition;
import net.hiwii.view.Entity;

/**
 * ladder的第一个runtimeContext必须是sessionContext
 * @author ha-wangzhenhai
 *
 */
public class RuntimeLadder extends Entity {
	private List<RuntimeContext> chains;

	public RuntimeLadder() {
		chains = new ArrayList<RuntimeContext>();
	}
	public List<RuntimeContext> getChains() {
		return chains;
	}
	public void setChains(List<RuntimeContext> chains) {
		this.chains = chains;
	}

	/************************manager begin**************/
	/*
	 * 对于目标表达式，可以采用数学上的运算方式，一步一步简化。
	 * 代换是简化的重要过程，对于发现可以代换一个表达式后，scan整个表达式，代换新表达式。
	 * 对于可以计算的表达式，用计算结果进行代换也是一种表达式代换。
	 */
	private Expression targetExpression;

	/**
	 * 对targetExpression进行计算。代换其中的表达式。
	 * 一部分表达式需要代换，如参数、hostObject等
	 * 一部分表达式不需要代换，如：数、string等
	 * @return
	 */
	public Expression calculateExpression(Expression target){

		return null;
	}

	/**
	 * 对targetExpression进行判断。代换其中的参数
	 * @return
	 */
	public Expression decideExpression(){

		return null;
	}
	/**
	 * 中断分为以下几种：
	 * 1,wait for answer. 系统由于需要异地运算或判断而中断。
	 * 2,suspend中断. 系统由外部或内部发起，中断于某一指令前。
	 */
	private char breakType; //waitinforAnswer,suspend
	//	private char state; //running,waitinforAnswer,haveAnswer，suspend
	//以下三个变量用于记录answer信息.
	//TODO 这种方法只能记录一次问答
	/*
	 * 就对象状态变化而言，只有计算和判断疑问。
	 * 动作指令虽然可能会有异常、ok等答复，但一般不需要等待答复。
	 * 疑问使用有两种情况，一种是赋值，包括计算赋值(assign)和判断赋值(judge)。
	 * 另一种是直接使用，比如：在判断语句中(如if)，或比较语句中(隐含计算)。
	 * 后一种解决，则全部解决。
	 * 解决办法是用ANSWER代替表达式中异地计算表达式，然后在proxyCalculation方法中
	 * 或proxyDecision中计算answer Expression。
	 * ?如何替代远程计算表达式
	 * 一次应该相同的表达式
	 */
	private boolean isBooType;   //boolean answer, false for calculation answer
	private Expression answer;  //answer Expression.
	//	private Entity calcuAnswer;
	//	private boolean booleanAnswer;

	public void start(){
		//read initial condition
		//execute

	}

	public void AnswerGot(){
		if(isBooType){

		}else{
			Entity result = chains.get(0).doCalculation(answer);

		}
	}

	/**
	 * 当task挂起
	 */
	public void taskContinue(){

	}

	public void stop(){

	}
	/************************manager end**************/
	public RuntimeContext getRootContext(){
		return chains.get(chains.size() - 1);
	}

	public RuntimeContext getTopContext(){
		return chains.get(0);
	}
	/**
	 * 正常情况下，chains中至少有一个runtime，且第一个必须是SessionContext
	 * @return
	 */
	public SessionContext getSessionContext(){
		SessionContext sc = (SessionContext) chains.get(chains.size() - 1);
		return sc;
	}
	
//	public Definition ladderHasDefinition(String name){
//		for(RuntimeContext context:chains){
//			Definition def = context.contextHasDefinition(name);
//			if(def != null){
//				return def;
//			}
//		}
//		
//		return proxyGetDefinition(name);
//	}
	
	public Definition proxyGetDefinition(String name){
		if(SystemDefinition.contains(name)){
			return new SimpleDefinition(name);
		}
		return null;
	}
	
	public Property proxyGetProperty(String name){
//		if(SystemDefinition.contains(name)){
//			return new SimpleDefinition(name);
//		}
		return null;
	}
	
	public RuntimeContext newRuntimeContext(char type){
		RuntimeContext rc = new RuntimeContext();
		rc.setType(type);
		rc.setLoop(false);
		RuntimeLadder rl = new RuntimeLadder();		
		List<RuntimeContext> ch = new ArrayList<RuntimeContext>();
		ch.add(rc);
		ch.addAll(chains);
		rl.setChains(ch);
		rc.setLadder(rl);
		return rc;
	}
}
