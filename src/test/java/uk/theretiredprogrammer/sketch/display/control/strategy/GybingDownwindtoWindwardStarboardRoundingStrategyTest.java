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
package uk.theretiredprogrammer.sketch.display.control.strategy;

import uk.theretiredprogrammer.sketch.display.entity.course.Decision;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import uk.theretiredprogrammer.sketch.core.entity.Angle;

public class GybingDownwindtoWindwardStarboardRoundingStrategyTest extends SailingStrategyTest {

    private static final Angle DELTAANGLE = new Angle(5);

    @Test
    public void testStarboardlayline1A() throws IOException {
        System.out.println("starboard layline 1A");
        Decision decision = makeDecision("/gybedownwindtowindward-starboardtack-starboardrounding.json",
                () -> setboatlocation(56, 88));
        Angle starboardreaching = getStarboardReaching();
        assertSailing(decision, starboardreaching, starboardreaching.plus(DELTAANGLE));
    }

    @Test
    public void testStarboardlayline2A() throws IOException {
        System.out.println("starboard layline 2A");
        Decision decision = makeDecision("/gybedownwindtowindward-starboardtack-starboardrounding.json",
                () -> setboatlocation(54, 90));
        Angle starboardreaching = getStarboardReaching();
        assertSailing(decision, starboardreaching, starboardreaching.plus(DELTAANGLE));
    }

    @Test
    public void testStarboardlayline3A() throws IOException {
        System.out.println("starboard layline 3A");
        Decision decision = makeDecision("/gybedownwindtowindward-starboardtack-starboardrounding.json",
                () -> setboatlocation(52, 92));
        assertMARKROUNDING(decision, 225, true);
    }

    @Test
    public void testStarboardlayline4A() throws IOException {
        System.out.println("starboard layline 4A");
        Decision decision = makeDecision("/gybedownwindtowindward-starboardtack-starboardrounding.json",
                () -> setboatlocation(51.8, 92.2));
        assertMARKROUNDING(decision, 225, true);
    }

    @Test
    public void testPortlayline1A() throws IOException {
        System.out.println("port layline 1A");
        Decision decision = makeDecision("/gybedownwindtowindward-porttack-starboardrounding.json",
                () -> setboatlocation(48, 84));
        Angle portreaching = getPortReaching();
        assertSailing(decision, portreaching.sub(DELTAANGLE), portreaching);
    }

    @Test
    public void testPortlayline2A() throws IOException {
        System.out.println("port layline 2A");
        Decision decision = makeDecision("/gybedownwindtowindward-porttack-starboardrounding.json",
                () -> setboatlocation(50, 86));
        Angle portreaching = getPortReaching();
        assertSailing(decision, portreaching.sub(DELTAANGLE), portreaching);
    }

    @Test
    public void testPortlayline3A() throws IOException {
        System.out.println("port layline 3A");
        Decision decision = makeDecision("/gybedownwindtowindward-porttack-starboardrounding.json",
                () -> setboatlocation(52, 88));
        assertTURN(decision, getStarboardReaching(), true);
    }

    @Test
    public void testPortlayline4A() throws IOException {
        System.out.println("port layline 4A");
        Decision decision = makeDecision("/gybedownwindtowindward-porttack-starboardrounding.json",
                () -> setboatlocation(52.2, 88.2));
        assertTURN(decision, getStarboardReaching(), true);
    }

    @Test
    public void testStarboardlayline1B() throws IOException {
        System.out.println("starboard layline 1B");
        Decision decision = makeDecision("/gybedownwindtowindward-starboardtack-starboardrounding-90Wind.json",
                () -> setboatlocation(52, 96));
        Angle starboardreaching = getStarboardReaching();
        assertSailing(decision, starboardreaching, starboardreaching.plus(DELTAANGLE));
    }

    @Test
    public void testStarboardlayline2B() throws IOException {
        System.out.println("starboard layline 2B");
        Decision decision = makeDecision("/gybedownwindtowindward-starboardtack-starboardrounding-90Wind.json",
                () -> setboatlocation(50, 94));
        Angle starboardreaching = getStarboardReaching();
        assertSailing(decision, starboardreaching, starboardreaching.plus(DELTAANGLE));
    }

    @Test
    public void testStarboardlayline3B() throws IOException {
        System.out.println("starboard layline 3B");
        Decision decision = makeDecision("/gybedownwindtowindward-starboardtack-starboardrounding-90Wind.json",
                () -> setboatlocation(48, 92));
        assertMARKROUNDING(decision, 315, true);
    }

    @Test
    public void testStarboardlayline4B() throws IOException {
        System.out.println("starboard layline 4B");
        Decision decision = makeDecision("/gybedownwindtowindward-starboardtack-starboardrounding-90Wind.json",
                () -> setboatlocation(47.8, 91.8));
        assertMARKROUNDING(decision, 315, true);
    }

    @Test
    public void testPortlayline1B() throws IOException {
        System.out.println("port layline 1B");
        Decision decision = makeDecision("/gybedownwindtowindward-porttack-starboardrounding-90Wind.json",
                () -> setboatlocation(56, 88));
        Angle portreaching = getPortReaching();
        assertSailing(decision, portreaching.sub(DELTAANGLE), portreaching);
    }

    @Test
    public void testPortlayline2B() throws IOException {
        System.out.println("port layline 2B");
        Decision decision = makeDecision("/gybedownwindtowindward-porttack-starboardrounding-90Wind.json",
                () -> setboatlocation(54, 90));
        Angle portreaching = getPortReaching();
        assertSailing(decision, portreaching.sub(DELTAANGLE), portreaching);
    }

    @Test
    public void testPortlayline3B() throws IOException {
        System.out.println("port layline 3B");
        Decision decision = makeDecision("/gybedownwindtowindward-porttack-starboardrounding-90Wind.json",
                () -> setboatlocation(52, 92));
        assertTURN(decision, getStarboardReaching(), true);
    }

    @Test
    public void testPortlayline4B() throws IOException {
        System.out.println("port layline 4B");
        Decision decision = makeDecision("/gybedownwindtowindward-porttack-starboardrounding-90Wind.json",
                () -> setboatlocation(51.8, 92.2));
        assertTURN(decision, getStarboardReaching(), true);
    }
}
