package app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeSet;

import org.junit.Test;

import net.hiwii.cache.CachedItem;
import net.hiwii.cognition.Expression;
import net.hiwii.expr.StringExpression;

public class MyTest6 {
	@Test
	public void testSyntax(){
		String str = "new[Definition:name::parent[inteface]{type}]";
		if(3>=2) {
			
		}
		Expression expr = new StringExpression(str).toExpression();
		System.out.print(expr.toString());
	}

	@Test
	public void testString(){
		String a= new String("abc");
		String b= new String("abc");
		if(a == b){          //结果是false
			System.out.println("1:true");
		}else{
			System.out.println("1:false");
		}
		a= "abc";
		b= "abc";
		if(a == b){          //结果是false
			System.out.println("2:true");
		}else{
			System.out.println("2:false");
		}

		a = "ABC";
		b="AB";
		String c=b+"C";
		System.out.println(a==c);
	}

	@Test
	public void testTreeSet(){
		TreeSet<CachedItem> set = new TreeSet<CachedItem>();  
		set.add(new CachedItem("e"));  
		set.add(new CachedItem("m"));  
		set.add(new CachedItem("w"));  
		set.add(new CachedItem("z"));  

		//此时的顺序是 e m  w z  
		System.out.println(set);  

		//把第一个改为 a ，虽然改变了但是依然维持了正确的顺序  
		set.first().setKey("a");  
		//删除 a 是成功的  
		set.remove(new CachedItem("a"));  
		// 此时的顺序是 m w z， a被删除了  
		System.out.println(set);  

		//采用同样的方式，但是改变打乱了正确的顺序  
		set.first().setKey("y");  
		//没有自动重新排序
		System.out.println(set);  
		//改变了顺序后就无法删除了  
		set.remove(new CachedItem("y"));  
		//打印出是 y w z，并没有删除成功  
		System.out.println(set);  

		//下面考虑一个极端的情况，就是改变的和它相邻的相等了，此时其实能删除，只是就删除两个相等的元素前面的那个  
		set.first().setKey("w");  
		set.remove(new CachedItem("w"));  
		System.out.println(set);  

		//另外未被改变的总能正确删除  
		set.add(new CachedItem("b"));  
		set.first().setKey("z");  
		set.remove(new CachedItem("z"));  

		System.out.println(set);  
	}
	
	@Test
	public void testTreeSet2(){
		TreeSet<CachedItem> set = new TreeSet<CachedItem>();  
		set.add(new CachedItem("e"));  
		set.add(new CachedItem("m"));  
		set.add(new CachedItem("w"));  
		set.add(new CachedItem("z"));  

		//此时的顺序是 e m  w z  
		System.out.println(set);  

		//把第一个改为 a ，虽然改变了但是依然维持了正确的顺序  
		CachedItem item = set.first();
		set.remove(set.first());
		System.out.println(set);
		item.setKey("y");
		set.add(item);
		System.out.println(set);
	}
	
	@Test
	public void testCalendar(){
		Calendar cal = Calendar.getInstance();
		long mills = cal.getTimeInMillis();
//		System.out.println(mills); 
//		System.out.println(cal.get(Calendar.MILLISECOND));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
//		String str = sdf.format(cal.getTime());
//		System.out.println(str); 
		System.out.println(sdf.format(cal.getTime())); 
		cal.add(Calendar.HOUR_OF_DAY, 100);
		System.out.println(sdf.format(cal.getTime())); 
	}
}
