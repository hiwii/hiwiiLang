package net.hiwii.reg;

import java.util.List;

public class AlternateRegular extends RegularExpression {
	private List<RegularExpression> alters;

	public List<RegularExpression> getAlters() {
		return alters;
	}

	public void setAlters(List<RegularExpression> alters) {
		this.alters = alters;
	}

	@Override
	public boolean match(String str) {
		for(RegularExpression re:alters){
			if(re.match(str))
				return true;				
		}
		return false;
	}

	@Override
	public int count(String str, int pos, boolean forward, boolean greedy) {
		boolean found = false;
		int max = 0;
		for(RegularExpression re:alters){
			if(re.guide(str, pos, forward)){
				found = true;
				int n = re.count(str, pos, forward, greedy);
				if(greedy){
					if(n > max){
						max = n;
					}
					continue;
				}
				//reluctant则返回第一个匹配
				return re.count(str, pos, forward, greedy);	
			}
		}
		if(!found){
			return 0;
		}
		return max;	
	}

	@Override
	public boolean guide(String str, int pos, boolean forward) {
		for(RegularExpression re:alters){
			if(re.guide(str, pos, forward))
				return true;	
		}
		return false;
	}
}
