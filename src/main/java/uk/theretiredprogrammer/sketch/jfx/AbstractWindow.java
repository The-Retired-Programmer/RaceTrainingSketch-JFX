/*
 * Copyright 2020 richard.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.theretiredprogrammer.sketch.jfx;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author richard
 */
public abstract class AbstractWindow {

    public static final boolean SCROLLABLE = true;

    private AbstractWindow parentwindow = null;
    private final List<AbstractWindow> childwindows = new ArrayList<>();

    private Stage stage = new Stage();
    private final Class clazz;
    private final MenuBar menubar = new MenuBar();
    private final ToolBar toolbar = new ToolBar();
    ;
    private final Text statusbar = new Text("");
    private String title;
    private Node contentnode;
    private Consumer<WindowEvent> closeaction;
    private Rectangle2D windowsize;
    private boolean scrollable = false;

    public AbstractWindow(Class clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public AbstractWindow(Class clazz, AbstractWindow parent) {
        this(clazz);
        parentwindow = parent;
        parent.childwindows.add(this);
    }

    public AbstractWindow(Class clazz, Stage stage) {
        this(clazz);
        this.stage = stage;
    }

    private static final double MINWINDOWWIDTH = 50;

    public final void setDefaultWindow() {
        windowsize = Screen.getPrimary().getVisualBounds();
    }

    public final void setDefaultWindowLeftOffsetAndWidth(double leftoffset, double width) {
        Rectangle2D screenbounds = Screen.getPrimary().getVisualBounds();
        if (leftoffset + width > screenbounds.getWidth()) {
            if (width > screenbounds.getWidth()) {
                windowsize = screenbounds;
                return;
            }
            leftoffset = screenbounds.getWidth() - width;
        }
        windowsize = new Rectangle2D(
                screenbounds.getMinX() + leftoffset,
                screenbounds.getMinY(),
                width,
                screenbounds.getHeight()
        );
    }

    public final void setDefaultWindowLeftAndRightOffsets(double leftoffset, double rightoffset) {
        Rectangle2D screenbounds = Screen.getPrimary().getVisualBounds();
        double offset = leftoffset + rightoffset;
        if (offset + MINWINDOWWIDTH > screenbounds.getWidth()) {
            if (MINWINDOWWIDTH > screenbounds.getWidth()) {
                windowsize = screenbounds;
                return;
            }
            double delta = screenbounds.getWidth() - (leftoffset + rightoffset);
            leftoffset -= delta * leftoffset / offset;
            rightoffset -= delta * rightoffset / offset;
        }
        windowsize = new Rectangle2D(
                screenbounds.getMinX() + leftoffset,
                screenbounds.getMinY(),
                screenbounds.getWidth() - leftoffset - rightoffset,
                screenbounds.getHeight()
        );
    }

    public final void setDefaultWindowWidth(double width) {
        setDefaultWindowLeftOffsetAndWidth(0, width);
    }

    public final void setDefaultWindowLeftOffset(double leftoffset) {
        Rectangle2D screenbounds = Screen.getPrimary().getVisualBounds();
        if (leftoffset + MINWINDOWWIDTH > screenbounds.getWidth()) {
            if (MINWINDOWWIDTH > screenbounds.getWidth()) {
                windowsize = screenbounds;
                return;
            }
            leftoffset = screenbounds.getWidth() - MINWINDOWWIDTH;
        }
        windowsize = new Rectangle2D(
                screenbounds.getMinX() + leftoffset,
                screenbounds.getMinY(),
                screenbounds.getWidth() - leftoffset,
                screenbounds.getHeight()
        );
    }

    public final void addtoMenubar(Menu... nodes) {
        menubar.getMenus().addAll(nodes);
    }

    public final void addtoToolbar(Node... nodes) {
        toolbar.getItems().addAll(nodes);
    }

    public final void setTitle(String title) {
        this.title = title;
    }

    public final void setContent(Node contentnode) {
        this.contentnode = contentnode;
        this.scrollable = false;
    }

    public final void setContent(Node contentnode, boolean scrollable) {
        this.contentnode = contentnode;
        this.scrollable = scrollable;
    }

    public final void setOnCloseAction(Consumer<WindowEvent> closeaction) {
        this.closeaction = closeaction;
    }

    public final Stage show() {
        BorderPane borderpane = new BorderPane();
        borderpane.setTop(new VBox(menubar, toolbar));
        borderpane.setCenter(scrollable ? new ScrollPane(contentnode) : contentnode);
        borderpane.setBottom(statusbar);
        Scene scene = new Scene(borderpane);
        SketchPreferences.applyWindowSizePreferences(stage, clazz, windowsize);
        stage.setScene(scene);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle(title);
        if (closeaction != null) {
            stage.setOnCloseRequest(e -> {
                closeaction.accept(e);
                closeRequest();
            });
        }
        stage.setOnHiding(e -> {
            SketchPreferences.saveWindowSizePreferences(stage, clazz);
            closeChildren();
        });
        stage.show();
        return stage;
    }

    public final void resetWindows() {
        SketchPreferences.clearWindowSizePreferences(clazz);
        SketchPreferences.applyWindowSizePreferences(stage, clazz, windowsize);
        childwindows.forEach(window -> window.resetWindows());
    }

    public final void statusbarDisplay(String message) {
        statusbar.setText(message);
    }

    public final void statusbarDisplay() {
        statusbarDisplay("");
    }

    private void closeChildren() {
        childwindows.forEach(window -> {
            window.stage.close();
        });
    }

    private void closeRequest() {
        if (parentwindow != null) {
            parentwindow.childwindows.remove(this);
        }
    }
}
