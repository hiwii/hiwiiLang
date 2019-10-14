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
 * ladder�ĵ�һ��runtimeContext������sessionContext
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
	 * ����Ŀ����ʽ�����Բ�����ѧ�ϵ����㷽ʽ��һ��һ���򻯡�
	 * �����Ǽ򻯵���Ҫ���̣����ڷ��ֿ��Դ���һ�����ʽ��scan�������ʽ�������±��ʽ��
	 * ���ڿ��Լ���ı��ʽ���ü��������д���Ҳ��һ�ֱ��ʽ������
	 */
	private Expression targetExpression;

	/**
	 * ��targetExpression���м��㡣�������еı��ʽ��
	 * һ���ֱ��ʽ��Ҫ�������������hostObject��
	 * һ���ֱ��ʽ����Ҫ�������磺����string��
	 * @return
	 */
	public Expression calculateExpression(Expression target){

		return null;
	}

	/**
	 * ��targetExpression�����жϡ��������еĲ���
	 * @return
	 */
	public Expression decideExpression(){

		return null;
	}
	/**
	 * �жϷ�Ϊ���¼��֣�
	 * 1,wait for answer. ϵͳ������Ҫ���������ж϶��жϡ�
	 * 2,suspend�ж�. ϵͳ���ⲿ���ڲ������ж���ĳһָ��ǰ��
	 */
	private char breakType; //waitinforAnswer,suspend
	//	private char state; //running,waitinforAnswer,haveAnswer��suspend
	//���������������ڼ�¼answer��Ϣ.
	//TODO ���ַ���ֻ�ܼ�¼һ���ʴ�
	/*
	 * �Ͷ���״̬�仯���ԣ�ֻ�м�����ж����ʡ�
	 * ����ָ����Ȼ���ܻ����쳣��ok�ȴ𸴣���һ�㲻��Ҫ�ȴ��𸴡�
	 * ����ʹ�������������һ���Ǹ�ֵ���������㸳ֵ(assign)���жϸ�ֵ(judge)��
	 * ��һ����ֱ��ʹ�ã����磺���ж������(��if)����Ƚ������(��������)��
	 * ��һ�ֽ������ȫ�������
	 * ����취����ANSWER������ʽ����ؼ�����ʽ��Ȼ����proxyCalculation������
	 * ��proxyDecision�м���answer Expression��
	 * ?������Զ�̼�����ʽ
	 * һ��Ӧ����ͬ�ı��ʽ
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
	 * ��task����
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
	 * ��������£�chains��������һ��runtime���ҵ�һ��������SessionContext
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
