package com.csueb.osd.hemali;

import java.util.Scanner;
public class FileSystemFrontEnd {
	
	public static void main(String[] args) {
		// add logic for initialization and various inputs
		System.out.println("Welcome to File System Project Please enter valid command of enter EXIT to Stop");
		Scanner scan = new Scanner(System.in);
		FileSystem fs = new FileSystem();
	        try {
	            while (scan.hasNextLine()){

	                String line = scan.nextLine().trim();
	                if(line.equalsIgnoreCase("exit")) {
	                	break;
	                }
	                String[] tokens = line.split(" ");
	                if(tokens[0].equalsIgnoreCase("create")) {
	                	fs.create(tokens[1], tokens[2]);
	                }
	                else if(tokens[0].equalsIgnoreCase("open")) {
	                	fs.open(tokens[1].toCharArray()[0], tokens[2]);
	                }
	                else if(tokens[0].equalsIgnoreCase("close")) {
	                	fs.close();
	                }
	                else if(tokens[0].equalsIgnoreCase("delete")) {
	                	fs.delete(tokens[1]);
	                }
	                else if(tokens[0].equalsIgnoreCase("read")) {
	                	fs.read(Integer.parseInt(tokens[1]));
	                }
	                else if(tokens[0].equalsIgnoreCase("write")) {
	                	String data = "";
	                	for(int i=2;i< tokens.length;i++) {
	                		data = data+tokens[i]+" ";
	                	}
	                	data = data.trim();
	                	fs.write(Integer.parseInt(tokens[1]), data);
	                }
	                else if(tokens[0].equalsIgnoreCase("seek")) {
	                	fs.seek(tokens[1], Integer.parseInt(tokens[2]));
	                }
	                else if(tokens[0].equalsIgnoreCase("print")) {
	                	fs.print();
	                }
	                
	            }

	        } finally {
	            scan.close();
	        }
	}

}
