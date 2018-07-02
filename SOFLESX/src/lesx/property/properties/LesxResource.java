package lesx.property.properties;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import lesx.gui.message.LesxMessage;
import lesx.utils.LesxString;
import lesx.xml.property.LesxResourceXMLParser;

public class LesxResource extends LesxComponent implements Cloneable {

  private final static Logger LOGGER = Logger.getLogger(LesxResource.class.getName());

  private LesxProperty id;
  private LesxProperty solicitud;
  private LesxProperty name;
  private LesxProperty cc;
  private LesxProperty birthday;
  private LesxProperty location;
  private LesxProperty registration_date;
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LesxMessage.getMessage("DATE-FORMATTER_PERIOD_DATE_FORMAT"), Locale.ENGLISH);

  public LesxResource() {
    initializeProperty();
  }

  /**
   * Constructor to create property on the property sheet
   *
   * @param properties List of edited properties
   */
  public LesxResource(Collection<LesxProperty> properties) {
    initializeProperty();
    for (LesxProperty property : properties) {
      if (id.propertyEquals(property)) {
        id.setValue(property.getValue());
      }
      if (solicitud.propertyEquals(property)) {
        solicitud.setValue(property.getValue());
      }
      if (name.propertyEquals(property)) {
        name.setValue(property.getValue());
      }
      if (cc.propertyEquals(property)) {
        cc.setValue(property.getValue());
      }
      if (birthday.propertyEquals(property)) {
        birthday.setValue(property.getValue());
      }
      if (location.propertyEquals(property)) {
        location.setValue(property.getValue());
      }
      if (registration_date.propertyEquals(property)) {
        registration_date.setValue(property.getValue());
      }
    }
    getPropertyValues().addAll(id, solicitud, name, cc, location, birthday, registration_date);
  }

  public LesxResource(LesxResourceXMLParser parse) {
    initializeProperty();
    if (parse.getId() == null) {
      LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FOUND_NULL_ID"));
      id.setValue(-1L);
    }
    else {
      id.setValue(parse.getId());
    }
    if (parse.getLocation() == null) {
      LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FOUND_NULL_LOCATION"));
      location.setValue(0L);
    }
    else {
      setLocation(parse.getLocation());
    }
    solicitud.setValue(parse.getSolicitud());
    name.setValue(parse.getName());
    cc.setValue(parse.getCc());
    birthday.setValue(parse.getBirthday());
    registration_date.setValue(parse.getRegistration_date());
    setKey(ELesxPropertyKeys.RESOURCE);
  }

  private void initializeProperty() {
    id = new LesxProperty();
    id.setType(ELesxPropertyType.LONG);
    id.setName(LesxString.PROPERTY_ID);
    id.setMandatory(true);
    id.setReadOnly(true);
    id.setUnique(true);
    id.setVisible(false);
    solicitud = new LesxProperty();
    solicitud.setType(ELesxPropertyType.LONG);
    solicitud.setName(LesxString.PROPERTY_SOLICITUD);
    solicitud.setMandatory(true);
    solicitud.setUnique(true);
    name = new LesxProperty();
    name.setType(ELesxPropertyType.TEXT);
    name.setName(LesxString.PROPERTY_NAME);
    name.setMandatory(true);
    cc = new LesxProperty();
    cc.setType(ELesxPropertyType.LONG);
    cc.setName(LesxString.PROPERTY_CC);
    cc.setMandatory(true);
    location = new LesxProperty();
    location.setType(ELesxPropertyType.LOCATION);
    location.setName(LesxString.PROPERTY_LOCATION);
    location.setValue(ELesxLocations.COLOMBIA.getKey());
    location.setMandatory(true);
    birthday = new LesxProperty();
    birthday.setType(ELesxPropertyType.DATE);
    birthday.setName(LesxString.PROPERTY_BIRTHDAY);
    birthday.setMandatory(true);
    registration_date = new LesxProperty();
    registration_date.setType(ELesxPropertyType.DATE);
    registration_date.setName(LesxString.PROPERTY_REGISTER_DATE);
    registration_date.setValue(LocalDate.now()
        .format(formatter)
        .toString());
    registration_date.setMandatory(true);
    getPropertyValues().addAll(id, solicitud, name, cc, location, birthday, registration_date);
    setKey(ELesxPropertyKeys.RESOURCE);
  }

  public String getName() {
    return (String) name.getValue();
  }

  public LesxProperty nameProperty() {
    return name;
  }

  public void setName(String name) {
    this.name.setValue(name);
  }

  public Long getCc() {
    return (Long) cc.getValue();
  }

  public LesxProperty ccProperty() {
    return cc;
  }

  public void setCc(Long cc) {
    this.cc.setValue(cc);
  }

  public Long getLocation() {
    return (Long) location.getValue();
  }

  public LesxProperty locationProperty() {
    return location;
  }

  public void setLocation(Long location) {
    this.location.setValue(location);
  }

  public void setLocation(LesxLocation location) {
    this.location = location.getPropertyByName(LesxString.PROPERTY_ID);
  }

  public Long getId() {
    return (Long) id.getValue();
  }

  public void setId(Long id) {
    this.id.setValue(id);
  }

  public Long getSolicitud() {
    return (Long) solicitud.getValue();
  }

  public void setSolicitud(Long solicitud) {
    this.solicitud.setValue(solicitud);
  }

  public LesxProperty solicitudProperty() {
    return solicitud;
  }

  public String getBirthday() {
    return (String) birthday.getValue();
  }

  public LesxProperty birthdayProperty() {
    return birthday;
  }

  public void setBirthday(String birthday) {
    this.birthday.setValue(birthday);
  }

  public String getRegistration_date() {
    return (String) registration_date.getValue();
  }

  public LesxProperty registration_dateProperty() {
    return registration_date;
  }

  public void setRegistration_date(String registration_date) {
    this.registration_date.setValue(registration_date);
  }

  @Override
  public String toString() {
    return getName();
  }

  @Override
  public LesxResource clone() {
    return (LesxResource) super.clone();
  }

}
