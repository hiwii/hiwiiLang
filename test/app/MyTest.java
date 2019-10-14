package app;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.hiwii.obj.file.FileObject;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.store.BTreeFile;
import net.hiwii.system.util.EntityUtil;

import org.junit.Test;

public class MyTest {
	@Test
	public void testTree(){
		File aa = new File("aa.dat");
		if(!aa.exists()){
			try {
				aa.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BTreeFile bf = new BTreeFile(aa);
		byte[] data = new byte[]{1,2,3,4,5,6,7};
		String key = "test";
		String key1 = "test1";
		String key2 = "btest1";
		try {
			for(int i=100;i<325;i++){//233
				String key0 = "test" + i;
				bf.insert(key0, data);
				System.out.println(key0);
			}
			for(int i=100;i<325;i++){//233
				String key0 = "test" + i;
				bf.delete(key0);
				System.out.println(key0 + " deleted!");
			}
//			bf.insert(key, data);
//			bf.insert(key1, data);
//			bf.insert(key2, data);
			bf.flush();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testNumber(){
//		byte[] val = new byte[4];
//		val[0] = (byte) 0xff;
//		val[1] = (byte) 0xff;
//		val[2] = (byte) 0xff;
//		val[3] = (byte) 0xff;
//		System.out.println(val[0]);
//		long ret = val[0] & 0xFF;
//		System.out.println(ret);
//		ret = ret + ((val[1] & 0xFF) << 8);
//		System.out.println(ret);
//		ret = ret + ((val[2]  & 0xFF) << 16);
//		System.out.println(ret);
//		ret = ret + ((val[3]  & 0xFFL) << 24);
//		System.out.println(ret);
//		
//		System.out.println((val[3] & 0xFF)<< 16);
		"abc".substring(1);
		int val = 260;
		byte[] ret = EntityUtil.getUnsignedShort(val);
		System.out.println((ret[0] & 0xFF));
		System.out.println((ret[1] & 0xFF));
	}
	@Test
	public void testString(){
		String a = "\"\"";
		byte[] b = a.getBytes();
		CharSequence c = a;
		String d = "3" + true;
		int ii = 3 + -(1+2);
		if('a' > 's'){
			
		}
//		if("a" == 'a'){
//			System.out.println("abc");
//		}
		String[] as={"ab","ab"};
//		if("ab" > 3){ //invalid
//			
//		}
		System.out.println(a.length());
		System.out.println(b.length);
		System.out.println(c.length());
		System.out.println(d);
		
		float ff = 13434234.398789f;
		double dd = 13434234.398789;
		boolean bb = 4 > 3;//将来为非法形式
		boolean b0 = !true;//将来用负号表示取反，！只用于逻辑
//		boolean b1 = -true;
	}
	
	/**
	 * case1:string="abc" len=5
	 * case2:str="abc""  len=6
	 * case3:str="abc\"" len=7
	 */
	@Test
	public void testInputString(){
		 try {
			 BufferedReader _stdin;
			 _stdin = new BufferedReader(new InputStreamReader(System.in));
			 String line = _stdin.readLine().trim();
			 System.out.println(line);
			 System.out.println(line.length());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testSubString(){
		String a=".";
		String b=null;
		int pos = a.lastIndexOf(".");
		if (pos >= 0){
			b = a.substring(0, pos);
			assertEquals(b,"");
		}
		 System.out.println("b="+b+"3");
	}
	
	@Test
	public void testMatcher(){
		String temp = " meta-data android:name=\"appid\" android:value=\"joy\"  /meta-data "; 
		Pattern pattern = Pattern.compile("android:(name|value)=\"(.+?)\"");    
		Matcher matcher = pattern.matcher(temp);
		while(matcher.find()) {
			//out: appid, joy       
			System.out.println(matcher.group(1)); 
			System.out.println(matcher.group(2));    
		} 
	}
	
	@Test
	public void testReg(){
		assertEquals("123".matches("[1-9]([0-9])*"), true);
		String src = "abcdefg";
		Pattern p = Pattern.compile("bc");
		Matcher m = p.matcher(src);
		System.out.print(src.matches("bc")+"\n");
		System.out.print(m.find()+"\n");
		System.out.print(m.group()+"\n");
		Pattern p2 = Pattern.compile("ef");
		Matcher m2 = p2.matcher(src);
		System.out.print(m2.find()+"\n");
		System.out.print(m2.group()+"\n");
		
		Pattern p3 = Pattern.compile("ef");
		Matcher m3 = p3.matcher(src);
		System.out.print(m3.find()+"\n");
		System.out.print(m3.group()+"\n");
	}
	
	@Test
	public void testReg1(){
		String src = "abababcdefg";
		int n = 3;
		String str = "ab";
		int pos = 0;
		String matches = "";
		while(pos < src.length()){
			String head = src.substring(pos, pos + str.length());
			if(head.equals(str)){
				matches = matches + str;
			}else{
				break;
			}
			pos = pos + str.length();
		}
		System.out.println(pos);
		System.out.println(matches);
	}
	
	@Test
	public void testFile(){
		File f = new File("阶乘.TXT");
		FileObject fo = new FileObject(f);
		byte[] a = new byte[1024];
		int ret = fo.read(a, 0);
		System.out.println(ret);
		String b = new String(a);
		System.out.println(b);
	}
	@Test
	public void testReference(){
		int a = 3;
		int b = a;
		assertEquals(a == b, true);
		System.out.println("step 1,right");
		String c = "abc";
		String d = "abc";
		assertEquals(c == d, true);
		System.out.println("step 2,right");
		String e = new String("abc");
		String f = new String("abc");
		assertEquals(e.equals(f), true);
		System.out.println("step 3,right");
		assertEquals(e == f, true);
		System.out.println("step 4,right");
	}
	
	@Test
	public void testPrint(){
		String a=null;
		System.out.println(a);
	}
}
