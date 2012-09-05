package com.comsysto.dalli.android.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.comsysto.modules.taskmanagement.api.dto.Task;
import com.comsysto.modules.usermanagement.api.AuthenticationException;
import com.comsysto.modules.usermanagement.api.dto.User;


/**
 * Implementation using {@link RestTemplate} from Spring to communicate with
 * our RestService to obtain Tasks.
 * 
 * @TODO: Currently readTimeout does not work. <a href="https://jira.springsource.org/browse/ANDROID-32">JIRA Ticket</a>
 * 
 * @author stefandjurasic
 *
 */
public class TaskManagementServiceImpl implements TaskManagementService{

	
	private RestTemplate restTemplate;
	
	private String taskManagementRestUrl;
	private String host;

	private String echoRestUrl;

	private String loginServiceRestUrl;

	
	
	public TaskManagementServiceImpl(String host) {
		this.host = "http://" + host;
		this.taskManagementRestUrl = this.host + "/rest/users/{userName}/tasks";
		this.loginServiceRestUrl = this.host + "/rest/login/";
		this.echoRestUrl = this.host + "/rest/echo/{echo}";
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		//TODO: NOT WOKRING, check again when spring-android-rest is released
		//requestFactory.setReadTimeout(100);
		this.restTemplate = new RestTemplate(requestFactory);
	}

	@Override
	public Task createTask(String userName, Task newTask) {
		return restTemplate.postForObject(taskManagementRestUrl, newTask, Task.class, userName);
	}

	@Override
	public void deleteTask(String userName, String id) {
		restTemplate.delete(taskManagementRestUrl + "/{taskId}", userName, id);
	}
	
	

	@Override
	public void saveTask(String userName, Task task) {
		restTemplate.put(taskManagementRestUrl, task, userName);
		task.setVersion(0);
	}

	@Override
	public String echo(String echo) {
		Map<String, String> echoMap = new HashMap<String, String>(); 
		echoMap.put("echo", echo);
		return restTemplate.getForObject(echoRestUrl, String.class, echoMap);
	}

	@Override
	public List<Task> getAllTasksFor(String userName) {
		Task[] tasks = restTemplate.getForObject(taskManagementRestUrl, Task[].class, userName);
		return new ArrayList<Task>(Arrays.asList(tasks));

	}

	@Override
	public User createUser(User user, String password) {
		return restTemplate.postForObject(loginServiceRestUrl + "{password}", user, User.class, password);
	}

	@Override
	public User login(String userName, String password) throws AuthenticationException {
		return restTemplate.getForObject(loginServiceRestUrl + "{userName}/{password}", User.class, userName, password);
	}
}
