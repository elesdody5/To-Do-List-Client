/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_request;

import Enum.REQUEST;
import authontication.LoginController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import org.json.JSONException;
import org.json.JSONObject;
import server_connection.Connection;

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
    LoginController loginController;
    private static Listener listener;
    
    public Server() throws IOException {
        socket = Connection.getSocketConnection();
        ps = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        if (listener == null) {
            startnewThread();
        }
    }
    
    @Override
    public JSONObject post(String[] paramters, JSONObject body) {
        ps.println(REQUEST.POST);
        for (String paramter : paramters) {
            ps.print("/");
            ps.print(paramter);

        }
        ps.println();
        ps.println(body.toString());
        // to notifay the client the response was ended 

        ps.println(REQUEST.END);

        JSONObject json = null;
        try {
            listener.readJson = true;
            // waiting for responde

            listener.join();
            json = listener.json;
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            startnewThread();
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
        //ps.println(REQUEST.END);
        JSONObject json = null;
        try {
            // waiting for responde
            listener.readJson = true;
            listener.join();
            json = listener.json;

        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            startnewThread();
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

        listener.readJson = false;
        try {
            listener.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        response = Integer.parseInt(listener.data);
        startnewThread();
        return response;
    }

    // return the element id that deleted from database or -1 if error
    @Override
    public int delete(String[] paramters) {
        ps.println(REQUEST.DELETE);

        for (String paramter : paramters) {
            ps.print("/");
            ps.print(paramter);
        }
        ps.println();
        // to notifay the client the response was ended 
        // ps.println(REQUEST.END);

        int response = 0;

        listener.readJson = false;
        try {
            listener.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        response = Integer.parseInt(listener.data);
        startnewThread();
        return response;
    }

    private void startnewThread() {
        listener = new Listener();
        listener.start();
    }

    private class Listener extends Thread {

        String data;
        JSONObject json;
        boolean readJson = false;

        @Override
        public void run() {
            try {
                data = in.readLine();
                if (data.equals(REQUEST.NOTIFICATION)) {
                    readJson = true;
                    System.out.println(data);

                }
                if (readJson) {
                    readJson();
                }
            } catch (IOException ex) {
                Platform.runLater(()->{
                    Alert alert  = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Connection Lost");
                    alert.showAndWait();
                });
            } catch (JSONException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        void readJson() throws IOException, JSONException {
            StringBuilder body = new StringBuilder();

            while (!data.equals(REQUEST.END)) {

                body.append(data);
                data = in.readLine();

            }
            json = new JSONObject(body.toString());
        }
    }
}
