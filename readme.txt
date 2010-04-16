simstruts 简单Servlet框架
-------------------------
CatfoOD 2009-10-29 v0.1

HttpBase: {

	封装Servlet常用操作，通过继承HttpBase并实现get/post方法

	HttpBase相当于HttpServlet
	get/post相当于doGet/doPost方法

	http请求(get/post)中的参数自动用配置文件中的beanclass指定的类包装,
	请求中的参数名与beanclass指定的类的属性名一一对应
	
	get/post直接返回要转发的路径字符串，可以返回null
	
	*注意：不要直接覆盖doXxx方法，否则将得不到这些功能。
	
}


HtmlPack: {
	
	html文本包装器

}


XmlPack: {

	xml文本包装器
	
}