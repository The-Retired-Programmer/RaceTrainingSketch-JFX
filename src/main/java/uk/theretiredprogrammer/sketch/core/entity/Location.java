/*
 * Copyright 2020-2021 Richard Linsdale (richard at theretiredprogrammer.uk).
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
package uk.theretiredprogrammer.sketch.core.entity;

import jakarta.json.JsonArray;
import jakarta.json.JsonValue;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import uk.theretiredprogrammer.sketch.core.ui.UI;

public class Location implements ModelProperty<Location> {

    private final SimpleDoubleProperty x = new SimpleDoubleProperty();
    private final SimpleDoubleProperty y = new SimpleDoubleProperty();

    public Location() {
        set(0.0, 0.0);
    }

    public Location(Location defaultvalue) {
        set(defaultvalue.getX(), defaultvalue.getY());
    }

    public Location(double x, double y) {
        set(x, y);
    }

    public final void set(Location locationproperty) {
        set(locationproperty.getXProperty().get(), locationproperty.getYProperty().get());
    }

    public final void set(double x, double y) {
        this.x.set(x);
        this.y.set(y);
    }

    public double getX() {
        return x.get();
    }

    public double getY() {
        return y.get();
    }

    public SimpleDoubleProperty getXProperty() {
        return x;
    }

    public SimpleDoubleProperty getYProperty() {
        return y;
    }

    @Override
    public void setOnChange(Runnable onchange) {
        x.addListener((o, oldval, newval) -> onchange.run());
        y.addListener((o, oldval, newval) -> onchange.run());
    }

    @Override
    public Location parsevalue(JsonValue jvalue) {
        return FromJson.location(jvalue);
    }

    @Override
    public JsonArray toJson() {
        return ToJson.serialise(this);
    }

    @Override
    public Node getControl() {
        return getControl(7);
    }

    @Override
    public Node getControl(int size) {
        return UI.control(size, this);
    }

    @Override
    public final void parse(JsonValue jvalue) {
        set(parsevalue(jvalue));
    }

    public double to(Location target) {
        double deltax = (target.getX() - this.getX());
        double deltay = (target.getY() - this.getY());
        return Math.sqrt(deltax * deltax + deltay * deltay);
    }

    public Angle angleto(Location target) {
        return new Angle(Math.round(Math.toDegrees(Math.atan2(target.getY() - this.getY(), target.getX() - this.getX()))));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (Double.doubleToLongBits(getX()) ^ (Double.doubleToLongBits(getX()) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits(getY()) ^ (Double.doubleToLongBits(getY()) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        if (Double.doubleToLongBits(this.getX()) != Double.doubleToLongBits(other.getX())) {
            return false;
        }
        return Double.doubleToLongBits(this.getY()) == Double.doubleToLongBits(other.getY());
    }

    @Override
    public String toString() {
        return "[" + getX() + "," + getY() + "]";
    }
}
