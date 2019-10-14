package net.hiwii.reg;

/**
 * 只能作为一个regular表达式的修饰使用，不能单独使用。
 * format：notReg(...)
 * 值得注意：空字符串的补集包括所有非空字符串。所有非空字符串的补集必包括空字符串。
 * @author ha-wangzhenhai
 *
 */
public class NotRegular extends RegularExpression {
	private RegularExpression holder;
	
	public NotRegular(RegularExpression regular) {
		holder = regular;
	}
	public RegularExpression getHolder() {
		return holder;
	}
	public void setHolder(RegularExpression holder) {
		this.holder = holder;
	}
	@Override
	public boolean match(String str) {
		if(holder.match(str)){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 否定regular中，贪婪参数有效。另外在循环regular中有效。
	 */
	@Override
	public int count(String str, int pos, boolean forward, boolean greedy) {
		if(greedy){
			if(forward){
				int end = str.length();
				while(pos < end){
					String sub = str.substring(pos, end);
					if(holder.match(sub)){
						return end - pos;
					}
					end--;
				}
			}else{
				int start = 0;
				while(start < pos){
					String sub = str.substring(start, pos + 1);
					if(holder.match(sub)){
						return pos - start + 1;
					}
					start++;
				}
			}
		}else{
			if(forward){
				int end = pos;
				int len = str.length();
				while(end < len){
					String sub = str.substring(pos, end);
					if(holder.match(sub)){
						return end - pos;
					}
					end++;
				}
			}else{
				int start = pos;
				while(start > 0){
					String sub = str.substring(start, pos + 1);
					if(holder.match(sub)){
						return pos - start + 1;
					}
					start--;
				}
			}
		}
		return 0;
	}
	
//	/**
//	 * 比如0-无穷个字母a，在匹配这样的字符时，返回-1
//	 */
//	@Override
//	public int locate(String str, int pos, boolean forward) {
//		int start = pos;
//		while(true){
//			if(!guide(str, start, forward)){
//				return start;
//			}
//			if(forward){
//				start++;
//			}else{
//				start--;
//			}
//			if(start < 0 || start > str.length()){
//				break;
//			}
//		}
//		return -1;
//	}

	@Override
	public boolean guide(String str, int pos, boolean forward) {
		if(holder.guide(str, pos, forward)){
			return false;
		}else{
			return true;
		}
	}
}
