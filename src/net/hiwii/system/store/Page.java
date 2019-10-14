package net.hiwii.system.store;

/**
 * btree file header存放在文件首512
 * 0-3 4 bytes 日志序号
 * 4   1 byte :page type;
 * 5-8 4 bytes pointer to btree root
 * u_int32_t       version;                           //数据库的当前版本号
 * u_int32_t       pagesize;                           //数据库页大小
 * u_int8_t       encrypt_alg;                        //加密算法
 * u_int8_t       type;                             //页类型
 * 
 * page type:00---leaf node, 01---internal node, 80---root page
 * @author Administrator
 *
 */
public class Page {
	private byte[] data;
	private byte type;//1:leaf,2:
	private long number;//page number
	private boolean dirty;
	
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public BTreeNode getNode(){
		return null;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

}
