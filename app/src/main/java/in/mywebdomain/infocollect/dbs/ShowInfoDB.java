package in.mywebdomain.infocollect.dbs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import in.mywebdomain.infocollect.entities.TheClient;

public class ShowInfoDB extends SQLiteOpenHelper {
    private final String TABLE_NAME = "client_info";
    private final String FNAME = "fname";
    private final String PNO = "pno";
    private final String DOB = "dob";
    private final String ADDRESS = "address";
    private final String LAT = "lat";
    private final String LNG = "lng";
    String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            FNAME + " VARCHAR(18), " +
            PNO + " VARCHAR(10), " +
            DOB + " VARCHAR(10), " +
            ADDRESS + " VARCHAR(64), " +
            LAT + " VARCHAR(20), " +
            LNG + " VARCHAR(20)" +
            ");";


    public ShowInfoDB(Context context) { super(context, "info_db", null, 1); }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }

    public boolean insert(TheClient theClient){
        clearDB();
        ContentValues cv = theClient.toContentValues();
        SQLiteDatabase database = getWritableDatabase();
        boolean b = database.insert(TABLE_NAME, null, cv)>0;
        database.close();
        return b;
    }

    public void insertAll(List<TheClient> list){
        ContentValues cv = null;
        SQLiteDatabase database = getWritableDatabase();
        for (TheClient client : list){
            cv = client.toContentValues();
            database.insert(TABLE_NAME, null, cv);
        }
    }

    public List<TheClient> getAllData(){
        List<TheClient> theClientList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        while(cursor.moveToNext()) {
            theClientList.add(new TheClient(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(5), cursor.getString(6), cursor.getString(4)));
        }
        cursor.close();
        return theClientList;
    }

    public String getPNO(){
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT " + PNO + " FROM " + TABLE_NAME, null);
        String pno = null;
        if(cursor.moveToNext()) {
            pno = cursor.getString(1);
        }
        return pno;
    }

    private void clearDB(){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DROP TABLE " + TABLE_NAME);
        database.execSQL(sql);
        database.close();
    }
}
