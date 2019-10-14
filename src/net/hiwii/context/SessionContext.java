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
	 * �ɷ�֧/ѭ��/����ȴ�������ı��ʽ��ִ���е���
	 * ��Ӧask/whether�������ﶯ�ʣ������������������ִ��
	 * 
	 * ask�������ʣ�whether�ж�����
	 * answer()�������ʻش�
	 * yes/no�ж����ʴ�
	 * exception��
	 * state����
	 * ������ʹ
	 * @param subject
	 * @param expr
	 * @return
	 */
	
	
	/**
	 * �����ν��ֻ����id/function
	 * ����program�У���filter������ʽ���κα��ʽ�����ʾΪ����+ν��
	 */
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	

	/**
	 * echo��ʾTerminal����ask��whether��Ϣ��
	 * ��ask��ͬ��ask�������м��㣬��echoֻ������ʾ����������ʱ��������ͬ��
	 * ԭ�ƻ���answer��ʾecho��ֻ��echo����ࡣ
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
