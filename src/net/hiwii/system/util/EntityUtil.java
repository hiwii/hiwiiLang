package net.hiwii.system.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.sleepycat.je.DatabaseException;

import net.hiwii.arg.Argument;
import net.hiwii.arg.DecoratedArgument;
import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.BreakReturn;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.cognition.result.NormalEnd;
import net.hiwii.cognition.result.SkipReturn;
import net.hiwii.context.HiwiiContext;
import net.hiwii.context.RuntimeContext;
import net.hiwii.context.SessionContext;
import net.hiwii.db.HiwiiDB;
import net.hiwii.db.ent.StoredValue;
import net.hiwii.def.Assignment;
import net.hiwii.def.Awareness;
import net.hiwii.def.Declaration;
import net.hiwii.def.Definition;
import net.hiwii.def.Perception;
import net.hiwii.def.decl.FunctionProperty;
import net.hiwii.expr.BinaryOperation;
import net.hiwii.expr.FunctionExpression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.NullExpression;
import net.hiwii.expr.BraceExpression;
import net.hiwii.expr.StringExpression;
import net.hiwii.expr.adv.IdentifierBrace;
import net.hiwii.expr.date.DateValue;
import net.hiwii.expr.date.TimeValue;
import net.hiwii.lambda.LambdaMapping;
import net.hiwii.message.HiwiiException;
import net.hiwii.obj.time.TimeObject;
import net.hiwii.prop.Property;
import net.hiwii.prop.Variable;
import net.hiwii.prop.VariableStore;
import net.hiwii.reg.RegularExpression;
import net.hiwii.reg.SimpleRegular;
import net.hiwii.reg.match.MatchGroup;
import net.hiwii.reg.match.MatchResult;
import net.hiwii.system.LocalHost;
import net.hiwii.system.SystemDefinition;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.runtime.RuntimePool;
import net.hiwii.system.syntax.cog.DefineSentence;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.system.syntax.sent.BranchSentence;
import net.hiwii.system.syntax.sent.LoopSentence;
import net.hiwii.system.syntax.sent.SwitchSentence;
import net.hiwii.system.util.tuple.ThreeTuple;
import net.hiwii.system.util.tuple.TwoTuple;
import net.hiwii.user.User;
import net.hiwii.view.Entity;
import net.hiwii.view.HiwiiInstance;

/**
 * 此模块作为对象不能完成自身运算、判断和动作后的认识操作。
 * 定义：
 * 1，canDefine
 * 2，String getDefinitionGene
 * 3，putDefinition(gene, DefineSentence, pool);
 * 4，removeDefinition(gene, name)//只删除persist，runtime自行收集
 * 5，没有update
 * 
 * 声明：
 * 1，调用时获得声明，进而获得implement
 * 2，声明时，canDeclare
 * 3，doDeclare
 * 4，removeDeclare
 * 5，updateDeclare
 *  
 * @author ha-wangzhenhai
 *
 */
public class EntityUtil {
	public static Entity deserialize(byte[] source){
		return null;
	}
	
	public static long unsignedInt(byte[] val, int pos){
		long ret = val[pos] & 0xFF;
		ret = ret + ((val[pos + 1] & 0xFF) << 8);
		ret = ret + ((val[pos + 2]  & 0xFF) << 16);
		ret = ret + ((val[pos + 3]  & 0xFFL) << 24);
	
		return ret;
	}
	
	public static int unsignedShort(byte[] val, int pos){
		int ret = val[pos] & 0xFF;
		ret = ret + ((val[pos + 1] & 0xFF) << 8);
	
		return ret;
	}
	
	public static byte[] getUnsignedShort(int val){
		byte[] ret = new byte[2];
		ret[0] = (byte) (val >>> 0);
		ret[1] = (byte) (val >>> 8);
		return ret;
	}
	
	public static byte[] getUnsignedInt(long val){
		byte[] ret = new byte[4];
		ret[0] = (byte) (val >>> 0);
		ret[1] = (byte) (val >>> 8);
		ret[2] = (byte) (val >>> 16);
		ret[3] = (byte) (val >>> 24);
		return ret;
	}
	
	/****************************************************/
	public static JudgmentResult decide(boolean bool){
		JudgmentResult jr = new JudgmentResult();
		jr.setResult(bool);
		return jr;
	}
	
	public static boolean judgeResult(JudgmentResult jr){
		return jr.isResult();
	}
	
	public static boolean judge(Expression expr){
		if(expr instanceof JudgmentResult){
			JudgmentResult jr = (JudgmentResult) expr;
			return jr.isResult();
		}else{
			return false;
		}
	}
	
	public static boolean error(Expression expr){
		if(expr instanceof HiwiiException){
			return true;
		}else{
			return false;
		}
	}
	/****************************************************/
	public static boolean isProperty(String gene, String name, RuntimePool pool){
		return false;
	}
	
	public static boolean isDefinition(String gene, String name, RuntimePool pool){
		return false;
	}
	
	public static boolean isClass(String gene, String name, RuntimePool pool){
		return false;
	}
	
	public static boolean isAware(String gene, String name, RuntimePool pool){
		return false;
	}
	
	public static boolean isCompound(String gene, String sig, RuntimePool pool){
		return false;
	}
	
	public static boolean judgeSource(Entity subject, Entity target){
		if(subject instanceof Definition){
			Definition def0 = (Definition) subject;
			if(target instanceof Definition){
				Definition def1 = (Definition) target;
				boolean mid = EntityUtil.judgeDefinitionIsAnother(def0, def1);
				return mid;
			}else{
				return false;
			}
		}else{
			//当前只考虑definition,subject is entity
			if(target instanceof Definition){
				Definition def1 = (Definition) target;
				boolean mid = EntityUtil.judgeEntityIsDefinition(subject, def1);
				return mid;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * 
	 * @param args
	 * @param props
	 * @return
	 * @throws Exception 
	 * @throws ApplicationException 
	 * @throws IOException 
	 * @throws DatabaseException 
	 */
	public static boolean matchArguments(List<Entity> args, List<Argument> props) 
			throws DatabaseException, IOException, ApplicationException, Exception{
		for(int i=0;i<args.size();i++){
			Entity arg = args.get(i);
			Argument prop = props.get(i);
			if(!(prop instanceof Property)){
				continue;
			}
			Property pro = (Property) prop;
			if(!judgeEntityInProperty(arg, pro)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean judgeEntityIsDefinition(Entity subject, Definition def){
		if(def == null){
			return true;
		}
		try {
			Definition def0 = proxyGetDefinition(subject.getClassName());
			if(def0 == null){
				return false;
			}
			String sig0 = def0.takeSignature();
			String sig1 = def.takeSignature();
			return StringUtil.matched(sig0, sig1);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean judgeEntityIsDefinition(Entity subject, String name, Expression expr)
			throws DatabaseException, IOException, ApplicationException, Exception{
		if(expr instanceof IdentifierExpression) {
			IdentifierExpression ie = (IdentifierExpression) expr;
			if(judgeEntityIsDefinition(subject, ie.getName())) {
				return true;
			}else {
				return false;
			}
		}
		if(expr instanceof BinaryOperation) {
			BinaryOperation bo = (BinaryOperation) expr;
			if(!bo.getOperator().equals("->")) {
				throw new ApplicationException();
			}
			Expression left = bo.getLeft();
			Expression right = bo.getRight();
			if(!(left instanceof IdentifierExpression)) {
				throw new ApplicationException();
			}
			IdentifierExpression ie = (IdentifierExpression) left;
			if(!judgeEntityIsDefinition(subject, ie.getName())) {
				return false;
			}
			RuntimeContext rc = (RuntimeContext) new HiwiiContext();
			Variable var = new Variable();
			var.setName(name);
			var.setValue(subject);
			Expression ret = rc.doDecision(right);
			if(ret instanceof JudgmentResult) {
				return EntityUtil.judge(ret);
			}
			throw new ApplicationException();
		}else {
			throw new ApplicationException();
		}
	}
	
	public static boolean judgeEntityIsDefinition(Entity subject, String dname)
			throws DatabaseException, IOException, ApplicationException, Exception{
		Definition def = proxyGetDefinition(dname);
		if(def == null){
			throw new ApplicationException();
		}
		return judgeEntityIsDefinition(subject, def);
	}
	
	public static boolean judgeEntityIsDefinition(Entity subject, Definition def, List<Expression> limits){
		if(def == null){
			return true;
		}
		try {
			Definition def0 = proxyGetDefinition(subject.getClassName());
			if(def0 == null){
				return false;
			}
			String sig0 = def0.takeSignature();
			String sig1 = def.takeSignature();
			return StringUtil.matched(sig0, sig1);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean judgeEntityIsDefinition(Entity subject, String dname, List<Expression> limits) 
			throws DatabaseException, IOException, ApplicationException, Exception{
		Definition def = proxyGetDefinition(dname);
		if(def == null){
			throw new ApplicationException();
		}
		try {
			Definition def0 = proxyGetDefinition(subject.getClassName());
			if(def0 == null){
				return false;
			}
			String sig0 = def0.takeSignature();
			String sig1 = def.takeSignature();
			return StringUtil.matched(sig0, sig1);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean judgeAbstractionIsAnother(Expression source, String target) 
			throws DatabaseException, IOException, ApplicationException, Exception{
		Definition def = proxyGetDefinition(target);
		if(def == null){
			throw new ApplicationException();
		}
		if(source instanceof IdentifierExpression) {
			IdentifierExpression ie = (IdentifierExpression) source;
			Definition def2 = proxyGetDefinition(ie.getName());
			if(def2 == null){
				throw new ApplicationException();
			}
			String sig = def.takeSignature();
			String sig2 = def2.takeSignature();
			return StringUtil.matched(sig2, sig);
		}
		return false;
	}
	
	public static boolean judgeDefinitionIsAnother(String subject, String target)
			throws DatabaseException, IOException, ApplicationException, Exception{
		Definition def1 = proxyGetDefinition(subject);
		Definition def2 = proxyGetDefinition(target);
		String sig0 = def1.takeSignature();
		String sig1 = def2.takeSignature();
		return StringUtil.matched(sig0, sig1);
	}
	public static boolean judgeDefinitionIsAnother(Definition subject, Definition target){
		String sig0 = subject.takeSignature();
		String sig1 = target.takeSignature();
		return StringUtil.matched(sig0, sig1);
	}
	
	public static boolean judgeValueToProperty(Entity value, Property prop){
		try {
			Definition def = proxyGetDefinition(prop.getType());
			return judgeEntityIsDefinition(value, def);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean judgeValueToType(Entity value, String type, List<Expression> limits) 
			throws DatabaseException, IOException, ApplicationException, Exception{
		if(type == null || type.length() == 0){
			return true;
		}
		Definition def = proxyGetDefinition(type);
		if(def == null){
			throw new ApplicationException();
		}
		if(!judgeEntityIsDefinition(value, def)){
			return false;
		}
		for(Expression expr:limits){
			
		}
		return true;
	}
	
	/**
	 * 获取已赋值的属性，如果未找到，则返回null
	 * @param gene
	 * @param name
	 * @param pool
	 * @return
	 */
	public static Entity getAssignment(String gene, String name, RuntimePool pool){
		return null;
	}
	
	public static Expression getImplement(String gene, String sig, RuntimePool pool){
		return null;
	}
	
	/**
	 * 该function用于方法调用中。返回任何满足signature特征的声明
	 * 如果未找到，返回null，如果找到返回declare和参数
	 * 如果runtime中未找到，则在store base中查找。
	 * 返回type：double String。
	 * 如果成功，返回声明
	 * first：real signature
	 * second:参数列表，以,分隔
	 * boolean:true表示在runtime中，false在persist中
	 * @param gene
	 * @param sig
	 * @param pool
	 * @param type
	 * @return
	 */
	public static ThreeTuple<String, String, Boolean> hasDeclare(String gene, String sig, RuntimePool pool, char type){
		boolean runt = true;
		TwoTuple<String, String> dec = pool.hasDeclare(gene, sig, type);
//		if(dec == null){
//			try {
//				dec = StoreBase.hasDeclare(gene, sig, type);
//				if(dec == null){
//					return null;
//				}
//				runt = false;
//			} catch (ApplicationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return null;
//			}
//		}
		ThreeTuple<String, String, Boolean> ret = new ThreeTuple<String, String, Boolean>(dec.getA(), dec.getB(), runt);
		return ret;
//		String name = getSignatureName(sig);
//		String start = idx.ceiling(name);
//		if(start == null){
//			return null;
//		}
//
//		String end = start;
//		while(end < idx.size() - 1){
//			end = end + 1;
//			if(getPart(idx.get(end), 0).equals(name)){
//				continue;
//			}else{
//				end = end - 1;
//				break;
//			}
//		}
//		int i = end;//从下端覆盖开始
//		while(i >= start && i <= end){
//			String ret = idx.get(i);
//			String gene1 = getPart(ret, 1);
//			String key0 = gene1 + "|" + name;
//			Map.Entry<String, String> record = map.ceilingEntry(key0);
//			while(true){
//				String key = record.getKey();
//				if(key == null){
//					break;
//				}
//				String sig1 = getPart(key, 1);
//				if(!getSignatureName(sig1).equals(name)){
//					break;
//				}
//				if(matchSignature(sig, sig1)){
//					return new DoubleString(sig1, record.getValue());
//				}
//				record = map.tailMap(key, false).firstEntry();
//			}
//		}
	}
	
	
	public static String getGene(Entity ent, RuntimePool pool){
		return ent.getGene();
	}
	
	/**
	 * identifier表达式直接返回name，function表达式由主程序获得entity参数后通过此函数返回signature.
	 * 因为获得entity必须主语介入。
	 * @param name
	 * @param args
	 * @param pool
	 * @return
	 */
	public static String getSignature(String name, List<Entity> args, RuntimePool pool){
		String ret = name + "(";
		for(Entity ent:args){
			if(ent != args.get(args.size() - 1)){
				ret = ret + ent.getGene() + ",";
			}else{
				ret = ret + ent.getGene();
			}
		}
		ret = ret + ")";
		return ret;
	}
	
	/**
	 * 是否key1覆盖key0(key1在祖先中定义，key0包含key1的基因)
	 * @param key0
	 * @param key1
	 * @return
	 */
	public static boolean isOverride(String key0, String key1){
		String name0 = getPart(key0, 0);
		String name1 = getPart(key1, 0);
		if(!name0.equals(name1)){
			return false;
		}
		String gene0 = getPart(key0, 1);
		String gene1 = getPart(key1, 1);
		if(!geneMatch(gene1, gene0)){
			return false;
		}
		String sig0 = getPart(key0, 2);
		String sig1 = getPart(key1, 2);
		if(isOverlapSignature(sig0, sig1)){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean mayProperty(String gene, String name, RuntimePool pool, boolean persist){
		
		return false;
	}
	
	public static boolean mayDefine(String gene, String name, RuntimePool pool, boolean persist){
		
		return false;
	}
	
	public static boolean mayClass(String gene, String name, RuntimePool pool, boolean persist){
		
		return false;
	}
	
	/**
	 * Expression type:identifier/FunctionDefinition
	 * @param gene
	 * @param expr
	 * @param pool
	 * @param persist
	 * @return
	 */
	public static boolean mayAware(String gene, Expression expr, RuntimePool pool, boolean persist){
		
		return false;
	}	
	
	public static String getSignatureName(String sig){
		int pos = sig.indexOf("(");
		if(pos < 0){
			return sig;
		}else{
			return sig.substring(0, pos);
		}
	}
	
	public static String getSignatureOthers(String sig){
		int pos = sig.indexOf("(");
		if(pos < 0){
			return "";
		}else{
			return sig.substring(pos);
		}
	}
	
	/**
	 * String line:"|"分隔
	 * part number:0,1,2
	 * 等待优化到n>2
	 * @return
	 */
	public static String getPart(String line, int n){
		int cnt = 0;
		int pos0 = 0, pos1 = 0;
		while(cnt <= n){
			if(pos0 >= line.length()){
				return null;
			}
			if(line.charAt(pos0) == '|'){
				if(cnt < n){
					pos0++;
					pos1 = pos0;
					cnt++;
					continue;
				}
				return "";
			}
//			int tmp = pos1;
			pos1 = line.indexOf("|", pos0);
			if(pos1 < 0){
				pos1 = line.length();
			}
			if(cnt < n){
				pos0 = pos1 + 1;
			}
			cnt++;
		}
		if(line.charAt(pos0) == '|'){
			return "";
		}
		return line.substring(pos0, pos1);
/**
 * 采用这种方式，而不采用split，是因为字符串等预备采用正则式关系。
 */
	}
	
	public static boolean isFunctionMatchDefinition(String sig, String def){
		return false;
	}
	/**
	 * sig0是否属于sig1
	 * @param sig0
	 * @param sig1
	 * @return
	 */
	public static boolean matchSignature(String sig0, String sig1){
		int pos0 = sig0.indexOf("(");
		int pos1 = sig1.indexOf("(");
		if(pos0 < 0 && pos1 < 0){
			if(sig0.equals(sig1)){
				return true;
			}else{
				return false;
			}
		}
		if((pos0 < 0 && pos1 >= 0) || (pos0 >= 0 && pos1 < 0)){
			return false;
		}
		String name0 = sig0.substring(0, pos0);
		String name1 = sig1.substring(0, pos1);
		if(!name0.equals(name1)){
			return false;
		}
		/**
		 * 保证(之间)至少一个参数，两个","没有空,最后一个字符是")"
		 */
		int st0, end0 = 0, st1, end1 = 0;
		boolean over = false;
		st0 = pos0 + 1;
		st1 = pos1 + 1;
		while(true){
			pos0 = sig0.indexOf(",", st0);
			pos1 = sig1.indexOf(",", st1);
			if((pos0 < 0 && pos1 >= 0) || (pos0 >= 0 && pos1 < 0)){
				return false;
			}
//			st0 = pos0;st1 = pos1;
			if(pos0 < 0 && pos1 < 0){
				end0 = sig0.length() - 1;
				end1 = sig1.length() - 1;
				over = true;
			}
			if(pos0 >= 0 && pos1 >= 0){
				end0 = pos0;
				end1 = pos1;
			}
			String g0 = sig0.substring(st0, end0);
			String g1 = sig1.substring(st1, end1);
			if(geneMatch(g0, g1)){
				if(over){
					break;
				}
				st0 = pos0 + 1;
				st1 = pos1 + 1;
				continue;
			}else{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 本函数确保两个signature不相互包含
	 * sig0,sig1不包括name和()
	 * signature格式：type0,type1....
	 * 暂时不考虑参数的判断说明部分
	 * 对于多参数，只要有一组参数没有重迭，就表示参数组没有重迭。
	 * @param sig0
	 * @param sig1
	 * @return false：没有重迭，true：有重迭
	 */
	public static boolean isOverlapSignature(String sig0, String sig1){
		String[] type0 = sig0.split(",");
		String[] type1 = sig1.split(",");
		if(type0.length != type1.length){
			return false;
		}
		boolean lap = false;
		for(int i=0;i<sig0.length();i++){
			if(!geneMatch(type0[i], type1[i]) && !geneMatch(type1[i], type0[i])){
				return false;
			}
		}
		return true;
	}
	/**
	 * 前者是否属于后者。
	 * @param g0
	 * @param g1
	 */
	public static boolean geneMatch(String g0, String g1){
		if(g0.length() > g1.length()){
			return false;
		}
		if(g0.equals(g1.substring(0, g0.length()))){
			return true;
		}else{
			return false;
		}
	}
	
	public static String getFunctionSignature(String gene, String name, List<Entity> args, RuntimePool pool){
		return "";
	}
	
	public static Expression doDeclare(String gene, Expression dec, char type, boolean persist, RuntimePool pool){
		return null;
	}
	
	public static Expression doDefine(Entity active, DefineSentence ds, RuntimePool pool){
		
		return null;
	}
		
	public static Expression doIdentifierAction(Entity subject, String name, RuntimePool pool){
		Expression ret = null;
		if(name.equals("break")){
			return new BreakReturn();
		}else if(name.equals("continue")){
			return new SkipReturn();
		}else if(name.equals("return")){
			return new NormalEnd();
		}
		String gene = subject.getGene();
		String sig = name;
		return null;
	}
	
	public static Expression doFunctionAction(Entity subject, String sig, RuntimePool pool){
		String name = getPart(sig, 0);
//		if(name.equals("return")){
//			if(fe.getArguments().size() == 1){
//				Entity res = doOperation(subject, fe.getArguments().get(0), pool);
//				OperationResult or = new OperationResult();
//				or.setResult(res);
//				return or;
//			}else{
//				return new HiwiiException();
//			}
//		}else if(fe.getName().equals("judge")){
//			if(fe.getArguments().size() == 1){
//				Expression res = doJudgment(subject, fe.getArguments().get(0), pool);
//				OperationResult or = new OperationResult();
//				or.setResult(res);
//				return or;
//			}else{
//				return new HiwiiException();
//			}
//		}
//		String gene = subject.getGene();
//		List<Entity> args = new ArrayList<Entity>();
//		for(Expression exp:fe.getArguments()){
//			Entity ent = doOperation(subject, exp, pool);
//			args.add(ent);
//		}
		return null;
	}
	
	
	public static Entity doIdentifierOperation(Entity subject, String name, RuntimePool pool) {
		if(name.equals("self")){//this
			return subject;
		}
		String gene = subject.getGene();
		//首先是系统定义的interface，如：Set(),List()
		//isProperty,getPer
		if(isProperty(gene, name, pool)){
			Entity ret = getAssignment(gene, name, pool);
//			if(ret == null){
//				return new Awareness(name);
//			}
			return ret;
		}
		if(isDefinition(gene, name, pool)){
			
		}
		if(isClass(gene, name, pool)){
			
		}
		//isClass,访问class对象需要索引，否则速度很慢
		//isAware
		if(isAware(gene, name, pool)){
			return new Awareness(name);
		}
		
		String sig = name + "|";//asDeclare
		Entity ret = doDeclaredOperation(subject, sig, pool);
		if(ret instanceof HiwiiException){
			//onException
		}
		return ret;
	}
	
	public static Entity doFunctionOperation(Entity subject, FunctionExpression func, RuntimePool pool) {
		String name = func.getName();
		String gene = subject.getGene();
		String sig = null;
		//isFunciontObject
		//isFunctionCognition
//		if(isCompound(gene, sig, pool)){
//			
//		}
//		if(isSynthesis(gene, sig, pool)){
//			
//		}
		//首先是系统定义的interface，如：Set(),List()
		if(name.equals("Set")){
			
		}else if(name.equals("List")){
			
		}
		Entity ret = doDeclaredOperation(subject, sig, pool);
		if(ret instanceof HiwiiException){
			//onException
		}
		return ret;
	}
	
	public static Entity doDeclaredOperation(Entity subject, String sig, RuntimePool pool) {
		String gene = subject.getGene();
		ThreeTuple<String, String, Boolean> ret = hasDeclare(gene, sig, pool, 'o');
		if(ret == null){
			return new HiwiiException();
		}
		Expression cont = null;
		if(ret.getC()){
			cont = pool.getImplement(gene, sig);
		}else{
//			cont = StoreBase.getImplement(gene, sig);
		}
		if(cont == null){
			return new HiwiiException();
		}
		return null;//subject.doOperaion(cont, pool);
	}
	
	
	
	public static Entity doIdentifierJudgment(Entity subject, String name, RuntimePool pool) {
		if(name.equals("true")){
			return new JudgmentResult(true);
		}else if(name.equals("false")){
			return new JudgmentResult(false);
		}
		return null;
	}
	
	public static Expression doBranch(Entity subject, BranchSentence bs, RuntimePool pool){
		return null;
	}
	
	public static Expression doLoop(Entity subject, LoopSentence ls, RuntimePool pool){
		return null;
	}
	public static BranchSentence getBranch(String name, List<Expression> args) throws ApplicationException{
		BranchSentence bs = new BranchSentence();
		List<Expression> judges = new ArrayList<Expression>();
		List<Expression> programs = new ArrayList<Expression>();
		Expression tail = null;
		int num = args.size();
		if(name.equals("if")){
			/* if(expression,expression,expression])第三参数为else
			 * 两个参数或三个参数，如果三个参数，第三参数为else
			 */
			if(num < 2 || num > 3){
				throw new ApplicationException("arguments wrongs!");
			}
			judges.add(args.get(0));
			programs.add(args.get(1));
			if(num == 3){
				tail =  args.get(2);
			}
		}else if(name.equals("choose")){
			/* choose(case(condition1):expression1,condition1:expression1....else:expression)
			 * choose(
			 * 	case(condition1):
			 * 		expression1,
			 * 	case(condition2):
			 * 		expression2,
			 * 	....
			 * 	else:
			 * 		expression);
			 * 最少一个case，else可以省略，else不能在中间
			 * case内条件不能空
			 */
			if(num < 1){
				throw new ApplicationException("arguments wrongs!");
			}
			int i = 0;
			for(Expression expr:args){
				if(!(expr instanceof BinaryOperation)){
					throw new ApplicationException("arguments wrongs!");
				}
				BinaryOperation bo = (BinaryOperation) expr;
				if(!bo.getOperator().equals(":")){
					throw new ApplicationException("arguments wrongs!");
				}
				Expression left = bo.getLeft();
				if(left instanceof FunctionExpression){
					FunctionExpression con = (FunctionExpression) left;
					if(!con.getName().equals("case")){
						throw new ApplicationException("arguments wrongs!");
					}
					if(con.getArguments().size() != 1){
						throw new ApplicationException("arguments wrongs!");
					}
					judges.add(con.getArguments().get(0));
					programs.add(bo.getRight());
				}else if(left instanceof IdentifierExpression){
					IdentifierExpression ie = (IdentifierExpression) left;
					if(!ie.getName().equals("else")){
						throw new ApplicationException("arguments wrongs!");
					}
					if(i != num - 1){
						throw new ApplicationException("else not tail!");
					}
					tail = bo.getRight();
				}else{
					throw new ApplicationException("arguments wrongs!");
				}
				i++;
			}
		}
		bs.setJudges(judges);
		bs.setPrograms(programs);
		bs.setElseCase(tail);
		return bs;
	}
	
	/**
	 * fe.getName == switch
	 * @param fe
	 * @return
	 * @throws ApplicationException
	 */
	public static SwitchSentence getSwitchExpression(List<Expression> args) throws ApplicationException{
		/* switch(operation, case():expression, case():expression....else:expression)
		 * switch(operation, 
		 * 	case(a [|b]):
		 * 		expression, 
		 * 	case(c):
		 * 		expression,
		 * 	....
		 * 	else:
		 * 		expression);
		 * 最少一个case，else可以省略，else不能在中间
		 */
		List<BraceExpression> results = new ArrayList<BraceExpression>();
		List<Expression> programs = new ArrayList<Expression>();
		Expression doElse = null;
		if(args.size() < 2){
			throw new ApplicationException("arguments wrongs!");
		}
		SwitchSentence ss = new SwitchSentence();
		int i = 0;
		for(Expression expr:args.subList(1, args.size())){
			if(!(expr instanceof BinaryOperation)){
				throw new ApplicationException("arguments wrongs!");
			}
			BinaryOperation bo = (BinaryOperation) expr;
			if(!bo.getOperator().equals(":")){
				throw new ApplicationException("arguments wrongs!");
			}
			Expression left = bo.getLeft();
			if(left instanceof FunctionExpression){
				FunctionExpression con = (FunctionExpression) left;
				if(!con.getName().equals("case")){
					throw new ApplicationException("arguments wrongs!");
				}
				if(con.getArguments().size() < 1){
					throw new ApplicationException("arguments wrongs!");
				}
				List<Expression> list = con.getArguments();
				BraceExpression prg = new BraceExpression();
				prg.setArray(list);
				results.add(prg);
				programs.add(bo.getRight());
			}else if(left instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) left;
				if(!ie.getName().equals("else")){
					throw new ApplicationException("arguments wrongs!");
				}
				if(i != args.size() - 2){
					throw new ApplicationException("else not tail!");
				}
				doElse = bo.getRight();
			}else{
				throw new ApplicationException("arguments wrongs!");
			}
			i++;
		}
		
		ss.setFormula(args.get(0));
		ss.setResults(results);
		ss.setPrograms(programs);
		ss.setDoElse(doElse);
		
		return ss;
	}
	/**
	 * 
	 * 
	 */
	public static Expression toLoopSentence(){
		/*
		 * while(condition,expression)
		 */
		/*
		 * each(var:collection,expression)
		 */
		/*
		 * 传统for语句
		 * for(var1=initV,condition,post,expression)
		 * 因为";"是作为操作符解析的，这里全部用","代替
		 * 减少语法难度
		 */
		/*
		 * 闭区间查找
		 * loop(var:range,start,step,wrap,forward,expression)
		 * range必须是闭区间 
		 */
		/*
		 * 从区间一端开始，
		 * loop(var:range,start,end,step,wrap,forward/backward,expression)
		 * 当前range仅限于number
		 * start默认从range左边界开始，如果start不在range内，则直接退出循环。
		 * 当range左侧开区间，则start不确定，循环不能开始
		 * end默认为range右边界，当右侧开区间，end不能确定。
		 * 当wrap为true，end默认为start
		 * step默认为1
		 * forward布尔型，默认为true
		 * wrap布尔型，默认为true
		 * 执行从start开始，到end结束，当end不在start step一侧，则wrap为true时end生效。
		 * 到达end表示到达或从forward一侧超越。
		 * wrap表示当执行到边界，自动从另一边界开始，这要求区间必须为闭区间。
		 */
		return null;
	}
	public static boolean identifierDefined(String gene, String name){//, boolean persist
		return false;
	}
	
	public static boolean declarationMatched(Declaration dec, List<Entity> args){
		return false;
	}
	
	public static List<Argument> parseArguments(List<Expression> args) throws ApplicationException{
		List<Argument> list = new ArrayList<Argument>();
		for(Expression exp:args){
			if(exp instanceof IdentifierExpression) {
				IdentifierExpression ie = (IdentifierExpression) exp;
				Argument arg = new Argument();
				arg.setName(ie.getName());
				list.add(arg);
			}else if(exp instanceof BinaryOperation) {
				BinaryOperation bo = (BinaryOperation) exp;
				if(!bo.getOperator().equals("->")) {
					throw new ApplicationException();
				}
				Expression left = bo.getLeft();
				Expression right = bo.getRight();
				if(!(left instanceof IdentifierExpression)) {
					throw new ApplicationException();
				}
				IdentifierExpression ie = (IdentifierExpression)left;
				List<Expression> states = new ArrayList<Expression>();
				
				if(right instanceof BraceExpression) {
					BraceExpression be = (BraceExpression) right;
					states = be.getArray();
				}else {
					states.add(right);
				}
				DecoratedArgument darg = new DecoratedArgument();
				darg.setName(ie.getName());
				darg.setStates(states);
				list.add(darg);
			}else {
				throw new ApplicationException();
			}
		}
		for(int i=0;i<list.size();i++){
			for(int j=i+1;j<list.size();j++){
				String n1 = list.get(i).getName();
				String n2 = list.get(j).getName();
				if(n1.equals(n2)){
					throw new ApplicationException();
				}
			}
		}
		return list;
	}
	
	public static List<String> parseArgumentString(List<Expression> args){
		List<String> list = new ArrayList<String>();
		try {
			for(Expression exp:args){
				Argument prop = parseProperty(exp);
				list.add(prop.getName());
			}
			for(int i=0;i<list.size();i++){
				for(int j=i+1;j<list.size();j++){
					String n1 = list.get(i);
					String n2 = list.get(j);
					if(n1.equals(n2)){
						throw new ApplicationException();
					}
				}
			}
			return list;
		} catch (ApplicationException e) {
			return list;
		}
	}
	
	public static boolean validArguments(List<String> args){
		for(int i=0;i<args.size();i++){
			for(int j=i+1;j<args.size();j++){
				String n1 = args.get(i);
				String n2 = args.get(j);
				if(n1.equals(n2)){
					return false;
				}
			}
		}
		return true;
	}
	public static Argument parseProperty(Expression expr) throws ApplicationException{
		
		try {
			if(expr instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) expr;
				Argument arg = new Argument();
				arg.setName(ie.getName());
				return arg;
			}
			Property prop = new Property();
			if(!(expr instanceof BinaryOperation)){
				 throw new ApplicationException();
			}
			BinaryOperation bo = (BinaryOperation) expr;
			if(!bo.getOperator().equals("->")){
				throw new ApplicationException();
			}
			List<Expression> limits = null;
			Definition type = null;
			Expression right = bo.getRight();
			if(right instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) right;
				String name = ie.getName();
				type = proxyGetDefinition(name);
				if(type == null){
					throw new ApplicationException();
				}
				prop.setType(type.getName());
			}else if(right instanceof IdentifierBrace){
				IdentifierBrace ib = (IdentifierBrace) right;			
				String name = ib.getName();
				type = proxyGetDefinition(name);
				if(type == null){
					throw new ApplicationException();
				}
				limits = ib.getConditions();
				prop.setType(type.getName());
				prop.setLimits(limits);
			}
			
			Expression left = bo.getLeft();
			
			if(left instanceof IdentifierExpression){
				prop.setName(((IdentifierExpression) left).getName());
			}
			else{
				throw new ApplicationException();
			}
			return prop;
		} catch (Exception e) {
			throw new ApplicationException();
		}
	}
	
	public static VariableStore variableToStore(Variable var){
		VariableStore ret = new VariableStore();
		ret.setType(var.getType());
		ret.setLimits(var.getLimits());
		Entity value = var.getValue();
//		Definition def = EntityUtil.proxyGetDefinition(value.getClassName());
	    if(value instanceof HiwiiInstance){
	    	HiwiiInstance inst = (HiwiiInstance) value;
	    	ret.setValueType('i');
	    	ret.setValue(inst.getUuid());
	    }else{
	    	ret.setValueType('s');
	    	ret.setValue(value.toString());
	    }
	    return ret;
	}
	
	/**
	 * 从pos位置向前或向后数len，返回新的pos
	 * @param pos
	 * @param len
	 * @param forward
	 * @return
	 */
	public static int countStep(int pos, int len, boolean forward){
		if(forward){
			return pos + len;
		}else{
			return pos - len;
		}
	}
	
	public static boolean matchRepeat(String str, RegularExpression re, int n, int pos){
		if(n == 0){
			return true;
		}else if(n == 1){
			return re.match(str);
		}
		
		int start = pos, end = 0;
		int len = str.length();
		int times = n;
		String head = "";
		String tail = "";
		if(re instanceof SimpleRegular){
			SimpleRegular sr = (SimpleRegular) re;
			String ptn = sr.getPattern();
			int pl = ptn.length();
			if(str.substring(start, start + pl).equals(ptn)){
				start = start + pl;
				return matchRepeat(str, re, times--, start);
			}else{
				return false;
			}
		}
		while(start <= len){
			head = str.substring(pos, start);
			if(re.match(head)){
				start++;
				boolean boo = matchRepeat(str, re, times--, start);
				if(boo){
					return true;
				}else{
					start = pos;
				}
			}else{
				start = 0;
			}
		}
		return false;
	}
	
	public static List<MatchResult> matching(String str, MatchResult mr, List<RegularExpression> regs, int start){
//		MatchResult m = new MatchResult();
//		List<MatchResult> results = new ArrayList<MatchResult>();
		List<MatchResult> results = mr.getGroups();
		int end = start;
		MatchResult mrl = null;
		RegularExpression head = null;
		List<RegularExpression> others = regs.subList(1, regs.size());
		while(end <= str.length()){
			head = regs.get(0);
//			m.setGroups(results);
			
			if(head instanceof MatchGroup){
				MatchGroup mg = (MatchGroup) head;
				
				MatchResult ret = mr.group(mg.getArguments());
				if(ret == null){
					return null;//match group is not valid
				}
				SimpleRegular sr = new SimpleRegular(ret.catchString(str));
				mrl = sr.matchResult(str, mr, end, end);
			}else{
				mrl = head.matchResult(str, mr, end, end);//left matchResult
			}
			if(mrl == null){
				results.clear();				
				end++;
				continue;
			}else{
				results.add(mrl);
				end = mrl.getEnd();
			}
//			end++;
			if(others.size() == 1){
				RegularExpression re = others.get(0);
				MatchResult tmp = new MatchResult();
				if(re instanceof MatchGroup){
					MatchGroup mg = (MatchGroup) re;
					MatchResult ret = mr.group(mg.getArguments());
					if(ret == null){
						return null;//match group is not valid
					}
					SimpleRegular sr = new SimpleRegular(ret.catchString(str));
					tmp = sr.matchResult(str, mr, end, end);
				}else{
					tmp = re.matchResult(str, mr, end, end);//left matchResult
				}
				if(tmp != null){
					results.add(tmp);
					return results;
				}else{
					results.clear();
					end++;
				}
			}else{
				List<MatchResult> ret = matching(str, mr, others, end);
				if(ret == null){
					results.clear();
					end++;
				}else{
//					results.addAll(ret);
					return results;
				}
			}
		}
		return null;
	}
	
	public static int toNumber(IntegerNumber in){
		return Integer.parseInt(in.getValue());
	}
	/**
	 * property访问有如下情况：
	 * 1,prop: identifier/function.
	 * 2,prop with host. host分null,entity,abstraction
	 * 3,prop with limit
	 * 以上三个维度可以交叉。
	 * name#0..n|host
	 * if host=null,separator = !
	 * if host=abstraction,separator = @
	 * if host=entity,separator = ~ for number, $ for string, * for others
	 * entity可以是number、string
	 * @param prop
	 * @return
	 */
	public static String getPropertyKey(Property prop){
		String num = "0";
		if(prop instanceof FunctionProperty){
			FunctionProperty fp = (FunctionProperty) prop;
			num = String.valueOf(fp.getArguments().size());
		}
		return "";
	}
	
	public static String getUUID(){
		UUID uuid = UUID.randomUUID();
		String ret = uuid.toString().replace("-", "");
		return ret;
	}
	
	/**
	 * [-]yyyy.MM.dd HH:mm:ss
	 * -表示公元前
	 * @param str
	 * @return
	 */
	public static Calendar getTimeObject(String str){
		if(str.length() == 0){
			return null;
		}

		Calendar ret = Calendar.getInstance();
		try {
			int year = 0, month = 1, day = 1, hour = 0, minu = 0, sec = 0;
			int pos = str.indexOf('.');
			if(pos == str.length() - 1){
				return null;
			}
			if(pos > 0){
				year = Integer.parseInt(str.substring(0, pos));
			}else{
				year = Integer.parseInt(str);
				ret.set(year, month - 1, day, hour, minu, sec);
				return ret;
			}
			int begin = pos;
			pos = str.indexOf('.', begin + 1);
			if(pos == str.length() - 1){
				return null;
			}
			if(pos > 0){
				month = Integer.parseInt(str.substring(begin + 1, pos));
			}else{
				month = Integer.parseInt(str);
				ret.set(year, month - 1, day, hour, minu, sec);
				return ret;
			}
			begin = pos;
			pos = str.indexOf(' ', begin + 1);
			if(pos == str.length() - 1){
				return null;
			}
			if(pos > 0){
				day = Integer.parseInt(str.substring(begin + 1, pos));
			}else{
				day = Integer.parseInt(str);
				ret.set(year, month - 1, day, hour, minu, sec);
				return ret;
			}
			begin = pos;
			pos = str.indexOf(':', begin + 1);
			if(pos == str.length() - 1){
				return null;
			}
			if(pos > 0){
				hour = Integer.parseInt(str.substring(begin + 1, pos));
			}else{
				hour = Integer.parseInt(str);
				ret.set(year, month - 1, day, hour, minu, sec);
				return ret;
			}
			begin = pos;
			pos = str.indexOf(':', begin + 1);
			if(pos == str.length() - 1){
				return null;
			}
			if(pos > 0){
				minu = Integer.parseInt(str.substring(begin + 1, pos));
			}else{
				minu = Integer.parseInt(str);
				ret.set(year, month - 1, day, hour, minu, sec);
				return ret;
			}
			begin = pos;
			sec = Integer.parseInt(str.substring(begin + 1));
			ret.set(year, month - 1, day, hour, minu, sec);
			return ret;
		} catch (NumberFormatException e) {
			return null;
		}
		
	}
	
	public static Calendar DateToCalendar(String str) 
	{ 
		Calendar cal = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			Date date = sdf.parse(str);
			cal=Calendar.getInstance();
			cal.setTime(date);
			return cal;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static TimeValue timeNow(){
		TimeValue ret = new TimeValue();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		ret.setValue(sdf.format(cal.getTime()));
		return ret;
	}
	
	public static TimeValue time(String str){
		try {
			TimeValue ret = new TimeValue();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			Date date = sdf.parse(str);
			ret.setValue(str);
			return ret;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static TimeValue time(int year, int month, int day){
		TimeValue ret = new TimeValue();
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		ret.setValue(sdf.format(cal.getTime()));
		return ret;
	}
	
	public static TimeValue time(int year, int month, int day, int hour, int minute){
		TimeValue ret = new TimeValue();
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day, hour, minute);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		ret.setValue(sdf.format(cal.getTime()));
		return ret;
	}
	
	public static TimeValue time(int year, int month, int day, int hour, int minute, int second){
		TimeValue ret = new TimeValue();
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day, hour, minute, second);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		ret.setValue(sdf.format(cal.getTime()));
		return ret;
	}
	
	public static String getKey(String name){
		String key = name;		
		return key;
	}
	
	/**
	 * from=localHost
	 * @param name
	 * @param num
	 * @return
	 */
	public static String getKey(String name, String num){
		if(num.equals("0")){
			return name;
		}
		String key = name + "#" + num;		
		return key;
	}
	
	public static String getKey(Entity from, String name, String num){
		if(from == null){
			return getKey(name, num);
		}
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
			}else if(from instanceof Perception){
				Perception pr = (Perception) from;
				key = name  + nt + pr.getSignature();
			}else{
//				String dd = from.getDefinitionName();
//				Entity ret = doContextIdentifierCalculation(dd);
				//				(Definition) ret;
				Definition def = null;//from.getDefinition();//contextGetDefinition(dd);
				//赋值key=signature + ":" + entity.toString
				key = name  + nt + def.takeSignature() + ":" + from.toString();
			}
		}
		return key;
	}
	
	public static List<Entity> sortList(List<Entity> input, boolean asc){
		List<Entity> list = new ArrayList<Entity>();
		SortUtil su = new SortUtil(asc);//asc = true
		su.quickSort(input, 0, input.size() - 1);
		return list;
	}
	
	public static Expression randomInteger(IntegerNumber start, IntegerNumber end){
		BigInteger arg1 = new BigInteger(start.getValue());
		BigInteger arg2 = new BigInteger(end.getValue());
		return randomInteger(arg1, arg2);
	}
	
	public static Expression randomInteger(BigInteger start, BigInteger end){
		if(end.compareTo(start) < 0){
			return new HiwiiException();
		}
		BigDecimal ran = new BigDecimal(Math.random());
		BigInteger add = new BigDecimal(end.subtract(start)).multiply(ran).toBigInteger();
		IntegerNumber in = new IntegerNumber(start.add(add).toString());
		return in;
	}
	
	public static DateValue parseDate(String str) throws ApplicationException{
		StringExpression se = new StringExpression(str);
		Expression expr = se.toExpression();
		if(!(expr instanceof IntegerNumber)){
			throw new ApplicationException();
		}
		IntegerNumber in = (IntegerNumber) expr;
		DateValue dv = new DateValue();
		dv.setValue(in.toString());
		return dv;
	}
	
	public static TimeValue parseTime(String str) throws ApplicationException{
		if(str.length() != 6){
			throw new ApplicationException();
		}
		for(int i = 0;i<6;i++){
			char ch = str.charAt(i);
			if(!Character.isDigit(ch)){
				throw new ApplicationException();
			}
		}
		TimeValue tv = new TimeValue();
		tv.setValue(str);
		return tv;
	}
	
	public static Definition proxyGetDefinition(String name)
			throws IOException, DatabaseException, ApplicationException, Exception{
		if(name == null){
			return null;
		}
		if(SystemDefinition.contains(name)){
			return SystemDefinition.defs.get(name);
		}
//		if(name.equals("use")){
//			User user = new User();
//			return new User
//		}
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		
		Definition def = db.getDefinitionByName(name);
		
		return def;
	}
	
	public static Definition proxyGetInstanceDefinition(String name)
			throws IOException, DatabaseException, ApplicationException, Exception{
		if(name == null){
			return null;
		}
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		
		Definition def = db.getDefinitionByName(name);
		
		return def;
	}
	
	public static boolean proxyHasState(String name)
			throws IOException, DatabaseException, ApplicationException, Exception{
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();			
		return db.hasStatus(name, null);
	}
	
	public static Property proxyGetProperty(String name)
			throws IOException, DatabaseException, ApplicationException, Exception{
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		
		Property ret = db.getProperty(name, null);
		
		return ret;
	}
	
	public static Property proxyGetProperty(String name, Definition def)
			throws IOException, DatabaseException, ApplicationException, Exception{
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Property ret = null;
		if(def != null){
			String key = name + "@" +  def.getSignature();
			ret = db.getLikeProperty(key, null);
			if(ret != null) {
				return ret;
			}
		}
		ret = db.getProperty(name, null);
		//TODO if exists system property
		return ret;
	}
	
	public static Property proxyGetProperty(String name, String type)
			throws IOException, DatabaseException, ApplicationException, Exception{

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Property ret = null;
		ret = db.getProperty(name, null);
		if(ret != null) {
			return ret;
		}
		Definition def = proxyGetDefinition(type);
		if(def != null){
			ret = db.getProperty(name, type, null);
//			ret = def.getProps().get(name);
//			if(ret != null){
//				return ret;
//			}
//			Definition parent = EntityUtil.proxyGetDefinition(def.getParent());
//			while(parent != null){
//				return proxyGetProperty(name, parent.getName());
//			}
		}
//		ret = db.getProperty(name, null);
		
		return ret;
	}
	
	public static boolean proxyHasPart(String name, Definition def) 
			throws DatabaseException, IOException, ApplicationException, Exception{
		Definition pdef = proxyGetDefinition(name);
		if(pdef == null){
			return false;
		}
		if(hasPart(pdef.getSignature(), def)){
			return true;
		}
		Definition parent = proxyGetDefinition(def.getParent());
		while(parent != null){
			if(hasPart(parent.getSignature(), def)){
				return true;
			}
			parent = proxyGetDefinition(parent.getParent());
		}
		return false;
	}
	
	public static boolean hasPart(String sign, Definition def){
//		for(String str:def.getParts().keySet()){
//			if(StringUtil.matched(sign, str)){
//				return true;
//			}
//		}
		return false;
	}
	public static Assignment proxyGetAssignment(String name)
			throws IOException, DatabaseException, ApplicationException, Exception{
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		
		Assignment ret = db.getAssignment(name, null);
		
		return ret;
	}
	
	public static Assignment hasDefinitionAssignment(String name, Definition def)
			throws IOException, DatabaseException, ApplicationException, Exception{
//		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Assignment ret = null;
		if(def != null){
//			if(def.getAssignments().containsKey(name)){
//				return def.getAssignments().get(name);
//			}
//			Definition parent = EntityUtil.proxyGetDefinition(def.getParent());
//			while(parent != null){
//				if(parent.getProps().containsKey(name)){
//					return parent.getAssignments().get(name);
//				}
//				parent = EntityUtil.proxyGetDefinition(parent.getParent());
//			}			
		}
//		ret = db.getProperty(name, null);
		//TODO if exists system property
		return ret;		
	}
	
	public static Assignment hasInstanceAssignment(String name, HiwiiInstance inst)
			throws IOException, DatabaseException, ApplicationException, Exception{
//		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Assignment ret = null;
		if(inst.getAssignments().containsKey(name)){
			return inst.getAssignments().get(name);
		}
		Definition def = proxyGetDefinition(inst.getClassName());
		return hasDefinitionAssignment(name, def);		
	}
	
	public static JudgmentResult hasDefinitionJudgment(String name, Definition def)
			throws IOException, DatabaseException, ApplicationException, Exception{
//		Judgment ret = null;
		if(def != null){
//			if(def.getJudgments().containsKey(name)){
//				return def.getJudgments().get(name);
//			}
//			Definition parent = EntityUtil.proxyGetDefinition(def.getParent());
//			while(parent != null){
//				if(parent.getProps().containsKey(name)){
//					return parent.getJudgments().get(name);
//				}
//				parent = EntityUtil.proxyGetDefinition(parent.getParent());
//			}			
		}
		return null;		
	}
	
	public static JudgmentResult hasInstanceJudgment(String name, HiwiiInstance inst)
			throws IOException, DatabaseException, ApplicationException, Exception{
		if(inst.getAssignments().containsKey(name)){
			return inst.getJudgments().get(name);
		}
		Definition def = proxyGetDefinition(inst.getClassName());
		return hasDefinitionJudgment(name, def);		
	}
	
	public static Entity proxyGetEntity(String name)
			throws IOException, DatabaseException, ApplicationException, Exception{
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		
		Assignment ass = db.getAssignment(name, null);
		if(ass != null){
			return ass.getValue();
		}
//		Reference ret = db.getProperty(name, null);
//		if(ret == null){
//			Entity ret = db.getInstance(name, null);
//		}
		
		return null;
	}

	public static Expression proxyGetIdCalculation(String name, Definition def)
			throws IOException, DatabaseException, ApplicationException, Exception{
		return null;
	}
	/**
	 * 根据entity，entityType，host，hostType判断entity是否可以增加。
	 * @param uid
	 * @param hostId
	 * @param hostType
	 * @param entType
	 * @return
	 * @throws IOException
	 * @throws DatabaseException
	 * @throws ApplicationException
	 * @throws Exception
	 */
	public static boolean shouldBeInPart(HiwiiInstance ent, Entity subject)
			throws IOException, DatabaseException, ApplicationException, Exception{
		HiwiiInstance host = null;
		if(subject instanceof HiwiiInstance){
			host = (HiwiiInstance) subject;
		}
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Definition hostDef = EntityUtil.proxyGetDefinition(host.getClassName());
	    if(hostDef == null){
	    	throw new ApplicationException();
	    }
//	    HiwiiInstance host = db.getInstanceById(hostId);
	    Definition type = EntityUtil.proxyGetDefinition(ent.getClassName());
	    String sign = type.getSignature();
	    Definition parent = hostDef;
//	    boolean found = false;
	    while(parent != null){
//	    	for(String str:hostDef.getParts().keySet()){
//	    		if(!StringUtil.matched(sign, str)){
//	    			continue;
//	    		}
//	    		EntityPart ep = hostDef.getParts().get(str);
//	    		int count = db.howManyChild(host.getUuid(), ep.getType());
//	    		return ep.satisfy(count + 1);
//	    	}
	    	parent = EntityUtil.proxyGetDefinition(parent.getParent());
	    }
		return false;
	}

	public static boolean judgeEntityInProperty(Entity ent, Property prop) 
			throws DatabaseException, IOException, ApplicationException, Exception {
		String cn = ent.getClassName();
		String type = prop.getType();
		if(cn.equals(type)){
			return true;
		}

		Definition def0 = proxyGetDefinition(cn);
		Definition def1 = proxyGetDefinition(type);

		if(StringUtil.matched(def0.getSignature(), def1.getSignature())){
			Expression ret = judgeEntityLimit(ent, prop.getLimits());
			if(EntityUtil.judge(ret)){
				return true;
			}
		}
		return false;
	}
	
	public static Expression judgeEntityLimit(Entity ent, List<Expression> limits){
		//Instance or Entity is 状态载体
		SessionContext sc = LocalHost.getInstance().newSessionContext();
		for(Expression expr:limits){
			Expression ret = sc.doDecision(ent, expr);
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
	/**
	 * names和ents长度相同，返回对应name的entity
	 * @param name
	 * @param ents
	 * @param names
	 * @return
	 */
	public static Entity getEntityInList(String name, List<Entity> ents, List<String> names){
		int i = 0;
		boolean found = false;
		for(String str:names){
			if(name.equals(str)){
				break;
			}
			i++;
		}
		if(found){
			return ents.get(i);
		}else{
			return null;
		}
	}
	
	public static String getSignture(Entity target){
		if(target instanceof Definition){
			Definition def = (Definition) target;
			return def.takeSignature();
		}else{
			String dd = target.getClassName();

			return "tempString";
			//TODO definition.signature+"%" + id;
		}
	}
	
//	public static User statementToUser(List<Expression> states) throws ApplicationException{
//		User user = new User();
//		for(Expression expr:states){
//			if(!(expr instanceof BinaryOperation)){
//				throw new ApplicationException("wrong syntax!");
//			}
//			BinaryOperation bo = (BinaryOperation) expr;
//			if(!bo.getOperator().equals(":=")){
//				throw new ApplicationException("wrong syntax!");
//			}
//			Expression left = bo.getLeft();
//			if(!(left instanceof IdentifierExpression)){
//				throw new ApplicationException("wrong syntax!");
//			}
//			IdentifierExpression ie = (IdentifierExpression) left;
//			String prop = ie.getName();
//			Entity val = ass.getValue();
//			if(!(val instanceof StringExpression)){
//				throw new ApplicationException();
//			}
//			String value = ((StringExpression)val).getValue();
//			if(ass.getName().equals("name")){
//				user.setUserName(value);
//			}else
//			if(ass.getName().equals("password")){
//				user.setPassword(value);
//			}
//		}
//		return user;
//	}
	
	public static User instanceToUser(HiwiiInstance inst) throws ApplicationException{
		User user = new User();
		for(Assignment ass:inst.getAssignments().values()){
			Entity val = ass.getValue();
			if(!(val instanceof StringExpression)){
				throw new ApplicationException();
			}
			String value = ((StringExpression)val).getValue();
			if(ass.getName().equals("id")){
				user.setUserid(value);
			}else if(ass.getName().equals("password")){
				user.setPassword(value);
			}
//			else if(ass.getName().equals("name")){
//				user.setUserName(value);
//			} name and idCard\mail is in other table.
		}
		if(user.getUserid() == null){
			throw new ApplicationException("userId is null!");
		}
		return user;
	}
	
	public static HiwiiInstance userToInstance(User user) throws ApplicationException{
		HiwiiInstance inst = new HiwiiInstance();
//		Assignment id = new Assignment();
//		id.setName("id");
//		id.setValue(new StringExpression(user.getUserid()));
//		inst.getAssignments().put("id", id);
		
		if(user.getPassword() != null){
			Assignment pass = new Assignment();
			pass.setName("password");
			pass.setValue(new StringExpression(user.getPassword()));
			inst.getAssignments().put("password", pass);
		}
		inst.setClassName("User");
		return inst;
	}
	
	public static Assignment recordToAssignment(StoredValue rec) 
			throws DatabaseException, IOException, ApplicationException, Exception{
		Assignment ass = new Assignment();
		Entity ent = recordToEntity(rec);
		ass.setValue(ent);
		return ass;
	}
			
	/**
	 * i:int
	 * t:time
	 * u:user
	 * s:String
	 * e:entity
	 * 
	 * @param ent
	 * @return
	 * @throws ApplicationException
	 */
	public static StoredValue entityToRecord(Entity ent) throws ApplicationException{
		StoredValue rec = new StoredValue();
		if(ent instanceof HiwiiInstance){
			HiwiiInstance inst = (HiwiiInstance) ent;
			rec.setType('i');
			rec.setValue(inst.getUuid());
		}else if(ent instanceof TimeObject){
			TimeObject t = (TimeObject) ent;
			rec.setType('t');
			rec.setValue(String.valueOf(t.getTime().getTimeInMillis()));
		}else if(ent instanceof User){
			User user = (User) ent;
			rec.setType('u');
			rec.setValue(user.getUserid());
		}else if(ent instanceof StringExpression){
			rec.setType('s');	
			rec.setValue(ent.toString());
		}else{
			rec.setType('e');	
			rec.setValue(ent.toString());
		}
		return rec;
	}
	
	public static Entity recordToEntity(StoredValue rec) 
			throws DatabaseException, IOException, ApplicationException, Exception{
		char type = rec.getType();
		String str = rec.getValue();
		if(type == 'e'){
			Expression expr = StringUtil.parseString(str);
			SessionContext sc = LocalHost.getInstance().newSessionContext();
			Entity ent = sc.doCalculation(expr);
			return ent;
		}else if(type == 't'){
			TimeObject time = new TimeObject();
			long val = Long.parseLong(str);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(val);
			time.setTime(cal);
			return time;
		}else if(type == 'u'){
			HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
			Entity ent = db.getUser(str, null);
			return ent;
		}else if(type == 's'){
			return new StringExpression(str);
		}else  if(type == 'i'){
			try {
				HiwiiInstance hi = LocalHost.getInstance().getHiwiiDB().getInstanceById(str);
				return hi;
			} catch (Exception e) {
				throw new ApplicationException();
			}
		}
		return new HiwiiException();
	}
	
	public static Entity doInstanceIdentifierCalculation(HiwiiInstance inst, String name,
			HiwiiContext cxt) {
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			Definition def = proxyGetDefinition(inst.getClassName());
			if(def == null){
				return new HiwiiException();
			}
			if(proxyHasPart(name, def)){
				Definition part = proxyGetDefinition(name);
				String sign = part.getSignature();
				return db.getPartEntity(inst.getUuid(), sign, null);
			}
			Property prop = EntityUtil.proxyGetProperty(name, def);
			if(prop != null){
				Entity ret = db.getInstanceProp(inst.getUuid(), name, null);
				return ret;
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
	
	public static Entity doInstanceIdentifierCalculation(String instId, String name,
			HiwiiContext cxt) {
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			Definition def = db.getInstanceClassName(instId);
			if(def == null){
				return new HiwiiException();
			}
//			if(proxyHasPart(name, def)){
//				Definition part = proxyGetDefinition(name);
//				String sign = part.getSignature();
//				return db.getPartEntity(instId, sign, null);
//			}
			Property prop = EntityUtil.proxyGetProperty(name, def);
			if(prop != null){
				Entity ret = db.getInstanceProp(instId, name, null);
				return ret;
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
	
	public static Entity doEntityCalculation(Entity subject, Expression expr,
			HiwiiContext cxt) {
		if(expr instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) expr;
			return subject.doIdentifierCalculation(ie.getName());
		}else if(expr instanceof FunctionExpression){
			FunctionExpression fe = (FunctionExpression) expr;
			List<Entity> list = new ArrayList<Entity>();
			for(Expression arg:fe.getArguments()){
				Entity ent = cxt.doCalculation(arg);
				list.add(ent);
			}
			return subject.doFunctionCalculation(fe.getName(), list);
		}
		return null;
	}
	
	public static LambdaMapping toLambaExpression(Expression expr){
		LambdaMapping le = new LambdaMapping();
		if(expr instanceof BinaryOperation){
			BinaryOperation bo = (BinaryOperation) expr;
			if(bo.getOperator().equals("=>")){
				if(bo.getLeft() instanceof IdentifierExpression){
					IdentifierExpression ie = (IdentifierExpression) bo.getLeft();
					le.getKeys().add(ie.getName());
					le.setStatement(bo.getRight());
					return le;
				}
			}
		}
		le.setStatement(expr);
		return le;
	}
	
	public static List<String> getFunctionArgument(FunctionExpression fe)throws ApplicationException {
		List<String> ret = new ArrayList<String>();
		for(Expression arg:fe.getArguments()) {
			if(arg instanceof IdentifierExpression) {
				IdentifierExpression ie = (IdentifierExpression) arg;
				ret.add(ie.getName());
			}else if(arg instanceof BinaryOperation) {
				BinaryOperation bo = (BinaryOperation) arg;
				if(!bo.getOperator().equals("->")) {
					throw new ApplicationException();
				}
				if(bo.getLeft() instanceof IdentifierExpression) {
					IdentifierExpression ie = (IdentifierExpression) bo.getLeft();
					ret.add(ie.getName());
				}else {
					throw new ApplicationException();
				}
			}else {
				throw new ApplicationException();
			}
		}
		if(StringUtil.hasNoDup(ret)) {
			return ret;
		}else {
			throw new ApplicationException();
		}
	}
	
	public static List<Expression> getFunctionArgumentType(FunctionExpression fe)throws ApplicationException {
		List<Expression> ret = new ArrayList<Expression>();
		for(Expression arg:fe.getArguments()) {
			if(arg instanceof IdentifierExpression) {
				ret.add(new NullExpression());
			}else if(arg instanceof BinaryOperation) {
				BinaryOperation bo = (BinaryOperation) arg;
				if(!bo.getOperator().equals("->")) {
					throw new ApplicationException();
				}
				if(bo.getLeft() instanceof IdentifierExpression) {
					Expression right = bo.getRight();
					ret.add(right);
				}else {
					throw new ApplicationException();
				}
			}else {
				throw new ApplicationException();
			}
		}
		return ret;
	}
}
