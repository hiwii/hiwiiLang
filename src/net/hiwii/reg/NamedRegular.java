package net.hiwii.reg;

import net.hiwii.reg.match.MatchResult;
import net.hiwii.system.util.tuple.ResultSet;

public class NamedRegular extends RegularExpression {
	private String name;
	private RegularExpression regular;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public RegularExpression getRegular() {
		return regular;
	}
	public void setRegular(RegularExpression regular) {
		this.regular = regular;
	}
	@Override
	public boolean match(String str) {
		return regular.match(str);
	}
	@Override
	public int locate(String str, int pos, boolean forward) {
		return regular.locate(str, pos, forward);
	}
	@Override
	public int count(String str, int pos, boolean forward, boolean greedy) {
		return regular.count(str, pos, forward, greedy);
	}
	@Override
	public boolean guide(String str, int pos, boolean forward) {
		return regular.guide(str, pos, forward);
	}
	@Override
	public MatchResult matchResult(String str, MatchResult mr, int start,	int pos) {
		MatchResult res = regular.matchResult(str, mr, start, pos);
		if(res != null){
			ResultSet rs = (ResultSet) mr.getNames().get(name);
			if(rs == null){
				rs = new ResultSet();
				rs.putValue(res);
				mr.put(name, rs);
			}else{
				rs.putValue(res);
				mr.put(name, rs);
			}
			return res;
		}
		return null;
	}
	
}
