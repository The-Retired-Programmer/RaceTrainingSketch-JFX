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
package uk.theretiredprogrammer.sketch.display.entity.course;

import uk.theretiredprogrammer.sketch.core.entity.Angle;
import uk.theretiredprogrammer.sketch.core.entity.Location;
import uk.theretiredprogrammer.sketch.display.entity.boats.Boat;
import uk.theretiredprogrammer.sketch.display.entity.flows.WaterFlow;
import uk.theretiredprogrammer.sketch.display.entity.flows.WindFlow;
import uk.theretiredprogrammer.sketch.display.entity.base.SketchModel;
import uk.theretiredprogrammer.sketch.display.entity.course.Decision.Importance;
import static uk.theretiredprogrammer.sketch.display.entity.course.Decision.Importance.INSIGNIFICANT;
import static uk.theretiredprogrammer.sketch.display.entity.course.Decision.Importance.MAJOR;

public class Params {

    public final Boat boat;
    public final SketchModel model;
    public final WindFlow windflow;
    public final WaterFlow waterflow;
    //
    public Decision decision;
    public CurrentLeg leg;
    public Angle winddirection;
    public Angle starboardCloseHauled;
    public Angle portCloseHauled;
    public Angle starboardReaching;
    public Angle portReaching;
    public Angle heading;
    public Location location;
    public double angletowind;
    public Angle meanwinddirection;
    public boolean reachesdownwind;
    public double downwindrelative;
    public double upwindrelative;
    public boolean isPort;
    public Location marklocation;
    public Angle markmeanwinddirection;
    public Angle angletomark;

    public Params(SketchModel model, Boat boat) {
        this.model = model;
        windflow = model.getWindFlow();
        waterflow = model.getWaterFlow();
        this.boat = boat;
        refresh();
    }

    public final void refresh() {
        leg = boat.getCurrentLeg();
        decision = leg.decision;
        //
        angletomark = leg.getAngleofLeg();
        marklocation = leg.getMarkLocation();
        //
        winddirection = windflow.getFlow(boat.getLocation()).getAngle();
        meanwinddirection = windflow.getMeanFlowAngle();
        markmeanwinddirection = marklocation == null ? null : windflow.getMeanFlowAngle(marklocation);
        //
        heading = boat.getDirection();
        location = boat.getLocation();
        downwindrelative = boat.metrics.downwindrelative.get();
        upwindrelative = boat.metrics.upwindrelative.get();
        reachesdownwind = boat.isReachdownwind();
        isPort = boat.isPort(winddirection);
        starboardCloseHauled = boat.getStarboardCloseHauledCourse(winddirection);
        portCloseHauled = boat.getPortCloseHauledCourse(winddirection);
        starboardReaching = boat.getStarboardReachingCourse(winddirection);
        portReaching = boat.getPortReachingCourse(winddirection);
        angletowind = heading.degreesDiff(winddirection).get();
    }
    
    
    public final Angle angleToSailToMark() {
        return angleToSailToMark(isPort);
    }
    
    public final Angle angleToSailToMark(boolean port) {
        return leg.getAngletoSail(location, port);
    }

    public final boolean setSAILON() {
        decision.setSAILON(heading);
        return true;
    }

    public final boolean setTURN(Angle degrees, boolean turndirection, Importance importance, String reason) {
        Angle change = degrees.degreesDiff(heading).fold();
        if (change.lt(0.1)) {
            decision.setSAILON(heading);
        } else if (change.lt(1) && importance != MAJOR) {
            decision.setTURN(degrees, turndirection, INSIGNIFICANT, reason);
        } else {
            decision.setTURN(degrees, turndirection, importance, reason);
        }
        return true;
    }

    public final boolean setMARKROUNDING(Angle degrees, boolean turndirection, Importance importance, String reason) {
        decision.setMARKROUNDING(degrees, turndirection, importance, reason);
        return true;
    }

    public final boolean setSTOP() {
        decision.setSTOP(heading);
        return true;
    }
}
