package lesx.xml.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lesx.property.properties.LesxCancelledBusiness;
import lesx.utils.LesxString;

@XmlAccessorType(XmlAccessType.FIELD)
public class LesxCancelledBusinessXMLParser {

  @XmlElement(name = LesxString.ATTR_XML_VALUE)
  private Boolean value;
  @XmlElement(name = LesxString.ATTR_XML_DATE)
  private String date;

  public LesxCancelledBusinessXMLParser() {
    //Nothing
  }

  public LesxCancelledBusinessXMLParser(LesxCancelledBusiness cancelled) {
    value = cancelled != null && cancelled.getValue() != null ? cancelled.getValue() : null;
    date = cancelled != null && cancelled.getDate() != null ? cancelled.getDate() : null;
  }

  /**
   * @return the value
   */
  public Boolean getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(Boolean value) {
    this.value = value;
  }

  /**
   * @return the date
   */
  public String getDate() {
    return date;
  }

  /**
   * @param date the date to set
   */
  public void setDate(String date) {
    this.date = date;
  }

}
