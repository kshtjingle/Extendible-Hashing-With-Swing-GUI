/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netbeans.extendible.hashing;

import java.util.*;
import java.io.*;

/**
 *
 * @author Kshitij
 */
public class UIClass {
    
    public static void main(String args[]){
        
        Scanner in = new Scanner(System.in);
        
        int input  = Integer.parseInt(in.nextLine());
        
        System.out.println(input);
        
        //in.close();
        
        while(!in.nextLine().equals("EOF")){
        
            System.out.println(input);
            
            input = Integer.parseInt(in.nextLine());
        
        }
        
        in.close();
        
        System.out.println(input);
        
    }
    
}
