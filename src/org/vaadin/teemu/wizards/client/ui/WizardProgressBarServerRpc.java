package org.vaadin.teemu.wizards.client.ui;

import com.vaadin.shared.communication.ServerRpc;

public interface WizardProgressBarServerRpc extends ServerRpc {

    public void progressBarItemClicked(String progressBarItemId);

}
