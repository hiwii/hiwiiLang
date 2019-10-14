package net.hiwii.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.StringExpression;
import net.hiwii.message.HiwiiException;
import net.hiwii.reg.AlternateRegular;
import net.hiwii.reg.CompoundRegular;
import net.hiwii.reg.NamedRegular;
import net.hiwii.reg.RegularExpression;
import net.hiwii.reg.SimpleRegular;
import net.hiwii.reg.match.MatchGroup;
import net.hiwii.reg.match.MatchResult;
import net.hiwii.reg.repeat.RepeatTimes;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.system.util.StringUtil;
import net.hiwii.term.CommandTerminal;
import net.hiwii.term.HiwiiTerminal;
import net.hiwii.view.Entity;

public class HiwiiTest {
	private static CommandTerminal term;
//	@BeforeClass
//	public static void setUP(){
//		System.out.println("set up done!");
//		try {
//			term = LocalHost.getInstance().openTerminal();
//			LocalHost.getInstance().setConsole(term);
//		} catch (ApplicationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	@Test
	public void testRandom(){
		BigInteger start = new BigInteger("10");
		BigInteger end = new BigInteger("90");
		for(int i=0;i<10;i++){
			IntegerNumber in = (IntegerNumber) EntityUtil.randomInteger(start, end);
			System.out.println(in.getValue());
		}
	}
	@Test
	public void testReflect1(){
		try {
			Class uuid = Class.forName("net.hiwii.term.JavaTerminal");
			Method m = uuid.getMethod("run", new Class[]{List.class});
			Object ent = uuid.newInstance();
			List<String> list = Arrays.asList("abc","ccd","eee");
			System.out.println(m.invoke(ent, list));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testReflect(){
		try {
			Class uuid = Class.forName("net.hiwii.cognition.result.JudgmentResult");
			Method m = uuid.getMethod("setResult", new Class[]{boolean.class});
			Object ent = uuid.newInstance();
			m.invoke(ent, false);
			Method me = uuid.getMethod("isResult", new Class[0]);
//			ent.setResult(true);
			System.out.println(me.invoke(ent, null));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testByte(){
//		byte a = Byte.valueOf("FF", 4);
//		System.out.println(a);
//		a = Byte.valueOf("10", 4);
//		System.out.println(a);
		byte a = Byte.valueOf("1E", 16);
		System.out.println(a);
	}
	@Test
	public void testNumber(){
//		int bi = 0x2372174091849038190483274585740; out of range
		byte a = 0x12;
//		byte b = 0b11;
		byte c = 011;
		System.out.println(a);
		System.out.println(c);
	}
	
	@Test
	public void testOperation() throws Exception{
		HiwiiTerminal ht = new HiwiiTerminal();
			
		String ret = null;
		ret = ht.doCalculation("1+1");
		assertEquals("2", ret);
		ret = ht.doCalculation("2+ -1");
		assertEquals("1", ret);
		ret = ht.doCalculation("1 + -1");
		assertEquals("0", ret);
		
		ret = ht.doCalculation("1+1.1");
		assertEquals("2.1", ret);
		ret = ht.doCalculation("2+ -1.1");
		assertEquals("0.9", ret);
		
		ret = ht.doCalculation("1+ 1/2");
		assertEquals("3/2", ret);
		ret = ht.doCalculation("1+ -1/2");
		assertEquals("1/2", ret);
		
		ret = ht.doCalculation("1 + 1e10");
		assertEquals("10000000001", ret);
		ret = ht.doCalculation("1 + 1e-2");
		assertEquals("1.01", ret);
		ret = ht.doCalculation("-10 + 1e1");
		assertEquals("0", ret);
		ret = ht.doCalculation("1 + -1e10");
		assertEquals("-9999999999", ret);
		
		ret = ht.doCalculation("1 + 1e-2");
		assertEquals("1.01", ret);
		ret = ht.doCalculation("1 + -1e-2");
		assertEquals("0.99", ret);
		
		ret = ht.doCalculation("2 - 1");
		assertEquals("1", ret);
		ret = ht.doCalculation("2 - -1");
		assertEquals("3", ret);
		
		ret = ht.doCalculation("2 - 1.1");
		assertEquals("0.9", ret);
		ret = ht.doCalculation("2 - -1.1");
		assertEquals("3.1", ret);
		
		ret = ht.doCalculation("2 - 1/2");
		assertEquals("3/2", ret);
		ret = ht.doCalculation("1 - -1/2");
		assertEquals("3/2", ret);
		
		ret = ht.doCalculation("1 - 1e10");
		assertEquals("-9999999999", ret);
		ret = ht.doCalculation("1 - -1e10");
		assertEquals("10000000001", ret);
		ret = ht.doCalculation("1 - 1e-2");
		assertEquals("0.99", ret);
		ret = ht.doCalculation("1 - -1e-2");
		assertEquals("1.01", ret);
		
		ret = ht.doCalculation("2 * 3");
		assertEquals("6", ret);
		ret = ht.doCalculation("2 * -1");
		assertEquals("-2", ret);
		
		ret = ht.doCalculation("2 * 1.1");
		assertEquals("2.2", ret);
		ret = ht.doCalculation("2 * -1.1");
		assertEquals("-2.2", ret);
		
		ret = ht.doCalculation("2 * 1/2");
		assertEquals("1", ret);
		ret = ht.doCalculation("3 * 1/2");
		assertEquals("3/2", ret);
		ret = ht.doCalculation("2 * -1/2");
		assertEquals("-1", ret);
		
		ret = ht.doCalculation("2 * 1e10");
		assertEquals("20000000000", ret);
		ret = ht.doCalculation("2 * -1e10");
		assertEquals("-20000000000", ret);
		
		ret = ht.doCalculation("2 * 1e-2");
		assertEquals("0.02", ret); //1/50
		ret = ht.doCalculation("2 * -1e-2");
		assertEquals("-0.02", ret);
		
		//integer / operand
		ret = ht.doCalculation("2 / 3");
		assertEquals("2/3", ret);
		ret = ht.doCalculation("2 / -1");
		assertEquals("-2", ret);
		
		ret = ht.doCalculation("2 / 0.5");
		assertEquals("4", ret);
		
		ret = ht.doCalculation("0.5 / 2");
		assertEquals("1/4", ret);
		
		ret = ht.doCalculation("2 / 1.1");
		assertEquals("20/11", ret);
		ret = ht.doCalculation("2 / -1.1");
		assertEquals("-20/11", ret);
		
		ret = ht.doCalculation("2 / 1/2");
		assertEquals("4", ret);
		ret = ht.doCalculation("3 / 1/2");
		assertEquals("6", ret);
		ret = ht.doCalculation("2 / -1/2");
		assertEquals("-4", ret);

		ret = ht.doCalculation("2 / 1e2");
		assertEquals("1/50", ret);
		ret = ht.doCalculation("2 / -1e2");
		assertEquals("-1/50", ret);
		
		//float number plus
		ret = ht.doCalculation("1.1 + 1");
		assertEquals("2.1", ret);
		ret = ht.doCalculation("1.1 + -1");
		assertEquals("0.1", ret);
		
		ret = ht.doCalculation("1.1 + 1.1");
		assertEquals("2.2", ret);
		ret = ht.doCalculation("1.1 + -2.2");
		assertEquals("-1.1", ret);
		ret = ht.doCalculation("1.1 + -1.1");
		assertEquals("0", ret);
		
		ret = ht.doCalculation("1.1 + 1/3");
		assertEquals("43/30", ret);
		ret = ht.doCalculation("1.1 + -1/3");
		assertEquals("23/30", ret);
		
		ret = ht.doCalculation("1.1 + 1e2");
		assertEquals("101.1", ret);
		ret = ht.doCalculation("1.1 + 1e-2");
		assertEquals("1.11", ret);
		ret = ht.doCalculation("1.1 + 1e1");
		assertEquals("11.1", ret);
		ret = ht.doCalculation("1.1 + -1e-2");
		assertEquals("1.09", ret);
		
		ret = ht.doCalculation("1.1 + -1e-2");
		assertEquals("1.09", ret);
		
		//float number mius
		ret = ht.doCalculation("1.1 - 1");
		assertEquals("0.1", ret);
		ret = ht.doCalculation("1.1 - -1");
		assertEquals("2.1", ret);
		
		ret = ht.doCalculation("1.1 - 1.1");
		assertEquals("0", ret);
		ret = ht.doCalculation("1.1 - -2.2");
		assertEquals("3.3", ret);
		ret = ht.doCalculation("1.1 - -1.1");
		assertEquals("2.2", ret);
		
		ret = ht.doCalculation("1.1 - 1/3");
		assertEquals("23/30", ret);
		ret = ht.doCalculation("1.1 - -1/3");
		assertEquals("43/30", ret);
		
		ret = ht.doCalculation("1.1 - 1e2");
		assertEquals("-98.9", ret);
		ret = ht.doCalculation("1.1 - 1e-2");
		assertEquals("1.09", ret);
		ret = ht.doCalculation("1.1 - -1e2");
		assertEquals("101.1", ret);
		ret = ht.doCalculation("1.1 - -1e-2");
		assertEquals("1.11", ret);
		
		//float number multiply
		ret = ht.doCalculation("1.1 * 1");
		assertEquals("1.1", ret);
		ret = ht.doCalculation("1.1 * -1");
		assertEquals("-1.1", ret);
		
		ret = ht.doCalculation("1.1 * 1.1");
		assertEquals("1.21", ret);
		ret = ht.doCalculation("1.1 * -2.2");
		assertEquals("-2.42", ret);
		ret = ht.doCalculation("1.1 * -1.1");
		assertEquals("-1.21", ret);
		
		ret = ht.doCalculation("1.1 * 1/3");
		assertEquals("11/30", ret);
		ret = ht.doCalculation("1.1 * -1/3");
		assertEquals("-11/30", ret);
		
		ret = ht.doCalculation("1.1 * 1e2");
		assertEquals("110", ret);
		ret = ht.doCalculation("1.1 * 1e-2");
		assertEquals("0.011", ret);
		ret = ht.doCalculation("1.1 * -1e2");
		assertEquals("-110", ret);
		ret = ht.doCalculation("1.1 * -1e-2");
		assertEquals("-0.011", ret);
		
		//float number divide
		ret = ht.doCalculation("1.1 / 2");
		assertEquals("11/20", ret);
		ret = ht.doCalculation("1.1 / -2");
		assertEquals("-11/20", ret);
		
		ret = ht.doCalculation("1.1 / 1.1");
		assertEquals("1", ret);
		ret = ht.doCalculation("1.1 / -2.2");
		assertEquals("-1/2", ret);
		ret = ht.doCalculation("1.1 / -1.1");
		assertEquals("-1", ret);
		
		ret = ht.doCalculation("1.1 / 1/3");
		assertEquals("33/10", ret);
		ret = ht.doCalculation("1.1 / -1/3");
		assertEquals("-33/10", ret);
		
		ret = ht.doCalculation("1.1 / 1e2");
		assertEquals("11/1000", ret);
		ret = ht.doCalculation("1.1 / 1e-2");
		assertEquals("110", ret);
		ret = ht.doCalculation("1.1 / -1e2");
		assertEquals("-11/1000", ret);
		ret = ht.doCalculation("1.1 / -1e-2");
		assertEquals("-110", ret);
		
		//fraction operation
		//fraction number plus
		ret = ht.doCalculation("1/3 + 1");
		assertEquals("4/3", ret);
		ret = ht.doCalculation("1/3 + -1");
		assertEquals("-2/3", ret);
		
		ret = ht.doCalculation("1/3 + 1.1");
		assertEquals("43/30", ret);
		ret = ht.doCalculation("1/3 + -2.2");
		assertEquals("-28/15", ret);
		ret = ht.doCalculation("1/3 + -1.1");
		assertEquals("-23/30", ret);
		
		ret = ht.doCalculation("1/3 + 1/3");
		assertEquals("2/3", ret);
		ret = ht.doCalculation("1/3 + -1/3");
		assertEquals("0", ret);
		
		ret = ht.doCalculation("1/3 + 1e2");
		assertEquals("301/3", ret);
		ret = ht.doCalculation("1/3 + 1e-2");
		assertEquals("103/300", ret);
		ret = ht.doCalculation("1/3 + 1e1");
		assertEquals("31/3", ret);
		ret = ht.doCalculation("1/3 + -1e-2");
		assertEquals("97/300", ret);
		
		//fraction number minus
		ret = ht.doCalculation("1/3 - 1");
		assertEquals("-2/3", ret);
		ret = ht.doCalculation("1/3 - -1");
		assertEquals("4/3", ret);
		
		ret = ht.doCalculation("1/3 - 1.1");
		assertEquals("-23/30", ret);
		ret = ht.doCalculation("1/3 - -1.1");
		assertEquals("43/30", ret);
		
		ret = ht.doCalculation("1/3 - 1/3");
		assertEquals("0", ret);
		ret = ht.doCalculation("1/3 - -1/3");
		assertEquals("2/3", ret);
		
		ret = ht.doCalculation("1/3 - 1e2");
		assertEquals("-299/3", ret);
		ret = ht.doCalculation("1/3 - -1e2");
		assertEquals("301/3", ret);
		ret = ht.doCalculation("1/3 - -1e-2");
		assertEquals("103/300", ret);
		ret = ht.doCalculation("1/3 - 1e-2");
		assertEquals("97/300", ret);
		
		//fraction number multiply
		ret = ht.doCalculation("1/3 * 1");
		assertEquals("1/3", ret);
		ret = ht.doCalculation("1/3 * -1");
		assertEquals("-1/3", ret);
		
		ret = ht.doCalculation("1/3 * 1.1");
		assertEquals("11/30", ret);
		ret = ht.doCalculation("1/3 * -1.1");
		assertEquals("-11/30", ret);
		
		ret = ht.doCalculation("1/3 * 1/3");
		assertEquals("1/9", ret);
		ret = ht.doCalculation("1/3 * -1/3");
		assertEquals("-1/9", ret);
		
		ret = ht.doCalculation("1/3 * 1e2");
		assertEquals("100/3", ret);
		ret = ht.doCalculation("1/3 * -1e2");
		assertEquals("-100/3", ret);
		ret = ht.doCalculation("1/3 * -1e-2");
		assertEquals("-1/300", ret);
		ret = ht.doCalculation("1/3 * 1e-2");
		assertEquals("1/300", ret);
		
		//fraction number divide
		ret = ht.doCalculation("1/3 / 1");
		assertEquals("1/3", ret);
		ret = ht.doCalculation("1/3 / -1");
		assertEquals("-1/3", ret);
		
		ret = ht.doCalculation("1/3 / 1.1");
		assertEquals("10/33", ret);
		ret = ht.doCalculation("1/3 / -1.1");
		assertEquals("-10/33", ret);
		
		ret = ht.doCalculation("1/3 / 1/3");
		assertEquals("1", ret);
		ret = ht.doCalculation("1/3 / -1/3");
		assertEquals("-1", ret);
		
		ret = ht.doCalculation("1/3 / 1e2");
		assertEquals("1/300", ret);
		ret = ht.doCalculation("1/3 / -1e2");
		assertEquals("-1/300", ret);
		ret = ht.doCalculation("1/3 / -1e-2");
		assertEquals("-100/3", ret);
		ret = ht.doCalculation("1/3 / 1e-2");
		assertEquals("100/3", ret);
		
		//scientific number operation
	
		ret = ht.doCalculation("1e2 + 1e2");
		assertEquals("2E2", ret);
//		ret = ht.doOperation("1e2 + -1e2");
//		assertEquals("0", ret);
//		ret = ht.doOperation("1e-2 + -1e-2");
//		assertEquals("0", ret);
		ret = ht.doCalculation("1e-2 + 1e-2");
		assertEquals("2E-2", ret);
		ret = ht.doCalculation("2e2 * 3e2");
		assertEquals("6E4", ret);
		ret = ht.doCalculation("1.2e2 * 1.2e2");
		assertEquals("1.44E4", ret);
		ret = ht.doCalculation("2e2 / 3e2");
		assertEquals("2/3", ret);
		ret = ht.doCalculation("2e1 / 3e2");
		assertEquals("1/15", ret);
	}
	
	@Test
	public void testPositive() throws Exception{
		HiwiiTerminal ht = new HiwiiTerminal();
		
		boolean ret = false;
		ret = ht.doPositive("2 > 1");
		assertTrue(ret);
		ret = ht.doPositive("2 > -1");
		assertTrue(ret);
		
		ret = ht.doPositive("1 > 1.1");
		assertFalse(ret);
		ret = ht.doPositive("1 > -1.1");
		assertTrue(ret);
		
		ret = ht.doPositive("1 > 1/2");
		assertTrue(ret);
		ret = ht.doPositive("1 > -1/2");
		assertTrue(ret);
		
		ret = ht.doPositive("1 > 1e10");
		assertFalse(ret);
		ret = ht.doPositive("1 > 1e-2");
		assertTrue(ret);
		ret = ht.doPositive("-10 > 1e1");
		assertFalse(ret);
		ret = ht.doPositive("1 > -1e10");
		assertTrue(ret);
		
		ret = ht.doPositive("2 < 1");
		assertFalse(ret);
		ret = ht.doPositive("2 < -1");
		assertFalse(ret);
		
		ret = ht.doPositive("2 < 1.1");
		assertFalse(ret);
		ret = ht.doPositive("2 < -1.1");
		assertFalse(ret);
		
		ret = ht.doPositive("2 < 1/2");
		assertFalse(ret);
		ret = ht.doPositive("1 < -1/2");
		assertFalse(ret);
		
		ret = ht.doPositive("1 < 1e10");
		assertTrue(ret);
		ret = ht.doPositive("1 < -1e10");
		assertFalse(ret);
		ret = ht.doPositive("1 < 1e-2");
		assertFalse(ret);
		ret = ht.doPositive("1 < -1e-2");
		assertFalse(ret);
		
		ret = ht.doPositive("2 >=1");
		assertTrue(ret);
		ret = ht.doPositive("2 >=-1");
		assertTrue(ret);
		
		ret = ht.doPositive("1 >=1.1");
		assertFalse(ret);
		ret = ht.doPositive("1 >=-1.1");
		assertTrue(ret);
		
		ret = ht.doPositive("1 >=1/2");
		assertTrue(ret);
		ret = ht.doPositive("1 >=-1/2");
		assertTrue(ret);
		
		ret = ht.doPositive("1 >=1e10");
		assertFalse(ret);
		ret = ht.doPositive("1 >=1e-2");
		assertTrue(ret);
		ret = ht.doPositive("-10 >=1e1");
		assertFalse(ret);
		ret = ht.doPositive("1 >=-1e10");
		assertTrue(ret);
		
		ret = ht.doPositive("2 <= 1");
		assertFalse(ret);
		ret = ht.doPositive("2 <= -1");
		assertFalse(ret);
		
		ret = ht.doPositive("2 <= 1.1");
		assertFalse(ret);
		ret = ht.doPositive("2 <= -1.1");
		assertFalse(ret);
		
		ret = ht.doPositive("2 <= 1/2");
		assertFalse(ret);
		ret = ht.doPositive("1 <= -1/2");
		assertFalse(ret);
		
		ret = ht.doPositive("1 <= 1e10");
		assertTrue(ret);
		ret = ht.doPositive("1 <= -1e10");
		assertFalse(ret);
		ret = ht.doPositive("1 <= 1e-2");
		assertFalse(ret);
		ret = ht.doPositive("1 <= -1e-2");
		assertFalse(ret);
		
		ret = ht.doPositive("2 = 1");
		assertFalse(ret);
		ret = ht.doPositive("2 = -1");
		assertFalse(ret);
		
		ret = ht.doPositive("2 = 1.1");
		assertFalse(ret);
		ret = ht.doPositive("2 = -1.1");
		assertFalse(ret);
		
		ret = ht.doPositive("2 = 1/2");
		assertFalse(ret);
		ret = ht.doPositive("1 = -1/2");
		assertFalse(ret);
		
		ret = ht.doPositive("1 = 1e10");
		assertFalse(ret);
		ret = ht.doPositive("1 = -1e10");
		assertFalse(ret);
		ret = ht.doPositive("1 = 1e-2");
		assertFalse(ret);
		ret = ht.doPositive("1 = -1e-2");
		assertFalse(ret);
		
		ret = ht.doPositive("2 !=1");
		assertTrue(ret);
		ret = ht.doPositive("2 !=-1");
		assertTrue(ret);
		
		ret = ht.doPositive("2 !=1.1");
		assertTrue(ret);
		ret = ht.doPositive("2 !=-1.1");
		assertTrue(ret);
		
		ret = ht.doPositive("2 !=1/2");
		assertTrue(ret);
		ret = ht.doPositive("1 !=-1/2");
		assertTrue(ret);
		
		ret = ht.doPositive("1 !=1e10");
		assertTrue(ret);
		ret = ht.doPositive("1 !=-1e10");
		assertTrue(ret);
		ret = ht.doPositive("1 !=1e-2");
		assertTrue(ret);
		ret = ht.doPositive("1 !=-1e-2");
		assertTrue(ret);

	}
	public String handleWith(String str){
		String com = str.substring(2, str.length() - 2);
		Expression expr = StringUtil.parseString(com);
		if(str.startsWith("<#")){			
			return "executing";
		}else{
			return "operating";
		}
	}
	
	@Test
	public void testParser(){
		String aa = "<#dfjkdsfldfj#>|<$abcde$>fkldsjf<#dffj#>";
		handleWith(aa);
	}
	
	
	@Test
	public void testReg1(){
		RegularExpression re1 = new SimpleRegular("");
		RegularExpression re2 = new SimpleRegular("abc");
		RegularExpression re3 = new SimpleRegular("345");
		
		assertTrue(re1.match(""));
		assertFalse(re1.match("a"));
		re1 = new SimpleRegular("abc");
		assertTrue(re1.match("abc"));
		assertFalse(re1.match("a"));
		
		String str = "abc345abc";
				
		int n = re1.locate(str, 0, true);
		System.out.println("n=" + n);
		assertTrue(re2.guide(str, 0, true));
		assertFalse(re3.guide(str, 0, true));
		
		assertTrue(re1.guide(str, 0, true));
		
		n = re2.count(str, 0, true, true);
		System.out.println("n=" + n);
		
		n = re3.count(str, 3, true, true);
		System.out.println("len=" + n);
	}
	
	@Test
	public void testRegular() throws Exception{
		HiwiiTerminal ht = new HiwiiTerminal();
		assertTrue(ht.doPositive("\"abc\"$match(alternate(\"45\",\"abc\"))"));
		assertEquals("3",ht.doCalculation("\"123abc\".locate(alternate(\"45\",\"abc\"), 0)"));
		assertEquals("3",ht.doCalculation("\"abc11223\".count(alternate(\"45\",\"abc\"), 0)"));
	}
	@Test
	public void testReg(){
		String reg0 = "(<#(.*?)#>)|(<\\$(.*?)\\$>)";
		String reg1 = "(<$(.*?)$>)|(<#(.*?)#>)";
		String aa = "<#dfjkdsfldfj#>|<$abcde$>fkldsjf<#dffj#>";
		Pattern p = Pattern.compile(reg0);
		Matcher m = p.matcher(aa);
		String result = "";
		int pos = 0;
		while(m.find()){
			String str0 = m.group(0);
			String rep = "==" + handleWith(str0);
			result = result + aa.substring(pos, m.start());
			result = result + rep;
			pos = m.end();
		}
		System.out.println(result);
	}
	
	@Test
	public void testMatching(){
		String str = "aabbaa";
		RegularExpression re0 = new SimpleRegular("aa");
		RegularExpression re1 = new SimpleRegular("bb");
		MatchGroup re2 = new MatchGroup();
		
		List<Entity> list = new ArrayList<Entity>();
		list.add(new IntegerNumber("1"));
		re2.setArguments(list);
		
		CompoundRegular cr = new CompoundRegular();
		cr.add(re0);
		cr.add(re1);
		cr.add(re2);
		
		MatchResult m = new MatchResult();
		MatchResult mr = cr.matchResult(str, m, 0, 0);
		System.out.println("end");
	}
	
	@Test
	public void testNamedMatching(){
		String str = "aabbaa";
		RegularExpression re0 = new SimpleRegular("aa");
		RegularExpression re1 = new SimpleRegular("bb");
		MatchGroup re2 = new MatchGroup();
		
		List<Entity> list = new ArrayList<Entity>();
		list.add(new StringExpression("hi"));
		re2.setArguments(list);
		
		NamedRegular nr = new NamedRegular();
		nr.setName("hi");
		nr.setRegular(re0);
		
		CompoundRegular cr = new CompoundRegular();
		cr.add(nr);
		cr.add(re1);
		cr.add(re2);
		
		MatchResult m = new MatchResult();
		MatchResult mr = cr.matchResult(str, m, 0, 0);
		System.out.println("end");
	}
	
	@Test
	public void testMyRegular(){
		assertTrue(StringUtil.isIdentifier("บน"));
		assertTrue(StringUtil.isIdentifier("บน1"));
		assertTrue(StringUtil.isIdentifier("_บน1"));
		
		assertFalse(StringUtil.isIdentifier("1aa"));
		
		assertTrue(StringUtil.isInteger("0"));
		assertTrue(StringUtil.isInteger("01"));
		assertTrue(StringUtil.isInteger("1"));

		assertFalse(StringUtil.isInteger("1aa"));
	}
	
	@Test
	public void testRegularRepeat(){
		AlternateRegular ar = new AlternateRegular();
		SimpleRegular re0 = new SimpleRegular("");
		SimpleRegular re1 = new SimpleRegular("a");
		SimpleRegular re2 = new SimpleRegular("b");
		
		List<RegularExpression> list = new ArrayList<RegularExpression>();
		list.add(re0);
		list.add(re1);
		list.add(re2);
		ar.setAlters(list);
		assertTrue(ar.match("a"));
		
		RepeatTimes rt = new RepeatTimes(ar, 3);
		assertTrue(rt.match("ab"));
	}
	@Test
	public void testOperation_String(){
		String command = "\"abc\"";
		Expression exp = parser(command);
		Entity res = term.getSession().getContext().doCalculation(exp);
		System.out.println(res.toString());
	}
	
	@Test
	public void testOperation_int(){
		String command = "9";
		Expression exp = parser(command);
		Entity res = term.getSession().getContext().doCalculation(exp);
		System.out.println(res.toString());
	}
	
	public Expression parser(String str){
		StringExpression se = new StringExpression(str);
		IdentifierExpression ie = new IdentifierExpression("toExpression");
		Entity ret = se.doIdentifierCalculation("toExpression");//se.doOperaion(ie, session.getPool());
		try {
			if(ret instanceof HiwiiException){
				throw new Exception();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Expression exp = (Expression) ret;
		return exp;
	}
}
