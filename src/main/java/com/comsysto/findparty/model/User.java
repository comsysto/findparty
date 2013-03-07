package org.comsysto.labs.user;

public class User {
	private String id;
	private String name;
	private int age;
	private String email;

	public User(String name, int age, String email) {
		this.name = name;
		this.age = age;
		this.email = email;
	}

    public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}
	
	
	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", email =" + email +"]";
	}
}
