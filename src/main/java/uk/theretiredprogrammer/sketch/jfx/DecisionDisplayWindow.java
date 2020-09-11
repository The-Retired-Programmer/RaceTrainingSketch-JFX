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

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author richard
 */
public class DecisionDisplayWindow {

    public static DecisionDisplayWindow create(String title) {
        return new DecisionDisplayWindow(title);
    }

    private final TextFlow textarea;

    private DecisionDisplayWindow(String title) {
        textarea = new TextFlow();
        new WindowBuilder().setTitle(title)
                .setContent(textarea)
                .show();
    }

    public void clear() {
        textarea.getChildren().clear();
    }

    public void writeline(String line) {
        textarea.getChildren().add(new Text(line + "\n"));
    }
}
