package net.hiwii.reg.match;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.IdentifierBracket;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.StringExpression;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.system.util.StringUtil;
import net.hiwii.system.util.tuple.ResultSet;
import net.hiwii.view.Entity;

public class MatchResult extends Entity {
	private List<MatchResult> groups;
	private int start;
	private int end;
	private Map<String, Entity> names;

	public MatchResult() {
		names = new HashMap<String, Entity>();
	}

	public List<MatchResult> getGroups() {
		return groups;
	}

	public void setGroups(List<MatchResult> groups) {
		this.groups = groups;
	}
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public Map<String, Entity> getNames() {
		return names;
	}

	public void setNames(Map<String, Entity> names) {
		this.names = names;
	}

//	/**
//	 * 
//	 * @param i
//	 * @return
//	 */
//	public MatchResult group(String param){
//		String head = StringUtil.head(param);
//		String tail = StringUtil.tail(param);
//		if(head != null){
//			if(groups == null){
//				return null;
//			}
//			MatchResult tmp = null;
//			if(StringUtil.isIdentifier(head)){
//				ResultSet rs = (ResultSet) names.get(head);
//				if(rs != null){
//					tmp = (MatchResult) rs.getValue();
//				}else{
//					return null;
//				}
//			}else if(StringUtil.isInteger(head)){
//				int i = 0;
//				try {
//					i = Integer.parseInt(head);
//				} catch (NumberFormatException e) {
//					return null;
//				}
//				if(i > groups.size()){
//					return null;
//				}
//				tmp = groups.get(i - 1);
//			}else{
//				return null;
//			}
//			
//			if(tail == null){
//				return tmp;
//			}else{
//				return tmp.group(tail);
//			}
//		}else{
//			return null;
//		}
//	}
	
	/**
	 * 
	 * @param i
	 * @return
	 */
	public MatchResult group(List<Entity> args){
		if(args.size() == 0){
			return null;
		}
		Expression head = (Expression) args.get(0);
		List<Entity> tail = args.subList(1, args.size());
		if(head != null){
			if(groups == null){
				return null;
			}
			MatchResult tmp = null;
			if(head instanceof StringExpression){
				StringExpression se = (StringExpression) head;
				ResultSet rs = (ResultSet) names.get(se.getValue());
				if(rs != null){
					tmp = (MatchResult) rs.getValue();
				}else{
					return null;
				}
			}else if(head instanceof IntegerNumber){
				IntegerNumber in = (IntegerNumber) head;
				String val = in.getValue();
				int i = Integer.parseInt(val);
				if(i > groups.size()){
					return null;
				}
				tmp = groups.get(i - 1);
			}else if(head instanceof IdentifierBracket){
				IdentifierBracket ib = (IdentifierBracket) head;
				if(ib.getArguments().size() != 1){
					return null;
				}
				if(!(ib.getArguments().get(0) instanceof IntegerNumber)){
					return null;
				}
				String nm = ib.getName();
				int i = EntityUtil.toNumber((IntegerNumber)ib.getArguments().get(0));
				ResultSet rs = (ResultSet) names.get(nm);
				if(rs != null){
					tmp = (MatchResult) rs.getValue(i);
				}else{
					return null;
				}
			}else{
				return null;
			}
			
			if(tail == null || tail.size() == 0){
				return tmp;
			}else{
				return tmp.group(tail);
			}
		}else{
			return null;
		}
	}
	
	public String catchString(String str){
		return str.substring(start, end);
	}
	
	public void put(String name, Entity value){	
		names.put(name, value);
	}
}