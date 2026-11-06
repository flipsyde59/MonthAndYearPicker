package com.whiteelephant.monthpickersample;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setNormalPicker();
    }

    private void setNormalPicker() {
        setContentView(R.layout.activity_main);
        final Calendar today = Calendar.getInstance();
        findViewById(R.id.month_picker).setOnClickListener(v -> {
            MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(MainActivity.this, (selectedMonth, selectedYear) -> {
                Log.d(TAG, "selectedMonth : " + selectedMonth + " selectedYear : " + selectedYear);
                Toast.makeText(MainActivity.this, "Date set with month" + selectedMonth + " year " + selectedYear, Toast.LENGTH_SHORT).show();
            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

            builder.setActivatedMonth(Calendar.JULY)
                    .setMinYear(1990)
                    .setActivatedYear(2017)
                    .setMaxYear(2030)
                    .setMinMonth(Calendar.FEBRUARY)
                    .setTitle("Select trading month")
                    .setMonthRange(Calendar.FEBRUARY, Calendar.NOVEMBER)
                    .setOnMonthChangedListener(selectedMonth -> Log.d(TAG, "Selected month : " + selectedMonth))
                    .setOnYearChangedListener(selectedYear -> Log.d(TAG, "Selected year : " + selectedYear))
                    .build()
                    .show();

        });

        findViewById(R.id.date_picker).setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, null, 2017,
                    cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            dialog.show();
        });
    }

    private void chooseMonthOnly() {
        setContentView(R.layout.activity_choose_month);

        findViewById(R.id.choose_month).setOnClickListener(v -> {
            MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(MainActivity.this, (selectedMonth, selectedYear) -> {

            }, /* activated number in year */ 3, 5);

            builder.showMonthOnly()
                    .build()
                    .show();
        });
    }

    int choosenYear = 2017;

    private void chooseYearOnly() {
        setContentView(R.layout.activity_choose_year);

        final TextView year = findViewById(R.id.year);
        findViewById(R.id.choose_year).setOnClickListener(v -> {
            MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(MainActivity.this, (selectedMonth, selectedYear) -> {
                year.setText(String.format(Locale.getDefault(), "%d", selectedYear));
                choosenYear = selectedYear;
            }, choosenYear, 0);

            builder.showYearOnly()
                    .setYearRange(1990, 2030)
                    .build()
                    .show();
        });
    }

    private void setBottleView() {

        setContentView(R.layout.activity_bottle);

        LinearLayout chooseQty = findViewById(R.id.select_quantity);
        final TextView qty = findViewById(R.id.qty);


        chooseQty.setOnClickListener(v -> {
            MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(MainActivity.this, (selectedMonth, selectedYear) -> qty.setText(String.format(Locale.getDefault(), "%d", selectedYear)), /* activated number in year */ 3, 0);

            builder.setActivatedMonth(Calendar.JULY)
                    .setTitle("Select Quantity")
                    .setYearRange(1, 15)
                    .showYearOnly()
                    .build()
                    .show();
        });

    }

}
