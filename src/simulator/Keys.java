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
import java.io.*;
import java.util.*;

public class Keys implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Integer> keyList = new ArrayList<Integer>();
	
	public void clearKeysList(){
		
		keyList.clear();
		
	}
	
}
