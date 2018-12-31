package lesx.db;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxListenerType;
import lesx.property.properties.ELesxPropertyKeys;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxBusiness;
import lesx.property.properties.LesxComponent;
import lesx.property.properties.LesxResource;
import lesx.property.properties.LesxResourceBusiness;
import lesx.utils.LesxMisc;
import lesx.xml.thread.LesxXMLSaveData;
import lesx.xml.thread.LesxXMLUtils;

public class LesxDBProperties {

  private final static Logger LOGGER = Logger.getLogger(LesxDBProperties.class.getName());

  private ObservableMap<Long, Map<Long, ? extends LesxComponent>> dataMap;
  private ListMultimap<ELesxListenerType, Runnable> listeners;
  private ListMultimap<ELesxListenerType, Runnable> listenersRB;
  private LesxMapChangeListener<Long, Map<Long, ? extends LesxComponent>> resourceListener = new LesxMapChangeListener<>();
  private LesxMapChangeListener<Long, LesxResourceBusiness> rbListener = new LesxMapChangeListener<>(false);
  private ObservableMap<Long, LesxResourceBusiness> mapRB;
  private boolean businessResourceMapBuild;
  private Runnable postSaved;
  private boolean resourceSaved;
  private boolean businessSaved;
  private static final LocalDate NOW = LocalDate.now();

  public LesxDBProperties() {
    dataMap = FXCollections.observableHashMap();
    listeners = ArrayListMultimap.create();
    listenersRB = ArrayListMultimap.create();
    mapRB = FXCollections.observableHashMap();
    dataMap.addListener(resourceListener);
    mapRB.addListener(rbListener);
  }

  public void setDataMap(Map<Long, Map<Long, ? extends LesxComponent>> map) {
    Preconditions.checkNotNull(map, "Map must not be null");
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
  public synchronized void setResourceMap(Map<Long, LesxResource> data) {
    Preconditions.checkNotNull(data, "Map must not be null");
    dataMap.removeListener(resourceListener);
    dataMap.remove(ELesxPropertyKeys.RESOURCE.getValue());
    dataMap.addListener(resourceListener);
    dataMap.put(ELesxPropertyKeys.RESOURCE.getValue(), data);
    businessResourceMapBuild = false;
  }

  public void addBusiness(LesxBusiness business) {
    final LesxBusiness temp = business.clone();
    getBusinessMap().put(temp.getId(), temp);
    businessResourceMapBuild = false;
  }

  public void removeBusiness(LesxBusiness business) {
    getBusinessMap().remove(business.getId());
    businessResourceMapBuild = false;
  }

  public void setBusinessMap(Map<Long, LesxBusiness> priceMap) {
    dataMap.removeListener(resourceListener);
    dataMap.remove(ELesxPropertyKeys.BUSINESS.getValue());
    dataMap.addListener(resourceListener);
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
        if (isBirthDay(entry.getValue()
            .getBirthday())) {
          names.add(entry.getValue()
              .getName());
        }
      }
    }
    return names;
  }

  private boolean isBirthDay(String birthday) {
    LocalDate resourceDate = LocalDate
        .parse(birthday, DateTimeFormatter.ofPattern(LesxMessage.getMessage("DATE-FORMATTER_PERIOD_DATE_FORMAT"), Locale.ENGLISH));
    if (resourceDate != null) {
      return NOW.getMonth() == resourceDate.getMonth() && NOW.getDayOfMonth() == resourceDate.getDayOfMonth();
    }
    return false;
  }

  public synchronized void buildBusinessResourceMap() {
    LOGGER.log(Level.INFO, "Called buildBusinessResourceMap");
    mapRB.removeListener(rbListener);
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
    mapRB.addListener(rbListener);
  }

  public Map<Long, LesxResourceBusiness> getBusinessResourceMap() {
    LOGGER.log(Level.INFO, "Called getBusinessResourceMap");
    if (!businessResourceMapBuild) {
      buildBusinessResourceMap();
    }
    return mapRB;
  }

  public synchronized void setBusinessResourceMap(Map<Long, LesxResourceBusiness> mapRB) {
    LOGGER.log(Level.INFO, "Called setBusinessResourceMap");
    this.mapRB.removeListener(rbListener);
    this.mapRB.clear();
    this.mapRB.addListener(rbListener);
    this.mapRB.putAll(mapRB);
    businessResourceMapBuild = true;
  }

  public boolean isBusinessResourceMapBuild() {
    LOGGER.log(Level.INFO, "Called isBusinessResourceMapBuild");
    return businessResourceMapBuild;
  }

  public void setListener(ELesxListenerType type, Runnable run) {
    LOGGER.log(Level.INFO, "Called setListener");
    listeners.put(type, run);
  }

  public void removeListener(ELesxListenerType type, Runnable run) {
    LOGGER.log(Level.INFO, "Called removeListener");
    listeners.remove(type, run);
  }

  public void setListenerRB(ELesxListenerType type, Runnable run) {
    LOGGER.log(Level.INFO, "Called setListenerRB");
    listenersRB.put(type, run);
  }

  public void removeListenerRB(ELesxListenerType type, Runnable run) {
    LOGGER.log(Level.INFO, "Called removeListenerRB");
    listenersRB.remove(type, run);
  }

  private class LesxMapChangeListener<V, K> implements MapChangeListener<V, K> {

    private boolean isResource = true;

    public LesxMapChangeListener() {
      //nothing
    }

    public LesxMapChangeListener(boolean isResource) {
      this.isResource = isResource;
    }

    @Override
    public void onChanged(Change<? extends V, ? extends K> change) {
      List<Runnable> runnables = isResource ? listeners.get(ELesxListenerType.UPDATE) : listenersRB.get(ELesxListenerType.UPDATE);
      if (!LesxMisc.isEmpty(runnables)) {
        LOGGER.log(Level.INFO, "Executing Runnable -- Cache Action (Updated)");
        runnables.forEach(run -> run.run());
      }
      if (change.wasAdded()) {
        runnables = isResource ? listeners.get(ELesxListenerType.ADD) : listenersRB.get(ELesxListenerType.ADD);
        if (!LesxMisc.isEmpty(runnables)) {
          LOGGER.log(Level.INFO, "Executing Runnable -- Cache Action (Added)");
          runnables.forEach(run -> run.run());
        }
      }
      if (change.wasRemoved()) {
        runnables = isResource ? listeners.get(ELesxListenerType.REMOVE) : listenersRB.get(ELesxListenerType.REMOVE);
        if (!LesxMisc.isEmpty(runnables)) {
          LOGGER.log(Level.INFO, "Executing Runnable -- Cache Action (Removed)");
          runnables.forEach(run -> run.run());
        }
      }
    }
  }

  public void persist(Runnable postSave) {
    LOGGER.log(Level.INFO, "Called persist");
    this.postSaved = postSave;
    LesxXMLSaveData saveResourceThread = new LesxXMLSaveData(getResourceMap().values(), ELesxUseCase.UC_XML_RESOURCE);
    saveResourceThread.setOnSucceeded(obs -> {
      resourceSaved = true;
      isSavedComplete();
    });
    LesxXMLSaveData saveBusinessThread = new LesxXMLSaveData(getBusinessMap().values(), ELesxUseCase.UC_XML_BUSINESS);
    saveBusinessThread.setOnSucceeded(obs -> {
      businessSaved = true;
      isSavedComplete();
    });
    saveResourceThread.start();
    saveBusinessThread.start();
  }

  public void refresh() {
    LOGGER.log(Level.INFO, "Called refresh");
    dataMap.removeListener(resourceListener);
    mapRB.removeListener(rbListener);
    mapRB.clear();
    dataMap.clear();
    dataMap.addListener(resourceListener);
    mapRB.addListener(rbListener);
    try {
      LesxXMLUtils.importAllXMLFileToLesxProperty(() -> {
        //Nothing
      });
    }
    catch (RuntimeException ex) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_READER_DATA"), ex);
      ex.printStackTrace();
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-XML_READER_DATA"), e);
      e.printStackTrace();
    }
  }

  private void isSavedComplete() {
    if (resourceSaved && businessSaved) {
      postSaved.run();
      resourceSaved = false;
      businessSaved = false;
    }
  }

}
