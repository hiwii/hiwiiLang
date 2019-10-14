package net.hiwii.db.key;

import java.io.UnsupportedEncodingException;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

import net.hiwii.db.bind.ValueBinding;
import net.hiwii.db.ent.StoredValue;

/**
 * entityDatabase key format:
 * 1Î¬£ºinstId£¬2Î¬:childId@instId
 * index
 * 1Î¬£ºnull£¬return false;
 * 2Î¬: instId%signature
 * @author hiwii
 *
 */
public class EntityPartKeyCreater  implements SecondaryKeyCreator {
	private TupleBinding<StoredValue> binding = new ValueBinding();
	@Override
	public boolean createSecondaryKey(SecondaryDatabase arg0, DatabaseEntry key, DatabaseEntry data,
			DatabaseEntry result) {
		try {
			String key0 = new String(key.getData(), "UTF-8");
			int pos = key0.indexOf('@');
			if(pos > 0){
				String instId = key0.substring(pos + 1);
				StoredValue rec = binding.entryToObject(data);
				String key1 = instId + '.' + rec.getSign();
				result.setData(key1.getBytes("UTF-8"));
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
