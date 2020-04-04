# 海微语言和海微系统

---
## 1.介绍
&emsp;&emsp;海微语言是介于人们日常使用的自然语言和计算机语言之间的一种语言。目标是建立一种适合于人机语言交互的语言。<br>
&emsp;&emsp;目前的计算机系统并没有基于语言交互设计，需要从一个全新的视角设计一种机器系统，能够在人与计算机、计算机与计算机之间进行语言交互。由于该系统以海微语言作为沟通方式，因此称系统为海微系统。
## ２.背景
&nbsp;&nbsp;&nbsp;&nbsp;在计算机使用方面，大部分计算机用户都是通过鼠标或触摸来完成和计算机的交互，如果需要输入一些信息给计算机，我们通常是通过一个输入框实现。通过鼠标或屏幕的点击，加上文字输入框，我们可以实现很多应用，比如购物、游戏、新闻、社交等。各种不同的应用满足了我们当前的生活需求。<br>
&nbsp;&nbsp;&nbsp;&nbsp;但是鼠标、触摸加输入框的方式只是一种图形交互方式，我们缺少一种真正的人机交互语言，通过人机交互语言，人和计算机能够像两个人交谈一样，进行语言的交互。<br>
&nbsp;&nbsp;&nbsp;&nbsp;造成人类不能和计算机顺利进行人机语言交互的原因主要有以下两点：<br>
&nbsp;&nbsp;&nbsp;&nbsp;(1)目前缺少一种人机交互语言。<br>
&nbsp;&nbsp;&nbsp;&nbsp;(2)目前的计算机不支持语言交互式应用。<br>
&nbsp;&nbsp;&nbsp;&nbsp;自然语言普通存在歧义、一词多义、语法复杂、习惯性用法多、不够严谨等多方面的问题。因为这些问题，当前情况下，自然语言并不适合作为一种人机交互语言。<br>
&nbsp;&nbsp;&nbsp;&nbsp;计算机语言涉及计算机原理，一般使用者较难掌握。且计算机语言大都是指令性语言，难以完成事实的陈述功能。因此计算机语言不能作为一种人机交互语言使用。<br>
&nbsp;&nbsp;&nbsp;&nbsp;基于以上原因，本文提出了一种全新的人机交互语言和适于进行人机交互的系统（以下简称本语言和本系统）。<br>
## ３.程序执行
该项目编写语言是java。下载源代码后，运行net.hiwii.obj.Console，出现语言交互界面。
## 4.语言示例
### 疑问
```
ask[3+4]
whether["abc" = "efg"]
```
### 分支和循环
```
if[true,ask[3]]
if[3>4,ask[5],ask[6]]
```
### number:运算和判断
加减乘除，大小关系
```
ask[2+3]
ask[5-3*4]
```
### 字符串：运算和判断
```
ask["abc"+"efg"]   //字符串相加
whether["abc" > "efg"]  //比较大小
```
### 程序
##### doProgram:
```
{ask[3],ask[4]}
```
##### skip
```
{ask[3],ask[4],skip,ask[5]}  //skip test
```
##### 循环
```
var[x, Integer]
x:=3
while[x>1, x:=x-1]
ask[x] //x=1
```
##### for循环
```
for[x:=1,x < 10, x:=x+1,ask[x]]
```
for循环的循环参数，执行体可以访问，但是不允许修改。而java中是可以修改的。
##### 变量
变量都是identifier，随runtime执行确定生命范围。
有三种类型，
###### variable：指向对象
```
var[x, Integer]
x:=3
ask[x]  //3
```
###### boolean：指向true/false
```
>boolean[boo]
>whether[boo]
Null
>boo::false
>whether[boo]
false
```
###### expression：指向表达式
```
express[exp, 3+4]
ask[exp]
```
#### 变量作用域

#### 流程控制
###### skip
###### break
###### return
###### throw[type]

### 定义：
###### action
```
define[act1, Action]
```
//对于action和state，只要定义了Id形式，则默认可以同时定义了函数和mapping形式，可以直接声明函数和mapping
```
declare[Action:act1, ask[4]]
```
###### Link
```
define[f(Integer), Link:Number]
undefine[Link:f(Integer)]
declare[Calculation:f(Integer x), x+1]   //ask[f(3)]
define[obj1, Object:create[Object]]
declare[Calculation:obj1.f(Integer x), x+2]  //ask[obj1.f(3)]
define[Product]   //ask[Product]
//define[prod, Object:create[Product]]
declare[Calculation:Product.f(Integer x),  x+3]    //ask[prod.f(3)]
define[f(String), Link:String]
declare[Calculation:f(String x), x+"xyz"]   //ask[f("abc")]

define[map1[Expression], Link:String]
```
###### State

###### 定义/Class


### 对象
define[obj1, Object:create[Object]]    //创建一个Object,并命名obj1

#### 异常


### declare
###### Action
###### Decision
###### Caculation

#### undeclare
不允许重复声明，如果要再声明，必须undelcare
#### delcared

### 对象描述
```
define[People]  //当父类是Object，可以省略。等同于：define[People, Object]
define[Police, People]
define[age, Link:Number]
define[name, Link:String]
```
###### 创建对象
```
define[小明, Object:create[Police]]
```
###### assign
```
小明#assign[age, 10]
ask[小明]   //Police{age:10}
```
###### turn
```
小明#turn[working, true]
```
###### Police.
```
ask[Police.that]
```
###### 再create一个Police
```
define[pol2, Object:create[Police]]
pol2#assign[age, 20]
pol2#turn[working, false]
```
###### 查询
```
ask[Police.all]
```
