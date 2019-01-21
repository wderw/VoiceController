package com.voicecontroller.voicecontroller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class RequestTask extends AsyncTask<Void, Integer, String> {

    private InetAddress address;
    private Socket socket;
    private String command;
    private Context context;


    public RequestTask(Context context, String command) {
        this.command = command;
        this.context =context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String stringAddress = sharedPreferences.getString("IP_ADDRESS","");
        if (stringAddress.isEmpty()){
            Log.e("RequestTask","Nie udało się odczytać IP z SharedPreferences");
            Toast.makeText(context,"Brak zapisanych ustawień", Toast.LENGTH_SHORT).show();
            cancel(true);
        }
        else {
            try {
                address = InetAddress.getByName(stringAddress);
            } catch (UnknownHostException e) {
                Log.e("RequestTask","Nieprawidłowy adres IP");
                e.printStackTrace();
                cancel(true);
            }
        }


    }

    @Override
    protected String doInBackground(Void... voids) {

        if (isCancelled()){
            return "Zadanie zostało przerwane";
        }
        OutputStreamWriter osw=null;
        BufferedWriter bw=null;
        PrintWriter printWriter=null;

        String retMsg;

        try{
            socket = new Socket(address,23);
            osw = new OutputStreamWriter(socket.getOutputStream());
            bw = new BufferedWriter(osw);
            printWriter = new PrintWriter(bw, true);
            printWriter.println(command);
            printWriter.flush();
            Log.i("RequestTask", "Wysłano żądanie: "+command);
            retMsg ="Wysłano rządanie: "+command;
        }
        catch (IOException e){
           e.printStackTrace();
           Log.e("RequestTask", "Niepowodzenie połączenia");
           retMsg = "Niepowodzenie połączenia";
        }
        finally {
            if (socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw!=null){
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bw!=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (printWriter!=null){
                printWriter.close();
            }
        }
        return retMsg;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
        if (socket!=null){
            if (socket.isConnected()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
        if (socket!=null){
            if (socket.isConnected()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
