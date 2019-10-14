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
 * 系统间session记录以索引。
 * message包头必须包括：1,p2p or groupTalk. 2,text包头 or bin包头
 * 3,port. 4,if bin,media type|media format.5,if groupTalk,group Id
 * 系统间包格式由包头和内容组成。
 * 包头格式：
 * 1，port len=6 0-5
 * 2，type(act|bin) len=3 6-8
 * 3，keep|talk len=4 live=keep-alive 9-12
 * 4，local sessionContextId len=32 13-44
 * 5，remote sessionContextId len=32  45-76 初次发送为空。
 * 媒体信息头格式
 * 3，媒体类型(pic|aud|mov|fil) len=3  fil=file
 * 4，媒体格式(pic:png/jpg/gif/bmp
 * 		aud:mp3,mov:mp4,fil
 * 通讯过程如下：
 * 1，localhost建立hostTerminal和sessionContext
 * 2，hostTerminal判断链接方式是否互动常连接方式。
 * 2.1 普通连接方式，发送包中包括本地监听sessionId
 * 系统间包格式前面8个字符采用特殊字符"!@#$%^&*"。
 * 在格式包头之后是2字节类型。'ac'表示指令，'md'表示媒体。
 * 在媒体之后，预留64字节媒体信息。
 * host只支持接收指令，答复的时间由host确定。
 * 如果包格式没有上述字符串包头，则可以认为是普通包。
 * 二进制文件的传送以普通包格式传送。
 * 系统认可的二进制文件包括图片(png/jpg/gif/bmp),声音(mp3),视频(mp4)
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
		//两个char的head，用来表示operation,action,judgment
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
