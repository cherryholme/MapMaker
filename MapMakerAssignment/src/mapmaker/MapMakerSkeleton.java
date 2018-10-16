package mapmaker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import mapmaker.MapAreaSkeleton;
import mapmaker.ToolStateSkeleton;

/**
 * <p>
 * this is the starting class of this application which will hold all creations
 * and initializations of {@link Node}s</br>
 * </p>
 * 
 * @author Shahriar (Shawn) Emami
 * @version Oct 8, 2018
 */
public class MapMakerSkeleton extends Application {

	/**
	 * <p>
	 * these two string represent how regex can allow only reading of decimal or
	 * integer numbers.</br>
	 * </p>
	 * 
	 * @see <a href="https://stackoverflow.com/a/45981297/764951"> how to read only
	 *      numbers in {@link TextField}</a>
	 */
	public static final String REGEX_DECIMAL = "-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?";
	public static final String REGEX_POSITIVE_INTEGER = "([1-9][0-9]*)";

	/**
	 * <p>
	 * this object will be used to check text against given regex.</br>
	 * </p>
	 */
	public static final Pattern P = Pattern.compile(REGEX_POSITIVE_INTEGER);

	/**
	 * <p>
	 * these static final fields are file and directory paths for this
	 * application.</br>
	 * </p>
	 */
	public static final String MAPS_DIRECTORY = "resources/maps";
	public static final String INFO_PATH = "resources/info.txt";
	public static final String HELP_PATH = "resources/help.txt";
	public static final String CREDITS_PATH = "resources/icons/credits.txt";

	private MapAreaSkeleton map;

	@Override
	public void init() throws Exception {
		super.init();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		map = new MapAreaSkeleton();

		BorderPane root = new BorderPane();
		MenuBar menuBar = new MenuBar(new Menu("File", null, createMenuItemAndIcon("New", (e) -> {
		}), createMenuItemAndIcon("Open", (e) -> {
		}), createMenuItemAndIcon("Save", (e) -> {
		}), createMenuItemAndIcon("Clear", (e) -> {
			map.clearMap();
		}), new SeparatorMenuItem(), createMenuItemAndIcon("Exit", (e) -> {
			primaryStage.hide();
		})), new Menu("Help", null, createMenuItemAndIcon("Credit", (e) -> displayCredit()),
				createMenuItemAndIcon("Info", (e) -> {
					displayInfo();
				}), new SeparatorMenuItem(), createMenuItemAndIcon("Help", (e) -> {
					displayHelp();
				})));

		Label statusLabel = new Label("Tool: " + map.activeTool());
		Label option = new Label("Option: " + getOption());
		ToolBar statusBar = new ToolBar();
		statusBar.getItems().add(statusLabel);
		statusBar.getItems().add(new Separator());
		statusBar.getItems().add(option);

		MenuButton mnuBtn = new MenuButton(null, null, createMenuItem("Line", (e) -> {

			ToolStateSkeleton.state().setTool(Tools.Room);
			ToolStateSkeleton.state().setOption(2);
			statusLabel.setText("Tool: " + map.activeTool());
		}), createMenuItem("Triangle", (e) -> {
			ToolStateSkeleton.state().setTool(Tools.Room);
			ToolStateSkeleton.state().setOption(3);
			statusLabel.setText("Tool: " + map.activeTool());
		}), createMenuItem("Rectangle", (e) -> {
			ToolStateSkeleton.state().setTool(Tools.Room);
			ToolStateSkeleton.state().setOption(4);
			statusLabel.setText("Tool: " + map.activeTool());
		}), createMenuItem("Pentagon", (e) -> {
			ToolStateSkeleton.state().setTool(Tools.Room);
			ToolStateSkeleton.state().setOption(5);
			statusLabel.setText("Tool: " + map.activeTool());
		}), createMenuItem("Hexagon", (e) -> {
			ToolStateSkeleton.state().setTool(Tools.Room);
			ToolStateSkeleton.state().setOption(6);
			statusLabel.setText("Tool: " + map.activeTool());
		}));

		ToolBar tool = new ToolBar(createButton("Select", (e) -> {

			ToolStateSkeleton.state().setTool(Tools.Select);
			statusLabel.setText("Tool: " + map.activeTool());
			option.setText("Option: " + getOption());
		}), createButton("Move", (e) -> {
			ToolStateSkeleton.state().setTool(Tools.Move);
			statusLabel.setText("Tool: " + map.activeTool());
			option.setText("Option: " + getOption());
		}), mnuBtn, createButton("Path", (e) -> {
			ToolStateSkeleton.state().setTool(Tools.Path);
			statusLabel.setText("Tool: " + map.activeTool());
			option.setText("Option: " + getOption());
		}), createButton("Erase", (e) -> {
			ToolStateSkeleton.state().setTool(Tools.Erase);
			statusLabel.setText("Tool: " + map.activeTool());
			option.setText("Option: " + getOption());
		}), createButton("Door", (e) -> {
			ToolStateSkeleton.state().setTool(Tools.Door);
			statusLabel.setText("Tool: " + map.activeTool());
			option.setText("Option: " + getOption());
		}));

		mnuBtn.setId("Room");
		mnuBtn.setPopupSide(Side.RIGHT);
		mnuBtn.setPadding(Insets.EMPTY);
		tool.setOrientation(Orientation.VERTICAL);
		tool.setPrefWidth(50);
		
		

		root.setBottom(statusBar);
		root.setLeft(tool);
		root.setTop(menuBar);
		root.setCenter(map);

		Scene scene = new Scene(root, 800, 800);
		scene.getStylesheets().add(new File("resources/css/style.css").toURI().toString());
		// if escape key is pressed quit the application
		primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
			if (e.getCode() == KeyCode.ESCAPE)
				primaryStage.hide();
		});

		primaryStage.setTitle("Map Maker Skeleton");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * <p>
	 * called when JavaFX application is closed or hidden.</br>
	 * </p>
	 */
	@Override
	public void stop() throws Exception {
		super.stop();
	}

	/**
	 * <p>
	 * create a {@link Button}.</br>
	 * </p>
	 * 
	 * @param id      - used as {@link Button#setId(String)} for CSS.
	 * @param handler - {@link EventHandler} object be called when {@link Button} is
	 *                clicked.
	 * @return created {@link Button}.
	 */
	private Button createButton(String id, EventHandler<MouseEvent> event) {
		Button button = new Button();
		button.setOnMouseClicked(event);
		button.setId(id);
		return button;
	}

	String getOption() {
		String option;
		switch (ToolStateSkeleton.state().getOption()) {

		case 2:
			option = "Line";

		case 3:
			option = "Triangle";
		case 4:
			option = "Rectangle";
		case 5:
			option = "Pentagon";
		case 6:
			option = "Hexagon";
		default:
			option = "{}";
		}
		return option;
	}

	/**
	 * <p>
	 * create a {@link MenuItem} with an icon as {@link Label}.</br>
	 * </p>
	 * 
	 * @param name    - name to be displayed on {@link MenuItem} and used as
	 *                {@link MenuItem#setId(String)} for CSS.
	 * @param handler - {@link EventHandler} object be called when {@link MenuItem}
	 *                is clicked.
	 * @return created {@link MenuItem} with an icon as {@link Label}.
	 */
	private MenuItem createMenuItemAndIcon(String name, EventHandler<ActionEvent> handler) {
		Label icon = new Label();
		icon.setId(name + "-icon");
		MenuItem item = createMenuItem(name, handler);
		item.setGraphic(icon);
		return item;
	}

	/**
	 * <p>
	 * create a {@link MenuItem}.</br>
	 * </p>
	 * 
	 * @param name    - name to be displayed on {@link MenuItem} and used as
	 *                {@link MenuItem#setId(String)} for CSS.
	 * @param handler - {@link EventHandler} object be called when {@link MenuItem}
	 *                is clicked.
	 * @return created {@link MenuItem}.
	 */
	private MenuItem createMenuItem(String name, EventHandler<ActionEvent> handler) {
		MenuItem item = new MenuItem(name);
		item.setOnAction(handler);
		item.setId(name);
		return item;
	}

	/**
	 * <p>
	 * load content of {@link MapMakerSkleton#CREDITS_PATH} and display it in an
	 * {@link Alert}.</br>
	 * </p>
	 */
	private void displayCredit() {
		displayAlert("Credit", loadFile(CREDITS_PATH, System.lineSeparator()));
	}

	private void displayInfo() {
		displayAlert("Info", loadFile(INFO_PATH, System.lineSeparator()));
	}

	private void displayHelp() {
		displayAlert("Help", loadFile(HELP_PATH, System.lineSeparator()));
	}

	/**
	 * <p>
	 * display an {@link Alert} to show {@link AlertType#INFORMATION}.</br>
	 * </p>
	 * 
	 * @param title   - string to be displayed as title of {@link Alert}
	 * @param message - string content to be displayed in {@link Alert}
	 */
	private void displayAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.show();
	}

	/**
	 * <p>
	 * read a file and convert it to one string separated with provided
	 * separator.</br>
	 * </p>
	 * 
	 * @param path      - {@link String} object containing the path to desired file.
	 * @param separator - {@link String} object containing the separator
	 */
	private String loadFile(String path, String separator) {
		try {
			// for each line in given file combine lines using the separator
			return Files.lines(Paths.get(path)).reduce("", (a, b) -> a + separator + b);
		} catch (IOException e) {
			e.printStackTrace();
			return "\"" + path + "\" was probably not found" + "\nmessage: " + e.getMessage();
		}
	}

	/**
	 * <p>
	 * ask the user where they need to save then get the content to write from
	 * {@link MapAreaSkeleton#convertToString()}.</br>
	 * </p>
	 * 
	 * @param primary - {@link Stage} object that will own the {@link FileChooser}.
	 */
	private void saveMap(Stage primary) {
		// get the file object to save to
		File file = getFileChooser(primary, true);
		if (file == null)
			return;
		try {
			if (!file.exists())
				file.createNewFile();
			Files.write(file.toPath(), map.convertToString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * ask the user what file they need to open then pass the content to
	 * {@link MapAreaSkeleton#convertFromString(java.util.Map)}.</br>
	 * </p>
	 * 
	 * @param primary - {@link Stage} object that will own the {@link FileChooser}.
	 */
	private void loadMap(Stage primary) {
		// get the file object to load from
		File file = getFileChooser(primary, false);
		if (file == null || !file.exists())
			return;
		try {
			// no parallel (threading) here but this is safer
			AtomicInteger index = new AtomicInteger(0);
			// index.getAndIncrement()/5 means every 5 elements increases by 1
			// allowing for every 5 element placed in the same key
			// for each line in file group every 5 and pass to map area
			map.convertFromString(
					Files.lines(file.toPath()).collect(Collectors.groupingBy(l -> index.getAndIncrement() / 5)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * using the {@link FileChooser} open a new window only showing .map extension;
	 * in starting path of {@link MapMakerSkleton#MAPS_DIRECTORY}.</br>
	 * this function can be used to save or open file depending on the boolean
	 * argument.</br>
	 * </p>
	 * 
	 * @param primary - {@link Stage} object that will own the {@link FileChooser}.
	 * @param save    - if true show
	 *                {@link FileChooser#showSaveDialog(javafx.stage.Window)} else
	 *                {@link FileChooser#showOpenDialog(javafx.stage.Window)}
	 * @return a {@link File} representing the save or load file object
	 */
	private File getFileChooser(Stage primary, boolean save) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Maps", "*.map"));
		fileChooser.setInitialDirectory(Paths.get(MAPS_DIRECTORY).toFile());
		return save ? fileChooser.showSaveDialog(primary) : fileChooser.showOpenDialog(primary);
	}

	/**
	 * <p>
	 * show an input dialog which to ask user for an input that matches the given
	 * regex.</br>
	 * </p>
	 * 
	 * @param title    - {@link String} object containing the title of dialog.
	 * @param content  - {@link String} object containing the body of dialog.
	 * @param match    - {@link String} object containing the regex to test against
	 *                 input.
	 * @param callBack - {@link Consumer} object to be called when there is a valid
	 *                 input.
	 */
	private void showInputDialog(String title, String content, String match, Consumer<String> callBack) {
		TextInputDialog input = new TextInputDialog();
		input.setTitle(title);
		input.setHeaderText(null);
		input.setContentText(content);
		input.getEditor().textProperty().addListener((value, oldV, newV) -> {
			// check if the inputed text matched the given regex
			if (!newV.isEmpty() && !Pattern.matches(match, newV)) {
				input.getEditor().setText(oldV);
			}
		});
		// show dialog and wait for an input, if valid call callBack
		input.showAndWait().ifPresent(e -> {
			if (e.matches(match))
				callBack.accept(e);
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}