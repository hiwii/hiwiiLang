package net.hiwii.runner;

import net.hiwii.cognition.Expression;
import net.hiwii.context.RuntimeLadder;
import net.hiwii.view.Entity;

/**
 * 
 * @author Administrator
 * 20150602
 * Runner��һ��Entity�����Ķ������ڼ�¼ִ��״̬��ִ��ָ����ʽ��
 * Runner�ĺ�����һ��Expression������ѧ�еĵ�ʽ���㣬Ŀ����ʽ���Լ򻯺ʹ��档
 * invoker��Runner�ĵ����ߣ��������runner��ִ�н����
 * �ܶ�����£�invoker��terminal��localHost��һ�������invoker��
 *
 */
public class ExpressionRunner extends SystemRunner {
	private RuntimeLadder ladder;
	private Expression target;
	private Entity invoker;
	private ExpressionRunner parent;
	
	public RuntimeLadder getLadder() {
		return ladder;
	}
	public void setLadder(RuntimeLadder ladder) {
		this.ladder = ladder;
	}
	public Expression getTarget() {
		return target;
	}
	public void setTarget(Expression target) {
		this.target = target;
	}
	public Entity getInvoker() {
		return invoker;
	}
	public void setInvoker(Entity invoker) {
		this.invoker = invoker;
	}
	
	public ExpressionRunner getParent() {
		return parent;
	}
	public void setParent(ExpressionRunner parent) {
		this.parent = parent;
	}
	public void run(){
		return;
	}
	
	public void goon(){
		if(parent != null){
			parent.run();
		}
	}
}
