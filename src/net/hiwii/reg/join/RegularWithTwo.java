package net.hiwii.reg.join;

import java.util.List;

import net.hiwii.message.HiwiiException;
import net.hiwii.reg.BinaryRegular;
import net.hiwii.reg.RegularBody;
import net.hiwii.reg.RegularExpression;
import net.hiwii.view.Entity;

public class RegularWithTwo extends RegularBody {
	private RegularExpression left;
	private RegularExpression right;
	public RegularExpression getLeft() {
		return left;
	}
	public void setLeft(RegularExpression left) {
		this.left = left;
	}
	public RegularExpression getRight() {
		return right;
	}
	public void setRight(RegularExpression right) {
		this.right = right;
	}
	
	@Override
	public Entity doFunctionCalculation(String name, List<Entity> args) {
		if(name.equals("leftJoin")){
			return new HiwiiException();
		}else if(name.equals("rightJoin")){
			return new HiwiiException();
		}else if(name.equals("twoJoin")){
			return new HiwiiException();
		}
		return null;
	}
	
	@Override
	public boolean guide(String str, int pos, boolean forward) {
		if(forward){
			if(left.guide(str, pos, !forward)){
				return false;
			}
			BinaryRegular br = new BinaryRegular();
			br.setLeft(getBody());
			br.setRight(right);
			return br.guide(str, pos, forward);
		}else{
			if(right.guide(str, pos, !forward)){
				return false;
			}
			BinaryRegular br = new BinaryRegular();
			br.setLeft(getBody());
			br.setRight(right);
			return br.guide(str, pos, forward);
		}
	}
}
