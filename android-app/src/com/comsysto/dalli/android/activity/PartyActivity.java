package com.comsysto.dalli.android.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.text.method.DigitsKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.comsysto.dalli.android.R;
import com.comsysto.dalli.android.menu.OptionMenuHandler;
import com.comsysto.dalli.android.service.LocationInfo;
import com.comsysto.dalli.android.service.LocationService;
import com.comsysto.findparty.Party;
import com.comsysto.findparty.Point;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

/**
 * Super class for {@link EditPartyActivity} and {@link CreatePartyActivity}.
 * 
 * @author stefandjurasic
 *
 */
public abstract class PartyActivity extends AbstractActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, Observer {

	private OptionMenuHandler optionMenuHandler;
	TextView categoryNameText;
	Spinner levelSpinner;
	Button saveButton;

    SimpleDateFormat formatter = new SimpleDateFormat();
    private Button partyTime;
    static final String[] LEVELS = new String[]{"Beginner", "Amateur", "Professional"};
    Button numberOfParticipantsButton;
    private Button partyLocationButton;
    LocationService locationService;
    Party party;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.optionMenuHandler = new OptionMenuHandler(this);
		
		setContentView(R.layout.edit_task);
		
        this.party = getParty();
        
		initCategory();
		initLevelSpinner();
		initPartyTimeButton();
		initSaveButton();
        initParticipantsButton();
        initPartyLocationButton();
    }

    protected abstract Party getParty();

    private void initPartyLocationButton() {
        locationService = new LocationService(getApplicationContext(), this);
        this.partyLocationButton = (Button)findViewById(R.id.partyLocationButton);

        this.partyLocationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               locationService.activate();
            }
        });

        if (party.getLocation() != null) {
                partyLocationButton.setText(locationService.getLocationFromPoint(party.getLocation()));
        } else {
            locationService.activate();
        }
    }

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
                        editText.setText("" + party.getSize());
                        editText.setKeyListener(new DigitsKeyListener());
                        builder.setView(editText);
                        builder.setTitle(getResources().getString(R.string.NUMBER_OF_PARTICIPANTS_DIALOG_TITLE));
                        builder.setPositiveButton(R.string.OK_BUTTON, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    int numberOfParticipants = Integer.parseInt(editText.getText().toString());
                                    party.setSize(numberOfParticipants);
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
        setTextOnNumberOfParticipantsButton(party.getSize());
    }

    private void initPartyTimeButton() {
        this.partyTime = (Button) findViewById(R.id.partyTimeButton);

        partyTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DialogFragment() {
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        Calendar calendar = getCalendarFromParty();
                        return new DatePickerDialog(getActivity(), PartyActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                    }
                };

                fragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        setTimeOnView();
    }

    private Calendar getCalendarFromParty() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(party.getStartDate());
        return calendar;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendarFromParty = getCalendarFromParty();
        calendarFromParty.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendarFromParty.set(Calendar.MINUTE, minute);
        party.setStartDate(calendarFromParty.getTime());
        setTimeOnView();
    }

    void setTimeOnView() {
        partyTime.setText("On " + formatter.format(party.getStartDate()));
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        DialogFragment fragment = new DialogFragment() {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                Calendar calendar = getCalendarFromParty();
                return new TimePickerDialog(getActivity(), PartyActivity.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));
            }
        };
        fragment.show(getSupportFragmentManager(), "timePicker");
        Calendar calendarFromParty = getCalendarFromParty();

        calendarFromParty.set(Calendar.YEAR, year);
        calendarFromParty.set(Calendar.MONTH, monthOfYear);
        calendarFromParty.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        party.setStartDate(calendarFromParty.getTime());


    }

	private void initSaveButton() {
		this.saveButton = (Button) findViewById(R.id.addButton);
		this.saveButton.setOnClickListener(getOnClickListener());
	}

	private void initLevelSpinner() {
		levelSpinner = (Spinner) findViewById(R.id.levelSpinner);

	    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, LEVELS);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    levelSpinner.setAdapter(adapter);
        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                party.setLevel(adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        levelSpinner.setSelection(getLevelSpinnerSelectionPosition(party.getLevel()));

    }

    public int getLevelSpinnerSelectionPosition(String selectedLevel) {
        for (int i = 0; i< LEVELS.length; i++) {
            if (LEVELS[i].equals(selectedLevel)) {
                return i;
            }

        }
        return 0;
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

    void setTextOnNumberOfParticipantsButton(int numberOfParticipants) {
        this.numberOfParticipantsButton.setText("Required Participants : " + numberOfParticipants);
    }


    @Override
    public void update(Observable observable, Object o) {
        LocationInfo locationInfo = (LocationInfo) o;
        this.partyLocationButton.setText(locationInfo.getLocationStringForParty());
        Point location = new Point();
        location.setLat(locationInfo.getLatitude());
        location.setLon(locationInfo.getLongitude());
        this.party.setLocation(location);
        locationService.deactivate();
    }
}
