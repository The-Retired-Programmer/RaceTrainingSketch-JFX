/*
 * Copyright 2020 Richard Linsdale (richard at theretiredprogrammer.uk).
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
package uk.theretiredprogrammer.sketch.display.entity.course;

import uk.theretiredprogrammer.sketch.core.entity.PropertyDegrees;
import uk.theretiredprogrammer.sketch.core.entity.PropertyLocation;
import uk.theretiredprogrammer.sketch.display.entity.flows.WindFlow;

public class CurrentLeg {

    private int legno = 0;

    private PropertyLeg currentleg;
    private final Course course;

    public CurrentLeg(Course course) {
        this(course, 0);
    }
    
    public CurrentLeg(CurrentLeg clonefrom) {
        this(clonefrom.course, clonefrom.legno);
    }
        
    private CurrentLeg(Course course, int legno) {
        this.course = course;
        this.legno = legno;
        currentleg = course.getLeg(legno);
        course.setOnChange(() -> refresh());
    }

    private void refresh() {
        currentleg = course.getLeg(legno);
    }

    public boolean isFollowingLeg() {
        return course.getLegsProperty().size() > legno + 1;
    }

    public CurrentLeg toFollowingLeg() {
        if (isFollowingLeg()) {
            currentleg = course.getLeg(++legno);
        }
        return this;
    }

    public PropertyDegrees getAngleofFollowingLeg() {
        return isFollowingLeg()
                ? course.getLeg(legno + 1).getAngleofLeg()
                : null;
    }

    // proxies to current PropertyLeg
    public boolean isPortRounding() {
        return currentleg.isPortRounding();
    }

    public double getDistanceToEnd(PropertyLocation here) {
        return currentleg.getDistanceToEnd(here);
    }

    public PropertyLocation getEndLocation() {
        return currentleg.getEndLocation();
    }

    public PropertyDegrees endLegMeanwinddirection(WindFlow windflow) {
        return currentleg.endLegMeanwinddirection(windflow);
    }

    public PropertyDegrees getAngleofLeg() {
        return currentleg.getAngleofLeg();
    }
}
