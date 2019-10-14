package net.hiwii.db.bind;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import net.hiwii.db.ent.StoredValue;

/**
 * type == 's'表示Expression，用string表示。
 * type == 'i'表示instance
 * type == 'm'表示multiple values.
 * @author hiwii
 *
 */
public class ValueBinding extends TupleBinding<StoredValue> {

	@Override
	public StoredValue entryToObject(TupleInput arg0) {
		StoredValue ass = new StoredValue();
		char type = arg0.readChar();
		String sign = arg0.readString();
		String str = arg0.readString();
		ass.setValue(str);
		ass.setSign(sign);
		ass.setType(type);
		return ass;
	}

	@Override
	public void objectToEntry(StoredValue arg0, TupleOutput arg1) {
		arg1.writeChar(arg0.getType());
		arg1.writeString(arg0.getSign());
		arg1.writeString(arg0.getValue());
	}
}

