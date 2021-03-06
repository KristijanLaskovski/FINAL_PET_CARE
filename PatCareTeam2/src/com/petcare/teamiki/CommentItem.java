package com.petcare.teamiki;

public class CommentItem {

	public String message;
	public String firstname;
	public String lastname;
	public String time_c;
	public String image_p;
	public String image_c;
	public String langitude;
	public String longitude;
	public String contact;
	public String type_c;
	public String post_id;
	public String user;
	public String address;
	public String hasimageC;
	
	public CommentItem(String user,String message, String firstname, String lastname,
			String time_c, String image_p, String image_c, String langitude,
			String longitude, String contact, String type_c,String post_id,String address,String hasimageC) {
		super();
		this.user=user;
		this.message = message;
		this.firstname = firstname;
		this.lastname = lastname;
		this.time_c = time_c;
		this.image_p = image_p;
		this.image_c = image_c;
		this.langitude = langitude;
		this.longitude = longitude;
		this.contact = contact;
		this.type_c = type_c;
		this.post_id=post_id;
		this.address=address;
		this.hasimageC=hasimageC;
	}

	
	public String getHasimageC() {
		return hasimageC;
	}


	public void setHasimageC(String hasimageC) {
		this.hasimageC = hasimageC;
	}


	public String getUser() {
		return user;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
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

	public String getTime_c() {
		return time_c;
	}

	public void setTime_c(String time_c) {
		this.time_c = time_c;
	}

	public String getImage_p() {
		return image_p;
	}

	public void setImage_p(String image_p) {
		this.image_p = image_p;
	}

	public String getImage_c() {
		return image_c;
	}

	public void setImage_c(String image_c) {
		this.image_c = image_c;
	}

	public String getLangitude() {
		return langitude;
	}

	public void setLangitude(String langitude) {
		this.langitude = langitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getType_c() {
		return type_c;
	}

	public void setType_c(String type_c) {
		this.type_c = type_c;
	}
	
	
	
	
}
