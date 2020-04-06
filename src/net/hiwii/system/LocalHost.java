package net.hiwii.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.sleepycat.je.DatabaseException;

import net.hiwii.cache.DefinitionPool;
import net.hiwii.cognition.Expression;
import net.hiwii.context.AdverbContext;
import net.hiwii.context.HiwiiContext;
import net.hiwii.context.RuntimeContext;
import net.hiwii.context.RuntimeLadder;
import net.hiwii.context.SessionContext;
import net.hiwii.db.HiwiiDB;
import net.hiwii.expr.BinaryOperation;
import net.hiwii.expr.FunctionExpression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.BraceExpression;
import net.hiwii.expr.SubjectVerb;
import net.hiwii.expr.UnaryOperation;
import net.hiwii.message.HiwiiException;
import net.hiwii.obj.Session;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.net.HostObject;
import net.hiwii.system.net.SocketServer;
import net.hiwii.system.runtime.RuntimePool;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.system.util.StringUtil;
import net.hiwii.term.CommandTerminal;
import net.hiwii.term.HostTerminal;
import net.hiwii.term.Terminal;
import net.hiwii.view.Entity;
import net.hiwii.view.HiwiiInstance;
import net.hiwii.view.runner.HttpServerRunner;
import net.hiwii.web.NetTerminal;

/**
 * 
 * @author ha-wangzhenhai
 * 对于console，LocalHost就是sessionContext
 * 20150602
 * terminal是一个特殊对象，异步接收和发送字符串。
 * 系统对象接收和发送Expression，同步进行。
 *
 */
public class LocalHost extends Entity{
	private static LocalHost instance = new LocalHost();
	private NavigableMap<String,SessionContext> remotes;//tcpip net teminal
	private Map<String,SessionContext> weixins;//tcpip net teminal
	
	private DefinitionPool defPool;
	private boolean openSpace;
	/*
	 * terminal有三种，commandTerminal、javaTermina和hostTerminal。
	 * 前两种都是本地执行，不需要保存。
	 * hostTerminal在客户端和服务端各自维护会话状态。
	 */
//	private NavigableMap<String,SessionContext> remIdx; //用于通过sessionId快速查找
//	private LanguageObject defaultLanguage;//用于输出
//	private Translator translator;
	private Terminal console;
	private String localDir = "";
	private String hostId;
	private SocketServer socketServer;
	
	//key is ip
	private NavigableMap<String,Terminal> terminals;
	//key is groupId
//	private NavigableMap<String,RuntimeLadder> groups;
	//Constants
	private HiwiiDB hiwiiDB = new HiwiiDB();


//	private JFXApplication application;

//	private Stage mainStage;
	private LocalHost(){
		remotes = new TreeMap<String,SessionContext>();
		weixins = new HashMap<String,SessionContext>();
		hostId = EntityUtil.getUUID();
		hiwiiDB.open();
		try {
			hiwiiDB.init();
			boolean res = hiwiiDB.isOpenSpace();
			setOpenSpace(res);
		} catch (DatabaseException e) {
			System.out.println("user init err!");
		} catch (IOException e) {
			System.out.println("user init err!");
		} catch (ApplicationException e) {
			System.out.println("user init err!");
		} catch (Exception e) {
			System.out.println("user init err!");
		}		
		startHttpServer();
		defPool = new DefinitionPool(100);
		System.out.println("hiwii system started!");
	}
	
	public static LocalHost getInstance(){
		return instance;
	}
	

//	public void  startSocketService(){
//		if(socketService == null){
//			socketService = new SocketService();
//			socketService.start();
//		}
//	}
	
	public void  startSocketServer(){
		if(socketServer == null){
			socketServer = new SocketServer();
			socketServer.start();
		}
	}
	
	public void startHttpServer(){
		HttpServerRunner httpServer = new HttpServerRunner();
		httpServer.start();
	}
	
	public HiwiiDB getHiwiiDB() {
		return hiwiiDB;
	}

	public void setHiwiiDB(HiwiiDB hiwiiDB) {
		this.hiwiiDB = hiwiiDB;
	}

	public Map<String, SessionContext> getWeixins() {
		return weixins;
	}

	public void setWeixins(Map<String, SessionContext> weixins) {
		this.weixins = weixins;
	}

	public DefinitionPool getDefPool() {
		return defPool;
	}

	public void setDefPool(DefinitionPool defPool) {
		this.defPool = defPool;
	}

	public NavigableMap<String, Terminal> getTerminals() {
		return terminals;
	}

	public void setTerminals(NavigableMap<String, Terminal> terminals) {
		this.terminals = terminals;
	}

	public NavigableMap<String, SessionContext> getRemotes() {
		return remotes;
	}

	public void setRemotes(NavigableMap<String, SessionContext> sessions) {
		this.remotes = sessions;
	}

//	public JFXApplication getApplication() {
//		return application;
//	}
//
//	public void setApplication(JFXApplication application) {
//		this.application = application;
//	}

	public boolean isOpenSpace() {
		return openSpace;
	}

	public void setOpenSpace(boolean openSpace) {
		this.openSpace = openSpace;
	}

	/**
	 * 用于记录当前路径
	 * @return
	 */
	public String getLocalDir() {
		return localDir;
	}

	public void setLocalDir(String localDir) {
		this.localDir = localDir;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	/*****************session begin***********************************/
	public SessionContext getRootSessionContext(){
		return getConsole().getSession().getContext();
	}
	
	public SessionContext newSessionContext(){
		SessionContext sc = new SessionContext();
		String id = EntityUtil.getUUID();//getNextChildId();
		sc.setContextId(id);
		return sc;
	}
	
	public void removeSessionByIp(String ip){
		if(remotes.containsKey(ip)){
			remotes.remove(ip);
		}
	}
	
	public SessionContext newSubSession(Terminal term, SessionContext parent){
		SessionContext context = new SessionContext();
		Session session = new Session();
		String id = EntityUtil.getUUID();
		context.setContextId(id);
		context.setSession(session);
		session.setContext(context);
		session.setTerminal(term);
		term.setSession(session);
		RuntimeLadder rl = new RuntimeLadder();		
		List<RuntimeContext> ch = new ArrayList<RuntimeContext>();
		ch.add(context);
		ch.addAll(parent.getLadder().getChains());
		rl.setChains(ch);
		context.setLadder(rl);
		
		remotes.put(id, context);
		return context;
	}
	
	public SessionContext getLocalSessionContext(String sessionId){
		if(remotes.containsKey(sessionId)){ //locals
			return remotes.get(sessionId);
		}
		return null;
	}
	
	public SessionContext getSubSession(String ip, int port){
		if(remotes.containsKey(ip)){
			return remotes.get(ip);
		}
		SessionContext context = new SessionContext();
		HostTerminal ht = new HostTerminal(ip, port);
		HostObject host = new HostObject(ip, port);
//		ht.setHost(host);
		Session session = new Session();
		String id = EntityUtil.getUUID();//getNextChildId();
		context.setContextId(id);
		context.setSession(session);
		session.setContext(context);
		session.setTerminal(ht);
		ht.setSession(session);
		return context;
	}
	
	public SessionContext getSessionContextByIP(String ip){
		return console.getSession().getContext(); //默认console
//		if(remotes.containsKey(ip)){
//			return remotes.get(ip);
//		}
//		return getSessionContextByIP(ip, 9099); //默认port=9099
	}
	
	public SessionContext getSessionContextByIP(String ip, int port){
		if(remotes.containsKey(ip)){
			return remotes.get(ip);
		}
		SessionContext context = new SessionContext();
		HostTerminal ht = new HostTerminal(ip, port);
//		HostObject host = new HostObject(ip, port);
//		ht.setHost(host);
		Session session = new Session();
		String id = EntityUtil.getUUID();//getNextChildId();
		context.setContextId(id);
		context.setSession(session);
		session.setContext(context);
		session.setTerminal(ht);
		ht.setSession(session);
		return context;
	}
	
	public SessionContext getSessionContextOfWeixin(String userId){
		if(weixins.containsKey(userId)){
			return weixins.get(userId);
		}
		System.out.print("微信用户登录:" + userId);
		SessionContext context = new SessionContext();
		NetTerminal nt = new NetTerminal();
		Session session = new Session();
		String id = EntityUtil.getUUID();//getNextChildId();
		context.setContextId(id);
		context.setSession(session);
		session.setContext(context);
		session.setTerminal(nt);
		HiwiiInstance user = new HiwiiInstance();
		user.setClassName("WeixinUser");
		user.setName(userId);
		nt.setSession(session);
		//TODO 删除不访问记录
		return context;
	}
	
	public SessionContext getSessionContext(String sessionId){
		if(remotes.containsKey(sessionId)){
			return remotes.get(sessionId);
		}
		return null;
	}

	public void putSessionContext(String sessionId, SessionContext sc){
		remotes.put(sessionId, sc);
	}
	public String getNextSessionId(String sessionId){
		String key = StringUtil.getNextKey(sessionId, remotes);
		return key;
	}
	
	
	/*****************session end***********************************/
	/**************************************localAction start
	 * @throws ApplicationException ***********************/
	public void startup() throws ApplicationException{
		CommandTerminal term = (CommandTerminal) openTerminal();
		console = term;
		term.doCommand();
	}
	public void exit(){
		hiwiiDB.close();
		System.exit(0);
	}
	public Terminal openTerminal() throws ApplicationException{
		CommandTerminal term = new CommandTerminal();
		SessionContext context = new SessionContext();
		Session session = context.getSession();
		String id = EntityUtil.getUUID();//getNextChildId();
		context.setContextId(id);
//		context.setSession(session);
//		session.setContext(context);
		session.setTerminal(term);
		term.setSession(session);

		return term;
	}
	
	/****************************local action start***********************/
	/**
	 * 每个进程有唯一signature，用于保证程序状态下，对象访问的reEntry和进程动作状态
	 */
	public synchronized String generateSequence(){
		return "0";
	}
	/***************************Entity Start*********************************/
	public void receiveMessage(Terminal term, String msg){
		
	}

	/***************************Entity End*********************************/


	public Terminal getConsole() {
		return console;
	}
	public void setConsole(Terminal console) {
		this.console = console;
	}

	public HiwiiException doStatement(Expression state){
		return null;
	}

	public String getGene() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getGene(Entity target) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//String.toBlock,toExpression,toJudgment,toStatement
	/***********************************************************/
	public void doProgram(BraceExpression program, RuntimePool pool){
		
	}
	
	public void doConcurrent(List<BraceExpression> programs, RuntimePool pool){
		
	}
	/*********************three base action**********/
	
	
	/**
	 * 用于程序中的单步执行，runtime表示程序环境,可以增加/删除/赋值变量
	 * @param subject
	 * @param expr
	 * @param pool
	 * @return
	 */
	public Expression doAction(Entity subject, Expression expr, HiwiiContext context){
		if(expr instanceof BraceExpression){
			
		}else if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			String name = fe.getName();
			if(name.equals("if")){
				
			}
			//persist search
			//if(defined(gene, function) getExpression
			//doAction(subject, expression, args, pool)
		}else if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			//persist search
			//if(defined(gene, identifier) getExpression
			//doAction(subject, expression, pool)
		}else if(expr instanceof SubjectVerb){
			SubjectVerb av = (SubjectVerb) expr;
			Entity ent = doOperation(subject, av.getAction(), context);
//			Expression ret = doAction(ent, av.getVerb(), pool);
		}
		return null;
	}
	public Entity doOperation(Entity subject, Expression expr, HiwiiContext context){
		return null;
	}
	public Expression doJudgment(Entity subject, Expression expr, HiwiiContext context){
		return null;
	}
	
	public Expression doContextPositive(Entity subject, Expression expr, List<AdverbContext> adverbs){
		if(expr instanceof UnaryOperation){
			UnaryOperation uo = (UnaryOperation) expr;
			return expr;
		}else if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			return expr;
		}else if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			String name = ie.getName();
		}else if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			String name = fe.getName();
			List<Entity> args = new ArrayList<Entity>();
			for(Expression exp:fe.getArguments()){
//				Entity ent = doContextCalculation(subject, exp, null);
//				args.add(ent);
			}
		}else if(expr instanceof BraceExpression){
			BraceExpression prg = (BraceExpression) expr;
			RuntimeContext con = new RuntimeContext();
			con.setType('j');
//			return doProgram(subject, prg, con);
		}
		return null;
	}
	
	//sesssion factory
	/**
	 * 先建立一个terminal，产生session，setTerminal.session
	 * session通过LocalHost管理，当session列表溢出，创建session失败，返回异常。
	 * @param terminal
	 * @return
	 * @throws ApplicationException
	 */
	public Session generateSession(Terminal term) throws ApplicationException{
			Session session = new Session();
			SessionContext context = new SessionContext();
			String id = EntityUtil.getUUID();
			context.setContextId(id);
			context.setSession(session);
			session.setContext(context);
			session.setTerminal(term);
			term.setSession(session);

			return session;
	}
	
//	private boolean started = false;

}
