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
		//һ���ֽڵ�head��������ʾcognition,operation,action,object,judgment,definition
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
		//һ���ֽڵ�head��������ʾcognition,operation,action,object,judgment,definition
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
					//��ȡ����ͨ��ѡ�����¼������׽���ͨ��  
					sc = (SocketChannel)key.channel(); 
//					//��ǰͨ��ѡ�������������Ѿ�׼�������¼������ҿͻ����׽���  
//					//ͨ����δ���ӵ�������׽���ͨ��  
//					if(key.isConnectable() && !sc.isConnected()){ 
//						InetAddress addr = InetAddress.getByName(null); 
//						//�ͻ����׽���ͨ���������׽���ͨ���������������  
//						boolean success = sc.connect(new InetSocketAddress( 
//								addr, 9088)); 
//						//����ͻ���û���������ӵ�����ˣ���ͻ�����ɷ��������Ӳ���  
//						if(!success) sc.finishConnect(); 
//					}
					while ( ! sc.finishConnect( )) {

					}
					
					//���ͨ��ѡ��������д�������׼�����¼���������δ��ͨ��д������  
					if(key.isWritable() && !written){ 
						//���׽���ͨ����д������ 
						buffer.flip();
						sc.write(buffer);
//						if(i < 10) sc.write(ByteBuffer.wrap(new String(��howdy �� + i + 
//								��\n��).getBytes())); 
//						else if(i == 10)sc.write(ByteBuffer.wrap(newString(��END��). 
//								getBytes())); 
						written = true; 
						i++; 
					}
					//���ͨ��ѡ����������ȡ������׼�����¼������Ѿ���ͨ��д������  
					if(key.isReadable() && written){ 
						buffer.clear();
						if(sc.read(buffer) > 0){ 
							written = false; 
							buffer.flip();
							
							//���׽���ͨ���ж�ȡ����  
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
