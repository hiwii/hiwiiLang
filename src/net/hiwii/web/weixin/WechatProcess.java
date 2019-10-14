package net.hiwii.web.weixin;

import net.hiwii.context.SessionContext;
import net.hiwii.system.LocalHost;
import net.hiwii.web.NetTerminal;



/** 
 * ΢��xml��Ϣ���������߼��� 
 * @author pamchen-1 
 * 
 */  
public class WechatProcess {  
	/** 
	 * ��������xml����ȡ���ܻظ������ͨ��ͼ�������api�ӿڣ� 
	 * @param xml ���յ���΢������ 
	 * @return  ���յĽ��������xml��ʽ���ݣ� 
	 */  
	public String processWechatMag(String xml){  
		/** ����xml���� */  
		ReceiveXmlEntity xmlEntity = new ReceiveXmlProcess().getMsgEntity(xml);

		/** ���ı���ϢΪ��������ͼ�������api�ӿڣ���ȡ�ظ����� */  
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

		/** ��ʱ������û�������ǡ���á����ھ�������Ĺ���֮��resultΪ����Ҳ�á����Ƶ�����  
		 *  ��Ϊ���ջظ���΢�ŵ�Ҳ��xml��ʽ�����ݣ�������Ҫ�����װΪ�ı����ͷ�����Ϣ 
		 * */  
		result = new FormatXmlProcess().formatXmlAnswer(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), result);
		
		return result;  
	} 
	
	/**
	 * ������ķ�����Ϊ��������һ�����entity�Ի���û���Ϣ��
	 * ����CDATAҪ���ַ����в��ܳ���]]>�����[[1,2],[2,3]]>4����������ʧ�ܡ�
	 * @param xml
	 * @return
	 */
	public String processXmlEntity(ReceiveXmlEntity xmlEntity){
		/** ���ı���ϢΪ��������ͼ�������api�ӿڣ���ȡ�ظ����� */  
		String result = "";  
		if("text".endsWith(xmlEntity.getMsgType())){  
			result = "testResult"; //new TulingApiProcess().getTulingResult(xmlEntity.getContent());  
		}  

		/** ��ʱ������û�������ǡ���á����ھ�������Ĺ���֮��resultΪ����Ҳ�á����Ƶ�����  
		 *  ��Ϊ���ջظ���΢�ŵ�Ҳ��xml��ʽ�����ݣ�������Ҫ�����װΪ�ı����ͷ�����Ϣ 
		 * */  
		result = new FormatXmlProcess().formatXmlAnswer(xmlEntity.getFromUserName(), xmlEntity.getToUserName(), result);

		return result;	
	}
}  