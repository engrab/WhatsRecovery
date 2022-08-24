package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdView;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.PreferencePurchase;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils;

public class DirectChatActivity extends AppCompatActivity implements ListCustomAdapter.ItemClickListener {

    String MyCountryCode = "92";
    public ArrayList<CountryCodeModel> codeModels;
    //    public ArrayList<String> CountryCode;
    private EditText edittext_phone, message_what;
    private Button start_direct_chat;
    private TextView text_spinner;
    private SearchView searchView;
    private ListView listView;
    private AlertDialog dialog;
    AdView adView;
    @Override
    protected void onDestroy() {
        if (adView != null){
            adView.destroy();
        }
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_chat);
        adView = AdmobUtils.showBanner(this, findViewById(R.id.llAdds));


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        InitArray();
        start_direct_chat = findViewById(R.id.start_direct_chat);
        edittext_phone = findViewById(R.id.edittext_phone);
        text_spinner = findViewById(R.id.text_spinner);
        message_what = findViewById(R.id.message_what);

        text_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDialog();
            }
        });


        start_direct_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//
                if (!TextUtils.isEmpty(edittext_phone.getText().toString())) {
                    // DirectChat(MyCountryCode, edittext_phone.getText().toString());

                    if (!TextUtils.isEmpty(message_what.getText().toString())) {
                        SendMessage(MyCountryCode, edittext_phone.getText().toString(), message_what.getText().toString());
                    } else {
                        message_what.setError("Write message First");
                    }
                } else {
                    edittext_phone.setError("Enter Phone number");
                }
            }
        });

    }

    private boolean whatsappInstalledOrNot() {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException ignore) {
        }
        return app_installed;
    }

    private void DirectChat(String CountryCode, String MobileNumber) {
        boolean isWhatsappInstalled = whatsappInstalledOrNot();
        if (isWhatsappInstalled) {
            Uri uri = Uri.parse("smsto:" + CountryCode + MobileNumber);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hai Good Morning");
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        } else {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(goToMarket);

        }
    }


    private void SendMessage(String CountryCode, String MobileNumber, String Message) {
        try {
            // String text = "This is a test";// Replace with your message.

            String toNumber = CountryCode + MobileNumber;

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + Message));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitArray() {

        codeModels = new ArrayList<>();
//        CountryCode = new ArrayList<>();
        codeModels.add(new CountryCodeModel("Afghanistan	", "93"));
        codeModels.add(new CountryCodeModel("Albania	", "355"));
        codeModels.add(new CountryCodeModel("Algeria	", "213"));
        codeModels.add(new CountryCodeModel("Andorra	", "376"));
        codeModels.add(new CountryCodeModel("Angola	", "244"));
        codeModels.add(new CountryCodeModel("Antarctica	", "672"));
        codeModels.add(new CountryCodeModel("Argentina	", "54"));
        codeModels.add(new CountryCodeModel("Armenia	", "374"));
        codeModels.add(new CountryCodeModel("Aruba	", "297"));
        codeModels.add(new CountryCodeModel("Australia	", "61"));
        codeModels.add(new CountryCodeModel("Austria	", "43"));
        codeModels.add(new CountryCodeModel("Azerbaijan	", "994"));
        codeModels.add(new CountryCodeModel("Bahrain	", "973"));
        codeModels.add(new CountryCodeModel("Bangladesh	", "880"));
        codeModels.add(new CountryCodeModel("Belarus	", "375"));
        codeModels.add(new CountryCodeModel("Belgium	", "32"));
        codeModels.add(new CountryCodeModel("Belize	", "501"));
        codeModels.add(new CountryCodeModel("Benin	", "229"));
        codeModels.add(new CountryCodeModel("Bhutan	", "975"));
        codeModels.add(new CountryCodeModel("Bolivia	", "591"));
        codeModels.add(new CountryCodeModel("Bosnia and Herzegovina	", "387"));
        codeModels.add(new CountryCodeModel("Botswana	", "267"));
        codeModels.add(new CountryCodeModel("Brazil	", "55"));
        codeModels.add(new CountryCodeModel("Brunei	", "673"));
        codeModels.add(new CountryCodeModel("Bulgaria	", "359"));
        codeModels.add(new CountryCodeModel("Burkina Faso	", "226"));
        codeModels.add(new CountryCodeModel("Burundi	", "257"));
        codeModels.add(new CountryCodeModel("Cambodia	", "855"));
        codeModels.add(new CountryCodeModel("Cameroon	", "237"));
        codeModels.add(new CountryCodeModel("Canada	", "1"));
        codeModels.add(new CountryCodeModel("Cape ", "Verde"));
        codeModels.add(new CountryCodeModel("Central African Republic	", "236"));
        codeModels.add(new CountryCodeModel("Chad	", "235"));
        codeModels.add(new CountryCodeModel("Chile	", "56"));
        codeModels.add(new CountryCodeModel("China	", "86"));
        codeModels.add(new CountryCodeModel("Christmas Island	", "61"));
        codeModels.add(new CountryCodeModel("Cocos Islands	", "61"));
        codeModels.add(new CountryCodeModel("Colombia	", "57"));
        codeModels.add(new CountryCodeModel("Comoros	", "269"));
        codeModels.add(new CountryCodeModel("Cook Islands	", "682"));
        codeModels.add(new CountryCodeModel("Costa Rica	", "506"));
        codeModels.add(new CountryCodeModel("Croatia	", "385"));
        codeModels.add(new CountryCodeModel("Cuba	", "53"));
        codeModels.add(new CountryCodeModel("Curacao	", "599"));
        codeModels.add(new CountryCodeModel("Cyprus	", "357"));
        codeModels.add(new CountryCodeModel("Czech Republic	", "420"));
        codeModels.add(new CountryCodeModel("Democratic Republic of the Congo	", "243"));
        codeModels.add(new CountryCodeModel("Denmark	", "45"));
        codeModels.add(new CountryCodeModel("Djibouti	", "253"));
        codeModels.add(new CountryCodeModel("East Timor	", "670"));
        codeModels.add(new CountryCodeModel("Ecuador	", "593"));
        codeModels.add(new CountryCodeModel("Egypt	", "20"));
        codeModels.add(new CountryCodeModel("El Salvador	", "503"));
        codeModels.add(new CountryCodeModel("Equatorial Guinea	", "240"));
        codeModels.add(new CountryCodeModel("Eritrea	", "291"));
        codeModels.add(new CountryCodeModel("Estonia	", "372"));
        codeModels.add(new CountryCodeModel("Ethiopia	", "251"));
        codeModels.add(new CountryCodeModel("Falkland Islands	", "500"));
        codeModels.add(new CountryCodeModel("Faroe Islands	", "298"));
        codeModels.add(new CountryCodeModel("Fiji	", "679"));
        codeModels.add(new CountryCodeModel("Finland	", "358"));
        codeModels.add(new CountryCodeModel("France	", "33"));
        codeModels.add(new CountryCodeModel("French Polynesia	", "689"));
        codeModels.add(new CountryCodeModel("Gabon	", "241"));
        codeModels.add(new CountryCodeModel("Gambia	", "220"));
        codeModels.add(new CountryCodeModel("Georgia	", "995"));
        codeModels.add(new CountryCodeModel("Germany	", "49"));
        codeModels.add(new CountryCodeModel("Ghana	", "233"));
        codeModels.add(new CountryCodeModel("Gibraltar	", "350"));
        codeModels.add(new CountryCodeModel("Greece	", "30"));
        codeModels.add(new CountryCodeModel("Greenland	", "299"));
        codeModels.add(new CountryCodeModel("Guatemala	", "502"));
        codeModels.add(new CountryCodeModel("Guinea	", "224"));
        codeModels.add(new CountryCodeModel("Guinea-Bissau	", "245"));
        codeModels.add(new CountryCodeModel("Guyana	", "592"));
        codeModels.add(new CountryCodeModel("Haiti	", "509"));
        codeModels.add(new CountryCodeModel("Honduras	", "504"));
        codeModels.add(new CountryCodeModel("Hong Kong	", "852"));
        codeModels.add(new CountryCodeModel("Hungary	", "36"));
        codeModels.add(new CountryCodeModel("Iceland	", "354"));
        codeModels.add(new CountryCodeModel("India	", "91"));
        codeModels.add(new CountryCodeModel("Indonesia", "360"));
        codeModels.add(new CountryCodeModel("Iran	", "98"));
        codeModels.add(new CountryCodeModel("Iraq	", "964"));
        codeModels.add(new CountryCodeModel("Ireland	", "353"));
        codeModels.add(new CountryCodeModel("Israel	", "972"));
        codeModels.add(new CountryCodeModel("Italy	", "39"));
        codeModels.add(new CountryCodeModel("Ivory Coast	", "225"));
        codeModels.add(new CountryCodeModel("Japan	", "81"));
        codeModels.add(new CountryCodeModel("Jordan	", "962"));
        codeModels.add(new CountryCodeModel("Kazakhstan	", "7"));
        codeModels.add(new CountryCodeModel("Kenya	", "254"));
        codeModels.add(new CountryCodeModel("Kiribati	", "686"));
        codeModels.add(new CountryCodeModel("Kosovo	", "383"));
        codeModels.add(new CountryCodeModel("Kuwait	", "965"));
        codeModels.add(new CountryCodeModel("Kyrgyzstan	", "996"));
        codeModels.add(new CountryCodeModel("Laos	", "856"));
        codeModels.add(new CountryCodeModel("Latvia	", "371"));
        codeModels.add(new CountryCodeModel("Lebanon	", "961"));
        codeModels.add(new CountryCodeModel("Lesotho	", "266"));
        codeModels.add(new CountryCodeModel("Liberia	", "231"));
        codeModels.add(new CountryCodeModel("Libya	", "218"));
        codeModels.add(new CountryCodeModel("Liechtenstein	", "423"));
        codeModels.add(new CountryCodeModel("Lithuania	", "370"));
        codeModels.add(new CountryCodeModel("Luxembourg	", "352"));
        codeModels.add(new CountryCodeModel("Macau	", "853"));
        codeModels.add(new CountryCodeModel("Macedonia	", "389"));
        codeModels.add(new CountryCodeModel("Madagascar	", "261"));
        codeModels.add(new CountryCodeModel("Malawi	", "265"));
        codeModels.add(new CountryCodeModel("Malaysia	", "60"));
        codeModels.add(new CountryCodeModel("Maldives	", "960"));
        codeModels.add(new CountryCodeModel("Mali	", "223"));
        codeModels.add(new CountryCodeModel("Malta	", "356"));
        codeModels.add(new CountryCodeModel("Marshall Islands	", "692"));
        codeModels.add(new CountryCodeModel("Mauritania	", "222"));
        codeModels.add(new CountryCodeModel("Mauritius	", "230"));
        codeModels.add(new CountryCodeModel("Mayotte	", "262"));
        codeModels.add(new CountryCodeModel("Mexico	", "52"));
        codeModels.add(new CountryCodeModel("Micronesia	", "691"));
        codeModels.add(new CountryCodeModel("Moldova	", "373"));
        codeModels.add(new CountryCodeModel("Monaco	", "377"));
        codeModels.add(new CountryCodeModel("Mongolia	", "976"));
        codeModels.add(new CountryCodeModel("Montenegro	", "382"));
        codeModels.add(new CountryCodeModel("Morocco	", "212"));
        codeModels.add(new CountryCodeModel("Mozambique	", "258"));
        codeModels.add(new CountryCodeModel("Myanmar	", "95"));
        codeModels.add(new CountryCodeModel("Namibia	", "264"));
        codeModels.add(new CountryCodeModel("Nauru	", "674"));
        codeModels.add(new CountryCodeModel("Nepal	", "977"));
        codeModels.add(new CountryCodeModel("Netherlands	", "31"));
        codeModels.add(new CountryCodeModel("Netherlands Antilles	", "599"));
        codeModels.add(new CountryCodeModel("New Caledonia	", "687"));
        codeModels.add(new CountryCodeModel("New Zealand	", "64"));
        codeModels.add(new CountryCodeModel("Nicaragua	", "505"));
        codeModels.add(new CountryCodeModel("Niger	", "227"));
        codeModels.add(new CountryCodeModel("Nigeria	", "234"));
        codeModels.add(new CountryCodeModel("Niue	", "683"));
        codeModels.add(new CountryCodeModel("North Korea	", "850"));
        codeModels.add(new CountryCodeModel("Norway	", "47"));
        codeModels.add(new CountryCodeModel("Oman	", "968"));
        codeModels.add(new CountryCodeModel("Pakistan	", "92"));
        codeModels.add(new CountryCodeModel("Palau	", "680"));
        codeModels.add(new CountryCodeModel("Palestine	", "970"));
        codeModels.add(new CountryCodeModel("Panama	", "507"));
        codeModels.add(new CountryCodeModel("Papua New Guinea	", "675"));
        codeModels.add(new CountryCodeModel("Paraguay	", "59"));
        codeModels.add(new CountryCodeModel("Peru	", "51"));
        codeModels.add(new CountryCodeModel("Philippines	", "63"));
        codeModels.add(new CountryCodeModel("Pitcairn	", "64"));
        codeModels.add(new CountryCodeModel("Poland	", "48"));
        codeModels.add(new CountryCodeModel("Portugal	", "351"));
        codeModels.add(new CountryCodeModel("Qatar	", "974"));
        codeModels.add(new CountryCodeModel("Republic of the Congo	", "242"));
        codeModels.add(new CountryCodeModel("Reunion	", "262"));
        codeModels.add(new CountryCodeModel("Romania	", "40"));
        codeModels.add(new CountryCodeModel("Russia	", "7"));
        codeModels.add(new CountryCodeModel("Rwanda	", "250"));
        codeModels.add(new CountryCodeModel("Saint Barthelemy	", "590"));
        codeModels.add(new CountryCodeModel("Saint Helena	", "290"));
        codeModels.add(new CountryCodeModel("Saint Martin	", "590"));
        codeModels.add(new CountryCodeModel("Samoa	", "685"));
        codeModels.add(new CountryCodeModel("San Marino	", "378"));
        codeModels.add(new CountryCodeModel("Saudi Arabia	", "966"));
        codeModels.add(new CountryCodeModel("Senegal	", "221"));
        codeModels.add(new CountryCodeModel("Serbia	", "381"));
        codeModels.add(new CountryCodeModel("Seychelles	", "248"));
        codeModels.add(new CountryCodeModel("Sierra Leone	", "232"));
        codeModels.add(new CountryCodeModel("Singapore	", "65"));
        codeModels.add(new CountryCodeModel("Slovakia	", "421"));
        codeModels.add(new CountryCodeModel("Slovenia	", "386"));
        codeModels.add(new CountryCodeModel("Solomon Islands	", "677"));
        codeModels.add(new CountryCodeModel("Somalia	", "252"));
        codeModels.add(new CountryCodeModel("South Africa	", "27"));
        codeModels.add(new CountryCodeModel("South Korea	", "82"));
        codeModels.add(new CountryCodeModel("South Sudan	", "211"));
        codeModels.add(new CountryCodeModel("Spain	", "34"));
        codeModels.add(new CountryCodeModel("Sri Lanka	", "94"));
        codeModels.add(new CountryCodeModel("Sudan	", "249"));
        codeModels.add(new CountryCodeModel("Suriname	", "597"));
        codeModels.add(new CountryCodeModel("Svalbard 	", "47"));
        codeModels.add(new CountryCodeModel("Swaziland	", "268"));
        codeModels.add(new CountryCodeModel("Sweden	", "46"));
        codeModels.add(new CountryCodeModel("Switzerland	", "41"));
        codeModels.add(new CountryCodeModel("Syria	", "963"));
        codeModels.add(new CountryCodeModel("Taiwan	", "886"));
        codeModels.add(new CountryCodeModel("Tajikistan	", "992"));
        codeModels.add(new CountryCodeModel("Tanzania	", "255"));
        codeModels.add(new CountryCodeModel("Thailand	", "66"));
        codeModels.add(new CountryCodeModel("Togo	", "228"));
        codeModels.add(new CountryCodeModel("Tokelau	", "690"));
        codeModels.add(new CountryCodeModel("Tonga	", "676"));
        codeModels.add(new CountryCodeModel("Tunisia	", "216"));
        codeModels.add(new CountryCodeModel("Turkey	", "90"));
        codeModels.add(new CountryCodeModel("Turkmenistan	", "993"));
        codeModels.add(new CountryCodeModel("U.S. Virgin Islands	", "1"));
        codeModels.add(new CountryCodeModel("Uganda	", "256"));
        codeModels.add(new CountryCodeModel("Ukraine	", "380"));
        codeModels.add(new CountryCodeModel("United Arab Emirates	", "971"));
        codeModels.add(new CountryCodeModel("United Kingdom	", "44"));
        codeModels.add(new CountryCodeModel("United States	", "1"));
        codeModels.add(new CountryCodeModel("Uruguay	", "598"));
        codeModels.add(new CountryCodeModel("Uzbekistan	", "998"));
        codeModels.add(new CountryCodeModel("Vanuatu	", "678"));
        codeModels.add(new CountryCodeModel("Vatican	", "379"));
        codeModels.add(new CountryCodeModel("Venezuela	", "58"));
        codeModels.add(new CountryCodeModel("Vietnam	", "84"));
        codeModels.add(new CountryCodeModel("Wallis and Futuna	", "681"));
        codeModels.add(new CountryCodeModel("Western Sahara	", "212"));
        codeModels.add(new CountryCodeModel("Yemen	", "967"));
        codeModels.add(new CountryCodeModel("Zambia	", "260"));
        codeModels.add(new CountryCodeModel("Zimbabwe	", "263"));


//        //............................................
//        CountryCode.add("+93");
//        CountryCode.add("+355");
//        CountryCode.add("+213");
//        CountryCode.add("+376");
//        CountryCode.add("+244");
//        CountryCode.add("+672");
//        CountryCode.add("+54");
//        CountryCode.add("+374");
//        CountryCode.add("+297");
//        CountryCode.add("+61");
//        CountryCode.add("+43");
//        CountryCode.add("+994");
//        CountryCode.add("+973");
//        CountryCode.add("+880");
//        CountryCode.add("+375");
//        CountryCode.add("+32");
//        CountryCode.add("+501");
//        CountryCode.add("+229");
//        CountryCode.add("+975");
//        CountryCode.add("+591");
//        CountryCode.add("+387");
//        CountryCode.add("+267");
//        CountryCode.add("+55");
//        CountryCode.add("+673");
//        CountryCode.add("+359");
//        CountryCode.add("+226");
//        CountryCode.add("+257");
//        CountryCode.add("+855");
//        CountryCode.add("+237");
//        CountryCode.add("+1");
//        CountryCode.add("+236");
//        CountryCode.add("+235");
//        CountryCode.add("+56");
//        CountryCode.add("+86");
//        CountryCode.add("+61");
//        CountryCode.add("+61");
//        CountryCode.add("+57");
//        CountryCode.add("+269");
//        CountryCode.add("+682");
//        CountryCode.add("+506");
//        CountryCode.add("+385");
//        CountryCode.add("+53");
//        CountryCode.add("+599");
//        CountryCode.add("+357");
//        CountryCode.add("+420");
//        CountryCode.add("+243");
//        CountryCode.add("+45");
//        CountryCode.add("+253");
//        CountryCode.add("+670");
//        CountryCode.add("+593");
//        CountryCode.add("+20");
//        CountryCode.add("+503");
//        CountryCode.add("+240");
//        CountryCode.add("+291");
//        CountryCode.add("+372");
//        CountryCode.add("+251");
//        CountryCode.add("+500");
//        CountryCode.add("+298");
//        CountryCode.add("+679");
//        CountryCode.add("+358");
//        CountryCode.add("+33");
//        CountryCode.add("+689");
//        CountryCode.add("+241");
//        CountryCode.add("+220");
//        CountryCode.add("+995");
//        CountryCode.add("+49");
//        CountryCode.add("+233");
//        CountryCode.add("+350");
//        CountryCode.add("+30");
//        CountryCode.add("+299");
//        CountryCode.add("+502");
//        CountryCode.add("+224");
//        CountryCode.add("+245");
//        CountryCode.add("+592");
//        CountryCode.add("+509");
//        CountryCode.add("+504");
//        CountryCode.add("+852");
//        CountryCode.add("+36");
//        CountryCode.add("+354");
//        CountryCode.add("+91");
//        CountryCode.add("+360");
//        CountryCode.add("+98");
//        CountryCode.add("+964");
//        CountryCode.add("+353");
//        CountryCode.add("+972");
//        CountryCode.add("+39");
//        CountryCode.add("+225");
//        CountryCode.add("+81");
//        CountryCode.add("+962");
//        CountryCode.add("+7");
//        CountryCode.add("+254");
//        CountryCode.add("+686");
//        CountryCode.add("+383");
//        CountryCode.add("+965");
//        CountryCode.add("+996");
//        CountryCode.add("+856");
//        CountryCode.add("+371");
//        CountryCode.add("+961");
//        CountryCode.add("+266");
//        CountryCode.add("+231");
//        CountryCode.add("+218");
//        CountryCode.add("+423");
//        CountryCode.add("+370");
//        CountryCode.add("+352");
//        CountryCode.add("+853");
//        CountryCode.add("+389");
//        CountryCode.add("+261");
//        CountryCode.add("+265");
//        CountryCode.add("+60");
//        CountryCode.add("+960");
//        CountryCode.add("+223");
//        CountryCode.add("+356");
//        CountryCode.add("+692");
//        CountryCode.add("+222");
//        CountryCode.add("+230");
//        CountryCode.add("+262");
//        CountryCode.add("+52");
//        CountryCode.add("+691");
//        CountryCode.add("+373");
//        CountryCode.add("+377");
//        CountryCode.add("+976");
//        CountryCode.add("+382");
//        CountryCode.add("+212");
//        CountryCode.add("+258");
//        CountryCode.add("+95");
//        CountryCode.add("+264");
//        CountryCode.add("+674");
//        CountryCode.add("+977");
//        CountryCode.add("+31");
//        CountryCode.add("+599");
//        CountryCode.add("+687");
//        CountryCode.add("+64");
//        CountryCode.add("+505");
//        CountryCode.add("+227");
//        CountryCode.add("+234");
//        CountryCode.add("+683");
//        CountryCode.add("+850");
//        CountryCode.add("+47");
//        CountryCode.add("+968");
//        CountryCode.add("+92");
//        CountryCode.add("+680");
//        CountryCode.add("+970");
//        CountryCode.add("+507");
//        CountryCode.add("+675");
//        CountryCode.add("+59");
//        CountryCode.add("+51");
//        CountryCode.add("+63");
//        CountryCode.add("+64");
//        CountryCode.add("+48");
//        CountryCode.add("+351");
//        CountryCode.add("+974");
//        CountryCode.add("+242");
//        CountryCode.add("+262");
//        CountryCode.add("+40");
//        CountryCode.add("+7");
//        CountryCode.add("+250");
//        CountryCode.add("+590");
//        CountryCode.add("+290");
//        CountryCode.add("+590");
//        CountryCode.add("+685");
//        CountryCode.add("+378");
//        CountryCode.add("+966");
//        CountryCode.add("+221");
//        CountryCode.add("+381");
//        CountryCode.add("+248");
//        CountryCode.add("+232");
//        CountryCode.add("+65");
//        CountryCode.add("+421");
//        CountryCode.add("+386");
//        CountryCode.add("+677");
//        CountryCode.add("+252");
//        CountryCode.add("+27");
//        CountryCode.add("+82");
//        CountryCode.add("+211");
//        CountryCode.add("+34");
//        CountryCode.add("+94");
//        CountryCode.add("+249");
//        CountryCode.add("+597");
//        CountryCode.add("+47");
//        CountryCode.add("+268");
//        CountryCode.add("+46");
//        CountryCode.add("+41");
//        CountryCode.add("+963");
//        CountryCode.add("+886");
//        CountryCode.add("+992");
//        CountryCode.add("+255");
//        CountryCode.add("+66");
//        CountryCode.add("+228");
//        CountryCode.add("+690");
//        CountryCode.add("+676");
//        CountryCode.add("+216");
//        CountryCode.add("+90");
//        CountryCode.add("+993");
//        CountryCode.add("+1");
//        CountryCode.add("+256");
//        CountryCode.add("+380");
//        CountryCode.add("+971");
//        CountryCode.add("+44");
//        CountryCode.add("+1");
//        CountryCode.add("+598");
//        CountryCode.add("+998");
//        CountryCode.add("+678");
//        CountryCode.add("+379");
//        CountryCode.add("+58");
//        CountryCode.add("+84");
//        CountryCode.add("+681");
//        CountryCode.add("+212");
//        CountryCode.add("+967");
//        CountryCode.add("+260");
//        CountryCode.add("+263");
    }

    public void SearchDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(DirectChatActivity.this).inflate(R.layout.code_search, null);
            builder.setView(view);
            builder.setCancelable(true);
            dialog = builder.create();
            dialog.show();


            searchView = view.findViewById(R.id.mSeacrch);
            listView = view.findViewById(R.id.code_list);

            final ListCustomAdapter adapter = new ListCustomAdapter(this, codeModels, this);
            listView.setAdapter(adapter);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    adapter.getFilter().filter(query);
                    return false;
                }
            });

        } catch (Exception ignored) {
        }
    }


    @Override
    public void onItemClick(View v, String code) {
        MyCountryCode = code;
        text_spinner.setText(String.format("+%s", code));
        if (dialog != null) {
            dialog.dismiss();
        }

    }
}
