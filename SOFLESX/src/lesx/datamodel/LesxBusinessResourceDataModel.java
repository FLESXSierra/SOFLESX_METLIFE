package lesx.datamodel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
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
  private TreeMap<LocalDate, LesxReportMonthBusiness> monthReport = new TreeMap<>();
  private boolean buildMonthReport = true;
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
        .buildBusinessResourceMapAndNotify();
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
      buildMonthReport = true;
      LOGGER.log(Level.INFO, LesxMessage.getMessage("INFO-OBJECT_ADDED", 1));
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-DATA_MODEL_SAVE", resourceBusiness.getResource()
          .getName(), resourceBusiness));
      e.printStackTrace();
    }
  }

  public Long createNewKeyForBusinessIdProperty() {
    return (LesxMisc.isEmpty(map) ? 1 : Collections.max(map.keySet()) + 1);
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
      buildMonthReport = true;
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

  public List<LesxReportMonthBusiness> getMonthToMonthNBSReport(Integer year) {
    return getMonthToMonthReport(year).stream()
        .filter(report -> report.getNBS() != null && report.getNBS() != 0)
        .collect(Collectors.toList());
  }

  public List<LesxReportMonthBusiness> getMonthToMonthReport(Integer year) {
    if (buildMonthReport) {
      buildMonthToMonthReport();
      buildMonthReport = false;
    }
    return monthReport.entrySet()
        .stream()
        .filter(entry -> entry.getKey()
            .getYear() == year)
        .map(entry -> entry.getValue())
        .collect(Collectors.toList());
  }

  private void buildMonthToMonthReport() {
    LocalDate currentDate;
    LocalDate startDate;
    boolean comision;
    boolean firstReport = true;
    int cont;
    LesxReportMonthBusiness report = null;
    LesxReportMonthBusiness current = null;
    LocalDate cancelledDate = null;
    for (LesxResourceBusiness rb : getResourceBusinessList()) {
      if (rb.getBusiness() != null && rb.getBusiness()
          .getNbs() != null) {
        currentDate = LocalDate.parse(rb.getBusiness()
            .getDate(), LesxPropertyUtils.FORMATTER);
        startDate = LocalDate.of(currentDate.getYear(), currentDate.getMonthValue(), 1);
        current = monthReport.get(startDate);
        firstReport = true;
        comision = true;
        cont = 1;
        if (rb.getBusiness()
            .getCancelled()
            .getValue() != null
            && rb.getBusiness()
                .getCancelled()
                .getValue()
                .booleanValue()) {
          cancelledDate = LocalDate.parse(rb.getBusiness()
              .getCancelled()
              .getDate(), LesxPropertyUtils.FORMATTER);
        }
        while (comision) {
          if (cancelledDate != null && cancelledDate.isBefore(startDate)) {
            break;
          }
          if (current != null) {
            current.setComision(current.getComision() + rb.getComision(startDate));
            if (firstReport) {
              current.setNBS(current.getNBS() + rb.getBusiness()
                  .getNbs());
              int ap = rb.getBusiness()
                  .getProduct()
                  .getTypeAP() != null ? 1 : 0;
              current.setAp(current.getAp() + ap);
              int vida = rb.getBusiness()
                  .getProduct()
                  .getTypeVida() != null ? 1 : 0;
              current.setVida(current.getVida() + vida);
            }
          }
          else {
            report = new LesxReportMonthBusiness(startDate.getMonthValue());
            report.setAp(firstReport && rb.getBusiness()
                .getProduct()
                .getTypeAP() != null ? 1L : 0L);
            report.setVida(firstReport && rb.getBusiness()
                .getProduct()
                .getTypeVida() != null ? 1L : 0L);
            report.setComision(rb.getComision(startDate));
            report.setNBS(firstReport && rb.getBusiness()
                .getNbs() != null
                    ? rb.getBusiness()
                        .getNbs()
                    : 0L);
            monthReport.put(startDate, report);
          }
          comision = cont <= 3;
          cont++;
          startDate = startDate.plusMonths(1);
          current = monthReport.get(startDate);
          cancelledDate = null;
          firstReport = false;
        }
      }
    }
    insertCAPP();
  }

  /**
   * Is assumed that buildMonthReport is {@code false}
   */
  private void insertCAPP() {
    LocalDate currentDate;
    LesxReportMonthBusiness report;
    int capp = 0;
    int newCapp = 0;
    for (Entry<LocalDate, LesxReportMonthBusiness> entry : monthReport.entrySet()) {
      currentDate = entry.getKey();
      report = entry.getValue();
      capp += report.getCapp();
      report.setCurrentCAPP(newCapp);
      if (currentDate.getMonth()
          .getValue() % 3 == 0) {
        newCapp = capp;
        capp = newCapp - 15 < 0 ? 0 : newCapp - 15;
      }
    }
  }

  public List<LesxReportMonthBusiness> getMonthToMonthCAPPReport(Integer year) {
    if (buildMonthReport) {
      buildMonthToMonthReport();
      buildMonthReport = false;
    }
    LocalDate startDate;
    List<LesxReportMonthBusiness> reportCapp = new ArrayList<>();
    long leftOverCapp = 0;
    for (int i = 0; i <= 11; i++) {
      startDate = LocalDate.of(year, (i + 1), 1);
      LesxReportMonthBusiness report = monthReport.get(startDate);
      if (report != null) {
        leftOverCapp = leftOverCapp + report.getCapp();
      }
      else {
        report = new LesxReportMonthBusiness(i + 1);
      }
      if ((i + 1) % 3 == 0) {
        leftOverCapp = (leftOverCapp - 15) < 0 ? 0 : (leftOverCapp - 15);
      }
      reportCapp.add(report);
    }
    return reportCapp;
  }

}
