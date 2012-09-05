package com.comsysto.dalli.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;

import com.comsysto.dalli.android.R;
import com.comsysto.dalli.android.adapter.TaskListAdapter;
import com.comsysto.dalli.android.application.TaskManagerApplication;
import com.comsysto.dalli.android.authentication.AccountAuthenticator;
import com.comsysto.dalli.android.menu.OptionMenuHandler;
import com.comsysto.modules.taskmanagement.api.dto.Task;
import com.comsysto.modules.taskmanagement.api.dto.TaskStatus;

/**
 * Abstract class for displaying a list of task's. Subclasses decide which
 * List of Tasks shall be displayed. 
 * 
 * @author stefandjurasic
 *
 */
public abstract class AbstractTaskListActivity extends ListActivity {

	private ArrayAdapter<Task> arrayAdapter;
	protected List<Task> tasks;
	private OptionMenuHandler optionMenuHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.optionMenuHandler = new OptionMenuHandler(this);
		registerForContextMenu(getListView());
	}

	@Override
	protected void onResume() {
		super.onResume();
	    AccountManager mAccountManager = AccountManager.get(this);
	    Account[] accountsByType = mAccountManager.getAccountsByType(AccountAuthenticator.AUTH_TYPE);
	    if (accountsByType.length == 0) {
	    	finish();
	    } else {
	    	initArrayAdapter();
	    }
	}

	protected void initArrayAdapter() {
		this.tasks = getTaskManagerApplication().getTaskList();
		filter();
		setArrayAdapter(new TaskListAdapter(this, tasks));
		setListAdapter(getArrayAdapter());
	}

	abstract protected void filter();
	
	protected void filterActiveTasks() {
		List<Task> filteredList = new ArrayList<Task>();
		for (Task currentTask : tasks) {
			if (currentTask.getStatus() == TaskStatus.ACTIVE) {
				filteredList.add(currentTask);
			}
		}
		this.tasks = filteredList;
	}
	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.task_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Task selectedTask = getArrayAdapter().getItem((int)info.id);

		switch (item.getItemId()) {
		case R.id.toggle_task:
			 TaskStatus taskStatus = selectedTask.getStatus() == TaskStatus.ACTIVE ? TaskStatus.DONE : TaskStatus.ACTIVE;
			 selectedTask.setStatus(taskStatus);
			 this.notifyDataSetChanged();
			 return true;
		case R.id.delete_task:
			getTaskManagerApplication().deleteTask(selectedTask);
			this.notifyDataSetChanged();
			return true;
		case R.id.open_task:
			getTaskManagerApplication().setSelectedTask(selectedTask);
			Intent intent = new Intent(this,
					EditTaskActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onContextItemSelected(item);

		}
	}

	public void setArrayAdapter(ArrayAdapter<Task> arrayAdapter) {
		this.arrayAdapter = arrayAdapter;
	}

	public ArrayAdapter<Task> getArrayAdapter() {
		return arrayAdapter;
	}

	public TaskManagerApplication getTaskManagerApplication() {
		return (TaskManagerApplication) getApplication();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return this.optionMenuHandler.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.optionMenuHandler.onOptionsItemSelected(item);
		return super.onOptionsItemSelected(item);
	}

	public void notifyDataSetChanged() {
		initArrayAdapter();
	}	

}
