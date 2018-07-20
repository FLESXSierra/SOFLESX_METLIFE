package lesx.property.properties;

import java.time.LocalDate;

import lesx.utils.LesxMisc;
import lesx.utils.LesxPropertyUtils;

public class LesxResourceBusiness extends LesxComponent implements Cloneable {

  private LesxResource resource;
  private LesxBusiness business;
  private String month;

  public LesxResourceBusiness() {
    resource = new LesxResource();
    business = new LesxBusiness();
    month = "";
    setKey(ELesxPropertyKeys.RESOURCE_BUSINESS);
  }

  public LesxResource getResource() {
    return resource;
  }

  public void setResource(LesxResource resource) {
    this.resource = resource;
  }

  public LesxBusiness getBusiness() {
    return business;
  }

  public void setBusiness(LesxBusiness business) {
    this.business = business;
  }

  public String getMonth() {
    return month;
  }

  public void setMonth(String month) {
    this.month = month;
  }

  public Long getComision() {
    Long comision = null;
    if (getResource() != null && getBusiness() != null && getBusiness().getNbs() != null) {
      int month = LocalDate.now()
          .getMonthValue()
          - LocalDate.parse(getResource().getRegistration_date(), LesxPropertyUtils.FORMATTER)
              .getMonthValue();
      switch (month) {
        case 0:
          comision = getBusiness().getNbs() / 24;
          break;
        case 1:
          comision = (getBusiness().getNbs() / 60);
          break;
        case 2:
          comision = (getBusiness().getNbs() / 60);
          break;
        case 3:
          comision = (getBusiness().getNbs() / 120);
          break;
        default:
          break;
      }
    }
    return comision;
  }

  @Override
  public String toString() {
    if (!LesxMisc.isEmptyString(resource.getName())) {
      return resource.getName();
    }
    return super.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || this == null) {
      return this == null && obj == null;
    }
    if (obj instanceof LesxResourceBusiness) {
      if (((LesxResourceBusiness) obj).getBusiness() != null && this.getBusiness() != null) {
        return LesxMisc.equals(
            ((LesxResourceBusiness) obj).getBusiness()
                .getId(),
            this.getBusiness()
                .getId());
      }
      else {
        return LesxMisc.equals(((LesxResourceBusiness) obj).getBusiness(), this.getBusiness());
      }
    }
    return false;
  }

  public static LesxResourceBusiness of(LesxResource resource, LesxBusiness business) {
    LesxResourceBusiness temp = new LesxResourceBusiness();
    temp.setKey(ELesxPropertyKeys.RESOURCE_BUSINESS);
    temp.setBusiness(business);
    temp.setResource(resource);
    return temp;
  }

  @Override
  public LesxResourceBusiness clone() {
    return (LesxResourceBusiness) super.clone();
  }

}
