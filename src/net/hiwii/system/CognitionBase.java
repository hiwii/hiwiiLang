package net.hiwii.system;

import java.util.NavigableMap;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.system.util.SortedList;
import net.hiwii.view.Entity;

/**
 * 
 * @author ha-wangzhenhai
 * 所有的cognition以定义为主线保存，保存状态有runtime和persist两种状态。
 * 保存的要求cognition能作为字符串保存，并从字符串能够翻译回cognition。
 * cognition访问从此主体发起。
 * 每个定义含有祖先定义的基因
 * 
 * 对象认识是特殊定义，作为定义树的最终节点，树叶。
 * 定义-----类s
 * |
 * 定义-----类s
 * |
 * |
 * |
 * 定义-----类s
 * |
 * 对象-----类s
 * 每个定义只有一个父定义，每个对象只有一个定义。
 * 每个定义有多个类，作为mixin。
 * 类和定义以及对象包括的内容如下：
 * 内部定义(定义/类/函数)
 * 
 * 声明
 * 结构定义
 * 属性定义
 * 
 * 声明实现
 * 结构描述
 * 属性描述(包含在判断中)
 * 
 * 判断
 * 推理
 * 
 * 声明AOP
 * 声明interface语法
 * 
 * 认识元素aware
 * 
 * 作为叶子对象，还有扩展对象、扩展属性和扩展声明。分别记录特别对象信息、属性信息、执行代码。
 * 
 * 认识为动作服务，即为doOperation/doAction/doJudgment服务
 * 执行依赖于声明实现，声明实现依赖于声明。一个声明可以有多个声明实现。
 * 声明实现一次只有一个生效(override)，声明支持重载(overload)。
 * 寻找声明实现从对象开始，然后是对象的类，然后是对象的定义，然后是同级定义的类，依次寻找。
 * 可以通过索引加快寻找速度。
 * 
 */
public class CognitionBase {
	/**
	 * 每个内容由主语gene|signature|path+内容构成
	 * gene定义了查找顺序
	 * 有两个索引：
	 * 1个:signature|gene|path--用于快速查找
	 * 1个:path|signature|gene--用于垃圾回收
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
	 * 声明:包括运算/动作/判断
	 * map key:gene|signature|path
	 * map value:参数名+位置
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
	 * 实现
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
	 * 本调用的发起：entity.doOperation(expression)
	 * subject可以是定义或对象id
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
