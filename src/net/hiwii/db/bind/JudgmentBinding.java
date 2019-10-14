package net.hiwii.db.bind;

import net.hiwii.def.Judgment;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class JudgmentBinding extends TupleBinding<Judgment> {
	@Override
	public Judgment entryToObject(TupleInput arg0) {
		char type = arg0.readChar();

		if(type == 'j'){
			Judgment jdg = new Judgment();
			
			String str = arg0.readString();
			boolean positive = arg0.readBoolean();
			jdg.setName(str);
//			jdg.setPositive(positive);
			return jdg;
		}
		return null;
	}

	@Override
	public void objectToEntry(Judgment arg0, TupleOutput arg1) {
		if(arg0 instanceof Judgment){
			char type = 'j';
			//∞¥’’name,property,value,fromÀ≥–Ú±£¥Ê
			arg1.writeChar(type);
			arg1.writeString(arg0.getName());
//			arg1.writeBoolean(arg0.isPositive());
			
		}
	}
}
