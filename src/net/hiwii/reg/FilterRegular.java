package net.hiwii.reg;

import java.util.List;

import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.StringUtil;

/**
 * 级联过滤正则表达式。
 * @author Administrator
 *
 */
public class FilterRegular extends RegularExpression {
	private List<RegularExpression> array;

	public List<RegularExpression> getArray() {
		return array;
	}
	public void setArray(List<RegularExpression> array) {
		this.array = array;
	}
	
	@Override
	public boolean match(String str) {
		for(RegularExpression re:array){
			boolean ret = re.match(str);
			if(!ret){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int count(String str, int pos, boolean forward, boolean greedy) {
		if(greedy){
			int tmp = count(str, pos, forward, true);
			boolean matched = false;
			while(tmp > 0){
				try {
					String sub = StringUtil.cut(str, pos, tmp, forward);
					if(!match(sub)){
						tmp--;
					}else{
						matched = true;
						break;
					}
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(matched){
				return tmp;
			}else{
				return 0;
			}
		}else{
			int tmp = count(str, pos, forward, false);
			boolean matched = false;
			while(StringUtil.inString(str, pos, tmp, forward)){
				try {
					String sub = StringUtil.cut(str, pos, tmp, forward);
					if(!match(sub)){
						tmp++;
					}else{
						matched = true;
						break;
					}
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(matched){
				return tmp;
			}else{
				return 0;
			}
		}
	}

	@Override
	public boolean guide(String str, int pos, boolean forward) {
		for(RegularExpression re:array){
			boolean ret = re.guide(str, pos, forward);
			if(!ret){
				return false;
			}
		}
		return true;
	}
}
