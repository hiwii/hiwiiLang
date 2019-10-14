package net.hiwii.web.weixin;

import net.hiwii.context.SessionContext;
import net.hiwii.system.LocalHost;
import net.hiwii.web.NetTerminal;



/** 
 * 微信xml消息处理流程逻辑类 
 * @author pamchen-1 
 * 
 */  
public class WechatProcess {  
	/** 
	 * 解析处理xml、获取智能回复结果（通过图灵机器人api接口） 
	 * @param xml 接收到的微信数据 
	 * @return  最终的解析结果（xml格式数据） 
	 */  
	public String processWechatMag(String xml){  
		/** 解析xml数据 */  
		ReceiveXmlEntity xmlEntity = new ReceiveXmlProcess().getMsgEntity(xml);

		/** 以文本消息为例，调用图灵机器人api接口，获取回复内容 */  
		String result = "";  
		if("text".endsWith(xmlEntity.getMsgType())){  
			//result = new TulingApiProcess().getTulingResult(xmlEntity.getContent());  
			//result = "testResult";
			LocalHost local = LocalHost.getInstance();
			String user = xmlEntity.getFromUserName();
			SessionContext sc = local.getSessionContextOfWeixin(user);
			NetTerminal nt = (NetTerminal) sc.getSession().getTerminal();
			nt.doCommand(xmlEntity.getContent());
			int i = 0;
			for(String str:nt.getResults()){
				if(i == 0){
					result = str;
					continue;
				}
				i++;
				result = result + "\r\n" + str;
			}
		} 
		
		if(result.length() == 0){
			result = "OK";
		}

		/** 此时，如果用户输入的是“你好”，在经过上面的过程之后，result为“你也好”类似的内容  
		 *  因为最终回复给微信的也是xml格式的数据，所有需要将其封装为文本类型返回消息 
		 * */  
		result = new FormatXmlProcess().formatXmlAnswer(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), result);
		
		return result;  
	} 
	
	/**
	 * 把上面的方法分为两步。第一步获得entity以获得用户信息。
	 * 由于CDATA要求字符串中不能出现]]>，因此[[1,2],[2,3]]>4的情况会解析失败。
	 * @param xml
	 * @return
	 */
	public String processXmlEntity(ReceiveXmlEntity xmlEntity){
		/** 以文本消息为例，调用图灵机器人api接口，获取回复内容 */  
		String result = "";  
		if("text".endsWith(xmlEntity.getMsgType())){  
			result = "testResult"; //new TulingApiProcess().getTulingResult(xmlEntity.getContent());  
		}  

		/** 此时，如果用户输入的是“你好”，在经过上面的过程之后，result为“你也好”类似的内容  
		 *  因为最终回复给微信的也是xml格式的数据，所有需要将其封装为文本类型返回消息 
		 * */  
		result = new FormatXmlProcess().formatXmlAnswer(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), result);

		return result;	
	}
}  