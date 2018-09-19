package com.csueb.osd.hemali;

import java.math.BigInteger;

public class UserData {
	private BigInteger back;
	private BigInteger fwd;
	private  char[] data;
	public BigInteger getBack() {
		return back;
	}
	public void setBack(BigInteger back) {
		this.back = back;
	}
	public BigInteger getFwd() {
		return fwd;
	}
	public void setFwd(BigInteger fwd) {
		this.fwd = fwd;
	}
	public char[] getData() {
		return data;
	}
	public void setData(char[] data) {
		this.data = data;
	}
}
