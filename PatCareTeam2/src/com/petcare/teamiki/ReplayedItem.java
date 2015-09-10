package com.petcare.teamiki;

public class ReplayedItem {
	public String timePost;
	public String message;
	public String firstname;
	public String lastname;
	public String image;
	
	public ReplayedItem(String timePost, String message, String firstname,
			String lastname, String image) {
		super();
		this.timePost = timePost;
		this.message = message;
		this.firstname = firstname;
		this.lastname = lastname;
		this.image = image;
	}

	public String getTimePost() {
		return timePost;
	}

	public void setTimePost(String timePost) {
		this.timePost = timePost;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	
}
