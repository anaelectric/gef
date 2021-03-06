/*******************************************************************************
 * Copyright (c) 2017 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthias Wienand (itemis AG) - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.gef.mvc.fx.providers;

import java.util.List;

import org.eclipse.gef.mvc.fx.models.SnappingModel.SnappingLocation;
import org.eclipse.gef.mvc.fx.parts.IContentPart;

import javafx.geometry.Orientation;
import javafx.scene.Node;

/**
 * The {@link CenterSnappingLocationProvider} is a specialization of
 * {@link BoundsSnappingLocationProvider} that returns the center of the bounds
 * as the only {@link SnappingLocation}s.
 */
public class CenterSnappingLocationProvider
		extends BoundsSnappingLocationProvider {

	@Override
	public List<SnappingLocation> getHorizontalSnappingLocations(
			IContentPart<? extends Node> part) {
		return getSnappingLocations(part, Orientation.HORIZONTAL, 0.5d);
	}

	@Override
	public List<SnappingLocation> getVerticalSnappingLocations(
			IContentPart<? extends Node> part) {
		return getSnappingLocations(part, Orientation.VERTICAL, 0.5d);
	}
}
