package net.hiwii.reg;

import java.util.ArrayList;
import java.util.List;

import net.hiwii.reg.match.MatchGroup;
import net.hiwii.reg.match.MatchResult;
import net.hiwii.system.util.EntityUtil;

/**
 * Compound是repeatRegular，BinaryRegular，TemplateRegular的祖先。
 * 在compound中，所有匹配结果保存，按照序号1,2，.......
 * 对于嵌套compound，序号采用1.1,1.2,2.1,2.2小数表示.
 * 后续的序号可以使用前面的序号，如果调用未声明的序号，相同将报错。
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
	 * size必定大于等于2
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
