package lesx.xml.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lesx.property.properties.LesxResource;
import lesx.utils.LesxString;

@XmlAccessorType(XmlAccessType.FIELD)
public class LesxResourceXMLParser {

  @XmlAttribute(name = LesxString.ATTR_XML_ID)
  private Long id;
  @XmlAttribute(name = LesxString.ATTR_XML_BUSINESS_ID)
  private Long business_id;
  @XmlElement(name = LesxString.ATTR_XML_NAME)
  private String name;
  @XmlElement(name = LesxString.ATTR_XML_CC)
  private Long cc;
  @XmlElement(name = LesxString.ATTR_XML_LOCATION)
  private Long location;
  @XmlAttribute(name = LesxString.ATTR_XML_SOLICITATION)
  private Long solicitud;
  @XmlElement(name = LesxString.ATTR_XML_BIRTHDAY)
  private String birthday;
  @XmlElement(name = LesxString.ATTR_XML_REGISTRATION)
  private String registration_date;

  public LesxResourceXMLParser() {

  }

  public LesxResourceXMLParser(LesxResource resource) {
    this.id = resource.getId();
    this.name = resource.getName();
    this.cc = resource.getCc();
    this.location = resource.getLocation();
    this.business_id = resource.getBusiness_id();
    this.solicitud = resource.getSolicitud();
    this.birthday = resource.getBirthday();
    this.registration_date = resource.getRegistration_date();
  }

  public Long getLocation() {
    return location;
  }

  public void setLocation(Long location) {
    this.location = location;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getCc() {
    return cc;
  }

  public void setCc(Long cc) {
    this.cc = cc;
  }

  public Long getBusiness_id() {
    return business_id;
  }

  public void setBusiness_id(Long business_id) {
    this.business_id = business_id;
  }

  public Long getSolicitud() {
    return solicitud;
  }

  public void setSolicitud(Long solicitud) {
    this.solicitud = solicitud;
  }

  public String getBirthday() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday = birthday;
  }

  public String getRegistration_date() {
    return registration_date;
  }

  public void setRegistration_date(String registration_date) {
    this.registration_date = registration_date;
  }

}
