package com.comsysto.dalli.android.activity;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.comsysto.dalli.android.R;
import com.comsysto.dalli.android.menu.OptionMenuHandler;
import com.comsysto.dalli.android.widget.ModifiableTextList;
import com.comsysto.modules.taskmanagement.api.dto.TaskStatus;
import com.comsysto.modules.taskmanagement.api.dto.TaskType;
import com.comsysto.modules.taskmanagement.api.dto.TimeContext;

/**
 * Super class for {@link EditTaskActivity} and {@link CreateTaskActivity}.
 * 
 * @author stefandjurasic
 *
 */
public abstract class TaskActivity extends AbstractActivity {

	private OptionMenuHandler optionMenuHandler;
	EditText editText;
	Spinner taskTypeSpinner;
	Spinner taskStatusSpinner;
	Button saveButton;
	Spinner taskTimeContextSpinner;
	DatePicker taskDueDatePicker;
	ModifiableTextList contextList;
	ModifiableTextList tagList;
	
	Calendar calendar = GregorianCalendar.getInstance();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.optionMenuHandler = new OptionMenuHandler(this);
		
		setContentView(R.layout.edit_task);
		
		initTaskName();
		initTaskTypeSpinner();
		initTaskStatusSpinner();
		initTaskTimeContextSpinner();
		initTaskDueDatePicker();
		initSaveButton();
		initModifiableLists();
	}

	private void initModifiableLists() {
		this.contextList = (ModifiableTextList) findViewById(R.id.contextList);
		this.tagList = (ModifiableTextList) findViewById(R.id.tagList);
	}

	private void initTaskDueDatePicker() {
		this.taskDueDatePicker = (DatePicker) findViewById(R.id.taskDueDatePicker);
	}

	private void initSaveButton() {
		this.saveButton = (Button) findViewById(R.id.addButton);
		this.saveButton.setOnClickListener(getOnClickListener());
	}

	private void initTaskTypeSpinner() {
		taskTypeSpinner = (Spinner) findViewById(R.id.taskTypeSpinner);

	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, TaskType.names());
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    taskTypeSpinner.setAdapter(adapter);
	}

	private void initTaskStatusSpinner() {
		taskStatusSpinner = (Spinner) findViewById(R.id.taskStatusSpinner);

	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, TaskStatus.names());
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    taskStatusSpinner.setAdapter(adapter);
	}	
	
	private void initTaskTimeContextSpinner() {
		taskTimeContextSpinner = (Spinner) findViewById(R.id.taskTimeContextSpinner);

	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, TimeContext.names());
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    taskTimeContextSpinner.setAdapter(adapter);
		
	}
	
	private void initTaskName() {
		this.editText = (EditText)findViewById(R.id.taskNameEditText);
		String taskName = getTaskName();
		if (taskName != null) {
			editText.setText(taskName);
		}
	}

	public abstract String getTaskName();
	
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
