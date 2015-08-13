package com.example.mapslabs;
/* vo City gi imam site informacii shto mozham da gi imam za eden grad, shto gi dobivam od
 * JSON file-ot. */
public class City {

	private String fcodeName;
	private String toponymName;
	private String countrycode;
	private String fcl;
	private String fclName;
	private String name;
	private String wikipedia;
	private double lng;
	private String fcode;
	private int geonameId;
	private double lat;
	private int population;
	
	public City() {
		// TODO Auto-generated constructor stub
	}

	public String getFcodeName() {
		return fcodeName;
	}

	public void setFcodeName(String fcodeName) {
		this.fcodeName = fcodeName;
	}

	public String getToponymName() {
		return toponymName;
	}

	public void setToponymName(String toponymName) {
		this.toponymName = toponymName;
	}

	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	public String getFcl() {
		return fcl;
	}

	public void setFcl(String fcl) {
		this.fcl = fcl;
	}

	public String getFclName() {
		return fclName;
	}

	public void setFclName(String fclName) {
		this.fclName = fclName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWikipedia() {
		return wikipedia;
	}

	public void setWikipedia(String wikipedia) {
		this.wikipedia = wikipedia;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getFcode() {
		return fcode;
	}

	public void setFcode(String fcode) {
		this.fcode = fcode;
	}

	public int getGeonameId() {
		return geonameId;
	}

	public void setGeonameId(int geonameId) {
		this.geonameId = geonameId;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

}
