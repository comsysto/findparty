package com.comsysto.dalli.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.comsysto.dalli.android.R;
import com.comsysto.modules.taskmanagement.api.dto.Task;
import com.comsysto.modules.taskmanagement.api.dto.TaskStatus;

/**
 * Acitivty for displaying only Active Tasks!
 * 
 * 
 * @author stefandjurasic
 *
 */
public class FocusActivity extends AbstractTaskListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.focus);		
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void filter() {
		filterActiveTasks();
	}
}
