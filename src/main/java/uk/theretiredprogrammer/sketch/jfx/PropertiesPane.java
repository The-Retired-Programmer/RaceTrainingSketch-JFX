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

import java.awt.Color;
import java.util.Map;
import java.util.Map.Entry;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import uk.theretiredprogrammer.sketch.core.ColorParser;
import uk.theretiredprogrammer.sketch.flows.WaterFlow;
import uk.theretiredprogrammer.sketch.ui.Controller;

/**
 *
 * @author richard
 */
public class PropertiesPane extends Accordion {

    public PropertiesPane() {
    }

    public void updateAllproperties(Controller controller) {
        this.getPanes().clear();
        this.getPanes().addAll(
                new PropertiesSection("Display", controller.displayparameters.properties()),
                new PropertiesSection("Sailing Area", controller.sailingarea.properties()),
                new PropertiesSection("Wind Flow", controller.windflow.properties())
        );
        WaterFlow waterflow = controller.waterflow;
        if (waterflow != null) {
            this.getPanes().add(
                    new PropertiesSection("Water Flow", waterflow.properties())
            );
        }
        this.getPanes().add(
                new PropertiesSection("Course", controller.course.properties())
        );
        controller.course.getMarks().forEach(mark -> {
            this.getPanes().add(
                    new PropertiesSection("Mark - " + mark.name, mark.properties())
            );
        });
        controller.boats.getBoats().forEach(boat -> {
            this.getPanes().add(
                    new PropertiesSection("Boat - " + boat.name, boat.properties())
            );
        });
    }

    private class PropertiesSection extends TitledPane {

        public PropertiesSection(String propertiessectionname, Map<String, Object> properties) {
            GridPane propertiestable = new GridPane();
            int row = 0;
            for (Entry<String, Object> mapentry : properties.entrySet()) {
                propertiestable.add(new Label(mapentry.getKey()), 0, row, 1, 1);
                Object value = mapentry.getValue();
                @SuppressWarnings("null")
                String valuetext = value instanceof Color ? ColorParser.color2String((Color) value) : value.toString();
                propertiestable.add(new Label(valuetext), 1, row++, 1, 1);
            }
            this.setText(propertiessectionname);
            this.setContent(propertiestable);
        }
    }
}