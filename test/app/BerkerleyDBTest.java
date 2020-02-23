package app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import net.hiwii.db.HiwiiDB;
import net.hiwii.def.Assignment;
import net.hiwii.def.Definition;
import net.hiwii.def.ExtendedDefinition;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.LocalHost;
import net.hiwii.system.exception.ApplicationException;

import org.junit.Test;

import com.sleepycat.je.DatabaseException;

public class BerkerleyDBTest {
	@Test
	public void testDB(){
		HiwiiDB db = new HiwiiDB();
		db.open();
		
		db.close();
		System.out.println("OK!");
	}
	
	@Test
	public void testInsert(){
		String aKey = "myFirstKey";
		String aData = "myFirstData";
		try {
			byte[] key = aKey.getBytes("UTF-8");
			byte[] data = aData.getBytes("UTF-8");
			
			HiwiiDB db = new HiwiiDB();
			db.open();
			db.insert(key, data);
			db.close();
			System.out.println("OK!");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testRetrieve(){
		String aKey = "myFirstKey";

		try {
			
			HiwiiDB db = new HiwiiDB();
			db.open();
			byte[] ret = db.getRecord(aKey);
			db.close();
			System.out.println(new String(ret, "UTF-8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDefinition(){
		//if allowed null String
		ExtendedDefinition ed = new ExtendedDefinition();
		ed.setName("test");
//		ed.setParent(null);
		ed.setParentKey(null);
//		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
//		try {
//			db.open();
//			db.putDefinition(ed, null);
//			db.close();
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ApplicationException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	@Test
	public void testGetDefinition(){
		//if allowed null String

		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.open();
			Definition def = db.getDefinitionByName("¶©µ¥");
			db.close();
			if(def != null){	
				System.out.println(def.getName());
//				System.out.println("parent" + ed.getParent());
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
