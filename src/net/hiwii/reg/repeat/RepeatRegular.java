package net.hiwii.reg.repeat;

import net.hiwii.reg.RegularExpression;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.StringUtil;


/**
 * repeatNTimes恰好N次
 * repeatLeast至少N次
 * repeatRange出现区间[min,max]
 * 必须考虑参与的循环正则式可能匹配空字符串的情况，这时可能会造成无限循环。
 * 
 * @author ha-wangzhenhai
 *
 */
public class RepeatRegular extends RegularExpression {
	private RegularExpression regular;
	
	public RepeatRegular(RegularExpression regular) {
		this.regular = regular;
	}
	public RegularExpression getRegular() {
		return regular;
	}
	public void setRegular(RegularExpression regular) {
		this.regular = regular;
	}
	
	public String getMaxLen(String str, int pos, boolean forward) throws ApplicationException{
		int n = regular.count(str, pos, forward, true);
		if(n == 0){
			return "";
		}
		return StringUtil.cut(str, pos, n, forward);
	}
	
	
	@Override
	public boolean match(String str) {
		int n = count(str, 0, true, true);
		if(str.length() == n){
			return true;
		}else{
			return false;
		}
	}
}
