package app;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.StringExpression;
import net.hiwii.protocol.HiwiiMessage;
import net.hiwii.protocol.HiwiiProtocolDecoder;
import net.hiwii.reg.RegularExpression;
import net.hiwii.reg.atom.IdentifierRegular;
import net.hiwii.reg.atom.StringRegular;
import net.hiwii.system.SystemParser;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.system.util.StringUtil;
import net.hiwii.web.weixin.ReceiveXmlEntity;
import net.hiwii.web.weixin.ReceiveXmlProcess;
import net.hiwii.web.weixin.WechatProcess;

import org.junit.Test;

/**
 * predicate谓语
 * assert断言
 * @author Administrator
 *
 */
public class MyTest4 {
	@Test
	public void testParser(){
//		String [] list = {"0", "1", "10", "0.3", "1/3", "1.983e-4", "0b010", "0xef", "0o71"};
//		String [] list = {"2+3*4", "--3", "4ab", "0.3abc", "1/3f(3)", "3* 3"};
//		String [] list = {"{}","{a;b;{a;b};c}"};
//		String [] list = {"f(x)", "[]","[a,b,c,d]"};
		String [] list = {"f(x,z)[y]", "f(x)[y]{aa,bb}","f(x,y)"};
		for(String str:list){
			ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
			SystemParser parser = new SystemParser(stream);
			Expression prg = null;
			try {
				prg = parser.getExpression();
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.print(prg + "\r\n");
		}
	}
	
	@Test
	public void testSymbolic(){
		int 汉字1 = 34;
		System.out.print(汉字1 + "\r\n");
	}
	
	@Test
	public void test汉字(){
		int 汉字1 = 34;
		System.out.print(汉字1 + "\r\n");
	}
	
	@Test
	public void testPatterns(){
		Pattern p = Pattern.compile("([1-9](.([0-9])+)?)[eE]([+-])?([0-9])+");
		Matcher m = p.matcher("6e100");
		boolean b = m.matches();
		System.out.print(b + "\r\n");
		
		p = Pattern.compile("[1-9](.[0-9]{1,2})?");
		m = p.matcher("3.4");
		b = m.matches();
		System.out.print(b + "\r\n");
		
		b = Pattern.matches("a*b", "aaaaab");
		System.out.print(b + "\r\n");
	}
	@Test
	public void testRegulars(){
		RegularExpression re = new IdentifierRegular();
		String[] id1 = {"A1","_1", "汉字1", "1a"};
		for(String str:id1){
			System.out.println(str + ":" + re.match(str));
		}
		String source = "\"\"";
		System.out.println(StringUtil.transferString(source));
		source = "\"abc\"";
		System.out.println(StringUtil.transferString(source));
	}
	
	@Test
	public void testRegulars1(){

		String source = "\"\"";
		System.out.println(StringUtil.transferString(source));
		source = "\"abc\"";
		System.out.println(StringUtil.transferString(source));
		source = "\"abc\\r\\ndefg\"";
		System.out.println(StringUtil.transferString(source));
		source = "abc\r\ndefg";
		System.out.println(source);
		source = StringUtil.transferBack(source);
		System.out.println(source);
	}
	
	@Test
	public void testRegulars2(){
		StringRegular re = new StringRegular();
		String source = "\"\"";
		System.out.println(re.match(source));
		source = "\"abc\\r\\ndefg\"";
		System.out.println(re.match(source));
	}
	@Test
	public void testUnicodeS(){
		String str = new String(Character.toChars(0x22489));
		if(str.length() == 2){
			char chh = str.charAt(0);
			char chl = str.charAt(1);
			if(!Character.isSurrogatePair(chh, chl)){
				return;
			}
			int code = Character.toCodePoint(chh, chl);
			System.out.print("2char:" + Character.isLetter(code) + "\r\n");
		}
		System.out.print(str + "\r\n");
		System.out.print(Character.isLetter('喊') + "\r\n");
		char ch = '=';
		System.out.print(Character.isLetter(ch) + "\r\n");
		System.out.print(Character.isSurrogate('喊'));
	}
	@Test
	public void testDecoderBuffer(){
		String str = "hiwii1.0\r\n" +
				"key1:abc\r\n" +
				"key2:cde\r\n" +
				"\r\ncontent";
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put(str.getBytes());
		buffer.flip();
		int len = str.length();
		HiwiiMessage message = HiwiiProtocolDecoder.decode(buffer, len);
		
		Map<String,String> keys = message.getHeaders();
		System.out.println(message.getTag());
		for(Entry<String,String> entry:keys.entrySet()){
			System.out.println("key=" + entry.getKey() + ";value=" + entry.getValue());
		}
		System.out.println(message.getContent());
	}
	@Test
	public void testDecoder(){
		String str = "hiwii1.0\r\n" +
				"key1:abc\r\n" +
				"key2:cde\r\n" +
				"\r\ncontent";
		HiwiiMessage message = HiwiiProtocolDecoder.decode(str.getBytes());
		Map<String,String> keys = message.getHeaders();
		System.out.println(message.getTag());
		for(Entry<String,String> entry:keys.entrySet()){
			System.out.println("key=" + entry.getKey() + ";value=" + entry.getValue());
		}
		System.out.println(message.getContent());
	}
	
	@Test
	public void testSyntax(){
		String s = "abcd";
		s.substring(2);
		int b = 3;
		int c = ++b;
		System.out.print(c);
//		for(int i=0;i<10;){
//			i++;//内部修改变量值在java中是允许的。而hiwii不允许
//		}
		int a=0;
		for(;a<10;a++){//迭代执行部分运行修改外部迭代变量，java中允许。hiwii不允许
			
		}
	}
	
	@Test
	public void testXmlEntity(){
		String xml = "<xml><ToUserName><![CDATA[gh_1c43e790d428]]></ToUserName><FromUserName>"
				+ "<![CDATA[o7hLojhs3OCmrtec9jJfhmpxk-tk]]></FromUserName><CreateTime>1447980360</CreateTime>"
				+ "<MsgType><![CDATA[text]]></MsgType><Content><![CDATA[奥斯卡\r\nabc]]></Content>"
				+ "<MsgId>6219028291850877736</MsgId></xml>";
		ReceiveXmlEntity xmlEntity = new ReceiveXmlProcess().getMsgEntity(xml);
		System.out.print(xmlEntity.getContent());
	}
	
	@Test
	public void testWeixin(){
		String xml = "<xml><ToUserName><![CDATA[gh_1c43e790d428]]></ToUserName><FromUserName>"
				+ "<![CDATA[o7hLojhs3OCmrtec9jJfhmpxk-tk]]></FromUserName><CreateTime>1447980360</CreateTime>"
				+ "<MsgType><![CDATA[text]]></MsgType><Content><![CDATA[ask[2+3]]]></Content>"
				+ "<MsgId>6219028291850877736</MsgId></xml>";
		String result = new WechatProcess().processWechatMag(xml);
		System.out.print(result);
	}
	
	@Test
	public void testSufix(){
		int i = 4;
		System.out.println(-i--);
		System.out.println(-i--);
	}
	@Test
	public void testSyntax1(){
		if(3>2){
			int i = 0;
		}
		int i = 0;
	}
	
	@Test
	public void testBoolean(){
		boolean a=false;  //must initialize
		a = !a;
		System.out.print(a);
	}
	
	@Test
	public void testRandom(){
		for(int i=0;i<10;i++){
			System.out.println(EntityUtil.randomInteger(BigInteger.ZERO, new BigInteger("10")).toString());
		}
	}
	
	@Test
	public void testStringFill(){
		String ret = StringUtil.fill(" 9099", 10);
		System.out.println("\"" + ret + "\"");
		System.out.println(ret.length());
		
		int i = Integer.parseInt(ret.trim());
		System.out.println(i);
	}
	
	@Test
	public void testVarNull(){
		String a = null;
		String b = null;
		a = "abc";
		b = a;
		a = null;
		System.out.println(a);
		System.out.println(b);
	}
}
