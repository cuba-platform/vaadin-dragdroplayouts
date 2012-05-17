/*
 * Copyright 2012 John Ahlroos
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package fi.jasoft.dragdroplayouts.drophandlers;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.terminal.gwt.client.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

import fi.jasoft.dragdroplayouts.DDVerticalLayout.VerticalLayoutTargetDetails;
import fi.jasoft.dragdroplayouts.events.LayoutBoundTransferable;

/**
 * A default drop handler for vertical layouts
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.6.0
 */
@SuppressWarnings("serial")
public class DefaultVerticalLayoutDropHandler extends
        AbstractDefaultLayoutDropHandler {

    private Alignment dropAlignment;

    /**
     * Constructor
     */
    public DefaultVerticalLayoutDropHandler() {

    }

    /**
     * Constructor
     * 
     * @param dropCellAlignment
     *            The cell alignment of the component after it has been dropped
     */
    public DefaultVerticalLayoutDropHandler(Alignment dropCellAlignment) {
        this.dropAlignment = dropCellAlignment;
    }

    /**
     * Called when a component changed location within the layout
     * 
     * @param event
     *            The drag and drop event
     */
    @Override
    protected void handleComponentReordering(DragAndDropEvent event) {
        // Component re-ordering
        LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
                .getTransferable();
        VerticalLayoutTargetDetails details = (VerticalLayoutTargetDetails) event
                .getTargetDetails();
        AbstractOrderedLayout layout = (AbstractOrderedLayout) details
                .getTarget();
        Component comp = transferable.getComponent();
        int idx = (details).getOverIndex();

        // Detach
        layout.removeComponent(comp);
        idx--;

        // Increase index if component is dropped after or above a previous
        // component
        VerticalDropLocation loc = details.getDropLocation();
        if (loc == VerticalDropLocation.MIDDLE
                || loc == VerticalDropLocation.BOTTOM) {
            idx++;
        }

        // Add component
        if (idx >= 0) {
            layout.addComponent(comp, idx);
        } else {
            layout.addComponent(comp);
        }

        // Add component alignment if given
        if (dropAlignment != null) {
            layout.setComponentAlignment(comp, dropAlignment);
        }

    }

    /**
     * Handle a drop from another layout
     * 
     * @param event
     *            The drag and drop event
     */
    @Override
    protected void handleDropFromLayout(DragAndDropEvent event) {
        LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
                .getTransferable();
        VerticalLayoutTargetDetails details = (VerticalLayoutTargetDetails) event
                .getTargetDetails();
        AbstractOrderedLayout layout = (AbstractOrderedLayout) details
                .getTarget();
        Component source = event.getTransferable().getSourceComponent();
        int idx = (details).getOverIndex();
        Component comp = transferable.getComponent();

        // Check that we are not dragging an outer layout into an inner
        // layout
        Component parent = layout.getParent();
        while (parent != null) {
            if (parent == comp) {
                return;
            }
            parent = parent.getParent();
        }

        // If source is an instance of a component container then remove
        // it
        // from there,
        // the component cannot have two parents.
        if (source instanceof ComponentContainer) {
            ComponentContainer sourceLayout = (ComponentContainer) source;
            sourceLayout.removeComponent(comp);
        }

        // Increase index if component is dropped after or above a
        // previous
        // component
        VerticalDropLocation loc = (details).getDropLocation();
        if (loc == VerticalDropLocation.MIDDLE
                || loc == VerticalDropLocation.BOTTOM) {
            idx++;
        }

        // Add component
        if (idx >= 0) {
            layout.addComponent(comp, idx);
        } else {
            layout.addComponent(comp);
        }

        // Add component alignment if given
        if (dropAlignment != null) {
            layout.setComponentAlignment(comp, dropAlignment);
        }
    }
}
