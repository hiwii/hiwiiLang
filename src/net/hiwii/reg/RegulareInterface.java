package net.hiwii.reg;

public interface RegulareInterface {
	/**
	 * �ַ�����ȫƥ�䡣
	 * @param str
	 * @return
	 */
	public boolean matches(String str);
	/**
	 * �������ַ������Թ���ƥ���ַ�������ƥ���ַ��Ŀ�ʼλ��
	 * @param str
	 * @param pos
	 * @param forward
	 * @return
	 */
	public int locate(String str, int pos, boolean forward);
	/**
	 * cover��ʾ��position��ʼ��ǰ��������ҵ�ƥ������ַ�����
	 * ���ַ��������մ�
	 * @param str
	 * @param pos
	 * @param forward
	 * @return
	 */
	public boolean guide(String str, int pos, boolean forward);
	/**
	 * ����ƥ���ַ�����
	 * ���cover=false������0
	 */
	public int count(String str, int pos, boolean forward, boolean greedy);

}
