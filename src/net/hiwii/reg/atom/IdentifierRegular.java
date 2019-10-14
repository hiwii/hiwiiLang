package net.hiwii.reg.atom;

import net.hiwii.reg.RegularExpression;
import net.hiwii.system.SystemCharacter;

/**
 * ����ʼλ�ò�ͬ����������ס���β�����ɼ��ַ�
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
	 * Ҫ��position����λ��word��ʼ���β
	 * word���Խ���һ�����֣��磺9a�Ϸ�����ʾ9��һ��word
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
			//��������letter��digit��others��isIdentifierPart,Ѱ�ҵ�һ��character
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
