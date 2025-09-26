package com.nicholastrimble.vacationscheduler.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.nicholastrimble.vacationscheduler.entities.Vacation;
import java.util.List;

@Dao
public interface VacationDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM vacations ORDER BY vacationId ASC")
    List<Vacation> getAllVacations();

    @Query("SELECT * FROM vacations WHERE vacationTitle LIKE :query")
    List<Vacation> searchVacations(String query);

    @Query("SELECT * FROM vacations LIMIT :pageSize OFFSET :pageIndex * :pageSize")
    List<Vacation> getVacationsPaginated(int pageSize, int pageIndex);
}