package net.hiwii.reg;

public interface RegulareInterface {
	/**
	 * 字符串完全匹配。
	 * @param str
	 * @return
	 */
	public boolean matches(String str);
	/**
	 * 用于在字符串中略过不匹配字符，返回匹配字符的开始位置
	 * @param str
	 * @param pos
	 * @param forward
	 * @return
	 */
	public int locate(String str, int pos, boolean forward);
	/**
	 * cover表示从position开始向前或向后能找到匹配的子字符串。
	 * 子字符串包括空串
	 * @param str
	 * @param pos
	 * @param forward
	 * @return
	 */
	public boolean guide(String str, int pos, boolean forward);
	/**
	 * 返回匹配字符数。
	 * 如果cover=false，返回0
	 */
	public int count(String str, int pos, boolean forward, boolean greedy);

}
