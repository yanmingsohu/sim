// CatfoOD 2010-10-16 ÏÂÎç08:52:20

package test;

import java.math.BigDecimal;

public class TestBean {
	
	private int a;
	private float b;
	private String c;
	private BigDecimal d;
	private boolean e;
	private char f;
	
	@SuppressWarnings("unused")
	private String pc;
	
	
	public boolean getE() {
		return e;
	}
	public void setE(boolean e) {
		this.e = e;
	}
	public int getA() {
		return a;
	}
	public void setA(int a) {
		this.a = a;
	}
	public float getB() {
		return b;
	}
	public void setB(float b) {
		this.b = b;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	public BigDecimal getD() {
		return d;
	}
	public void setD(BigDecimal d) {
		this.d = d;
	}
	public char getF() {
		return f;
	}
	public void setF(char f) {
		this.f = f;
	}
}