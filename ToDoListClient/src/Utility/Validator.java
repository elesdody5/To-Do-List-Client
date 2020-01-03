/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

/**
 *
 * @author Ehab mohamed
 */
public class Validator {
    
    
    /*
        private static final String EMPTY_USER_NAME = "Invalid User Name";
    private static final String EMPTY_PASSWORD = "Invalid Password";
    private static final String HEADER = "Invalid";
    private static final String CONTENT = "Check your user name and password";
    */
    
    public static boolean checkPasswordEquality(String p1 ,String p2){
        return p1.equals(p2);
    }
    
    public static boolean checkPasswordAndUserName(String userName, String pass){
        return (!userName.isEmpty() && !pass.isEmpty());
    }
    
}
