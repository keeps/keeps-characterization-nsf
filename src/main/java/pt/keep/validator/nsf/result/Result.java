package pt.keep.validator.nsf.result;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import pt.keep.validator.nsf.utils.jaxb.MapAdapter;

@XmlRootElement(name = "nsfCharacterizationResult")
@XmlType(propOrder = {"features", "validationInfo"})
public class Result {

  private int allDocumentsCount;
	
	private ValidationInfo validationInfo;
	
	 private Map<String,String> features;
	  @XmlElement
	  @XmlJavaTypeAdapter(MapAdapter.class)
	  public Map<String, String> getFeatures() {
	      return features;
	  }
	  public void setFeatures(Map<String, String> features) {
	      this.features = features;
	  }
	  
	

	@XmlElement(required=true)
	public ValidationInfo getValidationInfo() {
		return validationInfo;
	}

	public void setValidationInfo(ValidationInfo validationInfo) {
		this.validationInfo = validationInfo;
	}
	
	
	

}
