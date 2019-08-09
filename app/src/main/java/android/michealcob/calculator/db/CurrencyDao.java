package android.michealcob.calculator.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CurrencyDao {
    @Query("SELECT * FROM currency")
    List<Currency> getAll();

    @Insert
    void insert(Currency currency);

    @Delete
    void delete(Currency currency);

    @Update
    void update(Currency currency);

    @Query("DELETE FROM currency")
    void deleteAll();
}
