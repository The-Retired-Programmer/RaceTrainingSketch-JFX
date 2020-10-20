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
package uk.theretiredprogrammer.sketch.core.control;

import jakarta.json.JsonException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.WindowEvent;
import uk.theretiredprogrammer.sketch.core.ui.AbstractWindow;

/**
 *
 * @author richard
 * @param <W>
 */
public abstract class AbstractController<W extends AbstractWindow> {

    protected enum ExternalCloseAction {
        CLOSE, HIDE, IGNORE
    }

    protected enum ClosingMode {
        PROGRAMMATICALLY, EXTERNALLY, HIDEONLY
    }

    private ClosingMode closingmode = ClosingMode.HIDEONLY;
    private final ExternalCloseAction externalcloseaction;

    public AbstractController() {
        this.externalcloseaction = ExternalCloseAction.CLOSE;
    }

    public AbstractController(ExternalCloseAction externalcloseaction) {
        this.externalcloseaction = externalcloseaction;
    }

    public final void close() {
        closingmode = ClosingMode.PROGRAMMATICALLY;
        whenWindowIsClosingProgrammatically();
        whenWindowIsClosing();
        getWindow().close();
    }

    protected void whenWindowIsClosing() {
    }

    protected void whenWindowIsClosingProgrammatically() {
    }

    protected void whenWindowIsClosingExternally() {
    }

    public final void windowHasExternalCloseRequest(WindowEvent e) {
        switch (externalcloseaction) {
            case CLOSE -> {
                closingmode = ClosingMode.EXTERNALLY;
                whenWindowIsClosingExternally();
                whenWindowIsClosing();
            }
            case HIDE -> {
                closingmode = ClosingMode.HIDEONLY;
            }
            case IGNORE ->
                e.consume();
        }
    }

    public final void windowIsHiding(WindowEvent e) {
        getWindow().saveWindowSizePreferences();
        whenWindowIsHiding();
    }

    protected void whenWindowIsHiding() {
    }

    public final void windowIsHidden(WindowEvent e) {
        switch (closingmode) {
            case PROGRAMMATICALLY -> {
                whenWindowIsClosedProgrammatically();
                whenWindowIsClosed();
                setWindow(null);
            }
            case EXTERNALLY -> {
                whenWindowIsClosedExternally();
                whenWindowIsClosed();
                setWindow(null);
            }
            case HIDEONLY ->
                whenWindowIsHiddenOnly();
        }
    }

    protected void whenWindowIsHiddenOnly() {
    }

    protected void whenWindowIsClosed() {
    }

    protected void whenWindowIsClosedProgrammatically() {
    }

    protected void whenWindowIsClosedExternally() {
    }

    private W window;

    protected void setWindow(W window) {
        this.window = window;
    }

    protected W getWindow() {
        return window;
    }

    public class WorkRunner {

        private final Runnable work;
        private Consumer<Exception> illegalstatefailurereporting = (ex) -> catchDialog("Program Failure", ex);
        private Consumer<Exception> iofailurereporting = (ex) -> catchDialog("File read/write Failure", ex);
        private Consumer<Exception> parsefailurereporting = (ex) -> catchDialog("Problem parsing Config file", ex);
        private Consumer<Exception> otherexceptionsreporting = (ex) -> catchDialog("Program Failure", ex);
        private Runnable exceptionHandler = () -> nothing();
        private Runnable parsefailureHandler = () -> exceptionHandler.run();

        public WorkRunner(Runnable work) {
            this.work = work;
        }

        public WorkRunner reportOnIllegalStateFailure(Consumer<Exception> illegalstatefailurereporting) {
            this.illegalstatefailurereporting = illegalstatefailurereporting;
            return this;
        }

        public WorkRunner reportOnIOFailure(Consumer<Exception> iofailurereporting) {
            this.iofailurereporting = iofailurereporting;
            return this;
        }

        public WorkRunner reportOnParseFailure(Consumer<Exception> parsefailurereporting) {
            this.parsefailurereporting = parsefailurereporting;
            return this;
        }

        public WorkRunner reportOnOtherExceptions(Consumer<Exception> otherexceptionsreporting) {
            this.otherexceptionsreporting = otherexceptionsreporting;
            return this;
        }

        public WorkRunner setExceptionHandler(Runnable exceptionHandler) {
            this.exceptionHandler = exceptionHandler;
            return this;
        }

        public WorkRunner setParseFailureHandler(Runnable parsefailureHandler) {
            this.parsefailureHandler = parsefailureHandler;
            return this;
        }

        public void run() {
            try {
                work.run();
            } catch (IllegalStateFailure ex) {
                exceptionHandler.run();
                Platform.runLater(() -> illegalstatefailurereporting.accept(ex));
            } catch (JsonException | ParseFailure ex) {
                parsefailureHandler.run();
                Platform.runLater(() -> parsefailurereporting.accept(ex));
            } catch (IOFailure ex) {
                exceptionHandler.run();
                Platform.runLater(() -> iofailurereporting.accept(ex));
            } catch (Exception ex) {
                exceptionHandler.run();
                Platform.runLater(() -> otherexceptionsreporting.accept(ex));
            }
        }

        public void nothing() {
        }

        public void catchDialog(String title, Exception ex) {
            StringWriter writer = new StringWriter();
            PrintWriter pwriter = new PrintWriter(writer);
            ex.printStackTrace(pwriter);
            ButtonType loginButtonType = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
            Dialog<String> dialog = new Dialog<>();
            dialog.getDialogPane().getButtonTypes().add(loginButtonType);
            dialog.setTitle(title + ex.getLocalizedMessage());
            dialog.setContentText(writer.toString());
            dialog.setWidth(600);
            dialog.setResizable(true);
            dialog.showAndWait();
        }

    }
}
