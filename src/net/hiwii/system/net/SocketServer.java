package net.hiwii.system.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import net.hiwii.cognition.Expression;
import net.hiwii.context.SessionContext;
import net.hiwii.expr.StringExpression;
import net.hiwii.message.HiwiiException;
import net.hiwii.protocol.HiwiiMessage;
import net.hiwii.protocol.HiwiiProtocolDecoder;
import net.hiwii.system.LocalHost;
import net.hiwii.system.util.StringUtil;
import net.hiwii.view.Entity;

public class SocketServer extends Thread{
	private ByteBuffer buffer = ByteBuffer.allocate(1024);
	private Selector selector;
	
	private int port = 9099;
//	private LocalHost host = LocalHost.getInstance();
	
//	public static void main (String [] argv)
//	throws Exception
//	{
//		SocketServer serv = new SocketServer( );
//		serv.go();
//	}
	
	@Override
	public void run() {
		try {
			listen();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void init(){
		ServerSocketChannel servSocketChannel;

		try {
			servSocketChannel = ServerSocketChannel.open();
			servSocketChannel.configureBlocking(false);
			//绑定端口
			servSocketChannel.socket().bind(new InetSocketAddress(port));

			selector = Selector.open();
			servSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (ClosedChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public SocketServer() {
       init();
	}

	public SocketServer(int port) {
		this.port = port;
		init();
	}

	private void listen() {
        while(true){
            try{
                selector.select();             
                Iterator ite = selector.selectedKeys().iterator();
                 
                while(ite.hasNext()){
                    SelectionKey key = (SelectionKey) ite.next();                  
                    ite.remove();//确保不重复处理
                     
                    handleKey(key);
                }
            }
            catch(Throwable t){
                t.printStackTrace();
            }                          
        }              
    }
 
    private void handleKey(SelectionKey key)
            throws IOException, ClosedChannelException {
        SocketChannel channel = null;
         
        try{
            if(key.isAcceptable()){
                ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                channel = serverChannel.accept();//接受连接请求
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);
            }
            else if(key.isReadable()){
                channel = (SocketChannel) key.channel();
                buffer.clear();
                /*当客户端channel关闭后，会不断收到read事件，但没有消息，即read方法返回-1
                 * 所以这时服务器端也需要关闭channel，避免无限无效的处理*/              
                int count = channel.read(buffer);
                 
                if(count > 0){
                    //一定需要调用flip函数，否则读取错误数据
                    buffer.flip();
                    /*使用CharBuffer配合取出正确的数据
                    String question = new String(readBuffer.array());  
				                    可能会出错，因为前面readBuffer.clear();并未真正清理数据
				                    只是重置缓冲区的position, limit, mark，
				                    而readBuffer.array()会返回整个缓冲区的内容。
				                    decode方法只取readBuffer的position到limit数据。
				                    例如，上一次读取到缓冲区的是"where", clear后position为0，limit为 1024，
				                    再次读取“bye"到缓冲区后，position为3，limit不变，
				                    flip后position为0，limit为3，前三个字符被覆盖了，但"re"还存在缓冲区中，
				                    所以 new String(readBuffer.array()) 返回 "byere",
				                    而decode(readBuffer)返回"bye"。            
                    */
                    String ip = StringUtil.getIPAddress(channel.socket().getRemoteSocketAddress());
//        			String ret = response(req, head, sessionId, ip);
        			HiwiiMessage message = HiwiiProtocolDecoder.decode(buffer, count);
        			if(message == null){
        				handleWrite(key, "error");
        			}else{
        				String ret = response(message, ip);
        				handleWrite(key, ret);
        			}
                }
                else{
                    //这里关闭channel，因为客户端已经关闭channel或者异常了
                    channel.close();               
                }                      
            }
        }
        catch(Throwable t){
            t.printStackTrace();
            if(channel != null){
                channel.close();
            }
        }      
    }
	public void go() throws Exception{

		// Allocate an unbound server socket channel
		ServerSocketChannel serverChannel = ServerSocketChannel.open( );
		// Get the associated ServerSocket to bind it with
		ServerSocket serverSocket = serverChannel.socket( );
		// Create a new Selector for use below
		Selector selector = Selector.open( );
		// Set the port the server channel will listen to
		serverSocket.bind (new InetSocketAddress (port));
		// Set nonblocking mode for the listening socket
		serverChannel.configureBlocking (false);
		// Register the ServerSocketChannel with the Selector
		serverChannel.register (selector, SelectionKey.OP_ACCEPT);
		while (true) {
			// This may block for a long time. Upon returning, the
			// selected set contains keys of the ready channels.
			int n = selector.select( );
			if (n == 0) {
				continue; // nothing to do
			}
			// Get an iterator over the set of selected keys
			Iterator it = selector.selectedKeys().iterator( );
			// Look at each key in the selected set
			while (it.hasNext( )) {
				SelectionKey key = (SelectionKey) it.next( );
				// Is a new connection coming in?
				if (key.isAcceptable( )) {
					ServerSocketChannel server =
						(ServerSocketChannel) key.channel( );
					SocketChannel channel = server.accept( );
					registerChannel (selector, channel,
							SelectionKey.OP_READ);
//					System.out.println ("Incoming connection from: "
//							+ channel.socket().getRemoteSocketAddress( ));
//					sayHello (channel);
				}

				// Is there data to read on this channel?
				if (key.isReadable( )) {
					readDataFromSocket (key);
				}
				
//				if(key.isValid() && key.isWritable()){
//					handleWrite(key);
//				}
				// Remove key from selected set; it's been handled
				it.remove( );
			}
		}
	}

	// ----------------------------------------------------------
	/**
	 * Register the given channel with the given selector for
	 * the given operations of interest
	 */
	protected void registerChannel (Selector selector, SelectableChannel channel, int ops)
	throws Exception
	{
		if (channel == null) {
			return; // could happen
		}
		// Set the new channel nonblocking
		channel.configureBlocking (false);
		// Register it with the selector
		channel.register (selector, ops);
	}
	// ----------------------------------------------------------
	// Use the same byte buffer for all channels. A single thread is
	// servicing all the channels, so no danger of concurrent acccess.
	
	/**
	 * Sample data handler method for a channel with data ready to read.
	 * @param key A SelectionKey object associated with a channel
	 * determined by the selector to be ready for reading. If the
	 * channel returns an EOF condition, it is closed here, which
	 * automatically invalidates the associated key. The selector
	 * will then de-register the channel on the next select call.
	 */
	protected void readDataFromSocket (SelectionKey key)
	throws Exception
	{
		SocketChannel socketChannel = (SocketChannel) key.channel( );
		int count;
		buffer.clear( ); // Empty buffer
//		buffer.flip( ); // Make buffer readable
//		count = socketChannel.read (buffer);
//		System.out.println(count + "\n");
		// Loop while data is available; channel is nonblocking
		String con = "";
		int len = 0;
		while ((count = socketChannel.read (buffer)) > 0) {
			len = len + count;
		}
//			buffer.flip( ); // Make buffer readable
//			byte[] aa = new byte[count];
//			buffer.get(aa);
//			System.out.println(count + "\n");

//			buffer.clear();
			
//			String str = new String(aa);
//			System.out.println(str);
//			con = con + str;
			
//			buffer.flip();
//			System.out.println("remaining:" + buffer.hasRemaining( ));
			// Send the data; don't assume it goes all at once
//			while (buffer.hasRemaining( )) {
//				socketChannel.write (buffer);
//			}
			// WARNING: the above loop is evil. Because
			// it's writing back to the same nonblocking
			// channel it read the data from, this code can
			// potentially spin in a busy loop. In real life
			// you'd do something more useful than this.
//			buffer.clear( ); // Empty buffer
//			break;
//		}
	
//		count = -1;
//		if (count < 0) {
//			// Close channel on EOF, invalidates the key
//			socketChannel.close( );
//		}
		
//		buffer.flip();
//		if(len > 0){
//			byte[] aa = new byte[len];
//			buffer.get(aa);
//			con = new String(aa);
//		}
		
		buffer.flip();
		if(len > 0){
			String ip = StringUtil.getIPAddress(socketChannel.socket().getRemoteSocketAddress());
//			String ret = response(req, head, sessionId, ip);
			HiwiiMessage message = HiwiiProtocolDecoder.decode(buffer, len);
			if(message == null){
				handleWrite(key, "error");
			}else{
				String ret = response(message, ip);
				handleWrite(key, ret);
			}
		}else{
			handleWrite(key, "ok");
		}
		
	}
	
	private ByteBuffer wbuffer = ByteBuffer.allocate(1024*1024);
	protected void handleWrite (SelectionKey key, String resp) throws Exception{
		SocketChannel channel = (SocketChannel) key.channel( );
		HiwiiProtocolDecoder.encode(wbuffer, resp, "answer");
		channel.write(wbuffer);
		
//		wbuffer.clear();
		channel.close();
	}
	
	public String response(HiwiiMessage message, String ip){
		StringExpression se = new StringExpression(message.getContent());
//		IdentifierExpression ie = new IdentifierExpression("toExpression");
		Entity ret = se.doIdentifierCalculation("toExpression");//se.doOperaion(ie, session.getPool());
		if(ret instanceof HiwiiException){
			return "ex"; //exception
		}
		Expression exp = (Expression) ret;
		SessionContext sc = LocalHost.getInstance().getSessionContextByIP(ip);
//		getSession(sessionId, ip);//new SessionContext();
		Entity res = null;
		String msg = "";
		String head = message.getHeader("method");
		if(head.equals("calculation")){
			res = sc.doCalculation(exp);
			msg = msg + res.toString();
		}else if(head.equals("action")){
			res = sc.doAction(exp);
			msg = msg + res.toString();
		}else if(head.equals("decision")){
			res = sc.doDecision(exp);
			msg = msg + res.toString();
		}else{//"ne"
//			res = sc.doLadderNegative(exp, null);
//			for(Entity ent:sc.getResults()){
//				msg = msg + ent.toString() + "\n";
//			}
//			msg = msg + res.toString();
		}
//		session.getContext().clearResults();
		if(res instanceof HiwiiException){
			return "error"; //exception
		}
		return msg; //"as" for answer
	}
	
	public String response(String command, String head, String sessionId, String ip){
		StringExpression se = new StringExpression(command);
//		IdentifierExpression ie = new IdentifierExpression("toExpression");
		Entity ret = se.doIdentifierCalculation("toExpression");//se.doOperaion(ie, session.getPool());
		if(ret instanceof HiwiiException){
			return "ex"; //exception
		}
		Expression exp = (Expression) ret;
		SessionContext sc = getSession(sessionId, ip);//new SessionContext();
		Entity res = null;
		String msg = "";
		if(head.equals("calculation")){
			res = sc.doCalculation(exp);
			msg = msg + res.toString();
		}else if(head.equals("action")){
			res = sc.doAction(exp);
			msg = msg + res.toString();
		}else if(head.equals("decision")){
			res = sc.doDecision(exp);
			msg = msg + res.toString();
		}else{//"ne"
//			res = sc.doLadderNegative(exp, null);
//			for(Entity ent:sc.getResults()){
//				msg = msg + ent.toString() + "\n";
//			}
//			msg = msg + res.toString();
		}
//		session.getContext().clearResults();
		if(res instanceof HiwiiException){
			return "ex"; //exception
		}
		return "as" + sc.getSessionId() + "#" + msg; //"as" for answer
	}
	// ----------------------------------------------------------

	public SessionContext getSession(String ip){
		LocalHost local = LocalHost.getInstance();
		SessionContext sc = local.getSessionContext(ip);
		return sc;
		
	}
	public SessionContext getSession(String sessionId, String ip){
		LocalHost local = LocalHost.getInstance();
		if(sessionId.length() > 0){
			SessionContext sc = local.getSessionContext(ip);
			if(sc != null){
				if(sessionId.equals(sc.getSessionId())){
					return sc;
				}else{
					//sessionId不一致，说明前一个session已失效，删除
					local.removeSessionByIp(ip);
				}
			}
			sc = local.newSessionContext();
			//String key = local.getNextSessionId(ip);
			sc.setSessionId(sessionId);
			local.putSessionContext(ip, sc);
			return sc;
			
		}
		//following not access.sessionId.length() always > 0
		SessionContext sc = local.newSessionContext();
		String key = local.getNextSessionId(ip);
		sc.setSessionId(key);
		local.putSessionContext(ip, sc);
		return sc;
	}
}
