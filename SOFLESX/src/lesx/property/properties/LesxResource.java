package lesx.property.properties;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

import lesx.gui.message.LesxMessage;
import lesx.utils.LesxString;

public class LesxResource extends LesxComponent {

  private LesxProperty id;
  private LesxProperty business_id;
  private LesxProperty solicitud;
  private LesxProperty name;
  private LesxProperty cc;
  private LesxProperty birthday;
  private LesxProperty location;
  private LesxProperty registration_date;
  final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LesxMessage.getMessage("DATE-FORMATTER_PERIOD_DATE_FORMAT"), Locale.ENGLISH);

  public LesxResource() {
    initializeProperty();
  }

  private void initializeProperty() {
    id = new LesxProperty();
    id.setType(ELesxPropertyType.LONG);
    id.setName(LesxString.PROPERTY_ID);
    id.setMandatory(true);
    id.setReadOnly(true);
    id.setUnique(true);
    id.setVisible(false);
    business_id = new LesxProperty();
    business_id.setType(ELesxPropertyType.LONG);
    business_id.setName(LesxString.PROPERTY_BUSINESS_ID);
    business_id.setMandatory(true);
    business_id.setReadOnly(true);
    business_id.setUnique(true);
    business_id.setVisible(false);
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
    location.setValue(ELesxLocations.COLOMBIA);
    location.setMandatory(true);
    birthday = new LesxProperty();
    birthday.setType(ELesxPropertyType.DATE);
    birthday.setName(LesxString.PROPERTY_BIRTHDAY);
    birthday.setMandatory(true);
    registration_date = new LesxProperty();
    registration_date.setType(ELesxPropertyType.DATE);
    registration_date.setName(LesxString.PROPERTY_REGISTER_DATE);
    registration_date.setValue(LocalDate.parse(LocalDate.now()
        .toString(), formatter));
    registration_date.setMandatory(true);
    setPropertyValues(Arrays.asList(id, business_id, solicitud, name, cc, location, birthday, registration_date));
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

  public Long getBusiness_id() {
    return (Long) business_id.getValue();
  }

  public void setBusiness_id(Long business_id) {
    this.business_id.setValue(business_id);
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

}
