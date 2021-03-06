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

import javafx.scene.paint.Color;

public class SparDimensions3D {

    private final float boomLength;
    private final float gooseNeckHeight;
    private final float diameter;
    private final float mastHeight;
    private final float bowtoMast;
    private final float mastheightatdecklevel;
    private final Colour sparcolour;

    public SparDimensions3D(Colour sparcolour, float bowtoMast, float mastheightatdecklevel, float mastHeight, float diameter, float gooseNeckHeight, float boomLength) {
        this.sparcolour = sparcolour;
        this.bowtoMast = bowtoMast;
        this.mastheightatdecklevel = mastheightatdecklevel;
        this.mastHeight = mastHeight;
        this.diameter = diameter;
        this.gooseNeckHeight = gooseNeckHeight;
        this.boomLength = boomLength;
    }
    
    public Color getSparcolour() {
        return sparcolour.getFXColour();
    }

    public float getBowtoMast() {
        return bowtoMast;
    }

    public float getMastHeightatDeckLevel() {
        return mastheightatdecklevel;
    }

    public float getMastHeight() {
        return mastHeight;
    }

    public float getDiameter() {
        return diameter;
    }

    public float getGooseNeckHeight() {
        return gooseNeckHeight;
    }

    public float getBoomLength() {
        return boomLength;
    }
}
