package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels.ModelTextSqLite1;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Appdatabase.DatabaseTextRepeater;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Appdatabase.DatabaseRecentHandler;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RepeatTextActivity extends AppCompatActivity {
    public DatabaseRecentHandler databaseHandler;
    private CheckBox button_addspace;
    private CheckBox button_newLine;
    private Button button_repeat;
    private DatabaseTextRepeater database;
    private String repeated_text;
    private SeekBar seekbar_repeat;
    private EditText text_repeat;
    private EditText text_repeat_number;
    AdView adView;
    @Override
    protected void onDestroy() {
        if (adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_text);

        adView = AdmobUtils.showBanner(this, findViewById(R.id.llAdds));

        new BackgroundTask().execute();

        Toolbar toolbar = findViewById(R.id.repeat_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        text_repeat = findViewById(R.id.text_repeat);
        text_repeat_number = findViewById(R.id.text_repeat_number);
        seekbar_repeat = findViewById(R.id.seekbar_repeat);
        button_newLine = findViewById(R.id.button_newLine);
        button_addspace = findViewById(R.id.button_addspace);
        button_repeat = findViewById(R.id.button_repeat);
        text_repeat_number.setText("1");

        seekbar_repeat.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    try {
                        text_repeat_number.setText("1");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    EditText editText = text_repeat_number;
                    StringBuilder sb = new StringBuilder();
                    sb.append(progress);
                    sb.append("");
                    editText.setText(sb.toString());
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        button_repeat.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    if (TextUtils.isEmpty(text_repeat.getText().toString().trim())) {
                        text_repeat.setError("Please enter text to repeat.");
                        text_repeat.requestFocus();
                    } else if (TextUtils.isEmpty(text_repeat_number.getText().toString().trim())) {
                        text_repeat_number.setError("Please enter value.");
                        text_repeat_number.requestFocus();
                    } else if (Integer.parseInt(text_repeat_number.getText().toString()) > 0) {

                        InterstitialAd interstitial = AdmobUtils.getInterstitial();
                        if (interstitial != null) {
                            interstitial.show(RepeatTextActivity.this);
                            interstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();

                                    new TaskTextRepeater().execute();
                                    AdmobUtils.loadInterstitial(RepeatTextActivity.this);

                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    AdmobUtils.loadInterstitial(RepeatTextActivity.this);

                                }


                            });
                        } else {
                            new TaskTextRepeater().execute();
                            AdmobUtils.loadInterstitial(RepeatTextActivity.this);
                        }

                    } else {
                        text_repeat_number.setError("Repeat limit should be 1 or more.");
                        text_repeat_number.requestFocus();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class TaskTextRepeater extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        private String resp;

        private TaskTextRepeater() {
        }

        @Override
        public String doInBackground(String... params) {
            publishProgress("Repeating...");
            if (button_newLine.isChecked() && button_addspace.isChecked()) {
                try {
                    int no = Integer.parseInt(text_repeat_number.getText().toString());
                    RepeatTextActivity textRepeaterActivity = RepeatTextActivity.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append(text_repeat.getText().toString());
                    sb.append(" \n");
                    textRepeaterActivity.repeated_text = new String(new char[no]).replace("\u0000", sb.toString());
                    this.resp = "";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (!button_newLine.isChecked() && !button_addspace.isChecked()) {
                try {
                    int no2 = Integer.parseInt(text_repeat_number.getText().toString());
                    repeated_text = new String(new char[no2]).replace("\u0000", text_repeat.getText().toString());
                    this.resp = "";
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (button_newLine.isChecked() && !button_addspace.isChecked()) {
                try {
                    int no3 = Integer.parseInt(text_repeat_number.getText().toString());
                    RepeatTextActivity textRepeaterActivity2 = RepeatTextActivity.this;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(text_repeat.getText().toString());
                    sb2.append("\n");
                    textRepeaterActivity2.repeated_text = new String(new char[no3]).replace("\u0000", sb2.toString());
                    this.resp = "";
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
            if (!button_newLine.isChecked() && button_addspace.isChecked()) {
                try {
                    int no4 = Integer.parseInt(text_repeat_number.getText().toString());
                    RepeatTextActivity textRepeaterActivity3 = RepeatTextActivity.this;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(text_repeat.getText().toString());
                    sb3.append(" ");
                    textRepeaterActivity3.repeated_text = new String(new char[no4]).replace("\u0000", sb3.toString());
                    this.resp = "";
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            }
            return this.resp;
        }

        @Override
        public void onPostExecute(String result) {
            try {
                this.progressDialog.dismiss();
                ModelTextSqLite1 SqLiteText1 = new ModelTextSqLite1();
                SqLiteText1.setRep_text(repeated_text);
                SqLiteText1.setId(1);
                if (repeated_text != null) {
                    if (database.GetTotalRepeatText() > 0) {
                        database.updateText(SqLiteText1);
                    } else {
                        database.AddText(SqLiteText1);
                    }
                    databaseHandler.AddText(SqLiteText1);
                    startActivity(new Intent(RepeatTextActivity.this, ShareMessageActivity.class));
                } else {
                    Toast.makeText(RepeatTextActivity.this, "Some error occured please try again.", Toast.LENGTH_SHORT).show();
                }
                Log.e("Textrepeated", "ModelTextSqLite");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPreExecute() {
            try {
                this.progressDialog = ProgressDialog.show(RepeatTextActivity.this, "ProgressDialog", "Repeating text");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onProgressUpdate(String... text) {
            StringBuilder sb = new StringBuilder();
            sb.append("ModelTextSqLite");
            sb.append(text[0]);
            Log.e("Textrepeated", sb.toString());
        }
    }

    public class BackgroundTask extends AsyncTask<Void, Void, Void> {
        public BackgroundTask() {
        }

        @Override
        public void onPreExecute() {
            database = new DatabaseTextRepeater(RepeatTextActivity.this);
            databaseHandler = new DatabaseRecentHandler(RepeatTextActivity.this);
        }

        @Override
        public Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        public void onPostExecute(Void aVoid) {
        }
    }


}
