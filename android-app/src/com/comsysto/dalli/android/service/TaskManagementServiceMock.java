package com.comsysto.dalli.android.service;

import java.util.ArrayList;
import java.util.List;

import com.comsysto.modules.taskmanagement.api.dto.Task;
import com.comsysto.modules.usermanagement.api.AuthenticationException;
import com.comsysto.modules.usermanagement.api.dto.User;


/**
 * Current mock implementation. In future we need a offline implementation of this service,
 * returning Tasks from a local storage when offline.
 * 
 * @author stefandjurasic
 *
 */
public class TaskManagementServiceMock implements TaskManagementService {

	List<Task> tasks = new ArrayList<Task>();

	public TaskManagementServiceMock() {
		tasks.add(new Task("Brot backen"));
		tasks.add(new Task("Kaffe kochen"));
		tasks.add(new Task("Sauber machen"));
	}

	@Override
	public Task createTask(String userName, Task newTask) {
		return newTask;
	}

	@Override
	public void deleteTask(String userName, String id) {
		//mockidimock, adapter removes, is enough
	}

	@Override
	public void saveTask(String userName, Task task) {
		// TODO Auto-generated method stub

	}

	@Override
	public String echo(String echo) {
		return echo;
	}

	@Override
	public List<Task> getAllTasksFor(String userId) {
		// TODO Auto-generated method stub
		return tasks;
	}

	@Override
	public User createUser(User user, String password) {
		return user;
	}

	@Override
	public User login(String username, String password) throws AuthenticationException {
		// TODO Auto-generated method stub
		User user = new User();
		user.setUsername(username);
		return user;
	}

}
