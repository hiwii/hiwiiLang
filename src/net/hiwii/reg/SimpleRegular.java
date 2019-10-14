package net.hiwii.reg;

import net.hiwii.reg.match.MatchResult;

/**
 * patternÔÊÐí¿Õ×Ö·û´®""
 * @author ha-wangzhenhai
 *
 */
public class SimpleRegular extends RegularExpression {
	private String pattern;

	public SimpleRegular(){
		this.pattern = "";
	}
	public SimpleRegular(String pattern){
		this.pattern = pattern;
	}
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	@Override
	public boolean match(String str) {
		if(str.equals(pattern)){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int count(String str, int pos, boolean forward, boolean greedy) {
		if(guide(str, pos, forward)){
			return pattern.length();
		}else{
			return 0;
		}
	}
	
	@Override
	public boolean guide(String str, int pos, boolean forward) {
		if(pos >= str.length() || pos < 0){
			return false;
		}
		if(str.length() == 0){
			return true;
		}
		if(forward){
			if(pos + pattern.length() > str.length()){
				return false;
			}
			String subs = str.substring(pos, pos + pattern.length());
			if(subs.equals(pattern)){
				return true;
			}else{
				return false;
			}
		}else{
			if(pos - pattern.length() + 1 < 0){
				return false;
			}
			String subs = str.substring(pos - pattern.length() + 1, pos + 1);
			if(subs.equals(pattern)){
				return true;
			}else{
				return false;
			}
		}
	}
	
	@Override
	public MatchResult matchResult(String str, MatchResult mr, int start, int pos) {
		if(guide(str, pos, true)){
			if(start == pos && str.substring(pos, pos + pattern.length()).equals(pattern)){
				MatchResult m = new MatchResult();
				m.setStart(start);
				m.setEnd(pos + pattern.length());
				return m;
			}else{
				return null;
			}
		}
		return null;
	}

}
