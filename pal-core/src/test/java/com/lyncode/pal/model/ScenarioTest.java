package com.lyncode.pal.model;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class ScenarioTest {

    @Test
    public void greater() throws Exception {
        Scenario scenario1 = Scenario.from(getClass().getMethod("greater"));
        scenario1.markAs(Scenario.Status.Passed);

        Scenario scenario2 = Scenario.from(getClass().getMethod("greater"));
        scenario2.markAs(Scenario.Status.Failed);


        assertThat(scenario1.compareTo(scenario2), greaterThan(0));
    }
    @Test
    public void greater1() throws Exception {
        Scenario scenario1 = Scenario.from(getClass().getMethod("greater"));
        scenario1.markAs(Scenario.Status.Ignored);

        Scenario scenario2 = Scenario.from(getClass().getMethod("greater"));
        scenario2.markAs(Scenario.Status.Failed);


        assertThat(scenario1.compareTo(scenario2), greaterThan(0));
    }
    @Test
    public void greater2() throws Exception {
        Scenario scenario1 = Scenario.from(getClass().getMethod("greater"));
        scenario1.markAs(Scenario.Status.Ignored);

        Scenario scenario2 = Scenario.from(getClass().getMethod("greater"));
        scenario2.markAs(Scenario.Status.Passed);


        assertThat(scenario1.compareTo(scenario2), greaterThan(0));
    }
}