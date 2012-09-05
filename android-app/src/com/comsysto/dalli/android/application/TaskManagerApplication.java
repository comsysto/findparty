package com.comsysto.dalli.android.application;

import java.util.List;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.comsysto.dalli.android.service.TaskManagementService;
import com.comsysto.dalli.android.service.TaskManagementServiceImpl;
import com.comsysto.dalli.android.service.TaskManagementServiceMock;
import com.comsysto.modules.taskmanagement.api.dto.Task;
import com.comsysto.modules.usermanagement.api.dto.User;

/**
 * {@link TaskManagerApplication} holds relevant stuff for the whole app .
 * 
 * Currently:
 * <ul>
 * 	<li>caches the Tasks</li>
 * 	<li>holds the currently selectedTask (e.g. when editing a Task)</li>
 *  <li>delegates update,get,create,delete calls to the {@link TaskManagementService}</li>
 *  <li>whether online/offline selects a corresponding {@link TaskManagementService}</li>
 * 
 * @author stefandjurasic
 *
 */
public class TaskManagerApplication extends Application {

	private List<Task> tasks;

	private Task selectedTask;

	private TaskManagementService taskManagementService;
	
	private boolean ready;
	
	private User user;

	@Override
	public void onCreate() {
		initializeService();
	}

	public void initializeService() {
		this.tasks = null;
		this.ready = false;
		SharedPreferences defaultSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		String host = defaultSharedPreferences.getString("host",
				"10.0.2.2:8080");
		if (isConnected()) {
			initializeOnlineService(host);
		} else {
			this.taskManagementService = new TaskManagementServiceMock();
		}
	}

	private void initializeOnlineService(final String host) {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				TaskManagerApplication.this.taskManagementService = new TaskManagementServiceImpl(
						host);
				try {
					String echo = TaskManagerApplication.this.taskManagementService
							.echo("echo");
					if (echo.equals("echo")) {
						Log.i("Server Check", "Server is online");
					} else {
						Log.e("Server Check", "Server returned wrong echo ("+ echo + "), going offline.");
						TaskManagerApplication.this.taskManagementService = new TaskManagementServiceMock();
					}
				} catch (Exception e) {
					Log.e("Server Check", "Server not reachable", e);
					TaskManagerApplication.this.taskManagementService = new TaskManagementServiceMock();
				}
				TaskManagerApplication.this.ready = true;
				return null;
			}
		};
		task.execute();
	}

	public void createTask(Task newTask) {
		Task createdTask = this.taskManagementService.createTask(user.getUsername(), newTask);
		this.tasks.add(createdTask);
	}

	public void loadTasks() {
		this.tasks = this.taskManagementService.getAllTasksFor(user.getUsername());
	}
	
	public List<Task> getTaskList() {
		loadTasks();
		return tasks;
	}

	public void deleteTask(Task task) {
		this.taskManagementService.deleteTask(user.getUsername(), task.getId());
		this.tasks.remove(task);	
	}

	boolean isConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
		if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()
				&& activeNetworkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isReady() {
		return ready;
	}

	public void saveTask(Task updatedTask) {
		this.taskManagementService.saveTask(user.getUsername(), updatedTask);
	}

	public void setSelectedTask(Task selectedTask) {
		this.selectedTask = selectedTask;
	}

	public Task getSelectedTask() {
		return selectedTask;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public User createAccount(String userName, String password) {
		User user = new User();
		user.setUsername(userName);
		return this.taskManagementService.createUser(user, password);
	}

}
