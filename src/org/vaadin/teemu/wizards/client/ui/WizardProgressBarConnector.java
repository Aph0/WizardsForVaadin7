package org.vaadin.teemu.wizards.client.ui;

import org.vaadin.teemu.wizards.WizardProgressBar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.client.ui.SimpleManagedLayout;
import com.vaadin.shared.ui.Connect;

@Connect(value = WizardProgressBar.class)
public class WizardProgressBarConnector extends AbstractComponentConnector
        implements SimpleManagedLayout {

    WizardProgressBarServerRpc rpc = RpcProxy.create(
            WizardProgressBarServerRpc.class, this);

    public WizardProgressBarConnector() {
        System.out.println("connector initialized for Wizard");

        getWidget().setProgressBarItemClickHandler(
                new ProgressBarItemClickHandler() {

                    @Override
                    public void onProgressBarItemClicked(String id) {
                        getRpcProxy(WizardProgressBarServerRpc.class)
                                .progressBarItemClicked(id);

                    }
                });
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(VWizardProgressBar.class);
    }

    @Override
    public VWizardProgressBar getWidget() {
        return (VWizardProgressBar) super.getWidget();
    }

    @Override
    public WizardProgressBarState getState() {
        return (WizardProgressBarState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().set_completed(getState().completed);
        getWidget().set_hasVerticalSpacing(getState().verticalspacing);
        getWidget().set_isHorizontal(getState().hasHorizontalWizardProgressBar);
        getWidget().set_linkmode(getState().linkMode);
        getWidget().set_showProgressIndicatorBar(
                getState().showProgressIndicator);
        getWidget().set_steps(getState().steps);
        getWidget().update();
    }

    @Override
    public void layout() {

        getWidget().update();

    }

}
