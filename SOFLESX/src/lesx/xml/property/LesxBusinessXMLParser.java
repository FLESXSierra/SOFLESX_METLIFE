package lesx.xml.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lesx.property.properties.LesxBusiness;
import lesx.utils.LesxString;

@XmlAccessorType(XmlAccessType.FIELD)
public class LesxBusinessXMLParser {

  @XmlAttribute(name = LesxString.ATTR_XML_ID)
  private Long id;
  @XmlElement(name = LesxString.ATTR_XML_PRODUCT_TYPE)
  private LesxProductTypeXMLParser producto;
  @XmlElement(name = LesxString.ATTR_XML_PRICE)
  private Long prima;
  @XmlElement(name = LesxString.ATTR_XML_NBS)
  private Long nbs;
  @XmlElement(name = LesxString.ATTR_XML_RESOURCE_ID)
  private Long resource_id;

  public LesxBusinessXMLParser() {
    //Nothing
  }

  public LesxBusinessXMLParser(LesxBusiness business) {
    this.id = business.getId();
    this.producto = new LesxProductTypeXMLParser(business.getProduct());
    this.prima = business.getPrima();
    this.nbs = business.getNbs();
    this.resource_id = business.getResource_id();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LesxProductTypeXMLParser getProducto() {
    return producto;
  }

  public void setProducto(LesxProductTypeXMLParser producto) {
    this.producto = producto;
  }

  public Long getPrima() {
    return prima;
  }

  public void setPrima(Long prima) {
    this.prima = prima;
  }

  public Long getNbs() {
    return nbs;
  }

  public void setNbs(Long nbs) {
    this.nbs = nbs;
  }

  public Long getResource_id() {
    return resource_id;
  }

  public void setResource_id(Long resource_id) {
    this.resource_id = resource_id;
  }

}
