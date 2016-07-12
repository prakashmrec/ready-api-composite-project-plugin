package com.smartbear.readyapi.action;

import com.eviware.soapui.impl.wsdl.WsdlProjectPro;
import com.eviware.soapui.impl.wsdl.actions.project.WsdlProjectActionGroups;
import com.eviware.soapui.model.ModelItem;
import com.eviware.soapui.plugins.ActionConfiguration;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.action.support.AbstractSoapUIAction;
import com.eviware.soapui.support.action.swing.TargetProvider;
import com.smartbear.ready.ui.ContextModelItemProvider;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@ActionConfiguration(actionGroup = WsdlProjectActionGroups.COMMON_PROJECT_ACTIONS, targetType = WsdlProjectPro.class,
        afterAction = "VcsShareProjectAction")
public class RecreateFilesAndSaveCompositeProjectAction extends AbstractSoapUIAction<WsdlProjectPro> implements TargetProvider<WsdlProjectPro> {

    public static final String RECREATE_FILES_MESSAGE = "Existing files (if any) will be deleted and news files will be created. Are you sure you want to continue?";
    private final ContextModelItemProvider<WsdlProjectPro> modelItemProvider;

    public RecreateFilesAndSaveCompositeProjectAction() {
        super("Save and Recreate files", "Saves and recreates the files for composite project if it was saved already.");
        modelItemProvider = new ContextModelItemProvider<>(WsdlProjectPro.class);
    }

    @Override
    public void perform(WsdlProjectPro wsdlProjectPro, Object o) {

        try {
            if (!UISupport.confirm(RECREATE_FILES_MESSAGE, "Recreate files")) {
                return;
            }
            File projectDirectory = new File(wsdlProjectPro.getPath());

            if (projectDirectory.exists()) {
                FileUtils.deleteDirectory(projectDirectory);
            }
            clearFileNameSetting(wsdlProjectPro);
            wsdlProjectPro.save();
        } catch (IOException e) {
            e.printStackTrace();
            UISupport.showErrorMessage("Failed to delete project directory. Please delete is manually and retry.");
        }
    }

    private void clearFileNameSetting(ModelItem modelItem) {
        modelItem.getSettings().clearSetting(modelItem.getId() + "fileName");
        for (ModelItem childModelItem : modelItem.getChildren()) {
            clearFileNameSetting(childModelItem);
        }
    }

    @Override
    public WsdlProjectPro getTargetModelItem() {
        WsdlProjectPro projectPro = modelItemProvider.getTargetModelItem();
        return shouldBeEnabledFor(projectPro) ? projectPro : null;
    }

    @Override
    public boolean isEnabled() {
        return shouldBeEnabledFor(modelItemProvider.getTargetModelItem());
    }

    @Override
    public boolean shouldBeEnabledFor(WsdlProjectPro projectPro) {
        return projectPro != null && projectPro.isComposite();
    }
}

