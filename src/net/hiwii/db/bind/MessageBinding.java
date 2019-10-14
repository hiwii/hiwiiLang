package net.hiwii.db.bind;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import net.hiwii.msg.Message;
import net.hiwii.msg.RemoteMessage;
import net.hiwii.msg.TerminalMessage;

public class MessageBinding extends TupleBinding<Message> {

	@Override
	public Message entryToObject(TupleInput arg0) {
		char type = arg0.readChar();
		if(type == 't') {
			TerminalMessage msg = new TerminalMessage();
			msg.setInput(arg0.readBoolean());
			msg.setTime(arg0.readString());
			msg.setUserId(arg0.readString());
			msg.setContent(arg0.readString());
			return msg;
		}else {
			RemoteMessage msg = new RemoteMessage();
			msg.setInput(arg0.readBoolean());
			msg.setUserId(arg0.readString());
			msg.setContent(arg0.readString());
			return msg;
		}
	}

	@Override
	public void objectToEntry(Message arg0, TupleOutput arg1) {
		if(arg0 instanceof TerminalMessage) {
			TerminalMessage msg = (TerminalMessage) arg0;
			arg1.writeChar('t');//terminal message
			arg1.writeBoolean(msg.isInput());
			arg1.writeString(msg.getTime());
			arg1.writeString(msg.getUserId());
			arg1.writeString(msg.getContent());
		}else {
			RemoteMessage msg = (RemoteMessage) arg0;
			arg1.writeChar('r'); //remote message
			arg1.writeBoolean(msg.isInput());
			arg1.writeString(msg.getUserId());
			arg1.writeString(msg.getContent());
		}		
	}

}
