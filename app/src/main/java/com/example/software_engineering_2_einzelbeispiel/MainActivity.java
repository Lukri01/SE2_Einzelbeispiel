package com.example.software_engineering_2_einzelbeispiel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText matrNr_input;
    TextView output;

    Button abschicken;
    Button berechnen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        abschicken = findViewById(R.id.submit_button);
        berechnen = findViewById(R.id.berechnen_button);
        matrNr_input = findViewById(R.id.matrNr_input);
        output = findViewById(R.id.textView7);

        abschicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMatNr(matrNr_input.getText().toString());
            }
        });

        berechnen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                berechnung(matrNr_input.getText().toString());
            }
        });
    }

    public void sendMatNr(String matr){
        Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                final String matr_nr = matr;
                final String server_answer;
                try {
                    Socket client_socket = new Socket("se2-isys.aau.at",53212);
                    DataOutputStream outToServer = new DataOutputStream(client_socket.getOutputStream());
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));

                    outToServer.writeBytes(matr_nr+"\n");
                    server_answer = inFromServer.readLine();
                    client_socket.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            output.setText(server_answer);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void berechnung(String number) {
        for (int i = 0; i < number.length(); i++) {
            for (int j = 0; j < number.length(); j++) {
                if (ggt(number.charAt(i),number.charAt(j)) > 1 && i != j) {
                    output.setText(i+", "+j);
                }
            }
        }
    }

    public int ggt(int num1, int num2) {
        while (num2 != 0) {
            if (num1 > num2) {
                num1 -= num2;
            } else {
                num2 -= num1;
            }
        }
        return num1;
    }
}