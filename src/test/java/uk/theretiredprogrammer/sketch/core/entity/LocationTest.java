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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LocationTest {

    private final static double DELTA = 0.0000001;

    @Test
    public void testTo() {
        System.out.println("to");
        Location instance = new Location(10.0, 20.0);
        assertEquals(5.0, instance.to(new Location(13.0, 24.0)), DELTA);
        assertEquals(5.0, instance.to(new Location(13.0, 16.0)), DELTA);
        assertEquals(5.0, instance.to(new Location(7.0, 24.0)), DELTA);
        assertEquals(5.0, instance.to(new Location(7.0, 16.0)), DELTA);
    }

    @Test
    public void testAngleto() {
        System.out.println("angleto");
        Location instance = new Location(10.0, 20.0);
        assertEquals(0, instance.angleto(new Location(13.0, 20.0)).get());
        assertEquals(45, instance.angleto(new Location(13.0, 23.0)).get());
        assertEquals(90, instance.angleto(new Location(10.0, 23.0)).get());
        assertEquals(135, instance.angleto(new Location(7.0, 23.0)).get());
        assertEquals(180, instance.angleto(new Location(7.0, 20.0)).get());
        assertEquals(225, instance.angleto(new Location(7.0, 17.0)).get());
        assertEquals(270, instance.angleto(new Location(10.0, 17.0)).get());
        assertEquals(315, instance.angleto(new Location(13.0, 17.0)).get());
    }
}
