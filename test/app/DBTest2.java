package app;

import java.io.File;
import java.io.IOException;

import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.store.BTreeFile;
import net.hiwii.system.store.BTreeNode;
import net.hiwii.system.util.DefinitionTree;

import org.junit.Test;

public class DBTest2 {
	@Test
	public void testBTree(){
		try {
			File aa = new File("aa.dat");
			if(!aa.exists()){
				aa.createNewFile();
			}
			BTreeFile bf = new BTreeFile(aa);
			byte[] val = new byte[]{1,2,3,4,5,6,7};
			for(int i=100;i < 327; i++){
				String key = "test" + i;
				bf.insert(key, val);
				System.out.println("key = " + key);
			}
			bf.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBTree1(){
		try {
			File aa = new File("aa.dat");
			if(!aa.exists()){
				aa.createNewFile();
			}
			BTreeFile bf = new BTreeFile(aa);
			BTreeNode node = bf.searchLeafNode("test50506");
			if(node == null){
				System.out.println("pagenum null ");
			}else{
				System.out.println("pagenum = " + node.getPagenum());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBTreeDelete(){
		try {
			File aa = new File("aa.dat");
			if(!aa.exists()){
				aa.createNewFile();
			}
			BTreeFile bf = new BTreeFile(aa);
			for(int i=10000;i < 10050; i++){
				String key = "test" + i;
				bf.delete(key);
				System.out.println("key = " + key);
			}
			bf.flush();
//			bf.delete("test50506");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBTreeReinsert(){
		try {
			File aa = new File("aa.dat");
			if(!aa.exists()){
				aa.createNewFile();
			}
			BTreeFile bf = new BTreeFile(aa);
			byte[] val = new byte[]{1,2,3,4,5,6,7};
			for(int i=10000;i < 10050; i++){
				String key = "test" + i;
				bf.insert(key, val);
				System.out.println("key = " + key);
			}
			bf.flush();
//			bf.delete("test50506");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBTree2(){
		try {
			File aa = new File("aa.dat");
			if(!aa.exists()){
				aa.createNewFile();
			}
			BTreeFile bf = new BTreeFile(aa);
			byte[] val = new byte[]{1,2,3,4,5,6,7};
//			bf.insert("test327", val);
//			bf.insert("sest999", val);
			for(int i=400;i < 513; i++){
				String key = "test" + i;
				bf.insert(key, val);
				System.out.println("key = " + key);
			}
			bf.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testBTree3(){
		try {
			File aa = new File("aa.dat");
			if(!aa.exists()){
				aa.createNewFile();
			}
			BTreeFile bf = new BTreeFile(aa);
			byte[] val = new byte[]{1,2,3,4,5,6,7};
//			bf.insert("test511", val);
			bf.insert("test512", val);
			
			bf.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBTree4(){
		try {
			File aa = new File("aa.dat");
			if(!aa.exists()){
				aa.createNewFile();
			}
			BTreeFile bf = new BTreeFile(aa);
			byte[] val = new byte[]{1,2,3,4,5,6,7};
			for(int i=514;i < 714; i++){
				String key = "test" + i;
				bf.insert(key, val);
				System.out.println("key = " + key);
			}
			bf.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testBTree5(){
		try {
			File aa = new File("aa.dat");
			if(!aa.exists()){
				aa.createNewFile();
			}
			BTreeFile bf = new BTreeFile(aa);
			byte[] val = new byte[]{1,2,3,4,5,6,7};
			for(int i=10000;i < 31015; i++){// 31015,10195,10484
				String key = "test" + i;
				bf.insert(key, val);
				System.out.println("key = " + key);
			}
			bf.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBTree6(){
		try {
			File aa = new File("aa.dat");
			if(!aa.exists()){
				aa.createNewFile();
			}
			BTreeFile bf = new BTreeFile(aa);
			byte[] val = new byte[]{1,2,3,4,5,6,7};
			for(int i=40000;i < 51015; i++){
				String key = "test" + i;
				bf.insert(key, val);
				System.out.println("key = " + key);
			}
			bf.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
