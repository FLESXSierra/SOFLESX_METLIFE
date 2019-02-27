package lesx.ui.components.dialogs;

import static lesx.property.properties.ELesxUseCase.EDIT;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import lesx.datamodel.ILesxDataModel;
import lesx.datamodel.LesxBusinessResourceDataModel;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxPropertyKeys;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxBusiness;
import lesx.property.properties.LesxCancelledBusiness;
import lesx.property.properties.LesxComponent;
import lesx.property.properties.LesxProductType;
import lesx.property.properties.LesxResourceBusiness;

public class LesxEditBusinessDialog extends LesxEditComponentDialog {

  private final static Logger LOGGER = Logger.getLogger(LesxEditBusinessDialog.class.getName());

  private LesxBusinessResourceDataModel dataModel;
  private LesxResourceBusiness resourceBusiness;
  private LesxBusiness business;
  private Runnable onClose;

  @FXML
  @Override
  public void initialize() {
    super.initialize();
  }

  @Override
  protected void reInitialize() {
    setIsCreate(EDIT);
    init(dataModel, EDIT);
  }

  @Override
  public String getTitle() {
    return getUseCase() == EDIT ? LesxMessage.getMessage("TEXT-TITLE_SALE_DIALOG_EDIT") : LesxMessage.getMessage("TEXT-TITLE_SALE_DIALOG_CREATE");
  }

  @Override
  protected String getDescriptionText(ELesxUseCase isCreate) {
    return isCreate == EDIT ? LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_EDIT_SALE") : LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_NEW_SALE");
  }

  @Override
  protected String getHeader(ELesxUseCase isCreate) {
    StringBuilder string;
    string = new StringBuilder(128);
    string.append(LesxMessage.getMessage("TEXT-HEADER_LABEL_RESOURCE_PANE", resourceBusiness.getResource()
        .getName()));
    string.append(".");
    return string.toString();
  }

  @Override
  protected void saveValues() {
    resourceBusiness = LesxResourceBusiness
        .of(resourceBusiness.getResource(), new LesxBusiness(((LesxBusiness) getPropertySheet().getComponent()).getPropertyValues()));
    dataModel.addResourceBusiness(resourceBusiness);
    dataModel.setComponentSelected(resourceBusiness);
    dataModel.persist();
  }

  @Override
  protected void setDataModel(ILesxDataModel<? extends LesxComponent> newDataModel) {
    dataModel = (LesxBusinessResourceDataModel) newDataModel;
  }

  @Override
  protected boolean isDuplicate(ELesxUseCase useCase) {
    return dataModel.isDuplicate(resourceBusiness, useCase);
  }

  @Override
  protected String getComponentName() {
    return (resourceBusiness == null || resourceBusiness.getResource() == null) ? "*No Name*" : resourceBusiness.getResource()
        .getName();
  }

  @Override
  protected void setComponent(ELesxUseCase useCase) {
    if (useCase == EDIT) {
      business = dataModel.getComponentSelected()
          .getBusiness()
          .clone();
      resourceBusiness = dataModel.getComponentSelected()
          .clone();
    }
    else {
      business = new LesxBusiness();
      business.setId(dataModel.createNewKeyForBusinessIdProperty());
      business.setKey(ELesxPropertyKeys.BUSINESS);
      business.setCancelled(new LesxCancelledBusiness());
      business.setProduct(new LesxProductType());
      if (dataModel.getComponentSelected() != null) {
        resourceBusiness = dataModel.getComponentSelected()
            .clone();
        business.setResource_id(resourceBusiness.getResource()
            .getId());
        resourceBusiness.setBusiness(business);
      }
      else {
        LOGGER.log(Level.SEVERE, LesxMessage.getMessage("ERROR-NO_NULL_VALUE", "'resourceBusiness'"));
      }
    }
  }

  public void setOnClose(Runnable onClose) {
    this.onClose = onClose;
  }

  @Override
  protected boolean consumeEvent() {
    return false;
  }

  @Override
  protected void onCloseWindow() {
    if (onClose != null) {
      onClose.run();
    }
    super.onCloseWindow();
  }

  @Override
  protected LesxBusiness getComponent() {
    return business;
  }

}
