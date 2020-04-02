package app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeSet;

import org.junit.Test;

import net.hiwii.cache.CachedItem;
import net.hiwii.cognition.Expression;
import net.hiwii.expr.StringExpression;
import net.hiwii.system.util.StringUtil;

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
		if(a == b){          //�����false
			System.out.println("1:true");
		}else{
			System.out.println("1:false");
		}
		a= "abc";
		b= "abc";
		if(a == b){          //�����false
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

		//��ʱ��˳���� e m  w z  
		System.out.println(set);  

		//�ѵ�һ����Ϊ a ����Ȼ�ı��˵�����Ȼά������ȷ��˳��  
		set.first().setKey("a");  
		//ɾ�� a �ǳɹ���  
		set.remove(new CachedItem("a"));  
		// ��ʱ��˳���� m w z�� a��ɾ����  
		System.out.println(set);  

		//����ͬ���ķ�ʽ�����Ǹı��������ȷ��˳��  
		set.first().setKey("y");  
		//û���Զ���������
		System.out.println(set);  
		//�ı���˳�����޷�ɾ����  
		set.remove(new CachedItem("y"));  
		//��ӡ���� y w z����û��ɾ���ɹ�  
		System.out.println(set);  

		//���濼��һ�����˵���������Ǹı�ĺ������ڵ�����ˣ���ʱ��ʵ��ɾ����ֻ�Ǿ�ɾ��������ȵ�Ԫ��ǰ����Ǹ�  
		set.first().setKey("w");  
		set.remove(new CachedItem("w"));  
		System.out.println(set);  

		//����δ���ı��������ȷɾ��  
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

		//��ʱ��˳���� e m  w z  
		System.out.println(set);  

		//�ѵ�һ����Ϊ a ����Ȼ�ı��˵�����Ȼά������ȷ��˳��  
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
	
	@Test
	public void testMD5(){
		String url = "http://www.baidu.com";
        String generateHash = StringUtil.hashOperation(url);
        System.out.println(generateHash);
	}
}
