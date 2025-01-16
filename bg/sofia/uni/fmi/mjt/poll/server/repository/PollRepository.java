package bg.sofia.uni.fmi.mjt.poll.server.repository;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;

import java.util.Map;

public interface PollRepository {

    int addPoll(Poll poll);

    Poll getPoll(int pollId);

    Map<Integer, Poll> getAllPolls();

    void clearAllPolls();

}