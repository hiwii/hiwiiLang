package net.hiwii.reg.atom;

import net.hiwii.reg.RegularExpression;
import net.hiwii.system.SystemCharacter;

/**
 * 对起始位置不同结果，当串首、串尾、不可见字符
 * @author ha-wangzhenhai
 *
 */
public class IdentifierRegular extends RegularExpression {

	@Override
	public boolean match(String str) {
		if(str.length() == 0){
			return false;
		}
		int code = str.codePointAt(0);
		String ch = new String(Character.toChars(code));
		if(!SystemCharacter.isIdentifierFirst(ch)){
			return false;
		}
		int pos = Character.charCount(code);
		while(pos<str.length()){
			code = str.codePointAt(pos);
			if(!SystemCharacter.isIdentifierPart(ch)){
				return false;
			}
			pos = pos + Character.charCount(code);
		}
		return true;
	}

	@Override
	public int count(String str, int pos, boolean forward, boolean greedy) {
		if(forward){
			int code = str.codePointAt(pos);
			String ch = new String(Character.toChars(code));
			if(SystemCharacter.isIdentifierFirst(ch)){
				int start = pos++;
				while(start < str.length()){
					code = str.codePointAt(start);
					if(SystemCharacter.isIdentifierFirst(code)){
						start++;
					}else{
						break;
					}
				}
				return start - pos;
			}else{
				return 0;
			}
		}
		return 0;
	}

	/**
	 * 要求position必须位于word开始或结尾
	 * word可以紧跟一个数字，如：9a合法，表示9和一个word
	 */
	@Override
	public boolean guide(String str, int pos, boolean forward) {
		if(pos < 0 || pos >= str.length()){
			return false;
		}
		if(forward){
			int code = str.codePointAt(pos);
			String ch = new String(Character.toChars(code));
			return SystemCharacter.isIdentifierFirst(ch);
		}else{
			//逆向连续letter、digit、others，isIdentifierPart,寻找第一个character
			while(pos > 0){
				int code = str.codePointAt(pos);
				if(SystemCharacter.isIdentifierFirst(code)){
					return true;
				}else if(SystemCharacter.isIdentifierPart(code)){
					pos = pos - Character.charCount(code);
					continue;
				}else{
					return false;
				}
			}
			return false;
		}
	}
	
	
}
