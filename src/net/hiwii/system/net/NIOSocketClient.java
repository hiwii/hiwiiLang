package net.hiwii.system.net;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOSocketClient{ 
	private ByteBuffer buffer = ByteBuffer.allocate (1024);
	
	public String  cognize(HostObject host, String msg){
		//一个字节的head，用来表示cognition,operation,action,object,judgment,definition
		byte head = 0;
		buffer.clear();
		buffer.put(head);
		buffer.put(msg.getBytes());
		byte[] ret;
		try {
			ret = send(host);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "null";
		}
		return new String(ret);
	}
	
	public String  does(HostObject host, String msg){
		//一个字节的head，用来表示cognition,operation,action,object,judgment,definition
		byte head = 1;
		buffer.clear();
		buffer.put(head);
		buffer.put(msg.getBytes());
		byte[] ret;
		try {
			ret = send(host);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "null";
		}
		return new String(ret);
	}

	public byte[] send(HostObject host) throws IOException{
		String ip = host.getIpaddress();
		int port = host.getPort();
		
		InetSocketAddress addr = new InetSocketAddress (ip, port);
		
		SocketChannel sc = SocketChannel.open(); 
		Selector sel = Selector.open();
		try{ 
			sc.configureBlocking(false); 
			sc.socket().bind(addr); 
			sc.register(sel, SelectionKey.OP_READ | SelectionKey.OP_WRITE 
					| SelectionKey.OP_CONNECT); 
			int i = 0; 
			boolean written = false; 
			boolean done = false; 

			while(!done){ 
				sel.select(); 
				Iterator<SelectionKey> it = sel.selectedKeys().iterator(); 
				while(it.hasNext()){ 
					SelectionKey key = it.next(); 
					it.remove(); 
					//获取创建通道选择器事件键的套接字通道  
					sc = (SocketChannel)key.channel(); 
//					//当前通道选择器产生连接已经准备就绪事件，并且客户端套接字  
//					//通道尚未连接到服务端套接字通道  
//					if(key.isConnectable() && !sc.isConnected()){ 
//						InetAddress addr = InetAddress.getByName(null); 
//						//客户端套接字通道向服务端套接字通道发起非阻塞连接  
//						boolean success = sc.connect(new InetSocketAddress( 
//								addr, 9088)); 
//						//如果客户端没有立即连接到服务端，则客户端完成非立即连接操作  
//						if(!success) sc.finishConnect(); 
//					}
					while ( ! sc.finishConnect( )) {

					}
					
					//如果通道选择器产生写入操作已准备好事件，并且尚未想通道写入数据  
					if(key.isWritable() && !written){ 
						//向套接字通道中写入数据 
						buffer.flip();
						sc.write(buffer);
//						if(i < 10) sc.write(ByteBuffer.wrap(new String(“howdy ” + i + 
//								‘\n’).getBytes())); 
//						else if(i == 10)sc.write(ByteBuffer.wrap(newString(“END”). 
//								getBytes())); 
						written = true; 
						i++; 
					}
					//如果通道选择器产生读取操作已准备好事件，且已经向通道写入数据  
					if(key.isReadable() && written){ 
						buffer.clear();
						if(sc.read(buffer) > 0){ 
							written = false; 
							buffer.flip();
							
							//从套接字通道中读取数据  
							String response = buffer.toString(); 
							System.out.println(response);
							return response.getBytes();
						} 
						done = true;
					}
				}
			} 
		}finally{ 
			sc.close(); 
			sel.close(); 
		}
		return "null".getBytes();
	}
}  
