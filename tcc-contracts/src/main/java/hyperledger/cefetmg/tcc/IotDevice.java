/*
 * SPDX-License-Identifier: Apache-2.0
 */
package main.java.hyperledger.cefetmg.tcc;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public class IotDevice {

	@Property()
	private String assetID;

	@Property()
	private String name;

	@Property()
	private String status;

	@Property()
	private String temperature;

	@Property()
	private String lock;

	public IotDevice() {
	}

	public IotDevice(@JsonProperty("assetID") String assetID, @JsonProperty("name") String name,
			@JsonProperty("status") String status, @JsonProperty("temperature") String temperature, @JsonProperty("lock") String lock) {
		this.assetID = assetID;
		this.name = name;
		this.status = status;
		this.temperature = temperature;
		this.lock = lock;
	}

//	public static IotDevice fromJSONString(String json) {
//		String value = new JSONObject(json).getString("value");
//		IotDevice asset = new IotDevice();
//
//		asset.setValue(value);
//		return asset;
//	}

	public String getAssetID() {
		return assetID;
	}

	public String getName() {
		return name;
	}

	public String getStatus() {
		return status;
	}

	public String getTemperature() {
		return temperature;
	}

	public String getLock() {
		return lock;
	}

	public String toJSONString() {
		return new JSONObject(this).toString();
	}

}
