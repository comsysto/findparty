package com.comsysto.dalli.android.activity;

import android.view.View;
import android.view.View.OnClickListener;

import com.comsysto.modules.taskmanagement.api.dto.Context;
import com.comsysto.modules.taskmanagement.api.dto.Tag;
import com.comsysto.modules.taskmanagement.api.dto.Task;
import com.comsysto.modules.taskmanagement.api.dto.TaskStatus;
import com.comsysto.modules.taskmanagement.api.dto.TaskType;
import com.comsysto.modules.taskmanagement.api.dto.TimeContext;

/**
 * Activity for creating Tasks
 * 
 * @author stefandjurasic
 *
 */
public class CreateTaskActivity extends TaskActivity {

	@Override
	public OnClickListener getOnClickListener() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Task newTask = new Task(editText.getText().toString());
				newTask.setTaskType(TaskType.valueOf((String)taskTypeSpinner.getSelectedItem()));		
				newTask.setTimeContext(TimeContext.valueOf((String)taskTimeContextSpinner.getSelectedItem()));
				newTask.setStatus(TaskStatus.valueOf((String)taskStatusSpinner.getSelectedItem()));	
				calendar.set(taskDueDatePicker.getYear(), taskDueDatePicker.getMonth(), taskDueDatePicker.getDayOfMonth());
				newTask.setDue(calendar.getTime());
				
				for(String entry : contextList.getEntries()) {
					newTask.addContext(new Context(entry));
				}
				
				for(String entry : tagList.getEntries()) {
					newTask.addTag(new Tag(entry));
				}
				
				
				getTaskManagerApplication().createTask(newTask);
				finish();
			}
		};
	}
	
	public String getTaskName() {
		return (String) getIntent().getExtras().get("taskname");
	}
}
