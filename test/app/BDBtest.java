package app;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Test;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.SecondaryCursor;
import com.sleepycat.je.Transaction;

import net.hiwii.db.HiwiiDB;
import net.hiwii.def.Assignment;
import net.hiwii.system.LocalHost;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.StringUtil;
import net.hiwii.view.HiwiiInstance;

public class BDBtest {
	@Test
	public void testAllIdCalculation(){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Cursor cursor = null;
		cursor = db.getIdCalculation().openCursor(null, null);
		
		DatabaseEntry theKey = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    
	    try {
	    	db.open();
	    	OperationStatus found = cursor.getFirst(theKey, data, LockMode.DEFAULT);
	    	
	    	while(found == OperationStatus.SUCCESS)  {
	    		String key0 = new String(theKey.getData(), "UTF-8");
	    		System.out.println(key0);
	    		found = cursor.getNext(theKey, data, LockMode.DEFAULT);
	    	}
	    	
	    	db.close();
	    }catch(DatabaseException e) {
	    	e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally  {
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		e.printStackTrace();
	    	}
	    }
	}
	
	@Test
	public void testAllVariable(){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Cursor cursor = null;
		cursor = db.getVariables().openCursor(null, null);
		
		DatabaseEntry theKey = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    
	    try {
	    	db.open();
	    	OperationStatus found = cursor.getFirst(theKey, data, LockMode.DEFAULT);
	    	
	    	while(found == OperationStatus.SUCCESS)  {
	    		String key0 = new String(theKey.getData(), "UTF-8");
	    		System.out.println(key0);
	    		found = cursor.getNext(theKey, data, LockMode.DEFAULT);
	    	}
	    	
	    	db.close();
	    }catch(DatabaseException e) {
	    	e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally  {
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		e.printStackTrace();
	    	}
	    }
	}
	
	@Test
	public void testAllFunAction(){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Cursor cursor = null;
//		cursor = db.getfAction().openCursor(null, null);
		cursor = db.getMappingLink().openCursor(null, null);
		
		DatabaseEntry theKey = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    
	    try {
	    	db.open();
	    	OperationStatus found = cursor.getFirst(theKey, data, LockMode.DEFAULT);
	    	
	    	while(found == OperationStatus.SUCCESS)  {
	    		String key0 = new String(theKey.getData(), "UTF-8");
	    		System.out.println(key0);
	    		found = cursor.getNext(theKey, data, LockMode.DEFAULT);
	    	}
	    	
	    	
	    }catch(DatabaseException e) {
	    	e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally  {
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		e.printStackTrace();
	    	}
	    	db.close();
	    }
	}
	
	@Test
	public void testAllIndexJudgeHost(){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		SecondaryCursor cursor = null;
		cursor = db.getIndexJudgeHost().openCursor(null, null);
		
		DatabaseEntry theKey = new DatabaseEntry();
		DatabaseEntry key = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    
	    try {
	    	db.open();
	    	OperationStatus found = cursor.getFirst(theKey, key, data, LockMode.DEFAULT);
	    	
	    	while(found == OperationStatus.SUCCESS)  {
	    		String key0 = new String(theKey.getData(), "UTF-8");
	    		System.out.println("theKey:" + key0);
	    		String key1 = new String(key.getData(), "UTF-8");
	    		System.out.println("key:" + key1);
	    		found = cursor.getNext(theKey, data, LockMode.DEFAULT);
	    	}
	    	
	    	db.close();
	    }catch(DatabaseException e) {
	    	e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally  {
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		e.printStackTrace();
	    	}
	    }
	}
	
	@Test
	public void clearAllFunAction(){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		Cursor cursor = null;
		
		
		DatabaseEntry theKey = new DatabaseEntry();
	    DatabaseEntry data = new DatabaseEntry();
	    Transaction txn = null;
	    try {
	    	db.open();
	    	
	    	txn = db.beginTransaction();
	    	
	    	cursor = db.getfAction().openCursor(txn, null);
	    	OperationStatus found = cursor.getFirst(theKey, data, LockMode.DEFAULT);
	    	
	    	while(found == OperationStatus.SUCCESS)  {
	    		cursor.delete();
	    		String key0 = new String(theKey.getData(), "UTF-8");
	    		System.out.println(key0);
	    		found = cursor.getNext(theKey, data, LockMode.DEFAULT);
	    	}
	    	try {
	    		if (cursor != null) {
	    			cursor.close();
	    		}
	    	} catch(DatabaseException e) {
	    		e.printStackTrace();
	    	}
	    	txn.commit();
	    	db.close();
	    }catch(DatabaseException e) {
	    	e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally  {
			if (txn != null) {
				txn.abort();
				txn = null;
			}
	    	
	    }
	}
	
	@Test
	public void testAllInstance(){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.open();
			List<HiwiiInstance> list = db.allInstance();
			
			db.close();
			
			System.out.println("count:" + list.size());
			for(HiwiiInstance ass:list){
				System.out.println(ass.getUuid());
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAllAssignment(){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.open();
			List<Assignment> list = db.allAssignment();
			
			db.close();
			
			System.out.println("count:" + list.size());
			for(Assignment ass:list){
				System.out.println(ass.getName());
				System.out.println(ass.getValue().toString());
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAllStatus(){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.open();
			List<String> list = db.allStatus();
			
			db.close();
			
			System.out.println("count:" + list.size());
			for(String ass:list){
				System.out.println(ass);
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAllKeys(){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			
//			List<String> list = db.allKeys("propertyDatabase");
			List<String> list = db.allKeys("functionLink");
			
			System.out.println("count:" + list.size());
			for(String ass:list){
				System.out.println(ass);
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetSeondarylKeys(){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			
//			String maindb = "functionLink";
//			String secdb = "indexFunctionLink";
			String maindb = "fcalcuDatabase";
			String secdb = "indexfCalculation";
			List<String> list = db.getSecondaryKeys(maindb, secdb);
			
			System.out.println("count:" + list.size());
			for(String ass:list){
				System.out.println(ass);
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAllSwitches2(){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.open();
			List<String> list = db.allSwitches2();
			
			db.close();
			
			System.out.println("count:" + list.size());
			for(String ass:list){
				System.out.println(ass);
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAllSwitches(){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.open();
			List<String> list = db.allSwitches();
			
			db.close();
			
			System.out.println("count:" + list.size());
			for(String ass:list){
				System.out.println(ass);
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAllAssignmentKey(){
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		try {
			db.open();
			List<Assignment> list = db.allAssignmentKey();
			
			db.close();
			
			System.out.println("count:" + list.size());
			for(Assignment ass:list){
				System.out.println(ass.getName());
				System.out.println(ass.getValue().toString());
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
