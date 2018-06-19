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
  @XmlElement(name = LesxString.ATTR_XML_PRODUCT)
  private Integer producto;
  @XmlElement(name = LesxString.ATTR_XML_PRICE)
  private Long prima;
  @XmlElement(name = LesxString.ATTR_XML_NBS)
  private Long nbs;
  @XmlElement(name = LesxString.ATTR_XML_NBS)
  private Long resource_id;

  public LesxBusinessXMLParser() {

  }

  public LesxBusinessXMLParser(LesxBusiness business) {
    this.id = business.getId();
    this.producto = business.getProduct()
        .getKey();
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

  public Integer getProducto() {
    return producto;
  }

  public void setProducto(Integer producto) {
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
