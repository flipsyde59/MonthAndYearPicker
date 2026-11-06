package com.whiteelephant.monthpicker;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.prem.firstpitch.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class MonthPickerView extends FrameLayout {

    YearPickerView _yearView;
    ListView _monthList;
    static int _minYear = 1900, _maxYear = Calendar.getInstance().get(Calendar.YEAR);
    MonthViewAdapter _monthViewAdapter;
    TextView _month, _year, _title;
    Context _context;
    int _headerFontColorSelected, _headerFontColorNormal;
    boolean _showMonthOnly;
    int _selectedMonth, _selectedYear;
    MonthPickerDialog.OnYearChangedListener _onYearChanged;
    MonthPickerDialog.OnMonthChangedListener _onMonthChanged;
    OnDateSet _onDateSet;
    OnCancel _onCancel;
    private final String[] _monthNames;

    public MonthPickerView(Context context) {
        this(context, null);
        _context = context;
    }

    public MonthPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        _context = context;
    }

    public MonthPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _context = context;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.month_picker_view, this);
        _monthNames = new DateFormatSymbols(Locale.getDefault()).getShortMonths();

        int headerBgColor;
        int monthBgColor;
        int headerTitleColor;
        int actionButtonColor;
        HashMap<String, Integer> map;
        try (TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MonthPickerView,
                defStyleAttr, 0)) {

            headerBgColor = a.getColor(R.styleable.MonthPickerView_headerBgColor, 0);
            _headerFontColorNormal = a.getColor(R.styleable.MonthPickerView_headerFontColorNormal, 0);
            _headerFontColorSelected = a.getColor(R.styleable.MonthPickerView_headerFontColorSelected, 0);
            monthBgColor = a.getColor(R.styleable.MonthPickerView_monthBgColor, 0);
            int monthBgSelectedColor = a.getColor(R.styleable.MonthPickerView_monthBgSelectedColor, 0);
            int monthFontColorNormal = a.getColor(R.styleable.MonthPickerView_monthFontColorNormal, 0);
            int monthFontColorSelected = a.getColor(R.styleable.MonthPickerView_monthFontColorSelected, 0);
            int monthFontColorDisabled = a.getColor(R.styleable.MonthPickerView_monthFontColorDisabled, 0);
            headerTitleColor = a.getColor(R.styleable.MonthPickerView_headerTitleColor, 0);
            actionButtonColor = a.getColor(R.styleable.MonthPickerView_dialogActionButtonColor, 0);

            if (monthFontColorNormal == 0) {
                monthFontColorNormal = ContextCompat.getColor(_context, R.color.fontBlackEnable);
            }

            if (monthFontColorSelected == 0) {
                monthFontColorSelected = ContextCompat.getColor(_context, R.color.fontWhiteEnable);
            }

            if (monthFontColorDisabled == 0) {
                monthFontColorDisabled = ContextCompat.getColor(_context, R.color.fontBlackDisable);

            }
            if (_headerFontColorNormal == 0) {
                _headerFontColorNormal = ContextCompat.getColor(_context, R.color.fontWhiteDisable);
            }
            if (_headerFontColorSelected == 0) {
                _headerFontColorSelected = ContextCompat.getColor(_context, R.color.fontWhiteEnable);
            }
            if (headerTitleColor == 0) {
                headerTitleColor = ContextCompat.getColor(_context, R.color.fontWhiteEnable);
            }
            if (monthBgColor == 0) {
                monthBgColor = ContextCompat.getColor(_context, R.color.fontWhiteEnable);
            }

            if (headerBgColor == 0) {

                headerBgColor = android.R.attr.colorAccent;
                TypedValue outValue = new TypedValue();
                context.getTheme().resolveAttribute(headerBgColor, outValue, true);
                headerBgColor = outValue.data;
            }

            if (monthBgSelectedColor == 0) {

                monthBgSelectedColor = android.R.attr.colorAccent;
                TypedValue outValue = new TypedValue();
                context.getTheme().resolveAttribute(monthBgSelectedColor, outValue, true);
                monthBgSelectedColor = outValue.data;
            }

            map = new HashMap<>();
            if (monthBgColor != 0)
                map.put("monthBgColor", monthBgColor);
            if (monthBgSelectedColor != 0)
                map.put("monthBgSelectedColor", monthBgSelectedColor);
            if (monthFontColorNormal != 0)
                map.put("monthFontColorNormal", monthFontColorNormal);
            if (monthFontColorSelected != 0)
                map.put("monthFontColorSelected", monthFontColorSelected);
            if (monthFontColorDisabled != 0)
                map.put("monthFontColorDisabled", monthFontColorDisabled);

            a.recycle();
        }

        _monthList = findViewById(R.id.listview);
        _yearView = findViewById(R.id.yearView);
        _month = findViewById(R.id.month);
        _year = findViewById(R.id.year);
        _title = findViewById(R.id.title);
        RelativeLayout _pickerBg = findViewById(R.id.picker_view);
        LinearLayout _header = findViewById(R.id.header);
        RelativeLayout _actionBtnLay = findViewById(R.id.action_btn_lay);
        TextView ok = findViewById(R.id.ok_action);
        TextView cancel = findViewById(R.id.cancel_action);


        if (actionButtonColor != 0) {
            ok.setTextColor(actionButtonColor);
            cancel.setTextColor(actionButtonColor);
        } else {
            ok.setTextColor(headerBgColor);
            cancel.setTextColor(headerBgColor);
        }

        if (_headerFontColorSelected != 0)
            _month.setTextColor(_headerFontColorSelected);
        if (_headerFontColorNormal != 0)
            _year.setTextColor(_headerFontColorNormal);
        if (headerTitleColor != 0)
            _title.setTextColor(headerTitleColor);
        if (headerBgColor != 0)
            _header.setBackgroundColor(headerBgColor);
        if (monthBgColor != 0)
            _pickerBg.setBackgroundColor(monthBgColor);
        if(monthBgColor != 0)
            _actionBtnLay.setBackgroundColor(monthBgColor);

        ok.setOnClickListener(view -> _onDateSet.onDateSet());
        cancel.setOnClickListener(view -> _onCancel.onCancel());
        _monthViewAdapter = new MonthViewAdapter(context);
        _monthViewAdapter.setColors(map);
        _monthViewAdapter.setOnDaySelectedListener((view, selectedMonth) -> {
            Log.d("----------------", "MonthPickerDialogStyle selected month = " + selectedMonth);
            MonthPickerView.this._selectedMonth = selectedMonth;
            _month.setText(_monthNames[selectedMonth]);
            if (!_showMonthOnly) {
                _monthList.setVisibility(View.GONE);
                _yearView.setVisibility(View.VISIBLE);
                _month.setTextColor(_headerFontColorNormal);
                _year.setTextColor(_headerFontColorSelected);
            }
            if (_onMonthChanged != null) {
                _onMonthChanged.onMonthChanged(selectedMonth);
            }
        });
        _monthList.setAdapter(_monthViewAdapter);

        _yearView.setRange(_minYear, _maxYear);
        _yearView.setColors(map);
        _yearView.setYear(Calendar.getInstance().get(Calendar.YEAR));
        _yearView.setOnYearSelectedListener((view, selectedYear) -> {
            Log.d("----------------", "selected year = " + selectedYear);
            MonthPickerView.this._selectedYear = selectedYear;
            _year.setText(String.format(Locale.getDefault(), "%d", selectedYear));
            _year.setTextColor(_headerFontColorSelected);
            _month.setTextColor(_headerFontColorNormal);
            if (_onYearChanged != null) {
                _onYearChanged.onYearChanged(selectedYear);
            }
        });
        _month.setOnClickListener(view -> {
            if (_monthList.getVisibility() == GONE) {
                _yearView.setVisibility(GONE);
                _monthList.setVisibility(VISIBLE);
                _year.setTextColor(_headerFontColorNormal);
                _month.setTextColor(_headerFontColorSelected);
            }
        });
        _year.setOnClickListener(view -> {
            if (_yearView.getVisibility() == GONE) {
                _monthList.setVisibility(GONE);
                _yearView.setVisibility(VISIBLE);
                _year.setTextColor(_headerFontColorSelected);
                _month.setTextColor(_headerFontColorNormal);
            }
        });
    }

    protected void init(int year, int month) {
        _selectedYear = year;
        _selectedMonth = month;
    }

    protected void setMaxMonth(int maxMonth) {
        if (maxMonth <= Calendar.DECEMBER && maxMonth >= Calendar.JANUARY) {
            _monthViewAdapter.setMaxMonth(maxMonth);
            if (maxMonth < _selectedMonth) {
                setActivatedMonth(maxMonth);
            } else {
                _monthViewAdapter.setSelectedMonth(_selectedMonth);
            }
        } else {
            throw new IllegalArgumentException("Month out of range please send months between " +
                    "Calendar.JANUARY, Calendar.DECEMBER");
        }
    }


    protected void setMinMonth(int minMonth) {
        if (minMonth >= Calendar.JANUARY && minMonth <= Calendar.DECEMBER) {
            _monthViewAdapter.setMinMonth(minMonth);
            if (minMonth > _selectedMonth) {
                setActivatedMonth(minMonth);
            } else {
                _monthViewAdapter.setSelectedMonth(_selectedMonth);
            }
        } else {
            throw new IllegalArgumentException("Month out of range please send months between" +
                    " Calendar.JANUARY, Calendar.DECEMBER");
        }
    }

    protected void setMinYear(int minYear) {
        _yearView.setMinYear(minYear);
    }

    protected void setMaxYear(int maxYear) {
        _yearView.setMaxYear(maxYear);
    }

    protected void showMonthOnly() {
        _showMonthOnly = true;
        _year.setVisibility(GONE);
    }

    protected void showYearOnly() {
        _monthList.setVisibility(View.GONE);
        _yearView.setVisibility(VISIBLE);

        _month.setVisibility(GONE);
        _year.setTextColor(_headerFontColorSelected);
    }

    protected void setActivatedMonth(int activatedMonth) {
        if (activatedMonth >= Calendar.JANUARY && activatedMonth <= Calendar.DECEMBER) {
            _selectedMonth = activatedMonth;
            _monthViewAdapter.setSelectedMonth(activatedMonth);
           _month.setText(_monthNames[activatedMonth]);
        } else {
            throw new IllegalArgumentException("Month out of range please send months between Calendar.JANUARY, Calendar.DECEMBER");
        }

    }

    protected void setActivatedYear(int activatedYear) {
        _yearView.setActivatedYear(activatedYear);
        _year.setText(String.format(Locale.getDefault(), "%d", activatedYear));
    }

    protected void setMonthRange(int minMonth, int maxMonth) {
        if (minMonth < maxMonth) {
            setMinMonth(minMonth);
            setMaxMonth(maxMonth);
        } else {
            throw new IllegalArgumentException("maximum month is less then minimum month");
        }
    }

    protected void setYearRange(int minYear, int maxYear) {
        if (minYear < maxYear) {
            setMinYear(minYear);
            setMaxYear(maxYear);
        } else {
            throw new IllegalArgumentException("maximum year is less then minimum year");
        }
    }

    protected void setMonthYearRange(int minMonth, int maxMonth, int minYear, int maxYear) {
        setMonthRange(minMonth, maxMonth);
        setYearRange(minYear, maxYear);
    }

    protected void setTitle(String dialogTitle) {
        if (dialogTitle != null && !dialogTitle.trim().isEmpty()) {
            _title.setText(dialogTitle);
            _title.setVisibility(VISIBLE);
        } else {
            _title.setVisibility(GONE);
        }
    }

    protected int getMonth() {
        return _selectedMonth;
    }

    protected int getYear() {
        return _selectedYear;
    }

    protected void setOnMonthChangedListener(MonthPickerDialog.OnMonthChangedListener onMonthChangedListener) {
        if (onMonthChangedListener != null) {
            this._onMonthChanged = onMonthChangedListener;
        }
    }

    protected void setOnYearChangedListener(MonthPickerDialog.OnYearChangedListener onYearChangedListener) {
        if (onYearChangedListener != null) {
            this._onYearChanged = onYearChangedListener;
        }
    }

    public void setOnDateListener(OnDateSet onDateSet) {
        this._onDateSet = onDateSet;
    }

    public void setOnCancelListener(OnCancel onCancel) {
        this._onCancel = onCancel;
    }


    public interface OnDateSet{
        void onDateSet();
    }
    public interface OnCancel{
        void onCancel();
    }

    MonthPickerDialog.OnConfigChangeListener configChangeListener;

    protected void setOnConfigurationChanged(MonthPickerDialog.OnConfigChangeListener configChangeListener){
        this.configChangeListener = configChangeListener;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        configChangeListener.onConfigChange();
        super.onConfigurationChanged(newConfig);
    }
}
