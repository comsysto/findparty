package com.comsysto.dalli.android.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.comsysto.dalli.android.R;
import com.comsysto.dalli.android.menu.OptionMenuHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Super class for {@link EditPartyActivity} and {@link CreatePartyActivity}.
 * 
 * @author stefandjurasic
 *
 */
public abstract class PartyActivity extends AbstractActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener  {

	private OptionMenuHandler optionMenuHandler;
	TextView categoryNameText;
	Spinner levelSpinner;
	Button saveButton;

	Calendar calendar = GregorianCalendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat();
    private TextView partyTime;
    static final String[] LEVELS = new String[]{"Beginner", "Amateur", "Professional"};


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.optionMenuHandler = new OptionMenuHandler(this);
		
		setContentView(R.layout.edit_task);
		
		initCategory();
		initLevelSpinner();
		initTaskDueDatePicker();
		initSaveButton();
	}

	private void initTaskDueDatePicker() {
        this.partyTime = (TextView) findViewById(R.id.partyTime);
        setTimeOnView();

        partyTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DialogFragment() {
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        return new DatePickerDialog(getActivity(), PartyActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                    }
                };

                fragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

	}

	private void initSaveButton() {
		this.saveButton = (Button) findViewById(R.id.addButton);
		this.saveButton.setOnClickListener(getOnClickListener());
	}

	private void initLevelSpinner() {
		levelSpinner = (Spinner) findViewById(R.id.levelSpinner);

	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, LEVELS);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    levelSpinner.setAdapter(adapter);
	}

	private void initCategory() {
		this.categoryNameText = (TextView)findViewById(R.id.categoryNameText);
		String category = getCategory();
		if (category != null) {
			categoryNameText.setText(category);
		}
	}

	public abstract String getCategory();
	
	public abstract OnClickListener getOnClickListener();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return this.optionMenuHandler.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.optionMenuHandler.onOptionsItemSelected(item);
		return super.onOptionsItemSelected(item);
	}

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        setTimeOnView();
    }

    void setTimeOnView() {
        partyTime.setText("On " + formatter.format(calendar.getTime()));
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        DialogFragment fragment = new DialogFragment() {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                return new TimePickerDialog(getActivity(), PartyActivity.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));
            }
        };
        fragment.show(getSupportFragmentManager(), "timePicker");
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

    }
}
