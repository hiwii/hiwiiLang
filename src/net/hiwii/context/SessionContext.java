package net.hiwii.context;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.NormalEnd;
import net.hiwii.message.HiwiiException;
import net.hiwii.obj.Session;
import net.hiwii.term.Terminal;

public class SessionContext extends RuntimeContext {
	private Session session;
	private String sessionId;
//	private NavigableMap<String,Perception> perceptions;
	
	public SessionContext() {
//		results = new ArrayList<Entity>();
		RuntimeLadder rl = new RuntimeLadder();
		rl.getChains().add(this);
		setLadder(rl);
		setPath("");
		session = new Session();
		session.setContext(this);
//		this.sessionId = EntityUtil.getUUID();//TODO to delete reference of setSessionid
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
//	public List<Entity> getResults() {
//		return results;
//	}
//	public void setResults(List<Entity> results) {
//		this.results = results;
//	}
//	public void clearResults(){
//		results.clear();
//	}
	
	/**
	 * 由分支/循环/程序等带有主语的表达式，执行中调用
	 * 对应ask/whether等无主语动词，在有主语情况下优先执行
	 * 
	 * ask特殊疑问，whether判断疑问
	 * answer()特殊疑问回答
	 * yes/no判断疑问答复
	 * exception答复
	 * state陈述
	 * 其它祈使
	 * @param subject
	 * @param expr
	 * @return
	 */
	
	
	/**
	 * 对象的谓词只能是id/function
	 * 当在program中，在filter特殊表达式后，任何表达式必须表示为主语+谓词
	 */
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	

	/**
	 * echo表示Terminal返回ask或whether信息。
	 * 与ask不同，ask结果会进行计算，而echo只进行显示。但简单运算时，二者相同。
	 * 原计划用answer表示echo，只是echo更简洁。
	 * @param term
	 * @param args
	 * @param adverbs
	 * @return
	 */
	public Expression doEchoAction(Terminal term, List<Expression> args, List<AdverbContext> adverbs){
		if(args.size() != 1){
			return new HiwiiException();
		}
		Expression result = args.get(0);
		session.doResponse(term.toString() + ":" + result.toString());
		return new NormalEnd();
	}

}
