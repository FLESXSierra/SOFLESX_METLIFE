package lesx.property.properties;

import lesx.utils.LesxMisc;

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

  @Override
  public String toString() {
    if (!LesxMisc.isEmpty(resource.getName())) {
      return resource.getName();
    }
    return super.toString();
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
