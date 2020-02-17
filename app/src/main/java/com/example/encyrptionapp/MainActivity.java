package com.example.encyrptionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.encyrptionapp.Data.DatabaseHandler;
import com.example.encyrptionapp.Model.Encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.Normalizer;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    public EditText input;
    public TextView output;
    public EditText inputKey;
    public String outputString;
    public List<Encryption> encryptionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void encryptText(View view) {
        input =findViewById(R.id.inputCleartext);
        inputKey=findViewById(R.id.password);
        output =findViewById(R.id.encryptedOutput);


        DatabaseHandler db = new DatabaseHandler(MainActivity.this);

        encryptionList = db.getAllEncryption();

        boolean encryptionExists =false;

        try {
            outputString=encrypt(input.getText().toString(),inputKey.getText().toString());
            Encryption eo = new Encryption();
            for(Encryption e : encryptionList)
            {
                if(e.getKey().equals(inputKey.getText().toString()) && e.getValue().equals(input.getText().toString()))
                {
                   // outputString=encrypt(e.getValue(),e.getKey());
                    Toast.makeText(MainActivity.this,"Already Exists", Toast.LENGTH_SHORT).show();
                    encryptionExists=true;
                }
            }
            if(!encryptionExists)
            {
                eo.setValue(input.getText().toString());
                eo.setKey(inputKey.getText().toString());
                eo.setEncrypted(outputString);
                db.addEncryption(eo);
                output.setText(outputString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decryptText(View view) {


        DatabaseHandler db = new DatabaseHandler(MainActivity.this);

        encryptionList = db.getAllEncryption();

        boolean isDecrypted=false;
        try {
            for(Encryption e : encryptionList)
            {
                if(e.getKey().equals(inputKey.getText().toString()))
                {
                    outputString=decrypt(input.getText().toString(),e.getKey());
                    if(e.getValue().equals(outputString))
                    {
                        output.setText(outputString);
                        isDecrypted=true;
                    }
                }
            }
            if(!isDecrypted)
            {
                Toast.makeText(MainActivity.this,"Can't decrypt message,Not Existant", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(MainActivity.this,"Can't decrypt message", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String decrypt(String outputString, String privateKey)throws Exception {
        SecretKeySpec key=generateKey(privateKey);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE,key);
        byte[]decodedValue=Base64.decode(outputString,Base64.DEFAULT);
        byte[]decValue=c.doFinal(decodedValue);
        String decryptedValue=new String(decValue);
        return decryptedValue;

    }

    private String encrypt(String Data, String privateKey)throws Exception {
        SecretKeySpec key = generateKey(privateKey);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[]encVal=c.doFinal(Data.getBytes());
        String encryptedValue= Base64.encodeToString(encVal,Base64.DEFAULT);
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String privateKey)throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[]bytes=privateKey.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key=digest.digest();
        SecretKeySpec sks=new SecretKeySpec(key,"AES");
        return sks;
    }

}
