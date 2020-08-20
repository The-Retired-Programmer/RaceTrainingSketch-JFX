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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.text.Text;
import static uk.theretiredprogrammer.sketch.jfx.App.SplitPaneNodeName.FILECONTENT;

/**
 *
 * @author richard
 */
public class FileContentPane extends TitledPane {
    
    public FileContentPane() {
        this("No File Selected", "<empty>");
    }
    
    private FileContentPane(String title, String content) {
        this.setText(title);
        this.setContent(new ScrollPane(new Text(content)));
    }
    
    public void fileSelected(App app, TreeItem<Path> path) {
        if (path == null || path.getValue() == null) {
            app.replaceSplitPaneNode(FILECONTENT, new FileContentPane("No File Selected", "<empty>"));
        } else {
            try {
                app.replaceSplitPaneNode(FILECONTENT, new FileContentPane(path.toString(),
                        new String(Files.readAllBytes(path.getValue()))));
            } catch (IOException ex) {
                app.replaceSplitPaneNode(FILECONTENT, new FileContentPane("Error when reading file", "<empty>"));
            }
        }
    }
}
