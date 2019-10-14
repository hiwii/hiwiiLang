package net.hiwii.reg.join;

import java.util.List;

import net.hiwii.message.HiwiiException;
import net.hiwii.reg.BinaryRegular;
import net.hiwii.reg.RegularBody;
import net.hiwii.reg.RegularExpression;
import net.hiwii.view.Entity;

public class RegularWithRight extends RegularBody {
	private RegularExpression right;

	public RegularExpression getRight() {
		return right;
	}

	public void setRight(RegularExpression right) {
		this.right = right;
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
			RegularWithTwo reg = new RegularWithTwo();
			reg.setBody(this.getBody());
			reg.setLeft(re);
			reg.setRight(right);
			return reg;
		}else if(name.equals("rightJoin")){
			return new HiwiiException();
		}else if(name.equals("twoJoin")){
			return new HiwiiException();
		}
		return null;
	}

	@Override
	public boolean match(String str) {
		if(!(right instanceof StringTail)){
			return false;
		}
		return getBody().match(str);
	}

	@Override
	public int count(String str, int pos, boolean forward, boolean greedy) {
		// TODO Auto-generated method stub
		return super.count(str, pos, forward, greedy);
	}

	@Override
	public boolean guide(String str, int pos, boolean forward) {
		if(forward){
			BinaryRegular br = new BinaryRegular();
			br.setLeft(getBody());
			br.setRight(right);
			return br.guide(str, pos, forward);
		}else{
			if(right.guide(str, pos, true)){
				return false;
			}
			return getBody().guide(str, pos, forward);
		}
	}
	
}
