package com.comsysto.dalli.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.comsysto.dalli.android.R;
import com.comsysto.dalli.android.application.TaskManagerApplication;
import com.comsysto.modules.taskmanagement.api.dto.Task;
import com.comsysto.modules.taskmanagement.api.dto.TaskStatus;

/**
 * The TaskListAdapter holds the Tasks to be displayed in a List and
 * handles the click event when clicked on a task in the list which currently
 * toggles the Task.
 * 
 * LongClick will be ignored and can be handled from the List itself.
 * 
 * @author stefandjurasic
 *
 */
public class TaskListAdapter extends ArrayAdapter<Task> {

	private Context context;

	public TaskListAdapter(Context context, List<Task> objects) {
		super(context, R.layout.list_item, objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Task task = getItem(position);
		if (convertView == null) {
			convertView = (LinearLayout) View.inflate(context,
					R.layout.list_item, null);
		}
		final CheckedTextView checkedTextView = (CheckedTextView) convertView
				.findViewById(android.R.id.text1);
		checkedTextView.setText(task.getDescription());
		checkedTextView.setChecked(task.getStatus() == TaskStatus.DONE);
		checkedTextView.setOnClickListener(new View.OnClickListener() {
		
		 @Override
		 public void onClick(View v) {
			 checkedTextView.toggle();
			 TaskStatus newStatus = checkedTextView.isChecked() ? TaskStatus.DONE : TaskStatus.ACTIVE;
			 task.setStatus(newStatus);
			 ((TaskManagerApplication)context.getApplicationContext()).saveTask(task);
		 }
		 });
		
		checkedTextView.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		return convertView;
	}

}
