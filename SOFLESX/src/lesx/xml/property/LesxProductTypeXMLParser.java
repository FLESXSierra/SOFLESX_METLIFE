package lesx.xml.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lesx.property.properties.LesxProductType;
import lesx.utils.LesxString;

@XmlAccessorType(XmlAccessType.FIELD)
public class LesxProductTypeXMLParser {

  @XmlElement(name = LesxString.ATTR_XML_PRIMA_VIDA)
  private Long primaVida;
  @XmlElement(name = LesxString.ATTR_XML_TYPE_VIDA)
  private Integer typeVida;
  @XmlElement(name = LesxString.ATTR_XML_PRIMA_AP)
  private Long primaAP;
  @XmlElement(name = LesxString.ATTR_XML_TYPE_AP)
  private Integer typeAP;

  public LesxProductTypeXMLParser() {
    //Nothing
  }

  public LesxProductTypeXMLParser(LesxProductType product) {
    typeVida = product.getTypeVida()
        .getKey();
    primaVida = product.getPrimaVida();
    typeAP = product.getTypeAP()
        .getKey();
    primaAP = product.getPrimaAP();
  }

  public Long getPrimaVida() {
    return primaVida;
  }

  public void setPrimaVida(Long prima) {
    this.primaVida = prima;
  }

  public Integer getTypeVida() {
    return typeVida;
  }

  public void setTypeVida(Integer type) {
    this.typeVida = type;
  }

  public Long getPrimaAP() {
    return primaAP;
  }

  public void setPrimaAP(Long prima) {
    this.primaAP = prima;
  }

  public Integer getTypeAP() {
    return typeAP;
  }

  public void setTypeAP(Integer type) {
    this.typeAP = type;
  }

}
