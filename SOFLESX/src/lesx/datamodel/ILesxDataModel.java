package lesx.datamodel;

import java.util.Map;

import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxProperty;

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

  /**
   * Gets the component Selected
   *
   * @return Object
   */
  public abstract Object getComponentSelected();

  /**
   * Sets the component Selected
   */
  public abstract void setComponentSelected(T component);

  /**
   * Search for the property uniqueness by name
   */
  public abstract boolean isUniqueProperty(LesxProperty property, Long keyComponent, ELesxUseCase isCreate);

}
