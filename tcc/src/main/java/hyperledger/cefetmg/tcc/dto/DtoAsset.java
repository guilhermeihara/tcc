package hyperledger.cefetmg.tcc.dto;

public class DtoAsset {

	private String name;
	private String value;
	private String address;
	private String token;
	private String owner;

	public DtoAsset(String name, String value, String address) {
		super();
		this.name = name;
		this.value = value;
		this.address = address;
	}
	
	public DtoAsset(String name, String value, String address, String owner) {
		super();
		this.name = name;
		this.value = value;
		this.address = address;
		this.owner = owner;
	}

	public DtoAsset(String name, String value, String address, String token, String owner) {
		super();
		this.name = name;
		this.value = value;
		this.address = address;
		this.token = token;
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

}