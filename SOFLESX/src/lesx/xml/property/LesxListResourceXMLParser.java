package lesx.xml.property;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lesx.utils.LesxString;

@XmlRootElement(name = LesxString.ROOT_ELEMENT_XML)
@XmlAccessorType(XmlAccessType.FIELD)
public class LesxListResourceXMLParser {

  @XmlElement(name = LesxString.ELEMENT_XML_COSTUMER)
  private List<LesxResourceXMLParser> resource;

  public List<LesxResourceXMLParser> getResources() {
    return resource;
  }

  public void setResources(List<LesxResourceXMLParser> resource) {
    this.resource = resource;
  }

}
