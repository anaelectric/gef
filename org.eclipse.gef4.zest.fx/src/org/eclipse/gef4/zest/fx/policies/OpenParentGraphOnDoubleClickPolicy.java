/*******************************************************************************
 * Copyright (c) 2015 itemis AG and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthias Wienand (itemis AG) - initial API & implementation
 *
 *******************************************************************************/
package org.eclipse.gef4.zest.fx.policies;

import org.eclipse.gef4.graph.Graph;
import org.eclipse.gef4.mvc.domain.IDomain;
import org.eclipse.gef4.mvc.fx.policies.AbstractFXOnClickPolicy;
import org.eclipse.gef4.mvc.models.ContentModel;
import org.eclipse.gef4.zest.fx.parts.GraphRootPart;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class OpenParentGraphOnDoubleClickPolicy extends AbstractFXOnClickPolicy {

	@Override
	public void click(MouseEvent e) {
		if (e.getClickCount() == 2) {
			// double click, so open nesting graph, if it exists
			ContentModel contentModel = getHost().getRoot().getViewer().getAdapter(ContentModel.class);
			if (contentModel == null) {
				throw new IllegalArgumentException("ContentModel could not be obtained!");
			}

			final Graph currentGraph = (Graph) contentModel.getContents().get(0);
			final Graph nestingGraph = currentGraph.getNestingNode() != null ? currentGraph.getNestingNode().getGraph()
					: null;

			if (nestingGraph != null) {
				IDomain<Node> domain = getHost().getRoot().getViewer().getDomain();
				NavigationPolicy navigationPolicy = getHost().getRoot().getAdapter(NavigationPolicy.class);
				navigationPolicy.init();
				navigationPolicy.openNestingGraph(nestingGraph);
				domain.execute(navigationPolicy.commit());
			}
		}
	}

	@Override
	public GraphRootPart getHost() {
		return (GraphRootPart) super.getHost();
	}
}
