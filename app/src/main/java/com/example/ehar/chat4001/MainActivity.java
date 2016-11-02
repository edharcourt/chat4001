package com.example.ehar.chat4001;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    ConnectionManager conn = null;

    Button connect_button = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connect_button = (Button) findViewById(R.id.connect);

        connect_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Thread client = new Thread(conn.client_thread);
                client.start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        conn = new ConnectionManager();
        Thread server = new Thread(conn.server_thread);
        server.start();

        // fire up reader/writer threads
        Thread reader_thread = new Thread(new Reader(conn));
        reader_thread.start();

        Thread writer_thread = new Thread(new Writer(conn));
        writer_thread.start();
    }
}
