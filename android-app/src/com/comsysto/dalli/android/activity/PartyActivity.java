package com.comsysto.dalli.android.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.*;
import com.comsysto.dalli.android.R;
import com.comsysto.dalli.android.menu.OptionMenuHandler;
import com.comsysto.dalli.android.widget.ModifiableTextList;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Super class for {@link EditPartyActivity} and {@link CreatePartyActivity}.
 * 
 * @author stefandjurasic
 *
 */
public abstract class PartyActivity extends AbstractActivity {

	private OptionMenuHandler optionMenuHandler;
	TextView categoryNameText;
	Spinner taskTypeSpinner;
	Button saveButton;
	DatePicker partyDatePicker;

	Calendar calendar = GregorianCalendar.getInstance();
    private TimePicker partyTimePicker;


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
		this.partyDatePicker = (DatePicker) findViewById(R.id.partyDatePicker);
        this.partyTimePicker = (TimePicker) findViewById(R.id.partyTimePicker);
	}

	private void initSaveButton() {
		this.saveButton = (Button) findViewById(R.id.addButton);
		this.saveButton.setOnClickListener(getOnClickListener());
	}

	private void initLevelSpinner() {
		taskTypeSpinner = (Spinner) findViewById(R.id.levelSpinner);

	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, new String[]{"Beginner", "Amateur","Professional"});
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    taskTypeSpinner.setAdapter(adapter);
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
	
}
