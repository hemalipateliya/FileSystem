package com.csueb.osd.hemali;

import java.util.LinkedList;


public class DiskSpace {

   // private UserData[] blocks;
    private LinkedList<Object> freeBlocks;
    private LinkedList<Object> blocks;
    private boolean[] freeBlockArray = new boolean[100]; 
    
    
    public DiskSpace(int size) {
        //blocks = new UserData[size];
    	blocks = new LinkedList<Object>();
        freeBlocks = new LinkedList<Object>();
        
        for (int i = 0; i < size; i++) {
        	DirectoryNode  node = new DirectoryNode();
        	freeBlocks.add(new DirectoryNode());
        	freeBlockArray[i] = true;
        }
        freeBlockArray[0] = false;

    }
    
    public void read(int size, UserData data) {
    	
    }
    
    public void write(Object s, int link) {
    	freeBlockArray[link] = false;
    	blocks.add(link, s);
    		
    }
    
    public void delete(int link) {
    	blocks.remove(link);
    	freeBlockArray[link] = true;
    	freeBlocks.add(new DirectoryNode());
    	System.out.println(blocks.size() + "BLOCK SIZE");
    	System.out.println(freeBlocks.size() + " freeBlocks BLOCK SIZE");
    }
    
    public Object getNextBlock () {
    	return freeBlocks.poll();
    }
    public Object getDirectoryNodeByBlockNumber(int link) {
    	return blocks.get(link);
    }
    
    public int getFreeBlockNumber() {
    	for(int i=0; i< freeBlockArray.length; i++) {
    		if(freeBlockArray[i]) {
    			return i;
    		}
    	}
    	return -1;
    }

     
    public int countFreeSpace() {

        return freeBlocks.size();

    }


}
