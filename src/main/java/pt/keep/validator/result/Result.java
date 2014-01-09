package pt.keep.validator.result;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "nsfCharacterizationResult")
@XmlType(propOrder = { "validationInfo","data"})
public class Result {

	private String data;
	private ValidationInfo validationInfo;
	
	

	
	@XmlElement(required=false)
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@XmlElement(required=true)
	public ValidationInfo getValidationInfo() {
		return validationInfo;
	}

	public void setValidationInfo(ValidationInfo validationInfo) {
		this.validationInfo = validationInfo;
	}
	
	
	

}
