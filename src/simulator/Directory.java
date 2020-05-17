/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

import java.util.*;
import java.io.*;

/**
 *
 * @author Kshitij
 */
public class Directory implements Serializable
{
	public static int MAX_DEPTH = 30;
	
	private Bucket[] directory;
	private int depth;			/* max depth is 30 since depth=31 overflows an int data type */
	
	private IntegerHasher h;
	
	/**
	 * Constructs a Directory for Extendible Hashing with a starting length of 1 and depth of 0.
	 */
	public Directory(int bfr)
	{
		this.directory = new Bucket[] {new Bucket(bfr, 1, 0), new Bucket(bfr, 1, 1)};
		this.depth = 1;
		this.h = IntegerHasher.getInstance();
	}
	
	public int getDepth()
	{
		return this.depth;
	}
	
	public int getLength()
	{
		return this.directory.length;
	}
	
	/**
	 * Inserts a value into a Directory entry's bucket. 
	 * 
	 * @param value Value to insert.
	 */
	public void insert(Integer value, Integer flag)
	{
		int pseudokey = this.h.hash(value);
                
                Label2 label2 = new Label2();
		
		System.out.println("PseudoKey = " + pseudokey);
		
		int key = BitUtility.getRightMostBits(pseudokey, this.depth);
		
		System.out.println("depth = " + depth + "key(not pseudo) = " + key);
                
               if(flag == 1){ try {
    			
    			FileInputStream fi = new FileInputStream(new File("Label2.txt"));
    			ObjectInputStream oi = new ObjectInputStream(fi);
    			
    			label2 = (Label2) oi.readObject();
    			
    			oi.close();
    			fi.close();
    			
    		}
    		
    		catch(Exception e) {
    			
    			//System.out.println(e.getStackTrace());
    			e.printStackTrace();
    			
    		}
                
                System.out.println("label2.str = " + label2.str);
                
                label2.str += this.depth + " right-most bits of hashed value: " + String.format("%" + this.depth + "s", Integer.toBinaryString(key)).replace(' ', '0') + "<br/><br/>";
		
               }
                
		Bucket b = this.directory[key];
		
		if (Simulator.DEBUG){
			System.out.println("Inserting " + value + " to directory["+key+"] (Bucket: "+b.id+")");
                        
                       if(flag == 1){ label2.str += "Inserting " + value + " to directory[" + key + "] (Bucket: " + b.id + ")" + "<br/><br/>";}
                        
                }
       		
		boolean inserted = b.insert(value);
                
                if(flag == 1){try {
    			
    			FileOutputStream f = new FileOutputStream(new File("Label2.txt"));
    			ObjectOutputStream o = new ObjectOutputStream(f);
    		
    			o.writeObject(label2);
    		
    			o.close();
    			f.close();
    		
    		}
    	
    		catch(Exception e) {
    		
    			//System.out.println(e.getStackTrace());
    			e.printStackTrace();
    		
    		}}
		
		while (! inserted) {			
			if (this.depth > b.getDepth()) {
                            
                            if(flag == 1){
                                
                                label2 = new Label2();
                                
                                try {
    			
    			FileInputStream fi = new FileInputStream(new File("Label2.txt"));
    			ObjectInputStream oi = new ObjectInputStream(fi);
    			
    			label2 = (Label2) oi.readObject();
    			
    			oi.close();
    			fi.close();
    			
    		}
    		
    		catch(Exception e) {
    			
    			//System.out.println(e.getStackTrace());
    			e.printStackTrace();
    			
    		}
                                
                            }
                            
				if (Simulator.DEBUG){
					System.out.println("Bucket " + b.id + " is full!");
                                        
                                        if(flag == 1){label2.str += "Bucket " + b.id + " is full!" + "<br/><br/>";}
                                        
                                }      
                                
                                if(flag == 1){try {
    			
    			FileOutputStream f = new FileOutputStream(new File("Label2.txt"));
    			ObjectOutputStream o = new ObjectOutputStream(f);
    		
    			o.writeObject(label2);
    		
    			o.close();
    			f.close();
    		
    		}
    	
    		catch(Exception e) {
    		
    			//System.out.println(e.getStackTrace());
    			e.printStackTrace();
    		
    		}}
				
				Bucket b2 = new Bucket(b);
				b2.incDepth1();
				b.incDepth0();
				
				b.filter(b2, b.getBitPattern());
				
				// TODO: This could be optimized a bit if they're adjacent.
				// Could just loop left and right while directory[i] == b, starting at i = key.
				for (int i = 0; i < this.directory.length; i++) {
					if (directory[i] == b) {
						int dirIdxTruncatedToBucketDepth = /*i >>> (this.depth - b2.getDepth())*/ BitUtility.getRightMostBits(i, b2.getDepth());
						System.out.println("dirIdxTruncatedToBucketDepth = " + dirIdxTruncatedToBucketDepth);
						if (dirIdxTruncatedToBucketDepth != b.getBitPattern())
							directory[i] = b2;
					}
				}
			}
			else if (this.depth == b.getDepth()) {
				expand(key, flag);
                                
                                
			}
			else {
				ErrorLogger.logIssue("Directory.insertAt(String)", "Global depth < Local Depth");
				System.exit(-1);
			}
			
			key = BitUtility.getRightMostBits(pseudokey, this.depth);
			b = this.directory[key];
			inserted = b.insert(value);
		}
	}
	
	/**
	 * Searches for value and counts the number of probes along the way.
	 * 
	 * @param value The string to search for
	 * @return the number of probes to find value
	 */
	public int countProbes(Integer value)
	{
		int pseudokey = this.h.hash(value);
		int key = BitUtility.getRightMostBits(pseudokey, this.depth);
		Bucket b = this.directory[key];
		return b.countProbes(value);
	}
	
	/**
	 * Doubles the size of the directory, increments depth, and updates the references to buckets.
	 * 
	 * @param fullBucketIndex The index of the directory entry referencing a full bucket.
	 */
	private void expand(int fullBucketIndex, int flag)
	{		
		if (this.depth == Directory.MAX_DEPTH) {
			if (Simulator.DEBUG)
				System.out.println("Directory has reached max depth!. Creating chained bucket now.");
			
			this.directory[fullBucketIndex].chainBucket();
			return;
			/*
			ErrorLogger.logError("Directory.expand(int)", 
					"Directory has reached maximum size.\nThis is most likely because too many " + 
					"words are being hashed to the same bitpattern/pseudokey. " + 
					"Try a more distributed hash function.");
			System.exit(-1);
			*/
		}
                
                Label2 label2 = new Label2();
                
                if(flag == 1){try {
    			
    			FileInputStream fi = new FileInputStream(new File("Label2.txt"));
    			ObjectInputStream oi = new ObjectInputStream(fi);
    			
    			label2 = (Label2) oi.readObject();
    			
    			oi.close();
    			fi.close();
    			
    		}
    		
    		catch(Exception e) {
    			
    			//System.out.println(e.getStackTrace());
    			e.printStackTrace();
    			
    		}}
		
		if (Simulator.DEBUG){
			System.out.println("Expanding Directory: ");
                        
                        if(flag == 1){label2.str += "Expanding Directory: " + "<br/><br/>";}
                        
                }
		
		Bucket[] newDirectory = new Bucket[this.directory.length * 2];
		
		for (int i = 0; i < this.directory.length; i++) {
			if (i != fullBucketIndex) {
				newDirectory[/*i*2*/i] = this.directory[i];
				newDirectory[/*i*2 + 1*/i + BitUtility.pow(2, this.depth)] = this.directory[i];
			}
			else {
				Bucket b2 = new Bucket(this.directory[fullBucketIndex]);
				b2.incDepth1();
				this.directory[fullBucketIndex].incDepth0();
				this.directory[fullBucketIndex].filter(b2, /*fullBucketIndex * 2*/BitUtility.append0(fullBucketIndex));
				
				newDirectory[/*fullBucketIndex*2*/fullBucketIndex] = this.directory[fullBucketIndex];
				newDirectory[/*fullBucketIndex*2 + 1*/fullBucketIndex + BitUtility.pow(2, this.depth)] = b2;
			}
		}
		
		this.directory = newDirectory;
		this.depth++;
		
		System.out.println("newDepth = "+ this.depth);
                
                if(flag == 1){label2.str += "newDepth = " + this.depth + "<br/><br/>";}
		
		if (Simulator.DEBUG)
			print();
                
                if(flag == 1){try {
    			
    			FileOutputStream f = new FileOutputStream(new File("Label2.txt"));
    			ObjectOutputStream o = new ObjectOutputStream(f);
    		
    			o.writeObject(label2);
    		
    			o.close();
    			f.close();
    		
    		}
    	
    		catch(Exception e) {
    		
    			//System.out.println(e.getStackTrace());
    			e.printStackTrace();
    		
    		}}
	}
	
	public void print()
	{
            
            LabelString ls = new LabelString();
            
            try {
    			
    			FileInputStream fi = new FileInputStream(new File("LabelString.txt"));
    			ObjectInputStream oi = new ObjectInputStream(fi);
    			
    			ls = (LabelString) oi.readObject();
    			
    			oi.close();
    			fi.close();
    			
    		}
    		
    		catch(Exception e) {
    			
    			//System.out.println(e.getStackTrace());
    			e.printStackTrace();
    			
    		}
            
		System.out.println("Directory Depth: " + this.depth);
		int len = (int) Math.pow(2, this.depth);
		for (int i = 0; i < len; i++) {
			String binaryI = String.format("%30s", Integer.toBinaryString(i)).replace(' ', '0');
			System.out.print(binaryI);
			this.directory[i].printBucket();
			System.out.println();
		}
		System.out.println();
                
                String LabelText = "<html>";
            
            LabelText += "Directory Depth: " + this.getDepth() + "<br/>" + "<br/>";
            
            len = (int) Math.pow(2, this.getDepth());
		for (int j = 0; j < len; j++) {
			String binaryI = String.format("%" + this.depth + "s", Integer.toBinaryString(j)).replace(' ', '0');
			LabelText += binaryI;
			this.directory[j].printBucket();
                        try {
    			
    			FileInputStream fi = new FileInputStream(new File("LabelString.txt"));
    			ObjectInputStream oi = new ObjectInputStream(fi);
    			
    			ls = (LabelString) oi.readObject();
    			
    			oi.close();
    			fi.close();
    			
    		}
    		
    		catch(Exception e) {
    			
    			//System.out.println(e.getStackTrace());
    			e.printStackTrace();
    			
    		}
                        LabelText += ls.str + "<br/>" + "<br/>";
			System.out.println();
		}
                
                ls.str = LabelText + "</html>";
                
                try {
    			
    			FileOutputStream f = new FileOutputStream(new File("LabelString.txt"));
    			ObjectOutputStream o = new ObjectOutputStream(f);
    		
    			o.writeObject(ls);
    		
    			o.close();
    			f.close();
    		
    		}
    	
    		catch(Exception e) {
    		
    			//System.out.println(e.getStackTrace());
    			e.printStackTrace();
    		
    		}
                
	}
}
