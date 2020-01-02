/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_request;

import javax.json.JsonObject;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;

/**
 *
 * @author Elesdody
 */
public interface Request {
    JSONObject post(String[]paramter,JSONObject body); 
    JSONObject get(String[]paramter); 
    int put(String[]paramter,JSONObject body); 
    int delete(String[]paramter); 
}
