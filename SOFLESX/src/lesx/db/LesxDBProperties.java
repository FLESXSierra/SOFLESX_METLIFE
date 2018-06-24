package lesx.db;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxPropertyKeys;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxBusiness;
import lesx.property.properties.LesxComponent;
import lesx.property.properties.LesxResource;
import lesx.property.properties.LesxResourceBusiness;
import lesx.utils.LesxMisc;
import lesx.xml.thread.LesxXMLSaveData;

public class LesxDBProperties {

  private final static Logger LOGGER = Logger.getLogger(LesxDBProperties.class.getName());

  private Map<Long, Map<Long, ? extends LesxComponent>> dataMap;
  private Map<Long, LesxResourceBusiness> mapRB;
  private boolean businessResourceMapBuild;
  private static final String NOW = LocalDate.now()
      .format(DateTimeFormatter.ofPattern(LesxMessage.getMessage("DATE-FORMATTER_PERIOD_DATE_FORMAT"), Locale.ENGLISH));

  public LesxDBProperties() {
    dataMap = new HashMap<>();
    mapRB = new HashMap<>();
  }

  public void setDataMap(Map<Long, Map<Long, ? extends LesxComponent>> map) {
    dataMap.clear();
    dataMap.putAll(map);
    businessResourceMapBuild = false;
  }

  public void addDataMap(Map<Long, Map<Long, ? extends LesxComponent>> map) {
    if (!LesxMisc.isEmpty(map)) {
      dataMap.putAll(map);
      businessResourceMapBuild = false;
    }
  }

  /**
   * Assigns a new map into the DBProperties.
   *
   * @param data Map<Long,LesxCostumer>
   */
  public void setResourceMap(Map<Long, LesxResource> data) {
    dataMap.remove(ELesxPropertyKeys.RESOURCE.getValue());
    dataMap.put(ELesxPropertyKeys.RESOURCE.getValue(), data);
    businessResourceMapBuild = false;
  }

  public void setBusinessMap(Map<Long, LesxBusiness> priceMap) {
    dataMap.remove(ELesxPropertyKeys.BUSINESS.getValue());
    dataMap.put(ELesxPropertyKeys.BUSINESS.getValue(), priceMap);
    businessResourceMapBuild = false;
  }

  @SuppressWarnings("unchecked")
  public Map<Long, LesxResource> getResourceMap() {
    LOGGER.log(Level.INFO, "Called getResourceMap");
    return (Map<Long, LesxResource>) dataMap.get(ELesxPropertyKeys.RESOURCE.getValue());
  }

  @SuppressWarnings("unchecked")
  public Map<Long, LesxBusiness> getBusinessMap() {
    LOGGER.log(Level.INFO, "Called getBusinessMap");
    return (Map<Long, LesxBusiness>) dataMap.get(ELesxPropertyKeys.BUSINESS.getValue());
  }

  /**
   * Gets a list of costumers
   *
   * @param keys ID of the costumers.
   * @return a list of costumers
   */
  public List<LesxResource> getResourceMap(List<Long> keys) {
    List<LesxResource> result = new ArrayList<>();
    LesxResource value;
    Map<Long, LesxResource> costumerMap = getResourceMap();
    for (Long key : keys) {
      value = costumerMap.get(key);
      if (!LesxMisc.isEmpty(value)) {
        result.add(value);
      }
      else {
        LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-NO_COSTUMER_FOUND_BY_KEY", String.valueOf(key)));
      }
    }
    return result;
  }

  /**
   * Saves into XML the given data map.
   *
   * @param resourceMap Map containing data
   * @param run Runnable that runs on succeeded or failed
   */
  public void saveResourceXML(Map<Long, LesxResource> resourceMap, Runnable run) {
    LOGGER.log(Level.INFO, "Called saveResourceXML");
    setResourceMap(resourceMap);
    LesxXMLSaveData saveThread = new LesxXMLSaveData(resourceMap.values(), ELesxUseCase.UC_XML_RESOURCE);
    saveThread.start(); // Saving loading message
    saveThread.setOnSucceeded(obs -> run.run());
    saveThread.setOnFailed(obs -> run.run());
  }

  public void saveBusinessXML(Map<Long, LesxBusiness> businessMap, Runnable run) {
    LOGGER.log(Level.INFO, "Called saveBusinessXML");
    setBusinessMap(businessMap);
    LesxXMLSaveData saveThread = new LesxXMLSaveData(businessMap.values(), ELesxUseCase.UC_XML_BUSINESS);
    saveThread.start();
    saveThread.setOnSucceeded(obs -> run.run());
    saveThread.setOnFailed(obs -> run.run());
  }

  @SuppressWarnings("unchecked")
  public List<String> getBirthdayNames() {
    LOGGER.log(Level.INFO, "Called getBirthdayNames");
    final Map<Long, LesxResource> map = (Map<Long, LesxResource>) dataMap.get(ELesxPropertyKeys.RESOURCE.getValue());
    final List<String> names = new ArrayList<>();
    if (!LesxMisc.isEmpty(map)) {
      for (Entry<Long, LesxResource> entry : map.entrySet()) {
        if (NOW.equals(entry.getValue()
            .getBirthday())) {
          names.add(entry.getValue()
              .getName());
        }
      }
    }
    return names;
  }

  public void buildBusinessResourceMap() {
    LOGGER.log(Level.INFO, "Called buildBusinessResourceMap");
    mapRB.clear();
    if (getResourceMap() != null && getBusinessMap() != null) {
      for (Entry<Long, LesxBusiness> entry : getBusinessMap().entrySet()) {
        if (getResourceMap().keySet()
            .contains(entry.getValue()
                .getResource_id())) {
          mapRB.put(entry.getKey(), LesxResourceBusiness.of(getResourceMap().get(entry.getValue()
              .getResource_id()), entry.getValue()));
        }
      }
      businessResourceMapBuild = true;
    }
    else {
      businessResourceMapBuild = false;
      LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FOUND_NULL_DATAMODEL"));
    }
  }

  public Map<Long, LesxResourceBusiness> getBusinessResourceMap() {
    LOGGER.log(Level.INFO, "Called getBusinessResourceMap");
    if (!businessResourceMapBuild) {
      buildBusinessResourceMap();
    }
    return mapRB;
  }

  public void setBusinessResourceMap(Map<Long, LesxResourceBusiness> mapRB) {
    LOGGER.log(Level.INFO, "Called setBusinessResourceMap");
    this.mapRB = mapRB;
    businessResourceMapBuild = true;
  }

  public boolean isBusinessResourceMapBuild() {
    LOGGER.log(Level.INFO, "Called isBusinessResourceMapBuild");
    return businessResourceMapBuild;
  }

}
