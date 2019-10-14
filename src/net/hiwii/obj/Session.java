package net.hiwii.obj;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NavigableMap;
import java.util.Stack;
import java.util.TreeMap;

import com.sleepycat.je.DatabaseException;

import net.hiwii.cognition.Expression;
import net.hiwii.context.SessionContext;
import net.hiwii.context.question.Question;
import net.hiwii.def.Definition;
import net.hiwii.expr.StringExpression;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.system.util.StringUtil;
import net.hiwii.term.Terminal;
import net.hiwii.user.User;
import net.hiwii.view.Entity;

/**
 * Session��hiwiiϵͳ��terminal�Ự�Ĵ���(delegation)����
 * �����ձ��ʽ��";"��","��β�������м�����input�С�
 * questions�������ⷽ��¼�����¼�������յ��𸴺���𸴡�
 * count���ڴ����ⷽ��¼
 * 
 * commandTeminal��ϵͳ����һ��session��
 * hostTeminal�ڲ�ͬ��host����ά���Լ���session��
 * 
 * define/extend/class
 * declare/implement
 * perceive(there/notThere/be/notBe/did/didNot/...)
 * advice/onException/onStatus/onTime
 * property/assignment
 * aware/compound/synthesis
 * ֱ������session��runtime��Ϊprogramִ���ж���ֻ�������
 * 
 * �ӻ����Ƕȣ�sessionֻ���ǵ��̵߳ġ�
 * @author ha-wangzhenhai
 *
 */
public class Session extends Entity {
	private User user;
	private String input;
	private String sessionId;
	private SessionContext context;
	private Terminal terminal;
	private int count;
	private List<Question> questions;
	private String fromId;//from sessionId
	private Stack<SessionContext> sessionStack;
	
	/*
	 * entities��ʱ��˳����¼10����������getLastObject
	 */
	private NavigableMap<String,Entity> entities;
	
	public Session() {
		this.sessionId = EntityUtil.getUUID();
		entities = new TreeMap<String,Entity>();
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	/*
	 * ���������͵����ʾ䣬һ�����ʾ䣬�������ʾ䣬ѡ�����ʾ䡣
	 * �﷨���з������ʾ䣬����һ�����ʾ�ķ���ʽ������һ�ֶ������ʾ䡣
	 * ѡ�����ʾ����������ʾ�����࣬��Ϊ�ش������none��ѡ����������������ʾ䡣
	 */
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public SessionContext getContext() {
		return context;
	}
	public void setContext(SessionContext context) {
		this.context = context;
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	//	public Expression doRequest(Expression expr){
//		return null;
//	}
//	
//	public Expression doReponse(Expression expr){
//		return null;
//	}
	public Terminal getTerminal() {
		return terminal;
	}
	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}
	
	public Stack<SessionContext> getSessionStack() {
		return sessionStack;
	}

	public void setSessionStack(Stack<SessionContext> sessionStack) {
		this.sessionStack = sessionStack;
	}

	public NavigableMap<String, Entity> getEntities() {
		return entities;
	}

	public void setEntities(NavigableMap<String, Entity> entities) {
		this.entities = entities;
	}

	public void pushContext(SessionContext ctx){
		if(sessionStack == null){
			sessionStack = new Stack<SessionContext>();
		}
		sessionStack.push(this.context);
		this.context = ctx;
	}
	
	public void popContext(){
		if(!sessionStack.isEmpty()){
			this.context = sessionStack.pop();
		}
	}
	
	public Entity getLastEntity(String sig) {
		if(entities.size() == 0) {
			return new HiwiiException("last not exist");
		}
		String key = entities.lastKey();
		if(key == null) {
			return null;
		}
		Entity ent = entities.get(key);
		try {
			Definition def = EntityUtil.proxyGetDefinition(ent.getClassName());
			if(def == null) {
				return new HiwiiException("def get err!");
			}
			if(StringUtil.matched(def.getSignature(), sig)) {
				return ent;
			}
		} catch (DatabaseException e) {
			return new HiwiiException("def get err!");
		} catch (IOException e) {
			return new HiwiiException("def get err!");
		} catch (ApplicationException e) {
			return new HiwiiException("def get err!");
		} catch (Exception e) {
			return new HiwiiException("def get err!");	
		}
		
		
		while(key != null) {
			key = entities.lowerKey(key);
			if(key == null) {
				break;
			}
			ent = entities.get(key);
			try {
				Definition def = EntityUtil.proxyGetDefinition(ent.getClassName());
				if(def == null) {
					return new HiwiiException("last not exist");
				}
				if(StringUtil.matched(def.getSignature(), sig)) {
					return ent;
				}
			} catch (DatabaseException e) {
				return new HiwiiException("last not exist");
			} catch (IOException e) {
				return new HiwiiException("last not exist");
			} catch (ApplicationException e) {
				return new HiwiiException("last not exist");
			} catch (Exception e) {
				return new HiwiiException("last not exist");	
			}
		}
		return null;
	}
	
	public void putEntity(Entity obj) {
		Date date=new Date();  
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMddhhmmssSSS"); 
		//limit size in 10
		if(entities.size() >= 10) {
			String last = entities.lastKey();
			entities.remove(last);
		}
		entities.put(df.format(date), obj);
		return;
	}
	public void doRequest(String comm){
		StringExpression se = new StringExpression(comm);
		Entity ret = se.doIdentifierCalculation("toExpression");//se.doOperaion(ie, session.getPool());
		if(ret instanceof HiwiiException){
			terminal.doResponse("parsing error!");
			return;
		}
		Expression exp = (Expression) ret;
		Expression res = context.doAction(exp);
		
		if(res instanceof HiwiiException){
			terminal.doResponse("Exception happened!");
		}
	}
	
	public void doResponse(String comm){
		terminal.doResponse(comm);
	}
	
	public Entity doCalculation(String comm){
		StringExpression se = new StringExpression(comm);
		Entity ret = se.doIdentifierCalculation("toExpression");//se.doOperaion(ie, session.getPool());
		if(ret instanceof HiwiiException){
			terminal.doResponse("parsing error!");
			return new HiwiiException();
		}
		Expression exp = (Expression) ret;
		Entity res = context.doCalculation(exp);
		
		return res;
	}
	
	public Expression doPositive(String comm){
		StringExpression se = new StringExpression(comm);
		Entity ret = se.doIdentifierCalculation("toExpression");//se.doOperaion(ie, session.getPool());
		if(ret instanceof HiwiiException){
			terminal.doResponse("parsing error!");
			return new HiwiiException();
		}
		Expression exp = (Expression) ret;
		Expression res = context.doDecision(exp);
		
		return res;
	}
	
}
