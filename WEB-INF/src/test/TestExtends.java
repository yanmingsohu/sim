package test;


/**
 * 
 * 切勿调入Java继承的陷阱<br>
 * 
 * CatfoOD 2011-8-2 上午09:53:27 yanming-sohu@sohu.com/@qq.com<br>
 * 
 */
public class TestExtends {

	public static void main(String[] args) {
		B b = new B();
		/* 首先会打印"b: 0"然后才是"b: 3"*/
		b.rr();
	}

	
	static class A {
		private int a = 1;
		
		A() {
			/* 在构造函数中调用的方法可能被重写 */
			rr();
		}
		
		/** 该方法未被调用 */
		void rr() {
			System.out.println("a: " + a);
		}
	}
	
	static class B extends A {
		private int a = 2;
		
		B() {
			a = 3;
		}
		/**
		 * 由于该方法重写了A的rr方法所以当B被创建时A的构造函数先被
		 * 调用,但是此时B尚未初始但是B.rr()却被调用了,于是在rr()中
		 * 所有使用的属性都是默认值(0或null);同时A自己的属性也没有
		 * 被正确的初始化.<br>
		 * 所以在父类的构造函数中调用的方法最好是private或final(这
		 * 样就不能被子类重写), 或者很明确的利用这种情况来构造程序.
		 */
		void rr() {
			System.out.println("b: " + a);
		}
	}
}
