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
package uk.theretiredprogrammer.sketch.display.control.strategy;

import uk.theretiredprogrammer.sketch.display.entity.course.CurrentLeg;
import java.util.Optional;
import uk.theretiredprogrammer.sketch.display.entity.boats.Boat;
import uk.theretiredprogrammer.sketch.core.entity.PropertyDegrees;
import uk.theretiredprogrammer.sketch.core.control.IllegalStateFailure;
import uk.theretiredprogrammer.sketch.core.entity.PropertyDistanceVector;
import uk.theretiredprogrammer.sketch.display.entity.flows.WaterFlow;
import uk.theretiredprogrammer.sketch.display.entity.flows.WindFlow;
import uk.theretiredprogrammer.sketch.display.entity.base.SketchModel;
import uk.theretiredprogrammer.sketch.display.entity.course.PropertyLeg;
import uk.theretiredprogrammer.sketch.display.entity.course.PropertyLeg.LegType;

public class WindwardStrategy extends Strategy {

    private final WindwardStarboardSailingDecisions starboarddecisions;
    private final WindwardPortSailingDecisions portdecisions;
    private final RoundingDecisions roundingdecisions;
    private boolean useroundingdecisions = false;
    private PropertyDegrees portoffsetangle;
    private PropertyDegrees starboardoffsetangle;
    private double offset;
    
    public WindwardStrategy(Boat boat, CurrentLeg leg, WindFlow windflow, WaterFlow waterflow) {
        super(leg);
        //
        offset = boat.metrics.getWidth() * 2;
        PropertyDegrees winddirection = leg.endLegMeanwinddirection(windflow);
        PropertyDegrees relative = boat.metrics.upwindrelative;
        if (leg.isPortRounding()){
            portoffsetangle = winddirection.plus(90).plus(relative);
            starboardoffsetangle = winddirection.plus(90).sub(relative);
        } else {
            portoffsetangle = winddirection.sub(90).plus(relative);
            starboardoffsetangle = winddirection.sub(90).sub(relative);
        }
        //
        starboarddecisions = new WindwardStarboardSailingDecisions();
        portdecisions = new WindwardPortSailingDecisions();
        LegType followinglegtype = PropertyLeg.getLegType(boat.metrics, leg.getAngleofFollowingLeg(), windflow, boat.isReachdownwind());
        switch (followinglegtype) {
            case OFFWIND ->
                roundingdecisions = leg.isPortRounding()
                        ? new WindwardPortRoundingDecisions((windangle) -> leg.getAngleofFollowingLeg())
                        : new WindwardStarboardRoundingDecisions((windangle) -> leg.getAngleofFollowingLeg());
            case GYBINGDOWNWIND ->
                roundingdecisions = leg.isPortRounding()
                        ? new WindwardPortRoundingDecisions((windangle) -> boat.getStarboardReachingCourse(windangle))
                        : new WindwardStarboardRoundingDecisions((windangle) -> boat.getPortReachingCourse(windangle));
            case NONE ->
                roundingdecisions = leg.isPortRounding()
                        ? new WindwardPortRoundingDecisions((windangle) -> windangle.sub(90))
                        : new WindwardStarboardRoundingDecisions((windangle) -> windangle.plus(90));
            default ->
                throw new IllegalStateFailure("Illegal/unknown/Unsupported WindwardRounding: " + followinglegtype.toString());
        }
    }

    public WindwardStrategy(WindwardStrategy clonefrom, Boat newboat) {
        super(clonefrom);
        this.portdecisions = clonefrom.portdecisions;
        this.starboarddecisions = clonefrom.starboarddecisions;
        this.roundingdecisions = clonefrom.roundingdecisions;
        this.useroundingdecisions = clonefrom.useroundingdecisions;
        this.offset = clonefrom.offset;
        this.portoffsetangle = clonefrom.portoffsetangle;
        this.starboardoffsetangle = clonefrom.starboardoffsetangle;
    }

    @Override
    String strategyTimeInterval(Boat boat, Decision decision, SketchModel sketchproperty, WindFlow windflow, WaterFlow waterflow) {
        PropertyDegrees markMeanwinddirection = leg.endLegMeanwinddirection(windflow);
        PropertyDegrees winddirection = windflow.getFlow(boat.getLocation()).getDegreesProperty();
        if (useroundingdecisions) {
            return roundingdecisions.nextTimeInterval(boat, decision, sketchproperty, this, windflow, waterflow);
        }
        if (isNear2Mark(boat, markMeanwinddirection)) {
            useroundingdecisions = true;
            return roundingdecisions.nextTimeInterval(boat, decision, sketchproperty, this, windflow, waterflow);
        }
        return (boat.isPort(winddirection) ? portdecisions : starboarddecisions).nextTimeInterval(boat, decision, sketchproperty, this, windflow, waterflow);
    }

    boolean isNear2Mark(Boat boat, PropertyDegrees markMeanwinddirection) {
        Optional<Double> refdistance = PropertyLeg.getRefDistance(boat.getLocation(), leg.getEndLocation(), markMeanwinddirection.get());
        return refdistance.isPresent() ? refdistance.get() <= boat.metrics.getLength() * 5 : true;
    }
    
    @Override
    PropertyDistanceVector getOffsetVector(boolean onPort) {
        return new PropertyDistanceVector(offset, onPort ? portoffsetangle : starboardoffsetangle);
    }
}
