package net.hiwii.reg;

import java.util.ArrayList;
import java.util.List;

import net.hiwii.reg.match.MatchGroup;
import net.hiwii.reg.match.MatchResult;
import net.hiwii.system.util.EntityUtil;

/**
 * Compound��repeatRegular��BinaryRegular��TemplateRegular�����ȡ�
 * ��compound�У�����ƥ�������棬�������1,2��.......
 * ����Ƕ��compound����Ų���1.1,1.2,2.1,2.2С����ʾ.
 * ��������ſ���ʹ��ǰ�����ţ��������δ��������ţ���ͬ������
 * @author Administrator
 *
 */
public class CompoundRegular extends RegularExpression {
	private List<RegularExpression> array;
	public CompoundRegular() {
		array = new ArrayList<RegularExpression>();
	}

	public List<RegularExpression> getArray() {
		return array;
	}
	public void setArray(List<RegularExpression> array) {
		this.array = array;
	}
	
	public void add(RegularExpression item){
		array.add(item);
	}
	/**
	 * size�ض����ڵ���2
	 */
	@Override
	public boolean match(String str) {
		return getBinaryRegular().match(str);
	}

	@Override
	public int count(String str, int pos, boolean forward, boolean greedy) {
		return getBinaryRegular().count(str, pos, forward, greedy);
	}

	@Override
	public boolean guide(String str, int pos, boolean forward) {
		return getBinaryRegular().guide(str, pos, forward);
	}
	public BinaryRegular getBinaryRegular(){
		BinaryRegular br = new BinaryRegular();
		br.setLeft(array.get(0));
		RegularExpression right = null;
		if(array.size() == 2){
			right = array.get(1);
		}else{
			List<RegularExpression> list = array.subList(1, array.size());
			CompoundRegular cr = new CompoundRegular();
			cr.setArray(list);
			right = cr;
		}
		br.setRight(right);
		return br;
	}

	@Override
	public MatchResult matchResult(String str, MatchResult mr, int start, int pos) {
		MatchResult m = new MatchResult();
		List<MatchResult> list = new ArrayList<MatchResult>();
		m.setGroups(list);
		List<MatchResult> results = EntityUtil.matching(str, m, array, 0);
		if(results == null || results.size() == 0){
			return null;
		}
		m.setGroups(results);
		m.setStart(0);
		m.setEnd(str.length());
		return m;
	}
	
}
