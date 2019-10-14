package net.hiwii.reg;

public class BinaryRegular extends RegularExpression {
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
	public boolean match(String str) {
		if(!left.guide(str, 0, true)){
			return false;
		}
//		Str
		int pos = 0;
		String exp0, exp1;
		while(true){
			if(pos >= str.length()){
				break;
			}
			exp0 = str.substring(0, pos);
			exp1 = str.substring(pos);
			if(left.match(exp0) && right.match(exp1)){
				return true;
			}
			pos++;
		}
		return false;
	}
//	@Override
//	public int locate(String str, int pos, boolean forward) {
//		// TODO Auto-generated method stub
//		return super.locate(str, pos, forward);
//	}
	@Override
	public int count(String str, int pos, boolean forward, boolean greedy) {
		int len = str.length();
		String sub = null;
		boolean found = false;
		if(forward){
			int end = pos;
			sub = str.substring(pos, end);
			while(end < len){
				if(match(sub)){
					found = true;
					break;
				}
				end++;
			}
			if(!found){
				return 0;
			}
			if(greedy){
				end++;
				while(end < len){
					if(match(sub)){
						continue;
					}
					end++;
				}
			}
			return end - pos - 1;
		}else{
			int start = pos;
			sub = str.substring(start, pos + 1);
			while(start > 0){
				if(match(sub)){
					found = true;
					break;
				}
				start--;
			}
			if(!found){
				return 0;
			}
			if(greedy){
				start--;
				while(start > 0){
					if(match(sub)){
						continue;
					}
					start--;
				}
			}
			return pos - start;				
		}
	}
	
	@Override
	public boolean guide(String str, int pos, boolean forward) {
		String sub = null;
		int len = str.length();
		if(forward){
			int end = pos;
			sub = str.substring(pos, end);
			while(end < len){
				if(match(sub)){
					return true;
				}
				end++;
			}
			return false;
		}else{
			int start = pos;
			sub = str.substring(start, pos + 1);
			while(start > 0){
				if(match(sub)){
					return true;
				}
				start--;;
			}
			return false;
		}
	}
}
