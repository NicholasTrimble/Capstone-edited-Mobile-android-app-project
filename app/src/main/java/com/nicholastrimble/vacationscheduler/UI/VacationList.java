package com.nicholastrimble.vacationscheduler.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nicholastrimble.vacationscheduler.R;
import com.nicholastrimble.vacationscheduler.database.Repository;
import com.nicholastrimble.vacationscheduler.entities.Vacation;

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    private VacationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        // Initialize adapter
        adapter = new VacationAdapter(this);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        repository = new Repository(getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load initial data
        List<Vacation> allVacations = repository.getmAllVacations();
        adapter.setVacations(allVacations);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);

        // Setup search functionality
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Vacation> filtered = repository.searchVacations(newText);
                adapter.setVacations(filtered);
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Vacation> allVacations = repository.getmAllVacations();
        adapter.setVacations(allVacations);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.mysample) {
            repository = new Repository(getApplication());
            Vacation vacation = new Vacation(0, "Panama", "Mariott", "09/03/23", "09/04/23");
            repository.insert(vacation);
            vacation = new Vacation(0, "China", "Hilton", "09/01/23", "09/14/23");
            repository.insert(vacation);
            return true;
        }
        return true;
    }
}