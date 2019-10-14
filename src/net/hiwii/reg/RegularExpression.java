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
 * ������ʽ�����¼��ֻ������ʽ���ɣ�
 * 1��simple������empty
 * 2��alternate
 * 3��repeat(����һ��)
 * 4��not�Ǳ��ʽ�����ڵ����ȡ�����empty
 * 5��compound��ϱ��ʽ�������ϱ��ʽ����ϡ�
 * 6��filter�������Թ�����ʽ����Regular
 * ����repeatRegular��exactTimes��Range��ɡ�
 * Range�ַ�Ϊ��AtLeast��Range[min, max]��
 * ������atMost�ظ�������Range������ʽ��min=0
 * 
 * joinRegular������regular��
 * 
 * String��RegularExpression��ΪMatcher��
 * 
 * matches--ƥ��ȫ�ַ���
 * locate---�Թ���ƥ���ַ���Ѱ��ƥ���ַ�������ǰ�����
 * guide----��position��ʼ����ǰ������Ƿ�ƥ��
 * cut-----��position��ʼ����ǰ������ȡ����̰��������ѡ�������Ƕ��ѭ����̰��ѡ��̳С�
 * 
 * @author Administrator
 *
 */
public class RegularExpression extends Expression {
	public boolean match(String str){
		int n = count(str, 0, true, true);
		if(n == 0){
			if(str.length() == 0){//����
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
	 * �������ַ������Թ���ƥ���ַ�������ƥ���ַ��Ŀ�ʼλ��
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
	 * �ӵ�ǰλ�ÿ�ʼ������ƥ���ַ�����
	 * 
	 * @param greedy TODO
	 */
	public int count(String str, int pos, boolean forward, boolean greedy){
		return 0;
	}
	
	/**
	 * 
	 * ͬcount()��ֱ�ӷ����ַ�
	 * @param greedy TODO
	 */
	public String cut(String str, int pos, boolean forward, boolean greedy){
		return "";
	}
	/**
	 * cover��ʾ��position��ʼ��ǰ��������ҵ�ƥ������ַ�����
	 * ���ַ��������մ�
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
	 * startһ������µ���pos����repeatѭ���У�����ݽ�����start!=pos
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
	 * greedy�ڰ���repeatRegular����²���Ч��
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
