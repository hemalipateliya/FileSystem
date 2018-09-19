package com.csueb.osd.hemali;

import java.math.BigInteger;

public class DirectoryNode {
	private BigInteger back = BigInteger.valueOf(0);
	private BigInteger forward = BigInteger.valueOf(0);
	private BigInteger link;
	private char type;
	private String name;	
	private BigInteger freeBlockNo;
	private char[] filler = new char[4];
	private BigInteger size;
	private DirectoryNode[] directories = new DirectoryNode[31];
	
	public BigInteger getBack() {
		return back;
	}
	public void setBack(BigInteger back) {
		this.back = back;
	}
	public BigInteger getForward() {
		return forward;
	}
	public void setForward(BigInteger forward) {
		this.forward = forward;
	}
	public BigInteger getLink() {
		return link;
	}
	public void setLink(BigInteger link) {
		this.link = link;
	}
	public char getType() {
		return type;
	}
	public void setType(char type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigInteger getFreeBlockNo() {
		return freeBlockNo;
	}
	public void setFreeBlockNo(BigInteger freeBlockNo) {
		this.freeBlockNo = freeBlockNo;
	}
	public char[] getFiller() {
		return filler;
	}
	public void setFiller(char[] filler) {
		this.filler = filler;
	}
	public BigInteger getSize() {
		return size;
	}
	public void setSize(BigInteger size) {
		this.size = size;
	}
	public DirectoryNode[] getDirectories() {
		return directories;
	}
	public void setDirectories(DirectoryNode[] directories) {
		this.directories = directories;
	}
	
}
