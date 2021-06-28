package hufs.cse.grimpan1;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

public class DrawPanel extends JPanel implements MouseInputListener {

	private GrimPanModel model = null;
	
	public DrawPanel(GrimPanModel model){
		this.model = model;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setStroke(model.getShapeStroke());

		if (model.isShapeFill()){
			g2.setColor(model.getFillColor());  // (수정)
			if (model.curDrawShape != null)
				g2.fill(model.curDrawShape);
			for (Shape shape:model.shapeList){
				g2.fill(shape);
			}
		}
		else {
			g2.setColor(model.getStrokeColor());  // (수정)
			if (model.curDrawShape != null)
				g2.draw(model.curDrawShape);
			for (Shape shape:model.shapeList){
				g2.draw(shape);
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	public void mousePressed(MouseEvent ev) {
		Point p1 = ev.getPoint();
		model.setMousePosition(p1);
		//System.out.println("Pressed Mouse Point=("+p1.x+","+p1.y+")");

		if (SwingUtilities.isLeftMouseButton(ev)){
			model.setClickedMousePosition(p1);
			switch (model.getEditState()){
			case GrimPanModel.SHAPE_LINE:
				genLineShape();
				break;
			case GrimPanModel.SHAPE_PENCIL:
				model.setLastMousePosition(model.getMousePosition());				
				model.curDrawShape = new Path2D.Double();
				((Path2D)model.curDrawShape).moveTo((double)p1.x, (double)p1.y);
				break;
			case GrimPanModel.SHAPE_POLYGON:
				break;
			}

		}
		repaint();
	}

	public void mouseReleased(MouseEvent ev) {
		Point p1 = ev.getPoint();
		model.setMousePosition(p1);
		//System.out.println("Released Mouse Point=("+p1.x+","+p1.y+")");

		switch (model.getEditState()){
		case GrimPanModel.SHAPE_LINE:
			genLineShape();
			if (model.curDrawShape != null){
				model.shapeList.add(model.curDrawShape);
				model.curDrawShape = null;
			}
			break;
		case GrimPanModel.SHAPE_PENCIL:
			((Path2D)model.curDrawShape).lineTo((double)p1.x, (double)p1.y);
			if (model.curDrawShape != null){
				model.shapeList.add(model.curDrawShape);
				model.curDrawShape = null;
			}
			break;
		case GrimPanModel.SHAPE_POLYGON:
			model.polygonPoints.add(p1);
			model.curDrawShape = buildPath2DFromPoints(model.polygonPoints);
			if (ev.isShiftDown()) {
				((Path2D)(model.curDrawShape)).closePath();
				model.polygonPoints.clear();
				model.shapeList.add(model.curDrawShape);
			}
			break;
			
		}

		repaint();

	}

	public void mouseDragged(MouseEvent ev) {
		Point p1 = ev.getPoint();
		model.setLastMousePosition(model.getMousePosition());
		model.setMousePosition(p1);
		//System.out.println("Dragged Mouse Point=("+p1.x+","+p1.y+")");
		
		switch (model.getEditState()){
		case GrimPanModel.SHAPE_LINE:
			genLineShape();
			break;
		case GrimPanModel.SHAPE_PENCIL:
			((Path2D)model.curDrawShape).lineTo((double)p1.x, (double)p1.y);
			break;
		case GrimPanModel.SHAPE_POLYGON:
			break;
			
		}
		repaint();

	}
	private void genLineShape() {
		Point p1 = model.getClickedMousePosition();
		Point p2 = model.getMousePosition();
		model.curDrawShape = new Line2D.Double(p1, p2);
		
	}
	public static Path2D buildPath2DFromPoints(ArrayList<Point> points){
		Path2D result = new Path2D.Double();
		if (points != null && points.size() > 0) {
			Point p0 = points.get(0);
			result.moveTo((double)(p0.x), (double)(p0.y));
			for (int i=1; i<points.size(); ++i){
				p0 = points.get(i);
				result.lineTo((double)(p0.x), (double)(p0.y));
			}
		}
		
		return result;
	}

}
