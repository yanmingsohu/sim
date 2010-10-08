
Java Web Tools

----------------------------------------------------------------------------
CatfoOD 2009-10-29 


v0.34
edit: CatfoOD 2010-9-12


----------------------------------------------------------------------------

依赖项目:

	javaSE 1.5				(或更高)
	j2ee 1.4							http://java.sun.com/j2ee/overview.html
	struts2								http://struts.apache.org/
	commons-logging-1.0.4				http://commons.apache.org/logging/
	ognl-2.6.1
	tools.jar				(可选)		http://java.sun.com [JDK中工具]
	
	commons-dbcp-1.3		(可选)		http://commons.apache.org/dbcp/
	commons-pool-1.5.4		(可选)		http://commons.apache.org/pool/
	DBPool-5.0				(可选)		http://www.snaq.net/java/DBPool

----------------------------------------------------------------------------


file: format.html {
	把源代码格式化为网页
	并加入格式，渲染颜色
}


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
		Demo: test.TestDBPool
	]
}


package: jym.sim.filter {
	*: [
		过滤器，提供从页面到数据库过滤器
		和从数据库到页面过滤器
	]
}

package: jym.sim.sql.compile {
	*: [
		编译sql文件为class,并执行文件中的sql
		Demo: test.TestSqlCompiler
	]
}

package: jym.sim.java {
	*: [
		Java文件动态生成工具
	]
}