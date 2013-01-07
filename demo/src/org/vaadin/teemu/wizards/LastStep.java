package org.vaadin.teemu.wizards;

import java.util.Date;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

public class LastStep implements WizardStep {

    private CheckBox allowBack;
    private VerticalLayout layout;
    private Wizard owner;
    private final WizardsDemoApplication wizardsDemoApplication;

    public LastStep(Wizard owner, WizardsDemoApplication wizardsDemoApplication) {
        this.owner = owner;
        this.wizardsDemoApplication = wizardsDemoApplication;
    }

    public String getCaption() {
        return "Need more?";
    }

    public Component getContent() {
        if (layout == null) {
            allowBack = new CheckBox("Allow back?", false);

            layout = new VerticalLayout();
            layout.addComponent(new Label(
                    "<h2>Need more steps?</h2><p>You can also dynamically add new steps. Try it out with the button below.</p>",
                    Label.CONTENT_XHTML));
            layout.addComponent(new Button("Add new steps",
                    new Button.ClickListener() {

                        private static final long serialVersionUID = 1L;

                        public void buttonClick(ClickEvent event) {
                            owner.addStep(new WizardStep() {

                                private final Date createdAt = new Date();

                                public boolean onBack() {
                                    return true;
                                }

                                public boolean onAdvance() {
                                    return true;
                                }

                                public Component getContent() {
                                    return new Label(
                                            "This step was created on "
                                                    + createdAt);
                                }

                                public String getCaption() {
                                    return "Generated step";
                                }

                                @Override
                                public void onActivate() {
                                    System.out.println(getCaption()
                                            + " activated");

                                }

                            });
                        }
                    }));

            layout.addComponent(new Label(
                    "<h2>Want to go back?</h2><p>This step is also an example of conditionally allowing you to go back.<br />"
                            + "Try to click the back button and then again after checking the checkbox below.</p>",
                    Label.CONTENT_XHTML));
            layout.addComponent(allowBack);
        }
        layout.setMargin(true);
        return layout;
    }

    public boolean onAdvance() {
        System.out.println("onAdvance - last step");
        return true;
    }

    public boolean onBack() {
        boolean allowed = allowBack.booleanValue();
        if (!allowed) {
            Notification.show("Not allowed, sorry");

            layout.addComponent(new Label("Not allowed, sorry"));
        }
        return allowed;
    }

    @Override
    public void onActivate() {
        System.out.println("Laststep activated!");
    }
}
