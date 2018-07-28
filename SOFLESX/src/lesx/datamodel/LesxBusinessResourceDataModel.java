package lesx.datamodel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.scene.control.TreeItem;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxMonth;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxProperty;
import lesx.property.properties.LesxReportMonthBusiness;
import lesx.property.properties.LesxResourceBusiness;
import lesx.ui.soflesx.LesxMain;
import lesx.utils.LesxMisc;
import lesx.utils.LesxPropertyUtils;

public class LesxBusinessResourceDataModel implements ILesxDataModel<LesxResourceBusiness> {

  private final static Logger LOGGER = Logger.getLogger(LesxBusinessResourceDataModel.class.getName());

  private Map<Long, LesxResourceBusiness> map = new HashMap<>();
  private LesxResourceBusiness selectedItem;

  public LesxBusinessResourceDataModel() {
    //Nothing
  }

  public LesxBusinessResourceDataModel(Map<Long, LesxResourceBusiness> map) {
    this.map = new HashMap<>(map);
  }

  @Override
  public Map<Long, LesxResourceBusiness> getMap() {
    return map;
  }

  @Override
  public void setMap(Map<Long, LesxResourceBusiness> map) {
    this.map = new HashMap<>(map);
  }

  public List<LesxResourceBusiness> getResourceBusinessList() {
    List<LesxResourceBusiness> result = new ArrayList<>();
    if (!map.isEmpty()) {
      result.addAll(map.values());
    }
    return result;
  }

  @Override
  public void persist() {
    if (selectedItem != null) {
      LesxMain.getInstance()
          .getDbProperty()
          .addBusiness(selectedItem.getBusiness());
    }
    LesxMain.getInstance()
        .getDbProperty()
        .setBusinessResourceMap(map);
    LesxMain.getInstance()
        .getDbProperty()
        .buildBusinessResourceMap();
  }

  public TreeItem<LesxResourceBusiness> getTreeItem(ELesxMonth month, Integer year) {
    LesxResourceBusiness monthRes = new LesxResourceBusiness();
    monthRes.setMonth(month.toString());
    TreeItem<LesxResourceBusiness> monthLeaf = new TreeItem<>(monthRes);
    if (year != null) {
      for (LesxResourceBusiness pair : getResourceBusinessList()) {
        LocalDate date = LocalDate.parse(pair.getBusiness()
            .getDate(), LesxPropertyUtils.FORMATTER);
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
    return isCreate != ELesxUseCase.EDIT && map.get(resourceBusiness.getBusiness()
        .getId()) != null;
  }

  public void deleteSelectedBusiness() {
    if (selectedItem != null && selectedItem.getBusiness() != null) {
      map.remove(selectedItem.getBusiness()
          .getId());
      LesxMain.getInstance()
          .getDbProperty()
          .removeBusiness(selectedItem.getBusiness());
      selectedItem = null;
      persist();
    }
  }

  public double getNBSTotalFromYear(Integer year) {
    return getResourceBusinessList().stream()
        .filter(rbItem -> {
          int yearItem = LocalDate.parse(rbItem.getBusiness()
              .getDate(), LesxPropertyUtils.FORMATTER)
              .getYear();
          return yearItem == year;
        })
        .mapToDouble(rbItem -> rbItem.getBusiness()
            .getNbs())
        .sum();
  }

  public long countTotalVidaFromYear(Integer year) {
    return getResourceBusinessList().stream()
        .filter(rbItem -> {
          int yearItem = LocalDate.parse(rbItem.getBusiness()
              .getDate(), LesxPropertyUtils.FORMATTER)
              .getYear();
          return yearItem == year;
        })
        .filter(rbItem -> rbItem.getBusiness()
            .getProduct()
            .getTypeVida() != null)
        .count();
  }

  public long countTotalAPFromYear(Integer year) {
    return getResourceBusinessList().stream()
        .filter(rbItem -> {
          int yearItem = LocalDate.parse(rbItem.getBusiness()
              .getDate(), LesxPropertyUtils.FORMATTER)
              .getYear();
          return yearItem == year;
        })
        .filter(rbItem -> rbItem.getBusiness()
            .getProduct()
            .getTypeAP() != null)
        .count();
  }

  public List<LesxReportMonthBusiness> buildMonthToMonthReport(Integer year) {
    List<LesxReportMonthBusiness> result = new ArrayList<>();
    LesxReportMonthBusiness report = null;
    List<LesxResourceBusiness> monthRB = null;
    List<LesxResourceBusiness> yearRB = getResourceBusinessList().stream()
        .filter(rbItem -> {
          int yearItem = LocalDate.parse(rbItem.getBusiness()
              .getDate(), LesxPropertyUtils.FORMATTER)
              .getYear();
          return yearItem == year;
        })
        .collect(Collectors.toList());
    if (!LesxMisc.isEmpty(yearRB)) {
      for (ELesxMonth month : ELesxMonth.values()) {
        monthRB = getResourceBusinessList().stream()
            .filter(rbItem -> {
              int monthItem = LocalDate.parse(rbItem.getBusiness()
                  .getDate(), LesxPropertyUtils.FORMATTER)
                  .getMonthValue();
              return (monthItem - 1) == month.getKey();
            })
            .collect(Collectors.toList());
        if (!LesxMisc.isEmpty(monthRB)) {
          report = new LesxReportMonthBusiness(month.getKey());
          report.setAp(monthRB.stream()
              .filter(rbItem -> rbItem.getBusiness()
                  .getProduct()
                  .getTypeAP() != null)
              .count());
          report.setVida(monthRB.stream()
              .filter(rbItem -> rbItem.getBusiness()
                  .getProduct()
                  .getTypeVida() != null)
              .count());
          report.setComision(monthRB.stream()
              .mapToLong(rbItem -> rbItem.getComision())
              .sum());
          report.setNBS(monthRB.stream()
              .mapToLong(rbItem -> rbItem.getBusiness()
                  .getNbs())
              .sum());
          result.add(report);
        }
      }
    }
    return result;
  }

}
