package edu.mnstate.cw3967me.lab10;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    EditText etUrl;
    ListView listContact;
    WebView webView;
    EditText etPhoneNum;
    ArrayList<String> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUrl = (EditText) findViewById(R.id.etUrl);
        //get references to the web view and progress
        webView = (WebView) findViewById(R.id.webView);
        listContact = (ListView) findViewById(R.id.listContact);
        etPhoneNum = (EditText) findViewById(R.id.etPhoneNum);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        etPhoneNum = (EditText) findViewById(R.id.etPhoneNum);
    }

    public void displayMessage(View V) {
        //create the intent
        Intent myIntent = new Intent(this, Activity2.class);
        //start the intent
        this.startActivity(myIntent);
    }

    public void displayWebsite(View v) {
        Editable url = etUrl.getText();
        String sUrl = url.toString();
        //Load the content from the specified URL into the web view
        webView.loadUrl(sUrl);
    }

    // The column index for the _ID column
    private static final int CONTACT_ID_INDEX = 0;
    // The column index for the LOOKUP_KEY column
    private static final int LOOKUP_KEY_INDEX = 1;

    public void showContact(View v) {

        Uri data = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        //ContentUrisメソッドを使ってベースとなるURIとIDから該当レコードのUriを生成します
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(data, null, null, null, null);
        cursor.moveToFirst();
        getColumnData(cursor);
        cursor.close();
        // Adapterの作成
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.show_contacts, contacts);
        // ListViewにAdapterを関連付ける
        listContact.setAdapter(adapter);
    }

    private void getColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int nameColumn = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int phoneColumn = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1);
            do {
                contacts.add(cur.getString(nameColumn) + "  " + cur.getString(phoneColumn));
            } while (cur.moveToNext());
        }
    }

    public void viewPhoneDialer(View v) {
        Editable phoneNum = etPhoneNum.getText();
        String strPhoneNum = phoneNum.toString();
        Uri phoneNumber = Uri.parse("tel:"+strPhoneNum);
        Intent dialIntent = new Intent(Intent.ACTION_DIAL,phoneNumber);
        startActivity(dialIntent);
    }
}


