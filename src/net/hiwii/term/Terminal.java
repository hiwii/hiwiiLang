package net.hiwii.term;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sleepycat.je.DatabaseException;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.cognition.result.NormalEnd;
import net.hiwii.collection.EntityList;
import net.hiwii.db.HiwiiDB;
import net.hiwii.def.Definition;
import net.hiwii.expr.BinaryOperation;
import net.hiwii.expr.BracketExpression;
import net.hiwii.expr.CharExpression;
import net.hiwii.expr.FunctionExpression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.MappingExpression;
import net.hiwii.expr.NullExpression;
import net.hiwii.expr.ParenExpression;
import net.hiwii.expr.BraceExpression;
import net.hiwii.expr.StringExpression;
import net.hiwii.expr.SubjectVerb;
import net.hiwii.expr.UnaryOperation;
import net.hiwii.expr.adv.IdentifierBrace;
import net.hiwii.expr.sent.ConditionExpression;
import net.hiwii.expr.sent.SubjectAction;
import net.hiwii.message.HiwiiException;
import net.hiwii.obj.Session;
import net.hiwii.system.LocalHost;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.syntax.bin.BinaryFormat;
import net.hiwii.system.syntax.bin.HexFormat;
import net.hiwii.system.syntax.bin.OctalFormat;
import net.hiwii.system.syntax.number.DecimalNumber;
import net.hiwii.system.syntax.number.FractionNumber;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.system.syntax.number.ScientificNotation;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.user.User;
import net.hiwii.view.Entity;

/**
 * 
 * @author Administrator
 * 核心是response(Terminal term, String str)表示接收到一个其它Terminal的string请求。
 * 来自Terminal的请求有两种，一种是点对点的请求，一种是群的请求。两种请求获得会话的方式不同。
 * 点对点的请求不提供sessionId，群请求必然提供sessionId。
 * 另一种含有sessionId的请求是进程请求。由于一个系统可以同时执行多个进程，这样发往同一目标的
 * 请求都包含进程Id或者sessionId。
 *
 */
public abstract class Terminal extends Entity {
	private Session session;
	
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public abstract void doRequest(String comm);
//	public abstract Expression doRequest(String comm);
	public abstract String doResponse(String comm);
	public abstract void doQuestion(String quest);
	
	public void doResponse(Entity ent){}
	
	public void ReceiveMessage(Terminal term, String msg){
		
	}
	
	public void sendMessage(Terminal term, String msg){
		
	}
	
	public void response(Terminal term, String str){
		StringExpression se = new StringExpression(str);		
		Expression exp = se.toExpression();
		
		if(exp instanceof HiwiiException){
			//localHost.getService()异常处理
			return;
		}
		if(exp instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) exp;
			if(ie.getName().equals("true") || ie.getName().equals("false")){
				//判断疑问句
			}
		}else if(exp instanceof MappingExpression){
			MappingExpression map = (MappingExpression) exp;
			if(map.getName().equals("ask")){
				if(map.getArguments().size() != 1){
					doResponse("Exception happened!");
				}
				Entity ret = getSession().getContext().doCalculation(map.getArguments().get(0));
//				return doMediaResponse(ret);
			}else if(map.getName().equals("whether")){
				if(map.getArguments().size() != 1){
					doResponse("Exception happened!");
				}
				Expression result = getSession().getContext().doDecision(map.getArguments().get(0));
//				return doResponse(result.toString());
			}else if(map.getName().equals("answer")){
				//计算疑问句回答
				
			}
		}
	}
	
	public void response(byte[] content){
		
	}
	
	public Expression doAction(Expression expr){
		if(LocalHost.getInstance().isOpenSpace()) {
			return getSession().getContext().doAction(expr);
		}
		User user = getSession().getUser();
		if(user != null && user.getUserid().equals("admin")) {
			return getSession().getContext().doAction(expr);
		}
		if(expr instanceof BraceExpression){
			BraceExpression prg = (BraceExpression) expr;
			try {
				if(lockedProgram(prg)) {					
					if(user == null){
						return new HiwiiException("not login");
					}
					if(!permitProgram(prg)){
						return new HiwiiException("not permitted");
					}
				}				
			} catch (ApplicationException e) {
				return new HiwiiException();
			}
			return getSession().getContext().doAction(expr);
		}else if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			try {
				if(lockedIdentifier(ie)) {
					if(user == null){
						return new HiwiiException("not login");
					}
					if(!permitIdentifier(ie)){
						return new HiwiiException("not permitted");
					}
				}				
			} catch (ApplicationException e) {
				return new HiwiiException();
			}
			return getSession().getContext().doIdentifierAction(ie.getName());
		}else if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			try {
				if(lockedFunction(fe)) {
					if(user == null){
						return new HiwiiException();
					}			
				}
				if(!permitFunction(fe)){
					return new HiwiiException("not permitted");
				}
			} catch (ApplicationException e) {
				return new HiwiiException();
			}
			return getSession().getContext().doAction(expr);
		}else if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			String name = me.getName();
			Expression ret = proxyMappingAction(name, me.getArguments());
			if(ret != null) {
				return ret;
			}
			try {
				if(lockedMapping(me)) {
					if(user == null){
						return new HiwiiException();
					}			
				}
				if(!permitMapping(me)){
					return new HiwiiException("not permitted");
				}
			} catch (ApplicationException e) {
				return new HiwiiException();
			}
			return getSession().getContext().doMappingAction(name, me.getArguments());
		}else if(expr instanceof SubjectAction){
			SubjectAction sa = (SubjectAction) expr;
			try {
				if(lockedSubjectAction(sa)) {
					if(user == null){
						return new HiwiiException();
					}			
				}
				if(!permitSubjectAction(sa)){
					return new HiwiiException("not permitted");
				}
			} catch (ApplicationException e) {
				return new HiwiiException();
			}
			return getSession().getContext().doAction(expr);        	
		}else if(expr instanceof UnaryOperation){
			UnaryOperation uo = (UnaryOperation) expr;
			Expression result = getSession().getContext().unaryTranslate(uo);
			if(result == null){
				return new HiwiiException();//operator not recognized
			}
			return doAction(result);  //mapping or function
		}else if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			Expression result = getSession().getContext().binaryTranslate(bo);
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
		}else if(expr instanceof ConditionExpression){}
		return null;
	}

	public Entity doCalculation(Expression expr){
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
		if(LocalHost.getInstance().isOpenSpace()) {
			return getSession().getContext().doCalculation(expr);
		}
		User user = getSession().getUser();
		if(user != null && user.getUserid().equals("admin")) {
			return getSession().getContext().doAction(expr);
		}
		if(expr instanceof BraceExpression){
			BraceExpression prg = (BraceExpression) expr;
			try {
				if(lockedProgram(prg)) {					
					if(user == null){
						return new HiwiiException("not login");
					}
					if(!permitProgram(prg)){
						return new HiwiiException("not permitted");
					}
				}				
			} catch (ApplicationException e) {
				return new HiwiiException();
			}
			return getSession().getContext().doCalculation(expr);
		}else if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			try {
				if(lockedIdentifier(ie)) {
					if(user == null){
						return new HiwiiException("not login");
					}
					if(!permitIdentifier(ie)){
						return new HiwiiException("not permitted");
					}
				}				
			} catch (ApplicationException e) {
				return new HiwiiException();
			}
			return getSession().getContext().doIdentifierAction(ie.getName());
		}else if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			try {
				if(lockedFunction(fe)) {
					if(user == null){
						return new HiwiiException();
					}			
				}
				if(!permitFunction(fe)){
					return new HiwiiException("not permitted");
				}
			} catch (ApplicationException e) {
				return new HiwiiException();
			}
			return getSession().getContext().doCalculation(expr);
		}else if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			String name = me.getName();
			Expression ret = proxyMappingAction(name, me.getArguments());
			if(ret != null) {
				return ret;
			}
			try {
				if(lockedMapping(me)) {
					if(user == null){
						return new HiwiiException();
					}			
				}
				if(!permitMapping(me)){
					return new HiwiiException("not permitted");
				}
			} catch (ApplicationException e) {
				return new HiwiiException();
			}
			return getSession().getContext().doMappingAction(name, me.getArguments());
		}else if(expr instanceof SubjectAction){
			SubjectAction sa = (SubjectAction) expr;
			try {
				if(lockedSubjectAction(sa)) {
					if(user == null){
						return new HiwiiException();
					}			
				}
				if(!permitSubjectAction(sa)){
					return new HiwiiException("not permitted");
				}
			} catch (ApplicationException e) {
				return new HiwiiException();
			}
			return getSession().getContext().doCalculation(expr);        	
		}else if(expr instanceof UnaryOperation){
			UnaryOperation uo = (UnaryOperation) expr;
			Expression result = getSession().getContext().unaryTranslate(uo);
			if(result == null){
				return new HiwiiException();//operator not recognized
			}
			return doCalculation(result);  //mapping or function
		}else if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			Expression result = getSession().getContext().binaryTranslate(bo);
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
		}else if(expr instanceof IdentifierBrace){
			IdentifierBrace ib = (IdentifierBrace) expr;
			return getSession().getContext().doIdentifierCalculation(ib.getName(), ib.getConditions());
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
		}else if(expr instanceof SubjectVerb){
			SubjectVerb sv = (SubjectVerb) expr;
			Entity subject = doCalculation(sv.getSubject());
			return getSession().getContext().doDecision(subject, sv.getAction());
		}else if(expr instanceof UnaryOperation){
			UnaryOperation uo = (UnaryOperation) expr;
			Expression result = getSession().getContext().unaryTranslate(uo);
			if(result == null){
				return new HiwiiException();//operator not recognized
			}
			return doDecision(result);  //mapping or function
		}else if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			Expression result = getSession().getContext().binaryTranslate(bo);
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
		}else if(expr instanceof ConditionExpression){}
		return null;
	}
	
/*****************************************************************/
	public boolean lockedIdentifierAction(String name)
			throws ApplicationException{
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			boolean ret = db.lockedIdAction(name, null);
			return ret;
		} catch (DatabaseException e) {
			throw new ApplicationException("err");
		} catch (IOException e) {
			throw new ApplicationException("err");
		} catch (ApplicationException e) {
			throw new ApplicationException("err");
		} catch (Exception e) {
			throw new ApplicationException("err");
		}
	}
	
	public boolean lockedExpression(Expression expr) throws ApplicationException{
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			return lockedIdentifier(ie);
		}
		return false;
	}
	public boolean lockedFunction(FunctionExpression fe)
			throws ApplicationException{
		String name = fe.getName();
		if(name.equals("register")) {
			return false;
		}
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			boolean ret = db.lockedIdAction(name, null);
			if(ret) {
				return ret;
			}
			for(Expression expr:fe.getArguments()) {
				ret = lockedExpression(expr);
			}
			return false;
		} catch (DatabaseException e) {
			throw new ApplicationException("err");
		} catch (IOException e) {
			throw new ApplicationException("err");
		} catch (ApplicationException e) {
			throw new ApplicationException("err");
		} catch (Exception e) {
			throw new ApplicationException("err");
		}
	}
	
	public boolean lockedMapping(MappingExpression me)
			throws ApplicationException{
		String name = me.getName();
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			boolean ret = db.lockedIdAction(name, null);
			if(ret) {
				return ret;
			}
			for(Expression expr:me.getArguments()) {
				ret = lockedExpression(expr);
			}
			return false;
		} catch (DatabaseException e) {
			throw new ApplicationException("err");
		} catch (IOException e) {
			throw new ApplicationException("err");
		} catch (ApplicationException e) {
			throw new ApplicationException("err");
		} catch (Exception e) {
			throw new ApplicationException("err");
		}
	}
	
	public boolean lockedIdentifier(IdentifierExpression ie)
			throws ApplicationException{
		String name = ie.getName();
		if(name.equals("openSpace")) {
			return true;
		}
		if(name.equals("closeSpace")) {
			return true;
		}
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			boolean ret = db.lockedIdAction(name, null);
			return ret;
		} catch (DatabaseException e) {
			throw new ApplicationException("err");
		} catch (IOException e) {
			throw new ApplicationException("err");
		} catch (ApplicationException e) {
			throw new ApplicationException("err");
		} catch (Exception e) {
			throw new ApplicationException("err");
		}
	}
	
	public boolean lockedProgram(BraceExpression prg) throws ApplicationException{
		for(Expression expr:prg.getArray()) {
			boolean ret = lockedExpression(expr);
			if(ret) {
				return true;
			}
		}
		return false;
	}
	
	public boolean lockedSubjectAction(SubjectAction sa) throws ApplicationException{
		Expression sub = sa.getSubject();
		if(lockedExpression(sub)) {
			return true;
		}
		Expression act = sa.getAction();
		if(lockedExpression(act)) {
			return true;
		}
		return false;
	}
	
	/**-------------------------****/
	public boolean excludeExpression(Expression expr) throws ApplicationException{
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			return lockedIdentifier(ie);
		}
		return false;
	}
	public boolean excludeFunction(FunctionExpression fe)
			throws ApplicationException{
		String name = fe.getName();
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			boolean ret = db.lockedIdAction(name, null);
			if(ret) {
				return ret;
			}
			for(Expression expr:fe.getArguments()) {
				ret = lockedExpression(expr);
			}
			return false;
		} catch (DatabaseException e) {
			throw new ApplicationException("err");
		} catch (IOException e) {
			throw new ApplicationException("err");
		} catch (ApplicationException e) {
			throw new ApplicationException("err");
		} catch (Exception e) {
			throw new ApplicationException("err");
		}
	}
	
	public boolean excludeMapping(MappingExpression me)
			throws ApplicationException{
		String name = me.getName();
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			boolean ret = db.lockedIdAction(name, null);
			if(ret) {
				return ret;
			}
			for(Expression expr:me.getArguments()) {
				ret = lockedExpression(expr);
			}
			return false;
		} catch (DatabaseException e) {
			throw new ApplicationException("err");
		} catch (IOException e) {
			throw new ApplicationException("err");
		} catch (ApplicationException e) {
			throw new ApplicationException("err");
		} catch (Exception e) {
			throw new ApplicationException("err");
		}
	}
	
	public boolean excludeIdentifier(IdentifierExpression ie)
			throws ApplicationException{
		String name = ie.getName();
		if(name.equals("openSpace")) {
			return true;
		}
		if(name.equals("closeSpace")) {
			return true;
		}
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			boolean ret = db.lockedIdAction(name, null);
			return ret;
		} catch (DatabaseException e) {
			throw new ApplicationException("err");
		} catch (IOException e) {
			throw new ApplicationException("err");
		} catch (ApplicationException e) {
			throw new ApplicationException("err");
		} catch (Exception e) {
			throw new ApplicationException("err");
		}
	}
	
	public boolean excludeProgram(BraceExpression prg) throws ApplicationException{
		for(Expression expr:prg.getArray()) {
			boolean ret = lockedExpression(expr);
			if(ret) {
				return true;
			}
		}
		return false;
	}
	
	public boolean excludeSubjectAction(SubjectAction sa) throws ApplicationException{
		Expression sub = sa.getSubject();
		if(lockedExpression(sub)) {
			return true;
		}
		Expression act = sa.getAction();
		if(lockedExpression(act)) {
			return true;
		}
		return false;
	}
	/*****-----------------***/
	
	public boolean permitExpression(Expression expr) throws ApplicationException{
		if(expr instanceof CharExpression){
			return true;
		}else if(expr instanceof StringExpression){
			return true;
		}else if(expr instanceof FractionNumber){
			return true;
		}else if(expr instanceof ScientificNotation){
			return true;
		}else if(expr instanceof IntegerNumber){
			return true;
		}else if(expr instanceof DecimalNumber){
			return true;
		}else if(expr instanceof HexFormat){
			return true;
		}else if(expr instanceof OctalFormat){
			return true;
		}else if(expr instanceof BinaryFormat){//Number Cognition
			return true;
		}else if(expr instanceof NullExpression){
			return true;
		}
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			return permitIdentifier(ie);
		}
		if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			return permitFunction(fe);
		}
		if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			return permitMapping(me);
		}
		if(expr instanceof BraceExpression){
			BraceExpression me = (BraceExpression) expr;
			return permitProgram(me);
		}
		return false;
	}
	
	public boolean permitIdentifier(IdentifierExpression ie) throws ApplicationException{
		String name = ie.getName();
		User user = getSession().getUser();
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			boolean lock = db.userIdentifierAction(name, user.getUserid());
			if(lock){
				return true;
			}
			return false;
		} catch (DatabaseException e) {
			throw new ApplicationException();
		} catch (IOException e) {
			throw new ApplicationException();
		} catch (ApplicationException e) {
			throw new ApplicationException();
		} catch (Exception e) {
			throw new ApplicationException();
		} 
	}
	
	public boolean permitFunction(FunctionExpression fe) throws ApplicationException{
		User user = getSession().getUser();
		String name = fe.getName();
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			boolean lock = db.userIdentifierAction(name, user.getUserid());
			if(!lock){
				return false;
			}
			for(Expression expr:fe.getArguments()) {
				boolean ret = permitExpression(expr);
				if(!ret) {
					return false;
				}
			}
			return true;
		} catch (DatabaseException e) {
			throw new ApplicationException();
		} catch (IOException e) {
			throw new ApplicationException();
		} catch (ApplicationException e) {
			throw new ApplicationException();
		} catch (Exception e) {
			throw new ApplicationException();
		} 
	}
	
	public boolean permitMapping(MappingExpression me) throws ApplicationException{
		User user = getSession().getUser();
		String name = me.getName();
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			boolean lock = db.userIdentifierAction(name, user.getUserid());
			if(!lock){
				return false;
			}
			for(Expression expr:me.getArguments()) {
				boolean ret = permitExpression(expr);
				if(!ret) {
					return false;
				}
			}
			return true;
		} catch (DatabaseException e) {
			throw new ApplicationException();
		} catch (IOException e) {
			throw new ApplicationException();
		} catch (ApplicationException e) {
			throw new ApplicationException();
		} catch (Exception e) {
			throw new ApplicationException();
		} 
	}
	
	public boolean permitProgram(BraceExpression prg) throws ApplicationException{
		for(Expression expr:prg.getArray()) {
			boolean ret = permitExpression(expr);
			if(!ret) {
				return false;
			}
		}
		return true;
	}
	
	public boolean permitSubjectAction(SubjectAction sa) throws ApplicationException{
		Expression sub = sa.getSubject();
		if(!permitExpression(sub)) {
			return false;
		}
		Expression act = sa.getAction();
		if(!permitExpression(act)) {
			return false;
		}
		return true;
	}
	public boolean permitIdentifierAction(Entity subject, String name) throws ApplicationException{
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			Definition def = EntityUtil.proxyGetDefinition(subject.getClassName());
			if(def == null){
				throw new ApplicationException("definition null!");
			}
			
			User user = getSession().getUser();
			if(user == null){
				return false;
			}
			String userid = user.getUserid();
			
			if(def.isClosing()){
				JudgmentResult jr = db.userIdentifierAction(def.getClassName(), name, userid);
				if(jr != null){
					if(EntityUtil.judge(jr)){
						return true;
					}
				}
				return false;
			}else{
				JudgmentResult jr = db.userIdentifierAction(def.getClassName(), name, userid);
				if(jr != null){
					if(!EntityUtil.judge(jr)){
						return false;
					}
				}
				return true;
			}
		} catch (DatabaseException e) {
			throw new ApplicationException();
		} catch (IOException e) {
			throw new ApplicationException();
		} catch (ApplicationException e) {
			throw new ApplicationException();
		} catch (Exception e) {
			throw new ApplicationException();
		} 
	}
	
	public boolean permitIdentifierCalculation(String name) throws ApplicationException{
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			boolean lock = db.lockedIdCalculation(name, null);
			if(lock){
				User user = getSession().getUser();
				if(user == null){
					return false;
				}
				String userid = user.getUserid();
				boolean ok = db.userIdentifierCalculation(name, userid);
				return ok;
			}
			return true;
		} catch (DatabaseException e) {
			throw new ApplicationException();
		} catch (IOException e) {
			throw new ApplicationException();
		} catch (ApplicationException e) {
			throw new ApplicationException();
		} catch (Exception e) {
			throw new ApplicationException();
		} 
	}
	public Expression proxyMappingAction(String name, List<Expression> args){
		if(name.equals("login") || name.equals("logout")){
			return getSession().getContext().proxyMappingAction(name, args);
		}
		return null;
	}
	
}
