package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;

public class MyTest3 {
	@Test
	public void testFile(){
		try {
			File f = new File("test.dat");
			if(!f.exists()){
				f.createNewFile();
			}
			String str1 = "abcde";
			String str2 = "fghij";
			RandomAccessFile fos = new RandomAccessFile(f, "rw");
			FileChannel fc = fos.getChannel();
			ByteBuffer bf1 = ByteBuffer.wrap(str1.getBytes());
//			ByteBuffer bf2 = ByteBuffer.wrap(str2.getBytes());
			int ret = fc.write(bf1, 0);
//			ret = fc.write(bf2, 5);
			fc.close();
			fos.close();
			
			RandomAccessFile fos1 = new RandomAccessFile(f, "rw");
			FileChannel fc1 = fos1.getChannel();
			ByteBuffer bf2 = ByteBuffer.wrap(str2.getBytes());
			ret = fc1.write(bf2, 5);
			fc1.close();
			fos1.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
