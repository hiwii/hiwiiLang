package net.hiwii.term;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.sleepycat.je.DatabaseException;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.BreakReturn;
import net.hiwii.cognition.result.SkipReturn;
import net.hiwii.db.HiwiiDB;
import net.hiwii.def.Definition;
import net.hiwii.message.HiwiiException;
import net.hiwii.msg.TerminalMessage;
import net.hiwii.prop.Property;
import net.hiwii.system.LocalHost;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.EntityUtil;
import net.hiwii.system.util.StringUtil;
import net.hiwii.user.User;
import net.hiwii.view.Entity;

/**
 * Terminal是作为用户和系统的标准输入和输出
 * Terminal和Host等共同完成Message交互
 * @author ha-wangzhenhai
 *
 */
public class CommandTerminal extends Terminal{
	private BufferedReader _stdin;
	PrintWriter _stdout;
	
	public CommandTerminal(){
		_stdin = new BufferedReader(new InputStreamReader(System.in));
		_stdout = new PrintWriter(System.out, true);
		
//		_stdout.println("input command!");
	}

	public String getCommand() throws IOException {
		_stdout.print("> ");
		_stdout.flush();
		
//		System.out.println("");
		String line = _stdin.readLine().trim();
		return line;
	}
	
	public String readString() throws IOException {
		String line = _stdin.readLine().trim();
		return line;
	}
	
	public void outputLine(String str){
//		_stdout.print("> ");
		_stdout.print( str + "\n");
		_stdout.flush();
	}
	
	public void outputString(String str){
		_stdout.print( str);
		_stdout.flush();
	}
	
	public void doCommand() throws ApplicationException{
		try {
			while (true) {
				String comm = getCommand();
				if(comm.length() == 0){
					String in = getSession().getInput();
					if(in == null || in.length() == 0){
						continue;
					}
				}
				//				if(comm.charAt(comm.length() - 1) != ';'){
				//					_stdout.printf("bad command!\n");
				//				}else{
				if (comm.substring(0, comm.length()).equals("quit")){
//					break;
					LocalHost.getInstance().exit();
				}else{
					doRequest(comm);
				}
				//				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Terminal作为本机的一个控制设备，与host不同。host请求通过Message对象实现请求
	 * 如果输入字符串以","或";"结尾，则把输入字符串累加入会话缓冲区中，一起执行。
	 */
	public void doRequest(String comm){
		String command = "";
		String in = getSession().getInput();
		if(comm.length() > 0){
			char end = comm.charAt(comm.length() - 1);
			if(in == null){
				getSession().setInput(comm);
			}else{
				getSession().setInput(in + " " + comm);
			}
			if(end == ';' || end == ','){
				return;
			}else{
				command = getSession().getInput();
				getSession().setInput(null);
			}
		}else{
			command = getSession().getInput();
		}

		TerminalMessage msg = new TerminalMessage();
		msg.setInput(true);
		msg.setTime(StringUtil.getTimeNow());
		msg.setContent(command);
		User user = getSession().getUser();
		if(user != null) {
			msg.setUserId(user.getUserid());;
		}else {
			msg.setUserId("anonymous");
		}
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();
		
		try {
			db.saveMessage(msg, null);
		} catch (DatabaseException e) {
			this.outputLine("Exception!");
		} catch (IOException e) {
			this.outputLine("Exception!");
		} catch (ApplicationException e) {
			this.outputLine("Exception!");
		} catch (Exception e) {
			e.printStackTrace();
			this.outputLine("Exception!");
		}
		
		Expression exp = StringUtil.parseString(command);
//		Entity ret = se.doIdentifierCalculation("toExpression");
		if(exp instanceof HiwiiException){
			this.outputLine("parsing error!");
			getSession().setInput(null);
			return;
		}
//		Expression exp = (Expression) ret;
//		Expression res = getSession().getContext().doAction(exp);
		Expression res = doAction(exp);
		if(res instanceof HiwiiException){
			this.outputLine(res.toString());
			getSession().setInput(null);
		}
		//skip,break,return,decide[],return(),throw[]是程序元素，只能用于runtimeContext，不能作为返回值
		if(res instanceof SkipReturn){
			this.outputLine("Exception happened!");
		}
		if(res instanceof BreakReturn){
			this.outputLine("Exception happened!");
		}
	}
	
	@Override
	public String doResponse(String out){
		TerminalMessage msg = new TerminalMessage();
		msg.setInput(false);
		msg.setTime(StringUtil.getTimeNow());
		msg.setContent(out);
		HiwiiDB db = LocalHost.getInstance().getHiwiiDB();

		try {
			db.saveMessage(msg, null);
		} catch (DatabaseException e) {
			doResponse("Exception!");
		} catch (IOException e) {
			doResponse("Exception!");
		} catch (ApplicationException e) {
			doResponse("Exception!");
		} catch (Exception e) {
			doResponse("Exception!");
		}
		outputLine(out);
		return null;
	}
//	public void echo(Entity ret, RuntimePool pool){
//		String exp = ret.toString();
//		this.outputLine(exp);
//	}

	@Override
	public void doResponse(Entity res){
		if(res instanceof Definition){
			outputLine(res.toString());
		}else if(res instanceof Property){
//			Property prop = (Property) res;
//			outputLine(prop..toString());
		}else{
			outputLine(res.toString());
		}
	}
		
	

	@Override
	public String getGene() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "terminal";
	}

	@Override
	public void doQuestion(String quest) {
		doResponse(quest);
		try {
			getCommand();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
