package com.csueb.osd.hemali;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class FileSystem {
	private static DiskSpace d = new DiskSpace(100);
	private DirectoryNode root;
	private DirectoryNode lastCreatedNode;
	private char mode;
	private UserData dataNode;
	private int dataPointer = 0;
	private int freeDirectories = 0;
	public FileSystem() {
		root = (DirectoryNode) d.getNextBlock();
		//root.setFreeBlockNo(BigInteger.);
		root.setBack(BigInteger.valueOf(0));
		root.setType('D');
		
		root.setName("");
		root.setLink(BigInteger.valueOf(0));
		d.write(root, 0);
		root.setFreeBlockNo(BigInteger.valueOf(d.getFreeBlockNumber()));
	}
	
	
	public int create(String type, String name){
		String[] paths = name.split("/");
		List<String> arrayList = new ArrayList<String>();
		for(String s : paths) {
			arrayList.add(s);
		}
		this.addNodeToPath(arrayList, root, null, type);
		System.out.println(root);
		return 0;
	}
	public int open(char mode, String name){
		this.mode = mode;
		String[] paths = name.split("/");
		List<String> arrayList = new ArrayList<String>();
		for(String s : paths) {
			arrayList.add(s);
		}
		this.lastCreatedNode =  this.findNodeByByName(arrayList, root);
		this.dataNode = (UserData) d.getDirectoryNodeByBlockNumber(this.lastCreatedNode.getLink().intValue());
		if(this.lastCreatedNode != null ) {
			System.out.println("FILE NAME ->" + this.lastCreatedNode.getName() + " <- opend in  mode ->" + mode);
		}
		return 0;
	}
	public void close(){
		System.out.println("FILE WITH NAME -> " + this.lastCreatedNode.getName() + " <- IS CLOSED");
		this.lastCreatedNode = null;
		this.dataNode = null;
		this.print();
	}
	
	public void print() {
		this.freeDirectories = 0;
		int userDataFileBytes = 0;
		System.out.println( " PRINTING THE FINAL STATS");
		for(int i=0; i< root.getDirectories().length; i++ ) {
			if(root.getDirectories()[i] != null) {
				System.out.println("********** STARTING THE NEXT DIRECTORY ************");
				System.out.println(root.getDirectories()[i].getName());
				if(root.getDirectories()[i].getType() == 'U') {
					if(d.getDirectoryNodeByBlockNumber(root.getDirectories()[i].getLink().intValue()) instanceof UserData){
						UserData dataNode =  (UserData) d.getDirectoryNodeByBlockNumber(root.getDirectories()[i].getLink().intValue());
						userDataFileBytes += dataNode.getData().length;
						while(dataNode.getFwd().intValue() != 0) {
							dataNode = (UserData) d.getDirectoryNodeByBlockNumber(dataNode.getFwd().intValue());
							userDataFileBytes += dataNode.getData().length;
						}
						System.out.println("File Size for the name "+   " -> " +root.getDirectories()[i].getName() + " is : "+ userDataFileBytes);
					}
				}else {
					this.printStats(root.getDirectories()[i], root.getDirectories()[i].getName());
				}
				
			}
			this.freeDirectories++;
		}
		System.out.println("********** End of  DIRECTORY Printing ************");
		System.out.println("Count of Free Directories ===== > " + this.freeDirectories);
		System.out.println("Count of Free Data Blocks ===== > " + d.countFreeSpace());
		
	}
	
	public void printStats(DirectoryNode node, String preFix) {
		int userDataFileBytes = 0;
		for(int i =0; i< node.getDirectories().length; i++) {
			if(node.getDirectories()[i] != null && node.getDirectories()[i].getType() == 'D') {
				preFix += " -> " +node.getDirectories()[i].getName();
				System.out.println(preFix);
				printStats(node.getDirectories()[i], preFix);
				
			} else if(node.getDirectories()[i] != null && node.getDirectories()[i].getType() == 'U') {
				System.out.println(preFix + " -> " +node.getDirectories()[i].getName());
				if(d.getDirectoryNodeByBlockNumber(node.getDirectories()[i].getLink().intValue()) instanceof UserData){
					UserData dataNode =  (UserData) d.getDirectoryNodeByBlockNumber(node.getDirectories()[i].getLink().intValue());
					userDataFileBytes += dataNode.getData().length;
					while(dataNode.getFwd().intValue() != 0) {
						dataNode = (UserData) d.getDirectoryNodeByBlockNumber(dataNode.getFwd().intValue());
						userDataFileBytes += dataNode.getData().length;
					}
					System.out.println("File Size for the name "+ preFix + " -> " +node.getDirectories()[i].getName() + " is : "+ userDataFileBytes);
				}
				
			} else {
				this.freeDirectories++;
			}
		}
	}
	
	
	public int read(int n){
		if(this.mode == 'O') {
			System.out.println("Reading in permitted only in I (Input) mode or Update Mode");
			return 0;
		}
		if(this.dataNode == null || this.lastCreatedNode == null) {
			System.out.println("FILE IS CLOSED OR NOT FOUND");
			return  0;
		}
		this.dataNode = (UserData) d.getDirectoryNodeByBlockNumber(this.lastCreatedNode.getLink().intValue());
		int totalBytesRead = 0;
		int bytesToRead = n;
		int bytesReadCounter = 0;
		boolean shouldReadFile = true;
		StringBuffer sb = new StringBuffer();
		
		while(shouldReadFile) {
			for(int i =this.dataPointer; bytesReadCounter < bytesToRead && i < this.dataNode.getData().length; i++) {
				sb.append(this.dataNode.getData()[i]);
				totalBytesRead++;
				bytesReadCounter++;
				this.dataPointer++;
				
			}
			if(totalBytesRead <= n && this.dataNode.getFwd().intValue() != 0) {
				bytesToRead = bytesToRead - this.dataNode.getData().length;
				this.dataNode =  (UserData) d.getDirectoryNodeByBlockNumber(this.dataNode.getFwd().intValue());
				
			} else {
				shouldReadFile = false;
			} 
		}
		System.out.println(sb.toString());
		if(totalBytesRead < n) {
			System.out.println("END OF THE FILE IS REACHED");
		}
		return 0;
	}
	public int write(int n, String data){
		if(this.mode == 'I') {
			System.out.println("Only  Read and Seek operation is permitted in Input mode");
			return 0;
		}
		if(this.lastCreatedNode == null) {
			System.out.println("FILE IS CLOSED OR NOT FOUND");
			return  0;
		}
		if(data.length() < n) {
			for(int i=0; data.length() < n; i++) {
				data =  data+ " ";
			}
		}
		if( n < data.length()) {
			StringBuffer sb = new StringBuffer();
			for(int i =0;i < n; i++) {
				sb.append(data.charAt(i));
			}
			data = sb.toString();
		}
		
		char[] charData  = data.toCharArray();
		int startIndex = 0;
		UserData userData = null;
		int backBlockNumber = 0;
		int freeBlockNumber = root.getFreeBlockNo().intValue();
		char[] fileData = null;
		List<Character> fileDataList =  null;
		for(int counter = 0; counter < charData.length; counter++) {
			if(freeBlockNumber < 0 ) {
				System.out.println("DISK IS FULL PROGRAM WIll EXIT");
				return 0;
			}
			if(startIndex == 0) {
				userData = new UserData();
				fileData = new  char[504];
				fileDataList = new ArrayList<>();
				userData.setBack(BigInteger.valueOf(backBlockNumber));
				if(counter == 0) {
					this.lastCreatedNode.setLink(BigInteger.valueOf(freeBlockNumber));
				}
			}
			fileDataList.add(charData[counter]);
			fileData[startIndex] = charData[counter];
			startIndex++;
			if(startIndex == 503 || (counter + 1) == charData.length) {
				fileData = new char[fileDataList.size()];
				for(int i=0;i < fileDataList.size(); i++) {
					fileData[i] = fileDataList.get(i);
				}
				userData.setData(fileData);
				d.getNextBlock();
				d.write(userData, freeBlockNumber);
				backBlockNumber = freeBlockNumber;
				root.setFreeBlockNo(BigInteger.valueOf(d.getFreeBlockNumber()));
				freeBlockNumber = root.getFreeBlockNo().intValue();
				userData.setFwd(root.getFreeBlockNo());
				startIndex = 0;
			}
		}
		// for the last user data block
		userData.setFwd(BigInteger.valueOf(0));
		this.lastCreatedNode.setSize(BigInteger.valueOf(userData.getData().length));
		System.out.println(root);
		return 0;
	}
	public int seek(String base, int offset){
		if(this.mode == 'O') {
			System.out.println("File is opened in Output mode only write operation is permitted");
			return  0;
		}
		if(this.dataNode == null || this.lastCreatedNode == null) {
			System.out.println("FILE IS CLOSED OR NOT FOUND");
			return  0;
		}
		if(base.equalsIgnoreCase("0")) {
			this.moveFileDataPointer(offset);
			
		}else if(base.equalsIgnoreCase("+1")) {
			while(this.dataNode.getFwd().intValue() != 0) {
				this.dataNode = (UserData) d.getDirectoryNodeByBlockNumber(this.dataNode.getFwd().intValue());
			}
			this.dataPointer = this.dataNode.getData().length;
			this.moveFileDataPointer(offset);
		}else if(base.equalsIgnoreCase("-1")) {
			while(this.dataNode.getBack().intValue() != 0) {
				this.dataNode = (UserData) d.getDirectoryNodeByBlockNumber(this.dataNode.getBack().intValue());
			}
			this.dataPointer = 0;
			this.moveFileDataPointer(offset);
		}
		return 0;
	}
	private int moveFileDataPointer(int offset) {
		int nextBlock = 0;
		for(int i = 0; i < Math.abs((offset/504)) + (Math.abs((offset%504)) + this.dataPointer)/504 ; i++) {
			nextBlock = this.dataNode.getFwd().intValue();
			if(offset < 0) {
				nextBlock = this.dataNode.getBack().intValue();
			}
			if(nextBlock == 0) {
				System.out.println("END of the File reached for the Given Input ");
				return 0;
			}
			this.dataNode =  (UserData) d.getDirectoryNodeByBlockNumber(nextBlock);
		}
		this.dataPointer = Math.abs(((offset%504) + this.dataPointer)% 504);
		System.out.println("File Data Pointer Location:" + this.dataPointer);
		if(this.dataNode.getData().length <=  this.dataPointer) {
			System.out.println("END of the File reached for the Given Input ");
			return 0;
		}
		return 0;
	}
	public int delete(String name){
		String[] paths = name.split("/");
		List<String> arrayList = new ArrayList<String>();
		for(String s : paths) {
			arrayList.add(s);
		}
		DirectoryNode node = this.findNodeByByName(arrayList, root);
		if(node!=null && node.getBack().intValue() > 0 ) {
			this.deleteRecrusively(node, (DirectoryNode) d.getDirectoryNodeByBlockNumber(node.getBack().intValue()));
		}
		
		return 0;
	}
	
	private void deleteRecrusively(DirectoryNode node, DirectoryNode previousNode) {
		// we need to delete all the directories
		if(node != null && node.getType() == 'D') {
			for(DirectoryNode currentNode: node.getDirectories()) {
				if(currentNode != null) {
					deleteRecrusively(currentNode, node);
					d.delete(currentNode.getLink().intValue());
					
				}
				
			}
			node.setDirectories(new DirectoryNode[31]);
		}
		// TODO: figure out how to delete user data file
		System.out.println(root);
	}
	
	private DirectoryNode findNodeByByName(List<String> paths, DirectoryNode parent) {
		boolean found = false;
		if(paths.size() == 0) {
			return parent;
		}
		for(DirectoryNode node : parent.getDirectories()) {
			if(node!=null &&node.getName().equalsIgnoreCase(paths.get(0))) {
				paths.remove(0);
				found = true;
			return findNodeByByName(paths, node);
			}
		}
		if(!found) {
			System.out.println("FILE PATH NOT FOUND");
		}
		return null;
	}
	
	private DirectoryNode addNodeToPath(List<String> paths, DirectoryNode currentNode, DirectoryNode prevNode, String type) {
		if(paths.size() <= 0) {
			return null;
		}
		
		boolean found  = false;
		DirectoryNode freeNode = (DirectoryNode) d.getNextBlock();
		freeNode.setBack(currentNode.getLink());
		freeNode.setName(paths.get(0));
		freeNode.setLink(root.getFreeBlockNo());
		freeNode.setType('D');
		currentNode.setForward(root.getFreeBlockNo());
		DirectoryNode[] directories = currentNode.getDirectories();
		if(paths.size() == 1) {
			freeNode.setType(type.toCharArray()[0]);
				for(int i=0; i< directories.length; i++) {
					if(directories[i] == null) {
						d.write(freeNode, freeNode.getLink().intValue());
						root.setFreeBlockNo(BigInteger.valueOf(d.getFreeBlockNumber()));
						directories[i] = freeNode;
						lastCreatedNode = freeNode;
						currentNode.setDirectories(directories);
						return null;
					}
				}
			return null;
		}else {
			// TODO: add it to existing directory then we need to delete that and replace with new one
			for(DirectoryNode d : currentNode.getDirectories()) {
				if(d != null && d.getName().equalsIgnoreCase(paths.get(0)) && ( d.getType() == 'D' || d.getType() == 'd' ) ) {
					found = true;
					paths.remove(0);
					addNodeToPath(paths, d, currentNode, type);
					break;
				}
			}
			for(int i=0; i< directories.length; i++) {
				if(directories[i] == null) {
					directories[i] = freeNode;
					currentNode.setDirectories(directories);
					break;
				}
			}
			d.write(freeNode, freeNode.getLink().intValue());
			root.setFreeBlockNo(BigInteger.valueOf(d.getFreeBlockNumber()));
			paths.remove(0);
			addNodeToPath(paths, freeNode, currentNode, type);
			
		}
		return null;
	}
}
