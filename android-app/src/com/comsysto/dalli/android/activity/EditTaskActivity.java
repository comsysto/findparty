package com.comsysto.dalli.android.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.comsysto.dalli.android.R;
import com.comsysto.modules.taskmanagement.api.dto.Context;
import com.comsysto.modules.taskmanagement.api.dto.Tag;
import com.comsysto.modules.taskmanagement.api.dto.Task;
import com.comsysto.modules.taskmanagement.api.dto.TaskStatus;
import com.comsysto.modules.taskmanagement.api.dto.TaskType;
import com.comsysto.modules.taskmanagement.api.dto.TimeContext;
/**
 * Activity for editing exisiting Tasks
 * 
 * @author stefandjurasic
 *
 */
public class EditTaskActivity extends TaskActivity {

	/**
	 * Initializes fields from existing Task
	 * @TODO: Refactor in smaller methods for better readability 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.saveButton.setText(R.string.SAVE_TASK_BUTTON);
		
		Task selectedTask = getTaskManagerApplication().getSelectedTask();
		if (selectedTask.getTaskType() != null) {
			this.taskTypeSpinner.setSelection(selectedTask.getTaskType().ordinal());
		}
		
		if (selectedTask.getStatus() != null) {
			this.taskStatusSpinner.setSelection(selectedTask.getStatus().ordinal());
		}
		
		if (selectedTask.getTimeContext() != null) {
			this.taskTimeContextSpinner.setSelection(selectedTask.getTimeContext().ordinal());
		}
		
		if (selectedTask.getDue() != null) {
			calendar.setTime(selectedTask.getDue());
			this.taskDueDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
		}
		
		if (selectedTask.getDue() != null) {
			calendar.setTime(selectedTask.getDue());
			this.taskDueDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
		}		
		
		if (!selectedTask.getContexts().isEmpty()) {
			List<String> contexts = new ArrayList<String>();
			for (Context context : selectedTask.getContexts()) {
				contexts.add(context.getDescription());
			}
			contextList.setEntries(contexts);
		}
		
		if (!selectedTask.getTags().isEmpty()) {
			List<String> tags = new ArrayList<String>();
			for (Tag context : selectedTask.getTags()) {
				tags.add(context.getDescription());
			}
			tagList.setEntries(tags);
		}
		
	}


	@Override
	public OnClickListener getOnClickListener() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Task selectedTask = getTaskManagerApplication().getSelectedTask();
				selectedTask.setDescription(editText.getText().toString());
				selectedTask.setTaskType(TaskType.valueOf((String)taskTypeSpinner.getSelectedItem()));
				selectedTask.setTimeContext(TimeContext.valueOf((String)taskTimeContextSpinner.getSelectedItem()));
				selectedTask.setStatus(TaskStatus.valueOf((String)taskStatusSpinner.getSelectedItem()));	
				calendar.set(taskDueDatePicker.getYear(), taskDueDatePicker.getMonth(), taskDueDatePicker.getDayOfMonth());
				selectedTask.setDue(calendar.getTime());
				
				for(String entry : contextList.getEntries()) {
					selectedTask.addContext(new Context(entry));
				}
				
				for(String entry : tagList.getEntries()) {
					selectedTask.addTag(new Tag(entry));
				}
				
				getTaskManagerApplication().saveTask(selectedTask);
				finish();
			}
		};
	}		
	
	public String getTaskName() {
		return getTaskManagerApplication().getSelectedTask().getDescription();
	}
}
