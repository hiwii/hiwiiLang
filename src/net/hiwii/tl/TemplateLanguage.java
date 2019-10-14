package net.hiwii.tl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.hiwii.cognition.Expression;
import net.hiwii.context.SessionContext;
import net.hiwii.system.util.StringUtil;
import net.hiwii.view.Entity;

/**
 * TL处理String。
 * <#...#>表示action
 * <$...$>表示operation
 * <\#或\#>用于表示原<#或#>
 * <\$或\$>用于表示原<$或$>
 * @author ha-wangzhenhai
 *
 */
public class TemplateLanguage extends Entity {
	private String reg0 = "(<#(.*?)#>)|(<\\$(.*?)\\$>)";
	private SessionContext context;
	
	public String getReg0() {
		return reg0;
	}

	public void setReg0(String reg0) {
		this.reg0 = reg0;
	}

	public SessionContext getContext() {
		return context;
	}

	public void setContext(SessionContext context) {
		this.context = context;
	}

	public String handle(String input){
		Pattern p = Pattern.compile(reg0);
		Matcher m = p.matcher(input);
		String result = "";
		int pos = 0;
		while(m.find()){
			String str0 = m.group(0);
			String rep = resultString() + handleWith(str0);
			result = result + input.substring(pos, m.start());
			result = result + rep;
			pos = m.end();
		}
		return result;
	}
	
	public String handleWith(String str){
		String com = str.substring(2, str.length() - 2);
		Expression expr = StringUtil.parseString(com);
		if(str.startsWith("<#")){			
			Expression ret = context.doAction(expr);
			return ret.toString();
		}else{
			Entity ret = context.doCalculation(expr);
			return ret.toString();
		}
	}
	
	public String resultString(){
		String ret = "";
//		for(Entity ent:context.getResults()){
//			ret = ret + ent.toString();
//		}
		return ret;
	}
}
