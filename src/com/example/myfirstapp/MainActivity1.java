package com.example.myfirstapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.Builder;
import com.facebook.widget.WebDialog.OnCompleteListener;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SimpleAdapter.ViewBinder;
import android.view.View;
import android.view.View.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
public class MainActivity1 extends Activity{
	
	ListView lv;
	SimpleAdapter  adapter;
	private static final String[] PERMISSIONS = new String[] {"publish_stream"};
	String MY_APP_ID = "630903676925749";
    Facebook facebook = new Facebook("630903676925749");
    private Context context=MainActivity1.this;
    String type, url,content;
    String sampleURL;
    Boolean hasResult=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		type = intent.getStringExtra("type");
		content= intent.getStringExtra("content");
		String arr[] = new String [2];
		arr[0] = url;
		arr[1] = type;
		urlRequest request = new urlRequest();
		request.execute(arr);
		
	}
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }
	 public class urlRequest extends AsyncTask <String, Integer, String>{
			@Override
			protected String doInBackground(String... link){
				String myString="";
				try{
				URL url = new URL (link[0]);
		        URLConnection urlConnection = url.openConnection();
		        InputStream urlStream = urlConnection.getInputStream();
		        BufferedReader br = new BufferedReader(new InputStreamReader(urlStream));
		        String str;
		        while((str = br.readLine()) != null){
		        	myString += str;
		        }
		       }catch(Exception e)
		       {
		        myString=e.getMessage();
		       }
			   try {
				JSONObject json = new JSONObject (myString);
				JSONObject results=json.getJSONObject("results");
				JSONArray resultarray = results.getJSONArray("result");
				if(resultarray.length()!=0){
					hasResult = true;
					if (link[1].equals("artists")){
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						for(int i=0;i<resultarray.length();i++){
							JSONObject result = (JSONObject) resultarray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							String coverURL = result.getString("cover");
							if (coverURL.equals("NA")){
								coverURL = "http://cs-server.usc.edu:10435/examples/servlets/default_artist.png";
							}
							map.put("coverURL", coverURL);
					        try {
					        	BitmapFactory.Options o = new BitmapFactory.Options();
					            Bitmap cover = BitmapFactory.decodeStream((InputStream)new URL(coverURL).getContent(),null,o);
						        map.put("cover", cover);
					        } catch (Exception e) {
					            Log.e("Error", e.getMessage());
					            e.printStackTrace();
					        }
							String name = result.getString("name");
							map.put("name", "Name: \n"+name);
							String genre = result.getString("genre");
							map.put("genre", "Genre: \n"+genre);
							String year = result.getString("year");
							map.put("year", "Year: \n"+year);
							String details = result.getString("details");
							map.put("details", details);
							list.add(map);
						}
						adapter = new SimpleAdapter(MainActivity1.this, list, R.layout.activity,
				                new String[]{"cover", "name","genre","year"},
				                new int[]{R.id.cover,R.id.name,R.id.genre,R.id.year});
						
						adapter.setViewBinder(new ViewBinder() {    
	                        public boolean setViewValue(  
	                                View view,   
	                                Object data,    
	                             String textRepresentation) {    
	                        	if(view instanceof ImageView  && data instanceof Bitmap){    
	                        		ImageView iv = (ImageView) view;    
	                        		iv.setImageBitmap((Bitmap) data);    
	                        		return true;    
	                        	}else    
	                        		return false;    
	                        }    
						});    
					}
					else if (link[1].equals("albums")){
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						for(int i=0;i<resultarray.length();i++){
							JSONObject result = (JSONObject) resultarray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							String coverURL = result.getString("cover");
							if (coverURL.equals("NA")){
								coverURL = "http://cs-server.usc.edu:10435/examples/servlets/default_album.png";
							}
							map.put("coverURL", coverURL);
					        try {
					        	BitmapFactory.Options o = new BitmapFactory.Options();
					            Bitmap cover = BitmapFactory.decodeStream((InputStream)new URL(coverURL).getContent(),null,o);
						        map.put("cover", cover);
					        } catch (Exception e) {
					            Log.e("Error", e.getMessage());
					            e.printStackTrace();
					        }
					        
							String title = result.getString("title");
							map.put("title", "Title: \n"+title);
							String artist = result.getString("artist");
							map.put("artist", "Artist: \n"+artist);
							String genre = result.getString("genre");
							map.put("genre", "Genre: \n"+genre);
							String year = result.getString("year");
							map.put("year", "Year: \n"+year);
							String details = result.getString("details");	
							map.put("details", details);
							list.add(map);
						}
						adapter = new SimpleAdapter(MainActivity1.this, list, R.layout.activity_album,
				                new String[]{"cover", "title","artist","genre","year"},
				                new int[]{R.id.cover,R.id.title,R.id.artist,R.id.genre,R.id.year});
						
						adapter.setViewBinder(new ViewBinder() {    
	                        public boolean setViewValue(  
	                                View view,   
	                                Object data,    
	                             String textRepresentation) {    
	                        	if(view instanceof ImageView  && data instanceof Bitmap){    
	                        		ImageView iv = (ImageView) view;    
	                        		iv.setImageBitmap((Bitmap) data);    
	                        		return true;    
	                        	}else    
	                        		return false;    
	                        }    
						});    
						
					}
					else if(link[1].equals("songs")){
						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						for(int i=0;i<resultarray.length();i++){
							JSONObject result = (JSONObject) resultarray.get(i);
							Map<String, Object> map = new HashMap<String, Object>();
							String coverURL = "http://www-scf.usc.edu/~shuxiong/default_song.png";
							map.put("coverURL", coverURL);
					        try {
					        	BitmapFactory.Options o = new BitmapFactory.Options();
					            Bitmap cover = BitmapFactory.decodeStream((InputStream)new URL(coverURL).getContent(),null,o);
						        map.put("cover", cover);
					        } catch (Exception e) {
					            Log.e("Error", e.getMessage());
					            e.printStackTrace();
					        }
					        String sample = result.getString("sample");
					        map.put("sample",sample);
							String title = result.getString("title");
							if (title.isEmpty()){
								title = "NA";
							}
							map.put("title", "Title: \n"+title);
							String performer = result.getString("performer");
							if (performer.isEmpty()){
								performer = "NA";
							}
							map.put("performer", "Performer: \n"+performer);
							String composer = result.getString("composer");
							if (composer.isEmpty()){
								composer = "NA";
							}
							map.put("composer", "Composer: \n"+composer);
							String details = result.getString("details");
							map.put("details", details);
							list.add(map);
						}
						adapter = new SimpleAdapter(MainActivity1.this, list, R.layout.activity_song,
				                new String[]{"cover", "title","performer","composer"},
				                new int[]{R.id.cover,R.id.title,R.id.performer,R.id.composer});
						
						adapter.setViewBinder(new ViewBinder() {    
	                        public boolean setViewValue(  
	                                View view,   
	                                Object data,    
	                             String textRepresentation) {    
	                        	if(view instanceof ImageView  && data instanceof Bitmap){    
	                        		ImageView iv = (ImageView) view;    
	                        		iv.setImageBitmap((Bitmap) data);    
	                        		return true;    
	                        	}else    
	                        		return false;    
	                        }    
						}); 					
					}
				}
				else{
					hasResult=false;				
				}
				
				
			   } catch (JSONException e) {
				e.printStackTrace();
			}; 
			   return myString;
			}
	    @Override
        protected void onPostExecute(String result){
	    	if(hasResult){
	        	lv = (ListView) findViewById(R.id.list);
	        	lv.setAdapter(adapter);
	        	lv.setClickable(true);
	        	
	        	lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	
	        		  @Override
	        		  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	        		    HashMap<String, String> map = (HashMap<String, String>) lv.getItemAtPosition(position);
	        		    final Bundle params = new Bundle();
	        		    
	        		    if (type.equals("artists")){
	        		    	String cover = map.get("coverURL");
	        		    	String name = map.get("name").split("\n")[1];
	            		    String genre = map.get("genre").split("\n")[1];
	            		    String year = map.get("year").split("\n")[1];
	            		    String details = map.get("details");
	            		    params.putString("name", name);
	        	            params.putString("caption", "I like "+name+" who is active since "+year);
	        	            params.putString("description", "Genre of Music is: "+genre);
	        	            params.putString("link", details);
	        	            params.putString("picture", cover);
	        	            params.putString("properties", " {'Look at details':{'text':'here','href':'"+details+"'}}");
	        		    }
	        		    else if (type.equals("albums")){
	        		    	String cover = map.get("coverURL");
	        		    	String title = map.get("title").split("\n")[1];
	        		    	String artist = map.get("artist").split("\n")[1];
	            		    String genre = map.get("genre").split("\n")[1];
	            		    String year = map.get("year").split("\n")[1];
	            		    String details = map.get("details");
	            		    params.putString("name", title);
	        	            params.putString("caption", "I like "+title+" released in "+year);
	        	            params.putString("description", "Artist: "+artist+" Genre: "+genre);
	        	            params.putString("link", details);
	        	            params.putString("picture", cover); 
	        	            params.putString("properties", " {'Look at details':{'text':'here','href':'"+details+"'}}");
	        		    }
	        		    else if (type.equals("songs")){
	        		    	String cover = map.get("coverURL");
	        		    	String performer = map.get("performer").split("\n")[1];
	        		    	String composer = map.get("composer").split("\n")[1];
	            		    String title = map.get("title").split("\n")[1];
	            		    String details = map.get("details");
	            		    sampleURL = map.get("sample");
	            		    params.putString("name", title);
	        	            params.putString("caption", "I like "+title+" composed by "+composer);
	        	            params.putString("description",  "Performer: "+performer);
	        	            params.putString("link", details);
	        	            params.putString("picture", cover); 
	        	            params.putString("properties", " {'Look at details':{'text':'here','href':'"+details+"'}}");
	        		    }
	        		    if (type.equals("songs")&&!sampleURL.equals("NA")){   
	        		    		new AlertDialog.Builder(MainActivity1.this).setTitle("Post to Facebook").setItems(
	        		    				new String[] { "Post", "Cancel", "Sample Music" }, new DialogInterface.OnClickListener() {  
	          									public void onClick(DialogInterface dialog, int item) {  
	                  		    	    	        if (item==0){
	                  		    	    	        	postFeed(params);
	                  		    	    	        }
	                  		    	    	        if (item==1){
	                  		    	    	        	return;
	                  		    	    	        }
	                  		    	    	        if (item==2){
	                  		    	    	        	
	                  		    	    	        	String url = sampleURL; // your URL here
	                  		    	    	        	final MediaPlayer mediaPlayer = new MediaPlayer();
	                  		    	    	        	mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	                  		    	    	        	try {
	       												mediaPlayer.setDataSource(url);
	       												mediaPlayer.prepare();
	       											} catch (IllegalArgumentException e) {
	       												// TODO Auto-generated catch block
	       												e.printStackTrace();
	       											} catch (SecurityException e) {
	       												// TODO Auto-generated catch block
	       												e.printStackTrace();
	       											} catch (IllegalStateException e) {
	       												// TODO Auto-generated catch block
	       												e.printStackTrace();
	       											} catch (IOException e) {
	       												// TODO Auto-generated catch block
	       												e.printStackTrace();
	       											}
	                  		    	    	        	try {
	       												mediaPlayer.prepare();
	       											} catch (IllegalStateException e) {
	       												// TODO Auto-generated catch block
	       												e.printStackTrace();
	       											} catch (IOException e) {
	       												// TODO Auto-generated catch block
	       												e.printStackTrace();
	       											} // might take long! (for buffering, etc)
//	                  		    	    	        	mediaPlayer.start();
	                  		    	    	        	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
	                  		    	    	        	alertDialogBuilder.setTitle("Play Sample");
	                  		    	    	        	alertDialogBuilder.setPositiveButton("Play",new DialogInterface.OnClickListener(){

															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																// TODO Auto-generated method stub
																
																try {
																	Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
																	field.setAccessible(true);
																	field.set(dialog, false);
																	mediaPlayer.start();
																	
																	} catch (Exception e) {
																	e.printStackTrace();
																	
																}
															}
	                  		    	    	        		
	                  		    	    	        	});
	                  		    	    	        	alertDialogBuilder.setNegativeButton("Stop",new DialogInterface.OnClickListener(){

															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																// TODO Auto-generated method stub
																
																try {
																	Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
																	field.setAccessible(true);
																	field.set(dialog, true);
																	dialog.dismiss();
																	mediaPlayer.stop();
																	
																	} catch (Exception e) {
																	e.printStackTrace();
																	
																}
															}
	                  		    	    	        		
	                  		    	    	        	});
	                  		    	    	        	AlertDialog alertDialog = alertDialogBuilder.create();
	                  		    	    	        	alertDialog.show();

	                  		    	    	        	
	                  		    	    	        	
	                  		    	    	        	
	                  		    	    	        }                  		    	    	       
	                  		    	    	      

	                  		    	    	    }
	          							}).show();       		    		        		    	
	        		    }
	        		    else{
	        		    	new AlertDialog.Builder(MainActivity1.this).setTitle("Post to Facebook").setItems(
	           		    	     new String[] { "Post", "Cancel" }, new DialogInterface.OnClickListener() {  
	   									public void onClick(DialogInterface dialog, int item) {  
	           		    	    	        if (item==0){
	           		    	    	        	postFeed(params);
	           		    	    	        }
	           		    	    	        if (item==1){
	           		    	    	        	return;
	           		    	    	        }
	           		    	    	    }
	   							}).show();
	        		    	
	        		    }
	        		  }
	        		});
	    	}
	    	else{
	    		setContentView(R.layout.activity_noresult);
	    		TextView textview = (TextView) findViewById(R.id.textView2);
	    		String text = content+" of type "+type;
	    		textview.setText(text);
	    			    		
	    	}
        	
        	}

	    @SuppressWarnings("deprecation")
		public void postFeed(final Bundle params){
	    	
	    	facebook.setAccessToken("token");
	    	facebook.setAccessExpiresIn("3600");
	    	
	    	if (!facebook.isSessionValid()){
            	facebook.authorize(MainActivity1.this, PERMISSIONS, Facebook.FORCE_DIALOG_AUTH, new DialogListener(){
            		
        			@Override
        			public void onComplete(Bundle values) {
        				
        				Builder feedDialog1 = new WebDialog.Builder(context, MY_APP_ID, "feed", params);
                        feedDialog1.setOnCompleteListener(new OnCompleteListener(){

        					@Override
        					public void onComplete(Bundle values,
        							FacebookException error) {
        						// TODO Auto-generated method stub
        					if (error == null) {
        						final String postId = values.getString("post_id");        						
        						if (postId != null){
        							Toast.makeText(context, "Post Feed Success!", Toast.LENGTH_LONG).show();
        						}
        					}
        					}});
                        WebDialog feedDialog = feedDialog1.build();
                        feedDialog.show();
        			}

					@Override
					public void onFacebookError(
							FacebookError e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onError(
							DialogError e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						
					}});
            	
            }
            else{
                Builder feedDialog1 = new WebDialog.Builder(context, MY_APP_ID, "feed", params);
                feedDialog1.setOnCompleteListener(new OnCompleteListener(){

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						// TODO Auto-generated method stub
						if (error == null) {
    						final String postId = values.getString("post_id");        						
    						if (postId != null){
    							Toast.makeText(context, "Post Feed Success!", Toast.LENGTH_LONG).show();
    						}
    					}
					}});
                WebDialog feedDialog = feedDialog1.build();
                feedDialog.show();

            }
	    }
	    	
	    }
	 @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        facebook.authorizeCallback(requestCode, resultCode, data);
	    }
}

