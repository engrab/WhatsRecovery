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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.PreferencePurchase;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.AppModels.ModelTextSqLite1;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Appdatabase.DatabaseTextRepeater;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Appdatabase.DatabaseRecentHandler;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BlankMessageActivity extends AppCompatActivity {
    public DatabaseRecentHandler databaseHandler;
    private DatabaseTextRepeater database;
    private String repeated_text;
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
        setContentView(R.layout.activity_blank_message);
        adView = AdmobUtils.showBanner(this, findViewById(R.id.llAdds));

        Toolbar toolbar = findViewById(R.id.toolbar_blank);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new BackgroundTask().execute();


        text_repeat_number = findViewById(R.id.text_repeat_number);
        SeekBar seekBar = findViewById(R.id.seekbar_repeat);
        text_repeat_number.setText("1");
        Button button_repeat = findViewById(R.id.button_repeat);

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    try {
                        text_repeat_number.setText("1");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
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
                    if (TextUtils.isEmpty(text_repeat_number.getText().toString().trim())) {
                        text_repeat_number.setError("Please enter value.");
                        text_repeat_number.requestFocus();
                    } else if (Integer.parseInt(text_repeat_number.getText().toString()) > 0) {
                        new TaskBlankMessage().execute();
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


    private class TaskBlankMessage extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        int no;
        private String resp;

        private TaskBlankMessage() {

        }

        @Override
        public String doInBackground(String... params) {
            publishProgress("Repeating...");
            try {
                int no = Integer.parseInt(text_repeat_number.getText().toString());
                repeated_text = new String(new char[no]).replace("\u0000", "\n");
                resp = "\n";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resp;
        }

        @Override
        public void onPostExecute(String result) {
            try {
                progressDialog.dismiss();
                ModelTextSqLite1 sqLiteText = new ModelTextSqLite1();
                sqLiteText.setRep_text(repeated_text);
                sqLiteText.setId(1);
                if (repeated_text != null) {
                    if (database.GetTotalRepeatText() > 0) {
                        database.updateText(sqLiteText);
                    } else {
                        database.AddText(sqLiteText);
                    }
                    databaseHandler.AddText(sqLiteText);
                    startActivity(new Intent(BlankMessageActivity.this, ShareMessageActivity.class));
                } else {
                    Toast.makeText(BlankMessageActivity.this, "Some error occured please try again.", Toast.LENGTH_SHORT).show();
                }
                Log.e("Textrepeated", "ModelTextSqLite");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPreExecute() {
            try {
                no = Integer.parseInt(text_repeat_number.getText().toString());
                progressDialog = ProgressDialog.show(BlankMessageActivity.this, "ProgressDialog", "Repeating text");
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
            database = new DatabaseTextRepeater(BlankMessageActivity.this);
            databaseHandler = new DatabaseRecentHandler(BlankMessageActivity.this);
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
