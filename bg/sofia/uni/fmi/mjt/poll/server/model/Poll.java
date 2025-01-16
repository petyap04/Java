package bg.sofia.uni.fmi.mjt.poll.server.model;

import java.util.Map;

public record Poll(String question, Map<String, Integer> options) { }