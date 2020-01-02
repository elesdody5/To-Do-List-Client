/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_request;

import Enum.REQUEST;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Elesdody
 */
public class Server implements Request {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 5005;
    Socket socket;
    PrintStream ps;
    BufferedReader in;

    public Server() {
        try {
            socket = new Socket(IP, PORT);
            ps = new PrintStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public JSONObject post(String[] paramters, JSONObject body) {
        ps.println(REQUEST.POST);
        for (String paramter : paramters) {
            System.out.println("server sending " + paramter);
            ps.print("/");
            ps.print(paramter);

        }
        ps.println();
        ps.println(body.toString());
        // to notifay the client the response was ended 
        ps.println(REQUEST.END);
        JSONObject json = null;
        try {
            // waiting for responde
            json = readJson();

        } catch (IOException | JSONException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            return json;
        }

    }

    @Override
    public JSONObject get(String[] paramters) {
        ps.println(REQUEST.GET);
        for (String paramter : paramters) {
            ps.print("/");
            ps.print(paramter);
        }
        ps.println();
        // to notifay the client the response was ended 
        ps.println(REQUEST.END);
        JSONObject json = null;
        try {
            // waiting for responde
            json = readJson();

        } catch (IOException | JSONException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            return json;
        }
    }

    // return the element id that inserted in database or -1 if error
    @Override
    public int put(String[] paramters, JSONObject body) {
        ps.println(REQUEST.PUT);
        for (String paramter : paramters) {
            ps.print("/");
            ps.print(paramter);
        }
        ps.println();
        ps.println(body.toString());
        // to notifay the client the response was ended 
        ps.println(REQUEST.END);
        int response = 0;
        try {

            response = in.read();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    // return the element id that deleted from database or -1 if error
    @Override
    public int delete(String[] paramters) {
        ps.println(REQUEST.PUT);
        for (String paramter : paramters) {
            ps.print("/");
            ps.print(paramter);
        }
        ps.println();
        // to notifay the client the response was ended 
        ps.println(REQUEST.END);
        int response = 0;
        try {

            response = in.read();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    JSONObject readJson() throws IOException, JSONException {
        StringBuilder body = new StringBuilder();
        String data = in.readLine();

        while (!data.equals(REQUEST.END)) {
            body.append(data);
            data = in.readLine();

        }
        return new JSONObject(body.toString());
    }

}
