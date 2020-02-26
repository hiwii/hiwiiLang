package net.hiwii.def;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Transaction;

import net.hiwii.arg.Argument;
import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.NormalEnd;
import net.hiwii.collection.TypedEntityList;
import net.hiwii.db.HiwiiDB;
import net.hiwii.def.decl.FunctionDeclaration;
import net.hiwii.expr.BinaryOperation;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.MappingExpression;
import net.hiwii.expr.SubjectOperation;
import net.hiwii.expr.BraceExpression;
import net.hiwii.expr.FunctionExpression;
import net.hiwii.expr.adv.IdentifierBrace;
import net.hiwii.message.HiwiiException;
import net.hiwii.part.EntityPart;
import net.hiwii.prop.Property;
import net.hiwii.system.LocalHost;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.view.Entity;
import net.hiwii.view.HiwiiInstance;

/**
 * 定义的方式：组合，extend，define，specify
 * 组合没有顺序。量词形式是一种特殊组合。
 * simpleDefinition是SystemDefinition.
 * Definition既是一个instance，也是一个独立对象。
 * definition的抽象特征作为instance保存，具体特征作为映射instance保存。
 * @author Administrator
 *
 */
public class Definition extends HiwiiInstance {
	private String name;
	private String parent;
	private String signature;
	private NavigableMap<String,EntityPart> parts;
	private NavigableMap<String,Property> props;
	private List<String> states;
	private List<Expression> characteristics;

	private boolean isClosing;
	private String master;

	private HiwiiInstance instance;
	//instance共同属性
	public Definition() {
		props = new TreeMap<String,Property>();
		//	defines = new TreeMap<String,Definition>();
		states = new ArrayList<String>();
		//		setClosing(false);
	}

	
	public HiwiiInstance getInstance() {
		return instance;
	}


	public void setInstance(HiwiiInstance instance) {
		this.instance = instance;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}

	public NavigableMap<String, EntityPart> getParts() {
		return parts;
	}

	public void setParts(NavigableMap<String, EntityPart> parts) {
		this.parts = parts;
	}

	public NavigableMap<String, Property> getProps() {
		return props;
	}

	public void setProps(NavigableMap<String, Property> props) {
		this.props = props;
	}
	public List<String> getStates() {
		return states;
	}

	public void setStates(List<String> states) {
		this.states = states;
	}

	public List<Expression> getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(List<Expression> characteristics) {
		this.characteristics = characteristics;
	}

	public String takeSignature() {
		return signature;
	}

	public boolean isClosing() {
		return isClosing;
	}

	public void setClosing(boolean isClosing) {
		this.isClosing = isClosing;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	@Override
	public String getClassName() {
		return getName();
	}

	@Override
	public Entity doIdentifierCalculation(String name) {
		if(name.equals("all")) {
			return doGetMultiInstance();
		}
		if(name.equals("that")) {//原first
			return doGetSingleInstance();
		}
//		if(name.equals("last")) {
//			return doGetSingleInstance();
//		}
		if(name.equals("new")) {
			HiwiiInstance inst = new HiwiiInstance();
			inst.setClassName(this.getName());
			return inst;
		}
		return null;
	}

	@Override
	public Entity doMappingCalculation(String name, List<Expression> args) {
		if(name.equals("view")) {
			if(args.size() == 1) {
				Expression ex = args.get(0);
				if(ex instanceof IdentifierExpression) {
					IdentifierExpression ie = (IdentifierExpression) ex;
					if(ie.getName().equals("all")) {
						return this;
						//TODO none for none fields;
					}else {
						return new HiwiiException();
					}
				}else if(ex instanceof BraceExpression){
					BraceExpression be = (BraceExpression) ex;
					DefinitionView dv = new DefinitionView();
					dv.setName(this.name);
					dv.setClassName(getClassName());
					dv.setSignature(signature);
					dv.setParts(parts);
					dv.setProps(props);
					dv.setStates(states);
					dv.setCharacteristics(characteristics);
					dv.setFields(be.getArray());
					return dv;
				}
			}
		}
		return null;
	}

	@Override
	public Expression doMappingAction(String name, List<Expression> args) {
		if(name.equals("define")) {
			if(args.size() == 2){
				return doDefine(args.get(0), args.get(1));
			}else{
				return new HiwiiException();
			}
		}
		if(name.equals("undefine")) {
			if(args.size() != 1){
				return new HiwiiException();
			}

		}
		return null;
	}

	public Entity doGetSingleInstance(){
		try {			
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			HiwiiInstance ret = db.getSingleInstance(name, null);
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
		//		return null;
	}
	public Entity doGetMultiInstance(){
		try {			
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			TypedEntityList ret = db.getMultiInstance(name, null);
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
		//		return null;
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
				}else if(cogn.equals("Status")){ //原为new(Status
					//status=state 表示状态
					//					return newStatus(right);
				}else if(cogn.equals("Symbol")){ 
					//			return newStatus(right);
				}else if(cogn.equals("Assignment")){
					//			return doAssign(right, target);
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
				if(cogn.equals("Link")){// or Property
					return defineLink(source, right);
				}
			}
		}

		return new NormalEnd();
	}
	
	public Expression doDeclare(char type, Expression source, Expression expr){
		String name = null;

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			if(source instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) source;
				name = ie.getName();
				String key = name + "@" + this.getSignature();
				if(type == 'c'){
					db.putIdCalculation(key, expr.toString(), null);
				}else if(type == 'd'){

				}else{

				}
			}else if(source instanceof FunctionExpression){
				FunctionExpression fe = (FunctionExpression) source;
				name = fe.getName();
				FunctionDeclaration fd = new FunctionDeclaration();
//				fd.setName(name);
				try {
					List<Argument> args = EntityUtil.parseArguments(fe.getArguments());
//					fd.setArguments(args);
				} catch (ApplicationException e) {
					return new HiwiiException();//参数错误
				}

				fd.setStatement(expr);
				if(type == 'c'){
//					db.putFunCalculation(fd, null);
				}else if(type == 'd'){
//					db.putFunDecision(fd, null);
				}else{

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

	public Expression undefineAction(Expression expr){
		if(!(expr instanceof BinaryOperation)){
			//			return persistDefinition(expr);
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
				return undefineDeclare(tp, right);
			}else {
				//			return persistDefinition(expr);
			}
		}else {
			//			return persistDefinition(expr);
		}

		return new NormalEnd();
	}

	public Expression undefineDeclare(char type, Expression source){
		String expr = null;

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Transaction txn = null;
		try {
			txn = db.beginTransaction();
			if(source instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) source;
				name = ie.getName();
				String key = name + "@" + this.getSignature();
				if(type == 'c'){
					db.dropIdCalculation(key, null);
				}else if(type == 'd'){

				}else{
					db.dropIdAction(key, null);
				}
			}else if(source instanceof FunctionExpression){
				FunctionExpression fe = (FunctionExpression) source;
				name = fe.getName();
				FunctionDeclaration fd = new FunctionDeclaration();
//				fd.setName(name);
				try {
					List<Argument> args = EntityUtil.parseArguments(fe.getArguments());
//					fd.setArguments(args);
				} catch (ApplicationException e) {
					return new HiwiiException();//参数错误
				}

//				fd.setStatement(expr);
				if(type == 'c'){
//					db.putFunCalculation(fd, null);
				}else if(type == 'd'){
//					db.putFunDecision(fd, null);
				}else{

				}
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

	public Expression defineLink(Expression source, Expression right){
		Property prop = new Property();

		if(source instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) source;
			prop.setName(ie.getName());
		}else{
			return new HiwiiException();
		}

		List<Expression> limits = null;
		Definition type = null;
		if(right instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) right;
			String name = ie.getName();
			try {
				type = EntityUtil.proxyGetDefinition(name);
			} catch (DatabaseException e) {
				return new HiwiiException(e.getMessage());
			} catch (IOException e) {
				return new HiwiiException(e.getMessage());
			} catch (ApplicationException e) {
				return new HiwiiException(e.getMessage());
			} catch (Exception e) {
				return new HiwiiException(e.getMessage());
			}
			if(type == null){
				return new HiwiiException();
			}
			prop.setType(type.getName());
		}else if(right instanceof IdentifierBrace){
			IdentifierBrace ib = (IdentifierBrace) right;			
			String name = ib.getName();
			try {
				type = EntityUtil.proxyGetDefinition(name);
			} catch (DatabaseException e) {
				return new HiwiiException(e.getMessage());
			} catch (IOException e) {
				return new HiwiiException(e.getMessage());
			} catch (ApplicationException e) {
				return new HiwiiException(e.getMessage());
			} catch (Exception e) {
				return new HiwiiException(e.getMessage());
			}
			if(type == null){
				return new HiwiiException();
			}
			limits = ib.getConditions();
			prop.setType(type.getName());
			prop.setLimits(limits);
		}

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Transaction txn = null;//if need transaction, to be optimized
		try {
			txn = db.beginTransaction();
			db.putProperty(this, prop, null);
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
				//				return hasDeclared(tp, right);
			}else if(cogn.equals("Symbol")){ 
				//			return newStatus(right);
			}else if(cogn.equals("Status")){ //原为new(Status
				//				return hasStatus(right);
			}else if(cogn.equals("Verb")){
				//			return newVerb(right);
			}else if(cogn.equals("Assignment")){
				//			return doAssign(right, target);
			}else if(cogn.equals("Property")){
				//				return hasProperty(right);
			}else if(cogn.equals("Link")){
				return definedLink(right);
			}
		}else {

		}

		return null;
	}

	public Expression definedLink(Expression right){
		String prop = null;

		if(right instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) right;
			prop = ie.getName();
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
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

	public boolean definedProperty(String fname)
			throws IOException, DatabaseException, ApplicationException, Exception{

		if(getProps().containsKey(fname)) {
			return true;
		}
		Property ret = EntityUtil.proxyGetProperty(fname);
		if(ret != null) {
			return true;
		}		
		
		return false;
	}

	public boolean definedState(String fname)
			throws IOException, DatabaseException, ApplicationException, Exception{

		if(states.contains(fname)) {
			return true;
		}
		if(EntityUtil.proxyHasState(fname)) {
			return true;
		}		
		
		return false;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
