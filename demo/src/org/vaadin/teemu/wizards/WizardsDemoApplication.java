package org.vaadin.teemu.wizards;

import org.vaadin.teemu.wizards.Wizard.LinkMode;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Demo application for the <a
 * href="http://vaadin.com/addon/wizards-for-vaadin">Wizards for Vaadin</a>
 * add-on.
 * 
 * @author Teemu Pöntelin / Vaadin Ltd
 */
@Theme("demo")
@SuppressWarnings("serial")
public class WizardsDemoApplication extends UI implements
        WizardProgressListener {

    private Wizard wizard;
    private VerticalLayout mainLayout;

    // NOTE! These vars are ONLY for this demo purpose. When there are many
    // users, these are not thread safe
    public static boolean horizontal = true;
    public static LinkMode linkMode = LinkMode.NONE;
    public static boolean useURIFragments = true;
    public static boolean fullSize = false;
    public static boolean expandStepsVertically = false;
    public static boolean showProgressIndicator = true;

    @Override
    public void init(VaadinRequest request) {
        // setup the main window
        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        setContent(mainLayout);

        // create the Wizard component and add the steps
        wizard = new Wizard(horizontal, expandStepsVertically,
                showProgressIndicator);
        wizard.setUriFragmentEnabled(useURIFragments);
        wizard.addListener(this);
        wizard.addStep(new IntroStep(), "intro");
        wizard.addStep(new SetupStep(), "setup");
        wizard.addStep(new ListenStep(), "listen");
        wizard.addStep(new LastStep(wizard, this), "last");
        // wizard.getBackButton().setCaption("Takaisin");
        if (!fullSize) {
            wizard.setHeight("600px");
            wizard.setWidth("800px");
        } else {
            wizard.setSizeFull();
        }
        wizard.setLinkMode(linkMode);

        mainLayout.addComponent(wizard);
        mainLayout.setComponentAlignment(wizard, Alignment.TOP_CENTER);

    }

    public void wizardCompleted(WizardCompletedEvent event) {
        endWizard("Wizard Completed!");
    }

    public void activeStepChanged(WizardStepActivationEvent event) {
        // display the step caption as the window title
        Page.getCurrent().setTitle(event.getActivatedStep().getCaption());
    }

    public void stepSetChanged(WizardStepSetChangedEvent event) {
        // NOP, not interested on this event
    }

    public void wizardCancelled(WizardCancelledEvent event) {
        endWizard("Wizard Cancelled!");
    }

    private void endWizard(String message) {

        wizard.setVisible(false);
        Notification.show(message);
        Page.getCurrent().setTitle(message);

        final CheckBox cbURIF = new CheckBox("Allow UriFragments");
        cbURIF.setValue(useURIFragments);
        cbURIF.setImmediate(true);
        cbURIF.setSizeUndefined();
        final CheckBox cbHorizontal = new CheckBox("Horizontal progress bar");
        cbHorizontal.setValue(horizontal);
        cbHorizontal.setImmediate(true);
        cbHorizontal.setSizeUndefined();
        final ComboBox linkSelect = new ComboBox("Link mode");
        linkSelect.addItem(LinkMode.NONE);
        linkSelect.addItem(LinkMode.PREVIOUS);
        linkSelect.addItem(LinkMode.ALL);
        linkSelect.setValue(linkMode);
        linkSelect.setImmediate(true);
        linkSelect.setSizeUndefined();
        final CheckBox cbFull = new CheckBox("Full size");
        cbFull.setValue(fullSize);
        cbFull.setImmediate(true);
        cbFull.setSizeUndefined();
        final CheckBox cbExpand = new CheckBox(
                "Vertical spacing. (Steps expand, when vertically laid out)");
        cbExpand.setValue(expandStepsVertically);
        cbExpand.setImmediate(true);
        cbExpand.setSizeUndefined();
        final CheckBox showProgressIndicatorCb = new CheckBox(
                "Show progress indicator");
        showProgressIndicatorCb.setValue(showProgressIndicator);
        showProgressIndicatorCb.setImmediate(true);
        showProgressIndicatorCb.setSizeUndefined();

        Button startOverButton = new Button("Run the demo again",
                new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        // fast but unsafe to use statics :).
                        useURIFragments = cbURIF.booleanValue();
                        horizontal = cbHorizontal.booleanValue();
                        linkMode = (LinkMode) linkSelect.getValue();
                        fullSize = cbFull.booleanValue();
                        expandStepsVertically = cbExpand.booleanValue();
                        showProgressIndicator = showProgressIndicatorCb
                                .booleanValue();
                        WizardsDemoApplication.this.close();
                    }
                });
        VerticalLayout vl = new VerticalLayout();
        vl.setSizeUndefined();

        vl.addComponent(cbURIF);
        vl.addComponent(cbHorizontal);
        vl.addComponent(linkSelect);
        vl.addComponent(cbFull);
        vl.addComponent(cbExpand);
        vl.addComponent(showProgressIndicatorCb);
        vl.addComponent(startOverButton);
        mainLayout.addComponent(vl);
        mainLayout.setComponentAlignment(vl, Alignment.MIDDLE_CENTER);
    }

}
