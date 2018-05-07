package com.zhht.cloud.entity;

import java.util.Date;

public class User {
	
	private String name;
	private int age;
	private Date birthday;
	
	public User(String name,int age,Date birthday) {
		// TODO Auto-generated constructor stub
		this.name= name;
		this.age= age;
		this.birthday = birthday;
	}
	
	public User() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	

}
