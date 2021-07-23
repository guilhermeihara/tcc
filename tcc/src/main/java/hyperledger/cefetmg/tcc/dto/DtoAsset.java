package hyperledger.cefetmg.tcc.dto;

public class DtoAsset {

	private String id;
	private String name;
	private String value;
	
	public DtoAsset(String id, String name, String value) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
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

	@Override
	public String toString() {
		return "DtoAsset [id=" + id + ", name=" + name + ", value=" + value + "]";
	}
	
}