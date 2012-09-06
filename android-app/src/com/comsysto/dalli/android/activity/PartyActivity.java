package com.comsysto.dalli.android.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.method.DigitsKeyListener;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.*;
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

	Calendar calendar;
    SimpleDateFormat formatter = new SimpleDateFormat();
    private Button partyTime;
    static final String[] LEVELS = new String[]{"Beginner", "Amateur", "Professional"};
    Button numberOfParticipantsButton;
    int numberOfParticipants = 2;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.optionMenuHandler = new OptionMenuHandler(this);
		
		setContentView(R.layout.edit_task);
		
		initCategory();
		initLevelSpinner();
		initPartyTimeButton();
		initSaveButton();
        initParticipantsButton();
        
        fillValues();
	}

    protected abstract void fillValues();

    private void initParticipantsButton() {
        this.numberOfParticipantsButton = (Button) findViewById(R.id.numberOfParticipantsButton);
        this.numberOfParticipantsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DialogFragment() {
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        final EditText editText = new EditText(getActivity());
                        editText.setText("" + numberOfParticipants);
                        editText.setKeyListener(new DigitsKeyListener());
                        builder.setView(editText);
                        builder.setTitle(getResources().getString(R.string.NUMBER_OF_PARTICIPANTS_DIALOG_TITLE));
                        builder.setPositiveButton(R.string.OK_BUTTON, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    numberOfParticipants = Integer.parseInt(editText.getText().toString());
                                    setTextOnNumberOfParticipantsButton(numberOfParticipants);
                                }
                                catch(NumberFormatException e) {
                                    //Ignore wrong entered numbers
                                }

                            }
                        });
                        return builder.create();
                    }


                };
                fragment.show(getSupportFragmentManager(), "numberOfParticipantsPicker");

            }
        });
    }

    private void initPartyTimeButton() {
        this.partyTime = (Button) findViewById(R.id.partyTimeButton);

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

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    void setTextOnNumberOfParticipantsButton(int numberOfParticipants) {
        this.numberOfParticipantsButton.setText("Required Participants : " + numberOfParticipants);
    }



}
