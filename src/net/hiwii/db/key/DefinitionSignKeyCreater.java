package net.hiwii.db.key;

import java.io.UnsupportedEncodingException;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

import net.hiwii.db.bind.DefinitionBinding;
import net.hiwii.def.Definition;

public class DefinitionSignKeyCreater implements SecondaryKeyCreator {
	private TupleBinding<Definition> binding = new DefinitionBinding();
	@Override
	public boolean createSecondaryKey(SecondaryDatabase arg0, DatabaseEntry key, DatabaseEntry data,
			DatabaseEntry result) {
		try {
			Definition def = binding.entryToObject(data);
			if(def == null){
				return false;
			}
			result.setData(def.getSignature().getBytes("UTF-8"));
			return true;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
