package lesx.datamodel;

import java.util.Map;

import lesx.property.properties.LesxResource;

public class LesxResourcesDataModel implements ILesxDataModel<LesxResource> {

  private Map<Long, LesxResource> map;

  @Override
  public Map<Long, LesxResource> getMap() {
    return map;
  }

  @Override
  public void setMap(Map<Long, LesxResource> map) {
    this.map = map;
  }

  @Override
  public void persist(Runnable run) {
    // TODO Persist

  }

}
