package net.hiwii.reg.join;

import java.util.List;

import net.hiwii.message.HiwiiException;
import net.hiwii.reg.BinaryRegular;
import net.hiwii.reg.RegularBody;
import net.hiwii.reg.RegularExpression;
import net.hiwii.view.Entity;

public class RegularWithLeft extends RegularBody {
	private RegularExpression left;

	public RegularExpression getLeft() {
		return left;
	}

	public void setLeft(RegularExpression left) {
		this.left = left;
	}
	
	@Override
	public Entity doFunctionCalculation(String name, List<Entity> args) {
		if(name.equals("leftJoin")){
			return new HiwiiException();
		}else if(name.equals("rightJoin")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			if(!(args.get(0) instanceof RegularExpression)){
				return new HiwiiException();
			}
			RegularExpression re = (RegularExpression) args.get(0);
			RegularWithTwo reg = new RegularWithTwo();
			reg.setBody(this.getBody());
			reg.setLeft(left);
			reg.setRight(re);
			return reg;
		}else if(name.equals("twoJoin")){
			return new HiwiiException();
		}
		return null;
	}

	@Override
	public boolean match(String str) {
		if(!(left instanceof StringHead)){
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
			if(left.guide(str, pos, !forward)){
				return false;
			}
			return getBody().guide(str, pos, forward);
		}else{
			BinaryRegular br = new BinaryRegular();
			br.setLeft(left);
			br.setRight(getBody());
			return br.guide(str, pos, forward);
		}
	}
	
	
}
