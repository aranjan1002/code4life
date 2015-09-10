package edu.strippacking;

import java.util.ArrayList;

public interface SchedulingAlgorithm {
    public double schedule(ArrayList<Job> jobs, double D);
}