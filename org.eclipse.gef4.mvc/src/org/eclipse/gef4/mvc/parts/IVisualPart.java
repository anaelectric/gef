/*******************************************************************************
 * Copyright (c) 2014 itemis AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alexander Nyßen (itemis AG) - initial API and implementation
 *
 * Note: Parts of this interface have been transferred from org.eclipse.gef.EditPart
 *
 *******************************************************************************/
package org.eclipse.gef4.mvc.parts;

import java.util.List;
import java.util.Map;

import org.eclipse.gef4.common.activate.IActivatable;
import org.eclipse.gef4.common.adapt.AdapterKey;
import org.eclipse.gef4.common.adapt.IAdaptable;
import org.eclipse.gef4.common.notify.IPropertyChangeNotifier;
import org.eclipse.gef4.mvc.behaviors.IBehavior;
import org.eclipse.gef4.mvc.domain.IDomain;
import org.eclipse.gef4.mvc.policies.IPolicy;
import org.eclipse.gef4.mvc.tools.ITool;
import org.eclipse.gef4.mvc.viewer.IViewer;

import com.google.common.collect.Multiset;
import com.google.common.collect.SetMultimap;

/**
 * An {@link IVisualPart} plays the controller role in the model-view-controller
 * architecture. While it does not have to bound to a model (actually only
 * {@link IContentPart}s are bound to model elements, {@link IFeedbackPart}s and
 * {@link IHandlePart}s do not refer to model elements), an {@link IVisualPart}
 * controls a visual and is responsible of handling user interaction.
 * <p>
 * Within an {@link IViewer}, {@link IVisualPart} are organized in a hierarchy
 * via a <code>[1:n]</code> parent-children relationship ({@link #getParent()},
 * {@link #getChildren()}), which roots in an {@link IRootPart}. Furthermore a
 * <code>[n:m]</code> anchorage-anchored relationship ( {@link #getAnchorages()}, {@link #getAnchoreds()}) may be established between {@link IVisualPart}s
 * located at arbitrary places within the hierarchy.
 * <p>
 * An {@link IVisualPart} is adaptable ({@link IAdaptable}). Usually,
 * {@link IPolicy}s and {@link IBehavior}s are adapted to it (but arbitrary
 * adapters may indeed be registered as needed). {@link IPolicy}s are usually
 * required in case the {@link IVisualPart} is directly involved in user
 * interaction (e.g. the user clicks on its controlled visual). They may be
 * accessed type-safe by {@link ITool}s or other {@link IPolicy}s (
 * {@link IPolicy}s may delegate to other {@link IPolicy}s) via their class key
 * (see {@link IAdaptable}). {@link IBehavior}s are used to react to changes of
 * the attached model (in case of an {@link IContentPart}s), the viewer models,
 * or others sources (e.g. adapters of the {@link IViewer} or {@link IDomain}),
 * thereby reacting to changes of the interactive state.
 * <p>
 * {@link IVisualPart}s are activatable ({@link IActivatable}), and an
 * activation/deactivation of an {@link IVisualPart} will result in the
 * activation/deactivation of all registered adapters (i.e. {@link IPolicy}s and
 * {@link IBehavior}s).
 *
 * @noimplement This interface is not intended to be implemented by clients.
 *              Instead, {@link AbstractVisualPart} should be subclassed.
 *
 * @author anyssen
 *
 * @param <VR>
 *            The visual root node of the UI toolkit this {@link IVisualPart} is
 *            used in, e.g. javafx.scene.Node in case of JavaFX.
 */
public interface IVisualPart<VR> extends IActivatable, IAdaptable,
IPropertyChangeNotifier {

	public static final String PARENT_PROPERTY = "parent";
	public static final String CHILDREN_PROPERTY = "children";
	public static final String ANCHORAGES_PROPERTY = "anchorages";
	public static final String ANCHOREDS_PROPERTY = "anchoreds";

	public void addAnchorage(IVisualPart<VR> anchorage);

	// role may be null
	public void addAnchorage(IVisualPart<VR> anchorage, String role);

	/**
	 * Used by an anchored {@link IVisualPart} to establish an
	 * anchorage-anchored relationship with this anchorage {@link IVisualPart}.
	 * <P>
	 * Clients should never call this operation directly but instead add the
	 * anchorage to its anchored via the {@link #addAnchorage(IVisualPart)} and
	 * {@link #addAnchorage(IVisualPart, String)} operations, which will
	 * indirectly lead to a call here.
	 *
	 * @param anchored
	 *            An {@link IVisualPart} to attach to this anchorage
	 *            {@link IVisualPart} as anchored.
	 *
	 * @noreference Clients should call {@link #addAnchorage(IVisualPart)},
	 *              {@link #addAnchorage(IVisualPart, String)} instead to
	 *              establish an anchored-anchorage relationship.
	 */
	public void addAnchored(IVisualPart<VR> anchored);

	public void addChild(IVisualPart<VR> child);

	public void addChild(IVisualPart<VR> child, int index);

	public void addChildren(List<? extends IVisualPart<VR>> children);

	public void addChildren(List<? extends IVisualPart<VR>> children, int index);

	public SetMultimap<IVisualPart<VR>, String> getAnchorages();

	public Multiset<IVisualPart<VR>> getAnchoreds();

	public Map<AdapterKey<? extends IBehavior<VR>>, IBehavior<VR>> getBehaviors();

	public List<IVisualPart<VR>> getChildren();

	public IVisualPart<VR> getParent();

	public Map<AdapterKey<? extends IPolicy<VR>>, IPolicy<VR>> getPolicies();

	/**
	 * Returns the {@link IRootPart}. This method should only be called
	 * internally or by helpers such as edit policies. The root can be used to
	 * get the viewer.
	 *
	 * @return <code>null</code> or the {@link IRootPart}
	 */
	public IRootPart<VR> getRoot();

	public abstract VR getVisual();

	public boolean isRefreshVisual();

	public void refreshVisual();

	public void removeAnchorage(IVisualPart<VR> anchorage);

	// role may be null
	public void removeAnchorage(IVisualPart<VR> anchorage, String role);

	/**
	 * Used by an anchored {@link IVisualPart} to unestablish an
	 * anchorage-anchored relationship with this anchorage {@link IVisualPart}.
	 * <P>
	 * Clients should never call this operation directly but instead remove the
	 * anchorage from its anchored via the {@link #removeAnchorage(IVisualPart)}
	 * or {@link #removeAnchorage(IVisualPart, String)} operations, which will
	 * indirectly lead to a call here.
	 *
	 * @param anchored
	 *            An {@link IVisualPart} (currently attached as anchored to this
	 *            anchorage {@link IVisualPart}) to detach from this anchorage
	 *            {@link IVisualPart} as anchored.
	 *
	 * @noreference Clients should call {@link #removeAnchorage(IVisualPart)} or
	 *              {@link #removeAnchorage(IVisualPart, String)} instead to
	 *              unestablish an anchored-anchorage relationship.
	 */
	public void removeAnchored(IVisualPart<VR> anchored);

	public void removeChild(IVisualPart<VR> child);

	public void removeChildren(List<? extends IVisualPart<VR>> children);

	public void reorderChild(IVisualPart<VR> child, int index);

	/**
	 * Used by a parent {@link IVisualPart} to establish/unestablish a
	 * parent-child relationship with this child {@link IVisualPart}.
	 * <P>
	 * Clients should never call this operation directly but instead add the
	 * children to its parent via the {@link #addChild(IVisualPart)},
	 * {@link #addChild(IVisualPart, int)}, {@link #addChildren(List)}, or
	 * {@link #addChildren(List, int)} or remove it via the
	 * {@link #removeChild(IVisualPart)} or {@link #removeChildren(List)}
	 * operations, which will indirectly lead to a call here.
	 *
	 * @param parent
	 *            The new parent {@link IVisualPart} or <code>null</code>.
	 *
	 * @noreference Clients should use {@link #addChild(IVisualPart)},
	 *              {@link #addChild(IVisualPart, int)},
	 *              {@link #addChildren(List)}, {@link #addChildren(List, int)},
	 *              {@link #removeChild(IVisualPart)}, or
	 *              {@link #removeChildren(List)} to establish/unestablish a
	 *              parent-child relationship instead.
	 */
	public void setParent(IVisualPart<VR> parent);

	/**
	 * Allows to temporarily turn {@link #refreshVisual()} into a no-op
	 * operation. This may for instance be used to disable visual updates that
	 * are initiated by the model (in case of {@link IContentPart}s) while
	 * interacting with the {@link IVisualPart}.
	 *
	 * @param refreshVisual
	 *            Whether {@link #refreshVisual()} should perform updates of the
	 *            visual (<code>true</code>) or behave like a no-op operation (
	 *            <code>false</code>).
	 */
	public void setRefreshVisual(boolean refreshVisual);
}