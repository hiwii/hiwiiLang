package net.hiwii.term;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.NormalEnd;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.util.StringUtil;

/**
 * ϵͳ��session��¼��������
 * message��ͷ���������1,p2p or groupTalk. 2,text��ͷ or bin��ͷ
 * 3,port. 4,if bin,media type|media format.5,if groupTalk,group Id
 * ϵͳ�����ʽ�ɰ�ͷ��������ɡ�
 * ��ͷ��ʽ��
 * 1��port len=6 0-5
 * 2��type(act|bin) len=3 6-8
 * 3��keep|talk len=4 live=keep-alive 9-12
 * 4��local sessionContextId len=32 13-44
 * 5��remote sessionContextId len=32  45-76 ���η���Ϊ�ա�
 * ý����Ϣͷ��ʽ
 * 3��ý������(pic|aud|mov|fil) len=3  fil=file
 * 4��ý���ʽ(pic:png/jpg/gif/bmp
 * 		aud:mp3,mov:mp4,fil
 * ͨѶ�������£�
 * 1��localhost����hostTerminal��sessionContext
 * 2��hostTerminal�ж����ӷ�ʽ�Ƿ񻥶������ӷ�ʽ��
 * 2.1 ��ͨ���ӷ�ʽ�����Ͱ��а������ؼ���sessionId
 * ϵͳ�����ʽǰ��8���ַ����������ַ�"!@#$%^&*"��
 * �ڸ�ʽ��ͷ֮����2�ֽ����͡�'ac'��ʾָ�'md'��ʾý�塣
 * ��ý��֮��Ԥ��64�ֽ�ý����Ϣ��
 * hostֻ֧�ֽ���ָ��𸴵�ʱ����hostȷ����
 * �������ʽû�������ַ�����ͷ���������Ϊ����ͨ����
 * �������ļ��Ĵ�������ͨ����ʽ���͡�
 * ϵͳ�ϿɵĶ������ļ�����ͼƬ(png/jpg/gif/bmp),����(mp3),��Ƶ(mp4)
 * @author Administrator
 *
 */
public class HostTerminal extends Terminal {
	private ByteBuffer buffer = ByteBuffer.allocate (1024 * 1024);
	private String ipaddress;
	private int port;
//	private String head = "!@#$%^&*";
	
	public HostTerminal() {

	}
	public HostTerminal(String ipaddress, int port) {
		this.ipaddress = ipaddress;
		this.port = port;
	}

	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}


	@Override
	public void doRequest(String comm) {
		sendMessage(comm);
	}

	@Override
	public String doResponse(String comm) {
		String ret = "answer[" + comm + "]";
		sendMessage(ret);
		return "";
	}

	/**
	 * 
	 * @param host
	 * @param msg
	 * @return
	 */
	public Expression  sendMessage(String msg){//, String sessionId
		//����char��head��������ʾoperation,action,judgment
		buffer.clear();
		
		if(String.valueOf(port).length() > 6){
			return new HiwiiException();
		}
		String type = "act";
		String head = StringUtil.fill(String.valueOf(port), 6);
		head = head + type;
		buffer.put((head + msg).getBytes(Charset.forName("UTF-8")));
		try {
			InetSocketAddress addr = new InetSocketAddress (ipaddress, port);
			
			SocketChannel sc = SocketChannel.open( );
			sc.configureBlocking (false);
			sc.connect (addr);//todo timeout
			
			while ( ! sc.finishConnect( )) {

			}
			
			buffer.flip();
			while(buffer.hasRemaining()){
				sc.write (buffer);
			}
			buffer.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new HiwiiException();
		}
		return new NormalEnd();
	}
	@Override
	public String toString() {
		return ipaddress;
	}
	@Override
	public void doQuestion(String quest) {
		// TODO Auto-generated method stub
		
	}
	
}
