package lesx.datamodel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.TreeItem;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxMonth;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxProperty;
import lesx.property.properties.LesxResourceBusiness;
import lesx.utils.LesxPropertyUtils;

public class LesxBusinessResourceDataModel implements ILesxDataModel<LesxResourceBusiness> {

  private final static Logger LOGGER = Logger.getLogger(LesxBusinessResourceDataModel.class.getName());

  private Map<Long, LesxResourceBusiness> map = new HashMap<>();
  private LesxResourceBusiness selectedItem;

  @Override
  public Map<Long, LesxResourceBusiness> getMap() {
    return map;
  }

  @Override
  public void setMap(Map<Long, LesxResourceBusiness> map) {
    this.map = map;

  }

  public List<LesxResourceBusiness> getResourceBusinessList() {
    List<LesxResourceBusiness> result = new ArrayList<>();
    if (!map.isEmpty()) {
      result.addAll(map.values());
    }
    return result;
  }

  @Override
  public void persist(Runnable run) {
    // No persist here
  }

  public TreeItem<LesxResourceBusiness> getTreeItem(ELesxMonth month, Integer year) {
    LesxResourceBusiness monthRes = new LesxResourceBusiness();
    monthRes.setMonth(month.toString());
    TreeItem<LesxResourceBusiness> monthLeaf = new TreeItem<>(monthRes);
    if (year != null) {
      for (LesxResourceBusiness pair : getResourceBusinessList()) {
        LocalDate date = LocalDate.parse(pair.getResource()
            .getRegistration_date(), LesxPropertyUtils.FORMATTER);
        if ((month.getKey() + 1) == date.getMonthValue() && date.getYear() == year) {
          pair.setMonth(month.toString());
          monthLeaf.getChildren()
              .add(new TreeItem<>(LesxResourceBusiness.of(pair.getResource(), pair.getBusiness())));
        }
      }
    }
    return monthLeaf;
  }

  @Override
  public LesxResourceBusiness getComponentSelected() {
    return selectedItem;
  }

  @Override
  public void setComponentSelected(LesxResourceBusiness component) {
    selectedItem = component;
  }

  /**
   * Only applies for Business map
   */
  @Override
  public boolean isUniqueProperty(LesxProperty property, Long keyComponent, ELesxUseCase isCreate) {
    //Nothing
    return true;
  }

  public void addResourceBusiness(LesxResourceBusiness resourceBusiness) {
    try {
      final LesxResourceBusiness temp = resourceBusiness.clone();
      map.remove(temp.getBusiness()
          .getId());
      map.put(temp.getBusiness()
          .getId(), temp);
      LOGGER.log(Level.INFO, LesxMessage.getMessage("INFO-OBJECT_ADDED", 1));
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-DATA_MODEL_SAVE", resourceBusiness.getResource()
          .getName(), resourceBusiness));
      e.printStackTrace();
    }
  }

  public Long createNewKeyForBusinessIdProperty() {
    return (Collections.max(map.keySet()) + 1);
  }

  public boolean isDuplicate(LesxResourceBusiness resourceBusiness, ELesxUseCase isCreate) {
    return isCreate != ELesxUseCase.EDIT && map.values()
        .contains(resourceBusiness);
  }

  public void deleteSelectedBusiness() {
    // TODO Delete Selected

  }

}
