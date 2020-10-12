/*
 * Copyright 2020 richard linsdale.
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
package uk.theretiredprogrammer.sketch.controller;

import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import uk.theretiredprogrammer.sketch.boats.Boats;
import uk.theretiredprogrammer.sketch.core.IllegalStateFailure;
import uk.theretiredprogrammer.sketch.course.Course;
import uk.theretiredprogrammer.sketch.flows.WaterFlow;
import uk.theretiredprogrammer.sketch.flows.WindFlow;
import uk.theretiredprogrammer.sketch.jfx.sketchdisplay.SketchWindow.SketchPane;
import uk.theretiredprogrammer.sketch.properties.PropertyBoat;
import uk.theretiredprogrammer.sketch.properties.PropertyFlowComponent;
import uk.theretiredprogrammer.sketch.properties.PropertyMark;
import uk.theretiredprogrammer.sketch.properties.PropertySketch;
import uk.theretiredprogrammer.sketch.strategy.BoatStrategies;
import uk.theretiredprogrammer.sketch.timerlog.TimerLog;
import uk.theretiredprogrammer.sketch.upgraders.ConfigFileController;

/**
 *
 * @author Richard Linsdale (richard at theretiredprogrammer.uk)
 */
public class Controller {

    private PropertySketch sketchproperty;

    public BoatStrategies boatstrategies;
    public WindFlow windflow;
    public WaterFlow waterflow;
    public Course course;
    public Boats boats;
    private Painter painter;
    //
    private ConfigFileController configfilecontroller;
    private int simulationtime;
    private boolean isRunning;
    private Timer timer;
    private TimeStepRunner runner;
    private Runnable sketchchangeaction = () -> {
    };
    private Consumer<Integer> timechangeaction = (t) -> {
    };
    private Consumer<String> showdecisionline = (l) -> {
    };
    private Consumer<String> writetostatusline = (m) -> {
    };

    public Controller(Path path) throws IOException {
        try {
            configfilecontroller = new ConfigFileController(path);
            if (configfilecontroller.needsUpgrade()) {
                configfilecontroller.upgrade();
            }
            createObjectProperties(configfilecontroller.getParsedConfigFile());
        } catch (JsonException | IOException ex) {
            throw new IOException("Failed to load, upgrade or parse the config file\n" + ex.getLocalizedMessage());
        }
    }

    public Controller(String resourcename) throws IOException {
        try {
            configfilecontroller = new ConfigFileController(this.getClass().getResourceAsStream(resourcename));
            if (configfilecontroller.needsUpgrade()) {
                configfilecontroller.upgrade();
            }
            createObjectProperties(configfilecontroller.getParsedConfigFile());
        } catch (JsonException | IOException ex) {
            throw new IOException("Failed to load, upgrade or parse the config file\n" + ex.getLocalizedMessage());
        }
    }

    public Controller setOnSketchChange(Runnable sketchchangeaction) {
        this.sketchchangeaction = sketchchangeaction;
        return this;
    }

    public Controller setOnTimeChange(Consumer<Integer> timechangeaction) {
        this.timechangeaction = timechangeaction;
        return this;
    }

    public Controller setShowDecisionLine(Consumer<String> showdecisionline) {
        this.showdecisionline = showdecisionline;
        return this;
    }

    public Controller setWritetoStatusLine(Consumer<String> writetostatusline) {
        this.writetostatusline = writetostatusline;
        return this;
    }

    private void resetObjectProperties() {
        try {
            createObjectProperties(configfilecontroller.getParsedConfigFile());
        } catch (IOException ex) {
            writetostatusline.accept(ex.getLocalizedMessage());
        }
        sketchchangeaction.run();
    }

    private void createObjectProperties(JsonObject parsedjson) throws IOException {
        simulationtime = 0;
        sketchproperty = new PropertySketch();
        sketchproperty.parse(parsedjson);
        //
        windflow = new WindFlow(sketchproperty);
        waterflow = new WaterFlow(sketchproperty);
        course = new Course(sketchproperty);
        boats = new Boats(sketchproperty);
        boatstrategies = new BoatStrategies(sketchproperty, course, boats, windflow, waterflow);
        painter = new Painter(boatstrategies, sketchproperty, windflow, waterflow, course, boats, writetostatusline);
    }

    public PropertySketch getProperty() {
        return sketchproperty;
    }

    public void addNewMark() throws IOException {
        PropertyMark newmarkproperty = new PropertyMark();
        sketchproperty.getMarks().add(newmarkproperty);
    }

    public void addNewBoat() throws IOException {
        sketchproperty.getBoats().add(new PropertyBoat());
    }

    public void addNewWindFlowComponent() throws IOException {
        PropertyFlowComponent newflow = PropertyFlowComponent.factory("constantflow", () -> sketchproperty.getDisplayArea());
        sketchproperty.getWind().add(newflow);
    }

    public void addNewWaterFlowComponent() throws IOException {
        PropertyFlowComponent newflow = PropertyFlowComponent.factory("constantflow", () -> sketchproperty.getDisplayArea());
        sketchproperty.getWater().add(newflow);
    }

    public void addNewLeg() throws IOException {
        sketchproperty.getCourse().getPropertyLegValues().add();
    }

    public void paint(SketchPane canvas) {
        painter.paint(canvas);
    }

    public void start() {
        if (isRunning) {
            return;
        }
        int rate = (int) (sketchproperty.getDisplay().getSecondsperdisplay() * 1000 / sketchproperty.getDisplay().getSpeedup());
        timer = new Timer();
        runner = new TimeStepRunner();
        timer.scheduleAtFixedRate(runner, 0, rate);
        isRunning = true;
    }

    public void stop() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
        timer.cancel();
    }

    public void reset() {
        stop();
        timerlog.clear();
        resetObjectProperties();
    }

    public void displaylog() {
        timerlog.write2output(showdecisionline);
    }

    public void displayfilteredlog(String boatname) {
        timerlog.writefiltered2output(showdecisionline, boatname);
    }

    private TimerLog timerlog = new TimerLog();

    private class TimeStepRunner extends TimerTask {

        @Override
        public void run() {
            try {
                int secondsperdisplay = sketchproperty.getDisplay().getSecondsperdisplay();
                while (secondsperdisplay > 0) {
                    timerlog.setTime(simulationtime);
                    windflow.timerAdvance(simulationtime, timerlog);
                    waterflow.timerAdvance(simulationtime, timerlog);
                    boatstrategies.timerAdvance(sketchproperty, simulationtime, timerlog, windflow, waterflow);
                    secondsperdisplay--;
                    simulationtime++;
                }
                timechangeaction.accept(simulationtime);
                sketchchangeaction.run();
            } catch (IllegalStateFailure ex) {
                writetostatusline.accept(ex.getLocalizedMessage());
            } catch (Exception ex) {
                writetostatusline.accept(ex.getLocalizedMessage());
            }
        }
    }
}