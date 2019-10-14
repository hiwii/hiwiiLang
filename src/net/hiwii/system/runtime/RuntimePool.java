package net.hiwii.system.runtime;

import java.util.NavigableMap;
import java.util.NavigableSet;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.StringExpression;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.syntax.def.FunctionDefinition;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.system.util.tuple.TwoTuple;
import net.hiwii.view.Entity;

/**
 * 对于doProgram，对象必须包含runtimePool，用于记录程序运行时变量和程序状态。
 * 运行时变量包括：
 * 用于做主语或宾语的变量(通过new 语句建立，通过赋值语句赋值)
 * 每个定义由四部分组成。
 * 1,path. 2,object or definition gene. 3,定义名. 4,参数(只有函数定义有).
 * #用于分隔path与表达式
 * 符号.用于分隔path，|用于分隔定义名和参数，“,"用于分隔参数定义名,
 * %用于分隔宿主对象和定义表达式。
 * 
 * 每个Id索引由两部分组成，1，id或函数声明。2，定义/分类+判断
 * "@"引导数字，字符串还在引号中
 * set(object/cognition/operation/action)
 * define(class/definition/function/behavior)
 * char:c表示cognition，
 * o表示Object，p表示Operation，a表示action
 * 
 * persistDB与runtime一一对应。每个db有一个index。
 * @author ha-wangzhenhai
 *
 */
public class RuntimePool {
	private String path; //initial value is root
	private RuntimePool parent;
	
	/**
	 * 每个内容由主语gene|signature|path+内容构成
	 * gene定义了查找顺序
	 * 有两个索引：
	 * 1个:signature|gene|path--用于快速查找
	 * 1个:path|signature|gene--用于垃圾回收
	 */
	/**
	 * internal definition
	 * 定义基因由所在定义+父定义基因+定义名组成。
	 * 格式：父定义基因.name
	 * defMap key:gene|name#path
	 * defMap value:definition gene
	 * defIdx:name|gene|path
	 * defPath:path|signature|gene
	 * Idx中只使用name作为索引项。只评估name满足条件的Map项
	 */
	private NavigableMap<String,String> defMap;
	private NavigableSet<String> defIdx;
	private NavigableSet<String> defPath;
	
	private NavigableMap<String,String> classMap;
	private NavigableSet<String> classIdx;
	private NavigableSet<String> classPath;
	
	private NavigableMap<String,String> funcMap;
	private NavigableSet<String> funcIdx;
	private NavigableSet<String> funcPath;
	
	/**
	 * 声明:包括运算/动作/判断
	 * map key:gene|name|signature|path
	 * map value:参数名,之间以,分隔，所有参数必须有参数名。参数在runtime中以属性方式赋值。
	 * idx:name|gene|signature|path
	 * path:path|name|gene|signature
	 */
	private NavigableMap<String,String> opdecMap;
	private NavigableSet<String> opdecIdx;
	private NavigableSet<String> opdecPath;

	private NavigableMap<String,String> actdecMap;
	private NavigableSet<String> actdecIdx;
	private NavigableSet<String> actdecPath;
	
	private NavigableMap<String,String> jddecMap;
	private NavigableSet<String> jddecIdx;
	private NavigableSet<String> jddecPath;
	
	/**
	 * 实现
	 * map key:gene|signature|path
	 * map value:expression
	 * 
	 */
	private NavigableMap<String,Expression> opimpMap;
	private NavigableSet<String> opimpIdx;
	private NavigableSet<String> opimpPath;

	private NavigableMap<String,Expression> actimpMap;
	private NavigableSet<String> actimpIdx;
	private NavigableSet<String> actimpPath;
	
	private NavigableMap<String,Expression> jdimpMap;
	private NavigableSet<String> jdimpIdx;
	private NavigableSet<String> jdimpPath;
	
	/**
	 * 属性定义
	 * key：gene|judgment expression|path
	 * value:definition|judgment expression，即，定义+子判断，形成子定义
	 */
	private NavigableMap<String,String> propdefMap;
	private NavigableSet<String> propdefIdx;
	private NavigableSet<String> propdefPath;
	
	private NavigableMap<String,String> propdesMap;//describe
	private NavigableSet<String> propdesIdx;
	private NavigableSet<String> propdesPath;
	
	/**
	 * 特征判断-identifier
	 * idx:gene|judgment expression|path
	 * path:path|gene|judgment expression
	 */
	private NavigableSet<String> idListIdx;
	private NavigableSet<String> idListPath;
	/**
	 * 一般判断
	 * idx:gene|judgment expression|path
	 * path:path|gene|judgment expression
	 */
	private NavigableSet<String> jdListIdx;
	private NavigableSet<String> jdListPath;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public RuntimePool getParent() {
		return parent;
	}

	public void setParent(RuntimePool parent) {
		this.parent = parent;
	}

	public NavigableMap<String, String> getDefMap() {
		return defMap;
	}

	public void setDefMap(NavigableMap<String, String> defMap) {
		this.defMap = defMap;
	}

	public NavigableMap<String, String> getClassMap() {
		return classMap;
	}

	public void setClassMap(NavigableMap<String, String> classMap) {
		this.classMap = classMap;
	}

	public NavigableSet<String> getClassIdx() {
		return classIdx;
	}

	public void setClassIdx(NavigableSet<String> classIdx) {
		this.classIdx = classIdx;
	}

	public NavigableSet<String> getClassPath() {
		return classPath;
	}

	public void setClassPath(NavigableSet<String> classPath) {
		this.classPath = classPath;
	}

	public NavigableMap<String, String> getFuncMap() {
		return funcMap;
	}

	public void setFuncMap(NavigableMap<String, String> funcMap) {
		this.funcMap = funcMap;
	}

	public NavigableSet<String> getFuncIdx() {
		return funcIdx;
	}

	public void setFuncIdx(NavigableSet<String> funcIdx) {
		this.funcIdx = funcIdx;
	}

	public NavigableSet<String> getFuncPath() {
		return funcPath;
	}

	public void setFuncPath(NavigableSet<String> funcPath) {
		this.funcPath = funcPath;
	}

	public NavigableMap<String, String> getOpdecMap() {
		return opdecMap;
	}

	public void setOpdecMap(NavigableMap<String, String> opdecMap) {
		this.opdecMap = opdecMap;
	}

	public NavigableSet<String> getOpdecIdx() {
		return opdecIdx;
	}

	public void setOpdecIdx(NavigableSet<String> opdecIdx) {
		this.opdecIdx = opdecIdx;
	}

	public NavigableSet<String> getOpdecPath() {
		return opdecPath;
	}

	public void setOpdecPath(NavigableSet<String> opdecPath) {
		this.opdecPath = opdecPath;
	}

	public NavigableMap<String, String> getActdecMap() {
		return actdecMap;
	}

	public void setActdecMap(NavigableMap<String, String> actdecMap) {
		this.actdecMap = actdecMap;
	}

	public NavigableSet<String> getActdecIdx() {
		return actdecIdx;
	}

	public void setActdecIdx(NavigableSet<String> actdecIdx) {
		this.actdecIdx = actdecIdx;
	}

	public NavigableSet<String> getActdecPath() {
		return actdecPath;
	}

	public void setActdecPath(NavigableSet<String> actdecPath) {
		this.actdecPath = actdecPath;
	}

	public NavigableMap<String, String> getJddecMap() {
		return jddecMap;
	}

	public void setJddecMap(NavigableMap<String, String> jddecMap) {
		this.jddecMap = jddecMap;
	}

	public NavigableSet<String> getJddecIdx() {
		return jddecIdx;
	}

	public void setJddecIdx(NavigableSet<String> jddecIdx) {
		this.jddecIdx = jddecIdx;
	}

	public NavigableSet<String> getJddecPath() {
		return jddecPath;
	}

	public void setJddecPath(NavigableSet<String> jddecPath) {
		this.jddecPath = jddecPath;
	}

	public NavigableMap<String, Expression> getOpimpMap() {
		return opimpMap;
	}

	public void setOpimpMap(NavigableMap<String, Expression> opimpMap) {
		this.opimpMap = opimpMap;
	}

	public NavigableSet<String> getOpimpIdx() {
		return opimpIdx;
	}

	public void setOpimpIdx(NavigableSet<String> opimpIdx) {
		this.opimpIdx = opimpIdx;
	}

	public NavigableSet<String> getOpimpPath() {
		return opimpPath;
	}

	public void setOpimpPath(NavigableSet<String> opimpPath) {
		this.opimpPath = opimpPath;
	}

	public NavigableMap<String, Expression> getActimpMap() {
		return actimpMap;
	}

	public void setActimpMap(NavigableMap<String, Expression> actimpMap) {
		this.actimpMap = actimpMap;
	}

	public NavigableSet<String> getActimpIdx() {
		return actimpIdx;
	}

	public void setActimpIdx(NavigableSet<String> actimpIdx) {
		this.actimpIdx = actimpIdx;
	}

	public NavigableSet<String> getActimpPath() {
		return actimpPath;
	}

	public void setActimpPath(NavigableSet<String> actimpPath) {
		this.actimpPath = actimpPath;
	}

	public NavigableMap<String, Expression> getJdimpMap() {
		return jdimpMap;
	}

	public void setJdimpMap(NavigableMap<String, Expression> jdimpMap) {
		this.jdimpMap = jdimpMap;
	}

	public NavigableSet<String> getJdimpIdx() {
		return jdimpIdx;
	}

	public void setJdimpIdx(NavigableSet<String> jdimpIdx) {
		this.jdimpIdx = jdimpIdx;
	}

	public NavigableSet<String> getJdimpPath() {
		return jdimpPath;
	}

	public void setJdimpPath(NavigableSet<String> jdimpPath) {
		this.jdimpPath = jdimpPath;
	}

	public String getGene(Entity target){
		return "0";
	}
	
	/**********************************Begin operation implements********************/
	/**
	 * 本调用的发起：entity.doOperation(expression)
	 * 1,entity.native operation
	 * 2,common operation
	 * 3,runtime operation
	 * 4,persist operation
	 * subject可以是定义或对象id
	 * @param subject
	 * @param intf
	 * @return
	 */
	public Entity doOperation(Entity subject, Expression intf){
		if(subject instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) subject;
		}
		return null;
	}
	
	public TwoTuple<String, String> hasDeclare(String gene, String sig, char type){
		NavigableMap<String,String> map = null;
		NavigableSet<String> idx = null;
		if(type == 'o'){
			map = getOpdecMap();
			idx = getOpdecIdx();
		}else if(type == 'a'){
			map = getActdecMap();
			idx = getActdecIdx();
		}else if(type == 'o'){
			map = getJddecMap();
			idx = getJddecIdx();
		}else{
			return null;
		}
		return null;
	}
	
	public boolean hasOperationImpl(Expression subject, Expression intf){
		//getGene(subject)
		return false;
	}
	
	public Expression getImplement(String gene, String sig){
		return null;
	}
	
	/**
	 * 由Entity.doOperation(Expression,pool)调用，gene由entity提供
	 * 如果没有implement，return null
	 * 返回包括exception。
	 * @param gene
	 * @param expr
	 * @return
	 */
	public Expression getOperationImpl(String gene, Expression expr){
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			String name = ie.getName();
		}
		return null;
	}
	
	/**
	 * 异常tell方法：1，传统抛出和捕获。2，返回hiwiiException
	 * 这里正常返回StringExpression表示Declarence。
	 * @param gene
	 * @param dec
	 * @param type
	 * @return
	 */
	public Expression putDeclarence(String gene, Expression dec, char type){
		NavigableMap<String, String> map = null;
		NavigableSet<String> idx = null, pset = null;
		if(type == 'a'){
			map = actdecMap;
			idx = actdecIdx;
			pset = actdecPath;
		}else if(type == 'o'){
			map = opdecMap;
			idx = opdecIdx;
			pset = opdecPath;
		}else if(type == 'j'){
			map = jddecMap;
			idx = jddecIdx;
			pset = jddecPath;
		}else{
			return new HiwiiException();
		}
		String decl = "";
		if(dec instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) dec;
			String name = ie.getName();
			decl = gene + "|" + name + "||";
			String key = gene + "|" + name + "||" + path;
			String is = name + "|" + gene + "||" + path;
			String ps = path + "|" + name + "|" + gene + "||";
			map.put(key, null);
			idx.add(is);
			pset.add(ps);
		}else if(dec instanceof FunctionDefinition){
			FunctionDefinition fd = (FunctionDefinition) dec;
			String name = fd.getName();
			String sig = null;
//			try {
//				//sig including signature and condition.
//				sig = EntityUtil.getFunctionDefSignature(gene, fd, this);
//			} catch (ApplicationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			decl = gene+ "|" + name + "|" + sig + "|";
		}
		return new StringExpression(decl);
	}
	
	public Expression putImplement(String decl, Expression content){
		return null;
	}

	/************************************************/
	public String navigateUp() throws ApplicationException{
		path = path + ".0";
		return path;
	}
	
	public String navigateDown() throws ApplicationException{
		int pos = path.lastIndexOf('.');
		if (pos >= 0){
			//this is for cognition
//			while(true){
//
//			}
//			path = path.substring(0, pos);
			return path;
		}else{
			throw new ApplicationException("root path arrived.");
		}
	}
}
