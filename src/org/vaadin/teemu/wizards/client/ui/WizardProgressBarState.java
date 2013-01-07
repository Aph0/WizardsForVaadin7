package org.vaadin.teemu.wizards.client.ui;

import java.util.ArrayList;

public class WizardProgressBarState extends
        com.vaadin.shared.AbstractComponentState {

    public ArrayList<SerializableStep> steps = new ArrayList<SerializableStep>();

    public boolean hasHorizontalWizardProgressBar;

    public boolean showProgressIndicator = true;

    public boolean completed;

    public String linkMode;

    public boolean verticalspacing;

}