package org.example;

import java.util.Arrays;
import java.util.List;

public record Workflow(String id, List<Rule> rules, String destination) {

    public static Workflow ofWorkflow(String line) {
        String id = line.substring(0, line.indexOf("{"));
        String stepsString = line.substring(line.indexOf("{") + 1, line.indexOf("}"));
        List<String> steps = Arrays.asList(stepsString.split(","));
        String destination = steps.get(steps.size() - 1);
        List<Rule> rules = steps.subList(0, steps.size() - 1).stream().map(Rule::ofStep).toList();
        return new Workflow(id, rules, destination);
    }
    @Override
    public String   toString() {
        return "Workflow{" +
                "id='" + id + '\'' +
                ", rules=" + rules +
                ", destination='" + destination + '\'' +
                '}';
    }
}
