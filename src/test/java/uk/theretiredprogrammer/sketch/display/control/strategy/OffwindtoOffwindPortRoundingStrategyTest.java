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

public class OffwindtoOffwindPortRoundingStrategyTest extends SailingStrategyTest {

    @Test
    public void layline1() throws IOException {
        System.out.println("layline 1");
        Decision decision = makeDecision("/offwindtooffwind-starboardtack-portrounding.json",
                () -> setboatlocation(23, 47));
        assertSAILON(decision);
    }

    @Test
    public void layline2() throws IOException {
        System.out.println("layline 2");
        Decision decision = makeDecision("/offwindtooffwind-starboardtack-portrounding.json",
                () -> setboatlocation(21, 47));
        assertSAILON(decision);
    }

    @Test
    public void layline3() throws IOException {
        System.out.println("layline 3");
        Decision decision = makeDecision("/offwindtooffwind-starboardtack-portrounding.json",
                () -> setboatlocation(20, 47));
        assertMARKROUNDING(decision, 90, false);
    }

    @Test
    public void layline4() throws IOException {
        System.out.println("layline 4");
        Decision decision = makeDecision("/offwindtooffwind-starboardtack-portrounding.json",
                () -> setboatlocation(19.8, 47.2));
        assertMARKROUNDING(decision, 90, false);
    }

    @Test
    public void layline5() throws IOException {
        System.out.println("layline 5");
        Decision decision = makeDecision("/offwindtooffwind-starboardtack-portrounding.json",
                () -> setwindflow(4, 315),
                () -> setboatlocation(21, 47));
        assertSAILON(decision);
    }

    @Test
    public void layline6() throws IOException {
        System.out.println("layline 6");
        Decision decision = makeDecision("/offwindtooffwind-starboardtack-portrounding.json",
                () -> setwindflow(4, 315),
                () -> setboatlocation(20, 47));
        assertMARKROUNDING(decision, 90, false);
    }
}
