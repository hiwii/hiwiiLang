package net.hiwii.system.info;

import net.hiwii.cognition.Expression;

/**
 * PropertyValue用于保存赋值value
 * PropertyValue存在于不同级的context中。
 * 赋值和查询赋值前，都需要获得属性信息(PropertyInfo)
 * 为加快赋值的查询速度，属性的定义包括一个id，用于赋值查询。
 * 这个id包括from定义的签名+context赋予的序号+contextId
 * id中加入contextId是保证全局唯一
 * @author ha-wangzhenhai
 *
 */
public class PropertyValueOld extends Expression {

}
