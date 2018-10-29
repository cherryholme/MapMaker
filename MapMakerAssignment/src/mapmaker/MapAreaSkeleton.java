package mapmaker;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import mapmaker.PolyShapeSkeleton2;

/**
 * <p>
 * create this class once. this class will hold all control behavior related to
 * shapes.</br>
 * </p>
 * 
 * @author Shahriar (Shawn) Emami
 * @version Oct 8, 2018
 */
public class MapAreaSkeleton extends Pane {
	final ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty<>();

	private SelectionArea select;

	/**
	 * <p>
	 * instead of calling getChildren every time you can call directly the reference
	 * of it which is initialized in constructor.</br>
	 * </p>
	 */
	private ObservableList<Node> children;
	/**
	 * <p>
	 * active shape that is currently being manipulated.</br>
	 * </p>
	 */
	private PolyShapeSkeleton2 activeShape;
	/**
	 * <p>
	 * last location of the mouse.</br>
	 * </p>
	 */
	private double startX, startY;
	/**
	 * <p>
	 * Reference to ToolSate so you don't have to call ToolSate.getState() every
	 * time.</br>
	 * </p>
	 */
	private ToolStateSkeleton tool;

	/**
	 * <p>
	 * create a new object and register mouse events.</br>
	 * </p>
	 */
	public MapAreaSkeleton() {
		super();
		select = new SelectionArea();
		tool = ToolStateSkeleton.state();
		children = this.getChildren();
		registerMouseEvents();
	}

	/**
	 * <p>
	 * helper function to register all helper functions for mouse events.</br>
	 * </p>
	 */
	private void registerMouseEvents() {
		addEventHandler(MouseEvent.MOUSE_PRESSED, this::pressClick);
		addEventHandler(MouseEvent.MOUSE_RELEASED, this::releaseClick);
		addEventHandler(MouseEvent.MOUSE_DRAGGED, this::dragClick);
	}

	/**
	 * <p>
	 * this method is called by the JavaFX event system. should not be called
	 * manually.</br>
	 * this function will be called when {@link MouseEvent#MOUSE_PRESSED} is
	 * triggered.</br>
	 * </p>
	 * 
	 * @param e - {@link MouseEvent} object
	 */
	private void pressClick(MouseEvent e) {
		e.consume();
		startX = e.getX();
		startY = e.getY();
		switch (activeTool()) {
		case Door:
			break;
		case Move:

			break;
		case Path:
			break;
		case Select:

			children.add(select);

			select.start(e.getX(), e.getY());

			break;
		case Erase:

			break;
		case Room:
			activeShape = new PolyShapeSkeleton2(tool.getOption());
			children.add(activeShape);
			break;
		default:
			throw new UnsupportedOperationException(
					"Cursor for Tool \"" + activeTool().name() + "\" is not implemneted");
		}
	}

	/**
	 * <p>
	 * this method is called by the JavaFX event system. should not be called
	 * manually.</br>
	 * this function will be called when {@link MouseEvent#MOUSE_DRAGGED} is
	 * triggered.</br>
	 * </p>
	 * 
	 * @param e - {@link MouseEvent} object
	 */
	private void dragClick(MouseEvent e) {
		e.consume();
		switch (activeTool()) {
		case Door:
			break;
		case Path:
			break;
		case Erase:
			break;
		case Select:
			select.end(e.getX(), e.getY());

			break;
		case Move:
            //used similar structure as well as variable name from code found on stackoverflow, link: https://stackoverflow.com/questions/27080039/proper-way-to-move-a-javafx8-node-around
			double deltaX = e.getX() - startX;
			double deltaY = e.getY() - startY;
			((PolyShapeSkeleton2) e.getTarget()).translate(deltaX,deltaY);
	
			
			
			

			startX = e.getX();
			startY = e.getY();

			// startX = e.getX();
			// startY = e.getY();

			break;
		case Room:
			// if you are not using PolyShapeSkeleton2 use line below
			// activeShape.reDraw( startX, startY, distance(startX, startY, e.getX(),
			// e.getY()));
			activeShape.reDraw(startX, startY, e.getX(), e.getY(), true);
			break;
		default:
			throw new UnsupportedOperationException("Drag for Tool \"" + activeTool().name() + "\" is not implemneted");
		}
	}

	/**
	 * <p>
	 * this method is called by the JavaFX event system. should not be called
	 * manually.</br>
	 * this function will be called when {@link MouseEvent#MOUSE_RELEASED} is
	 * triggered.</br>
	 * </p>
	 * 
	 * @param e - {@link MouseEvent} object
	 */
	private void releaseClick(MouseEvent e) {
		e.consume();
		switch (activeTool()) {
		case Door:
			break;
		case Move:
			break;
		case Path:
			break;
		case Select:
			children.remove(select);
			select.containsAny(children, (filter) -> {

			});
			select.clear();
			break;
		case Erase:

			children.remove(e.getTarget());
			children.removeAll(((PolyShapeSkeleton2) e.getTarget()).getControlPoints());

			break;
		case Room:
			activeShape.registerControlPoints();
			children.addAll(activeShape.getControlPoints());
			
			break;
		default:
			throw new UnsupportedOperationException(
					"Release for Tool \"" + activeTool().name() + "\" is not implemneted");
		}
		activeShape = null;
	}

	/**
	 * <p>
	 * helper function that returns the current {@link Tools}.</br>
	 * </p>
	 * 
	 * @return current active {@link Tools}
	 */
	public Tools activeTool() {
		return tool.getTool();
	}

	/**
	 * <p>
	 * create a new string that adds all shapes to one string separated by
	 * {@link System#lineSeparator()}.</br>
	 * </p>
	 * 
	 * @return string containing all shapes.
	 */
	public String convertToString() {
		// for each node in children
		return children.stream()
				// filter out any node that is not PolyShape
				.filter(PolyShapeSkeleton2.class::isInstance)
				// cast filtered nodes to PolyShapes
				.map(PolyShapeSkeleton2.class::cast)
				// convert each shape to a string format
				.map(PolyShapeSkeleton2::convertToString)
				// join all string formats together using new line
				.collect(Collectors.joining(System.lineSeparator()));
	}

	/**
	 * <p>
	 * create all shapes that are stored in given map. each key contains one list
	 * representing on PolyShape.</br>
	 * </p>
	 * 
	 * @param map - a data set which contains all shapes in this object.
	 */
	public void convertFromString(Map<Object, List<String>> map) {
		// for each key inside of map
		map.keySet().stream()
				// create a new PolyShape with given list in map
				.map(k -> new PolyShapeSkeleton2(map.get(k)))
				// for each created PolyShape
				.forEach(s -> {
					children.add(s);
					children.addAll(s.getControlPoints());
				});
		;
	}

	/**
	 * <p>
	 * call this function to clear all shapes in {@link MapAreaSkeleton}.</br>
	 * </p>
	 */
	public void clearMap() {
		children.clear();
	}

}
