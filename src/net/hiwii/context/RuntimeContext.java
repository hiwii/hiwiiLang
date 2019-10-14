package net.hiwii.context;

import java.util.HashMap;
import java.util.Map;

import net.hiwii.context.path.PathInfo;
import net.hiwii.expr.BraceExpression;
import net.hiwii.refer.PerceptionRefer;

/**
 * ����identifier��ʽ����program�������ʹ�á�
 * ����ִ�е����ͣ����������Ϊoperation��action��judgmentִ�С�
 * ֻ����Ϊactionִ��ʱ��subject������Ըı䡣
 * ��֧��ѭ�������Ϊ���⶯����䣬�������κ�������ִ�С�
 * 
 * declares key��ʽ:
 * [a|o|j]|name|argument number|types|conditions|parameter conditions
 * types|conditions�ں�����������Ч
 * parameter conditions��ʾ�������ϵ����function(����,condition)С��Ч
 * value:
 * ������list + ִ�� expression
 * 
 * perception:
 * ��definitionId��Ϊ���У�list<Entity>��Ϊ���ݣ�������ʱ�洢��֪����
 * 
 * ÿһ��runtimeContext������
 * List<Entity>���ڼ�¼��ʶ�Ķ���
 * List<EntityInfo>���ڼ�¼������Ϣ����������/�ж�/��
 * 
 * NavigableMap<String,EntityInfo> idx_defs����ͨ������������������
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
	 * ��¼����perceptionId�������ǻỰ��Ҳ�����ǳ־�
	 */
	private PerceptionRefer just;
//	private boolean memory;//true for session cache, false for persist
	/*
	 * just��¼���µ�perception���Ա����á�
	 * just�ж��������ͬ���壬�Ա�ͨ���������֡�
	 * just����ʱ�����򣬳���justLen����ɾ������Ķ���
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
