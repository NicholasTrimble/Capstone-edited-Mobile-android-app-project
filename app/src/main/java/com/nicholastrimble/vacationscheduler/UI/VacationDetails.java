package com.nicholastrimble.vacationscheduler.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nicholastrimble.vacationscheduler.R;
import com.nicholastrimble.vacationscheduler.database.Repository;
import com.nicholastrimble.vacationscheduler.entities.Excursion;
import com.nicholastrimble.vacationscheduler.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import com.nicholastrimble.vacationscheduler.database.SecurePrefs;

public class VacationDetails extends AppCompatActivity {

    String title;
    String hotel;
    int vacationID;
    String setStartDate;
    String setEndDate;

    EditText editTitle;
    EditText editHotel;
    TextView editStartDate;
    TextView editEndDate;
    Repository repository;
    Vacation currentVacation;
    int numExcursions;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();

    List<Excursion> filteredExcursions = new ArrayList<>();

    Random rand = new Random();
    int numAlert = rand.nextInt(99999);

    // NEW: Input validation pattern
    private static final String INVALID_CHARS_PATTERN = ".*[;'\"><].*";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        repository = new Repository(getApplication());

        editTitle = findViewById(R.id.titletext);
        editHotel = findViewById(R.id.hoteltext);
        vacationID = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        hotel = getIntent().getStringExtra("hotel");
        setStartDate = getIntent().getStringExtra("startdate");
        setEndDate = getIntent().getStringExtra("enddate");
        editTitle.setText(title);
        editHotel.setText(hotel);
        numAlert = rand.nextInt(99999);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
            intent.putExtra("vacationID", vacationID);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (Excursion e: repository.getmAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setmExcursions(filteredExcursions);

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (setStartDate != null) {
            try {
                Date startDate = sdf.parse(setStartDate);
                Date endDate = sdf.parse(setEndDate);
                myCalendarStart.setTime(startDate);
                myCalendarEnd.setTime(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);

        editStartDate.setOnClickListener(view -> {
            String info = editStartDate.getText().toString();
            if (info.equals("")) info = setStartDate;
            try {
                myCalendarStart.setTime(sdf.parse(info));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            new DatePickerDialog(VacationDetails.this, startDate, myCalendarStart
                    .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                    myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
        });

        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        };

        editEndDate.setOnClickListener(view -> {
            String info = editEndDate.getText().toString();
            if (info.equals("")) info = setEndDate;
            try {
                myCalendarEnd.setTime(sdf.parse(info));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            new DatePickerDialog(VacationDetails.this, endDate, myCalendarEnd
                    .get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                    myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
        });

        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, month);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEnd();
            }
        };
    }

    // NEW: Input validation helper
    private boolean isValidInput(String input) {
        return !input.matches(INVALID_CHARS_PATTERN);
    }

    private void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editStartDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void updateLabelEnd() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editEndDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    private void generateReport() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US);
        String timestamp = sdf.format(new Date());

        StringBuilder report = new StringBuilder();
        report.append("VACATION REPORT\n\n");
        report.append("Generated: ").append(timestamp).append("\n\n");
        report.append("Title: ").append(editTitle.getText()).append("\n");
        report.append("Hotel: ").append(editHotel.getText()).append("\n");
        report.append("Dates: ").append(editStartDate.getText()).append(" to ").append(editEndDate.getText()).append("\n\n");
        report.append("EXCURSIONS:\n");

        for (Excursion e : filteredExcursions) {
            report.append("- ").append(e.getExcursionTitle()).append(" (").append(e.getExcursionDate()).append(")\n");
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Vacation Report");
        shareIntent.putExtra(Intent.EXTRA_TEXT, report.toString());
        startActivity(Intent.createChooser(shareIntent, "Share Report"));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.vacationsave) {
            String titleInput = editTitle.getText().toString();

            // NEW: Input validation check
            if (!isValidInput(titleInput)) {
                Toast.makeText(this, "Invalid characters detected (; ' \" < >)", Toast.LENGTH_LONG).show();
                return true;
            }

            if (titleInput.trim().isEmpty()) {
                editTitle.setError("Title is required");
                editTitle.requestFocus();
                return true;
            }

            String hotelInput = editHotel.getText().toString();
            if (hotelInput.trim().isEmpty()) {
                editHotel.setError("Hotel is required");
                editHotel.requestFocus();
                return true;
            }

            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            String startDateString = sdf.format(myCalendarStart.getTime());
            String endDateString = sdf.format(myCalendarEnd.getTime());

            try {
                Date startDate = sdf.parse(startDateString);
                Date endDate = sdf.parse(endDateString);
                if (endDate.before(startDate)) {
                    Toast.makeText(this, "End date must be AFTER start date", Toast.LENGTH_LONG).show();
                } else {
                    Vacation vacation;
                    if (vacationID == -1) {
                        if (repository.getmAllVacations().size() == 0) vacationID = 1;
                        else vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationId() + 1;
                        vacation = new Vacation(vacationID, titleInput, hotelInput, startDateString, endDateString);
                        repository.insert(vacation);
                        this.finish();
                    } else {
                        vacation = new Vacation(vacationID, titleInput, hotelInput, startDateString, endDateString);
                        repository.update(vacation);
                        this.finish();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (item.getItemId() == R.id.vacationdelete) {
            for (Vacation vac : repository.getmAllVacations()) {
                if (vac.getVacationId() == vacationID) currentVacation = vac;
            }
            numExcursions = 0;
            for (Excursion excursion : repository.getmAllExcursions()) {
                if (excursion.getVacationID() == vacationID) ++numExcursions;
            }
            if (numExcursions == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationTitle() + " was deleted", Toast.LENGTH_LONG).show();
                VacationDetails.this.finish();
            } else {
                Toast.makeText(VacationDetails.this, "Can't delete a vacation with excursions", Toast.LENGTH_LONG).show();
            }
        }

        if (item.getItemId() == R.id.alertstart) {
            String dateFromScreen = editStartDate.getText().toString();
            String alert = "Vacation " + title + " is starting";
            alertPicker(dateFromScreen, alert);
            return true;
        }

        if (item.getItemId() == R.id.alertend) {
            String dateFromScreen = editEndDate.getText().toString();
            String alert = "Vacation " + title + " is ending";
            alertPicker(dateFromScreen, alert);
            return true;
        }

        if (item.getItemId() == R.id.alertfull) {
            String dateFromScreen = editStartDate.getText().toString();
            String alert = "Vacation " + title + " is starting";
            alertPicker(dateFromScreen, alert);
            dateFromScreen = editEndDate.getText().toString();
            alert = "Vacation " + title + " is ending";
            alertPicker(dateFromScreen, alert);
            return true;
        }

        if (item.getItemId() == R.id.share) {
            Intent sentIntent = new Intent();
            sentIntent.setAction(Intent.ACTION_SEND);
            sentIntent.putExtra(Intent.EXTRA_TITLE, "Vacation Shared");
            StringBuilder shareData = new StringBuilder();
            shareData.append("Vacation title: ").append(editTitle.getText()).append("\n");
            shareData.append("Hotel name: ").append(editHotel.getText()).append("\n");
            shareData.append("Start Date: ").append(editStartDate.getText()).append("\n");
            shareData.append("End Date: ").append(editEndDate.getText()).append("\n");
            for (int i = 0; i < filteredExcursions.size(); i++) {
                shareData.append("Excursion ").append(i + 1).append(": ").append(filteredExcursions.get(i).getExcursionTitle()).append("\n");
                shareData.append("Excursion ").append(i + 1).append(" Date: ").append(filteredExcursions.get(i).getExcursionDate()).append("\n");
            }
            sentIntent.putExtra(Intent.EXTRA_TEXT, shareData.toString());
            sentIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sentIntent, null);
            startActivity(shareIntent);
            return true;
        }

        if (item.getItemId() == R.id.generate_report) {
            generateReport();
            return true;
        }

        return true;
    }

    public void alertPicker(String dateFromScreen, String alert) {
        try {
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            long triggerTime = sdf.parse(dateFromScreen).getTime();

            // Store securely
            SecurePrefs.saveAlertTime(this, "alert_" + numAlert, triggerTime);

            Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
            intent.putExtra("key", alert);
            PendingIntent sender = PendingIntent.getBroadcast(
                    VacationDetails.this,
                    numAlert,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
            );
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
            numAlert = rand.nextInt(99999);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to set alert", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e: repository.getmAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setmExcursions(filteredExcursions);
        updateLabelStart();
        updateLabelEnd();
    }

}