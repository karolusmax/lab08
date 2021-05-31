package pollub.ism.lab08;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PozycjaMagazynowaDAO {

    @Insert
    void insert(PozycjaMagazynowa pozycjaMagazynowa);

    @Update
    void update(PozycjaMagazynowa pozycjaMagazynowa);

    @Query("SELECT QUANTITY FROM Warzywniak WHERE NAME= :wybraneWarzywoNazwa")
    int findQuantityByName(String wybraneWarzywoNazwa);

    @Query("UPDATE Warzywniak SET QUANTITY =:wybraneWarzywoNowaIlosc WHERE NAME= :wybraneWarzywoNazwa")
    void updateQuantityByName(String wybraneWarzywoNazwa, int wybraneWarzywoNowaIlosc);

    @Query("UPDATE Warzywniak SET OLD_QUANTITY =:staraIlosc, QUANTITY =:nowaIlosc, data=:data, czas=:czas WHERE NAME =:wybraneWarzywoNazwa")
    void updateTimeDateAndQuantity(String wybraneWarzywoNazwa, int staraIlosc, int nowaIlosc, String data, String czas);

    @Query("SELECT COUNT(*) FROM Warzywniak")
    int size();

}
