package lesx.datamodel;

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

import javafx.scene.control.TreeItem;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxMonth;
import lesx.property.properties.LesxBusiness;
import lesx.property.properties.LesxResource;
import lesx.utils.LesxPair;

public class LesxBusinessResourceDataModel implements ILesxDataModel<LesxPair<LesxResource, LesxBusiness>> {

  private final static Logger LOGGER = Logger.getLogger(LesxBusinessResourceDataModel.class.getName());

  private Map<Long, LesxPair<LesxResource, LesxBusiness>> map = new HashMap<>();
  private Map<Long, LesxResource> resourceMap;
  private Map<Long, LesxBusiness> businessMap;
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LesxMessage.getMessage("DATE-FORMATTER_PERIOD_DATE_FORMAT"), Locale.ENGLISH);

  @Override
  public Map<Long, LesxPair<LesxResource, LesxBusiness>> getMap() {
    return map;
  }

  @Override
  public void setMap(Map<Long, LesxPair<LesxResource, LesxBusiness>> map) {
    this.map = map;

  }

  public void setResourceMap(Map<Long, LesxResource> resourceMap) {
    this.resourceMap = resourceMap;
  }

  public void setBusinessMap(Map<Long, LesxBusiness> businessMap) {
    this.businessMap = businessMap;
  }

  public void buildResourceBusinessPairs() {
    map.clear();
    if (resourceMap != null && businessMap != null) {
      for (Entry<Long, LesxBusiness> entry : businessMap.entrySet()) {
        if (resourceMap.keySet()
            .contains(entry.getValue()
                .getResource_id())) {
          map.put(entry.getKey(), LesxPair.of(resourceMap.get(entry.getValue()
              .getResource_id()), entry.getValue()));
        }
      }
    }
    else {
      LOGGER.log(Level.WARNING, LesxMessage.getMessage("WARNING-FOUND_NULL_DATAMODEL"));
    }
  }

  public List<LesxPair<LesxResource, LesxBusiness>> getPairList() {
    List<LesxPair<LesxResource, LesxBusiness>> result = new ArrayList<>();
    if (!map.isEmpty()) {
      result.addAll(map.values());
    }
    return result;
  }

  @Override
  public void persist(Runnable run) {
    // No persist here
  }

  public List<TreeItem<LesxPair<LesxResource, LesxBusiness>>> getTreeItem(ELesxMonth month, Integer year) {
    List<TreeItem<LesxPair<LesxResource, LesxBusiness>>> listLeaf = new ArrayList<>();
    if (year != null) {
      for (LesxPair<LesxResource, LesxBusiness> pair : getPairList()) {
        LocalDate date = LocalDate.parse(pair.getFirst()
            .getRegistration_date(), formatter);
        if ((month.getKey() + 1) == date.getMonthValue() && date.getYear() == year) {
          listLeaf.add(new TreeItem<>(pair));
        }
      }
    }
    return listLeaf;
  }

}
