package android.michealcob.calculator.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Currency.class}, version = 1)
public abstract class CurrencyDB extends RoomDatabase {
    public abstract CurrencyDao currencyDao();
}
