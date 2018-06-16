package lesx.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxPropertyKeys;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxBusiness;
import lesx.property.properties.LesxComponent;
import lesx.property.properties.LesxResource;
import lesx.utils.LesxMisc;
import lesx.xml.thread.LesxXMLSaveData;

public class LesxDBProperties {

  private final static Logger LOGGER = Logger.getLogger(LesxDBProperties.class.getName());

  private static Map<Long, Map<Long, ? extends LesxComponent>> dataMap;

  public LesxDBProperties() {
    dataMap = new HashMap<>();
  }

  public void setDataMap(Map<Long, Map<Long, ? extends LesxComponent>> map) {
    dataMap.clear();
    dataMap.putAll(map);
  }

  public void addDataMap(Map<Long, Map<Long, ? extends LesxComponent>> map) {
    if (!LesxMisc.isEmpty(map)) {
      dataMap.putAll(map);
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
  }

  public void setBusinessMap(Map<Long, LesxBusiness> priceMap) {
    dataMap.remove(ELesxPropertyKeys.BUSINESS.getValue());
    dataMap.put(ELesxPropertyKeys.BUSINESS.getValue(), priceMap);
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
   * @param data Map containing data
   * @param run Runnable that runs on succeeded or failed
   */
  public void saveResourceXML(Map<Long, LesxResource> data, Runnable run) {
    LOGGER.log(Level.INFO, "Called saveResourceXML");
    setResourceMap(data);
    LesxXMLSaveData saveThread = new LesxXMLSaveData(data.values(), ELesxUseCase.UC_XML_RESOURCE);
    saveThread.start(); // Saving loading message
    saveThread.setOnSucceeded(obs -> run.run());
    saveThread.setOnFailed(obs -> run.run());
  }

  public void saveBusinessXML(Map<Long, LesxBusiness> priceMap, Runnable run) {
    LOGGER.log(Level.INFO, "saveBusinessXML");
    setBusinessMap(priceMap);
    LesxXMLSaveData saveThread = new LesxXMLSaveData(priceMap.values(), ELesxUseCase.UC_XML_BUSINESS);
    saveThread.start();
    saveThread.setOnSucceeded(obs -> run.run());
    saveThread.setOnFailed(obs -> run.run());
  }

}
