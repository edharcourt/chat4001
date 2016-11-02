package com.example.ehar.chat4001;

import android.util.Log;

import java.io.IOException;

/**
 * Created by ehar on 10/27/2016.
 */

public class Reader implements Runnable {

    private String LOG_TAG = Reader.class.getName();

    ConnectionManager conn = null;


    public Reader(ConnectionManager conn) {
        this.conn = conn;
    }

    @Override
    public void run() {

        // wait for a valid connection???????
        while (!conn.connected) {
            synchronized (conn) {
                try {
                    conn.wait(); // pause the thread
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        while (true) {

            // read messages from socket
            try {
                String s = conn.from.readLine();
                Log.i(LOG_TAG, s);
            } catch (IOException e) {
                Log.e(LOG_TAG, e.toString());
            }

        }


    }


}
