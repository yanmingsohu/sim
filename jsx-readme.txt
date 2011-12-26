Java Script 辅助库

自动兼容FireFox

// ---------------------- API目录 ----------------------

	// 等待id指定的标签加载完毕,执行alertfunc方法
waitTag(alertfunc, id)

	// 等待document.body加载完毕,执行alertfunc方法
waitBody(alertfunc)

	// divid放在窗口中间
moveCenter(divid)

	// 同document.getElementById(id)
getByid(id)

	// 在文档的末尾显示msg消息,一般用来显示调试信息
showError(msg)

	// 在obj对象后面插入dom对象
insertDom(obj, dom)

	// 取得formid指定的form对象中表单值的格式化字符串
getFormData(formid)

	// 创建request对象(ajax)
creatHttpRequest()

	// 包含一个js脚本,该包含的脚本需在body载入后执行
include(filename)

	// 编码uri,对中文字符不进行转换
encodeUri(uri)

	// 没什么用....
getDiv(divid)

	// 用动画显示一个div
showDiv(divid, after)

	// 用动画隐藏一个div
hideDiv(divid, after)

	// 如果div已经显示返回true
divDisplay(divid)

	// 让表格动画化
changeTableColor(tableid, fcolor, scolor, mousecolor)

	// 表格行鼠标点击事件封装
tableRowMouseOverListener(tableid, func)

	// 鼠标在obj对象上停留,颜色变为color
onMouseOverChangeColor(obj, color)

	// obj背景颜色用动画方法由scolor变为ecolor
transitionColor(obj, scolor, ecolor)

	// 直接改变obj的背景色
changeColor(obj, color)

	// 取得css颜色格式的int值
getColorInt(csscolor)

	// 把int转换为css颜色
int2color(int)

	// 直接设置obj的水平坐标(绝对定位)
setX(obj, x)

	// 直接设置obj的垂直坐标(绝对定位)
setY(obj, y)

	// 计算指定的标记对象到文档顶端的像素
getTop(tag)
	
	// 计算指定的标记对象到文档左端的像素
getLeft(tag)

	// 动画化水平移动obj
movex(obj, startx, finishx, after)

	// 动画化垂直移动obj
movey(obj, starty, finishy, after)

	// 如果是ie返回真
isie()

	// 如果是opera返回true
isopera()

	// 如果是火狐浏览器
isff()

	// 动画化设置obj的透明度
setOpacity(obj, opa) [opa(0-100)]

	// 一个动画函数,millise是从start到end的时间每次执行func
anim(func, start, end, millise)

	// 为target设置右键菜单,menu指定菜单对象
setMenu(menu, target)

	// 取得标签对象的背景色
getBGColor(obj)

	// 取得html对象的style属性,该方法可以取得css中定义的值
getStyle(obj)

	// 为tag绑定onchange事件
onchange(tag, handle)

	// 等待res完成,并执行方法
waitRes(res, whenFinish)

	// 删除指定标签上所有与脚本交叉关联的对象
removeAllCrossLink(tag)

	// 当dom对象从文档中删除后,立即删除与脚本的交叉引用
onremove(tag)

	// 同anim的参数, 动画效果更圆滑
conic(func, start, end, millise, after)

	// 为事件处理提供通用的栈
eventStack(obj, eventName, newEvent)

	// 生成圆角矩形, 返回的对象可以对它操作
create_round_horn(_parent)

	// 生成样式编辑器, 返回的对象可以对它操作
createCssDialog(_parent, _initValue, _objArrs)
	show()		显示
	close()		关闭并释放资源
	objArrays	标签数组是_objArrs参数
	listener()	加入事件监听器
	addTag()	插入新的标签
	
// ---------------------- class目录 ----------------------

LockObj(obj)
	lock()
	check()
	free()
	
DivPack(divid, touchid)
	setX()
	setY()
	getX()
	getY()
	getDiv()
	setInertia(bool)
	event(name, handle)
	
Parabola(x1, x2, y)
	get(x)
	
Dialog(w,h)
	show()
	close()
	setHtml(html)
	getContentDiv()
	
ajax()
	post(url)
	get(url)
	send(rul)
	abort()
	setAsync()
	setRequestHeader()
	setOkListener(h)
	setTextListener(h)
	setXmlListener(h)
	setJSonListener(h)
	setErrorListener(h)
	
ajaxform(form)
	setTextListener(h)
	setXmlListener(h)
	setJSonListener(h)
	setErrorListener(h)
	
selector()
	tagname(name)
	clazz(name)
	id(id)
	attr(attrName, value)
	todo(function)
	getTags()
	node(path)
	hasattr(attrName)
	
// ---------------------- 浏览器信息 ----------------------

navigator.userAgent:

	google:		Mozilla/5.0 (Windows; U; Windows NT 5.2; en-US) AppleWebKit/533.4 (KHTML, like Gecko) Chrome/5.0.375.55 Safari/533.4
	ie8:		Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727)
	ie6:		Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)
	Opera:		Opera/9.80 (Windows NT 5.2; U; Edition IBIS; zh-cn) Presto/2.5.24 Version/10.53
	FireFox:	Mozilla/5.0 (Windows; U; Windows NT 5.2; zh-CN; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13
	Safari:		Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) Version/5.0.3 Safari/533.19.4

navigator.appName:

	google:		Netscape
	ie8:		Microsoft Internet Explorer
	ie6:		Microsoft Internet Explorer
	Opera:		Opera
	FireFox:	Netscape
	Safari:		Netscape
		