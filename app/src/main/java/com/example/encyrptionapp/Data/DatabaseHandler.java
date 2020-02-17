package com.example.encyrptionapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.encyrptionapp.Model.Encryption;
import com.example.encyrptionapp.Util.database_Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context){
        super(context, database_Util.DATABASE_NAME,null,database_Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_KEY_TABLE= "CREATE TABLE " + database_Util.KEY_TABLE + "("
                + database_Util.KEY_ID + " INTEGER PRIMARY KEY,"
                + database_Util.KEY_KEY + " TEXT,"
                + database_Util.KEY_VALUE + " TEXT,"
                + database_Util.KEY_ENCRYPTED + " TEXT" + ")";
        db.execSQL(CREATE_KEY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf("DROP TABLE IF EXISTS");
        db.execSQL(DROP_TABLE, new String[]{database_Util.DATABASE_NAME});

        onCreate(db);
    }

    public void addEncryption(Encryption e)
    {
        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(database_Util.KEY_KEY,e.getKey());
        values.put(database_Util.KEY_VALUE,e.getValue());
        values.put(database_Util.KEY_ENCRYPTED,e.getEncrypted());

        db.insert(database_Util.KEY_TABLE,null,values);
        db.close();
    }

    public Encryption getEncryption(int id)
    {
        SQLiteDatabase db= this.getReadableDatabase();

        Cursor cursor =db.query(database_Util.KEY_TABLE,new String[]{database_Util.KEY_ID,database_Util.KEY_KEY,database_Util.KEY_VALUE,database_Util.KEY_ENCRYPTED},database_Util.KEY_ID +"=?",
                new String[]{String.valueOf(id)},null,null,null);

        if(cursor !=null)
        {
            cursor.moveToFirst();

            Encryption e = new Encryption(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3));

            return e;
        }else
        {
            db.close();
            return null;
        }
    }

    public List<Encryption> getAllEncryption()
    {
        List<Encryption> encryptionList = new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();

        String query="SELECT * FROM encryption";

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst())
        {
            do{
                Encryption e = new Encryption(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                    encryptionList.add(e);
            }while(cursor.moveToNext());
        }
        return encryptionList;
    }
}
