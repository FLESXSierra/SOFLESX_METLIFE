package lesx.datamodel;

import java.util.Map;

public interface ILesxDataModel<T> {

  /**
   * Gets the DataModel Map
   * 
   * @return Map<Long, T>
   */
  public abstract Map<Long, T> getMap();

  /**
   * Sets the DataModel Map
   * 
   * @param map Map<Long, T>
   */
  public abstract void setMap(Map<Long, T> map);

  /**
   * Persist the data.
   * 
   * @param run after persist.
   */
  public abstract void persist(Runnable run);

}
