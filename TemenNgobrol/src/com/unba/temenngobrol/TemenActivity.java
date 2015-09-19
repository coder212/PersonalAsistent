package com.unba.temenngobrol;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.TextView;
import android.widget.Toast;

public class TemenActivity extends Activity implements OnInitListener {

	



	private SensorManager sm;
	private double hasilsensor=10;
	private TextToSpeech ts;
	/// E, dd MMM yyyy HH:mm:ss
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private final int CODE_SPEECH_TO_TEXT = 100;
	private final int KODE_CEK=0;
	private TextView tv;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODOs Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temen);
		sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		tv = (TextView)findViewById(R.id.text);
		tv.setText("dekat kan tangan ke sensor proximity untuk berbicara");
		Intent intentNew  = new Intent();
		intentNew.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(intentNew, KODE_CEK);
	}
	
	private SensorEventListener proximlisten = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODOs Auto-generated method stub
			hasilsensor= event.values[0];
			
			if(hasilsensor==0){
				ts.speak("hai bro please asking to me ", TextToSpeech.QUEUE_ADD, null);
				
			}else {
				mengeluarkanMik();
			}
		}
		

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODOs Auto-generated method stub
			
		}
	};


	private void mengeluarkanMik() {
		// TODOs Auto-generated method stub
		
		Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "please speak...");
		try{
			TemenActivity.this.startActivityForResult(speechIntent, CODE_SPEECH_TO_TEXT);
		}catch(ActivityNotFoundException e){
			Toast.makeText(getApplicationContext(), "perangkat tak di dukung", Toast.LENGTH_SHORT).show();
		}
	}
	
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODOs Auto-generated method stub
		super.onResume();
		
		Sensor proxi = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		if(proxi !=null){
			sm.registerListener(proximlisten,proxi,SensorManager.SENSOR_DELAY_GAME);
		}else{
			tv.setText("Maaf devicemu tidak mendukung sensor proximity jadi tak bisa menggunakan applikasi ini");
		}
	}
	
	 /* @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODOs Auto-generated method stub
		sm.unregisterListener(proximlisten);
		super.onPause();

	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODOs Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CODE_SPEECH_TO_TEXT:
			if(resultCode == RESULT_OK && data != null){
				ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				if(result.get(0).contains("hello")){
					ts.speak("friend to speak created by unba laboratory", TextToSpeech.QUEUE_ADD, null);
				}else if(result.get(0).contains("now")){
					ts.speak("the time now is "+sdf.format(new Date(System.currentTimeMillis())), TextToSpeech.QUEUE_ADD, null);
				}else if(result.get(0).contains("real")){
					ts.speak("i like real Madrid football team", TextToSpeech.QUEUE_ADD, null);
				}else if(result.get(0).contains("love")){
					ts.speak("I Love My self", TextToSpeech.QUEUE_ADD, null);
				}else {
					ts.speak("you speak "+result.get(0), TextToSpeech.QUEUE_ADD, null);
				}
			}
			break;

		case KODE_CEK:
			if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
				//succes bray
				ts = new TextToSpeech(this, this);
			}else{
				Intent intentInstall = new Intent();
				intentInstall.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(intentInstall);
			}
			break;
		}
	}



	@Override
	public void onInit(int status) {
		// TODOs Auto-generated method stub
		if(status == TextToSpeech.SUCCESS){
			Toast.makeText(TemenActivity.this,"berhasil inisiasi text to speech", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(TemenActivity.this,"gagal inisiasi text to speech", Toast.LENGTH_LONG).show();
		}
	}

}
