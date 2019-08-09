package android.michealcob.calculator.db;

import android.content.Context;

import androidx.room.Room;

public class DBClient {
    private Context mCtx;
    private static DBClient mInstance;

    private CurrencyDB currencyDB;

    private DBClient(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        currencyDB = Room.databaseBuilder(mCtx, CurrencyDB.class, "Currency").build();
    }

    public static synchronized DBClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DBClient(mCtx);
        }
        return mInstance;
    }

    public CurrencyDB getAppDatabase() {
        return currencyDB;
    }
}
