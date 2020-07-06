package com.example.noteai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Locale;


public class NoteEditor extends AppCompatActivity implements RecognitionListener{
    private static final int REQUEST_RECORD_PERMISSION = 100;
    Model model;
    EditText text;
    long rowId;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    public String LOG_TAG = "notedev";
    private static final String TAG = "notedev";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        text = findViewById(R.id.completeText);
        text.setSelection(text.getText().length());


        model = new Model(getApplicationContext(),"NoteAI",null,1);
        loadData();

        try{
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this));
//        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        }catch (Exception e){Log.i(LOG_TAG,"speech exception "+e.getMessage());}
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
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                // Use a language model based on free-form speech recognition.
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                        getApplicationContext().getPackageName());

                // Add custom listeners.
//                CustomRecognitionListener listener = new CustomRecognitionListener();
                SpeechRecognizer sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                sr.setRecognitionListener(this);
                sr.startListening(intent);
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
        String text = "";
        matches.get(0);
        this.text.setText(this.text.getText() +" "+matches.get(0));
        this.text.setSelection(this.text.getText().length());
    }

    public void onPartialResults(Bundle partialResults) {
        Log.d(TAG, "onPartialResults");
    }

    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "onEvent " + eventType);
    }
}
