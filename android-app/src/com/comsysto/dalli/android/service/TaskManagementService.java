package com.comsysto.dalli.android.service;

import com.comsysto.modules.taskmanagement.api.dto.Task;
import com.comsysto.modules.usermanagement.api.UserManagementService;

import java.util.List;

/**
 * {@link TaskManagementService} containing all relevant methods 
 * for the app to interact with the backend.
 * 
 * @author Stefan Djurasic
 *
 */
public interface TaskManagementService extends UserManagementService{
	
	Task createTask(String userName, Task newTask);
	
	List<Task> getAllTasksFor(String userName);
	
	void saveTask(String userName, Task task);
	
	void deleteTask(String userName, String id);

	String echo(String echo);
}