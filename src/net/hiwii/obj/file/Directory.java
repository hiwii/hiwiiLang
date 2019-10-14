package net.hiwii.obj.file;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.JudgmentResult;

public class Directory extends FileObject {

	public Directory(String path) {
		super(path);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Expression doIdentifierDecision(String name) {
		if(name.equals("exists")){
			JudgmentResult jr = new JudgmentResult();
			if(exists()){
				jr.setResult(true);
			}else{
				jr.setResult(false);
			}
			return jr;
		}
		return super.doIdentifierDecision(name);
	}
}
