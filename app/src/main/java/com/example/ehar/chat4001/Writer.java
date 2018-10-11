package com.example.ehar.chat4001;

import android.util.Log;

import java.io.IOException;

/**
 * Created by ehar on 10/27/2016.
 */

// This does not need to be a thread. Just get the text from the text view and send it along on the socket. Cant do that 
// on the UI thread but it doesn need to be a thread that just stays alove forever.

public class Writer implements Runnable {

    private String LOG_TAG = Writer.class.getName();

    ConnectionManager conn = null;

    public Writer(ConnectionManager conn) {
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

        int i = 0;
        while (true) {
            conn.to.println("Msg: " + Integer.toString(i++));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, e.toString());
            }
        }
    }
}
