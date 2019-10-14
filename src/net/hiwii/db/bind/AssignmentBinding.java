package net.hiwii.db.bind;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import net.hiwii.cognition.Expression;
import net.hiwii.context.SessionContext;
import net.hiwii.def.Assignment;
import net.hiwii.system.LocalHost;
import net.hiwii.system.util.StringUtil;
import net.hiwii.view.Entity;
import net.hiwii.view.HiwiiInstance;

/**
 * type == 's'表示Expression，用string表示。
 * type == 'i'表示instance
 * @author hiwii
 *
 */
public class AssignmentBinding extends TupleBinding<Assignment> {

	@Override
	public Assignment entryToObject(TupleInput arg0) {
		Assignment ass = new Assignment();
		char type = arg0.readChar();
		String str = arg0.readString();
		if(type == 's'){
			Expression expr = StringUtil.parseString(str);
			SessionContext sc = LocalHost.getInstance().newSessionContext();
			Entity ent = sc.doCalculation(expr);
			ass.setValue(ent);
		}else{
			try {
				HiwiiInstance hi = LocalHost.getInstance().getHiwiiDB().getInstanceById(str);
				ass.setValue(hi);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ass;
	}

	@Override
	public void objectToEntry(Assignment arg0, TupleOutput arg1) {
		char type = 'i';
		Entity val = arg0.getValue();
		if(!(val instanceof HiwiiInstance)){
			type = 's';
		}
		String str = null;
		if(type == 'i'){
			str = ((HiwiiInstance)val).getUuid();
		}else{
			str = val.toString();
		}
		arg1.writeChar(type);
		arg1.writeString(str);
	}
}

