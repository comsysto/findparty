package com.comsysto.dalli.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.comsysto.dalli.android.R;
import com.comsysto.findparty.Party;

/**
 * Activity for editing exisiting Tasks
 * 
 * @author stefandjurasic
 *
 */
public class EditPartyActivity extends PartyActivity {

	/**
	 * Initializes fields from existing Task
	 * @TODO: Refactor in smaller methods for better readability 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.saveButton.setText(R.string.SAVE_TASK_BUTTON);
		
		Party selectedParty = getTaskManagerApplication().getSelectedParty();
//		if (selectedTask.getTaskType() != null) {
//			this.taskTypeSpinner.setSelection(selectedTask.getTaskType().ordinal());
//		}
//
//		if (selectedTask.getStatus() != null) {
//			this.taskStatusSpinner.setSelection(selectedTask.getStatus().ordinal());
//		}
//
//		if (selectedTask.getTimeContext() != null) {
//			this.taskTimeContextSpinner.setSelection(selectedTask.getTimeContext().ordinal());
//		}
//
//		if (selectedTask.getDue() != null) {
//			calendar.setTime(selectedTask.getDue());
//			this.partyDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
//		}
//
//		if (selectedTask.getDue() != null) {
//			calendar.setTime(selectedTask.getDue());
//			this.partyDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
//		}
//
//		if (!selectedTask.getContexts().isEmpty()) {
//			List<String> contexts = new ArrayList<String>();
//			for (Context context : selectedTask.getContexts()) {
//				contexts.add(context.getDescription());
//			}
//			contextList.setEntries(contexts);
//		}
//
//		if (!selectedTask.getTags().isEmpty()) {
//			List<String> tags = new ArrayList<String>();
//			for (Tag context : selectedTask.getTags()) {
//				tags.add(context.getDescription());
//			}
//			tagList.setEntries(tags);
//		}
//
	}


	@Override
	public OnClickListener getOnClickListener() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Party selectedParty = getTaskManagerApplication().getSelectedParty();
//				selectedParty.setDescription(categoryNameText.getText().toString());
//				selectedParty.setTaskType(TaskType.valueOf((String) taskTypeSpinner.getSelectedItem()));
//				selectedParty.setTimeContext(TimeContext.valueOf((String) taskTimeContextSpinner.getSelectedItem()));
//				selectedParty.setStatus(TaskStatus.valueOf((String) taskStatusSpinner.getSelectedItem()));
//				calendar.set(partyDatePicker.getYear(), partyDatePicker.getMonth(), partyDatePicker.getDayOfMonth());
//				selectedParty.setDue(calendar.getTime());
//
//				for(String entry : contextList.getEntries()) {
//					selectedParty.addContext(new Context(entry));
//				}
//
//				for(String entry : tagList.getEntries()) {
//					selectedParty.addTag(new Tag(entry));
//				}
				
				getTaskManagerApplication().saveParty(selectedParty);
				goToSplashScreen(EditPartyActivity.this);
			}
		};
	}		
	
	public String getCategory() {
        return getTaskManagerApplication().getSelectedParty().getCategory();
	}
}
