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
  @XmlElement(name = LesxString.ATTR_XML_RESOURCE_ID)
  private Long resource_id;
  @XmlElement(name = LesxString.ATTR_XML_DATE)
  private String date;
  @XmlElement(name = LesxString.ATTR_XML_CANCELLED)
  private LesxCancelledBusinessXMLParser cancelled;

  public LesxBusinessXMLParser() {
    //Nothing
  }

  public LesxBusinessXMLParser(LesxBusiness business) {
    this.id = business.getId();
    this.producto = new LesxProductTypeXMLParser(business.getProduct());
    this.resource_id = business.getResource_id();
    this.date = business.getDate();
    this.cancelled = new LesxCancelledBusinessXMLParser(business.getCancelled());
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

  public Long getResource_id() {
    return resource_id;
  }

  public void setResource_id(Long resource_id) {
    this.resource_id = resource_id;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public LesxCancelledBusinessXMLParser getCancelled() {
    return cancelled;
  }

  public void setCancelled(LesxCancelledBusinessXMLParser cancelled) {
    this.cancelled = cancelled;
  }

}
