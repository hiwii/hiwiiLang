package net.hiwii.obj.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.collection.EntityList;
import net.hiwii.expr.StringExpression;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.view.Entity;

public class FileObject extends Entity {
	private File file;
//	private ByteBuffer buffer;
	public FileObject(String path){
		File f = new File(path);
//		buffer = ByteBuffer.allocateDirect(4096);
		this.file = f;
	}
	public FileObject(File file){
		this.file = file;
//		buffer = ByteBuffer.allocateDirect(4096);
	}
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
//	public ByteBuffer getBuffer() {
//		return buffer;
//	}
//	public void setBuffer(ByteBuffer buffer) {
//		this.buffer = buffer;
//	}
	public void create() throws IOException{
		if(!file.exists()){
			file.createNewFile();
		}
	}
	
	public long size() throws IOException{
		FileInputStream fos = new FileInputStream(file);
		FileChannel fc = fos.getChannel();
		return fc.size();		
	}
	
	public List<Entity> lines(){
		try {
			//循环，每次读一行
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			List<Entity> list = new ArrayList<Entity>();

			while ((line = reader.readLine()) != null) {
				StringExpression se = new StringExpression(line);
				list.add(se);
			}
			reader.close();
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<Entity>();
		}
	}
	public String readLine(int pos) throws IOException{
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel fc = raf.getChannel();
		MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size());
		Charset charset = Charset.forName("iso-8859-1");  
		CharsetDecoder decoder = charset.newDecoder();  
		CharBuffer charBuffer = decoder.decode(byteBuffer);  
//		Scanner sc = new Scanner(charBuffer).useDelimiter(System.getProperty("line.separator"));
		charBuffer.position(pos);
		int end = pos;
		while(true){
			try {
				char ch = charBuffer.get();
				if(ch == '\n' || ch == '\r'){
//				end = charBuffer.position();
					break;
				}
				end ++;
			} catch (Exception e) {
				break;
			}
		}
		charBuffer.clear();
//		charBuffer.flip();
		charBuffer.position(pos);
		try {
			if(end > pos){
				char[] dst = new char[end - pos];
				CharBuffer cb = charBuffer.get(dst, 0, end - pos );
				return new String(dst);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return "";
	}
	public int read(byte[] dst, long position){
//		buffer.clear();
//		buffer.put(dst);
		ByteBuffer buffer = ByteBuffer.wrap(dst);
//		buffer.get(dst);
		return read(buffer, position);
	}
	
	public int write(byte[] dst, long position){
//		buffer.clear();
//		buffer.put(dst);
		ByteBuffer buffer = ByteBuffer.wrap(dst);
		return write(buffer, position);
	}
	
	public int read(ByteBuffer dst, long position){
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			FileChannel fc = raf.getChannel();
			int ret = fc.read(dst, position);
			raf.close();
//			buffer.flip();
			return ret;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public int write(ByteBuffer dst, long position){
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			FileChannel fc = raf.getChannel();
			int ret = fc.write(dst, position);
			raf.close();
			return ret;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	public boolean exists(){
		return file.exists();
	}
	@Override
	public String getGene() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Entity doIdentifierCalculation(String name) {
		if(name.equals("size")){
			try {
				return new IntegerNumber(String.valueOf(size()));
			} catch (IOException e) {
				return new HiwiiException();
			}
		}else if(name.equals("lines")){
			List<Entity> ret = lines();
			EntityList el = new EntityList();
			el.setItems(ret);
			return el;
		}else if(name.equals("content")){
			try {
				byte[] bf = new byte[(int)size()];
				int get = read(bf, 0);
				String read = new String(bf, "UTF-8");
				return new StringExpression(read);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new HiwiiException();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new HiwiiException();
			}
		}
		return super.doIdentifierCalculation(name);
	}
	@Override
	public Expression doIdentifierAction(String name) {
		if(name.equals("create")){
			try {
				create();
				return null;
			} catch (IOException e) {
				return new HiwiiException();
			}
		}
		return super.doIdentifierAction(name);
	}
	@Override
	public Entity doFunctionCalculation(String name, List<Entity> args) {
		if(name.equals("read")){
			if(args.size() != 2){
				return new HiwiiException();
			}
			Entity ent = args.get(0);
			if(args.get(0) instanceof IntegerNumber && args.get(1) instanceof IntegerNumber){
				IntegerNumber pos = (IntegerNumber) args.get(0);
				IntegerNumber len = (IntegerNumber) args.get(1);
//				try {
//					write(se.getValue().getBytes(), size());
//					return null;
//				} catch (IOException e) {
//					return new HiwiiException();
//				}
			}
		}else if(name.equals("readLine")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			try {
				if(args.get(0) instanceof IntegerNumber){
					IntegerNumber in = (IntegerNumber) args.get(0);
					int pos = Integer.parseInt(in.getValue());
					String ret = readLine(pos);
					return new StringExpression(ret);
				}
			} catch (NumberFormatException e) {
				return new HiwiiException();
			} catch (IOException e) {
				return new HiwiiException();
			}
			return new HiwiiException();
		}
		return super.doFunctionCalculation(name, args);
	}
	@Override
	public Expression doFunctionAction(String name, List<Entity> args) {
		if(name.equals("write")){
			if(args.size() != 1){
				return new HiwiiException();
			}
			Entity ent = args.get(0);
			if(ent instanceof StringExpression){
				StringExpression se = (StringExpression) ent;
				try {
					write(se.getValue().getBytes(), size());
					return null;
				} catch (IOException e) {
					return new HiwiiException();
				}
			}
		}
		return super.doFunctionAction(name, args);
	}
	@Override
	public Expression doIdentifierDecision(String name) {
		if(name.equals("exist")){
			JudgmentResult jr = new JudgmentResult();
			if(exists()){
				jr.setResult(true);
			}else{
				jr.setResult(false);
			}
			return jr;
		}
		return super.doIdentifierDecision(name);
	}

}
