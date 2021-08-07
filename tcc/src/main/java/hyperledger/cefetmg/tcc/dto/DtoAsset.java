package hyperledger.cefetmg.tcc.dto;

public class DtoAsset {

	private String id;
	private String name;
	private String value;
	private String address;
	
	public DtoAsset(String id, String name, String value, String address) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.address = address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	
}