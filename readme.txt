
Java Web Tools

----------------------------------------------------------------------------

    v0.51 update 2012-3-1                           CatfoOD 2009-10-29 

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

2012-03-01	sql模板
2011-08-23	可以把普通sql语句转换为使用PreparedStatement调用的方式.

----------------------------------------------------------------------------

format.html
	把源代码格式化为网页
	并加入格式，渲染颜色


jym.sim.base
	HttpBase
		封装Servlet常用操作，通过继承HttpBase并实现get/post方法

		HttpBase相当于HttpServlet
		get/post相当于doGet/doPost方法

		http请求(get/post)中的参数自动用配置文件中的beanclass指定的类包装,
		请求中的参数名与beanclass指定的类的属性名一一对应
		
		get/post直接返回要转发的路径字符串，可以返回null
		*注意：不要直接覆盖doXxx方法，否则将得不到这些功能。

	HtmlPack
		html文本包装器 
	XmlPack
		xml文本包装器 


jym.sim.orm
	OrmTemplate
		实体对象映射类
		提供基本的增删改查
		*实体映射策略在程序中配置


jym.sim.orm.page
	数据库查询分页策略, orm支持自动分页
	

jym.sim.sql
	Jdbctemplate 	
		jdbc模板类
	Orm	
		数据库与对象映射
	complie
		把sql编译为class(功能不全)
	reader
		读取sql模板,支持el表达式和控制命令


jym.sim.validator
	annotation.* (validatorAnno)
		对象属性验证策略的注解类 
	
	verify.* (annoImpl)
		validatorAnno的算法实现类,与validatorAnno一一对应
	
	Validator
		对象验证器,要验证对象的属性需用validatorAnno注释 


jym.sim.util
	SafeidMapping
		在客户端显示安全的ID值 ]
	
	Tools
		一些经常使用的辅助函数, 直接打印ResultSet等
	
	ResourceLoader	: [ 本地资源读取类
		在Servlet中本地资源不能使用File直接读取
		需用Url引用,此类会遍历所有父类的ClassLoader
		直到获取到指定的资源或者失败 ]
		
	SqlFormat
		格式化混乱的sql语句
	UsedTime
		方便的计算运行时间


jym.sim.jstags
	一些集成java script Tag的实现, 如调用一个函数


jym.sim.tags
	一些集成htmlTag的实现, 如打印一个表格
	用于在java代码中生成html
	该模块需要增强...坑 


jym.sim.json
	 生成JSON的支持, 木有解析, 
	 偶从来不再java端解析json, 就不应该往后台传json


jym.sim.pool
	数据库连接池的通用封装, 使用一种配置, 无缝的在多种连接池之间切换


jym.sim.filter
	过滤器，提供从页面到数据库过滤器
	和从数据库到页面过滤器; 
	过滤器的概念是广义的, IFilter接口在多个模块被使用


jym.sim.sql.compile
	 编译sql文件为class,并执行文件中的sql,需要jdk支持
	因为不是所有环境都安装jdk所以该模块已经被废弃
	保留的原因是,有一些动态编译技术的代码值得参考


jym.sim.java
	Java文件动态生成工具, 
	**该模块未完成


jym.sim.parser
	解析器基础框架

	expr	
		表达式解析, 现在只支持数字类型的数学表达式和布尔表达式
		支持动态变量
	cmd	
		命令解析, 为模板提供进一步的控制力
	el	
		el模板解析
		
		
jym.sim.test
	各种测试和demo
