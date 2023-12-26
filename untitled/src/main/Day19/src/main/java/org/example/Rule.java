package org.example;

public class Rule {
        private final String destination;
        private final char condition;
        private final int value;
        private final String compared;

        public Rule(String destination, char condition, int value, String compared) {
                this.destination = destination;
                this.condition = condition;
                this.value = value;
                this.compared = compared;
        }

        public static Rule ofStep(String step) {
                final String[] stepParts = step.replaceAll(":", " ").split(" ");
                final String destination = stepParts[stepParts.length - 1];
                final String compared = String.valueOf(stepParts[0].charAt(0));
                final char condition = stepParts[0].charAt(1);
                final int value = Integer.parseInt(stepParts[0].substring(2));
                return new Rule(destination, condition, value, compared);
        }

        public String getDestination() {
                return destination;
        }

        public char getCondition() {
                return condition;
        }

        public int getValue() {
                return value;
        }

        public String getCompared() {
                return compared;
        }

        @Override
        public String toString() {
                return "Rule{" +
                        "destination='" + destination + '\'' +
                        ", condition=" + condition +
                        ", value=" + value +
                        ", compared='" + compared + '\'' +
                        '}';
        }
}
