package net.hiwii.system.store;

/**
 * overflow page���ڴ�ų���128��key��value��
 * һ��overflow page�����64��cell���.page���64k��ÿ��cell���256�ֽ�
 * һ��overflow���ݿ���ռ�ö��cell�����cell������ʽ���ӡ�
 * part overflow cell�ṹ��
 * 1 byte length + 4 byte overflow[next page number] + 1 byte[cell number] + data
 * ���û��ռ����һ��overflow����[next page number] = 0
 * 
 * @author Administrator
 *
 */
public class OverflowPage extends Page {

}
