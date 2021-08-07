package hyperledger.cefetmg.tcc.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DeviceForm {

	@NotNull @NotEmpty
	private String name;
	
	@NotNull @NotEmpty
	private String address;
	
	@NotNull
	private Long tokenDuration;
	
	@NotNull
	private Long type;
	
	private String Value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTokenDuration() {
		return tokenDuration;
	}

	public void setTokenDuration(Long tokenDuration) {
		this.tokenDuration = tokenDuration;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
