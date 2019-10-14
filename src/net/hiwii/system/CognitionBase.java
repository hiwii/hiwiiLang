package net.hiwii.system;

import java.util.NavigableMap;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.system.util.SortedList;
import net.hiwii.view.Entity;

/**
 * 
 * @author ha-wangzhenhai
 * ���е�cognition�Զ���Ϊ���߱��棬����״̬��runtime��persist����״̬��
 * �����Ҫ��cognition����Ϊ�ַ������棬�����ַ����ܹ������cognition��
 * cognition���ʴӴ����巢��
 * ÿ�����庬�����ȶ���Ļ���
 * 
 * ������ʶ�����ⶨ�壬��Ϊ�����������սڵ㣬��Ҷ��
 * ����-----��s
 * |
 * ����-----��s
 * |
 * |
 * |
 * ����-----��s
 * |
 * ����-----��s
 * ÿ������ֻ��һ�������壬ÿ������ֻ��һ�����塣
 * ÿ�������ж���࣬��Ϊmixin��
 * ��Ͷ����Լ�����������������£�
 * �ڲ�����(����/��/����)
 * 
 * ����
 * �ṹ����
 * ���Զ���
 * 
 * ����ʵ��
 * �ṹ����
 * ��������(�������ж���)
 * 
 * �ж�
 * ����
 * 
 * ����AOP
 * ����interface�﷨
 * 
 * ��ʶԪ��aware
 * 
 * ��ΪҶ�Ӷ��󣬻�����չ������չ���Ժ���չ�������ֱ��¼�ر������Ϣ��������Ϣ��ִ�д��롣
 * 
 * ��ʶΪ�������񣬼�ΪdoOperation/doAction/doJudgment����
 * ִ������������ʵ�֣�����ʵ��������������һ�����������ж������ʵ�֡�
 * ����ʵ��һ��ֻ��һ����Ч(override)������֧������(overload)��
 * Ѱ������ʵ�ִӶ���ʼ��Ȼ���Ƕ�����࣬Ȼ���Ƕ���Ķ��壬Ȼ����ͬ��������࣬����Ѱ�ҡ�
 * ����ͨ�������ӿ�Ѱ���ٶȡ�
 * 
 */
public class CognitionBase {
	/**
	 * ÿ������������gene|signature|path+���ݹ���
	 * gene�����˲���˳��
	 * ������������
	 * 1��:signature|gene|path--���ڿ��ٲ���
	 * 1��:path|signature|gene--������������
	 */
	/**
	 * internal definition
	 * defMap key:gene|signature#path
	 * defMap value:gene
	 * defIdx:signature:gene|path
	 * defPath:path|signature|gene
	 */
	private NavigableMap<String,String> defMap;
	private SortedList<String> defIdx;
	private SortedList<String> defPath;
	
	private NavigableMap<String,String> classMap;
	private SortedList<String> classIdx;
	private SortedList<String> classPath;
	
	private NavigableMap<String,String> funcMap;
	private SortedList<String> funcIdx;
	private SortedList<String> funcPath;
	
	/**
	 * ����:��������/����/�ж�
	 * map key:gene|signature|path
	 * map value:������+λ��
	 */
	private NavigableMap<String,String> opdecMap;
	private SortedList<String> opdecIdx;
	private SortedList<String> opdecPath;

	private NavigableMap<String,String> actdecMap;
	private SortedList<String> actdecIdx;
	private SortedList<String> actdecPath;
	
	private NavigableMap<String,String> jddecMap;
	private SortedList<String> jddecIdx;
	private SortedList<String> jddecPath;
	
	/**
	 * ʵ��
	 * map key:gene|signature|path
	 * map value:expression
	 * 
	 */
	private NavigableMap<String,Expression> opimpMap;
	private SortedList<String> opimpIdx;
	private SortedList<String> opimpPath;

	private NavigableMap<String,Expression> actimpMap;
	private SortedList<String> actimpIdx;
	private SortedList<String> actimpPath;
	
	private NavigableMap<String,Expression> jdimpMap;
	private SortedList<String> jdimpIdx;
	private SortedList<String> jdimpPath;
	
	public String getGene(Entity target){
		return "0";
	}
	
	public String getSignature(Expression target){
		return "0";
	}
	
	/**********************************Begin operation implements********************/
	public boolean hasOperationImpl(Expression subject, Expression intf){
		//getGene(subject)
		return false;
	}
	
	/**
	 * �����õķ���entity.doOperation(expression)
	 * subject�����Ƕ�������id
	 * @param subject
	 * @param intf
	 * @return
	 */
	public Entity doOperationImpl(Entity subject, Expression intf){
		if(subject instanceof IdentifierExpression){
			IdentifierExpression ie = (IdentifierExpression) subject;
		}
		return null;
	}
}
