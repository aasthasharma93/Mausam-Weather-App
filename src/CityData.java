package sharma.sugandha.mausam;

public class CityData {
	
	private int _id;
	private String city;
	private String zipCode;
	
	public CityData(){}
	
	public CityData(int _id, String city, String zipCode){
		this._id = _id;
		this.city = city;
		this.zipCode = zipCode;
	}
	
	public CityData(String city, String zipCode){
		this.city = city;
		this.zipCode = zipCode;
	}
	
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}
