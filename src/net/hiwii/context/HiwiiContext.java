package net.hiwii.context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.NavigableMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Transaction;

import net.hiwii.arg.Argument;
import net.hiwii.cognition.Expression;
import net.hiwii.cognition.NullValue;
import net.hiwii.cognition.result.BreakReturn;
import net.hiwii.cognition.result.ExitEnd;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.cognition.result.NormalEnd;
import net.hiwii.cognition.result.ReturnEnd;
import net.hiwii.cognition.result.ReturnResult;
import net.hiwii.cognition.result.SkipReturn;
import net.hiwii.collection.EntityList;
import net.hiwii.collection.EnumSet;
import net.hiwii.collection.GangOfObject;
import net.hiwii.collection.ObjectContainer;
import net.hiwii.collection.TypedEntityList;
import net.hiwii.db.HiwiiDB;
import net.hiwii.def.Abstraction;
import net.hiwii.def.Assignment;
import net.hiwii.def.Cognition;
import net.hiwii.def.Declaration;
import net.hiwii.def.DecoratedDefinition;
import net.hiwii.def.Definition;
import net.hiwii.def.DefinitionLimitView;
import net.hiwii.def.DefinitionView;
import net.hiwii.def.ExtendedDefinition;
import net.hiwii.def.Judgment;
import net.hiwii.def.Perception;
import net.hiwii.def.SimpleDefinition;
import net.hiwii.def.TypeView;
import net.hiwii.def.decl.ConditionDeclaration;
import net.hiwii.def.decl.FunctionDeclaration;
import net.hiwii.def.list.Array;
import net.hiwii.def.list.ListClass;
import net.hiwii.def.list.SetClass;
import net.hiwii.def.list.Tuple;
import net.hiwii.entity.ImageEntity;
import net.hiwii.expr.ActionAtSubject;
import net.hiwii.expr.BinaryOperation;
import net.hiwii.expr.BraceExpression;
import net.hiwii.expr.BracketExpression;
import net.hiwii.expr.CharExpression;
import net.hiwii.expr.FunctionExpression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.MappingExpression;
import net.hiwii.expr.NullExpression;
import net.hiwii.expr.ParenExpression;
import net.hiwii.expr.Parentheses;
import net.hiwii.expr.StringExpression;
import net.hiwii.expr.SubjectOperation;
import net.hiwii.expr.SubjectStatus;
import net.hiwii.expr.SubjectVerb;
import net.hiwii.expr.SubjectVerbAtom;
import net.hiwii.expr.UnaryOperation;
import net.hiwii.expr.adv.FunctionBrace;
import net.hiwii.expr.adv.IdentifierBrace;
import net.hiwii.expr.date.TimeValue;
import net.hiwii.expr.entity.FunctionEntity;
import net.hiwii.expr.sent.ConditionExpression;
import net.hiwii.lambda.ArgumentedLambda;
import net.hiwii.lambda.EntityLambda;
import net.hiwii.lambda.LambdaMapping;
import net.hiwii.lambda.Symbol;
import net.hiwii.message.HiwiiException;
import net.hiwii.obj.Session;
import net.hiwii.obj.decl.SwitchObject;
import net.hiwii.obj.file.Directory;
import net.hiwii.obj.file.FileObject;
import net.hiwii.obj.time.CoupleEvent;
import net.hiwii.obj.time.TimeObject;
import net.hiwii.prop.Property;
import net.hiwii.prop.Variable;
import net.hiwii.prop.VariableStore;
import net.hiwii.reg.AlternateRegular;
import net.hiwii.reg.RegularExpression;
import net.hiwii.struct.EntityProperty;
import net.hiwii.system.LocalHost;
import net.hiwii.system.SystemDefinition;
import net.hiwii.system.SystemOperators;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.net.HostObject;
import net.hiwii.system.syntax.bin.BinaryFormat;
import net.hiwii.system.syntax.bin.HexFormat;
import net.hiwii.system.syntax.bin.OctalFormat;
import net.hiwii.system.syntax.number.DecimalNumber;
import net.hiwii.system.syntax.number.FractionNumber;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.system.syntax.number.RealNumber;
import net.hiwii.system.syntax.number.ScientificNotation;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.system.util.StringUtil;
import net.hiwii.term.Terminal;
import net.hiwii.user.Group;
import net.hiwii.user.User;
import net.hiwii.view.Entity;
import net.hiwii.view.HiwiiInstance;

/**
 * 从doAction开始。
 * doCalculation,doDecision都是基本动作。
 * 
 * doIdentifier/function/mapping分为ladder动作和context动作。
 * proxy动作表示系统自身的动作。
 * @author hiwii
 *
 */
public class HiwiiContext extends Entity {
	/*
	 * isFunction用于设置一个silentAction的顶点，当assign等方法沿ladder找到此则结束。
	 */
	private boolean isFunction;
	//	private List<HiwiiContext> children;
	private boolean loop = false;
	private String contextId;
	private RuntimeLadder ladder;

	private NavigableMap<String,Definition> defines;
	private NavigableMap<String,Property> props;

	//	private NavigableMap<String,EntityProperty> eprops;

	private NavigableMap<String,Assignment> assigns;
	private NavigableMap<String,Perception> perceptions;

	private NavigableMap<String,Declaration> calculs;
	private NavigableMap<String,Declaration> actions;
	private NavigableMap<String,Declaration> decisions;

	private NavigableMap<String,FunctionDeclaration> fcalculs;
	private NavigableMap<String,FunctionDeclaration> factions;
	private NavigableMap<String,FunctionDeclaration> fdecisions;

	//interface function
	private NavigableMap<String,FunctionDeclaration> intf_fcalculs;
//	private NavigableMap<String,FunctionDeclaration> intf_factions;
//	private NavigableMap<String,FunctionDeclaration> intf_fdecisions;

	//implement function
	private NavigableMap<String,FunctionDeclaration> impl_fcalculs;
	private NavigableMap<String,FunctionDeclaration> impl_factions;
	private NavigableMap<String,FunctionDeclaration> impl_fdecisions;

	//zero argument lambda.
	private NavigableMap<String,Expression> mcalculs;
	private NavigableMap<String,Expression> mactions;
	private NavigableMap<String,Expression> mdecisions;

	//lambda with arguments
	private NavigableMap<String,LambdaMapping> lcalculs;
	private NavigableMap<String,LambdaMapping> lactions;
	private NavigableMap<String,LambdaMapping> ldecisions;
	
	private NavigableMap<String,SwitchObject> switches;
	//	private NavigableMap<String,MappingObject> mappings;

	private NavigableMap<String,Judgment> positives;
	private NavigableMap<String,Judgment> negatives;

	private SortedSet<String> verbs;
	private SortedSet<String> states;

	private SortedSet<String> symbols;  //nouns

	private NavigableMap<String,CoupleEvent> dones;
	private NavigableMap<String,CoupleEvent> todos;
	private NavigableMap<String,CoupleEvent> doings;

	private NavigableMap<String,Entity> entities; //identifier

	private NavigableMap<String,Cognition> cogs;
	
	private NavigableMap<String,Variable> vars;
	private NavigableMap<String,Entity> refers; //identifier
	
	private NavigableMap<String,JudgmentResult> bools; 
	private NavigableMap<String,JudgmentResult> boolST; 
	private NavigableMap<String,Expression> expressions; 
	private NavigableMap<String,Expression> expressionST; 

//	private NavigableMap<String,HiwiiInstance> exists;
	/*
	 * sessionContext是所有runtimeContext的root
	 * perception只在sessionContext。
	 * sessionContext属性每个context都有。
	 * localHost的sessionContext属性为null。
	 * sessionContext由localHost产生。
	 * sessionContext的对应属性为自身。
	 * 所有runtimeContext由SessionContext产生。 
	 */
	//	private SessionContext sessionContext;

	/**
	 * 对于context中每一次执行命令，默认条件存在于adverb中。
	 * 由于adverb与context内容相似，存在优先顺序。
	 * 因此adverb作为属性存在于context中，当沿着context chain寻找属性，每个context优先于adverb。
	 */
	private RuntimeContext adverb;

	public HiwiiContext() {
		this.setFunction(false);
		defines = new TreeMap<String,Definition>();
		props = new TreeMap<String,Property>();

		//		eprops = new TreeMap<String,EntityProperty>();

		assigns = new TreeMap<String,Assignment>();
		perceptions = new TreeMap<String,Perception>();

		calculs = new TreeMap<String,Declaration>();
		actions = new TreeMap<String,Declaration>();
		decisions = new TreeMap<String,Declaration>();

		fcalculs = new TreeMap<String,FunctionDeclaration>();
		factions = new TreeMap<String,FunctionDeclaration>();
		fdecisions = new TreeMap<String,FunctionDeclaration>();

		intf_fcalculs = new TreeMap<String,FunctionDeclaration>();
//		intf_factions = new TreeMap<String,FunctionDeclaration>();
//		intf_fdecisions = new TreeMap<String,FunctionDeclaration>();

		impl_fcalculs = new TreeMap<String,FunctionDeclaration>();
		impl_factions = new TreeMap<String,FunctionDeclaration>();
		impl_fdecisions = new TreeMap<String,FunctionDeclaration>();

		mcalculs = new TreeMap<String,Expression>();
		mactions = new TreeMap<String,Expression>();
		mdecisions = new TreeMap<String,Expression>();

		lcalculs = new TreeMap<String,LambdaMapping>();
		lactions = new TreeMap<String,LambdaMapping>();
		ldecisions = new TreeMap<String,LambdaMapping>();

		positives = new TreeMap<String,Judgment>();
		negatives = new TreeMap<String,Judgment>();

		switches = new TreeMap<String,SwitchObject>();
		//		mappings = new TreeMap<String,MappingObject>();

		verbs = new TreeSet<String>();
		states = new TreeSet<String>();

		symbols = new TreeSet<String>();

		dones = new TreeMap<String,CoupleEvent>();

		refers = new TreeMap<String,Entity>();

		entities = new TreeMap<String,Entity>();

		vars = new TreeMap<String,Variable>();
		bools = new TreeMap<String,JudgmentResult>();
		boolST = new TreeMap<String,JudgmentResult>();
		expressions = new TreeMap<String,Expression>();
		expressionST = new TreeMap<String,Expression>();
		
//		exists = new TreeMap<String,HiwiiInstance>();		
		cogs = new TreeMap<String,Cognition>();
		//		<String,ExistStatement> states
		//		children = new ArrayList<HiwiiContext>();
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public RuntimeContext getAdverb() {
		return adverb;
	}

	public void setAdverb(RuntimeContext adverb) {
		this.adverb = adverb;
	}

	public NavigableMap<String, Definition> getDefines() {
		return defines;
	}

	public void setDefines(NavigableMap<String, Definition> defines) {
		this.defines = defines;
	}

	public NavigableMap<String, Property> getProps() {
		return props;
	}

	public void setProps(NavigableMap<String, Property> props) {
		this.props = props;
	}

	public NavigableMap<String, Assignment> getAssigns() {
		return assigns;
	}

	public void setAssigns(NavigableMap<String, Assignment> assigns) {
		this.assigns = assigns;
	}

	public NavigableMap<String, Perception> getPerceptions() {
		return perceptions;
	}

	public void setPerceptions(NavigableMap<String, Perception> perceptions) {
		this.perceptions = perceptions;
	}

	public NavigableMap<String, Expression> getMcalculs() {
		return mcalculs;
	}

	public void setMcalculs(NavigableMap<String, Expression> ecalculs) {
		this.mcalculs = ecalculs;
	}

	public NavigableMap<String, Expression> getMactions() {
		return mactions;
	}

	public void setMactions(NavigableMap<String, Expression> eactions) {
		this.mactions = eactions;
	}

	public NavigableMap<String, Expression> getMdecisions() {
		return mdecisions;
	}

	public void setMdecisions(NavigableMap<String, Expression> edecisions) {
		this.mdecisions = edecisions;
	}

	public NavigableMap<String, LambdaMapping> getLcalculs() {
		return lcalculs;
	}

	public void setLcalculs(NavigableMap<String, LambdaMapping> lcalculs) {
		this.lcalculs = lcalculs;
	}

	public NavigableMap<String, LambdaMapping> getLactions() {
		return lactions;
	}

	public void setLactions(NavigableMap<String, LambdaMapping> lactions) {
		this.lactions = lactions;
	}

	public NavigableMap<String, LambdaMapping> getLdecisions() {
		return ldecisions;
	}

	public void setLdecisions(NavigableMap<String, LambdaMapping> ldecisions) {
		this.ldecisions = ldecisions;
	}

	public NavigableMap<String, FunctionDeclaration> getImpl_fcalculs() {
		return impl_fcalculs;
	}

	public void setImpl_fcalculs(NavigableMap<String, FunctionDeclaration> impl_fcalculs) {
		this.impl_fcalculs = impl_fcalculs;
	}

	public NavigableMap<String, FunctionDeclaration> getImpl_factions() {
		return impl_factions;
	}

	public void setImpl_factions(NavigableMap<String, FunctionDeclaration> impl_factions) {
		this.impl_factions = impl_factions;
	}

	public NavigableMap<String, FunctionDeclaration> getImpl_fdecisions() {
		return impl_fdecisions;
	}

	public void setImpl_fdecisions(NavigableMap<String, FunctionDeclaration> impl_fdecisions) {
		this.impl_fdecisions = impl_fdecisions;
	}

	public NavigableMap<String, SwitchObject> getSwitches() {
		return switches;
	}

	public void setSwitches(NavigableMap<String, SwitchObject> switches) {
		this.switches = switches;
	}

	public NavigableMap<String, Judgment> getPositives() {
		return positives;
	}

	public void setPositives(NavigableMap<String, Judgment> positives) {
		this.positives = positives;
	}

	public NavigableMap<String, Judgment> getNegatives() {
		return negatives;
	}

	public void setNegatives(NavigableMap<String, Judgment> negatives) {
		this.negatives = negatives;
	}

	public SortedSet<String> getVerbs() {
		return verbs;
	}

	public void setVerbs(SortedSet<String> verbs) {
		this.verbs = verbs;
	}

	public SortedSet<String> getAdjs() {
		return states;
	}

	public void setAdjs(SortedSet<String> adjs) {
		this.states = adjs;
	}

	public SortedSet<String> getSymbols() {
		return symbols;
	}

	public void setSymbols(SortedSet<String> symbols) {
		this.symbols = symbols;
	}

	public NavigableMap<String, CoupleEvent> getDones() {
		return dones;
	}

	public void setDones(NavigableMap<String, CoupleEvent> dones) {
		this.dones = dones;
	}

	public NavigableMap<String, CoupleEvent> getTodos() {
		return todos;
	}

	public void setTodos(NavigableMap<String, CoupleEvent> todos) {
		this.todos = todos;
	}

	public NavigableMap<String, CoupleEvent> getDoings() {
		return doings;
	}

	public void setDoings(NavigableMap<String, CoupleEvent> doings) {
		this.doings = doings;
	}

	public NavigableMap<String, Entity> getRefers() {
		return refers;
	}

	public void setRefers(NavigableMap<String, Entity> refers) {
		this.refers = refers;
	}

	public NavigableMap<String, Variable> getVars() {
		return vars;
	}

	public void setVars(NavigableMap<String, Variable> vars) {
		this.vars = vars;
	}

	public NavigableMap<String, JudgmentResult> getBools() {
		return bools;
	}
	public void setBools(NavigableMap<String, JudgmentResult> bools) {
		this.bools = bools;
	}

	public NavigableMap<String, JudgmentResult> getBoolST() {
		return boolST;
	}

	public void setBoolST(NavigableMap<String, JudgmentResult> boolST) {
		this.boolST = boolST;
	}

	public NavigableMap<String, Expression> getExpressions() {
		return expressions;
	}

	public void setExpressions(NavigableMap<String, Expression> expressions) {
		this.expressions = expressions;
	}

	public NavigableMap<String, Expression> getExpressionST() {
		return expressionST;
	}

	public void setExpressionST(NavigableMap<String, Expression> expressionST) {
		this.expressionST = expressionST;
	}

	public NavigableMap<String, Cognition> getCogs() {
		return cogs;
	}

	public void setCogs(NavigableMap<String, Cognition> cogs) {
		this.cogs = cogs;
	}

	public boolean isFunction() {
		return isFunction;
	}

	public void setFunction(boolean isFunction) {
		this.isFunction = isFunction;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public NavigableMap<String, Declaration> getCalculs() {
		return calculs;
	}

	public void setCalculs(NavigableMap<String, Declaration> calculs) {
		this.calculs = calculs;
	}

	public NavigableMap<String, Declaration> getActions() {
		return actions;
	}

	public void setActions(NavigableMap<String, Declaration> actions) {
		this.actions = actions;
	}

	public NavigableMap<String, Declaration> getDecisions() {
		return decisions;
	}

	public void setDecisions(NavigableMap<String, Declaration> decisions) {
		this.decisions = decisions;
	}

	public RuntimeLadder getLadder() {
		return ladder;
	}

	public void setLadder(RuntimeLadder ladder) {
		this.ladder = ladder;
	}

	public SessionContext getTopSessionContext(){
		for(RuntimeContext rc:getLadder().getChains()){
			if(rc instanceof SessionContext){
				return (SessionContext) rc;
			}
		}
		return LocalHost.getInstance().getRootSessionContext();
	}

	/***************************public actions Begin*********************/
	public Expression doAction(Expression expr){
		if(expr instanceof BraceExpression){
			BraceExpression prg = (BraceExpression) expr;
			return doProgramAction(prg);
		}else if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			String name = ie.getName();
			return doIdentifierAction(name);
		}else if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			String name = fe.getName();

			List<Entity> list = new ArrayList<Entity>();
			for(Expression arg:fe.getArguments()){
				Entity ent = doCalculation(arg);
				list.add(ent);
			}
			return doFunctionAction(name, list);
		}else if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			String name = me.getName();

			return doMappingAction(name, me.getArguments());
		}else if(expr instanceof ActionAtSubject){
			ActionAtSubject sv = (ActionAtSubject) expr;
			Expression result = doAction(sv.getAction());
			if(result != null){
				return result;
			}
			Entity subject = doCalculation(sv.getSubject());
			if(subject instanceof HiwiiInstance) {
				HiwiiInstance inst = (HiwiiInstance) subject;
				return doInstanceAction(inst, sv.getAction());
			}
			return doAction(subject, sv.getAction());
		}else if(expr instanceof SubjectVerbAtom){
			SubjectVerbAtom sv = (SubjectVerbAtom) expr;
			Entity subject = doCalculation(sv.getSubject());
			if(subject instanceof HiwiiInstance) {
				HiwiiInstance inst = (HiwiiInstance) subject;
				return doInstanceAction(inst, sv.getAction());
			}
			return subject.doAction(sv.getAction());        	
		}else if(expr instanceof SubjectVerb){
			SubjectVerb sv = (SubjectVerb) expr;
			Entity subject = doCalculation(sv.getSubject());
			if(subject instanceof HiwiiInstance) {
				HiwiiInstance inst = (HiwiiInstance) subject;
				return doInstanceAction(inst, sv.getAction());
			}
			return doAction(subject, sv.getAction()); 
		}else if(expr instanceof UnaryOperation){
			UnaryOperation uo = (UnaryOperation) expr;
			Expression result = unaryTranslate(uo);
			if(result == null){
				return new HiwiiException();//operator not recognized
			}
			return doAction(result);  //mapping or function
		}else if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			Expression result = binaryTranslate(bo);
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
		}else if(expr instanceof SubjectStatus){
			//statement sentence
			SubjectStatus ss = (SubjectStatus) expr;
		}
		return null;
	}

	@Override
	public Entity doCalculation(Expression expr){
		if(expr instanceof LambdaMapping){
			return expr;
		}
		if(expr instanceof ArgumentedLambda){
			return expr;
		}
		if(expr instanceof EntityLambda){
			return expr;
		}
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


		if(expr instanceof BraceExpression){
			BraceExpression prg = (BraceExpression) expr;
			Expression ret = doProgramCalculation(prg);
			if(ret instanceof ReturnResult){
				ReturnResult or = (ReturnResult) ret;
				return or.getResult();
			}else{
				return new HiwiiException();
			}
		}else if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			String name = ie.getName();
			return doIdentifierCalculation(name);
		}else if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			String name = fe.getName();
			List<Entity> list = new ArrayList<Entity>();
			for(Expression arg:fe.getArguments()){
				Entity ent = doCalculation(arg);
				if(ent instanceof HiwiiException){
					return ent;
				}
				list.add(ent);
			}
			return doFunctionCalculation(name, list);
		}else if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			String name = me.getName();
			return doMappingCalculation(name, me.getArguments());
		}else if(expr instanceof SubjectOperation){
			SubjectOperation sv = (SubjectOperation) expr;
			Entity subject = doCalculation(sv.getSubject());
			if(subject == null){
				return new HiwiiException();
			}
//			Expression arg = doInstance(sv.getAction());
//			return subject.doCalculation(arg);
			return doCalculation(subject, sv.getAction());
		}else if(expr instanceof ActionAtSubject){
			ActionAtSubject sv = (ActionAtSubject) expr;
			Entity subject = doCalculation(sv.getSubject());
			if(subject == null){
				return new HiwiiException();
			}
			return doCalculation(subject, sv.getAction());
		}else if(expr instanceof UnaryOperation){
			UnaryOperation uo = (UnaryOperation) expr;
			Expression result = unaryTranslate(uo);
			if(result == null){
				return new HiwiiException();//operator not recognized
			}
			return doCalculation(result);  //mapping or function
		}else if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			Expression result = binaryTranslate(bo);
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
		}else if(expr instanceof Parentheses){
			Parentheses pe = (Parentheses) expr;
			return pe;
		}else if(expr instanceof IdentifierBrace){
			IdentifierBrace ib = (IdentifierBrace) expr;
			return doIdentifierCalculation(ib.getName(), ib.getConditions());
		}
		return null;
	}

	@Override
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
		}else if(expr instanceof ActionAtSubject){
			//statement sentence
			SubjectStatus ss = (SubjectStatus) expr;
			Entity subject = doCalculation(ss.getSubject());
			boolean right = ss.isRight();
			Expression ret = doDecision(subject, ss.getAction());
			if(!(ret instanceof JudgmentResult)){
				return ret;
			}
			if(EntityUtil.judge(ret)){
				if(right){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else{
				if(right){
					return EntityUtil.decide(false);
				}else{
					return EntityUtil.decide(true);
				}
			}
		}else if(expr instanceof SubjectStatus){
			//statement sentence
			SubjectStatus ss = (SubjectStatus) expr;
			Entity subject = doCalculation(ss.getSubject());
			boolean right = ss.isRight();
			Expression ret = doDecision(subject, ss.getAction());
			if(!(ret instanceof JudgmentResult)){
				return ret;
			}
			if(EntityUtil.judge(ret)){
				if(right){
					return EntityUtil.decide(true);
				}else{
					return EntityUtil.decide(false);
				}
			}else{
				if(right){
					return EntityUtil.decide(false);
				}else{
					return EntityUtil.decide(true);
				}
			}
		}else if(expr instanceof UnaryOperation){
			UnaryOperation uo = (UnaryOperation) expr;
			Expression result = unaryTranslate(uo);
			if(result == null){
				return new HiwiiException();//operator not recognized
			}
			return doDecision(result);  //mapping or function
		}else if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			Expression result = binaryTranslate(bo);
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
		}else if(expr instanceof ConditionExpression){
			ConditionExpression ce = new ConditionExpression();
			Expression body = ce.getBody();
			List<Expression> cons = ce.getConditions();
			RuntimeContext context = null;
			context.doAction(body);
			if(body instanceof IdentifierExpression){
				try {
					context = makeEnvironment(cons);
					return context.doAction(body);
				} catch (ApplicationException e) {
					return new HiwiiException();
				}
			}else if(body instanceof FunctionExpression){
				FunctionExpression fe = (FunctionExpression) body;
				List<Entity> list = new ArrayList<Entity>();
				for(Expression arg:fe.getArguments()){
					Entity ent = doCalculation(arg);
					list.add(ent);
				}
				context = makeEnvironment(list, cons);
			}
			if(body instanceof SubjectVerb){
				SubjectVerb sv = (SubjectVerb) expr;
				Entity subject = doCalculation(sv.getSubject());
				Expression verb = sv.getAction();
				if(verb instanceof IdentifierExpression){
					context = makeEnvironment(subject, cons);
				}else if(verb instanceof FunctionExpression){
					FunctionExpression fe = (FunctionExpression) verb;
					List<Entity> list = new ArrayList<Entity>();
					for(Expression arg:fe.getArguments()){
						Entity ent = doCalculation(arg);
						list.add(ent);
					}
					context = makeEnvironment(subject, cons);
				}else{
					context = makeEnvironment(subject, cons);
				}
			}else{
				try {
					context = makeEnvironment(cons);
				} catch (ApplicationException e) {
					return new HiwiiException();
				}
			}
			return context.doAction(body);
		}
		return null;
	}

	public Entity getDefinition(Expression expr){
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			try {
				return EntityUtil.proxyGetDefinition(ie.getName());
			} catch (Exception e) {
				return new HiwiiException();
			}
		}
		return null;
	}
	
	public Entity calculateDefinition(Expression expr){
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			try {
				return EntityUtil.proxyGetDefinition(ie.getName());
			} catch (Exception e) {
				return new HiwiiException();
			}
		}else if(expr instanceof IdentifierBrace){
			IdentifierBrace ib = (IdentifierBrace) expr;
			Definition def = null;
			try {
				def = EntityUtil.proxyGetDefinition(ib.getName());
			} catch (Exception e) {
				return new HiwiiException();
			}
			DecoratedDefinition dd = new DecoratedDefinition();
			dd.setName(ib.getName());
			dd.setParent(def.getParent());
			dd.setSignature(def.getSignature());
			dd.setLimits(ib.getConditions());
			def = dd;
			return def;
		}
		return new HiwiiException();
	}
	
	/**
	 * doCalculation
	 * doDecision
	 * doExecution
	 * doLambda
	 * 是系统基本动作。
	 * entity.toExpression是返回方法。
	 * 以上构成五行
	 * program/mapping在传递给对象前，必须经过本地处理。
	 * @param source
	 * @return
	 */
	public Expression doInstance(Expression expr){
		if(expr instanceof IdentifierExpression){
			return expr;
		}
		if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			List<Entity> list = new ArrayList<Entity>();
			for(Expression arg:fe.getArguments()){
				Entity ent = doCalculation(arg);
				if(ent instanceof HiwiiException){
					return (HiwiiException)ent;
				}
				list.add(ent);
			}
			FunctionEntity ret = new FunctionEntity();
			ret.setName(fe.getName());
			ret.setArguments(list);
			return ret;
		}
		if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			List<Expression> args = expressionMapping(me.getArguments());
			MappingExpression ret = new MappingExpression();
			ret.setName(me.getName());
			ret.setArguments(args);
			return ret;
		}
		if(expr instanceof BraceExpression){
			BraceExpression me = (BraceExpression) expr;
			List<Expression> args = expressionMapping(me.getArray());
			BraceExpression ret = new BraceExpression();
			ret.setArray(args);
			return ret;
		}
		return expr;
	}
	
	public Entity doProcess(Entity subject, Expression expr){
		Entity result = null;
		if(expr instanceof BraceExpression){			
			BraceExpression prg = (BraceExpression) expr;
			result = doProgramProcess(subject, prg);
			if(result instanceof ReturnResult){
				ReturnResult ret = (ReturnResult) result;
				return ret.getResult();
			}
		}else{
			result = doProcessItem(subject, expr);
		}
		if(result instanceof ReturnResult) {
			ReturnResult ret = (ReturnResult) result;
			return ret.getResult();
		}
		if(result instanceof HiwiiException) {
			return result;
		}
		return new HiwiiException();
	}
	
	public Entity doProcessItem(Entity subject, Expression expr){		
		if(expr instanceof BraceExpression){
			Entity result = null;
			
			BraceExpression prg = (BraceExpression) expr;
			result = doProgramProcess(subject, prg);
			if(result instanceof ReturnResult){
				ReturnResult ret = (ReturnResult) result;
				return ret.getResult();
			}
		}
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			String name = ie.getName();
			if(name.equals("skip")){
				return new SkipReturn();
			}else if(name.equals("break")){
				return new BreakReturn();
			}else if(name.equals("return")){
				return new ReturnEnd();
			}
		}
		if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			List<Expression> args = expressionMapping(me.getArguments());
			if(me.getName().equals("return")) {
				if(args.size() != 1) {
					return new HiwiiException();
				}
				Entity ret = doCalculation(subject, args.get(0));
				if(ret instanceof HiwiiException || ret == null) {
					return ret;
				}else {
					ReturnResult result = new ReturnResult();
					result.setResult(ret);
					return result;
				}
			}
		}

		return doAction(subject, expr);
	}
	/**
	 * mapping传递给对象前，进行最后的处理
	 * @param name
	 * @param args
	 * @return
	 */
	public List<Expression> expressionMapping(List<Expression> args){
		List<Expression> ret = new ArrayList<Expression>();
		for(Expression expr:args){
			Expression arg = doInstance(expr);
			ret.add(arg);
		}
		return ret;
	}
	/**
	 * 只有两种形式参数，小括号形式参数表示entity参数
	 * 中括号参数表示表达式参数。
	 * 没有括号的其它表达式表示一个参数的小括号entity。
	 */
	@Override
	public Expression doLambda(Expression source, Expression arg) {
		if(arg instanceof Parentheses){
			Parentheses par = (Parentheses) arg;
			List<Entity> ents = new ArrayList<Entity>();
			for(Expression expr:par.getArray()){
				Entity ent = doCalculation(expr);
				ents.add(ent);
			}
			return doLambdaEntity(source, ents);
		}else if(arg instanceof BracketExpression){
			BracketExpression be = (BracketExpression) arg;
		}else{
			Entity ent = doCalculation(arg);
			List<Entity> ents = Arrays.asList(ent);
			return doLambdaEntity(source, ents);
		}
		return null;
	}

	public Expression doLambdaEntity(Expression source, List<Entity> entities){
		return null;
	}
	
	public Expression doLambdaExpression(Expression source, List<Expression> entities){
		return null;
	}
	
	/**
	 * entity#doAction
	 * 1，entity本身动作。
	 * 2，entity defined的动作
	 * 3、entity' definition defined的动作。
	 * 4、系统defined的动作。
	 * 系统为instance和definition定义了一些动作。比如：define property。
	 * 因此如果subject是instance或definition需要单独处理。
	 * @param subject
	 * @param expr
	 * @return
	 */
	public Expression doAction(Entity subject, Expression expr){
//		if(subject instanceof HostObject){
//
//		}else if(subject instanceof LambdaExpression){
//			//do Lambda entity action
//		}else{
//
//		}
		
//		if(subject instanceof Definition){
//			Definition def = (Definition) subject;
//			return doDefinitionAction(def, expr);
//		}
		
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			return doIdentifierAction(subject, ie.getName());
		}else if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			List<Entity> list = new ArrayList<Entity>();
			for(Expression arg:fe.getArguments()){
				Entity ent = doCalculation(arg);
				list.add(ent);
			}
			return doFunctionAction(subject, fe.getName(), list);
		}else if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			String name = me.getName();
//			if(me.getName().equals("define")) {
//				
//			}
			return doMappingAction(subject, name, me.getArguments());
		}else if(expr instanceof BraceExpression){
			BraceExpression prg = (BraceExpression) expr;
			Expression ret = doProgramAction(subject, prg);
			if(ret instanceof SkipReturn) {
				return new NormalEnd();
			}
			return ret;
		}else if(expr instanceof SubjectVerb){
			SubjectVerb sv = (SubjectVerb) expr;
			Entity target = doCalculation(subject, sv.getSubject());
			return doAction(target, sv.getAction());
		}else if(expr instanceof UnaryOperation){
			UnaryOperation uo = (UnaryOperation) expr;
			Expression result = unaryTranslate(uo);
			if(result == null){
				return new HiwiiException();//operator not recognized
			}
			return doAction(subject, result);  //mapping or function
		}else if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			Expression result = binaryTranslate(bo);
			if(result == null){
				return new HiwiiException();//operator not recognized
			}
			return doAction(subject, result);  //mapping or function
		}

		return null;
	}

	public Entity doCalculation(Entity subject, Expression expr){
//		if(subject instanceof LambdaExpression){
//			LambdaExpression le = (LambdaExpression) subject;
//			if(expr instanceof IdentifierExpression){
//				IdentifierExpression ie = (IdentifierExpression) expr;
//				if(ie.getName().equals("doCalculation")){
//					return doLambdaCalculation(le);
//				}
//			}
//			if(expr instanceof FunctionExpression){
//				FunctionExpression fe = (FunctionExpression) expr;
//				if(fe.getName().equals("doCalculation")){
//					List<Entity> list = new ArrayList<Entity>();
//					for(Expression arg:fe.getArguments()){
//						Entity ent = doCalculation(arg);
//						list.add(ent);
//					}
//					return doLambdaFunctionCalculation(le, list);
//				}
//			}
//			if(expr instanceof MappingExpression){
//				MappingExpression me = (MappingExpression) expr;
//				if(me.getName().equals("doCalculation")){					
//					return doLambdaMappingCalculation(le, me.getArguments());
//				}
//			}
//			return new HiwiiException();
//		}
		if(subject instanceof HostObject){
			HostObject ho = (HostObject) subject;
			String ret = ho.doRemoteCalculation(expr);
			Entity ent = doCalculation(StringUtil.parseString(ret));
			return ent;
		}
		if(subject instanceof Definition){
			Definition def = (Definition)subject;
			return doDefinitionCalculation(def, expr);
		}
//		if(subject instanceof HiwiiInstance){
//			HiwiiInstance inst = (HiwiiInstance) subject;
//			if(expr instanceof IdentifierExpression){
//				IdentifierExpression ie = (IdentifierExpression) expr;
//				return EntityUtil.doInstanceIdentifierCalculation(inst, ie.getName(), this);
//			}			
//		}
		if(expr instanceof BraceExpression){
			BraceExpression prg = (BraceExpression) expr;
			return doProgramCalculation(subject, prg);
		} 
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			String name = ie.getName();
			Entity ret = doIdentifierCalculation(subject, name);
			if(ret != null) {
				return ret;
			}else {
				return doIdentifierCalculation(name);
			}
		}else if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			String name = fe.getName();
			List<Entity> list = new ArrayList<Entity>();
			for(Expression arg:fe.getArguments()){
				Entity ent = doCalculation(subject, arg);
				list.add(ent);
			}
			return doFunctionCalculation(subject, name, list);
		}else if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			String name = me.getName();
			return doMappingCalculation(subject, name, me.getArguments());
		}else if(expr instanceof SubjectVerb){
			SubjectVerb sv = (SubjectVerb) expr;
			Entity subj = doCalculation(subject, sv.getSubject());
			return doCalculation(subj, sv.getAction());
		}else if(expr instanceof UnaryOperation){
			UnaryOperation uo = (UnaryOperation) expr;
			Expression result = unaryTranslate(uo);
			if(result == null){
				return new HiwiiException();//operator not recognized
			}
			return doCalculation(subject, result);  //mapping or function
		}else if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			Expression result = binaryTranslate(bo);
			if(result == null){
				return new HiwiiException();//operator not recognized
			}
			return doCalculation(subject, result);  //mapping or function
		}else if(expr instanceof BracketExpression){
			//            BracketExpression be = (BracketExpression) expr;
			//            for(Expression comm:be.getArray()){
			//                Expression ret = doContextAction(comm, adverbs);
			//                if(ret instanceof HiwiiException){
			//                    return ret;
			//                }
			//            }
			return new NormalEnd();
		}else if(expr instanceof ConditionExpression){
			ConditionExpression ce = new ConditionExpression();
			Expression body = ce.getBody();
			List<Expression> cons = ce.getConditions();
			RuntimeContext context = null;
			context.doAction(body);
			if(body instanceof IdentifierExpression){
				try {
					context = makeEnvironment(cons);
					return context.doAction(body);
				} catch (ApplicationException e) {
					return new HiwiiException();
				}
			}else if(body instanceof FunctionExpression){
				FunctionExpression fe = (FunctionExpression) body;
				List<Entity> list = new ArrayList<Entity>();
				for(Expression arg:fe.getArguments()){
					Entity ent = doCalculation(arg);
					list.add(ent);
				}
				context = makeEnvironment(list, cons);
			}
			if(body instanceof SubjectVerb){
				SubjectVerb sv = (SubjectVerb) expr;
				Entity subj = doCalculation(subject, sv.getSubject());
				Expression verb = sv.getAction();
				if(verb instanceof IdentifierExpression){
					context = makeEnvironment(subject, cons);
				}else if(verb instanceof FunctionExpression){
					FunctionExpression fe = (FunctionExpression) verb;
					List<Entity> list = new ArrayList<Entity>();
					for(Expression arg:fe.getArguments()){
						Entity ent = doCalculation(arg);
						list.add(ent);
					}
					context = makeEnvironment(subject, cons);
				}else{
					context = makeEnvironment(subject, cons);
				}
			}else{
				try {
					context = makeEnvironment(cons);
				} catch (ApplicationException e) {
					return new HiwiiException();
				}
			}
			return context.doAction(body);
		}
//		return null; 
		return doCalculation(expr);
	}

	public Expression doDecision(Entity subject, Expression expr){
		if(expr instanceof JudgmentResult){
			return expr;
		}
		if(expr instanceof BraceExpression){
			BraceExpression prg = (BraceExpression) expr;
			return doProgramAction(prg);
		}else if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			String name = ie.getName();
			return doIdentifierDecision(subject, name);
//			Expression ret = subject.doIdentifierDecision(name);
//			if(ret != null){
//				return ret;
//			}
//			return doIdentifierDecision(name);
		}else if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			String name = fe.getName();
			List<Entity> list = new ArrayList<Entity>();
			for(Expression arg:fe.getArguments()){
				Entity ent = doCalculation(subject, arg);
				list.add(ent);
			}
			Expression ret = subject.doFunctionDecision(name, list);
			if(ret != null){
				return ret;
			}
			return doFunctionDecision(name, list);
		}else if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			String name = me.getName();
			return subject.doMappingDecision(name, me.getArguments());
		}else if(expr instanceof SubjectVerb){
			SubjectVerb sv = (SubjectVerb) expr;
			Entity sub = doCalculation(subject, sv.getSubject());
			return doDecision(sub, sv.getAction());
		}else if(expr instanceof UnaryOperation){
			UnaryOperation uo = (UnaryOperation) expr;
			Expression result = unaryTranslate(uo);
			if(result == null){
				return new HiwiiException();//operator not recognized
			}
			return doDecision(result);  //mapping or function
		}else if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			Expression result = binaryTranslate(bo);
			if(result == null){
				return new HiwiiException();//operator not recognized
			}
			return doDecision(subject, result);  //mapping or function
		}else if(expr instanceof BracketExpression){
			//            BracketExpression be = (BracketExpression) expr;
			//            for(Expression comm:be.getArray()){
			//                Expression ret = doContextAction(comm, adverbs);
			//                if(ret instanceof HiwiiException){
			//                    return ret;
			//                }
			//            }
			return new NormalEnd();
		}else if(expr instanceof ConditionExpression){
			ConditionExpression ce = new ConditionExpression();
			Expression body = ce.getBody();
			List<Expression> cons = ce.getConditions();
			RuntimeContext context = null;
			context.doAction(body);
			if(body instanceof IdentifierExpression){
				try {
					context = makeEnvironment(cons);
					return context.doAction(body);
				} catch (ApplicationException e) {
					return new HiwiiException();
				}
			}else if(body instanceof FunctionExpression){
				FunctionExpression fe = (FunctionExpression) body;
				List<Entity> list = new ArrayList<Entity>();
				for(Expression arg:fe.getArguments()){
					Entity ent = doCalculation(arg);
					list.add(ent);
				}
				context = makeEnvironment(list, cons);
			}
			if(body instanceof SubjectVerb){
				SubjectVerb sv = (SubjectVerb) expr;
				Entity sub = doCalculation(sv.getSubject());
				Expression verb = sv.getAction();
				if(verb instanceof IdentifierExpression){
					context = makeEnvironment(subject, cons);
				}else if(verb instanceof FunctionExpression){
					FunctionExpression fe = (FunctionExpression) verb;
					List<Entity> list = new ArrayList<Entity>();
					for(Expression arg:fe.getArguments()){
						Entity ent = doCalculation(arg);
						list.add(ent);
					}
					context = makeEnvironment(subject, cons);
				}else{
					context = makeEnvironment(subject, cons);
				}
			}else{
				try {
					context = makeEnvironment(cons);
				} catch (ApplicationException e) {
					return new HiwiiException();
				}
			}
			return context.doAction(body);
		}
		return null;
	}

	public Expression doIdentifierAction(String name){
		Expression ret = null;
		ret = proxyIdentifierAction(name);
		if(ret != null){
			return ret;
		}
		try {
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			String str = db.getIdAction(name, null);
			if(str != null){
				Expression expr = StringUtil.parseString(str);
				RuntimeContext rc = getLadder().newRuntimeContext('c');
				rc.setFunction(true);
				return rc.doAction(expr);
			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			e.printStackTrace();
			return new HiwiiException();
		}
		return null;
	}

	public Expression doFunctionAction(String name, List<Entity> args){
		if(name.equals("return")){
			if(args.size() != 1){
				return new HiwiiException();
			}			
			ReturnResult rr = new ReturnResult();
			rr.setResult(args.get(0));
			return rr;
		}
		if(name.equals("connect")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof HostObject)){
				return new HiwiiException();
			}
			HostObject host = (HostObject) args.get(0);
			ClientSessionContext rsc = new ClientSessionContext();
			Session se = getLadder().getSessionContext().getSession();
			rsc.setSession(se);
			rsc.setHost(host);
			se.pushContext(rsc);
			return new NormalEnd();
		}
		if(name.equals("echo")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof StringExpression)){
				return new HiwiiException();
			}
			StringExpression se = (StringExpression) args.get(0);
//			LocalHost.getInstance().getConsole().doResponse(se.getValue());
			SessionContext sc = getLadder().getSessionContext();
			sc.getSession().doResponse(se.getValue());
			return new NormalEnd();
		}
		if(name.equals("question")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			SessionContext sc = getLadder().getSessionContext();
			sc.getSession().getTerminal().doQuestion("test question");
			return new NormalEnd();
		}
		if(name.equals("register")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			Expression res = register(args.get(0), args.get(1));
			return res;//res is Exception or JudgmentResult
		}
		
		if(name.equals("exclude")) {
			if(args.size() != 1){
				return new HiwiiException();
			}
			User user = getLadder().getSessionContext().getSession().getUser();
			if(user == null) {
				return new HiwiiException("not logined");
			}
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			try {
				db.openSpace();
				LocalHost.getInstance().setOpenSpace(true);
				return new NormalEnd();
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				return new HiwiiException();
			}
		}
		
		if(name.equals("include")) {
			if(args.size() != 1){
				return new HiwiiException();
			}
			User user = getLadder().getSessionContext().getSession().getUser();
			if(user == null) {
				return new HiwiiException("not logined");
			}
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			try {
				db.openSpace();
				LocalHost.getInstance().setOpenSpace(true);
				return new NormalEnd();
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				return new HiwiiException();
			}
		}
		
		try {
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			FunctionDeclaration fd = db.getFunctionAction(name, args, null, this);
			if(fd != null){
				RuntimeContext rc = getLadder().newRuntimeContext('a');
				int i = 0;
//				for(Argument arg:fd.getArguments()){
//					rc.getRefers().put(arg.getName(), args.get(i));
//					i++;
//				}
				Expression ret = rc.doAction(fd.getStatement());
				rc = null;  //释放内存
				return ret;
			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			e.printStackTrace();
			return new HiwiiException();
		}
		return null;
	}

	public Expression doMappingAction(String name, List<Expression> args){
		Expression ret = proxyMappingAction(name, args);
		if(ret != null){
			return ret;
		}
		return null;
	}

	public Expression doMappingDecision(String name, List<Expression> args){
		if(name.equals("negate")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Expression ret = doDecision(args.get(0));
			if(!(ret instanceof JudgmentResult)){
				return ret;
			}
			JudgmentResult jr = (JudgmentResult) ret;
			jr.negate();
			return jr;
		}
		if(name.equals("locked")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			return locked(args.get(0));
		}
		if(name.equals("decide")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			return doDecision(args.get(0));
		}
		if(name.equals("defined")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			return hasDefined(args.get(0));
		}
		if(name.equals("is")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			return belongToDecision(args.get(0), args.get(1));
		}
		if(name.equals("turn")){
			if(args.size() != 2){
				return new HiwiiException();
			}
//			return belongToDecision(args.get(0), args.get(1));
		}
		
		return null;
	}

	public Expression doMappingDecision(Entity subject, String name, List<Expression> args){
		return subject.doMappingDecision(name, args);
	}
	
	/**
	 * 1、确定是否innerAction.
	 * 2、是否一般Action
	 */
	public Expression doIdentifierAction(Entity subject, String name){
		Expression ret = subject.doIdentifierAction(name);
		if(ret != null) {
			return ret;
		}
		if(subject instanceof Definition){
//			Definition def = (Definition) subject;
//			return doDefinitionIdentifierAction(def, name); //i.e. closeSpace
		}
		
		try {
			String key = null;
			if(subject instanceof HiwiiInstance) {
				HiwiiInstance inst = (HiwiiInstance) subject;
				key = name + "@" + inst.getUuid();
			}
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			String str = db.getIdProcess(key, null);
			Expression expr0 = new StringExpression(str).toExpression();
//			return doProcess(subject, expr0);
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			e.printStackTrace();
			return new HiwiiException();
		}
		
		return null;
//		return subject.doIdentifierAction(name);
	}

	public Expression doFunctionAction(Entity subject, String name, List<Entity> args){
		if(subject instanceof Definition){
			return new HiwiiException();
		}
		try {
			Definition def = EntityUtil.proxyGetDefinition(subject.getClassName());
			if(def == null){
				return new HiwiiException();
			}
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			FunctionDeclaration fd = db.getFunctionAction(def, name, args, null);
			if(fd != null){
				RuntimeContext rc = getLadder().newRuntimeContext('c');
				int i = 0;
//				for(Argument arg:fd.getArguments()){
//					rc.getRefers().put(arg.getName(), args.get(i));
//				}
				return rc.doAction(subject, fd.getStatement());
			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			e.printStackTrace();
			return new HiwiiException();
		}
		return subject.doFunctionAction(name, args);
		//    	return null;
	}

	/**
	 * 调用方式program@subject
	 * @param subject
	 * @param name
	 * @param args
	 * @return
	 */
	public Expression doMappingAction(Entity subject, String name, List<Expression> args){
		if(subject instanceof Definition){
			Definition inst = (Definition) subject;
			Expression ret = doDefinitionMappingAction(inst, name, args);
			if(ret != null) {
				return ret; 
			}
			return inst.doMappingAction(name, args);
		}else if(subject instanceof HiwiiInstance){
			HiwiiInstance inst = (HiwiiInstance) subject;
			return doInstanceMappingAction(inst, name, args);
		}else if(subject instanceof LambdaMapping){
			LambdaMapping le = (LambdaMapping) subject;
			RuntimeContext rc = getLadder().newRuntimeContext('c');
			if(args.size() != le.getKeys().size()){
				return new HiwiiException();
			}
			int i = 0;
			for(Expression arg:args){
				if(arg instanceof LambdaMapping){
					
					rc.getLactions().put(le.getKeys().get(i), (LambdaMapping)args.get(i));
				}else{
					rc.getMactions().put(le.getKeys().get(i), args.get(i));
				}
				i++;
			}
		}

		if(name.equals("assign")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			Entity value = doCalculation(subject, args.get(1));
			if(value instanceof HiwiiException){
				return (Expression) value;
			}
			return subject.doAssign(args.get(0), value);
		}

		if(name.equals("turn")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			Expression value = doDecision(subject, args.get(1));
			if(value instanceof HiwiiException){
				return value;
			}
			JudgmentResult res = (JudgmentResult) value;
			return subject.doJudge(args.get(0), res);
		}
		if(name.equals("IF")){
			//if和IF是不同的，if用于对象外部程序，先判断后执行，IF用于对象本身，判断和执行同时进行
			//根据关键字不同，直接送对象执行。
			return subject.doIfAction(args);
		}
		if(name.equals("put")){
			
		}
		if(name.equals("update")){
//			subject.doUpdate(args);
		}
		List<Expression> exps = expressionMapping(args);
//		MappingExpression ret = new MappingExpression();
//		ret.setName(name);
//		ret.setArguments(exps);
		return subject.doMappingAction(name, exps); 
		
//		return null;
	}

	public Expression doMappingCalculation(Entity subject, String name, List<Expression> args){
		if(name.equals("return")) {
			if(args.size() != 1) {
				return new HiwiiException();
			}
		}else {
			List<Expression> exps = expressionMapping(args);
			MappingExpression ret = new MappingExpression();
			ret.setName(name);
			ret.setArguments(exps);
		}
		return null;
	}
	
	/**
	 * 1,内置计算
	 * 2，context计算
	 * 3，记忆计算
	 */
	public Entity doIdentifierCalculation(String name){
		Entity ret = null;

		ret = proxyIdentifierCalculation(name);
		if(ret != null){
			return ret;
		}		

		try {
			Definition def = EntityUtil.proxyGetDefinition(name);
			if(def != null){
				return def;
			}
		} catch (Exception e) {
			return new HiwiiException();
		}
		
		for(RuntimeContext context:this.getLadder().getChains()){
			Variable var = context.contextHasVariable(name);
			if(var != null){
				return var.getValue();
			}
			if(context.getSymbols().contains(name)){
				return new Symbol(name);
			}
			if(context.getRefers().containsKey(name)){
				return context.getRefers().get(name);
			}
			if(context.getExpressions().containsKey(name)){
				return context.getExpressions().get(name);
			}
			if(context.getExpressionST().containsKey(name)){
				return context.getExpressionST().get(name);
			}
			if(context.isFunction()){
				break;
			}
		}

		try {
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			
			Entity target = db.getReference(name, null);
			if(target != null){
				return target;
			}
			
			HiwiiInstance inst = db.getInstanceByName(name);
			if(inst != null){
				return inst;
			}
			String str = db.getIdCalculation(name, null);
			if(str != null){
				Expression expr = StringUtil.parseString(str);
				RuntimeContext rc = getLadder().newRuntimeContext('c');
				rc.setFunction(true);
				return rc.doCalculation(expr);
			}
						
			VariableStore vs = db.getVariable(name, null);
			if(vs != null){
				if(vs.getValueType() == 'i'){
					HiwiiInstance ent = db.getInstanceById(vs.getValue());
					return ent;
				}else{
					//valueType = 's'
					Expression expr = new StringExpression(vs.getValue()).toExpression();
					Entity ent = doCalculation(expr);
					return ent;
				}
			}
		} catch (Exception e) {
			return new HiwiiException();
		}
		
		return null;
	}

	public Entity doIdentifierCalculation(Entity subject, String name){	
		Entity ret = subject.doIdentifierCalculation(name);
		if(ret != null) {
			return ret;
		}
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			String str = db.getEntityIdCalculation(subject, name, null);
			if(str != null){
				Expression expr = StringUtil.parseString(str);
				RuntimeContext rc = getLadder().newRuntimeContext('c');
				rc.setFunction(true);  //?
				return rc.doCalculation(subject, expr);
			}
			
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}		
		
		return null;
	}

	/**
	 * definition{limits...}
	 * @param name
	 * @param limits
	 * @return
	 */
	public Entity doIdentifierCalculation(String name, List<Expression> limits){
		try {
			Definition def = EntityUtil.proxyGetDefinition(name);
			if(def == null){
				return new HiwiiException();
			}
			DecoratedDefinition dd = new DecoratedDefinition();
			dd.setName(name);
			dd.setParent(def.getParent());
			dd.setSignature(def.getSignature());
			dd.setLimits(limits);
			return dd;
		} catch (Exception e) {
			return new HiwiiException();
		}
	}

	public Entity doFunctionCalculation(String name, List<Entity> args){
		Entity ret = null;

		ret = proxyFunctionCalculation(name, args);
		if(ret != null){
			return ret;
		}

		try {
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			FunctionDeclaration fd = db.getFunctionCalculation(name, args, null);
			if(fd != null){
				RuntimeContext rc = getLadder().newRuntimeContext('c');
				int i = 0;
				for(String vname:fd.getArguments()){
					rc.getRefers().put(vname, args.get(i));
					i++;
				}
				ret = rc.doCalculation(fd.getStatement());
				rc = null;  //释放内存
				return ret;
			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			e.printStackTrace();
			return new HiwiiException();
		}
		return null;
	}
	
	public Entity doFunctionCalculation(LambdaMapping expr, List<Entity> args){
		return null;
	}
	
	public Entity doFunctionCalculation(Entity subject, String name, List<Entity> args){
		try {
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			
			String type = subject.getClassName();
			Definition def = EntityUtil.proxyGetDefinition(type);
			FunctionDeclaration fd = db.getFunctionCalculation(def, name, args, null);
			if(fd != null){
				RuntimeContext rc = getLadder().newRuntimeContext('c');
				int i = 0;
				for(String arg:fd.getArguments()){
					rc.getRefers().put(arg, args.get(i));
				}
				return rc.doCalculation(subject, fd.getStatement());
			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			e.printStackTrace();
			return new HiwiiException();
		}
		
		Entity ret = subject.doFunctionCalculation(name, args);
		if(ret != null) {
			return ret;
		}
		return doFunctionCalculation(name, args);
	}

	public Expression doFunctionDecision(String name, List<Entity> args){
		Expression ret = null;

		ret = proxyFunctionDecision(name, args);
		if(ret != null){
			return ret;
		}

		try {
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			FunctionDeclaration fd = db.getFunctionDecision(name, args, null);
			if(fd != null){
				RuntimeContext rc = getLadder().newRuntimeContext('c');
				int i = 0;
//				for(Argument arg:fd.getArguments()){
//					rc.getRefers().put(arg.getName(), args.get(i));
//				}
				return rc.doDecision(fd.getStatement());
			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			e.printStackTrace();
			return new HiwiiException();
		}
		return null;
	}

	public Entity doMappingCalculation(String name, List<Expression> args){
		if(name.equals("one")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			return doGetSingleAction(args.get(0));
		}
		if(name.equals("all")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			return doGetSingleAction(args.get(0));
		}
		if(name.equals("new")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			return newInstance(args.get(0));
		}
		if(name.equals("doCalculation")){
			if(args.size() == 1){
				Entity result = doCalculation(args.get(0));
				if(!(result instanceof Expression)) {
					return new HiwiiException();
				}
				Expression arg = (Expression) result;
				return doCalculation(arg);
			}
		}
		if(name.equals("last")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Session sess = getLadder().getSessionContext().getSession();
			if(!(args.get(0) instanceof IdentifierExpression)) {
				return new HiwiiException();
			}
			IdentifierExpression ie = (IdentifierExpression) args.get(0);
			
			try {
				Definition def = EntityUtil.proxyGetDefinition(ie.getName());
				if(def == null) {
					return new HiwiiException();
				}
				return sess.getLastEntity(def.getSignature());
			} catch (Exception e) {
				return new HiwiiException();
			}
		}
		if(name.equals("create") || name.equals("put")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Expression ret = putInstance(args.get(0));
			if(ret instanceof ReturnResult) {
				ReturnResult rr = (ReturnResult) ret;
				return rr.getResult();
			}
			return ret;
		}
		if(name.equals("symbol")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof IdentifierExpression)){
				return new HiwiiException();
			}
			IdentifierExpression ie = (IdentifierExpression) args.get(0);
			return new Symbol(ie.getName());
		}
		if(name.equals("lambda")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof IdentifierExpression)){
				return new HiwiiException();
			}
			IdentifierExpression ie = (IdentifierExpression) args.get(0);
			return new Symbol(ie.getName());
		}
		if(name.equals("doLambda")){
			if(args.size() == 2){
				LambdaMapping le = null;
				if(args.get(0) instanceof BinaryOperation){
					BinaryOperation bo = (BinaryOperation) args.get(0);
					Expression exp0 = binaryTranslate(bo);
					if(!(exp0 instanceof LambdaMapping)){
						return new HiwiiException();
					}
					le = (LambdaMapping) exp0;
				}else if(args.get(0) instanceof IdentifierExpression){
					IdentifierExpression ie = (IdentifierExpression) args.get(0);
					Expression exp = hasExpression(ie.getName());
					if(exp == null){
						return new HiwiiException();
					}
					if(!(exp instanceof LambdaMapping)){
						return new HiwiiException();
					}
					le = (LambdaMapping) exp;
				}else{
					return new HiwiiException();
				}
				
				List<Entity> list = new ArrayList<Entity>();
				if(args.get(1) instanceof Parentheses){
					Parentheses p = (Parentheses) args.get(1);
					for(Expression e:p.getArray()){
						Entity ent = doCalculation(e);
						if(ent instanceof HiwiiException){
							return ent;
						}
						list.add(ent);
					}
				}else{
					Entity ent = doCalculation(args.get(1));
					list = Arrays.asList(ent);
				}
				return doLambdaFunctionCalculation(le, list);
			}else{
				return new HiwiiException();
			}
		}
		return null;//proxyMappingCalculation(name, args);
	}

	public Expression doIdentifierDecision(String name){
		if(name.equals("true")) {
			return EntityUtil.decide(true);
		}
		if(name.equals("false")) {
			return EntityUtil.decide(false);
		}
		Expression ret = null;

		ret = proxyIdentifierDecision(name);
		if(ret != null){
			return ret;
		}

		for(RuntimeContext context:getLadder().getChains()){
			if(context.getBools().containsKey(name)){
				JudgmentResult var = context.getBools().get(name);
				if(var == null){
					return new NullValue();
				}
				return var;
			}
//			ret = context.doContextIdentifierDecision(name);
//			if(ret != null){
//				return ret;
//			}
		}


		return null;
	}
	
	public Expression doIdentifierDecision(Entity subject, String name){
		if(name.equals("true")) {
			return EntityUtil.decide(true);
		}
		if(name.equals("false")) {
			return EntityUtil.decide(false);
		}
		if(subject instanceof Definition){
			
		}else if(subject instanceof HiwiiInstance){
			HiwiiInstance inst = (HiwiiInstance) subject;
			
			
			return doInstanceIdentifierDecision(inst, name);
		}else{
			return subject.doIdentifierDecision(name);
		}
		return null;
	}

	/**
	 * Calculation
	 * [type:表达式]返回type下表达式表示的对象，如：[Group:department1]
	 * @param type
	 * @param expr
	 * @return
	 */
	public Entity doTypeCalculation(Expression type, Expression expr){
		Definition def = null;
		Entity ret = getDefinition(type);
		if(ret == null){
			return new HiwiiException();
		}
		if(ret instanceof HiwiiException){
			return ret;
		}if(ret instanceof Definition){
			def = (Definition) ret;
		}
		//    	if(def instanceof SimpleDefinition){
		//    		//return message that is definition
		//    	}
		try {
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
//			List<HiwiiInstance> items = db.getMultiInstance(def.getName(), null);
//			if(items.size() == 0){
//				return new NullValue();
//			}else if(items.size() == 1){
//				return items.get(0);
//			}else{
//				EntityList list = new EntityList();
//				for(HiwiiInstance item:items){
//					list.add(item);
//				}
//				return list;
//			}
			return null;
		} catch (Exception e) {
			return new HiwiiException();
		}
		//    	return null;
	}
	public Variable hasVariable(String name){
		Variable var = null;
		for(RuntimeContext context:this.getLadder().getChains()){
			var = context.contextHasVariable(name);
			if(var != null){
				return var;
			}
		}
		return null;
	}

	public JudgmentResult hasBoolean(String name){
		for(RuntimeContext context:this.getLadder().getChains()){
			if(context.getBools().containsKey(name)){
				return context.getBools().get(name);
			}
		}
		return null;
	}
	public Property hasProperty(String name){
		Property prop = getLadder().proxyGetProperty(name);
		if(prop != null){
			return prop;
		}
		for(RuntimeContext context:this.getLadder().getChains()){
			prop = context.contextHasProperty(name);
			if(prop != null){
				return prop;
			}
		}		
		return null;
	}
	
	public Expression hasExpression(String name){
//		LambdaExpression le = null;
		for(RuntimeContext context:this.getLadder().getChains()){
			if(context.getExpressions().containsKey(name)){
				return context.getExpressions().get(name);
			}
		}
		return null;
	}

	public Property hasProperty(HiwiiInstance target, String name){
		try {
			Definition def = EntityUtil.proxyGetDefinition(target.getClassName());
			if(def != null){
				return null;
				//hasDefinitionProperty(def, name)
			}
			Property prop = getLadder().proxyGetProperty(name);
			if(prop != null){
				return prop;
			}	
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
			return null;
		}
	}

	public Property hasDefinitionProperty(Definition def, String name){
		if(def.getProps().containsKey(name)){
			return def.getProps().get(name);
		}else if(def.getParent() != null){
			try {
				Definition parent = EntityUtil.proxyGetInstanceDefinition(def.getParent());
				if(parent != null){
					return hasDefinitionProperty(parent, name);
				}else{
					return null;
				}
			} catch (Exception e) {
				//TODO exception
				return null;
			}
		}
		return null;
	}

	public Definition hasDefinition(String name){
		Definition def = getLadder().proxyGetDefinition(name);
		if(def != null){
			return def;
		}
		for(RuntimeContext context:this.getLadder().getChains()){
			def = context.contextHasDefinition(name);
			if(def != null){
				return def;
			}
		}

		return null;
	}

	public Definition hasDefinition(Definition def, String name){
		if(def != null){
			return def;
		}
		for(RuntimeContext context:this.getLadder().getChains()){
			def = context.contextHasDefinition(name);
			if(def != null){
				return def;
			}
		}

		return null;
	}

	/**
	 * name is a definition
	 * @param name
	 * @return
	 */
	public Entity hasEntityList(String name){
		Definition def = getLadder().proxyGetDefinition(name);
		//TODO get persisted entity list 

		for(RuntimeContext context:this.getLadder().getChains()){
			if(def == null){
				context.contextHasDefinition(name);
			}
			if(def != null){
				String key = def.takeSignature();
				Entity ent = context.contextHasEntity(key);
				if(ent != null){
					return ent;
				}
			}
		}

		return def;
	}

	/**************************doContext begin*******************/
	public Entity doContextIdentifierCalculation(String name){
		if(refers.containsKey(name)){
			return refers.get(name);
		}
		Definition def = contextHasDefinition(name);
		if(def != null){
			return def;
		}
		Assignment ass = contextHasAssignment(name);
		if(ass != null){
			return ass.getValue();
		}
		Property prop = contextHasProperty(name);
		if(prop != null){
			return new NullValue();
		}

		Variable var = contextHasVariable(name);
		if(var != null){
			return var.getValue();
		}
		Expression expr = contextHasCalculation(name);
		if(expr != null){
			return doCalculation(expr);
		}

		//		if(symbols.)
		return null;
	}

	public Entity doContextIdentifierCalculation(Entity subject, String name){

		Property prop = contextHasProperty(subject, name);
		if(prop != null){
			return new NullValue();
		}

		return null;
	}

	public Definition contextHasDefinition(String name){
		if(defines.containsKey(name)){
			return defines.get(name);
		}
		return null;
	}

	public Entity contextHasEntity(String key){
		String floor = entities.floorKey(key);
		if(floor != null  && StringUtil.matched(key, floor)){
			return entities.get(floor);
		}
		return null;
	}

	public Assignment contextHasAssignment(String name){
		if(assigns.containsKey(name)){
			return assigns.get(name);
		}
		return null;
	}
	public Property contextHasProperty(String name){
		if(props.containsKey(name)){
			return props.get(name);
		}
		return null;
	}

	public Property contextHasProperty(Entity subject, String name){
		String key = getKey(subject, name);
		String floor = props.floorKey(key);

		if(floor != null){
			if(StringUtil.matched(floor, key)){
				return props.get(floor);
			}
		}
		return null;
	}

	public Variable contextHasVariable(String name){
		if(vars.containsKey(name)){
			return vars.get(name);
		}
		return null;
	}

	public Expression contextHasCalculation(String name){
		if(calculs.containsKey(name)){
			Declaration dec = calculs.get(name);
			return dec.getStatement();
		}
		return null;
	}

	public FunctionDeclaration contextHasDeclaration(String name, List<Entity> args, char type){
		String num = "0";

		num = String.valueOf(args.size());

		String key = name + "#" + num;

		FunctionDeclaration fd = null;
		if(type == 'c'){
			if(fcalculs.containsKey(key)){
				fd = fcalculs.get(key);
			}
			//			String floor = calculs.floorKey(nt);
			//			while(floor != null){
			//				String key0 = StringUtil.dropTail(floor);
			//				if(StringUtil.matched(key, key0)){
			//					Declaration dec = calculs.get(floor);
			//					
			//					floor = calculs.lowerKey(floor);
			//				}else{
			//					break;
			//				}
			//			}
		}else if(type == 'a'){
			if(factions.containsKey(key)){
				fd = factions.get(key);
			}
		}else{
			if(fdecisions.containsKey(key)){
				fd = fdecisions.get(key);
			}
		}


		return fd;
	}

	public FunctionDeclaration contextHasDeclarationImpl(String name, List<Entity> args, char type){
		String num = "0";

		num = String.valueOf(args.size());

		String key = name + "#" + num;

		FunctionDeclaration fd = null;
		if(type == 'c'){
			if(impl_fcalculs.containsKey(key)){
				fd = impl_fcalculs.get(key);
			}
			String key0 = impl_fcalculs.ceilingKey(key);
			while(key0 != null){
				if(StringUtil.matched(key0, key)){
					FunctionDeclaration dec = impl_fcalculs.get(key0);
					//若key0匹配，则必然是条件声明					
					ConditionDeclaration cd = (ConditionDeclaration) dec;
					//check whether conditions are satisfied
					RuntimeContext rc = getLadder().newRuntimeContext('c');
					int i = 0;
					//		    		for(String arg:cd.getArguments()){
					//		    			rc.getRefers().put(arg, args.get(i));
					//		    		}
					boolean right = true;
					for(Expression expr:cd.getConditions()){
						Expression ret = rc.doDecision(expr);
						if(ret instanceof JudgmentResult){
							if(!EntityUtil.judge(ret)){
								right = false;
								break;
							}
						}else{
							return null;
						}
					}
					if(right){
						return cd;
					}else{
						right = true;
						key0 = calculs.higherKey(key0);
					}
				}else{
					break;
				}
			}
		}else if(type == 'a'){
			if(impl_factions.containsKey(key)){
				fd = impl_factions.get(key);
			}
		}else{
			if(impl_fdecisions.containsKey(key)){
				fd = impl_fdecisions.get(key);
			}
		}


		return fd;
	}
	/**************************doContext end*******************/
	public RuntimeContext makeEnvironment(List<Entity> args, List<Expression> cons){
		return null;
	}

	public RuntimeContext makeEnvironment(Entity subject, List<Expression> cons){
		return null;
	}

	public RuntimeContext makeEnvironment(List<Expression> cons) throws ApplicationException{
		RuntimeContext rc = new RuntimeContext();
		for(Expression expr:cons){
			Expression ret = doStatement(expr);
			if(ret instanceof HiwiiException){
				throw new ApplicationException();
			}
		}
		return rc;
	}

	/**
	 * new Runtime过程中，doStatement与doAction不同。
	 * 与doProgramCalculation也不同。doStatement只允许陈述，不允许定义
	 * @param cons
	 */
	public Expression doStatement(Expression expr){
		return null;
	}

	public Expression unaryTranslate(UnaryOperation expr){
		String op = expr.getOperator();
		if(op.equals("!")){
			return new MappingExpression("negate",  Arrays.asList(expr.getOperand()));
		}else{
			try {
				String name = SystemOperators.getOperationName(op);
				FunctionExpression fe = new FunctionExpression(name, Arrays.asList(expr.getOperand()));
				return fe;
			} catch (ApplicationException e) {
				return null;
			}
		}
		//    	return null;
	}

	public Expression binaryTranslate(BinaryOperation expr){
		String op = expr.getOperator();
		List<Expression> args =  Arrays.asList(expr.getLeft(), expr.getRight());
		if(op.equals("&")){
			return new MappingExpression("and", args);
		}
		if(op.equals("!")){
			return new MappingExpression("or", args);
		}
		if(op.equals(":")){
			return new MappingExpression("describe", args);
		}
		if(op.equals("@")){
			return new MappingExpression("at", args);
		}
		if(op.equals(":=")){
			return new MappingExpression("assign", args);
		}
		if(op.equals("::")){
			return new MappingExpression("turn", args);
		}
		Expression left = expr.getLeft();
		Expression right = expr.getRight();
		if(op.equals("->")){			
//			if(!(right instanceof IdentifierExpression)){
//				return new HiwiiException();  //format err!
//			}
//			IdentifierExpression ie = (IdentifierExpression) right;
//			Definition def = hasDefinition(ie.getName());
//			if(def == null){
//				return new HiwiiException();
//			}
//			Entity ent = doCalculation(left);
//			if(ent == null){
//				return new HiwiiException();
//			}
//			if(ent instanceof HiwiiException){
//				return (HiwiiException) ent;
//			}
//			boolean res = EntityUtil.judgeEntityIsDefinition(ent, def);
//			return EntityUtil.decide(res);
			return new MappingExpression("is", args);
		}
		if(op.equals("=>")){
			LambdaMapping le = new LambdaMapping();
			List<String> keys = new ArrayList<String>();
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				keys.add(ie.getName());
			}else if(left instanceof BracketExpression){
				BracketExpression be = (BracketExpression) left;
				for(Expression exp:be.getArray()){
					if(left instanceof IdentifierExpression){
						IdentifierExpression ie = (IdentifierExpression) exp;
						keys.add(ie.getName());
					}else{
						return new HiwiiException();
					}
				}
			}else{
				return new HiwiiException();
			}
			le.setKeys(keys);
			le.setStatement(right);
			return le;
		}
		else{
			try {
				String name = SystemOperators.getOperationName(op);
				FunctionExpression fe = new FunctionExpression(name, args);
				return fe;
			} catch (ApplicationException e) {
				return new HiwiiException();
			}
		}
		//    	return null;
	}

	public Expression proxyIdentifierAction(String name){
		if(name.equals("skip")){
			return new SkipReturn();
		}else if(name.equals("break")){
			return new BreakReturn();
		}else if(name.equals("return")){
			return new ReturnEnd();
		}else if(name.equals("logout")){
			return logout();
		}else if(name.equals("openSpace")){
			User user = getLadder().getSessionContext().getSession().getUser();
			if(user == null) {
				return new HiwiiException("not logined");
			}
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			try {
				db.openSpace();
				LocalHost.getInstance().setOpenSpace(true);
				return new NormalEnd();
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				return new HiwiiException();
			}
		}else if(name.equals("closeSpace")){
			User user = getLadder().getSessionContext().getSession().getUser();
			if(user == null) {
				return new HiwiiException("not logined");
			}
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			try {
				db.closeSpace();
				LocalHost.getInstance().setOpenSpace(false);
				return new NormalEnd();
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				return new HiwiiException();
			}
		}else if(name.equals("history")){
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			try {
				String ret = db.historyMessage();
				getLadder().getSessionContext().getSession().doResponse(ret);
				return new NormalEnd();
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				return new HiwiiException();
			}
		}else if(name.equals("clearHistory")){
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			Transaction txn = null;
			try {
				txn = db.beginTransaction();
				db.clearHistory(txn);
				getLadder().getSessionContext().getSession().doResponse("ok");
				txn.commit();
				return new NormalEnd();
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				return new HiwiiException();
			} finally{
				if (txn != null) {
					txn.abort();
					txn = null;
				}
			}			
		}
//		else if(name.equals("exit")){
//			LocalHost.getInstance().exit();
//		}
		//TODO idAction on db
		return null;
	}

	public Expression proxyMappingAction(String name, List<Expression> args){
		if(name.equals("ask")){
			//运算疑问
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity target = doCalculation(args.get(0));
			SessionContext sc = getLadder().getSessionContext();
			if(target != null){
				if(target instanceof Definition){
					
				}
				sc.getSession().doResponse(target.toString());
			}else{
				sc.getSession().doResponse("");
			}
			return new NormalEnd();
		}else if(name.equals("answer")){
			//运算疑问
			if(args.size() != 1){
				return new HiwiiException();
			}
			SessionContext sc = getLadder().getSessionContext();
			sc.getSession().doResponse(args.get(0).toString());
			return new NormalEnd();
		}else if(name.equals("whether")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Expression target = doDecision(args.get(0));
			SessionContext sc = getLadder().getSessionContext();
			if(target != null){
				sc.getSession().doResponse(target.toString());
			}else{
				sc.getSession().doResponse("");
			}
			return new NormalEnd();
		}
		if(name.equals("decide")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Expression res = doDecision(args.get(0));
			return res;//res is Exception or JudgmentResult
		}else if(name.equals("throw")){
			return new HiwiiException();
		}else if(name.equals("return")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			ReturnResult ret = new ReturnResult();
			Entity res = doCalculation(args.get(0));
			if(res instanceof HiwiiException){
				return (Expression) res;
			}
			ret.setResult(res);
			return ret;
		}
		
		if(name.equals("if")){
			return doIfAction(args);
		}else if(name.equals("choose")){

		}else if(name.equals("each")){

		}else if(name.equals("for")){
			//for[x:=int0,condition,post,statement]
			if(args.size() == 2){
				//dowhileLoop
				return doWhileLoop(args.get(0), args.get(1));
			}
			if(args.size() != 4){
				return new HiwiiException();
			}
			//for[initial,condition,post,statement]
			return doForLoop(args.get(0), args.get(1), args.get(2), args.get(3));
		}else if(name.equals("while")){
			if(args.size() != 2){
				return new HiwiiException();
			}			
			return doWhileLoop(args.get(0), args.get(1));
		}else if(name.equals("assign")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			return doAssign(args.get(0), args.get(1));
		}else if(name.equals("turn")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			return turnJudge(args.get(0), args.get(1));
		}else if(name.equals("put")){
			if(args.size() == 1){
				return putInstance(args.get(0));
			}else if(args.size() == 2){
				//put(Object, childObject)
				return doNewAction(args.get(0), args.get(1));
			}
			return new HiwiiException();
		}else if(name.equals("create")){
			if(args.size() == 1){
				Expression ret = putInstance(args.get(0));
				return ret;
			}else if(args.size() == 2){
				//put(Object, childObject)
				return doNewAction(args.get(0), args.get(1));
			}
			return new HiwiiException();
		}else if(name.equals("define")){
			if(args.size() == 1){
				return doDefine(args.get(0));
			}else if(args.size() == 2){
				return doDefine(args.get(0), args.get(1));
			}else{
				return new HiwiiException();
			}
		}else if(name.equals("redefine")){
			if(args.size() == 2){
				return doDefine(args.get(0), args.get(1));
			}
		}else if(name.equals("declare")){
			if(args.size() == 2){
				return doDeclare(args.get(0), args.get(1));
			}else{
				return new HiwiiException();
			}
		}else if(name.equals("undefine")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			return undefineAction(args.get(0));
		}else if(name.equals("defineFunction")){
			/**
			 * defineFunction[name, argumentsSize, Link/State/Action]
			 * argumentsSize:1..n,0 
			 */
			if(args.size() == 3){
				return defineFunction(args.get(0), args.get(1), args.get(2));
			}else{
				return new HiwiiException();
			}
		}else if(name.equals("drop")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			return doDropAction(args.get(0));
		}else if(name.equals("var") || name.equals("variable")){
			if(args.size() == 1){
				return newVariable(args.get(0));
			}else if(args.size() == 2) {
				return newVariable(args.get(0), args.get(1));
			}else {
				return new HiwiiException();
			}
			
		}else if(name.equals("refer")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			return doRefer(args.get(0), args.get(1));
		}else if(name.equals("boolean")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			return newBoolean(args.get(0));
		}else if(name.equals("booleans")){
			//s表示静态
			if(args.size() != 1){
				return new HiwiiException();
			}
			return newBooleanST(args.get(0));
		}else if(name.equals("symbol")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			return newSymbol(args.get(0));
		}else if(name.equals("expression")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			return newExpression(args.get(0));
		}else if(name.equals("expressions")){
			//s表示静态
			if(args.size() != 1){
				return new HiwiiException();
			}
			return newExpressionST(args.get(0));
		}if(name.equals("make")){
			if(args.size() == 0){
				return new HiwiiException();
			}
			return constantDescribe(args);
		}else if(name.equals("let")){
			if(args.size() == 0){
				return new HiwiiException();
			}
			return constantDescribe(args);
		}else if(name.equals("start")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			//			return doStartService(args.get(0), adverbs);
		}else if(name.equals("run")){
			if(args.size() != 1){
				return new HiwiiException();
			}			
			return doAction(args.get(0));
		}else if(name.equals("login")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			return login(args.get(0), args.get(1));
		}else if(name.equals("closeSpace")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			return closeSpace(args.get(0));
		}else if(name.equals("lock")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			return lock(args.get(0));
		}else if(name.equals("permit")){
			if(args.size() != 1){
				return new HiwiiException();
			}
//			return login(args.get(0));
		}else if(name.equals("grant")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			return grant(args.get(0), args.get(1));
		}else if(name.equals("drop")){
			if(args.size() != 1){
				return new HiwiiException();
			}			
			//			return doDropAction(args.get(0), adverbs);
		}
		return null;
	}
	

	public Expression proxyIdentifierDecision(String name){
		if(name.equals("true")){
			JudgmentResult jr = new JudgmentResult();
			jr.setResult(true);
			return jr;
		}else if(name.equals("false")){
			JudgmentResult jr = new JudgmentResult();
			jr.setResult(false);
			return jr;
		}else if(name.equals("isOpenSpace")){
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			try {
				boolean res = db.isOpenSpace();
				return EntityUtil.decide(res);
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				return new HiwiiException();
			}
		}
		if(name.equals("logined")) {
			User user = getLadder().getSessionContext().getSession().getUser();
			if(user != null) {
				return EntityUtil.decide(true);
			}else {
				return EntityUtil.decide(false);
			}
		}
		return null;
	}

	public Expression proxyFunctionDecision(String name, List<Entity> args){
		if(name.equals("GT") || name.equals("LT") || name.equals("GE")|| name.equals("LE") ||
				name.equals("LEQ") || name.equals("NE") || name.equals("EQ")){
			if(args.size() == 2){
				Entity left = args.get(0);
				Entity right = args.get(1);;
				List<Entity> ents = Arrays.asList(right);
				return left.doFunctionDecision(name, ents);
			}else{
				return new HiwiiException();
			}
		}
		if(name.equals("hasUser")){
			if(!(args.get(0) instanceof StringExpression)){
				return new HiwiiException();
			}
			StringExpression se = (StringExpression) args.get(0);
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			try {
				if(db.hasUser(se.getValue(), null)){
					return EntityUtil.decide(true);
				}
				return EntityUtil.decide(false);
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				return new HiwiiException();
			} 
		}
		return null;
	}
	/**
	 * 分为词汇定义、陈述、原理指导三类new语句。
	 * 词汇定义又分为名词、动词和形容词三类
	 * 名词类定义：definition
	 * @param expr
	 * @return
	 */
	public Expression doNewAction(Expression expr){
//		return putInstance(expr);
		return null;
	}

	public Expression doNewAction(Expression expr, Expression base){
		Entity entity = doCalculation(base);

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Transaction txn = null;
		try {
			if(base instanceof IdentifierExpression){

			}
			HiwiiInstance vo = formObject(expr);

			if(!instanceCheck(vo)){
				return new HiwiiException();
			}
			if(!EntityUtil.shouldBeInPart(vo, entity)){
				return new HiwiiException();
			}
			txn = db.beginTransaction();
			db.putChildInstance(vo, entity, null);
			txn.commit();
			return new NormalEnd();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		} finally{
			if (txn != null) {
				txn.abort();
				txn = null;
			}
		}
		//		return new NormalEnd();

	}
	
	
	
	public Expression doDefine(Expression expr){
		if(expr instanceof IdentifierExpression){
			Definition def = new Definition();
//			String name = "";
			String master = "";
			if(getLadder().getSessionContext().getSession().getUser() != null){
				master = getLadder().getSessionContext().getSession().getUser().getUserid();
			}
			def.setMaster(master);
			IdentifierExpression ie = (IdentifierExpression) expr;
			def.setName(ie.getName());
			def.setSignature(ie.getName());

			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			Transaction txn = null;
			try {
				txn = db.beginTransaction();
				db.putDefinition(def, null);
				txn.commit();
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				return new HiwiiException();
			}finally{
				if (txn != null) {
					txn.abort();
					txn = null;
				}
			}
			return new NormalEnd();
		}
		return new HiwiiException();
	}
	
	public Expression doDeclare(Expression source, Expression expr){
		if(source instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) source;
			if(!(bo.getOperator().equals(":"))){
				return new HiwiiException();
			}
			
			Expression left = bo.getLeft();
			String cogn = null;

			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				cogn = ie.getName();
			}else{
				return new HiwiiException();
			}

			Expression right = bo.getRight();
			if(cogn.equals("Calculation") || cogn.equals("Decision") || cogn.equals("Action")){
				char tp = 0;
				if(cogn.equals("Action")){
					tp = 'a';
				}else if(cogn.equals("Calculation")){
					tp = 'c';
				}else{
					tp = 'd';
				}
				return doDeclare(tp, right, expr);
			}
		}else {
			return new HiwiiException();
		}
		return new NormalEnd();
	}
	public Expression doDefine(Expression source, Expression expr){
		if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			if(bo.getOperator().equals(":")){
				Expression left = bo.getLeft();
				String cogn = null;

				if(left instanceof IdentifierExpression){
					IdentifierExpression ie = (IdentifierExpression) left;
					cogn = ie.getName();
				}else{
					return new HiwiiException();
				}

				Expression right = bo.getRight();
				
				if(cogn.equals("Property")|| cogn.equals("Link")){
					return defineLink(source, right);
				}else if(cogn.equals("State")){ //原为new(Status
					//status=state 表示状态
					return newStatus(right);
				}else if(cogn.equals("Switch")){ //原为new(Status
					//status=state 表示状态
					return newSwitch(source, right);
				}else if(cogn.equals("Variable")){
					return persistVariable(right);
				}else if(cogn.equals("Constanct")){ 
					return doRefer(right);
				}else if(cogn.equals("Object")||cogn.equals("Reference")){ //before “Object"
					Entity target = doCalculation(right);
					return defineReference(source, target);
				}else if(cogn.equals("Calculation") || cogn.equals("Decision") || cogn.equals("Action")){
					char tp = 0;
					if(cogn.equals("Action")){
						tp = 'a';
					}else if(cogn.equals("Calculation")){
						tp = 'c';
					}else{
						tp = 'd';
					}
					return doDeclare(tp, source, right);
				}else if(cogn.equals("AtomAction")) {
					//原子Action
				}else if(cogn.equals("Process")) {
					
				}else if(cogn.equals("Calculation_intf") || cogn.equals("Decision_intf") || cogn.equals("Action_intf")){
					char tp = 0;
					if(cogn.equals("Action_intf")){
						tp = 'a';
					}else if(cogn.equals("Calculation_intf")){
						tp = 'c';
					}else{
						tp = 'd';
					}
					return doDeclareInterface(tp, right);
				}else if(cogn.equals("Calculation_impl") || cogn.equals("Decision_impl") || cogn.equals("Action_impl")){
					char tp = 0;
					if(cogn.equals("Action_impl")){
						tp = 'a';
					}else if(cogn.equals("Calculation_impl")){
						tp = 'c';
					}else{
						tp = 'd';
					}
					return doDeclareImplement(tp, right);
				}else if(cogn.equals("Adjective")){ //原为new(Status
					return newStatus(right);
				}else if(cogn.equals("Verb")){
					//			return newVerb(right);
				}else if(cogn.equals("Symbol")){ 
					//			return newStatus(right);
				}else if(cogn.equals("Assignment")){
					//			return doAssign(right, target);
				}else if(cogn.equals("Observation")){ //or Statement陈述
					// || cogn.equals("Negative"))fact exist be or notBe.to be or not to be
					//			return newJudgment(target, right);
				}else if(cogn.equals("Cognition")){ //Judgment

				}else if(cogn.equals("Inference")){ 

				}else if(cogn.equals("User")){ 

				}else if(cogn.equals("Definition")){
					return newDefinition(source, right);
				}
			}else if(bo.getOperator().equals("->")){
				Expression left = bo.getLeft();
				Expression right = bo.getRight();
				String cogn = null;

				if(left instanceof IdentifierExpression){
					IdentifierExpression ie = (IdentifierExpression) left;
					cogn = ie.getName();
				}else{
					return new HiwiiException();
				}
				if(cogn.equals("Link") || cogn.equals("Property")){
					return defineLink(source, right);
				}else if(cogn.equals("Variable")){
					return defineVariable(source, right);
				}else{
					
				}
				return newDefinition(source, expr);
			}else {
				return newDefinition(source, expr);
			}
		}else{
			return newDefinition(source, expr);
		}
		
		return new NormalEnd();
	}
	
	public Expression defineFunction(Expression func, Expression arg, Expression expr){
		if(!(func instanceof IdentifierExpression)){
			return new HiwiiException();
		}
		String num = null;
		if(arg instanceof IntegerNumber) {
			IntegerNumber in = (IntegerNumber) arg;
			num = in.getValue();
		}else if(arg instanceof IdentifierExpression) {
			IdentifierExpression ie = (IdentifierExpression) arg;
			if(!ie.getName().equals("")) {
				return new HiwiiException();
			}
			num = ie.getName();
		}else {
			return new HiwiiException();
		}
		IdentifierExpression ie = (IdentifierExpression) func;
//		String name = ie.getName();
		String type = null;
		if(expr instanceof BinaryOperation) {
			BinaryOperation bo = (BinaryOperation) expr;
			if(bo.getOperator().equals(":")) {
				Expression left = bo.getLeft();
				if(left instanceof IdentifierExpression) {
					IdentifierExpression ie0 = (IdentifierExpression) left;
					type = ie0.getName();
				}else {
					return new HiwiiException();
				}
			}else {
				return new HiwiiException();
			}
			Expression right = bo.getRight();
			String linktype = null;
			if(right instanceof IdentifierExpression) {
				IdentifierExpression ie0 = (IdentifierExpression) right;
				linktype = ie0.getName();
			}else {
				return new HiwiiException();
			}
			if(type.equals("Link")) {
				String key = type + "#" + num;
				HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
				Transaction txn = null;
				try {
					txn = db.beginTransaction();
					db.putProperty(key, linktype, null);
					txn.commit();
				} catch (DatabaseException e) {
					return new HiwiiException();
				} catch (IOException e) {
					return new HiwiiException();
				} catch (ApplicationException e) {
					return new HiwiiException();
				} catch (Exception e) {
					return new HiwiiException();
				}finally{
					if (txn != null) {
						txn.abort();
						txn = null;
					}
				}
			}
		}else if(expr instanceof IdentifierExpression) {
			IdentifierExpression ie0 = (IdentifierExpression) expr;
			type = ie0.getName();
			if(type.equals("Action")) {
				String key = type + "#" + num;
				HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
				Transaction txn = null;
				try {
					txn = db.beginTransaction();
					db.putAction(key, null);
					txn.commit();
				} catch (DatabaseException e) {
					return new HiwiiException();
				} catch (IOException e) {
					return new HiwiiException();
				} catch (ApplicationException e) {
					return new HiwiiException();
				} catch (Exception e) {
					return new HiwiiException();
				}finally{
					if (txn != null) {
						txn.abort();
						txn = null;
					}
				}
			}else if(type.equals("State")) {
				String key = type + "#" + num;
				HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
				Transaction txn = null;
				try {
					txn = db.beginTransaction();
					db.putStatus(key, null);
					txn.commit();
				} catch (DatabaseException e) {
					return new HiwiiException();
				} catch (IOException e) {
					return new HiwiiException();
				} catch (ApplicationException e) {
					return new HiwiiException();
				} catch (Exception e) {
					return new HiwiiException();
				}finally{
					if (txn != null) {
						txn.abort();
						txn = null;
					}
				}
			}else {
				
			}
		}else {
			return new HiwiiException();
		}
		return new NormalEnd();
	}
	
	public Expression doDefine(Entity subject, Expression source, Expression expr){
		if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			if(bo.getOperator().equals(":")){
				Expression left = bo.getLeft();
				String cogn = null;

				if(left instanceof IdentifierExpression){
					IdentifierExpression ie = (IdentifierExpression) left;
					cogn = ie.getName();
				}else{
					return new HiwiiException();
				}

				Expression right = bo.getRight();
				if(cogn.equals("Calculation") || cogn.equals("Decision") || cogn.equals("Action")){
					char tp = 0;
					if(cogn.equals("Action")){
						tp = 'a';
					}else if(cogn.equals("Calculation")){
						tp = 'c';
					}else{
						tp = 'd';
					}
					return doDeclare(tp, source, right);
				}else if(cogn.equals("AtomAction")) {
					//原子Action
				}else if(cogn.equals("Process")) {
					return newProcess(subject, source, right);
				}else if(cogn.equals("Adjective")){ //原为new(Status
					return newStatus(right);
				}else if(cogn.equals("Verb")){
					//			return newVerb(right);
				}else if(cogn.equals("Status")){ //原为new(Status
					//status=state 表示状态
					return newStatus(right);
				}else if(cogn.equals("Symbol")){ 
					//			return newStatus(right);
				}else if(cogn.equals("Assignment")){
					//			return doAssign(right, target);
				}else if(cogn.equals("Property")){
					return defineLink(source, right);
				}else if(cogn.equals("Variable")){
					return persistVariable(right);
				}else if(cogn.equals("Constanct")){ 
					return doRefer(right);
				}else if(cogn.equals("Reference")){ //before “Object"
					Entity target = doCalculation(right);
					return defineReference(source, target);
				}else if(cogn.equals("Observation")){ //or Statement陈述
					// || cogn.equals("Negative"))fact exist be or notBe.to be or not to be
					//			return newJudgment(target, right);
				}else if(cogn.equals("Cognition")){ //Judgment

				}else if(cogn.equals("Inference")){ 

				}else if(cogn.equals("User")){ 

				}else if(cogn.equals("Definition")){
					return newDefinition(source, right);
				}
			}else if(bo.getOperator().equals("->")){
				Expression left = bo.getLeft();
				Expression right = bo.getRight();
				String cogn = null;

				if(left instanceof IdentifierExpression){
					IdentifierExpression ie = (IdentifierExpression) left;
					cogn = ie.getName();
				}else{
					return new HiwiiException();
				}
				if(cogn.equals("Link")){
					return defineLink(source, right);
				}else if(cogn.equals("Variable")){
					return defineVariable(source, right);
				}else{
					
				}
				return newDefinition(source, expr);
			}else {
				return newDefinition(source, expr);
			}
		}else{
			return newDefinition(source, expr);
		}
		
		return new NormalEnd();
	}
	
	public Expression newDefinition(Expression source, Expression expr){
		String name = "";
		if(source instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) source;
			name = ie.getName();
		}else{
			return new HiwiiException();
		}

		Definition def = new Definition();
		String master = "";
		if(getLadder().getSessionContext().getSession().getUser() != null){
			master = getLadder().getSessionContext().getSession().getUser().getUserid();
//			Entity val = inst.getPropertyValue("userid");
//			if(val instanceof StringExpression){
//				StringExpression se = (StringExpression) val;
//				master = se.getValue();
//			}
		}
		def.setMaster(master);
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			if(ie.getName().equals("State")) {
				return newStatus(source);
			}
			if(ie.getName().equals("Action")) {
				return newStatus(source);
			}
			try {
				Definition parent = EntityUtil.proxyGetDefinition(ie.getName());
				if(parent == null){
					return new HiwiiException();
				}
				def.setSignature(parent.getSignature() + "." + name);
			} catch (Exception e) {
				return new HiwiiException();
			}
			def.setName(name);			
		}else if(expr instanceof BraceExpression){
			BraceExpression prg = (BraceExpression) expr;
			def.setName(name);
			def.setSignature(name);
			Expression ret = this.fillDefinition(def, prg.getArray());
			if(ret instanceof HiwiiException){
				return ret;
			}
		}else if(expr instanceof IdentifierBrace){
			IdentifierBrace ib = (IdentifierBrace) expr;
			def.setName(ib.getName());
			def.setSignature(ib.getName());
			Expression ret = this.fillDefinition(def, ib.getConditions());
			if(ret instanceof HiwiiException){
				return ret;
			}
		}else if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			if(!bo.getOperator().equals("->")){
				return new HiwiiException();
			}
			Expression left = bo.getLeft();
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				try {
					Definition parent = EntityUtil.proxyGetDefinition(ie.getName());
					if(parent == null){
						return new HiwiiException();
					}
					def.setSignature(parent.getSignature() + "." + name);
				} catch (Exception e) {
					return new HiwiiException();
				}
				def.setName(ie.getName());
				def.setSignature(ie.getName());
			}else if(left instanceof IdentifierBrace){
				IdentifierBrace ib = (IdentifierBrace) left;
				def.setName(ib.getName());
				def.setSignature(ib.getName());
				Expression ret = this.fillDefinition(def, ib.getConditions());
				if(ret instanceof HiwiiException){
					return ret;
				}
			}else{
				return new HiwiiException();
			}

			Expression right = bo.getRight();

			if(right instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) right;
				def.setParent(ie.getName());
			}else if(right instanceof IdentifierBrace){
				IdentifierBrace ib = (IdentifierBrace) right;
				def.setParent(ib.getName());
//				def.setCharacteristics(ib.getConditions());
			}else{
				return new HiwiiException();
			}
			try {
				Definition parent = EntityUtil.proxyGetDefinition(def.getParent());
				if(parent == null){
					return new HiwiiException();
				}
				def.setSignature(parent.getSignature() + "." + name);
			} catch (Exception e) {
				return new HiwiiException();
			}
		}else{
			return new HiwiiException();
		}

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Transaction txn = null;
		try {
			txn = db.beginTransaction();
			db.putDefinition(def, null);
			txn.commit();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}finally{
			if (txn != null) {
				txn.abort();
				txn = null;
			}
		}
		return new NormalEnd();
	}
//	public Expression doDefineAction(Expression expr){}
	
	public Expression undefineAction(Expression expr){
		if(!(expr instanceof BinaryOperation)){
			return persistDefinition(expr);
		}
		BinaryOperation bo = (BinaryOperation) expr;
		if(bo.getOperator().equals(":")){
			Expression left = bo.getLeft();
			String cogn = null;

			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				cogn = ie.getName();
			}else{
				return new HiwiiException();
			}

			Expression right = bo.getRight();
			if(cogn.equals("Calculation") || cogn.equals("Decision") || cogn.equals("Action")){
				char tp = 0;
				if(cogn.equals("Action")){
					tp = 'a';
				}else if(cogn.equals("Calculation")){
					tp = 'c';
				}else{
					tp = 'd';
				}
				return unDeclare(tp, right);
			}else if(cogn.equals("Calculation_intf") || cogn.equals("Decision_intf") || cogn.equals("Action_intf")){
				char tp = 0;
				if(cogn.equals("Action_intf")){
					tp = 'a';
				}else if(cogn.equals("Calculation_intf")){
					tp = 'c';
				}else{
					tp = 'd';
				}
				return doDeclareInterface(tp, right);
			}else if(cogn.equals("Calculation_impl") || cogn.equals("Decision_impl") || cogn.equals("Action_impl")){
				char tp = 0;
				if(cogn.equals("Action_impl")){
					tp = 'a';
				}else if(cogn.equals("Calculation_impl")){
					tp = 'c';
				}else{
					tp = 'd';
				}
				return doDeclareImplement(tp, right);
			}else if(cogn.equals("Adjective")){ //原为new(Status
				return newStatus(right);
			}else if(cogn.equals("Verb")){
				//			return newVerb(right);
			}else if(cogn.equals("Status")){ //原为new(Status
				//status=state 表示状态
				return newStatus(right);
			}else if(cogn.equals("Symbol")){ 
				//			return newStatus(right);
			}else if(cogn.equals("Assignment")){
				//			return doAssign(right, target);
			}else if(cogn.equals("Property")){
//				return defineLink(source, right);
			}else if(cogn.equals("Variable")){
				return persistVariable(right);
			}else if(cogn.equals("Constanct")){ 
				return doRefer(right);
			}else if(cogn.equals("Object")){
				//			return doPerceive(right);
			}else if(cogn.equals("Link")){ //or Statement陈述
				return undefineLink(right);
			}else if(cogn.equals("Cognition")){ //Judgment

			}else if(cogn.equals("Inference")){ 

			}else if(cogn.equals("User")){ 

			}else if(cogn.equals("Definition")){
				return persistDefinition(right);
				//doDefine(right);
			}
		}else {
			return persistDefinition(expr);
		}
		
		return new NormalEnd();
	}

	public Expression hasDefined(Expression expr){
		if(!(expr instanceof BinaryOperation)){
			if(expr instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) expr;
				Definition def;
				try {
					def = EntityUtil.proxyGetDefinition(ie.getName());
				} catch (Exception e) {
					return new HiwiiException();
				}
				if(def == null){
					return EntityUtil.decide(false);
				}else{
					return EntityUtil.decide(true);
				}
			}else{
				return new HiwiiException();
			}
		}
		BinaryOperation bo = (BinaryOperation) expr;
		if(bo.getOperator().equals(":")){
			Expression left = bo.getLeft();
			String cogn = null;

			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				cogn = ie.getName();
			}else{
				return new HiwiiException();
			}

			Expression right = bo.getRight();
			if(cogn.equals("Calculation") || cogn.equals("Decision") || cogn.equals("Action")){
				char tp = 0;
				if(cogn.equals("Action")){
					tp = 'a';
				}else if(cogn.equals("Calculation")){
					tp = 'c';
				}else{
					tp = 'd';
				}
				return hasDeclared(tp, right);
			}else if(cogn.equals("Symbol")){ 
				//			return newStatus(right);
			}else if(cogn.equals("State")){ //原为new(Status
				return hasStatus(right);
			}else if(cogn.equals("Verb")){
				//			return newVerb(right);
			}else if(cogn.equals("Assignment")){
				//			return doAssign(right, target);
			}else if(cogn.equals("Property")){
				return hasProperty(right);
			}else if(cogn.equals("Link")){
				return definedLink(right);
			}else if(cogn.equals("Variable")){
				return newVariable(right);
			}else if(cogn.equals("Object")){
				//			return doPerceive(right);
			}else if(cogn.equals("Observation")){ //or Statement陈述
				// || cogn.equals("Negative"))fact exist be or notBe.to be or not to be
				//			return newJudgment(target, right);
			}else if(cogn.equals("Cognition")){ //Judgment

			}else if(cogn.equals("Reference")){ 
				return doRefer(right);
			}else if(cogn.equals("Inference")){ 

			}else if(cogn.equals("User")){ 

			}else if(cogn.equals("Definition")){
				return persistDefinition(right);
				//doDefine(right);
			}
		}else {
				
		}
		
		return null;
	}
	
	/**
	 * 参数：
	 * that[definition/class]  //view无意义
	 * all[definition/class]  //view无意义
	 * @param expr
	 * @return
	 */
	public Expression doDropAction(Expression expr){
		if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			if(me.getName().equals("one")){
				if(me.getArguments().size() != 1){
					return new HiwiiException();
				}
				return dropSingleAction(me.getArguments().get(0));
			}else if(me.getName().equals("all")){
				if(me.getArguments().size() != 1){
					return new HiwiiException();
				}
				return dropMultiAction(me.getArguments().get(0));
			}else {
				
			}
		}else if(expr instanceof IdentifierExpression){
//			IdentifierExpression ie = (IdentifierExpression) expr;
			return dropMultiAction(expr);
		}
		
		
		BinaryOperation bo = (BinaryOperation) expr;
		if(!bo.getOperator().equals(":")){
			return new HiwiiException();
		}
		Expression left = bo.getLeft();
		String cogn = null;

		if(left instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) left;
			cogn = ie.getName();
		}else if(left instanceof BinaryOperation){
			BinaryOperation bl = (BinaryOperation) left;
			if(!(bl.getLeft() instanceof IdentifierExpression)){
				return new HiwiiException();
			}
			if(!bl.getOperator().equals("@")){
				return new HiwiiException();
			}
			IdentifierExpression ie = (IdentifierExpression) bl.getLeft();
			cogn = ie.getName();
		}else{
			return new HiwiiException();
		}


		Expression right = bo.getRight();
		//new(noun/adjective/verb/noun@target

		if(cogn.equals("Definition")){
			return dropDefinition(right);
			//doDefine(right);
		}else if(cogn.equals("Symbol")){ 
			//			return newStatus(right);
		}else if(cogn.equals("Status")){ //原为new(Status
			//			return newStatus(right);
		}else if(cogn.equals("Verb")){
			//			return newVerb(right);
		}else if(cogn.equals("Assignment")){
			//			return doAssign(right, target);
		}else if(cogn.equals("Property")){
//			return defineLink(source, right);
			//			return newVariable(right);
		}else if(cogn.equals("Variable")){
			return newVariable(right);
		}else if(cogn.equals("Object")){
			//			return doPerceive(right);
		}else if(cogn.equals("Observation")){ //or Statement陈述
			// || cogn.equals("Negative"))fact exist be or notBe.to be or not to be
			//			return newJudgment(target, right);
		}else if(cogn.equals("Cognition")){ //Judgment

		}else if(cogn.equals("Reference")){ 
			return doRefer(right);
		}else if(cogn.equals("Inference")){ 

		}else if(cogn.equals("User")){ 

		}
		return new NormalEnd();
	}

	public Expression doDefinitionDefine(Definition def, Expression expr){
		if(!(expr instanceof BinaryOperation)){
			return new HiwiiException();
		}
		BinaryOperation bo = (BinaryOperation) expr;
		if(!bo.getOperator().equals(":")){
			return new HiwiiException();
		}
		Expression left = bo.getLeft();
		String cogn = null;

		if(left instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) left;
			cogn = ie.getName();
		}else{
			return new HiwiiException();
		}


		Expression right = bo.getRight();

		if(cogn.equals("Status")){
			return newStatus(def, right);
		}else if(cogn.equals("Property")){
			return newProperty(def, right);
		}else if(cogn.equals("Calculation") || cogn.equals("Decision") || cogn.equals("Action")){
			char tp = 0;
			if(cogn.equals("Action")){
				tp = 'a';
			}else if(cogn.equals("Calculation")){
				tp = 'c';
			}else{
				tp = 'd';
			}
			return doDeclare(def, tp, right);
		}else{
			try {
				Definition type = EntityUtil.proxyGetDefinition(cogn);
				if(type == null){
					return new HiwiiException();
				}
			} catch (Exception e) {
				return new HiwiiException();
			}
		}

		return new NormalEnd();
	}
	
	public Expression doDefinitionDeclare(Definition def, Expression source, Expression expr){
		if(!(source instanceof BinaryOperation)){
			return new HiwiiException();
		}
		BinaryOperation bo = (BinaryOperation) source;
		if(!bo.getOperator().equals(":")){
			return new HiwiiException();
		}
		Expression left = bo.getLeft();
		String cogn = null;

		if(left instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) left;
			cogn = ie.getName();
		}else{
			return new HiwiiException();
		}


		Expression right = bo.getRight();

		char tp = 0;
		if(cogn.equals("Calculation")) {
			tp = 'c';
		}else if( cogn.equals("Decision") ) {
			tp = 'd';
		}else if( cogn.equals("Action") ) {
			tp = 'a';
		}else{
			return new HiwiiException();
		}

		return doDefinitionDeclare(def, tp, right, expr);
//		return new NormalEnd();
	}
	
	public Expression doDefinitionDeclare(Definition def, char type, Expression source, Expression expr){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			if(source instanceof IdentifierExpression) {

			}else if(source instanceof FunctionExpression) {
				FunctionExpression fe = (FunctionExpression) source;
				if(type == 'c'){
					db.putFunctionCalculation(def, fe, expr, null);
				}else if(type == 'd'){
					//				db.putFunDecision(fd, null);
				}else{
					//				db.putFunAction(fd, null);
				}
			}else if(source instanceof MappingExpression) {

			}else {

			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			e.printStackTrace();
			return new HiwiiException();
		} 
		return new NormalEnd();
	}

	/**
	 * put[definition]
	 * @param expr
	 * @return
	 */
	public Expression putInstance(Expression expr){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Transaction txn = null;
		try {
			String name = null;
			if(expr instanceof IdentifierExpression) {
				IdentifierExpression ie = (IdentifierExpression) expr;
				name = ie.getName();
				Definition def = EntityUtil.proxyGetDefinition(name);
				if(def == null) {
					return new HiwiiException();
				}
				String defname = def.getName();
				if(defname.equals("User") || defname.equals("Group") || defname.equals("Role")) {
					return new HiwiiException();
				}
				HiwiiInstance vo = new HiwiiInstance();
				vo.setClassName(name);
				txn = db.beginTransaction();
				
				String key = db.putInstance(name, null);
				vo.setUuid(key);
				txn.commit();
				Session sess = getLadder().getSessionContext().getSession();
				sess.putEntity(vo);
				ReturnResult result = new ReturnResult();
				result.setResult(vo);
				return result;
			}else if(expr instanceof BinaryOperation) {
				BinaryOperation bo = (BinaryOperation) expr;
				if(!bo.getOperator().equals(":")) {
					return new HiwiiException();
				}
				Definition def = null;
				Expression left = bo.getLeft();
				if(left instanceof IdentifierExpression) {
					IdentifierExpression ie = (IdentifierExpression) left;
					name = ie.getName();
					def = EntityUtil.proxyGetDefinition(name);
				}
				String defname = def.getName();
				if(defname.equals("User") || defname.equals("Group") || defname.equals("Role")) {
					return new HiwiiException();
				}
				return putInstance(def, bo.getRight());
			}else {
				return new HiwiiException();
			}			

		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		} finally{
			if (txn != null) {
				txn.abort();
				txn = null;
			}
		}
		//		}
//		return new NormalEnd();
	}
	
	
	/**
	 * put[definition:{decorations}]
	 * @param expr
	 * @return
	 */
	public Expression putInstance(Definition def0, Expression prg){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Transaction txn = null;
		try {
			HiwiiInstance vo = new HiwiiInstance();
			if(!(prg instanceof BraceExpression)) {
				return new HiwiiException("sytax err");
			}
			BraceExpression states = (BraceExpression) prg;
			for(Expression expr:states.getArray()){
				if(expr instanceof BinaryOperation){
					BinaryOperation bo = (BinaryOperation) expr;
					Expression left = bo.getLeft();
					Expression right = bo.getRight();

					if(bo.getOperator().equals(":=")){
						Entity val = doCalculation(right);
						if(val instanceof HiwiiException){
							return (HiwiiException)val;
						}
						//property set
						if(left instanceof IdentifierExpression){
							IdentifierExpression ie = (IdentifierExpression) left;
							if(vo.getAssignments().containsKey(ie.getName())){
								Assignment ass = vo.getAssignments().get(ie.getName());
								ass.setValue(val);
								continue;
							}
							Assignment ass = new Assignment();
							ass.setName(ie.getName());
							ass.setValue(val);
							vo.getAssignments().put(ie.getName(), ass);
						}else{
							return new HiwiiException();
						}
					}else if(bo.getOperator().equals("::")){

					}
				}else{
					return new HiwiiException();
				}
			}
			
			txn = db.beginTransaction();

			String key = db.putInstance(vo, null);
			txn.commit();
			Session sess = getLadder().getSessionContext().getSession();
			sess.putEntity(vo);
			vo.setUuid(key);
			ReturnResult result = new ReturnResult();
			result.setResult(vo);
			return result;
//			return new NormalEnd();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		} finally{
			if (txn != null) {
				txn.abort();
				txn = null;
			}
		}
		//		}
//		return new NormalEnd();
	}

	public Group instanceToGroup(HiwiiInstance inst) throws ApplicationException{
		Group grp = new Group();
		for(Assignment ass:inst.getAssignments().values()){
			Entity val = ass.getValue();
			if(!(val instanceof StringExpression)){
				throw new ApplicationException();
			}
			String value = ((StringExpression)val).getValue();
			if(ass.getName().equals("id")){
				grp.setGroupId(value);
			}else if(ass.getName().equals("name")){
				grp.setName(value);
			}else if(ass.getName().equals("note")){
				grp.setNote(value);
			}
		}
		if(grp.getName() == null){
			grp.setName(grp.getGroupId());
		}
		return grp;
	}
	/**
	 * 仅形成对象，不验证
	 * @param expr
	 * @return
	 * @throws ApplicationException
	 */
	public HiwiiInstance formObject(Expression expr) throws ApplicationException{
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			String name = ie.getName();
			if(name.equals("User") || name.equals("Role") || name.equals("Group")){
				throw new ApplicationException("more information required！");
			}
			try {
				Definition def = EntityUtil.proxyGetDefinition(ie.getName());
				if(def == null){
					throw new ApplicationException("definition is null！");
				}
			} catch (Exception e) {
				throw new ApplicationException("definition is null！");
			}
			HiwiiInstance vo = new HiwiiInstance();
			vo.setClassName(ie.getName());
			return vo;
		}else if(expr instanceof IdentifierBrace){
			IdentifierBrace ib = (IdentifierBrace) expr;
			try {
				Definition def = EntityUtil.proxyGetDefinition(ib.getName());
				if(def == null){
					throw new ApplicationException("definition is null！");
				}
			} catch (Exception e) {
				throw new ApplicationException("definition is null！");
			}
			HiwiiInstance vo = new HiwiiInstance();
			vo.setClassName(ib.getName());
			Expression ret = toCognize(vo, ib.getConditions());
			if(ret instanceof HiwiiException){
				return null;
			}
			return vo;
		}
		throw new ApplicationException();
	}

	public Entity newInstance(Expression expr){
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			
//			Entity ret = calculateDefinition(expr);
//			if(!(ret instanceof Definition)){
//				return ret;
//			}
			Definition def = null;
			try {
				def = EntityUtil.proxyGetDefinition(ie.getName());
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				e.printStackTrace();
				return new HiwiiException();
			}
//			VarInstance inst = new VarInstance();
			HiwiiInstance inst = new HiwiiInstance();
			inst.setClassName(def.getName());
			return inst;
		}
		if(expr instanceof IdentifierBrace){
//			Entity ret = calculateDefinition(expr);
//			if(!(ret instanceof Definition)){
//				return ret;
//			}
//			Definition def = (Definition) ret;
//			HiwiiInstance inst = new HiwiiInstance();
//			inst.setClassName(def.getName());
			HiwiiInstance inst;
			try {
				inst = formObject(expr);
			} catch (ApplicationException e) {
				return new HiwiiException(e.getMessage());
			}
			return inst;
		}
		if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			String name = me.getName();
			List<Expression> args = me.getArguments();
			if(name.equals("List")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				Entity ret = getListClass(args.get(0));
				if(ret instanceof HiwiiException){
					return ret;
				}
				return ret.doIdentifierCalculation("new");
			}
			if(name.equals("Set")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				Entity ret = getSetClass(args.get(0));
				if(ret instanceof HiwiiException){
					return ret;
				}
				return ret.doIdentifierCalculation("new");
			}

			if(name.equals("Array")){
				if(args.size() == 0){
					return new HiwiiException();
				}
				List<Integer> dims = new ArrayList<Integer>();
				for(Expression item:args){
					Entity ret = doCalculation(item);
					if(ret instanceof IntegerNumber){
						IntegerNumber in = (IntegerNumber) item;
						int num = Integer.parseInt(in.getValue());
						dims.add(num);
					}else if(ret instanceof HiwiiException){
						return ret;
					}else{
						return new HiwiiException("type err!");
					}
				}
				Array array = new Array();
				array.setDimension(dims);
				
				return array.doIdentifierCalculation("new");
			}
			if(name.equals("Tuple")){
				if(args.size() < 2){
					return new HiwiiException();
				}
				Tuple tuple = new Tuple();
				try {
					List<Argument> names = EntityUtil.parseArguments(args);
					tuple.setNames(names);
				} catch (ApplicationException e) {
					return new HiwiiException();
				}
				
				return tuple.doIdentifierCalculation("new");
			}
		}
		return null;
	}
	public Expression toCognize(HiwiiInstance target, List<Expression> decos){
		for(Expression expr:decos){
			if(expr instanceof BinaryOperation){
				BinaryOperation bo = (BinaryOperation) expr;
				Expression left = bo.getLeft();
				Expression right = bo.getRight();

				if(bo.getOperator().equals(":=")){
					Entity val = doCalculation(right);
					if(val instanceof HiwiiException){
						return (HiwiiException)val;
					}
					//property set
					if(left instanceof IdentifierExpression){
						IdentifierExpression ie = (IdentifierExpression) left;
						if(target.getAssignments().containsKey(ie.getName())){
							Assignment ass = target.getAssignments().get(ie.getName());
							ass.setValue(val);
							continue;
						}
						Assignment ass = new Assignment();
						ass.setName(ie.getName());
						ass.setValue(val);
						target.getAssignments().put(ie.getName(), ass);
					}else{
						return new HiwiiException();
					}
				}else if(bo.getOperator().equals("::")){

				}
			}else{
				return new HiwiiException();
			}
		}
		return new NormalEnd();
	}

	public boolean instanceCheck(HiwiiInstance target) throws ApplicationException{
		Definition def = null;
		try {
			def = EntityUtil.proxyGetDefinition(target.getClassName());
		} catch (Exception e) {
			throw new ApplicationException();
		}
		if(def == null){
			throw new ApplicationException();
		}
		for(Assignment ass:target.getAssignments().values()){
			Property prop;
			try {
				prop = EntityUtil.proxyGetProperty(ass.getName(), target.getClassName());
				//hasProperty(target, ie.getName());
				if(prop == null){
					throw new ApplicationException();
				}
			} catch (Exception e) {
				throw new ApplicationException();
			}
			if(!EntityUtil.judgeValueToProperty(ass.getValue(), prop)){
				return false;
			}
		}
		return true;
	}
	public Entity doGetSingleAction(Expression expr){
//		String type = "test";
//		if(expr instanceof IdentifierExpression){
//			IdentifierExpression ie = (IdentifierExpression) expr;
//			type = ie.getName();
//		}
//		try {			
//			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
//			HiwiiInstance ret = db.getSingleInstance(type, null, null);
//			return ret;
//		} catch (DatabaseException e) {
//			return new HiwiiException();
//		} catch (IOException e) {
//			return new HiwiiException();
//		} catch (ApplicationException e) {
//			return new HiwiiException();
//		} catch (Exception e) {
//			return new HiwiiException();
//		}
		return null;
	}

	public Entity doGetMultiAction(Expression expr){
//		String type = "test";
//		if(expr instanceof IdentifierExpression){
//			IdentifierExpression ie = (IdentifierExpression) expr;
//			type = ie.getName();
//		}
//		try {			
//			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
//			HiwiiInstance ret = db.getSingleInstance(type, null, null);
//			return ret;
//		} catch (DatabaseException e) {
//			return new HiwiiException();
//		} catch (IOException e) {
//			return new HiwiiException();
//		} catch (ApplicationException e) {
//			return new HiwiiException();
//		} catch (Exception e) {
//			return new HiwiiException();
//		}
		return null;
	}

	public Expression dropSingleAction(Expression expr){
		String type = "test";
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			type = ie.getName();
		}
		try {			
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			db.deleteSingleInstance(type, null, null);
			return new NormalEnd();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}
	}
	
	public Expression dropMultiAction(Expression expr){
		String type = "test";
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			type = ie.getName();
		}
		try {			
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			db.deleteMultiInstance(type);
			return new NormalEnd();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}
	}

	public Entity proxyIdentifierCalculation(String name){
		if(name.equals("LocalHost")){
			return LocalHost.getInstance();
		}
//		if(name.equals("context")){
//			return this;
//		}
//		if(name.equals("Precision")){
//			return new IntegerNumber("4");
//		}
//		if(name.equals("DefaultRound")){
//			return new Rounding();
//		}
//		if(name.equals("ascent")){
//			return new AscentExpression();
//		}
//		if(name.equals("descent")){
//			return new DescentExpression();
//		}
		if(name.equals("theUser")){
			User user = getLadder().getSessionContext().getSession().getUser();
			if(user == null){
				return new NullValue();
			}
			StringExpression se = new StringExpression(user.getUserid());
			return se;
		}
		if(name.equals("now")){
//			TimeValue ret = EntityUtil.timeNow();
			Calendar cal = Calendar.getInstance();
			TimeObject ret = new TimeObject();
			ret.setTime(cal);
			return ret;
		}
		if(name.equals("me") || name.equals("wo") || name.equals("我")){
			if(getLadder().getSessionContext().getSession().getUser() != null){
				User user = getLadder().getSessionContext().getSession().getUser();
				return user;
			}
		}
		if(name.equals("null")){
			return new NullValue();
		}

		return null;
	}

	public Entity proxyFunctionCalculation(String name, List<Entity> args){
		if(name.equals("doCalculation")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof StringExpression)) {
				return new HiwiiException();
			}
			StringExpression  str = (StringExpression) args.get(0);
			Expression expr = str.toExpression();
			if(expr instanceof HiwiiException) {
				return expr;
			}
			return doCalculation(expr);
		}
		if(name.equals("alternate")){
			if(args.size() < 2){
				return new HiwiiException();
			}
			AlternateRegular ar = new AlternateRegular();
			List<RegularExpression> alt = new ArrayList<RegularExpression>();
			for(Entity ent:args){
				if(ent instanceof RegularExpression){
					RegularExpression re = (RegularExpression) ent;
					alt.add(re);
				}else{
					return new HiwiiException();
				}
			}
			ar.setAlters(alt);
			return ar;
		}else if(name.equals("file")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity ent = args.get(0);
			if(!(ent instanceof StringExpression)){
				return new HiwiiException();
			}
			StringExpression expr = (StringExpression) ent;
			File f = new File(expr.getValue());
			if(f.exists() && f.isDirectory()){
				return new HiwiiException();
			}
			FileObject fo = new FileObject(expr.getValue());
			return fo;
		}else if(name.equals("dir")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity ent = args.get(0);
			if(!(ent instanceof StringExpression)){
				return new HiwiiException();
			}
			StringExpression expr = (StringExpression) ent;
			File f = new File(expr.getValue());
			if(f.exists() && !f.isDirectory()){
				return new HiwiiException();
			}
			Directory dir = new Directory(expr.getValue());
			return dir;
		}else if(name.equals("host")){
			if(args.size() == 1){
				Entity a = args.get(0);
				if(!(a instanceof StringExpression)){
					return new HiwiiException();
				}
				StringExpression ip = (StringExpression) a;
				HostObject ho = new HostObject(ip.getValue());
				return ho;
			}
			if(args.size() != 2){
				return new HiwiiException();
			}
			Entity a = args.get(0);
			Entity b = args.get(1);
			if(!(a instanceof StringExpression)){
				return new HiwiiException();
			}
			if(!(b instanceof IntegerNumber)){
				return new HiwiiException();
			}
			StringExpression ip = (StringExpression) a;
			IntegerNumber port = (IntegerNumber) b;

			HostObject ho = new HostObject(ip.getValue(), Integer.parseInt(port.getValue()));
			return ho;
		}else if(name.equals("gang")){ //多个对象
			if(args.size() != 2){
				return new HiwiiException();
			}
			Entity a = args.get(0);
			Entity b = args.get(1);
			if(!(a instanceof RealNumber)){
				return new HiwiiException();
			}
			if(!(b instanceof Abstraction)){
				return new HiwiiException();
			}
			RealNumber ne = (RealNumber) a;
			Abstraction tar = (Abstraction) b;

			GangOfObject goo = new GangOfObject();
			goo.setNumber(ne);
			goo.setTarget(tar);
			return goo;
		}else if(name.equals("unit")){ //container
			if(args.size() != 2){
				return new HiwiiException();
			}
			Entity a = args.get(0);
			Entity b = args.get(1);
			if(!(a instanceof Abstraction)){
				return new HiwiiException();
			}
			if(!(b instanceof Abstraction)){
				return new HiwiiException();
			}
			Abstraction con = (Abstraction) a;
			Abstraction tar = (Abstraction) b;

			ObjectContainer oc = new ObjectContainer();
			oc.setContainer(con);
			oc.setTarget(tar);
			return oc;
		}else if(name.equals("list")){
			EntityList el = new EntityList();
			for(Entity itm : args){
				if((itm != null) && !(itm instanceof NullExpression) ){
					el.add(itm);
				}
			}
			return el;
		}else if(name.equals("enum")){
			//			try {
			//				if(overlapInList(args)){
			//					return new HiwiiException();
			//				}
			//			} catch (ApplicationException e) {
			//				return new HiwiiException();
			//			}
			EnumSet es = new EnumSet();
			es.setItems(args);
			return es;
		}else if(name.equals("plus") || name.equals("minus") || name.equals("multiply") || 
				name.equals("divide") || name.equals("pow")){
			if(args.size() == 0){
				return new HiwiiException();
			}
			if(args.size() == 1){
				return args.get(0).doIdentifierCalculation(name);
			}else{
				return args.get(0).doFunctionCalculation(name, args.subList(1, args.size()));
			}
		}else if(name.equals("image")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof StringExpression)){
				return new HiwiiException();
			}
			StringExpression se = (StringExpression) args.get(0);
			ImageEntity ie = new ImageEntity();
			ie.setPath(se.getValue());
			return ie;
		}else if(name.equals("time")){
			if(args.size() == 1){
				if(!(args.get(0) instanceof StringExpression)){
					return new HiwiiException();
				}
				StringExpression se = (StringExpression) args.get(0);
				return EntityUtil.time(se.getValue());
			}else if(args.size() == 3){
				for(int i=0;i<3;i++){
					if(!args.get(i).getClassName().equals("Integer")){
						return new HiwiiException();
					}
				}
				int year = Integer.parseInt(((IntegerNumber)args.get(0)).getValue());
				int month = Integer.parseInt(((IntegerNumber)args.get(1)).getValue());
				int day = Integer.parseInt(((IntegerNumber)args.get(2)).getValue());
				return EntityUtil.time(year, month, day);
			}else if(args.size() == 5){
				for(int i=0;i<5;i++){
					if(!args.get(i).getClassName().equals("Integer")){
						return new HiwiiException();
					}
				}
				int year = Integer.parseInt(((IntegerNumber)args.get(0)).getValue());
				int month = Integer.parseInt(((IntegerNumber)args.get(1)).getValue());
				int day = Integer.parseInt(((IntegerNumber)args.get(2)).getValue());
				int hour = Integer.parseInt(((IntegerNumber)args.get(3)).getValue());
				int minute = Integer.parseInt(((IntegerNumber)args.get(4)).getValue());
				return EntityUtil.time(year, month, day, hour, minute);
			}else if(args.size() == 6){
				for(int i=0;i<6;i++){
					if(!args.get(i).getClassName().equals("Integer")){
						return new HiwiiException();
					}
				}
				int year = Integer.parseInt(((IntegerNumber)args.get(0)).getValue());
				int month = Integer.parseInt(((IntegerNumber)args.get(1)).getValue());
				int day = Integer.parseInt(((IntegerNumber)args.get(2)).getValue());
				int hour = Integer.parseInt(((IntegerNumber)args.get(3)).getValue());
				int minute = Integer.parseInt(((IntegerNumber)args.get(4)).getValue());
				int second = Integer.parseInt(((IntegerNumber)args.get(5)).getValue());
				return EntityUtil.time(year, month, day, hour, minute, second);
			}else{
				return new HiwiiException();
			}
		}

		return null;
	}
	public Expression doIfAction(List<Expression> args){
		boolean doelse = true;
		Expression expr = args.get(0);
		Expression decide = doDecision(expr);
		if(decide instanceof JudgmentResult){
			JudgmentResult jr = (JudgmentResult) decide;
			if(jr.isResult()){
				doelse = false;
				Expression ret = doAction(args.get(1));				
				return ret;
			}
		}else{
			return decide;
		}

		if(doelse && args.size() == 3){
			Expression ret = doAction(args.get(2));
			return ret;
		}
		return new NormalEnd();
	}

	public Expression doSilentIfAction(List<Expression> args){
		boolean doelse = true;
		Expression expr = args.get(0);
		Expression decide = contextDecision(expr);
		if(decide instanceof JudgmentResult){
			JudgmentResult jr = (JudgmentResult) decide;
			if(jr.isResult()){
				doelse = false;
				Expression ret = doSilentAction(args.get(1));				
				return ret;
			}
		}else{
			return decide;
		}

		if(doelse && args.size() == 3){
			Expression ret = doSilentAction(args.get(2));
			return ret;
		}
		return new NormalEnd();
	}
	public Expression doIfAction(Entity subject, List<Expression> args){
		boolean doelse = true;
		Expression expr = args.get(0);
		Expression decide = doDecision(subject, expr);
		if(decide instanceof JudgmentResult){
			JudgmentResult jr = (JudgmentResult) decide;
			if(jr.isResult()){
				doelse = false;
				Expression ret = doAction(subject, args.get(1));				
				return ret;
			}
		}else{
			return decide;
		}

		if(doelse && args.size() == 3){
			Expression ret = doAction(subject, args.get(2));
			return ret;
		}
		return new NormalEnd();
	}

	/**
	 * choose[case[3>2]:ask[3], case[5>4]:ask[5], else:ask[6]]
	 * 未判断结果是否为exception
	 * @param subject
	 * @param bs
	 * @param context
	 * @return
	 */
	public Expression doBranchAction(List<Expression> args){
		List<Expression> judges = new ArrayList<Expression>();
		List<Expression> prgs = new ArrayList<Expression>();
		Expression other = null;

		int i = 0;
		int last = args.size() - 1;
		for(Expression expr:args){
			if(!(expr instanceof BinaryOperation)){
				return new HiwiiException();
			}
			BinaryOperation bo = (BinaryOperation) expr;
			if(!bo.getOperator().equals(":")){
				return new HiwiiException();
			}
			Expression left = bo.getLeft();
			Expression right = bo.getRight();
			if(left instanceof MappingExpression){
				MappingExpression me = (MappingExpression) left;
				if(!me.getName().equals("case")){
					return new HiwiiException();
				}
				if(me.getArguments().size() != 1){
					return new HiwiiException();
				}
				judges.add(me.getArguments().get(0));
				prgs.add(right);
			}else if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				String name = ie.getName();
				if(name.equals("else") && i == last){
					other = right;
				}else{
					return new HiwiiException();
				}
			}else{
				return new HiwiiException();
			}
		}
		//		if(judges.size() != prgs.size()){
		//			return new HiwiiException();
		//		}
		boolean doelse = true;

		i = 0;
		while(i < judges.size()){
			Expression expr = judges.get(i);
			Expression decide = doDecision(expr);
			if(decide instanceof JudgmentResult){
				JudgmentResult jr = (JudgmentResult) decide;
				if(jr.isResult()){
					doelse = false;
					Expression ret = doAction(prgs.get(i));
					return ret;
				}
			}else{
				return decide;
			}
			i++;
		}
		if(other != null && doelse){
			Expression ret = doAction(other);
			return ret;
		}
		return null;
	}

	/**
	 * while[condition, statement]
	 * @param cond
	 * @param stm
	 * @return
	 */
	public Expression doWhileLoop(Expression cond, Expression stm){
		RuntimeContext rc = getLadder().newRuntimeContext('a');
		rc.setLoop(true);
		//		boolean old = this.isLoop();
		//		this.setLoop(true);
		//new runtimeContext
		//		this.setLoop(old);
		int temp = 0;
		while(true){
			Expression jud = null;
			jud = doDecision(cond);
			if(jud instanceof JudgmentResult){
				JudgmentResult jr = (JudgmentResult) jud;
				if(jr.isResult()){
					Expression result = null;
					result = doAction(stm);

					if(result instanceof BreakReturn){
						break; //consume break
					}else if(result instanceof ReturnEnd){
						return result;
					}else if(result instanceof JudgmentResult){
						return result;
					}else if(result instanceof ReturnResult){
						return result;
					}

					if(result instanceof HiwiiException){
						return result;
					}
				}else{
					return new NormalEnd();
				}
			}else{
				return jud;
			}
			temp++;
			if(temp >= 10){
				break;  //为防止测试环境压力大，每次测试最多允许10次循环。
			}
		}
		return new NormalEnd();
	}

	/**
	 * for[x:=value1,condition,post,statement]
	 * for[[x,y]:=[value1,value2],condition,post,statement]
	 * 不需要声明初始变量的类型。如果类型不匹配，post运算或条件判断会出错。但这不是必须声明类型的理由。
	 * 在表达式编辑阶段就发现错误很好，但和语法一致性相比，后者更重要。判断一个变量的类型是在运行中，而不是在编辑器中。
	 * for循环的循环参数，执行体可以访问，但是不允许修改。而java中是可以修改的。
	 * 初始条件部分是variable定义和初始化
	 * for循环变量只能说identifier，不设置类型限制。因此，当与期望的类型不匹配，
	 * 赋值时不会报错，但是条件判断时会报错。
	 * @param init
	 * @param cond
	 * @param post
	 * @param stm
	 * @return
	 */
	public Expression doForLoop(Expression init, Expression cond, Expression post, Expression stm){
		RuntimeContext rc = getLadder().newRuntimeContext('a');

		Expression judge = doForLoopInit(init, rc);
		if(judge instanceof HiwiiException){
			return judge;
		}
		int temp = 0;
		while(true){
			Expression ret = rc.doDecision(cond);
			if(!(ret instanceof JudgmentResult)){
				return ret;
			}
			if(!EntityUtil.judge(ret)){
				break;
			}
			Expression result = rc.doAction(stm);

			if(result instanceof BreakReturn){
				break; //consume break
			}else if(result instanceof ReturnEnd){
				return result;
			}else if(result instanceof JudgmentResult){
				return result;
			}else if(result instanceof ReturnResult){
				return result;
			}

			if(result instanceof HiwiiException){
				return result;
			}

			if (post != null) {
				judge = doForLoopPost(post, rc);
				if(judge instanceof HiwiiException){
					return judge;
				}
			}
			temp++;
			if(temp >= 10){
				break;  //为防止测试环境压力大，每次测试最多允许10次循环。
			}
		}
		return new NormalEnd();
	}

	public Expression doProgramAction(BraceExpression prg){
		RuntimeContext rc = getLadder().newRuntimeContext('c');
		List<Expression> sents = prg.getArray();
		Expression result = null;
		for(Expression expr:sents){
			result = rc.doAction(expr);

			if(result instanceof SkipReturn){
				break; //consume skip identifier
			}else if(result instanceof BreakReturn){
				return result;//will be consumed in loop action.if there is no loop will throw exception
			}else if(result instanceof ExitEnd){
//				return result;
				//不允许程序执行中退出系统，只能命令行退出
			}else if(result instanceof ReturnEnd){
				return result;
			}else if(result instanceof JudgmentResult){
				return result;
			}else if(result instanceof ReturnResult){
				return result;
			}

			if(result instanceof HiwiiException){
				//				System.out.println("some exception happened!");
				return result;
			}
		}
		return new NormalEnd();
	}
	
	public Expression doProgramAction(Entity subject, BraceExpression prg){
		RuntimeContext rc = getLadder().newRuntimeContext('c');
		List<Expression> sents = prg.getArray();
		Expression result = null;
		for(Expression expr:sents){
			
			result = rc.doAction(expr);
			if(result == null){
				result = rc.doAction(subject, expr);
			}

			if(result instanceof SkipReturn){
				break; //consume skip identifier
			}else if(result instanceof BreakReturn){
				return result;//will be consumed in loop action.if there is no loop will throw exception
			}else if(result instanceof ExitEnd){
				return result;
			}else if(result instanceof ReturnEnd){
				return result;
			}else if(result instanceof JudgmentResult){
				return result;
			}else if(result instanceof ReturnResult){
				return result;
			}

			if(result instanceof HiwiiException){
				//System.out.println("some exception happened!");
				return result;
			}
		}
		return new NormalEnd();
	}

	public Expression doProgramCalculation(BraceExpression prg){
		RuntimeContext rc = getLadder().newRuntimeContext('c');
		List<Expression> sents = prg.getArray();
		Expression result = null;
		for(Expression expr:sents){
			//只允许context操作
			result = rc.doSilentAction(expr);
			
			if(result instanceof SkipReturn){
				break; //consume skip identifier
			}else if(result instanceof BreakReturn){
				return result;//will be consumed in loop action.if there is no loop will throw exception
			}else if(result instanceof ExitEnd){
				return result;
			}else if(result instanceof ReturnEnd){
				return result;
			}else if(result instanceof JudgmentResult){
				return result;
			}else if(result instanceof ReturnResult){
//				ReturnResult or = (ReturnResult) result;
//				return or.getResult();
				return result;
			}

			if(result instanceof HiwiiException){
				//				System.out.println("some exception happened!");
				return result;
			}
		}
		return new HiwiiException();
	}
	
	public Expression doProgramCalculation(Entity subject, BraceExpression prg){
		RuntimeContext rc = getLadder().newRuntimeContext('c');
		List<Expression> sents = prg.getArray();
		Expression result = null;
		for(Expression expr:sents){
			result = rc.doSilentAction(expr);
			if(result == null){
				result = rc.doSilentAction(subject, expr);
			}

			if(result instanceof SkipReturn){
				break; //consume skip identifier
			}else if(result instanceof BreakReturn){
				return result;//will be consumed in loop action.if there is no loop will throw exception
			}else if(result instanceof ExitEnd){
				return result;
			}else if(result instanceof ReturnEnd){
				return result;
			}else if(result instanceof JudgmentResult){
				return result;
			}else if(result instanceof ReturnResult){
				return result;
			}

			if(result instanceof HiwiiException){
				//				System.out.println("some exception happened!");
				return result;
			}
		}
		return new NormalEnd();
	}
	
	public Entity doProgramProcess(Entity subject, BraceExpression prg){
		RuntimeContext rc = getLadder().newRuntimeContext('c');
		List<Expression> sents = prg.getArray();
		Entity result = null;
		for(Expression expr:sents){
			result = rc.doProcessItem(subject, expr);		

			if(result instanceof SkipReturn){
				break; //consume skip identifier
			}else if(result instanceof BreakReturn){
				return result;//will be consumed in loop action.if there is no loop will throw exception
			}else if(result instanceof ExitEnd){
				return result;
			}else if(result instanceof ReturnEnd){
				return result;
			}else if(result instanceof JudgmentResult){
				return result;
			}else if(result instanceof ReturnResult){
				return result;
			}			

			if(result instanceof HiwiiException){
				//				System.out.println("some exception happened!");
				return result;
			}
		}
		return new NormalEnd();
	}
	
	public Expression doSilentAction(Expression expr){
		if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			String name = me.getName();
			List<Expression> args = me.getArguments();
			if(name.equals("decide")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				Expression res = doDecision(args.get(0));
				return res;//res is Exception or JudgmentResult
			}else if(name.equals("throw")){
				return new HiwiiException();
			}else if(name.equals("return")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				ReturnResult ret = new ReturnResult();
				Entity res = doCalculation(args.get(0));
				if(res instanceof HiwiiException){
					return (Expression) res;
				}
				ret.setResult(res);
				return ret;
			}
			if(name.equals("if")){
				return doSilentIfAction(args);
			}else if(name.equals("choose")){

			}else if(name.equals("each")){

			}else if(name.equals("for")){
				//for[x:=int0,condition,post,statement]
				if(args.size() == 2){
					//dowhileLoop
					return doWhileLoop(args.get(0), args.get(1));
				}
				if(args.size() != 4){
					return new HiwiiException();
				}
				//for[initial,condition,post,statement]
				return doForLoop(args.get(0), args.get(1), args.get(2), args.get(3));
			}else if(name.equals("while")){
				if(args.size() != 2){
					return new HiwiiException();
				}			
				return doWhileLoop(args.get(0), args.get(1));
			}else if(name.equals("assign")){
				if(args.size() != 2){
					return new HiwiiException();
				}
				doAssignSilent(args.get(0), args.get(1));
			}else if(name.equals("turn")){
				if(args.size() != 2){
					return new HiwiiException();
				}
				return turnJudge(args.get(0), args.get(1));
			}else if(name.equals("var") || name.equals("variable")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				return newVariable(args.get(0));
			}else if(name.equals("vars") || name.equals("variables")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				return newVariable(args.get(0));
			}else if(name.equals("boolean")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				return newBoolean(args.get(0));
			}else if(name.equals("booleans")){
				//s表示静态
				if(args.size() != 1){
					return new HiwiiException();
				}
				return newBooleanST(args.get(0));
			}else if(name.equals("symbol")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				return newSymbol(args.get(0));
			}else if(name.equals("expression")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				return newExpression(args.get(0));
			}else if(name.equals("expressions")){
				//s表示静态
				if(args.size() != 1){
					return new HiwiiException();
				}
				return newExpressionST(args.get(0));
			}else{
				return new HiwiiException();
			}
		}else if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			String name = ie.getName();
			if(name.equals("skip")){
				return new SkipReturn();
			}else if(name.equals("break")){
				return new BreakReturn();
			}else if(name.equals("return")){
				return new ReturnEnd();
			}else{
				return new HiwiiException();
			}
		}else if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			String name = fe.getName();
			if(name.equals("return")){
				if(fe.getArguments().size() != 1){
					return new HiwiiException();
				}			
				ReturnResult rr = new ReturnResult();
				Entity ent = doCalculation(fe.getArguments().get(0));
				rr.setResult(ent);
				return rr;
			}			
		}
		return null;
	}

	public Expression doSilentAction(Entity subject, Expression expr){
		if(expr instanceof MappingExpression){
			MappingExpression me = (MappingExpression) expr;
			String name = me.getName();
			List<Expression> args = me.getArguments();
			if(name.equals("decide")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				Expression res = doDecision(args.get(0));
				if(res != null){
					return res;
				}
				res = doDecision(subject, args.get(0));
				return res;//res is Exception or JudgmentResult
			}else if(name.equals("throw")){
				return new HiwiiException();
			}else if(name.equals("return")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				ReturnResult ret = new ReturnResult();
				Entity res = doCalculation(args.get(0));
				if(res instanceof HiwiiException){
					return (Expression) res;
				}
				if(res != null){
					ret.setResult(res);
					return ret;
				}
				res = doCalculation(subject, args.get(0));
				if(res instanceof HiwiiException){
					return (Expression) res;
				}
				ret.setResult(res);
				return ret;
			}
			if(name.equals("if")){
				//TODO mappingExpression处理
				return doSilentIfAction(args);
			}else if(name.equals("choose")){

			}else if(name.equals("each")){

			}else if(name.equals("for")){
				//for[x:=int0,condition,post,statement]
				if(args.size() == 2){
					//dowhileLoop
					return doWhileLoop(args.get(0), args.get(1));
				}
				if(args.size() != 4){
					return new HiwiiException();
				}
				//for[initial,condition,post,statement]
				return doForLoop(args.get(0), args.get(1), args.get(2), args.get(3));
			}else if(name.equals("while")){
				if(args.size() != 2){
					return new HiwiiException();
				}			
				return doWhileLoop(args.get(0), args.get(1));
			}else if(name.equals("assign")){
				if(args.size() != 2){
					return new HiwiiException();
				}
				doAssignSilent(args.get(0), args.get(1));
			}else if(name.equals("turn")){
				if(args.size() != 2){
					return new HiwiiException();
				}
				return turnJudge(args.get(0), args.get(1));
			}else if(name.equals("var") || name.equals("variable")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				return newVariable(args.get(0));
			}else if(name.equals("vars") || name.equals("variables")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				return newVariable(args.get(0));
			}else if(name.equals("boolean")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				return newBoolean(args.get(0));
			}else if(name.equals("booleans")){
				//s表示静态
				if(args.size() != 1){
					return new HiwiiException();
				}
				return newBooleanST(args.get(0));
			}else if(name.equals("expression")){
				if(args.size() != 1){
					return new HiwiiException();
				}
				return newExpression(args.get(0));
			}else if(name.equals("expressions")){
				//s表示静态
				if(args.size() != 1){
					return new HiwiiException();
				}
				return newExpressionST(args.get(0));
			}else{
				return new HiwiiException();
			}
		}else if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			String name = fe.getName();
			if(name.equals("return")){
				if(fe.getArguments().size() != 1){
					return new HiwiiException();
				}			
				ReturnResult rr = new ReturnResult();
				Expression arg = fe.getArguments().get(0);
				Entity ent = doCalculation(subject, arg);
				rr.setResult(ent);
				return rr;
			}			
		}
		return null;
	}
	public Entity contextCalculation(Expression expr){
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			String name = ie.getName();
			for(RuntimeContext context:this.getLadder().getChains()){
				Variable var = context.contextHasVariable(name);
				if(var != null){
					return var.getValue();
				}
				if(context.getSymbols().contains(name)){
					return new Symbol(name);
				}
				if(context.getRefers().containsKey(name)){
					return context.getRefers().get(name);
				}
				if(context.getExpressions().containsKey(name)){
					return context.getExpressions().get(name);
				}
				if(context.getExpressionST().containsKey(name)){
					return context.getExpressionST().get(name);
				}
				if(context.isFunction()){
					break;
				}
			}
		}
		return null;
	}
	
	public Expression contextDecision(Expression expr){
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			String name = ie.getName();
			for(RuntimeContext context:this.getLadder().getChains()){
				if(context.getBools().containsKey(name)){
					JudgmentResult var = context.getBools().get(name);
					return var;
				}
				if(context.isFunction()){
					break;
				}
			}
		}
		return null;
	}
	
	public Expression doProgramDecision(BraceExpression prg){
		RuntimeContext rc = getLadder().newRuntimeContext('c');
		List<Expression> sents = prg.getArray();
		Expression result = null;
		for(Expression expr:sents){
			result = rc.doAction(expr);

			if(result instanceof SkipReturn){
				break; //consume skip identifier
			}else if(result instanceof BreakReturn){
				return result;//will be consumed in loop action.if there is no loop will throw exception
			}else if(result instanceof ExitEnd){
				return result;
			}else if(result instanceof ReturnEnd){
				return result;
			}else if(result instanceof JudgmentResult){
				//				JudgmentResult jr = (JudgmentResult) result;
				return result;
			}else if(result instanceof ReturnResult){
				return result;
			}

			if(result instanceof HiwiiException){
				//				System.out.println("some exception happened!");
				return result;
			}
		}
		return new HiwiiException();
	}

	private Expression doForLoopInit(Expression init, RuntimeContext rc){
		if(init instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) init;
			if(!bo.getOperator().equals(":=")){
				return new HiwiiException("init format error!");
			}
			Expression left = bo.getLeft();
			Expression right = bo.getRight();
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				Entity value = doCalculation(right);
				rc.getRefers().put(ie.getName(), value);
			}else if(left instanceof BracketExpression){
				BracketExpression be = (BracketExpression) left;
				if(be.getArray().size() < 2){
					return new HiwiiException("init format error!");
				}
				//item in bracket must be identifier
				for(Expression item:be.getArray()){
					if(item instanceof IdentifierExpression){
						return new HiwiiException("init format error!");
					}
				}
				//when left is bracket expression, right must be too
				if(!(right instanceof BracketExpression)){
					return new HiwiiException("init format error!");
				}
				BracketExpression re = (BracketExpression) right;
				if(be.getArray().size() != re.getArray().size()){
					return new HiwiiException("number matched error!");
				}
				for(int i=0;i<be.getArray().size();i++){
					IdentifierExpression ie = (IdentifierExpression) be.getArray().get(i);
					String nm = ie.getName();
					Expression expr = re.getArray().get(i);
					Entity val = rc.doCalculation(expr);
					if(val instanceof HiwiiException){
						return (Expression) val;
					}
					rc.getRefers().put(nm, val);
				}
			}
		}else if(init == null){
			//skip initialize runtime
		}else{
			return new HiwiiException();
		}
		return new NormalEnd();
	}

	private Expression doForLoopPost(Expression init, RuntimeContext rc){
		if(init instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) init;
			if(!bo.getOperator().equals(":=")){
				return new HiwiiException("init format error!");
			}
			Expression left = bo.getLeft();
			Expression right = bo.getRight();
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				if(!rc.getRefers().containsKey(ie.getName())){
					return new HiwiiException("post assign error!");
				}
				Entity value = rc.doCalculation(right);
				rc.getRefers().put(ie.getName(), value);
			}else if(left instanceof BracketExpression){
				BracketExpression be = (BracketExpression) left;
				if(be.getArray().size() < 2){
					return new HiwiiException("post assign error!");
				}
				//item in bracket must be identifier
				for(Expression item:be.getArray()){
					if(item instanceof IdentifierExpression){
						return new HiwiiException("post assign error!");
					}
					IdentifierExpression ie = (IdentifierExpression) item;
					if(!rc.getRefers().containsKey(ie.getName())){
						return new HiwiiException("post assign error!");
					}
				}
				//when left is bracket expression, right must be too
				if(!(right instanceof BracketExpression)){
					return new HiwiiException("post assign error!");
				}
				BracketExpression re = (BracketExpression) right;
				if(be.getArray().size() != re.getArray().size()){
					return new HiwiiException("post assign error!");
				}
				for(int i=0;i<be.getArray().size();i++){
					IdentifierExpression ie = (IdentifierExpression) be.getArray().get(i);
					String nm = ie.getName();
					Expression expr = re.getArray().get(i);
					Entity val = rc.doCalculation(expr);
					if(val instanceof HiwiiException){
						return (Expression) val;
					}
					rc.getRefers().put(nm, val);
				}
			}
		}else if(init == null){
			//skip initialize runtime
		}else{
			return new HiwiiException();
		}
		return new NormalEnd();
	}

	public Definition isDefinitionName(String name){
		return null ;
	}

	/**
	 * definition由三个部分组成
	 * 1，name。2，parent。3，description
	 * new[definition:name]
	 * new[definition:name{}]
	 * new[definition:name->parent]
	 * new[definition:name{}->parent]
	 * parent can be parent{limits...}
	 * @param expr
	 * @return
	 */
	public Expression newDefinition(Expression expr){
		Definition def = new Definition();
		String name = "";
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			def.setName(ie.getName());
		}else if(expr instanceof IdentifierBrace){
			IdentifierBrace ib = (IdentifierBrace) expr;
			def.setName(ib.getName());
			Expression ret = this.fillDefinition(def, ib.getConditions());
			if(ret instanceof HiwiiException){
				return ret;
			}
		}else if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			if(!bo.getOperator().equals("->")){
				return new HiwiiException();
			}
			Expression left = bo.getLeft();
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) expr;
				def.setName(ie.getName());
			}else if(left instanceof IdentifierBrace){
				IdentifierBrace ib = (IdentifierBrace) expr;
				def.setName(ib.getName());
				Expression ret = this.fillDefinition(def, ib.getConditions());
				if(ret instanceof HiwiiException){
					return ret;
				}
			}

			Expression right = bo.getRight();
			if(right instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) right;
				try {
					Definition parent = EntityUtil.proxyGetDefinition(ie.getName());
					//hasDefinition(ie.getName());
					if(parent != null){
						def.setParent(ie.getName());
					}else{
						return new HiwiiException();
					}
				} catch (Exception e) {
					return new HiwiiException();
				}
			}else if(right instanceof IdentifierBrace){
				IdentifierBrace ib = (IdentifierBrace) right;
				Definition parent = hasDefinition(ib.getName());
				if(parent != null){
					def.setParent(ib.getName());
				}else{
					return new HiwiiException();
				}
			}
		}else{
			return new HiwiiException();
		}

		defines.put(def.getName(), def);
		return new NormalEnd();

	}

	/**
	 * definition由三个部分组成
	 * 1，name。2，structure, 3,parent。4，condition
	 * define[definition:name]
	 * define[definition:name{}]
	 * define[definition:name->parent]
	 * define[definition:name{}->parent]
	 * define[name{构成,属性,状态}->parent{条件列表}]
	 * parent can be parent{limits...}
	 * @param expr
	 * @return
	 */
	public Expression persistDefinition(Expression expr){
		Definition def = new Definition();
		String name = "";
		String master = "";
		if(getLadder().getSessionContext().getSession().getUser() != null){
//			HiwiiInstance inst = getLadder().getSessionContext().getSession().getUser();
			master = getLadder().getSessionContext().getSession().getUser().getUserid();
//			Entity val = inst.getPropertyValue("userid");
//			if(val instanceof StringExpression){
//				StringExpression se = (StringExpression) val;
//				master = se.getValue();
//			}
		}
//		def.setMaster(master);
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			def.setName(ie.getName());
			def.setSignature(ie.getName());
		}else if(expr instanceof IdentifierBrace){
			IdentifierBrace ib = (IdentifierBrace) expr;
			def.setName(ib.getName());
			def.setSignature(ib.getName());
			Expression ret = this.fillDefinition(def, ib.getConditions());
			if(ret instanceof HiwiiException){
				return ret;
			}
		}else if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			if(!bo.getOperator().equals("->")){
				return new HiwiiException();
			}
			Expression left = bo.getLeft();
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				def.setName(ie.getName());
				def.setSignature(ie.getName());
			}else if(left instanceof IdentifierBrace){
				IdentifierBrace ib = (IdentifierBrace) left;
				def.setName(ib.getName());
				def.setSignature(ib.getName());
				Expression ret = this.fillDefinition(def, ib.getConditions());
				if(ret instanceof HiwiiException){
					return ret;
				}
			}else{
				return new HiwiiException();
			}

			Expression right = bo.getRight();

			if(right instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) right;
				def.setParent(ie.getName());
			}else if(right instanceof IdentifierBrace){
				IdentifierBrace ib = (IdentifierBrace) right;
				def.setParent(ib.getName());
//				def.setCharacteristics(ib.getConditions());
			}else{
				return new HiwiiException();
			}
			try {
				Definition parent = EntityUtil.proxyGetDefinition(def.getParent());
				if(parent == null){
					return new HiwiiException();
				}
				def.setSignature(parent.getSignature() + "." + name);
			} catch (Exception e) {
				return new HiwiiException();
			}
		}else{
			return new HiwiiException();
		}

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Transaction txn = null;
		
		try {
			txn = db.beginTransaction();
			db.putDefinition(def, null);
			txn.commit();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}finally{
			if (txn != null) {
				txn.abort();
				txn = null;
			}
		}
		return new NormalEnd();

	}

	public Expression dropDefinition(Expression expr){
		String name = "";
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			name = ie.getName();
		}else{
			return new HiwiiException();
		}

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.deleteDefinitionByName(name);
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}
		return new NormalEnd();

	}


	/**
	 * decorations to describe definition.
	 * 修饰以binary形式表示。
	 * @param def
	 * @param decos
	 * @return
	 */
	public Expression fillDefinition(Definition def, List<Expression> decos){
		for(Expression expr:decos){
			if(expr instanceof BinaryOperation){
				BinaryOperation bo = (BinaryOperation) expr;
				if(bo.getOperator().equals(":")){
					doDefinitionDefine(def, bo);
				}else if(bo.getOperator().equals(":=")){
					doDefinitionDefine(def, bo);
				}
			}
		}
		return new NormalEnd();
	}
	
	public Expression defineLink(Expression source, Expression right){
		Property prop = new Property();

		List<Expression> limits = null;
		Definition type = null;
		if(right instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) right;
			String name = ie.getName();
			type = hasDefinition(name);
			if(type == null){
				return new HiwiiException();
			}
			prop.setType(type.getName());
		}else if(right instanceof BraceExpression){
			
		}else if(right instanceof IdentifierBrace){
			IdentifierBrace ib = (IdentifierBrace) right;			
			String name = ib.getName();
			type = hasDefinition(name);
			if(type == null){
				return new HiwiiException();
			}
			limits = ib.getConditions();
			prop.setType(type.getName());
			prop.setLimits(limits);
		}

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Transaction txn = null;
		try {
			txn = db.beginTransaction();
			if(source instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) source;
				prop.setName(ie.getName());
				db.putProperty(prop.getName(), type.getName(), null);
			}else if(source instanceof FunctionExpression){
				FunctionExpression func = (FunctionExpression) source;
				db.putFunctionLink(func, type.getName(), null);
			}else if(source instanceof MappingExpression){
				MappingExpression fe = (MappingExpression) source;
				if(fe.getArguments().size() == 0) {
					return new HiwiiException();
				}
				return new HiwiiException();
			}else{
				return new HiwiiException();
			}
			
			txn.commit();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}finally{
			if (txn != null) {
				txn.abort();
				txn = null;
			}
		}

		return new NormalEnd();
	}
	
	public Expression undefineLink(Expression right){
		String prop = null;

		if(right instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) right;
			prop = ie.getName();
		}else{
			return new HiwiiException();
		}

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Transaction txn = null;
		try {
			txn = db.beginTransaction();
			db.deleteProperty(prop, null);
			txn.commit();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}finally{
			if (txn != null) {
				txn.abort();
				txn = null;
			}
		}

		return new NormalEnd();
	}

	public Expression definedLink(Expression right){
		String prop = null;

		if(right instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) right;
			prop = ie.getName();
			try {
				Property ret = EntityUtil.proxyGetProperty(prop);
				if(ret != null) {
					return EntityUtil.decide(true);
				}
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				return new HiwiiException();
			}
		}else{
			return new HiwiiException();
		}
		return EntityUtil.decide(false);
	}
	
	public Expression newStatus(Expression expr){
		String name = null;
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			name = ie.getName();
		}else{
			return new HiwiiException();
		}
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.putStatus(name, null);
		}catch (DatabaseException e) {
			return new HiwiiException();
		}catch (Exception e) {
			return new HiwiiException();
		} 
		return new NormalEnd();
	}
	
	public Expression newAction(Expression expr){		
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			String name = null;
			if(expr instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) expr;
				name = ie.getName();
				db.putAction(name, null);
			}else if(expr instanceof FunctionExpression){
				FunctionExpression fe = (FunctionExpression) expr;
				if(fe.getArguments().size() == 0) {
					return new HiwiiException();
				}
				name = fe.getName();
				String key = name + "#" + fe.getArguments().size();
				db.putAction(key, null);
			}else if(expr instanceof MappingExpression){
				MappingExpression fe = (MappingExpression) expr;
				if(fe.getArguments().size() == 0) {
					return new HiwiiException();
				}
				name = fe.getName();
				String key = name + "?" + fe.getArguments().size();
				db.putAction(key, null);
			}else{
				return new HiwiiException();
			}			
		}catch (DatabaseException e) {
			return new HiwiiException();
		}catch (Exception e) {
			return new HiwiiException();
		} 
		return new NormalEnd();
	}
	
	public Expression newSwitch(Expression source, Expression expr){
		String name = null;
		if(source instanceof IdentifierExpression) {
			IdentifierExpression ie = (IdentifierExpression) source;
			name = ie.getName();
		}else {
			return new HiwiiException();
		}
		List<Expression> list = new ArrayList<Expression>();
		if(expr instanceof BraceExpression){
			BraceExpression be = (BraceExpression) expr;
			list = be.getArray();
		}else{
			return new HiwiiException();
		}
		if(list.size() == 0) {
			return new HiwiiException();
		}
		
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			for(Expression item:list) {
				if(!(item instanceof IdentifierExpression)) {
					return new HiwiiException();
				}
				IdentifierExpression ie = (IdentifierExpression) item;
				if(!db.hasStatus(ie.getName(), null)) {
					return new HiwiiException();
				}
			}
			db.putSwitch(name, list, null);
		}catch (DatabaseException e) {
			return new HiwiiException();
		}catch (Exception e) {
			return new HiwiiException();
		}
		return new NormalEnd();
	}
	
	public Expression newProcess(Entity subject, Expression left, Expression expr){
		String name = null;
		if(left instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) left;
			name = ie.getName();
		}else{
			return new HiwiiException();
		}
		String key = null;
		if(subject instanceof HiwiiInstance) {
			HiwiiInstance inst = (HiwiiInstance) subject;
			key = name + "@" + inst.getUuid();
		}else if(subject instanceof Definition) {
			
		}else {
			
		}
		
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.putProcess(key, expr.toString(), null);
		}catch (DatabaseException e) {
			return new HiwiiException();
		}catch (Exception e) {
			return new HiwiiException();
		} 
		return new NormalEnd();
	}
	
	public Expression hasStatus(Expression expr){
		String name = null;
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			name = ie.getName();
		}else{
			return new HiwiiException();
		}
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			boolean bool = db.hasStatus(name, null);
			return EntityUtil.decide(bool);
		}catch (DatabaseException e) {
			return new HiwiiException();
		}catch (Exception e) {
			return new HiwiiException();
		} 
	}
	
	public Expression hasProperty(Expression expr){
		String name = null;
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			name = ie.getName();
		}else{
			return new HiwiiException();
		}
		Property prop;
		try {
			prop = EntityUtil.proxyGetProperty(name);
			if(prop != null){
				return EntityUtil.decide(true);
			}else{
				return EntityUtil.decide(false);
			}
		} catch (Exception e) {
			return new HiwiiException();
		}
	}

	public Expression newSymbol(Expression expr){
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			if(!symbols.contains(ie.getName())){
				symbols.add(ie.getName());
			}else{
				return new HiwiiException();
			}
		}else{
			return new HiwiiException();
		}
		return new NormalEnd();
	}

	/**
	 * new[Property:name->type(){condition}]
	 * 小括号中是null或大于0的整数。当参数=1，则可以省略。
	 * new[Property:name->type{condition}]
	 * @param def
	 * @param expr
	 * @return
	 */
	public Expression newProperty(Definition def, Expression expr){
		Property prop = new Property();
		if(!(expr instanceof BinaryOperation)){
			return new HiwiiException();
		}
		BinaryOperation bo = (BinaryOperation) expr;
		if(!bo.getOperator().equals("->")){
			return new HiwiiException();
		}
		List<Expression> limits = null;
		Definition type = null;
		Expression right = bo.getRight();
		if(right instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) right;
			String name = ie.getName();
			type = hasDefinition(name);
			if(type == null){
				return new HiwiiException();
			}
			prop.setType(type.getName());
		}else if(right instanceof IdentifierBrace){
			IdentifierBrace ib = (IdentifierBrace) right;			
			String name = ib.getName();
			type = hasDefinition(name);
			if(type == null){
				return new HiwiiException();
			}
			limits = ib.getConditions();
			prop.setType(type.getName());
			prop.setLimits(limits);
		}else if(right instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) right;			
			String name = fe.getName();
			type = hasDefinition(name);
			if(type == null){
				return new HiwiiException();
			}
			prop.setType(type.getName());
			if(fe.getArguments().size() == 0){
				prop.setNumber(-1);
			}else if(fe.getArguments().size() == 1){
				if(!(fe.getArguments().get(0) instanceof IntegerNumber)){
					return new HiwiiException();
				}
				IntegerNumber in = (IntegerNumber) fe.getArguments().get(0);
				int n = Integer.parseInt(in.getValue());
				if(n < 0){
					return new HiwiiException();
				}
				prop.setNumber(n);
			}else{
				return new HiwiiException();
			}
		}else if(right instanceof FunctionBrace){
			FunctionBrace fb = (FunctionBrace) right;			
			String name = fb.getName();
			type = hasDefinition(name);
			if(type == null){
				return new HiwiiException();
			}
			prop.setType(type.getName());
			limits = fb.getStatements();
			if(fb.getArguments().size() == 0){
				prop.setLimits(limits);
				prop.setNumber(-1);
			}else if(fb.getArguments().size() == 1){
				if(!(fb.getArguments().get(0) instanceof IntegerNumber)){
					return new HiwiiException();
				}
				IntegerNumber in = (IntegerNumber) fb.getArguments().get(0);
				int n = Integer.parseInt(in.getValue());
				if(n < 0){
					return new HiwiiException();
				}
				prop.setLimits(limits);
				prop.setNumber(n);
			}else{
				return new HiwiiException();
			}
		}else{
			return new HiwiiException();
		}

		Expression left = bo.getLeft();

		if(left instanceof IdentifierExpression){
			prop.setName(((IdentifierExpression) left).getName());
		}else{
			return new HiwiiException();
		}

//		def.getProps().put(prop.getName(), prop);

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.putProperty(def, prop, null);
		}catch (DatabaseException e) {
			return new HiwiiException();
		}catch (Exception e) {
			return new HiwiiException();
		} 
		return new NormalEnd();
	}

	public Expression newStatus(Definition def, Expression expr){
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
//			def.getStates().add(ie.getName());
		}

		return new NormalEnd();
	}
	/**
	 * new(Property@target :type{condition} -> id/function)
	 * condition:1,definition
	 * 2,definition?{decision expression}
	 * property可以有默认值。
	 * property有赋值是variable
	 * @return
	 */
	public Expression newProperty(Expression expr, Entity target){
		Property prop = null;
		String sign = null;
		if(target instanceof Definition){
			sign = ((Definition) target).takeSignature();
		}else{
			//			Definition def = target.getDefinition();def.takeSignature()
			sign =   target.toString();
			//TODO entity's signature describe
		}

		String key = null;
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			prop = new EntityProperty();
			prop.setName(ie.getName());
			//			prop.setValue(new NullValue());
			key = ie.getName() + "#" + sign;
			props.put(key, prop);
			return new NormalEnd();
		}
		if(!(expr instanceof BinaryOperation)){
			return new HiwiiException();
		}
		BinaryOperation bo = (BinaryOperation) expr;
		if(bo.getOperator().equals(":=")){
			Entity value = doCalculation(bo.getRight());
			Expression left = bo.getLeft();
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				prop = new EntityProperty();
				prop.setName(ie.getName());
				//				prop.setValue(value);
				key = ie.getName() + "#" + sign;
				props.put(key, prop);
				return new NormalEnd();
			}else if(left instanceof BinaryOperation){
				BinaryOperation form = (BinaryOperation) left;
				if(form.getOperator().equals("->")){
					Expression ve = form.getRight();
					Entity type = doCalculation(form.getLeft());
					if(!(type instanceof Definition)){
						return new HiwiiException();
					}
					Definition def = (Definition) type;
					if(ve instanceof IdentifierExpression){
						IdentifierExpression ie = (IdentifierExpression) ve;
						prop = new EntityProperty();
						prop.setName(ie.getName());
						prop.setType(def.getName());
						//						prop.setValue(value);
						key = ie.getName() + "#" + sign;
						props.put(key, prop);						
						return new NormalEnd();
					}
				}
			}
		}else if(bo.getOperator().equals("->")){
			Expression left = bo.getRight();
			Entity type = doCalculation(bo.getLeft());
			if(!(type instanceof Definition)){
				return new HiwiiException();
			}
			Definition def = (Definition) type;
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				prop = new Property();
				prop.setName(ie.getName());
				prop.setType(def.getName());
				//				prop.setValue(new NullValue());
				key = ie.getName() + "#" + sign;
				props.put(key, prop);
				return new NormalEnd();
			}else if(left instanceof IdentifierBrace){
				IdentifierBrace ib = (IdentifierBrace) left;
			}
		}else{
			return new HiwiiException();
		}		
		return new NormalEnd();
	}

	/**
	 * variable与property的不同点在于，variable通常用在程序中.
	 * 涉及：name，type，condition，initial四个部分。
	 * new[Variable:type/condition->id/function :=value]
	 * 1,variable[name]  //value=null
	 * 2,variable[name:=value]
	 * 3,variable[name->type]
	 * 4,variable[name->type :=value]
	 * 5,variable[name->type{condition}]
	 * 6,variable[name->type{condition} :=value]
	 * type和value是可选项。
	 * 候选格式：
	 * variable[type:name:=value]
	 * 当只有id/function，则表示变量可以说任意类型。
	 * condition:
	 * 1,definition
	 * 2,definition~{decision expression}
	 * @return
	 */
	public Expression newVariable(Expression expr){
		Variable var = new Variable();
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			var = new Variable();
			var.setName(ie.getName());
			var.setValue(new NullValue());
			vars.put(ie.getName(), var);
			return new NormalEnd();
		}
		//TODO function variable;
		if(!(expr instanceof BinaryOperation)){
			return new HiwiiException();
		}
		BinaryOperation bo = (BinaryOperation) expr;
		if(bo.getOperator().equals(":=")){
			Entity value = doCalculation(bo.getRight());
			if(value instanceof HiwiiException){
				return (HiwiiException)value;
			}

			Expression left = bo.getLeft();
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				var.setName(ie.getName());
				var.setValue(value);
			}else if(left instanceof BinaryOperation){
				BinaryOperation form = (BinaryOperation) left;
				if(!form.getOperator().equals("->")){
					return new HiwiiException();
				}
				if(form.getLeft() instanceof IdentifierExpression){
					IdentifierExpression ie = (IdentifierExpression) form.getLeft();
					var.setName(ie.getName());
				}else{
					return new HiwiiException();
				}

				Definition def = null;
				if(form.getRight() instanceof IdentifierExpression){
					IdentifierExpression ie = (IdentifierExpression) form.getRight();
					try {
						def = EntityUtil.proxyGetDefinition(ie.getName());
						if(def == null){
							return new HiwiiException();
						}
						if(!EntityUtil.judgeEntityIsDefinition(value, def)){
							return new HiwiiException();
						}
						var.setType(def.getName());
						var.setValue(value);
					} catch (Exception e) {
						return new HiwiiException();
					}
				}else if(form.getRight() instanceof IdentifierBrace){
					IdentifierBrace ib = (IdentifierBrace) form.getRight();
					try {
						def = EntityUtil.proxyGetDefinition(ib.getName());
						if(def == null){
							return new HiwiiException();
						}
						var.setType(def.getName());
					} catch (Exception e) {
						return new HiwiiException();
					}
				}				
			}
			vars.put(var.getName(), var);
		}else if(bo.getOperator().equals("->")){
			if(bo.getLeft() instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) bo.getLeft();
				var.setName(ie.getName());
			}else{
				return new HiwiiException();
			}

			Definition def = null;
			if(bo.getRight() instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) bo.getRight();
				try {
					def = EntityUtil.proxyGetDefinition(ie.getName());
					if(def == null){
						return new HiwiiException();
					}
					var.setType(def.getName());
					var.setValue(new NullValue());
				} catch (Exception e) {
					return new HiwiiException();
				}
			}else if(bo.getRight() instanceof IdentifierBrace){
				IdentifierBrace ib = (IdentifierBrace) bo.getRight();
				try {
					def = EntityUtil.proxyGetDefinition(ib.getName());
					if(def == null){
						return new HiwiiException();
					}
					var.setType(def.getName());
				} catch (Exception e) {
					return new HiwiiException();
				}
			}
			vars.put(var.getName(), var);
		}else{
			return new HiwiiException();
		}		
		return new NormalEnd();
	}
	
	public Expression newVariable(Expression expr, Expression target){
		Variable var = new Variable();
		
		
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			var = new Variable();
			var.setName(ie.getName());
			var.setValue(new NullValue());
		}else {
			return new HiwiiException();
		}
		

		Definition def = null;
		if(target instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression)target;
			try {
				def = EntityUtil.proxyGetDefinition(ie.getName());
				if(def == null){
					return new HiwiiException();
				}
				
			} catch (Exception e) {
				return new HiwiiException();
			}
		}else if(target instanceof IdentifierBrace){
			IdentifierBrace ib = (IdentifierBrace) target;
			try {
				def = EntityUtil.proxyGetDefinition(ib.getName());
				if(def == null){
					return new HiwiiException();
				}
			} catch (Exception e) {
				return new HiwiiException();
			}
		}

		var.setType(def.getName());
		vars.put(var.getName(), var);
//	
//		if(bo.getOperator().equals(":=")){}else if(bo.getOperator().equals("->")){
//			if(bo.getLeft() instanceof IdentifierExpression){
//				IdentifierExpression ie = (IdentifierExpression) bo.getLeft();
//				var.setName(ie.getName());
//			}else{
//				return new HiwiiException();
//			}
//
//			Definition def = null;
//			if(bo.getRight() instanceof IdentifierExpression){
//				IdentifierExpression ie = (IdentifierExpression) bo.getRight();
//				try {
//					def = EntityUtil.proxyGetDefinition(ie.getName());
//					if(def == null){
//						return new HiwiiException();
//					}
//					var.setType(def.getName());
//					var.setValue(new NullValue());
//				} catch (Exception e) {
//					return new HiwiiException();
//				}
//			}else if(bo.getRight() instanceof IdentifierBrace){
//				IdentifierBrace ib = (IdentifierBrace) bo.getRight();
//				try {
//					def = EntityUtil.proxyGetDefinition(ib.getName());
//					if(def == null){
//						return new HiwiiException();
//					}
//					var.setType(def.getName());
//				} catch (Exception e) {
//					return new HiwiiException();
//				}
//			}
//			vars.put(var.getName(), var);
//		}else{
//			return new HiwiiException();
//		}		
		return new NormalEnd();
	}
	
	public Expression persistVariable(Expression expr){
		Variable var = new Variable();
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			var = new Variable();
			var.setName(ie.getName());
			var.setValue(new NullValue());
			vars.put(ie.getName(), var);
			return new NormalEnd();
		}
		//TODO function variable;
		if(!(expr instanceof BinaryOperation)){
			return new HiwiiException();
		}
		BinaryOperation bo = (BinaryOperation) expr;
		if(bo.getOperator().equals(":=")){
			Entity value = doCalculation(bo.getRight());
			if(value instanceof HiwiiException){
				return (HiwiiException)value;
			}

			Expression left = bo.getLeft();
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				var.setName(ie.getName());
				var.setValue(value);
			}else if(left instanceof BinaryOperation){
				BinaryOperation form = (BinaryOperation) left;
				if(!form.getOperator().equals("->")){
					return new HiwiiException();
				}
				if(form.getLeft() instanceof IdentifierExpression){
					IdentifierExpression ie = (IdentifierExpression) form.getLeft();
					var.setName(ie.getName());
				}else{
					return new HiwiiException();
				}

				Definition def = null;
				if(form.getRight() instanceof IdentifierExpression){
					IdentifierExpression ie = (IdentifierExpression) form.getRight();
					try {
						def = EntityUtil.proxyGetDefinition(ie.getName());
						if(def == null){
							return new HiwiiException();
						}
						if(!EntityUtil.judgeEntityIsDefinition(value, def)){
							return new HiwiiException();
						}
						var.setType(def.getName());
						var.setValue(value);
					} catch (Exception e) {
						return new HiwiiException();
					}
				}else if(form.getRight() instanceof IdentifierBrace){
					IdentifierBrace ib = (IdentifierBrace) form.getRight();
					try {
						def = EntityUtil.proxyGetDefinition(ib.getName());
						if(def == null){
							return new HiwiiException();
						}
						var.setType(def.getName());
					} catch (Exception e) {
						return new HiwiiException();
					}
				}				
			}
		}else if(bo.getOperator().equals("->")){
			if(bo.getLeft() instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) bo.getLeft();
				var.setName(ie.getName());
			}else{
				return new HiwiiException();
			}

			Definition def = null;
			if(bo.getRight() instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) bo.getRight();
				try {
					def = EntityUtil.proxyGetDefinition(ie.getName());
					if(def == null){
						return new HiwiiException();
					}
					var.setType(def.getName());
					var.setValue(new NullValue());
				} catch (Exception e) {
					return new HiwiiException();
				}
			}else if(bo.getRight() instanceof IdentifierBrace){
				IdentifierBrace ib = (IdentifierBrace) bo.getRight();
				try {
					def = EntityUtil.proxyGetDefinition(ib.getName());
					if(def == null){
						return new HiwiiException();
					}
					var.setType(def.getName());
				} catch (Exception e) {
					return new HiwiiException();
				}
			}	
		}else{
			return new HiwiiException();
		}
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Transaction txn = null;
		try {
			txn = db.beginTransaction();
			VariableStore vs = EntityUtil.variableToStore(var);
			db.putVariable(var.getName(), vs, null);
			txn.commit();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}finally{
			if (txn != null) {
				txn.abort();
				txn = null;
			}
		}

		return new NormalEnd();
	}

	public Expression defineVariable(Expression source, Expression expr){
		String name = null;
		if(source instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) source;
			name = ie.getName();
		}else{
			return new HiwiiException();
		}
		Variable var = new Variable();
		var.setName(name);
		var.setValue(new NullValue()); //暂时不允许设初始值

		//TODO function variable;
		Definition def = null;
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			try {
				def = EntityUtil.proxyGetDefinition(ie.getName());
				if(def == null){
					return new HiwiiException();
				}
				var.setType(def.getName());				
			} catch (Exception e) {
				return new HiwiiException();
			}
		}else if(expr instanceof IdentifierBrace){
			IdentifierBrace ib = (IdentifierBrace)expr;
			try {
				def = EntityUtil.proxyGetDefinition(ib.getName());
				if(def == null){
					return new HiwiiException();
				}
				var.setType(def.getName());
			} catch (Exception e) {
				return new HiwiiException();
			}
		}
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Transaction txn = null;
		try {
			txn = db.beginTransaction();
			VariableStore vs = EntityUtil.variableToStore(var);
			db.putVariable(var.getName(), vs, null);
			txn.commit();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}finally{
			if (txn != null) {
				txn.abort();
				txn = null;
			}
		}

		return new NormalEnd();
	}
	
	public Expression defineReference(Expression expr, Entity target){
		if(!(expr instanceof IdentifierExpression)){
			return new HiwiiException();
		}
		
		IdentifierExpression ie = (IdentifierExpression) expr;
		String name = ie.getName();
		if(refers.containsKey(name)){
			return new HiwiiException();
		}
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Transaction txn = null;
		try {
			txn = db.beginTransaction();			
			db.putReference(name, target, txn);
			txn.commit();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}finally{
			if (txn != null) {
				txn.abort();
				txn = null;
			}
		}
		return new NormalEnd();
	}
	
	public Expression doRefer(Expression left, Expression right){
		if(!(left instanceof IdentifierExpression)) {
			return new HiwiiException();
		}
		IdentifierExpression ie = (IdentifierExpression) left;
		String name = ie.getName();

		if(refers.containsKey(name)){
			return new HiwiiException();
		}
		Entity result = doCalculation(right);
		if(result == null || result instanceof HiwiiException){
			return new HiwiiException();
		}
		refers.put(name, result);
		return new NormalEnd();
	}
	
	/**
	 * refer[x=4]
	 * 是等于的简单应用形式，左侧必须是identifier，右侧必须是一个可以计算的表达式。
	 * =表示等式是不能随意改变的。
	 * @param from
	 * @param expr
	 * @return
	 */
	public Expression doRefer(Expression expr){
		Expression left = null;
		Expression right = null;
		String name = null;

		if(!(expr instanceof BinaryOperation)){
			return new HiwiiException();
		}
		BinaryOperation bo = (BinaryOperation) expr;
		if(!bo.getOperator().equals(":=")){
			return new HiwiiException();
		}
		left = bo.getLeft();
		right = bo.getRight();

		if(!(left instanceof IdentifierExpression)){
			return new HiwiiException();
		}
		IdentifierExpression ie = (IdentifierExpression) left;
		name = ie.getName();
		if(refers.containsKey(name)){
			return new HiwiiException();
		}
		Entity result = doCalculation(right);
		if(result == null || result instanceof HiwiiException){
			return new HiwiiException();
		}
		refers.put(name, result);
		return new NormalEnd();
	}
	
	public Expression constantDescribe(List<Expression> list){
		for(Expression expr:list){
			if(!(expr instanceof BinaryOperation)){
				return new HiwiiException();
			}
			BinaryOperation bo = (BinaryOperation) expr;
			Expression left = bo.getLeft();
			Expression right = bo.getRight();
			if(bo.getOperator().equals(":=")){
				return doAssign(left, right);
			}else if(bo.getOperator().equals("::")){
				return newBooleanST(expr);
			}else if(bo.getOperator().equals(":")){
				//expression describe
				return newExpressionST(expr);
			}else{
				return new HiwiiException();
			}
		}
		return new NormalEnd();
	}
	public Expression newBoolean(Expression expr){
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;			
			bools.put(ie.getName(), null);
			return new NormalEnd();
		}
		//TODO function variable;
		if(!(expr instanceof BinaryOperation)){
			return new HiwiiException();
		}
		BinaryOperation bo = (BinaryOperation) expr;
		if(bo.getOperator().equals("::")){
			Expression value = doDecision(bo.getRight());
			if(value instanceof HiwiiException){
				return (HiwiiException)value;
			}
			if(value instanceof NullValue){
				return value;
			}
			
			JudgmentResult result = (JudgmentResult) value;

			Expression left = bo.getLeft();
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				bools.put(ie.getName(), result);
			}else{
				return new HiwiiException();
			}
			return new NormalEnd();
		}else{
			return new HiwiiException();
		}
	}
	
	/**
	 * 不允许null值静态变量
	 * @param expr
	 * @return
	 */
	public Expression newBooleanST(Expression expr){
		if(!(expr instanceof BinaryOperation)){
			return new HiwiiException();
		}
		BinaryOperation bo = (BinaryOperation) expr;
		Expression value = doDecision(bo.getRight());
		if(value == null){
			return new HiwiiException("null value!");
		}
		if(value instanceof HiwiiException){
			return (HiwiiException)value;
		}
		if(value instanceof NullValue){
			return value;
		}
		
		JudgmentResult result = (JudgmentResult) value;
		
		if(bo.getOperator().equals("::")){
			Expression left = bo.getLeft();
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				boolST.put(ie.getName(), result);
			}else{
				return new HiwiiException();
			}
			return new NormalEnd();
		}else{
			return new HiwiiException();
		}
	}
	
	public Expression newExpression(Expression expr){
		if(!(expr instanceof BinaryOperation)){
			return new HiwiiException();
		}
		BinaryOperation bo = (BinaryOperation) expr;
		if(!bo.getOperator().equals(":")){
			return new HiwiiException();
		}
		Expression left = bo.getLeft();
		Expression right = bo.getRight();
		if(!(left instanceof IdentifierExpression)){
			return new HiwiiException();
		}
		IdentifierExpression ie = (IdentifierExpression) left;
		LambdaMapping le = new LambdaMapping();
		if(right instanceof BinaryOperation){
			BinaryOperation lambda = (BinaryOperation) right;
			if(lambda.getOperator().equals("=>")){
				if(lambda.getLeft() instanceof IdentifierExpression){
					IdentifierExpression arg = (IdentifierExpression) left;					
					List<String> keys = Arrays.asList(arg.getName());
					le.setKeys(keys);
					le.setStatement(lambda.getRight());
					if(expressions.containsKey(ie.getName())){
						return new HiwiiException();
					}
					expressions.put(ie.getName(), lambda);
				}else if(lambda.getLeft() instanceof BracketExpression){
					BracketExpression be = (BracketExpression) lambda.getLeft();
					List<String> args = EntityUtil.parseArgumentString(be.getArray());
					le.setKeys(args);
					le.setStatement(lambda.getRight());
					if(expressions.containsKey(ie.getName())){
						return new HiwiiException();
					}
					expressions.put(ie.getName(), lambda);
				}else{
					return new HiwiiException();
				}
				return new NormalEnd();
			}else{
				return new HiwiiException();
			}
		}else{
			expressions.put(ie.getName(), right);
		}
		return new NormalEnd();
	}
	
	public Expression newExpressionST(Expression expr){
		if(!(expr instanceof BinaryOperation)){
			return new HiwiiException();
		}
		BinaryOperation bo = (BinaryOperation) expr;
		if(bo.getOperator().equals("=>")){
			Expression left = bo.getLeft();
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				LambdaMapping le = new LambdaMapping();
				le.getKeys().add(ie.getName());
				expressionST.put(ie.getName(), bo.getRight());
			}else{
				return new HiwiiException();
			}
			return new NormalEnd();
		}else{
			return new HiwiiException();
		}
	}

	public Expression doAssign(Expression left, Expression right){
		Entity value = doCalculation(right);
		if(value == null){
			return new HiwiiException();
		}
		if(value instanceof HiwiiException){
			return (HiwiiException)value;
		}
		String name = "";
		if(left instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) left;
			name = ie.getName();
			Variable var = hasVariable(name);
			if(var != null){
				try {
					if(!var.doAccept(value)){
						return new HiwiiException();
					}
				} catch (DatabaseException e) {
					return new HiwiiException();
				} catch (IOException e) {
					return new HiwiiException();
				} catch (ApplicationException e) {
					return new HiwiiException();
				} catch (Exception e) {
					return new HiwiiException();
				}
				var.setValue(value);
				return new NormalEnd();
			}
			if(value instanceof HiwiiException){
				return (HiwiiException)value;
			}
		}else if(left instanceof SubjectOperation){
			SubjectOperation sv = (SubjectOperation) left;
//			Expression sub = sv.getSubject();
			Expression exp = sv.getAction();
			
			if(exp instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) exp;
				name = ie.getName();
			}
			
			Entity subject = doCalculation(sv.getSubject());
			if(subject == null){
				return new HiwiiException();
			}
			return subject.doAssign(name, value);
		}else{
			return new HiwiiException();
		}

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.updateVariable(name, value, null);
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}

		return new NormalEnd();
	}
	
	public Expression doAssignSilent(Expression left, Expression right){
		Entity value = doCalculation(right);
		if(value == null){
			return new HiwiiException();
		}
		if(value instanceof HiwiiException){
			return (HiwiiException)value;
		}
		String name = "";
		if(left instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) left;
			name = ie.getName();
			Variable var = hasVariable(name);
			if(var == null){
				return new HiwiiException();
//				var.setValue(value);
			}
			try {
				if(!var.doAccept(value)){
					return new HiwiiException();
				}
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				return new HiwiiException();
			}
			var.setValue(value);
		}else{
			return new HiwiiException();
		}

		return new NormalEnd();
	}

	public Expression doInstanceAssign(HiwiiInstance subject, Expression left, Expression right){
		Entity value = doCalculation(subject, right);
		if(value == null){
			return new HiwiiException();
		}
		if(value instanceof HiwiiException){
			return (HiwiiException)value;
		}

		if(left instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) left;			
			return subject.setProperty(ie.getName(), value);
		}else{
			return new HiwiiException();
		}
	}
	
	public Expression doDefinitionAssign(Definition def, Expression left, Expression right){
		Entity value = doCalculation(right);
		if(value == null){
			return new HiwiiException();
		}
		if(value instanceof HiwiiException){
			return (HiwiiException)value;
		}

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				Assignment ass = null; //def.getAssignments().get(ie.getName());
				if(ass == null){
					ass = new Assignment();
					String pkey = ie.getName() + "#" + def.getSignature();
					Property prop = EntityUtil.proxyGetProperty(ie.getName(), def);
					if(prop == null){
						return new HiwiiException();
					}
					if(!EntityUtil.judgeValueToProperty(value, prop)){
						return new HiwiiException();
					}
					ass.setValue(value);
					ass.setName(ie.getName());
					db.putAssignment(pkey, ass, null);
//					def.getAssignments().put(ass.getName(), ass);
					return new NormalEnd();
				}else{
					Property prop = EntityUtil.proxyGetProperty(ie.getName(), def);
					if(!EntityUtil.judgeValueToProperty(value, prop)){
						return new HiwiiException();
					}
					String pkey = ie.getName() + "#" + def.getSignature();
					ass.setValue(value);
					db.putAssignment(pkey, ass, null);				
					return new NormalEnd();
				}
			}else{
				return new HiwiiException();
			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}
	}
	
	public Expression doInstanceJudge(HiwiiInstance subject, Expression left, Expression right){
		Expression value = doDecision(subject, right);
		if(value == null){
			return new HiwiiException();
		}
		if(value instanceof HiwiiException){
			return (HiwiiException)value;
		}
		if(!(value instanceof JudgmentResult)){
			return new HiwiiException();
		}		
		JudgmentResult result = (JudgmentResult) value;
		
		String name = null;
		if(left instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) left;			
			name = ie.getName();
		}else{
			return new HiwiiException();
		}
		
		return subject.doJudge(name, result);
	}
	
	public Expression doInstanceSwitch(HiwiiInstance subject, Expression left, Expression right){
		String name = null;
		if(right instanceof IdentifierExpression) {
			IdentifierExpression ie = (IdentifierExpression) right;
			name = ie.getName();
		}
		
		String key = null;
		if(left instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) left;			
			key = ie.getName();
		}else{
			return new HiwiiException();
		}
		
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
//			String pkey = name + "@" + subject.getUuid();
			db.putSwitchResult(subject, key, name, null);
			subject.getSwitches().put(key, name);
			return new NormalEnd();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new HiwiiException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new HiwiiException();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}
	}
	
	public Expression doDefinitionJudge(Definition def, Expression left, Expression right){
		Expression value = doDecision(right);
		if(value == null){
			return new HiwiiException();
		}
		if(value instanceof HiwiiException){
			return (HiwiiException)value;
		}
		if(!(value instanceof JudgmentResult)){
			return new HiwiiException();
		}
		
		JudgmentResult result = (JudgmentResult) value;
		String name = null;
		if(left instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) left;			
			name = ie.getName();
		}else{
			return new HiwiiException();
		}
		
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.putDefinitionJudgment(def, name, result, null);
			return new NormalEnd();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}
	}
	
	public Expression turnJudge(Expression left, Expression right){
		Expression value = doDecision(right);
		if(value instanceof HiwiiException){
			return (HiwiiException)value;
		}
		JudgmentResult result = (JudgmentResult) value;
		if(left instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) left;
			String name = ie.getName();
			for(RuntimeContext context:this.getLadder().getChains()){
				if(context.getBools().containsKey(name)){
					return context.getBools().put(name, result);
				}
			}
		}

		return new NormalEnd();
	}
	/**
	 * interface declaration
	 * format:new[Calculation_intf:identifier or function]
	 * temp:condition expression not allowed as interface, can only be set as implementation
	 * @param type
	 * @param expr
	 * @return
	 */
	public Expression doDeclareInterface(char type, Expression expr){
//		String name = null;
//		String num = null;
//		Declaration dec = null;
//		String key = null;
//
//		if(expr instanceof IdentifierExpression){
//			IdentifierExpression ie = (IdentifierExpression) expr;
//			name = ie.getName();
//			num = "0";
//			dec = new Declaration();
//			dec.setName(name);
//			dec.setStatement(null);
//			key = name;
//			calculs.put(key, dec);
//		}else if(expr instanceof FunctionExpression){
//			FunctionExpression fe = (FunctionExpression) expr;
//			name = fe.getName();
//			num = String.valueOf(fe.getArguments().size());
//			FunctionDeclaration fd = new FunctionDeclaration();
//			fd.setName(name);
//			key = name + "#" + num;
//			//			try {
//			//				List<String> args = EntityUtil.parseDeclaration(fe.getArguments());
//			//				fd.setArguments(args);
//			//			} catch (ApplicationException e) {
//			//				return new HiwiiException();//参数错误
//			//			}
//
//			fd.setStatement(null);
//			intf_fcalculs.put(key, fd);
//		}else{
//			return new HiwiiException();
//		}
		return new NormalEnd();
	}

	public Expression doDeclareImplement(char type, Expression expr){
//		Expression left = null;
//		Expression right = null;
//		String name = null;
//		String num = null;
//		Declaration dec = null;
//		String key = null;
//
//		if(expr instanceof BinaryOperation){
//			BinaryOperation bo = (BinaryOperation) expr;
//			if(!bo.getOperator().equals("=")){
//				return new HiwiiException();
//			}
//			left = bo.getLeft();
//			right = bo.getRight();
//
//			if(left instanceof IdentifierExpression){
//				IdentifierExpression ie = (IdentifierExpression) left;
//				name = ie.getName();
//				num = "0";
//				dec = new Declaration();
//				dec.setName(name);
//				dec.setStatement(right);
//				key = name;
//				calculs.put(key, dec);//TODO identifier implement
//			}else if(left instanceof FunctionExpression){
//				FunctionExpression fe = (FunctionExpression) left;
//				name = fe.getName();
//				num = String.valueOf(fe.getArguments().size());
//				FunctionDeclaration fd = new FunctionDeclaration();
//				fd.setName(name);
//				key = name + "#" + num;
//				//				try {
//				//					List<String> args = EntityUtil.parseDeclaration(fe.getArguments());
//				//					fd.setArguments(args);
//				//				} catch (ApplicationException e) {
//				//					return new HiwiiException();//参数错误
//				//				}
//
//				fd.setStatement(right);
//				impl_fcalculs.put(key, fd);
//			}else if(left instanceof ConditionExpression){
//				ConditionExpression ce = (ConditionExpression) left;
//				//only function expression allowed case conditionExpression.
//				Expression body = ce.getBody();
//				if(!(body instanceof FunctionExpression)){
//					return new HiwiiException();
//				}
//				FunctionExpression fe = (FunctionExpression) body;
//				name = fe.getName();
//				num = String.valueOf(fe.getArguments().size());
//				ConditionDeclaration fd = new ConditionDeclaration();
//				fd.setName(name);
//				key = name + "#" + num + "%" + EntityUtil.getUUID();
//				//				try {
//				//					List<String> args = EntityUtil.parseDeclaration(fe.getArguments());
//				//					fd.setArguments(args);
//				//				} catch (ApplicationException e) {
//				//					return new HiwiiException();//参数错误
//				//				}
//				fd.setConditions(ce.getConditions());
//				fd.setStatement(right);
//				impl_fcalculs.put(key, fd);
//			}else{
//				return new HiwiiException();
//			}
//		}else{
//			return new HiwiiException();
//		}

		return new NormalEnd();
	}

	/**
	 * new([operation|calculation|decision]@Definition:id/function@condition=expression)
	 * 计算机语言通常用function(type1 x, type2 y...)形式表示函数，
	 * 而数学中，通常用function(x, y...){x.belongTo[type1], y.belongTo[type2]}
	 * hiwii语言使用数学的方式表示函数。
	 * type表示抽象。抽象定义是一个特殊的名词。
	 * @param expr
	 * @return
	 */
	public Expression doDeclare(char type, Expression source, Expression expr){
		String name = null;

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			if(source instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) source;
				name = ie.getName();
				if(type == 'c'){
					db.putIdCalculation(name, expr.toString(), null);
				}else if(type == 'd'){

				}else{
					db.putIdAction(name, expr.toString(), null);
				}
			}else if(source instanceof FunctionExpression){
				FunctionExpression fe = (FunctionExpression) source;
				
				if(type == 'c'){
					db.putFunctionCalculation(fe, expr, null);
				}else if(type == 'd'){
//					db.putFunDecision(fd, null);
				}else{
//					db.putFunAction(fd, null);
				}
			}else if(source instanceof MappingExpression){

			}else if(source instanceof SubjectOperation){
				SubjectOperation so = (SubjectOperation) source;
				if(!(so.getSubject() instanceof IdentifierExpression)){
					//不允许有修饰出现,someObject do f1(), another do f2().那么有不同的definition
					return new HiwiiException();
				}
				IdentifierExpression id = (IdentifierExpression) so.getSubject();
				Definition def = EntityUtil.proxyGetDefinition(id.getName());
				if(def != null){
					if(so.getAction() instanceof IdentifierExpression){
						IdentifierExpression ie = (IdentifierExpression) so.getAction();
						Declaration dec = new Declaration();
						dec.setName(ie.getName());
						dec.setStatement(expr);
						if(type == 'c'){
							db.putIdCalculation(name, expr.toString(), null);
						}else if(type == 'd'){

						}else{

						}
					}else if(so.getAction() instanceof FunctionExpression){
						FunctionExpression fe = (FunctionExpression) so.getAction();
						name = fe.getName();
						FunctionDeclaration fd = new FunctionDeclaration();
//						fd.setName(name);
						try {
							List<Argument> args = EntityUtil.parseArguments(fe.getArguments());
//							fd.setArguments(args);
						} catch (ApplicationException e) {
							return new HiwiiException();//参数错误
						}

						fd.setStatement(expr);
//						if(type == 'c'){
//							db.putFunCalculation(fd, null);
//						}else if(type == 'd'){
//							db.putFunDecision(fd, null);
//						}else{
//							db.putFunAction(fd, null);
//						}
					}
				}
				
			}else{
				return new HiwiiException();
			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			e.printStackTrace();
			return new HiwiiException();
		} 

		return new NormalEnd();
	}
	
	public Expression doDeclare(char type, Expression expr){
		Expression left = null;
		Expression right = null;
		String name = null;
//		String num = null;
//		Declaration dec = null;
//		String key = null;

		if(!(expr instanceof BinaryOperation)){
			return new HiwiiException();
		}

		BinaryOperation bo = (BinaryOperation) expr;
		if(!bo.getOperator().equals("=")){
			return new HiwiiException();
		}
		left = bo.getLeft();
		right = bo.getRight();

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				name = ie.getName();
				if(type == 'c'){
					db.putIdCalculation(name, right.toString(), null);
				}else if(type == 'd'){

				}else{

				}
			}else if(left instanceof FunctionExpression){
				FunctionExpression fe = (FunctionExpression) left;
				name = fe.getName();
				FunctionDeclaration fd = new FunctionDeclaration();
//				fd.setName(name);
				try {
					List<Argument> args = EntityUtil.parseArguments(fe.getArguments());
//					fd.setArguments(args);
				} catch (ApplicationException e) {
					return new HiwiiException();//参数错误
				}

				fd.setStatement(right);
				if(type == 'c'){
//					db.putFunCalculation(fd, null);
				}else if(type == 'd'){
//					db.putFunDecision(fd, null);
				}else{

				}
			}else if(left instanceof MappingExpression){

			}else if(left instanceof SubjectOperation){
				SubjectOperation so = (SubjectOperation) left;
				if(!(so.getSubject() instanceof IdentifierExpression)){
					//不允许有修饰出现,someObject do f1(), another do f2().那么有不同的definition
					return new HiwiiException();
				}
				IdentifierExpression id = (IdentifierExpression) so.getSubject();
				Definition def = EntityUtil.proxyGetDefinition(id.getName());
				if(def != null){
					if(so.getAction() instanceof IdentifierExpression){
						IdentifierExpression ie = (IdentifierExpression) so.getAction();
						Declaration dec = new Declaration();
						dec.setName(ie.getName());
						dec.setStatement(right);
						if(type == 'c'){
							db.putIdCalculation(name, right.toString(), null);
						}else if(type == 'd'){

						}else{

						}
					}else if(so.getAction() instanceof FunctionExpression){
						FunctionExpression fe = (FunctionExpression) so.getAction();
						name = fe.getName();
						FunctionDeclaration fd = new FunctionDeclaration();
//						fd.setName(name);
						try {
							List<Argument> args = EntityUtil.parseArguments(fe.getArguments());
//							fd.setArguments(args);
						} catch (ApplicationException e) {
							return new HiwiiException();//参数错误
						}

						fd.setStatement(right);
						if(type == 'c'){
//							db.putFunCalculation(fd, null);
						}else if(type == 'd'){
//							db.putFunDecision(fd, null);
						}else{
//							db.putFunAction(fd, null);
						}
					}
				}
				
			}else{
				return new HiwiiException();
			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		} 

		return new NormalEnd();
	}
	
	public Expression unDeclare(char type, Expression expr){
		String name = null;
//		String num = null;
//		Declaration dec = null;
//		String key = null;


		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Transaction txn = null;
		try {
			txn = db.beginTransaction();
			if(expr instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) expr;
				name = ie.getName();
				if(type == 'c'){
//					db.putIdCalculation(name, right.toString(), null);
				}else if(type == 'd'){

				}else{

				}
			}else if(expr instanceof FunctionExpression){
				FunctionExpression fe = (FunctionExpression) expr;
				name = fe.getName();
				FunctionDeclaration fd = new FunctionDeclaration();
//				fd.setName(name);
				try {
					List<Argument> args = EntityUtil.parseArguments(fe.getArguments());
//					fd.setArguments(args);
				} catch (ApplicationException e) {
					return new HiwiiException();//参数错误
				}

//				fd.setStatement(right);
				if(type == 'c'){
					db.dropFunCalculation(fd, txn);
				}else if(type == 'd'){
//					db.putFunDecision(fd, null);
				}else{
//					db.dropFunAction(fd, txn);
				}
			}else if(expr instanceof MappingExpression){

			}else if(expr instanceof SubjectOperation){
				SubjectOperation so = (SubjectOperation) expr;
				if(!(so.getSubject() instanceof IdentifierExpression)){
					//不允许有修饰出现,someObject do f1(), another do f2().那么有不同的definition
					return new HiwiiException();
				}
				IdentifierExpression id = (IdentifierExpression) so.getSubject();
				Definition def = EntityUtil.proxyGetDefinition(id.getName());
				if(def != null){
					if(so.getAction() instanceof IdentifierExpression){
						IdentifierExpression ie = (IdentifierExpression) so.getAction();
						Declaration dec = new Declaration();
						dec.setName(ie.getName());
//						dec.setStatement(right);
						if(type == 'c'){
//							db.putIdCalculation(name, right.toString(), null);
						}else if(type == 'd'){

						}else{

						}
					}else if(so.getAction() instanceof FunctionExpression){
						FunctionExpression fe = (FunctionExpression) so.getAction();
						name = fe.getName();
						FunctionDeclaration fd = new FunctionDeclaration();
//						fd.setName(name);
						try {
							List<Argument> args = EntityUtil.parseArguments(fe.getArguments());
//							fd.setArguments(args);
						} catch (ApplicationException e) {
							return new HiwiiException();//参数错误
						}

//						fd.setStatement(right);
						if(type == 'c'){
//							db.putFunCalculation(fd, null);
						}else if(type == 'd'){
//							db.putFunDecision(fd, null);
						}else{
//							db.putFunAction(fd, null);
						}
					}
				}
				
			}else{
				return new HiwiiException();
			}
			txn.commit();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		} finally{
			if (txn != null) {
				txn.abort();
				txn = null;
			}
		}

		return new NormalEnd();
	}
	
	public Expression hasDeclared(char type, Expression expr){
		if(!(expr instanceof IdentifierExpression)){
			return new HiwiiException();
		}

		IdentifierExpression ie = (IdentifierExpression) expr;

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			if(type == 'c'){
				boolean ret = db.hasCalculation(ie.getName(), null);
				return EntityUtil.decide(ret);
			}else if(type == 'd'){
//				db.putFunDecision(fd, null);
			}else{
//				db.putFunAction(fd, null);
			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		} 

		return new NormalEnd();
	}

	public Expression doDeclare(Definition def, char type, Expression expr){
		Expression left = null;
		Expression right = null;
		String name = null;
//		String num = null;
//		Declaration dec = null;
//		String key = null;

		if(!(expr instanceof BinaryOperation)){
			return new HiwiiException();
		}

		BinaryOperation bo = (BinaryOperation) expr;
		if(!bo.getOperator().equals("=")){
			return new HiwiiException();
		}
		left = bo.getLeft();
		right = bo.getRight();

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				name = ie.getName();
				if(type == 'c'){
					db.putIdCalculation(def, name, right.toString(), null);
				}else if(type == 'd'){
					db.putIdDecision(def, name, right.toString(), null);
				}else{
					db.putIdAction(def, name, right.toString(), null);
				}
			}else if(left instanceof FunctionExpression){
				FunctionExpression fe = (FunctionExpression) left;
				name = fe.getName();
				FunctionDeclaration fd = new FunctionDeclaration();
//				fd.setName(name);
				try {
					List<Argument> args = EntityUtil.parseArguments(fe.getArguments());
//					fd.setArguments(args);
				} catch (ApplicationException e) {
					return new HiwiiException();//参数错误
				}

				fd.setStatement(right);
				if(type == 'c'){
//					db.putFunCalculation(def, fd, null);
				}else if(type == 'd'){
//					db.putFunDecision(def, fd, null);
				}else{
//					db.putFunAction(def, fd, null);
				}
			}else if(left instanceof MappingExpression){

			}else{
				return new HiwiiException();
			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		} 

		return new NormalEnd();
	}

	/**
	 * Property:name<-type{}
	 * @param def
	 * @param expr
	 * @return
	 */
	public Expression doDefinitionProperty(Definition def, Expression expr){
		if(!(expr instanceof BinaryOperation)){
			return new HiwiiException();
		}
		BinaryOperation bo = (BinaryOperation) expr;
		if(!bo.getOperator().equals("->")){
			return new HiwiiException();
		}
		List<Expression> limits = null;
		Definition type = null;
		Expression right = bo.getRight();
		if(right instanceof IdentifierBrace){
			IdentifierBrace ib = (IdentifierBrace) right;			
			String name = ib.getName();
			type = hasDefinition(name);
			if(type == null){
				return new HiwiiException();
			}
			limits = ib.getConditions();
		}

		Expression left = bo.getLeft();
		Property prop = null;
		if(left instanceof IdentifierExpression){
			prop = new Property();
			prop.setName(((IdentifierExpression) left).getName());
		}
		//		else if(left instanceof FunctionExpression){
		//			prop = new FunctionProperty();
		//			prop.setName(((FunctionExpression) left).getName());
		//		}
		else{
			return new HiwiiException();
		}

		props.put(prop.getName(), prop);

		return new NormalEnd();
	}

	/**
	 * to describe definition or entity
	 * @return
	 */
	public Expression doObjectCognition(Definition def, List<Expression> states){
		for(Expression expr:states){
			if(expr instanceof BinaryOperation){
				BinaryOperation bo = (BinaryOperation) expr;
				Expression left = bo.getLeft();
				Expression right = bo.getRight();
				if(bo.getOperator().equals(":")){
					if(!(left instanceof IdentifierExpression)){
						return new HiwiiException();
					}
					IdentifierExpression ie = (IdentifierExpression) left;
					String name = ie.getName();
					if(name.equals("Property")){
						Expression ret = doDefinitionProperty(def, right);
						if(ret instanceof HiwiiException){
							return ret;
						}
					}
				}
			}
		}
		return new NormalEnd();
	}

	public Expression doEntityIsDefinition(Entity subject, Entity target){
		if(target instanceof ExtendedDefinition){
			return new NullExpression();//extend无法精确判断，不知道,TODO从定义判断
		}
		String sig1 = subject.getClassName();
		if(target instanceof SimpleDefinition){
			SimpleDefinition sd = (SimpleDefinition) target;
			return SystemDefinition.doIsPositive(sig1, sd.getName());
		}

		return null;
	}

	public Expression doEntityIsAbstraction(Entity subject, Entity target){
		if(target instanceof ExtendedDefinition){
			return new NullExpression();//extend无法精确判断，不知道,TODO从定义判断
		}
		String sig1 = subject.getClassName();
		if(target instanceof SimpleDefinition){
			SimpleDefinition sd = (SimpleDefinition) target;
			return SystemDefinition.doIsPositive(sig1, sd.getName());
		}

		return null;
	}

	/**
	 * global decision
	 * involved in context statement.
	 * 用于函数判断
	 * @param subject
	 * @param target
	 * @return
	 */
	public Expression doEntitiesIsDefinition(List<Entity> subjects, List<Entity> defs){
		if(subjects.size() != defs.size()){
			return null;
		}
		for(int i=0;i<subjects.size();i++){
			Expression ret = doEntityIsDefinition(subjects.get(i), defs.get(i));
			if(ret instanceof JudgmentResult){
				if(!EntityUtil.judge(ret)){
					return EntityUtil.decide(false);
				}
			}else{
				return ret;
			}
		}
		return EntityUtil.decide(true);
	}

	public String getKey(Entity from, String name){
		//from is not null
		String key = null;

		String nt = "#";

		if(from instanceof Definition){
			Definition def = (Definition) from;
			key = name  + nt + def.takeSignature();
		}else{
			if(from instanceof LocalHost){
				//				key = name  + nt + SystemDefinition.getIdentifier("LocalHost");
			}else{
				String dd = from.getClassName();
				Entity ret = doIdentifierCalculation(dd);
				if(!(ret instanceof Definition)){
					return null;
				}
				Definition def = (Definition) ret;//contextGetDefinition(dd);
				//赋值key=signature + ":" + entity.toString
				key = name  + nt + def.takeSignature() + ":" + from.toString();
			}
		}
		return key;
	}

	public String getKey(Entity from, String name, String num){
		//from is not null
		String key = null;

		String nt = "#";
		if(!num.equals("0")){
			nt = nt + num + "#";
		}
		if(from instanceof Definition){
			Definition def = (Definition) from;
			key = name  + nt + def.takeSignature();
		}else{
			if(from instanceof LocalHost){
				//				key = name  + nt + SystemDefinition.getIdentifier("LocalHost");
			}else{
				String dd = from.getClassName();
				Entity ret = doIdentifierCalculation(dd);
				if(!(ret instanceof Definition)){
					return null;
				}
				Definition def = (Definition) ret;//contextGetDefinition(dd);
				//赋值key=signature + ":" + entity.toString
				key = name  + nt + def.takeSignature() + ":" + from.toString();
			}
		}
		return key;
	}

	/**
	 * 口令必须是字母开始+字母/数字
	 * 或者纯数字。
	 * @param expr
	 * @return
	 */
	public Expression login(Expression arg0, Expression arg1){
		User logUser =  getLadder().getSessionContext().getSession().getUser();
		if(logUser != null) {
			return new HiwiiException("logined already!");
		}
		if(!(arg1 instanceof StringExpression)){
			return new HiwiiException();
		}
		StringExpression se = (StringExpression) arg1;
		String password = se.getValue();
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		String userid = null;
		if(arg0 instanceof StringExpression){
			StringExpression uid = (StringExpression) arg0;
			userid = uid.getValue();
		}else if(arg0 instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) arg0;
			userid = ie.getName();
		}
		try {
			User user = db.getUser(userid, null);
			if(user == null){
				return new HiwiiException();
			}
			if(user.getPassword().equals(password)){
				getLadder().getSessionContext().getSession().setUser(user);
				return new NormalEnd();
			}else{
				return new HiwiiException();
			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}
	}
	
	public Expression register(Entity user, Entity passwd){
		if(!(user instanceof StringExpression)){
			return new HiwiiException();
		}
		if(!(passwd instanceof StringExpression)){
			return new HiwiiException();
		}
		StringExpression str0 = (StringExpression) user;
		StringExpression str1 = (StringExpression) passwd;
		List<Expression> args = Arrays.asList(new IdentifierExpression("IdentifierPattern"));
		Expression ret = doMappingDecision(str0, "in", args);
		
		if(!EntityUtil.judge(ret)){
			return new HiwiiException();
		}
		
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Transaction txn = null;
		try {
			User user0 = new User();
			user0.setUserid(str0.getValue());
			user0.setPassword(str1.getValue());
			txn = db.beginTransaction();
			db.putUser(user0, txn);
			txn.commit();
			return new NormalEnd();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		} finally{
			if (txn != null) {
				txn.abort();
				txn = null;
			}
		}
//		return new HiwiiException();
	}
	
	public Expression logout(){
		getLadder().getSessionContext().getSession().setUser(null);
		return new NormalEnd();
	}

	public Expression closeSpace(Expression expr){
		return new NormalEnd();
	}
	/**
	 * 方法默认是开放的，lock使方法成为封闭状态。
	 * 开放的方法不需要访问权限，而封闭的方法需要访问权限才能访问。
	 * @param expr
	 * @return
	 */
	public Expression lock(Expression expr){
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			return lockIdAction(ie.getName());
		}else {
			return new HiwiiException();
		}
//		if(!(expr instanceof BinaryOperation)){
//			return new HiwiiException();
//		}
//		BinaryOperation bo = (BinaryOperation) expr;
//		if(!bo.getOperator().equals(":")){
//			return new HiwiiException();
//		}
//		Expression left = bo.getLeft();
//		Expression right = bo.getRight();
//		if(left instanceof IdentifierExpression){
//			IdentifierExpression ie = (IdentifierExpression) left;
//			if(ie.getName().equals("Calculation")){
//				return lockCalculation(right);
//			}else if(ie.getName().equals("Action")){
//				return lockAction(right);
//			}else{
//				return new HiwiiException();
//			}
//		}else{
//			return new HiwiiException();
//		}
	}

	public Expression lockCalculation(Expression expr){
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			return lockIdCalculation(ie.getName());
		}
		return new NormalEnd();
	}

//	public Expression lockAction(Expression expr){
//		if(expr instanceof IdentifierExpression){
//			IdentifierExpression ie = (IdentifierExpression) expr;
//			return lockIdAction(ie.getName());
//		}
//		return new NormalEnd();
//	}
	public Expression lockIdCalculation(String name){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.lockIdCalculation(name, null);
			return new NormalEnd();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		} 
	}

	public Expression lockIdAction(String name){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.lockIdAction(name, null);
			return new NormalEnd();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		} 
	}

	/**
	 * whether[locked[expression]]
	 * @param expr
	 * @return
	 */
	public Expression locked(Expression expr){
		if(!(expr instanceof BinaryOperation)){
			return new HiwiiException();
		}
		BinaryOperation bo = (BinaryOperation) expr;
		if(!bo.getOperator().equals(":")){
			return new HiwiiException();
		}
		Expression left = bo.getLeft();
		Expression right = bo.getRight();
		if(left instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) left;
			if(ie.getName().equals("Calculation")){
				return lockCalculation(right);
			}else if(ie.getName().equals("Action")){
				return lockedAction(right);
			}else{
				return new HiwiiException();
			}
		}else{
			return new HiwiiException();
		}
		//		return new NormalEnd();
	}

	public Expression lockedAction(Expression expr){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			if(expr instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) expr;
				boolean ret = db.lockedIdAction(ie.getName(), null);
				return EntityUtil.decide(ret);
			}
			return new NormalEnd();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		} 
	}

	public Expression lockedIdentifierCalculation(String name){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			boolean lock = db.lockedIdCalculation(name, null);
			return EntityUtil.decide(lock);
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		} 
	}

	public Expression lockedIdentifierAction(String name){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			boolean lock = db.lockedIdAction(name, null);
			return EntityUtil.decide(lock);
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		} 
	}
	/**
	 * grant privilege
	 * grant[user/group/role, action]
	 * argument1格式：
	 * 1，id，表示userId
	 * 2，[id1,id2,..]多个useId
	 * 3，group:gid, group:[gid1,gid2,..]
	 * 4，role,role@group,role@[gid1,gid2],[roleId1,roleId2]@[gid1,gid2]
	 * action:
	 * 1、action可以不加前缀。
	 * 2、action包括calculation和decision
	 * 3、action只包括identifier，function和mapping都只验证identifier。
	 * 4、action可以是regular形式。
	 * 
	 * @param expr
	 * @return
	 */
	public Expression grant(Expression user, Expression expr){
		List<String> users = new ArrayList<String>();
		if(user instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) user;
			users.add(ie.getName());
		}
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			if(expr instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) expr;
				for(String userid:users){
					db.putIdentifierAction(ie.getName(), userid, null);
				}
			}else {
				//only identifier expression for permit
				return new HiwiiException();
			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		} 
		return new NormalEnd();
	}

	/**
	 * grant privilege
	 * grant[user/group/role, action]
	 * argument1格式：
	 * 1，id，表示userId
	 * 2，[id1,id2,..]多个useId
	 * 3，group:gid, group:[gid1,gid2,..]
	 * 4，role,role@group,role@[gid1,gid2],[roleId1,roleId2]@[gid1,gid2]
	 * 
	 * @param expr
	 * @return
	 */
	public Expression grant(Definition def, Expression user, Expression expr){
		List<String> users = new ArrayList<String>();
		if(user instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) user;
			users.add(ie.getName());
		}
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			if(expr instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) expr;
				for(String userid:users){
					db.putIdentifierAction(def, ie.getName(), userid, null);
				}
			}else{
				return new HiwiiException();
			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		} 
		return new NormalEnd();
	}

	
	public Expression grantCalculation(char gid, String id, Expression expr){
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			return grantIdentifierCalculation(gid, id, ie.getName());
		}else{
			return new HiwiiException();
		}
	}
	public Expression grantIdentifierCalculation(char gid, String id, String name){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.putIdentifierCalculation(name, id, null);
			return new NormalEnd();
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		} 
	}

	public boolean permitIdentifierAction(String name) throws ApplicationException{
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			boolean lock = db.lockedIdCalculation(name, null);
			if(lock){
				User user = getLadder().getSessionContext().getSession().getUser();
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
	
	public Entity selectCalculation(String name){
		try {
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			TypedEntityList result = db.getMultiInstance(name, null);
			//当name不是定义，返回null。
			if(result != null){
				if(result.getItems().size() == 0){
					return new NullValue();
				}else if(result.getItems().size() == 1){
					return result.getItems().get(0);
				}else{
					return result;
				}
			}			
		} catch (Exception e) {
			return new HiwiiException();
		}
		return null;
	}
	
	public Entity selectByName(String name){
		try {
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			
			HiwiiInstance inst = db.getInstanceByName(name);
			if(inst != null){
				return inst;
			}
			String str = db.getIdCalculation(name, null);
			if(str != null){
				Expression expr = StringUtil.parseString(str);
				RuntimeContext rc = getLadder().newRuntimeContext('c');
				rc.setFunction(true);
				return rc.doCalculation(expr);
			}
		} catch (Exception e) {
			return new HiwiiException();
		}
		return null;
	}
	
	public Entity selectByCalc(String name){
		try {
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			
			String str = db.getIdCalculation(name, null);
			if(str != null){
				Expression expr = StringUtil.parseString(str);
				RuntimeContext rc = getLadder().newRuntimeContext('c');
				rc.setFunction(true);
				return rc.doCalculation(expr);
			}
		} catch (Exception e) {
			return new HiwiiException();
		}
		return null;
	}
	
	public Expression doDefinitionIdentifierAction(Definition def, String name){
		if(name.equals("closeSpace")){
			String master = "";
			User user = getLadder().getSessionContext().getSession().getUser();
			if(user != null){
				master = user.getUserid();
			}
			if(!def.getMaster().equals(master)){
				return new HiwiiException("only master can close or open space!");
			}
			if(def.isClosing()){
				return new HiwiiException("closed already!");
			}
			def.setClosing(true);
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
//			try {
//				db.putDefinitionMain(def, null);
//			} catch (DatabaseException e) {
//				return new HiwiiException();
//			} catch (IOException e) {
//				return new HiwiiException();
//			} catch (ApplicationException e) {
//				return new HiwiiException();
//			} catch (Exception e) {
//				return new HiwiiException();
//			} 
		}else if(name.equals("openSpace")){
			String master = "";
			User user = getLadder().getSessionContext().getSession().getUser();
			if(user != null){
				master = user.getUserid();
			}
			if(!def.getMaster().equals(master)){
				return new HiwiiException("only master can close or open space!");
			}
			if(!def.isClosing()){
				return new HiwiiException("opened already!");
			}
			def.setClosing(false);
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
//			try {
//				db.putDefinitionMain(def, null);
//			} catch (DatabaseException e) {
//				return new HiwiiException();
//			} catch (IOException e) {
//				return new HiwiiException();
//			} catch (ApplicationException e) {
//				return new HiwiiException();
//			} catch (Exception e) {
//				return new HiwiiException();
//			} 
		}
		return null;
	}
	
	public Entity doDefinitionAction(Definition def, Expression expr) {
		return null;
	}
	
	public Entity doDefinitionCalculation(Definition def, Expression expr) {
		if(expr instanceof IdentifierExpression) {
			IdentifierExpression ie = (IdentifierExpression) expr;
			return doDefinitionIdentifierCalculation(def, ie.getName());
		}
		if(expr instanceof FunctionExpression) {
			FunctionExpression fe = (FunctionExpression) expr;
			List<Entity> list = new ArrayList<Entity>();
			for(Expression arg:fe.getArguments()) {
				Entity ent = doCalculation(arg);
				list.add(ent);
			}
			return doDefinitionFunctionCalculation(def, fe.getName(), list);
		}
		if(expr instanceof MappingExpression) {
			MappingExpression me = (MappingExpression) expr;
			return doDefinitionMappingCalculation(def, me.getName(), me.getArguments());
		}
		return null;
	}
	
	public Entity doDefinitionIdentifierCalculation(Definition def, String name) {
		if(name.equals("new")) {
			return def.doIdentifierCalculation(name);
		}
		if(name.equals("put")) {  //add
			//when def is real definition
			String defName = def.getName();
			try {			
				HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
				String instid = db.putInstance(defName, null);
				HiwiiInstance inst = new HiwiiInstance();
				inst.setUuid(instid);
				inst.setClassName(defName);
				inst.setPersisted(true);
				return inst;
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				return new HiwiiException();
			}
		}
		if(def instanceof DecoratedDefinition) {
			DecoratedDefinition dd = (DecoratedDefinition) def;
			if(name.equals("that")) {
				try {			
					HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
					HiwiiInstance ret = db.getSingleInstance(dd.getName(), dd.getLimits(), this);
					return ret;
				} catch (DatabaseException e) {
					return new HiwiiException();
				} catch (IOException e) {
					return new HiwiiException();
				} catch (ApplicationException e) {
					return new HiwiiException();
				} catch (Exception e) {
					return new HiwiiException();
				}
			}
			if(name.equals("all")) {
				try {			
					HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
					Entity ret = db.getMultiLimits(dd.getName(), dd.getLimits(), this);
					return ret;
				} catch (DatabaseException e) {
					return new HiwiiException();
				} catch (IOException e) {
					return new HiwiiException();
				} catch (ApplicationException e) {
					return new HiwiiException();
				} catch (Exception e) {
					return new HiwiiException();
				}
			}
		}else if(def instanceof DefinitionView) {
			DefinitionView dv = (DefinitionView) def;
			if(name.equals("all")) {
				try {			
					HiwiiDB db = LocalHost.getInstance().getHiwiiDB();					
					Entity ret = db.getMultiInstanceView(def.getName(), dv.getFields(), null);
					return ret;
				} catch (DatabaseException e) {
					return new HiwiiException();
				} catch (IOException e) {
					return new HiwiiException();
				} catch (ApplicationException e) {
					return new HiwiiException();
				} catch (Exception e) {
					return new HiwiiException();
				}
			}
			if(name.equals("that")) {
				
			}
		}else if(def instanceof DefinitionLimitView) {
			DefinitionLimitView dd = (DefinitionLimitView) def;
			if(name.equals("that")) {
				try {			
					HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
					HiwiiInstance ret = db.getSingleInstance(dd.getName(), dd.getLimits(), this);
					return ret;
				} catch (DatabaseException e) {
					return new HiwiiException();
				} catch (IOException e) {
					return new HiwiiException();
				} catch (ApplicationException e) {
					return new HiwiiException();
				} catch (Exception e) {
					return new HiwiiException();
				}
			}
			if(name.equals("all")) {
				try {			
					HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
					Entity ret = db.getMultiLimitView(dd.getName(), dd.getFields(),dd.getLimits(), this);
					return ret;
				} catch (DatabaseException e) {
					return new HiwiiException();
				} catch (IOException e) {
					return new HiwiiException();
				} catch (ApplicationException e) {
					return new HiwiiException();
				} catch (Exception e) {
					return new HiwiiException();
				}
			}
		}else {
			if(name.equals("that") || name.equals("all")) {
				return def.doIdentifierCalculation(name);
//				String sig = def.getSignature();
//				Entity ret = getLadder().getSessionContext().getSession().getLastEntity(sig);
//				if(ret != null) {
//					return ret;
//				}
			}
		}
		return def;
	}
	
	public Entity doDefinitionFunctionCalculation(Definition def, String name, 
			List<Entity> args) {
		
		return def;
	}
	
	public Entity doDefinitionMappingCalculation(Definition def, String name, 
			List<Expression> args) {
		if(def instanceof DecoratedDefinition) {
			DecoratedDefinition dd0 = (DecoratedDefinition) def;
			if(name.equals("view")) {
				if(args.size() == 0) {
					return new HiwiiException();
				}
				//最少一个view参数
				DefinitionLimitView dv = new DefinitionLimitView();
				dv.setName(def.getName());
				dv.setClassName(def.getClassName());
				dv.setSignature(def.getSignature());
				dv.setParts(def.getParts());
				dv.setProps(def.getProps());
				dv.setStates(def.getStates());
				dv.setCharacteristics(def.getCharacteristics());
				dv.setFields(args);
				return dv;
			}if(name.equals("meet")) {
				DecoratedDefinition dd = new DecoratedDefinition();
				dd.setName(def.getName());
				dd.setClassName(def.getClassName());
				dd.setSignature(def.getSignature());
				dd.setParts(def.getParts());
				dd.setProps(def.getProps());
				dd.setStates(def.getStates());
				dd.setCharacteristics(def.getCharacteristics());
				List<Expression> list = dd0.getLimits();
				list.addAll(args);
				dd.setLimits(args);;
				return dd;
			}
		}else if(def instanceof DefinitionView) {
			DefinitionView view = (DefinitionView) def;
			if(name.equals("view")) {
				if(args.size() == 0) {
					return new HiwiiException();
				}
				//最少一个view参数
				DefinitionView dv = new DefinitionView();
				dv.setName(def.getName());
				dv.setClassName(def.getClassName());
				dv.setSignature(def.getSignature());
				dv.setParts(def.getParts());
				dv.setProps(def.getProps());
				dv.setStates(def.getStates());
				dv.setCharacteristics(def.getCharacteristics());
				List<Expression> list = view.getFields();
				list.addAll(args);
				dv.setFields(list);
				return dv;
			}else if(name.equals("meet")) {
				if(args.size() == 0) {
					return new HiwiiException();
				}
				//最少一个view参数
				DefinitionLimitView dv = new DefinitionLimitView();
				dv.setName(def.getName());
				dv.setClassName(def.getClassName());
				dv.setSignature(def.getSignature());
				dv.setParts(def.getParts());
				dv.setProps(def.getProps());
				dv.setStates(def.getStates());
				dv.setCharacteristics(def.getCharacteristics());
				dv.setLimits(args);
				dv.setFields(view.getFields());
				return dv;
			}
		}else {     //is definition
			if(name.equals("view")) {
				if(args.size() == 0) {
					return new HiwiiException();
				}
				//最少一个view参数
				DefinitionView dv = new DefinitionView();
				dv.setName(def.getName());
				dv.setClassName(def.getClassName());
				dv.setSignature(def.getSignature());
				dv.setParts(def.getParts());
				dv.setProps(def.getProps());
				dv.setStates(def.getStates());
				dv.setCharacteristics(def.getCharacteristics());
				dv.setFields(args);
				return dv;
			}else if(name.equals("meet")){
				if(args.size() == 0) {
					return new HiwiiException();
				}
				DecoratedDefinition dd = new DecoratedDefinition();
				dd.setName(def.getName());
				dd.setClassName(def.getClassName());
				dd.setSignature(def.getSignature());
				dd.setParts(def.getParts());
				dd.setProps(def.getProps());
				dd.setStates(def.getStates());
				dd.setCharacteristics(def.getCharacteristics());
				dd.setLimits(args);;
				return dd;
			}
		}		
		return def;
	}

	public Expression doDefinitionMappingAction(Definition def, String name, 
			List<Expression> args) {
		if(name.equals("grant")){
			if(args.size() != 2){
				return new HiwiiException("arguments error!");
			}			
			if(!(args.get(0) instanceof IdentifierExpression)){
				return new HiwiiException();
			}
			if(!(args.get(1) instanceof IdentifierExpression)){
				return new HiwiiException();
			}
			String userid = ((IdentifierExpression)args.get(0)).getName();
			String action = ((IdentifierExpression)args.get(1)).getName();
			
			String master = getLadder().getSessionContext().getSession().getUser().getUserid();
			if(!def.getMaster().equals(master)){
				return new HiwiiException("only master can grant or deny!");
			}
			if(!def.isClosing()){
				return new HiwiiException("closed Space can only deny!");
			}
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			try {
				boolean grant = true;
				db.putUserActionRight(def.getClassName(), action, userid, grant, null);
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				return new HiwiiException();
			} 
		}if(name.equals("deny")){
			if(args.size() != 2){
				return new HiwiiException("arguments error!");
			}			
			if(!(args.get(0) instanceof IdentifierExpression)){
				return new HiwiiException();
			}
			if(!(args.get(1) instanceof IdentifierExpression)){
				return new HiwiiException();
			}
			String userid = ((IdentifierExpression)args.get(0)).getName();
			String action = ((IdentifierExpression)args.get(1)).getName();
			
			User user = getLadder().getSessionContext().getSession().getUser();
			if(user == null){
				return new HiwiiException("please login!");
			}
			String master = user.getUserid();
			if(!def.getMaster().equals(master)){
				return new HiwiiException("only master can grant or deny!");
			}
			if(def.isClosing()){
				return new HiwiiException("open Space can only grant!");
			}
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			try {
				boolean grant = false;
				db.putUserActionRight(def.getClassName(), action, userid, grant, null);
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				return new HiwiiException();
			} 
			
		}else if(name.equals("assign")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			return doDefinitionAssign(def, args.get(0), args.get(1));
		}else if(name.equals("turn")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			return doDefinitionJudge(def, args.get(0), args.get(1));
		}if(name.equals("define")) {
			if(args.size() != 2) {
				return new HiwiiException();
			}
			return def.doDefine(args.get(0), args.get(1));
		}if(name.equals("declare")) {
			if(args.size() != 2) {
				return new HiwiiException();
			}
			return doDefinitionDeclare(def, args.get(0), args.get(1));
		}if(name.equals("undefine")) {
			if(args.size() != 1) {
				return new HiwiiException();
			}
			return def.undefineAction(args.get(0));
		}
		return null;
	}
	
	public Expression doInstanceAction(HiwiiInstance inst, Expression expr) {
		if(expr instanceof IdentifierExpression) {
			IdentifierExpression ie = (IdentifierExpression) expr;
			return doInstanceIdentifierAction(inst, ie.getName());
		}
		if(expr instanceof MappingExpression) {
			MappingExpression me = (MappingExpression) expr;
			if(me.getName().equals("define")) {
				if(me.getArguments().size() != 2) {
					return new HiwiiException();
				}
				return doDefine(inst, me.getArguments().get(0), me.getArguments().get(1));
			}
			return doInstanceMappingAction(inst, me.getName(), me.getArguments());
		}else if(expr instanceof FunctionExpression) {
			FunctionExpression fe = (FunctionExpression) expr;
			List<Entity> list = new ArrayList<Entity>();
			for(Expression exp:fe.getArguments()) {
				Entity ent = doCalculation(inst, exp);
				list.add(ent);
			}
			return inst.doFunctionAction(fe.getName(), list);
//			if(fe.getName().equals("add")) {
//				return inst.doFunctionAction(fe.getName(), list);
//			}
		}
		return null;
	}
	
	/**
	 * 可能性：1、innerAction.2,commonAction,3,intergratedAction
	 * @param inst
	 * @param name
	 * @return
	 */
	public Expression doInstanceIdentifierAction(HiwiiInstance inst, String name) {
		Expression expr = null;//getInnerIdentifierAction(inst, name);
		if(expr != null) {
//			inst.doInnerAction(expr)
		}
		expr = null;//get OuterIdentifierAction(inst, name)
		return null;
	}
	
	public Expression doInstanceMappingAction(HiwiiInstance inst, String name, 
			List<Expression> args) {
		if(name.equals("put")){
			//在不固定数量属性或成分中，增加元素。
			if(args.size() != 2){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof IdentifierExpression)){
				return new HiwiiException();
			}
			IdentifierExpression ie = (IdentifierExpression) args.get(0);
			String pname = ie.getName();
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			Definition def = null;
			Transaction txn = null;
			try {
				def = EntityUtil.proxyGetDefinition(inst.getClassName());
				if(def == null){
					return new HiwiiException();
				}
//				if(!def.getProps().containsKey(pname)){
//					return new HiwiiException();
//				}
				Property prop = null; //def.getProps().get(pname);
				Entity value = doCalculation(args.get(1));
				if(prop.getNumber() == 1){
					//一对多固定数或非固定属性才可以使用put，一对一使用assign
					return new HiwiiException();
				}
				txn = db.beginTransaction();
				db.addAssignment(inst.getUuid(), pname, value, txn);
				txn.commit();
			} catch (DatabaseException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			} catch (ApplicationException e) {
				return new HiwiiException();
			} catch (Exception e) {
				return new HiwiiException();
			} finally{
				if (txn != null) {
					txn.abort();
					txn = null;
				}
			}
			return new NormalEnd();
		}else if(name.equals("assign")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			return doInstanceAssign(inst, args.get(0), args.get(1));
		}else if(name.equals("turn")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			
			return doInstanceJudge(inst, args.get(0), args.get(1));
		}else if(name.equals("switch")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			
			return doInstanceSwitch(inst, args.get(0), args.get(1));
		}
		return inst.doMappingAction(name, args);
	}
	
	public Expression doInstanceIdentifierDecision(HiwiiInstance inst, String name){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
//		Definition def = null;
		try {
			JudgmentResult jr = db.getInstanceJudgment(inst, name, null);
			return jr; //null is undecidable
//			return null;  //undecidable
//			def = EntityUtil.proxyGetDefinition(inst.getClassName());
//			if(def == null){
//				return new HiwiiException();
//			}
//			jr = db.getJudgment(def, name, null);
//			if(jr != null){
//				return jr;
//			}
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}
	}
	/**
	 * 真正意义的lambda演算，返回表达式
	 * 调用方式：lambda[expression, []]
	 * 取消lambda作为主语，坚持entity作为主语，便于掌握权限。
	 * doCalculation和doLambdaCalculation不同。
	 * doCalculation是表达式的执行，而lambdaCalculation是编程
	 * @param expr
	 * @return
	 */
	public Expression lambdaCalculation(Expression expr) {
		//结果可能是lambdaExpression
		return expr;
	}
	/**
	 * 以lambda表达式为主体，以表达式输入为参数，输出lambda表达式
	 * @param expr
	 * @param args
	 * @return
	 */
	public Expression lambdaCalculus(LambdaMapping expr, List<Expression> args) {
		LambdaMapping le = new LambdaMapping();
		return le;
	}
	
	public Expression lambdaMapping(LambdaMapping expr, List<Expression> args) {
		if(expr.getKeys().size() != args.size()){
			//HiwiiException虽然是expression，但不能参与lambda演算，也不能作为lambda演算的结果。
			return new HiwiiException();
		}
		ArgumentedLambda ale = new ArgumentedLambda();
		ale.setKeys(expr.getKeys());
		ale.setStatement(expr.getStatement());
		ale.setArguments(args);
		return ale;
	}
	/**
	 * lambda演算的entity参数形式。函数的构成前提
	 * functor[expression, ()]
	 * @param expr
	 * @param args
	 * @return
	 */
	public Expression lambdaFunction(LambdaMapping expr, List<Entity> args) {
		if(expr.getKeys().size() != args.size()){
			//HiwiiException虽然是expression，但不能参与lambda演算，也不能作为lambda演算的结果。
			return new HiwiiException();
		}
		EntityLambda ale = new EntityLambda();
		ale.setKeys(expr.getKeys());
		ale.setStatement(expr.getStatement());
		ale.setArguments(args);
		return ale;
	}
	
	
	public Entity doLambdaCalculation(LambdaMapping expr) {
		RuntimeContext rc = getLadder().newRuntimeContext('c');
		if(expr.getKeys().size() != 0){
			return new HiwiiException();
		}
		return rc.doCalculation(expr);
	}

	public Entity doLambdaFunctionCalculation(LambdaMapping expr, List<Entity> args) {
		RuntimeContext rc = getLadder().newRuntimeContext('c');
		if(expr.getKeys().size() != args.size()){
			return new HiwiiException();
		}
		int i = 0;
		for(String key:expr.getKeys()){
			rc.getRefers().put(key, args.get(i));
			i++;
		}
		return rc.doCalculation(expr.getStatement());
	}

	public Entity doLambdaMappingCalculation(LambdaMapping expr, List<Expression> args) {
		RuntimeContext rc = getLadder().newRuntimeContext('c');
		if(expr.getKeys().size() != args.size()){
			return new HiwiiException();
		}
		int i = 0;
		for(String key:expr.getKeys()){
			rc.getExpressionST().put(key, args.get(i));
			i++;
		}
		return rc.doCalculation(expr.getStatement());
	}

	public Expression doLambdaAction(Expression expr) {
		RuntimeContext rc = getLadder().newRuntimeContext('c');
		return rc.doAction(expr);
	}
	
	public Expression doLambdaFunctionAction(Expression expr, List<Expression> args) {
		
		return null;
	}

	public Expression doLambdaMappingAction(Expression expr, List<Expression> args) {
		
		return null;
	}	

	public Expression doLambdaDecision(Expression expr) {
		RuntimeContext rc = getLadder().newRuntimeContext('c');
		return rc.doDecision(expr);
	}
	
	public Entity getListClass(Expression expr){
		ListClass type = new ListClass();
		try {
			if(expr instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) expr;
				Definition def = EntityUtil.proxyGetDefinition(ie.getName());
				if(def == null){
					return new HiwiiException();
				}
				type.setType(ie.getName());
			}else if(expr instanceof IdentifierBrace){
				IdentifierBrace ib = (IdentifierBrace) expr;
				Definition def = EntityUtil.proxyGetDefinition(ib.getName());
				if(def == null){
					return new HiwiiException();
				}
				type.setType(ib.getName());
				type.setLimits(ib.getConditions());
			}else{
				return new HiwiiException();
			}
			return type;
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}
		
	}
	
	public Entity getSetClass(Expression expr){
		SetClass type = new SetClass();
		try {
			if(expr instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) expr;
				Definition def = EntityUtil.proxyGetDefinition(ie.getName());
				if(def == null){
					return new HiwiiException();
				}
				type.setType(ie.getName());
			}else if(expr instanceof IdentifierBrace){
				IdentifierBrace ib = (IdentifierBrace) expr;
				Definition def = EntityUtil.proxyGetDefinition(ib.getName());
				if(def == null){
					return new HiwiiException();
				}
				type.setType(ib.getName());
				type.setLimits(ib.getConditions());
			}else{
				return new HiwiiException();
			}
			return type;
		} catch (DatabaseException e) {
			return new HiwiiException();
		} catch (IOException e) {
			return new HiwiiException();
		} catch (ApplicationException e) {
			return new HiwiiException();
		} catch (Exception e) {
			return new HiwiiException();
		}
		
	}
	
	/**
	 * 通过表达式获得定义+条件
	 * @param expr
	 * @return
	 * @throws DatabaseException
	 * @throws IOException
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public TypeView getTypeView(Expression expr) 
			throws DatabaseException, IOException, ApplicationException, Exception{
		TypeView type = new TypeView();
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			Definition def = EntityUtil.proxyGetDefinition(ie.getName());
			if(def == null){
				throw new ApplicationException();
			}
			type.setType(ie.getName());
		}else if(expr instanceof IdentifierBrace){
			IdentifierBrace ib = (IdentifierBrace) expr;
			Definition def = EntityUtil.proxyGetDefinition(ib.getName());
			if(def == null){
				throw new ApplicationException();
			}
			type.setType(ib.getName());
			type.setLimits(ib.getConditions());
		}else{
			throw new ApplicationException();
		}
		return type;
	}
	/**
	 * 对于action有前处理和后处理。通过前后处理，实现action的动作修饰。
	 */
	public Expression preAction(Expression expr){
		return null;
	}
	public Expression postAction(Expression expr){
		return null;
	}
	/**
	 * 对于calculation、decision、action都有before和after判断处理
	 * 前后判断用于判断执行权限。calculation执行权限判断有些必须放在执行后，因为无法在执行前获得计算结果。
	 * 与preAction和postAction不同，before和after处理只能进行参数设置，不能进行action处理。
	 */
	public Expression beforeAction(Expression expr){
		return null;
	}
	public Expression afterAction(Expression expr){
		return null;
	}
	public Expression beforeCalculation(Expression expr){
		return null;
	}
	public Expression afterCalculation(Expression expr){
		return null;
	}
	public void receiveMessage(Terminal term, String msg){
		
	}
}