/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enum;

/**
 *
 * @author Ehab mohamed
 */
public class RESPOND_CODE {

    public static final int SUCCESS = 1;
    public static final int FAILD = 2;
    public static final int ERROR = 3;

    public static final int REG_SUCCESSFUL = 1; // correct input , successful insert(user not exist more than one)
    public static final int REG_FAILED = -1;// user exist 
    public static final int SHORT_PASSWORD = -2;// wrong input (short password or username)
    public static final int NOT_EQUAL_PASSWORD = 4;
    public static final int CORRECT_INPUT = 5;
    public static final int IS_LOGIN = 7;

}
