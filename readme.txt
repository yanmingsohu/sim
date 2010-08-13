
Java Web Tools

----------------------------------------------------------------------------
CatfoOD 2009-10-29 v0.31
edit: CatfoOD 2010-7-29


----------------------------------------------------------------------------

依赖项目:

	j2ee 1.4							http://java.sun.com/j2ee/overview.html
	struts2								http://struts.apache.org/
	struts1 				(可选)
	commons-logging-1.0.4				http://commons.apache.org/logging/
	ognl-2.6.1
	
	commons-dbcp-1.3		(可选)		http://commons.apache.org/dbcp/
	commons-pool-1.5.4		(可选)		http://commons.apache.org/pool/
	DBPool-5.0				(可选)		http://www.snaq.net/java/DBPool

----------------------------------------------------------------------------

package: jym.sim.base {

	HttpBase: [
		封装Servlet常用操作，通过继承HttpBase并实现get/post方法

		HttpBase相当于HttpServlet
		get/post相当于doGet/doPost方法

		http请求(get/post)中的参数自动用配置文件中的beanclass指定的类包装,
		请求中的参数名与beanclass指定的类的属性名一一对应
		
		get/post直接返回要转发的路径字符串，可以返回null
		
		*注意：不要直接覆盖doXxx方法，否则将得不到这些功能。
	]


	HtmlPack: [
		html文本包装器
	]


	XmlPack: [
		xml文本包装器
	]
}


package: jym.sim.orm {

	OrmTemplate: [
		实体对象映射类
		提供基本的增删改查
		*实体映射策略在程序中配置
	]
}


package: jym.sim.orm.page {

	*: [
		数据库查询分页策略
	]
	
	PageBean: [
		分页数据
	]
}


package: jym.sim.sql {

	Jdbctemplate: [
		jdbc模板类
	]
}


package: jym.sim.validator {

	annotation.* (validatorAnno) : [
		对象属性验证策略的注解类
	]
	
	verify.* (annoImpl) : [
		validatorAnno的算法实现类,与validatorAnno一一对应
	]
	
	Validator: [
		对象验证器,要验证对象的属性需用validatorAnno注释
	]
}


package: jym.sim.util {
	
	SafeidMapping: [
		在客户端显示安全的ID值
	]
	
	Tools: [
		一些经常使用的辅助函数
	]
	
	ResourceLoader: [
		本地资源读取类
		在Servlet中本地资源不能使用File直接读取
		需用Url引用,此类会遍历所有父类的ClassLoader
		直到获取到指定的资源或者失败
	]
}


package: jym.sim.jstags {
	*: [
		一些集成java script Tag的实现
	]
}


package: jym.sim.tags {
	*: [
		一些集成Tag的实现
	]
}


packae: jym.sim.json {
	*: [
		生成JSON的支持
	]
}


package: jym.sim.pool {
	*: [
		数据库连接池的通用封装
	]
}

----------------------------------------------------------------------------
备注：

应该把transformValue方法，提取出来做成接口，让用户自定义输入数据的过滤方式
过滤输出数据，并把过滤方式做成接口，让用户自定义，修改IPlot接口的定义，修改
fieldPlot方法的第三个参数，让他可以接受不止IWhere接口，即不只是能修改where
子句的拼装方式，还能对add update 和select的输出值过滤，为IWhere增加父类，
并让过滤器实现它