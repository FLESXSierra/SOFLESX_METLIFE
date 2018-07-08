package lesx.ui.components.dialogs;

import static lesx.property.properties.ELesxUseCase.EDIT;

import javafx.fxml.FXML;
import lesx.datamodel.ILesxDataModel;
import lesx.datamodel.LesxResourcesDataModel;
import lesx.gui.message.LesxMessage;
import lesx.property.properties.ELesxPropertyKeys;
import lesx.property.properties.ELesxUseCase;
import lesx.property.properties.LesxComponent;
import lesx.property.properties.LesxResource;

public class LesxEditResourceDialog extends LesxEditComponentDialog {

  private LesxResourcesDataModel dataModel;
  private LesxResource resource;

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
    return getUseCase() == EDIT ? LesxMessage.getMessage("TEXT-TITLE_RESOURCE_DIALOG_EDIT") : LesxMessage.getMessage("TEXT-TITLE_RESOURCE_DIALOG_CREATE");
  }

  @Override
  protected String getDescriptionText(ELesxUseCase useCase) {
    return useCase == EDIT ? LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_EDIT_RESOURCE") : LesxMessage.getMessage("TEXT-DESCRIPTION_LABEL_NEW_RESOURCE");
  }

  @Override
  protected String getHeader(ELesxUseCase useCase) {
    StringBuilder string;
    string = new StringBuilder(128);
    if (useCase == EDIT) {
      string.append(LesxMessage.getMessage("TEXT-HEADER_LABEL_RESOURCE_PANE", resource.getName()));
    }
    else {
      string.append(LesxMessage.getMessage("TEXT-HEADER_LABEL_RESOURCE_PANE", "Nuevo"));
    }
    string.append(".");
    return string.toString();
  }

  @Override
  protected void saveValues() {
    resource = new LesxResource(((LesxResource) getPropertySheet().getComponent()).getPropertyValues());
    dataModel.addResource(resource);
    dataModel.setComponentSelected(resource);
  }

  @Override
  protected void setDataModel(ILesxDataModel<? extends LesxComponent> newDataModel) {
    dataModel = (LesxResourcesDataModel) newDataModel;
  }

  @Override
  protected boolean isDuplicate(ELesxUseCase useCase) {
    return dataModel.isDuplicate(resource, useCase);
  }

  @Override
  protected String getComponentName() {
    return resource.getName() == null ? "*No Name*" : resource.getName();
  }

  @Override
  protected void setComponent(ELesxUseCase useCase) {
    if (useCase == EDIT) {
      resource = dataModel.getComponentSelected()
          .clone();
    }
    else {
      resource = new LesxResource();
      resource.setId(dataModel.createNewKeyForIdProperty());
      resource.setKey(ELesxPropertyKeys.RESOURCE);
    }
  }

  @Override
  protected LesxResource getComponent() {
    return resource;
  }

}
