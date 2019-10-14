package net.hiwii.test;

import java.nio.charset.Charset;

import net.hiwii.system.net.format.PackageHeader;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.system.util.PackageUtil;

import org.junit.Test;

public class OtherTest {
	@Test
	public void testPackage(){
		String head = "port:9099\r\n";
		head = head + "type:p2p" + "\r\n"; //others:group,groupId
		head = head + "Content-Type:text"  + "\r\n"; //others:audio,image,video
		System.out.println(head);
		PackageHeader ph = PackageUtil.parseHeaders(head);
		System.out.println(ph.toString());
	}
	@Test
	public void testUUID(){
		System.out.println(EntityUtil.getUUID());
		System.out.println(EntityUtil.getUUID());
		System.out.println(EntityUtil.getUUID());
	}
	
	@Test
	public void testCharset(){
		String str = "我";
		String str1 = "\08";
		System.out.println(Integer.toHexString(str.codePointAt(0)));

		byte[] bs = str.getBytes();

		System.out.println(Charset.defaultCharset());

		for (int i=0; i<bs.length; i++) {

		    System.out.print(bs[i] + " ");

		}

		System.out.println("-------");
		
		//utf-8
		bs = str.getBytes(Charset.forName("utf-8"));

		System.out.println("utf-8");

		for (int i=0; i<bs.length; i++) {

		    System.out.print(bs[i] + " ");

		}

		System.out.println("-------");

		//utf-8
		bs = str.getBytes(Charset.forName("utf-16"));

		System.out.println("utf-16");

		for (int i=0; i<bs.length; i++) {

			System.out.print(bs[i] + " ");

		}

		System.out.println("-------");
//		6211
//
//		GBK
//
//		-50 -46
//
//		当指定编码为UTF-8时：
//
//		6211
//
//		GBK
//
//		-26 -120 -111
//
//		 
//
//		当指定为UTF-16时：
//
//		-2 -1 98 17
	}
	
	@Test
	public void testUnicode(){
		int[] vals = {0x20000,0x2f801};
		String b = new String(vals, 0, 1);//"\x{2f800}";
//		String a = Character.getName(0x2f800);
		System.out.println(b);
		int codep = b.codePointAt(0);
		System.out.println(codep);
		
		String aa="嘗";
		System.out.println(aa);
	}
	@Test
	public void testByteString(){
		byte[] a = new byte[10];
		a[0] = 0x00;
		a[1] = 0x00;
		a[2] = 0x00;
		a[3] = 0x00;
		a[4] = 0x39;
		a[5] = 0x59;
//		a[6] = -0x19;
		a[6] = (byte)0x00E9;
		a[7] = 0x03;
		a[8] = -0x19;
		a[9] = -0x09;
		String b = new String(a);
		System.out.println(b);
		System.out.println(b.length());
	}
	
	@Test
	public void testSyntax(){
		"abc".substring(0);//block最后一句必须有;分隔符
	}
}
