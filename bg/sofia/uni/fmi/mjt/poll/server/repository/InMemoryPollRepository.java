package bg.sofia.uni.fmi.mjt.poll.server.repository;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPollRepository implements PollRepository {
    private final Map<Integer, Poll> polls = new ConcurrentHashMap<>();
    private int currentId = 1;

    @Override
    public synchronized int addPoll(Poll poll) {
        polls.put(currentId, poll);
        return currentId++;
    }

    @Override
    public Poll getPoll(int pollId) {
        return polls.get(pollId);
    }

    @Override
    public Map<Integer, Poll> getAllPolls() {
        return Map.copyOf(polls);
    }

    @Override
    public void clearAllPolls() {
        polls.clear();
    }
}
