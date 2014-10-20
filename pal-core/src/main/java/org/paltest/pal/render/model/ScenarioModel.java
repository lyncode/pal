package org.paltest.pal.render.model;

import org.paltest.pal.model.PalTestScenario;
import org.paltest.pal.result.group.Group;
import org.paltest.pal.result.group.communication.CommunicationDiagramGroup;
import org.paltest.pal.result.group.given.GivensGroup;
import org.paltest.pal.result.group.specification.SpecificationGroup;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ScenarioModel implements Comparable<ScenarioModel> {
    private final PalTestScenario scenario;

    public ScenarioModel(PalTestScenario scenario) {
        this.scenario = scenario;
    }

    public String prettyStatus () {
        switch (scenario.status()) {
            case Failed:
                return "danger";
            case Passed:
                return "success";
        }
        return "default";
    }

    public String status ()  {
        return scenario.status().name();
    }

    public String name () {
        return scenario.name();
    }

    public String id () {
        return scenario.name().replaceAll("\\s", "");
    }

    public List<Group> groups () {
        return Arrays.asList(
                new SpecificationGroup(scenario.specification()),
                new GivensGroup(scenario.givensStore()),
                new CommunicationDiagramGroup(scenario.communicationStore(), UUID.randomUUID().toString())
        );
    }

    public String time () {
        long milliseconds = scenario.timeElapsed();
        if (Unit.Minutes.getDivisionFromMillis() < milliseconds)
            return plural(milliseconds / Unit.Minutes.getDivisionFromMillis(), "h");
        if (Unit.Seconds.getDivisionFromMillis() < milliseconds)
            return plural(milliseconds / Unit.Seconds.getDivisionFromMillis(), "m");
        if (Unit.Milliseconds.getDivisionFromMillis() < milliseconds)
            return plural(milliseconds / Unit.Milliseconds.getDivisionFromMillis(), "s");

        if (milliseconds <= 0)
            return "---";
        return plural(milliseconds, "ms");
    }

    private String plural(long value, String hour) {
        return String.format("%d%s", value, value == 1 ? hour : hour);
    }

    @Override
    public int compareTo(ScenarioModel o) {
        return scenario.compareTo(o.scenario);
    }

    private enum Unit {
        Milliseconds(1000),
        Seconds(1000*60),
        Minutes(1000*60*60),
        Hours(1000*60*60*24);
        private final int divisionFromMillis;

        Unit(int divisionFromMillis) {
            this.divisionFromMillis = divisionFromMillis;
        }

        public int getDivisionFromMillis() {
            return divisionFromMillis;
        }
    }
}
