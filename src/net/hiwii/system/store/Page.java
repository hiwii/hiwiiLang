package net.hiwii.system.store;

/**
 * btree file header������ļ���512
 * 0-3 4 bytes ��־���
 * 4   1 byte :page type;
 * 5-8 4 bytes pointer to btree root
 * u_int32_t       version;                           //���ݿ�ĵ�ǰ�汾��
 * u_int32_t       pagesize;                           //���ݿ�ҳ��С
 * u_int8_t       encrypt_alg;                        //�����㷨
 * u_int8_t       type;                             //ҳ����
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
