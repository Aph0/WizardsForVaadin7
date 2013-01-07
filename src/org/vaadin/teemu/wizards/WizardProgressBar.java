package org.vaadin.teemu.wizards;

import org.vaadin.teemu.wizards.Wizard.LinkMode;
import org.vaadin.teemu.wizards.client.ui.SerializableStep;
import org.vaadin.teemu.wizards.client.ui.WizardProgressBarServerRpc;
import org.vaadin.teemu.wizards.client.ui.WizardProgressBarState;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.vaadin.ui.AbstractComponent;

/**
 * WizardProgressBar displays the progress bar for a {@link Wizard}.
 */
public class WizardProgressBar extends AbstractComponent implements
        WizardProgressListener {

    private final Wizard wizard;

    private WizardProgressBarServerRpc rpc = new WizardProgressBarServerRpc() {

        @Override
        public void progressBarItemClicked(String progressBarItemId) {
            wizard.tryToActivateStep(progressBarItemId);
        }
    };

    public WizardProgressBar(Wizard wizard,
            boolean horizontalWizardProgressBar, boolean showProgressIndicator) {
        this.wizard = wizard;
        registerRpc(rpc);
        getState().showProgressIndicator = showProgressIndicator;
        getState().hasHorizontalWizardProgressBar = horizontalWizardProgressBar;
        if (horizontalWizardProgressBar) {
            setWidth("100%");
        } else {
            if (wizard.hasVerticalStepSpacing) {
                setHeight("100%");
            } else {
                setHeight(null);
            }
            // This must be in pixels, because the content on the right side is
            // 100% and expanded. This cannot either be undefined, since the
            // captions try to take as much width as possible at the client
            // widget
            setWidth("150px");
        }
    }

    @Override
    protected WizardProgressBarState getState() {
        return (WizardProgressBarState) super.getState();
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        if (wizard.currentLinkmode == LinkMode.NONE) {
            getState().linkMode = "none";
        } else if (wizard.currentLinkmode == LinkMode.PREVIOUS) {
            getState().linkMode = "previous";
        } else if (wizard.currentLinkmode == LinkMode.ALL) {
            getState().linkMode = "all";
        }

        getState().verticalspacing = wizard.hasVerticalStepSpacing;
        getState().steps.clear();
        for (WizardStep step : wizard.getSteps()) {

            SerializableStep ss = new SerializableStep();
            ss.caption = step.getCaption();
            ss.stepid = wizard.getId(step);
            ss.completed = wizard.isCompleted(step);
            ss.current = wizard.isActive(step);

            getState().steps.add(ss);
        }

    }

    /*
     * @Override public void changeVariables(Object source, Map<String, Object>
     * variables) { super.changeVariables(source, variables);
     * 
     * if (variables.containsKey("pbitemid")) {
     * wizard.tryToActivateStep((String) variables.get("pbitemid")); } }
     */
    public void activeStepChanged(WizardStepActivationEvent event) {
        markAsDirty();
    }

    public void stepSetChanged(WizardStepSetChangedEvent event) {
        markAsDirty();
    }

    public void wizardCompleted(WizardCompletedEvent event) {
        getState().completed = true;
        markAsDirty();
    }

    public void wizardCancelled(WizardCancelledEvent event) {
        // NOP, no need to react to cancellation
    }

    public void setPixelWidth(int pixels) {
        setWidth(pixels + "px");
        markAsDirty();
    }

}
