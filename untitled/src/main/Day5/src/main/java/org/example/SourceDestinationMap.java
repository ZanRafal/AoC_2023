package org.example;

public class SourceDestinationMap {
    private long source;
    private long destination;
    private long range;

    public long getSource() {
        return source;
    }

    public long getDestination() {
        return destination;
    }

    public long getRange() {
        return range;
    }

    public SourceDestinationMap(long source, long destination, long range) {
        this.source = source;
        this.destination = destination;
        this.range = range;
    }

    @Override
    public String toString() {
        return "SourceDestinationMap{" +
                "source=" + source +
                ", destination=" + destination +
                ", range=" + range +
                '}';
    }
}
