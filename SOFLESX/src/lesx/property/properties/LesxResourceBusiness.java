package lesx.property.properties;

public class LesxResourceBusiness {

  private LesxResource resource;
  private LesxBusiness business;
  private String month;

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

  public static LesxResourceBusiness of(LesxResource resource, LesxBusiness business) {
    LesxResourceBusiness temp = new LesxResourceBusiness();
    temp.setBusiness(business);
    temp.setResource(resource);
    return temp;
  }

}
