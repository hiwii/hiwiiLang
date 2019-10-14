package net.hiwii.system.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map.Entry;

import net.hiwii.protocol.HiwiiMessage;
import net.hiwii.protocol.HiwiiProtocolDecoder;

public class SocketClient {
		
	public static void main (String [] argv)
	throws Exception
	{
//		SocketClient cli = new SocketClient( );
//		cli.run();
	}
	
	private ByteBuffer buffer = ByteBuffer.allocate (1024*10124);

	public String  doRequest(HostObject host, String msg, String head){//, String sessionId
//		//两个char的head，用来表示operation,action,judgment
//		buffer.clear();
//		String sessionId = LocalHost.getInstance().getHostId();
//		buffer.put((head + sessionId + "#" + msg).getBytes());
		encode(msg, head);
		byte[] ret = talkTo(host);
		return new String(ret);
	}

	public byte[] talkTo(HostObject host){
		try {
			String ip = host.getIpaddress();
			int port = host.getPort();
			
			InetSocketAddress addr = new InetSocketAddress (ip, port);
			
			SocketChannel sc = SocketChannel.open( );
			sc.configureBlocking (true);

//			sc.socket().connect (addr, 10000);//todo timeout
			sc.connect (addr);
			
			while ( ! sc.finishConnect( )) {
				System.out.print(".");
			}
			byte[] ret = transfer(sc);
			
			sc.close( );

			return ret;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public byte[] transfer(SocketChannel channel){
		try {
//			buffer.put(msg.toString().getBytes());
			buffer.flip();
			while(buffer.hasRemaining()){
				channel.write (buffer);
			}
			buffer.clear();
			int count = 0, len = 0;
			while ((count = channel.read (buffer)) > 0) {
				len = len + count;
			}
			if(len >= 0){
				buffer.flip( ); // Make bufferfer readable
//				byte[] aa = new byte[count];
//				buffer.get(aa);
//				String str = new String(aa);
				HiwiiMessage message = HiwiiProtocolDecoder.decode(buffer, len);
				return message.getContent().getBytes();
			}
			System.out.println("err get response!");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public void run() throws Exception{
		try {
			String host = "localhost";
			int port = 9099;

			InetSocketAddress addr = new InetSocketAddress (host, port);
			SocketChannel sc = SocketChannel.open( );
			sc.configureBlocking (false);
//			System.out.println ("initiating connection");
			sc.connect (addr);

			while ( ! sc.finishConnect( )) {
				
			}
			sayHello(sc);
			System.out.println ("connection established");
			// Do something with the connected socket
			// The SocketChannel is still nonblocking
			sc.close( );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// ----------------------------------------------------------
	/**
	 * Spew a greeting to the incoming client connection.
	 * @param channel The newly connected SocketChannel to say hello to.
	 */
	private void sayHello (SocketChannel channel)
	throws Exception
	{
		ByteBuffer buffer = ByteBuffer.allocate (1024);//ByteBuffer.wrap("Hi there!\r\n".getBytes( ));
		buffer.put("head:".getBytes());
		buffer.put("Hi there!\r\n".getBytes( ));
		System.out.println(buffer.array().length + "\n");
		String str = new String(buffer.array());
		System.out.println(str);
		buffer.flip();
		while(buffer.hasRemaining()){
			channel.write (buffer);
		}
//		Bytebufffer buff = Bytebufffer.allocate (1024);
		buffer.clear();
		int count = 0 ;
		while ((count = channel.read (buffer)) > 0) {
			buffer.flip( ); // Make bufferfer readable
			byte[] aa = new byte[count];
			buffer.get(aa);
			str = new String(aa);
			System.out.println("recived response:" + str);
		}
	}
	
	public void encode(String src, String head){
		buffer.clear();
		buffer.put("hiwii1.0\r\n".getBytes());
		HiwiiMessage message = new HiwiiMessage();
		message.putHeader("method", head);
//		message.putHeader(, value);
		for(Entry<String,String> entry:message.getHeaders().entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();
			buffer.put((key + ":" + value + "\r\n").getBytes());
		}
		buffer.put("\r\n".getBytes());
		buffer.put(src.getBytes());
	}
}
