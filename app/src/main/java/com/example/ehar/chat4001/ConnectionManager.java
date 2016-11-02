package com.example.ehar.chat4001;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

/**
 * Created by ehar on 10/20/2016.
 */

public class ConnectionManager {

    public static int PORT = 12345;

    private static String LOG_TAG = ConnectionManager.class.getName();

    ServerSocket server_sock = null;
    Socket client_sock = null;
    BufferedReader from = null;
    PrintWriter to = null;

    boolean connected = false;

    Runnable server_thread = new Runnable() {

        @Override
        public void run() {

            try {
                server_sock = new ServerSocket(PORT);
                client_sock = server_sock.accept();
                from = new BufferedReader(
                        new InputStreamReader(
                                client_sock.getInputStream()));
                //Scanner s = new Scanner(client_sock.getInputStream());

                to = new PrintWriter(client_sock.getOutputStream(), true);

                // should have a valid connection
            }
            catch (SocketException e) {
                Log.i(LOG_TAG, e.toString());
                return;
                // not necessarily an error
            }
            catch (IOException e) {
                Log.e(LOG_TAG, e.toString());
                return;
                // TODO send a message to the UI that there was a problem
            }
            catch (IllegalArgumentException e) { // bad port number
                Log.e(LOG_TAG, e.toString());
                return;
            }


            // we have a connection
            Log.i(LOG_TAG, "Connected to: " +
                    client_sock.getInetAddress());

            // TODO let user know we are connected
            // Start reader thread?????
            connected = true;
            synchronized (ConnectionManager.this) {
                ConnectionManager.this.notifyAll();
            }
        }
    };


    Runnable client_thread = new Runnable() {
        @Override
        public void run() {
            // what IP address am I connecting to
            try {
                client_sock = new Socket("10.60.28.140", PORT);

                // TODO tell user we connected
                from  = new BufferedReader(
                            new InputStreamReader(
                                    client_sock.getInputStream()));
                to = new PrintWriter(client_sock.getOutputStream());

                server_sock.close();

            } catch (IOException e) {
                Log.e(LOG_TAG, e.toString());
                return;
            }

            // TODO let user know we are connected
            Log.i(LOG_TAG, "Connected");

            connected = true;

            // wake up reader and writer threads
            // who are both waiting for a connection
            synchronized (ConnectionManager.this) {
                ConnectionManager.this.notifyAll();
            }
        }
    };


}
