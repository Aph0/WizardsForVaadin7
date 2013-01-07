package org.vaadin.teemu.wizards.client.ui;

import java.io.Serializable;

public class SerializableStep implements Serializable {

    private static final long serialVersionUID = 43434747312L;

    public String stepid;

    public boolean completed;

    public boolean current;

    public String caption;

}
