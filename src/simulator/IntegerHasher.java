/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

/**
 *
 * @author Kshitij
 */
public class IntegerHasher implements Hasher<Integer>{
	
	private static IntegerHasher instance = null;
	
	private IntegerHasher() {}
	
	public static IntegerHasher getInstance()
	{
		if (instance == null) {
			instance = new IntegerHasher();
		}
		
		return instance;
	}

	//@Override
	public int hash(Integer key) {

		Integer hashk = key % 37;
		
		System.out.println("HAsh = " + hashk);
		
		return hashk;
		
	}	
	
}
