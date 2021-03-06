/*
 * Copyright 2021 Richard Linsdale (richard at theretiredprogrammer.uk).
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
package uk.theretiredprogrammer.sketch.display.entity.boats;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Boat3D extends Group {

    private final Rotate boatrotate;
    private final Translate boattranslate;

    private final BoatRig3D rig;
    private final Group boat;

    public Boat3D(Dimensions3D dimensions, BoatCoordinates boatcoordinates) {
        boat = new Group();
        BoatHull3D hull = new BoatHull3D(dimensions.getHulldimensions());
        rig = new BoatRig3D(dimensions.getSpardimensions(), boatcoordinates.getBoomAngleProperty());
        boat.getChildren().addAll(hull, rig);
        boat.getTransforms().addAll(boatrotate = new Rotate(0, Rotate.Z_AXIS));
        boatrotate.angleProperty().bind(boatcoordinates.getAngleProperty());
        getChildren().add(boat);
        getTransforms().addAll(boattranslate = new Translate(0f, 0f, 0f));
        boattranslate.xProperty().bind(boatcoordinates.getXProperty());
        boattranslate.yProperty().bind(boatcoordinates.getYProperty());
    }
}
