package net.hiwii.system.store;

/**
 * overflow page用于存放超出128的key或value：
 * 一个overflow page由最多64个cell组成.page最大64k，每个cell最多256字节
 * 一个overflow数据可以占用多个cell。多个cell采用链式连接。
 * part overflow cell结构：
 * 1 byte length + 4 byte overflow[next page number] + 1 byte[cell number] + data
 * 如果没有占用下一个overflow，则[next page number] = 0
 * 
 * @author Administrator
 *
 */
public class OverflowPage extends Page {

}
