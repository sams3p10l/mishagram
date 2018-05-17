package com.example.specter.mishagram;


public class Contact
{
	private int id;
	private String username;
	private String firstName;
	private String lastName;

	Contact(int pID, String pUsername, String pFirstname, String pLastname)
	{
		this.id = pID;
		this.username = pUsername;
		this.firstName = pFirstname;
		this.lastName = pLastname;
	}

	Contact(int id, String pUsername)
	{
		this.id = id;
		this.username = pUsername;
	}

	public int getId()
	{
		return id;
	}

	public String getUsername()
	{
		return username;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}
}
