package com.example.noteai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class NoteEditor extends AppCompatActivity implements RecognitionListener{
    private static final int REQUEST_RECORD_PERMISSION = 100;
    Model model;
    EditText text;
    long rowId;
    private SpeechRecognizer speech = null;
    Intent speechIntent;
    private Intent recognizerIntent;
    public String LOG_TAG = "notedev";
    private static final String TAG = "notedev";
    boolean textChange = true;
    private JSONArray jsonArray;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        text = findViewById(R.id.completeText);
        text.setSelection(text.getText().length());


        model = new Model(getApplicationContext(),"NoteAI",null,1);
        loadData();

        try{
            speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

            // Use a language model based on free-form speech recognition.
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                    getApplicationContext().getPackageName());

            // Add custom listeners.
//                CustomRecognitionListener listener = new CustomRecognitionListener();
            speech = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
            speech.setRecognitionListener(this);


        }catch (Exception e){Log.i(LOG_TAG,"speech exception "+e.getMessage());}



        SeekBar seekBar=(SeekBar)findViewById(R.id.seekBar2);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
//                int p = Integer.min(6-progress,jsonArray.length());
//                try {
//                    text.setText(jsonArray.get(p).toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                Toast.makeText(getApplicationContext(),"seekbar progress: "+progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
                AsyncTask<Void, Void, JSONArray> js = new HTTPReqTask(text).execute();
                try {
                    Log.i(TAG,"async "+js.get());
                    jsonArray = js.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    Log.i(TAG,"async "+e.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.i(TAG,"async "+e.getMessage());
                }

                try{
                    Log.i(TAG,"data insertion in child table "+model.insertChild(rowId));

                    Cursor cursor = model.fetchChildAll();
                    while(cursor.moveToNext()) {
                        RowData row = new RowData(cursor.getLong(0),cursor.getString(1));
                        Log.i(TAG,"fetch all "+cursor.getLong(0)+cursor.getString(1)+cursor.getLong(2));
                    }
                    cursor.close();

                }catch (Exception e){
                    Log.i(TAG,"data insertion in child table "+e.getMessage());
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getApplicationContext(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void loadData(){
        if(getIntent().hasExtra("Text")){
            text.setText(getIntent().getStringExtra("Text"));
            text.setSelection(text.getText().length());
        }

        rowId = getIntent().getLongExtra("RowId",-1);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.i("notedev","before text change");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("notedev","on Text change");
                if(rowId == -1){
                    rowId = model.insertBody(String.valueOf(charSequence));
                }else{
                    model.updateNote(rowId, String.valueOf(charSequence));
                }
                textChange = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                Log.i("notedev","after text change");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speech.stopListening();
        Log.i("notedev","destroyed");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.del:
                Log.i("notedev","delete option item selected "+Long.toString(rowId));
                try{

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Delete the Note")
                            .setMessage("Are you sure you want to delete the note?")
                            .setIcon(R.drawable.ic_baseline_delete_note);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            model.deleteNote(rowId);
                            finish();
                        }
                    });
                    builder.setNegativeButton("No", null);
                    AlertDialog dialog = builder.create();
                    builder.show();
                }
                catch (Exception e){
                    Log.i("notedev",e.getMessage());
                }
                break;
            case R.id.noteRecord:
                try{
                Log.i("notedev","record clicked");

                speech.startListening(speechIntent);
                }catch (Exception e){
                    Log.i(LOG_TAG,"record button "+e.getMessage());
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onReadyForSpeech(Bundle params) {
        Log.d(TAG, "onReadyForSpeech");
    }

    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech");
    }

    public void onRmsChanged(float rmsdB) {
        Log.d(TAG, "onRmsChanged "+ Float.toString(rmsdB));
    }

    public void onBufferReceived(byte[] buffer) {
        Log.d(TAG, "onBufferReceived");
    }

    public void onEndOfSpeech() {
        Log.d(TAG, "onEndofSpeech");
    }

    public void onError(int error) {
        Log.e(TAG, "error " + error);

//        conversionCallaback.onErrorOccured(TranslatorUtil.getErrorText(error));
    }

    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        matches.get(0);
        this.text.setText(this.text.getText() +" "+matches.get(0));
        this.text.setSelection(this.text.getText().length());
        speech.startListening(speechIntent);
    }

    public void onPartialResults(Bundle partialResults) {
        Log.d(TAG, "onPartialResults");
    }

    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "onEvent " + eventType);
    }



    private static class HTTPReqTask extends AsyncTask<Void, Void, JSONArray> {
        EditText text;
        JSONArray jsonArray;
        public HTTPReqTask(EditText input) {
            text = input;
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;

            try {
                HashMap<String,String> mp =new HashMap<String,String>();
                String tex = "To explore Pandora's biosphere, scientists use Na'vi-human hybrids called avatars, operated by genetically matched humans. Jake Sully, a paraplegic former Marine, replaces his deceased identical twin brother as an operator of one. Dr. Grace Augustine, head of the Avatar Program, considers Sully an inadequate replacement but accepts his assignment as a bodyguard. While escorting the avatars of Grace and fellow scientist Dr. Norm Spellman, Jake's avatar is attacked by a thanator and flees into the forest, where he is rescued by Neytiri, a female Na'vi. Witnessing an auspicious sign, she takes him to her clan. Neytiri's mother Mo'at, the clan's spiritual leader, orders her daughter to initiate Jake into their society.";
                mp.put("text",text.getText().toString());
                JSONObject jb = new JSONObject(mp);

                URL url = new URL("http://18.191.145.141:5000/summarize");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setChunkedStreamingMode(0);

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        out, "UTF-8"));
                writer.write(jb.toString());
                writer.flush();

                int code = urlConnection.getResponseCode();
                if (code !=  200) {
                    throw new IOException("Invalid response from server: " + code);
                }

                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject js = new JSONObject(sb.toString());
                jsonArray = js.getJSONArray("spacy");
                Log.i("notedev", String.valueOf(js.getJSONArray("spacy").get(0)));

            } catch (Exception e) {
                Log.i("notedev","http "+e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return jsonArray;
        }
    }

}

