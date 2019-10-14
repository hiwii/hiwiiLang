package net.hiwii.context;

import java.util.HashMap;
import java.util.Map;

import net.hiwii.context.path.PathInfo;
import net.hiwii.expr.BraceExpression;
import net.hiwii.refer.PerceptionRefer;

/**
 * 在以identifier形式调用program的情况下使用。
 * 程序执行的类型：程序可以作为operation、action、judgment执行。
 * 只有作为action执行时，subject对象可以改变。
 * 分支和循环语句作为特殊动作语句，可以在任何类型中执行。
 * 
 * declares key格式:
 * [a|o|j]|name|argument number|types|conditions|parameter conditions
 * types|conditions在函数定义下有效
 * parameter conditions表示参数间关系，则function(函数,condition)小有效
 * value:
 * 参数名list + 执行 expression
 * 
 * perception:
 * 以definitionId作为所有，list<Entity>作为内容，用于临时存储感知对象
 * 
 * 每一级runtimeContext包含：
 * List<Entity>用于记录认识的对象
 * List<EntityInfo>用于记录定义信息，包括属性/判断/等
 * 
 * NavigableMap<String,EntityInfo> idx_defs用于通过定义名索引到属性
 * @author ha-wangzhenhai
 *
 */
public class RuntimeContext extends HiwiiContext{
	private String path;
	private char type;
//	private Entity host;
	private Map<String,PathInfo> pathMap;
		
//	private NavigableMap<String,Entity> variables;
	/*
	 * 记录最新perceptionId。可以是会话，也可以是持久
	 */
	private PerceptionRefer just;
//	private boolean memory;//true for session cache, false for persist
	/*
	 * just记录最新的perception，以便引用。
	 * just中对象分属不同定义，以便通过定义区分。
	 * just按照时间排序，超过justLen，则删除最早的对象。
	 */
//	private List<Entity> just;
//	private int justLen = 3;
	
//	private NavigableMap<String,Entity> entities;
//	private NavigableSet<EntityInfo> definitions;
//	private NavigableMap<String,EntityInfo> idx_defs;
//	
//	private NavigableMap<String,Reference> refers;
	
	public RuntimeContext() {
//		variables = new TreeMap<String,Entity>();
//		props = new TreeMap<String,Property>();
		
//		refers = new TreeMap<String,Reference>();		
//		
//		entities = new TreeMap<String,Entity>();
//		
//		idx_defs = new TreeMap<String,EntityInfo>();
		
		pathMap = new HashMap<String,PathInfo>();
		
		setPath("");
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public PerceptionRefer getJust() {
		return just;
	}
	public void setJust(PerceptionRefer just) {
		this.just = just;
	}
	
	//20150317
	private BraceExpression program;
	public BraceExpression getProgram() {
		return program;
	}
	public void setProgram(BraceExpression program) {
		this.program = program;
	}
}
