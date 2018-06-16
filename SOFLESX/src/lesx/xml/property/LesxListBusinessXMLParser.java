package lesx.xml.property;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lesx.utils.LesxString;

@XmlRootElement(name = LesxString.ROOT_ELEMENT_XML)
@XmlAccessorType(XmlAccessType.FIELD)
public class LesxListBusinessXMLParser {

  @XmlElement(name = LesxString.ELEMENT_XML_BUSINESS)
  private List<LesxBusinessXMLParser> business;

  public List<LesxBusinessXMLParser> getBusiness() {
    return business;
  }

  public void setBusiness(List<LesxBusinessXMLParser> business) {
    this.business = business;
  }

}
