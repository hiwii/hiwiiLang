package net.hiwii.reg;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.message.HiwiiException;
import net.hiwii.reg.join.RegularWithLeft;
import net.hiwii.reg.join.RegularWithRight;
import net.hiwii.reg.join.RegularWithTwo;
import net.hiwii.reg.match.MatchResult;
import net.hiwii.view.Entity;

/**
 * 正则表达式由以下几种基本表达式构成：
 * 1，simple，包含empty
 * 2，alternate
 * 3，repeat(最少一次)
 * 4，not非表达式，存在但不等。包含empty
 * 5，compound组合表达式，是以上表达式的组合。
 * 6，filter，允许以过滤形式建立Regular
 * 其中repeatRegular由exactTimes，Range组成。
 * Range又分为：AtLeast，Range[min, max]。
 * 另外有atMost重复，这是Range特殊形式，min=0
 * 
 * joinRegular是条件regular。
 * 
 * String和RegularExpression成为Matcher。
 * 
 * matches--匹配全字符串
 * locate---略过不匹配字符，寻找匹配字符，有向前和向后
 * guide----从position开始，向前或向后是否匹配
 * cut-----从position开始，向前或向后截取，有贪婪和懒惰选项。当存在嵌套循环，贪婪选项继承。
 * 
 * @author Administrator
 *
 */
public class RegularExpression extends Expression {
	public boolean match(String str){
		int n = count(str, 0, true, true);
		if(n == 0){
			if(str.length() == 0){//测试
				return true;
			}else{
				return false;
			}
		}
		if(n == str.length()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 用于在字符串中略过不匹配字符，返回匹配字符的开始位置
	 * @param str
	 * @param pos
	 * @param forward
	 * @return
	 */
	public int locate(String str, int pos, boolean forward){
		int start = pos;
		while(true){
			if(guide(str, start, forward)){
				return start;
			}
			if(forward){
				start++;
			}else{
				start--;
			}
			if(start < 0 || start >= str.length()){
				break;
			}
		}
		return pos;
	}
	/**
	 * 从当前位置开始，返回匹配字符数。
	 * 
	 * @param greedy TODO
	 */
	public int count(String str, int pos, boolean forward, boolean greedy){
		return 0;
	}
	
	/**
	 * 
	 * 同count()，直接返回字符
	 * @param greedy TODO
	 */
	public String cut(String str, int pos, boolean forward, boolean greedy){
		return "";
	}
	/**
	 * cover表示从position开始向前或向后能找到匹配的子字符串。
	 * 子字符串包括空串
	 * @param str
	 * @param pos
	 * @param forward
	 * @return
	 */
	public boolean guide(String str, int pos, boolean forward){
		return false;
	}
	
	public boolean isLeftJoin(String str, int pos){
		return false;
	}
	
	public boolean isRightJoin(String str, int pos){
		return false;
	}

	/**
	 * start一般情况下等于pos，在repeat循环中，懒惰递进，则start!=pos
	 * @param str
	 * @param mr
	 * @param start
	 * @param pos
	 * @return
	 */
	public MatchResult matchResult(String str, MatchResult mr, int start, int pos){
		return null;
	}
	
	/**
	 * greedy在包含repeatRegular情况下才有效。
	 * @param str
	 * @param mr
	 * @param start
	 * @param pos
	 * @return
	 */
	public MatchResult matchResult(String str, MatchResult mr, int start, int pos, boolean greedy){
		return null;
	}
	@Override
	public Entity doFunctionCalculation(String name, List<Entity> args) {
		if(name.equals("leftJoin")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof RegularExpression)){
				return new HiwiiException();
			}
			RegularExpression re = (RegularExpression) args.get(0);
			RegularWithLeft reg = new RegularWithLeft();
			reg.setBody(this);
			reg.setLeft(re);
			return reg;
		}else if(name.equals("rightJoin")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof RegularExpression)){
				return new HiwiiException();
			}
			RegularExpression re = (RegularExpression) args.get(0);
			RegularWithRight reg = new RegularWithRight();
			reg.setBody(this);
			reg.setRight(re);
			return reg;
		}else if(name.equals("twoJoin")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof RegularExpression)){
				return new HiwiiException();
			}
			if(!(args.get(1) instanceof RegularExpression)){
				return new HiwiiException();
			}
			RegularExpression rl = (RegularExpression) args.get(0);
			RegularExpression rr = (RegularExpression) args.get(1);
			RegularWithTwo reg = new RegularWithTwo();
			reg.setBody(this);
			reg.setLeft(rl);
			reg.setRight(rr);
			return reg;
		}else if(name.equals("name")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof IdentifierExpression)){
				return new HiwiiException();
			}
			IdentifierExpression ie = (IdentifierExpression) args.get(0);
			String nm = ie.getName();
			NamedRegular nr = new NamedRegular();
			nr.setName(nm);
			nr.setRegular(this);
			return nr;
		}
		return null;
	}

}
