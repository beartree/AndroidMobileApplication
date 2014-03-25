package com.example.myfirstapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.text.Editable;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View;
import android.view.View.*;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends Activity {
	private static final String[] typestring={"Artists","Albums","Songs"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button = (Button) findViewById(R.id.button1);
        final EditText textfield = (EditText) findViewById(R.id.content);
        final Spinner spin = (Spinner) findViewById(R.id.spinner1);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,typestring);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        spin.setAdapter(adapter);
        
        
        button.setOnClickListener(new OnClickListener() { 
        	public void onClick(View v) {
        		final String content=textfield.getText().toString();
        		final String type = spin.getSelectedItem().toString().toLowerCase();
        		if (content.isEmpty()){
        			AlertDialog dialog = new AlertDialog.Builder(v.getContext()).create();
        			dialog.setMessage("Please Enter something in the Search Box!");
        			dialog.setButton("OK", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
        				
        			});
        		    dialog.show();
        		    return;
        		}
        		else{
        			String new_content=content.replace(' ', '+'); 
        			String url = "http://cs-server.usc.edu:10435/examples/servlet/HelloWorldExample?content="+new_content+"&type="+type;
					Intent intent = new Intent();
					intent.putExtra("url",url);
					intent.putExtra("type",type);
					intent.putExtra("content",content);
					intent.setClass(MainActivity.this,MainActivity1.class);
					startActivity(intent);
					return;
        			
        		}
        	}
        });       
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
   
}

