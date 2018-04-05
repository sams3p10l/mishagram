package com.example.specter.mishagram;


public class Contact
{
	private String name;

	Contact(String pname)
	{
		this.name = pname;
	}

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
