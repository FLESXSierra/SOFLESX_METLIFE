package lesx.datamodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lesx.property.properties.ELesxLocations;
import lesx.property.properties.LesxBusiness;
import lesx.property.properties.LesxResource;
import lesx.utils.LesxMisc;
import lesx.utils.LesxPair;
import lesx.utils.LesxString;

public class LesxResourcesDataModel implements ILesxDataModel<LesxResource> {

  private Map<Long, LesxResource> map;
  private ELesxLocations locationsSelected;
  private LesxResource resourceSelected;

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

  /**
   * Returns the costumers that belongs to the location given, if getChilderAlso is {@code true} I'll accepts the
   * children of the locations also.
   *
   * @param location Location to verify precedence
   * @param getChilderAlso Search for children also
   * @return List of costumer filtered. Not {@code null}
   */
  public List<LesxResource> getValuesByLocation(ELesxLocations location, boolean getChilderAlso) {
    final List<LesxResource> result = new ArrayList<>();
    if (map != null) {
      if (getChilderAlso) {
        if (LesxMisc.equals(location, ELesxLocations.COLOMBIA)) {
          result.addAll(map.values());
          return result;
        }
        for (Entry<Long, LesxResource> entry : map.entrySet()) {
          Long tempKey = entry.getValue()
              .getLocation();
          if (verifyPrecedenceRecursively(tempKey, location)) {
            result.add(entry.getValue());
          }
        }
      }
      else {
        for (Entry<Long, LesxResource> entry : map.entrySet()) {
          if (LesxMisc.equals(entry.getValue()
              .getPropertyByName(LesxString.PROPERTY_LOCATION)
              .getValue(), location.getKey())) {
            result.add(entry.getValue());
          }
        }
      }
    }
    return result;
  }

  /**
   * Verify keys of the parent and children.
   *
   * @param key Long
   * @param location ELesxLocations
   * @return
   */
  private boolean verifyPrecedenceRecursively(Long keyToSearch, ELesxLocations location) {
    final Long keyFiltered = location.getKey();
    final ELesxLocations locationToSearch = ELesxLocations.getLocation(keyToSearch);
    final Long parentKey = locationToSearch.getParentKey();
    if (LesxMisc.equals(keyToSearch, ELesxLocations.COLOMBIA.getKey())) {
      return false;
    }
    if (LesxMisc.equals(parentKey, keyFiltered) || LesxMisc.equals(keyToSearch, keyFiltered)) {
      return true;
    }
    return verifyPrecedenceRecursively(ELesxLocations.getParentLocation(locationToSearch)
        .getKey(), location);
  }

  public Set<ELesxLocations> getDataLocations() {
    final Set<ELesxLocations> locations = new HashSet<>();
    if (map != null) {
      for (LesxResource costumer : map.values()) {
        locations.add(ELesxLocations.getLocation(costumer.getLocation()));
      }
    }
    return locations;
  }

  public ELesxLocations getLocationsSelected() {
    return locationsSelected;
  }

  public void setLocationsSelected(ELesxLocations locationsSelected) {
    this.locationsSelected = locationsSelected;
  }

  public void deleteSelectedCostumer() {
    if (resourceSelected != null) {
      map.remove(resourceSelected.getId());
    }
  }

  public LesxResource getResourceSelected() {
    return resourceSelected;
  }

  public void setResourceSelected(LesxResource resourceSelected) {
    if (resourceSelected != null) {
      this.resourceSelected = resourceSelected.clone();
    }
    else {
      this.resourceSelected = null;
    }
  }

  public LesxPair<LesxResource, LesxBusiness> createResourceBusinessPair() {
    return null;// TODO LesxPair
  }

}
