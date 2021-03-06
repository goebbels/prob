package de.bmotionstudio.gef.editor.part;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.RGB;

import de.bmotionstudio.gef.editor.AttributeConstants;
import de.bmotionstudio.gef.editor.BMotionStudioImage;
import de.bmotionstudio.gef.editor.EditorImageRegistry;
import de.bmotionstudio.gef.editor.editpolicy.BMSConnectionEditPolicy;
import de.bmotionstudio.gef.editor.editpolicy.TableLayoutEditPolicy;
import de.bmotionstudio.gef.editor.figure.TableColumnFigure;
import de.bmotionstudio.gef.editor.model.BControl;

public class BTableColumnPart extends BMSAbstractEditPart {

	@Override
	protected IFigure createEditFigure() {
		TableColumnFigure tableColumnFigure = new TableColumnFigure();
		Label figure = new Label();
		tableColumnFigure.add(figure);
		if (!isRunning()) {
			figure.setIcon(BMotionStudioImage
					.getImage(EditorImageRegistry.IMG_ICON_TR_UP));
		}
		return tableColumnFigure;
	}

	@Override
	protected void prepareEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new TableLayoutEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new BMSConnectionEditPolicy());
	}

	@Override
	protected void prepareRunPolicies() {
	}

	@Override
	public void refreshEditFigure(IFigure figure, BControl model,
			PropertyChangeEvent evt) {

		Object value = evt.getNewValue();
		String aID = evt.getPropertyName();

		// if (aID.equals(AttributeConstants.ATTRIBUTE_BACKGROUND_COLOR)) {
		// for (BControl cell : model.getChildrenArray())
		// cell.setAttributeValue(
		// AttributeConstants.ATTRIBUTE_BACKGROUND_COLOR, value);
		// }
		//
		if (aID.equals(AttributeConstants.ATTRIBUTE_FOREGROUND_COLOR)) {
			((TableColumnFigure) figure).setForegroundColor((RGB) value);
			for (BControl cell : model.getChildrenArray())
				cell.setAttributeValue(
						AttributeConstants.ATTRIBUTE_FOREGROUND_COLOR, value);
		}
		//
		// if (aID.equals(AttributeConstants.ATTRIBUTE_TEXT_COLOR)) {
		// for (BControl cell : model.getChildrenArray())
		// cell.setAttributeValue(AttributeConstants.ATTRIBUTE_TEXT_COLOR,
		// value);
		// }
		//
		// if (aID.equals(AttributeConstants.ATTRIBUTE_FONT)) {
		// for (BControl cell : model.getChildrenArray())
		// cell.setAttributeValue(AttributeConstants.ATTRIBUTE_FONT, value);
		// }

	}

	@Override
	protected void refreshEditLayout(IFigure figure, BControl control) {

		int width = control.getDimension().width;

		// Change width of all cells
		List<BControl> cells = control.getChildrenArray();
		for (BControl cell : cells) {
			cell.setAttributeValue(AttributeConstants.ATTRIBUTE_WIDTH, width,
					true, true);
		}

		// Notify parent table about change
		if (getParent() instanceof BMSAbstractEditPart) {
			BMSAbstractEditPart tablePart = (BMSAbstractEditPart) getParent();
			tablePart.refreshEditLayout(tablePart.getFigure(),
					control.getParent());
		}

		super.refreshEditLayout(figure, control);

	}

	@Override
	public List<BControl> getModelChildren() {
		return ((BControl) getModel()).getChildrenArray();
	}

}
