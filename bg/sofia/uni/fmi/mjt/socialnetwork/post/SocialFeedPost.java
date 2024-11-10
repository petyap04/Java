package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SocialFeedPost implements Post {
    private static long idCounter;
    private final String uniqueId;
    private final UserProfile author;
    private final String content;
    private final LocalDateTime publishedAt;
    private final Map<UserProfile, ReactionType> reactionsOfUsers;
    private final Map<ReactionType, Integer> reactionCounts;

    static {
        idCounter = 0;
    }

    public SocialFeedPost(UserProfile author, String content) {
        this.author = author;
        this.content = content;
        publishedAt = LocalDateTime.now();
        reactionsOfUsers = new HashMap<UserProfile, ReactionType>();
        this.reactionCounts = new HashMap<>();
        uniqueId = String.valueOf(idCounter);
        idCounter++;
    }

    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public UserProfile getAuthor() {
        return author;
    }

    @Override
    public LocalDateTime getPublishedOn() {
        return publishedAt;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean addReaction(UserProfile userProfile, ReactionType reactionType) {
        if (userProfile == null || reactionType == null) {
            throw new IllegalArgumentException();
        }
        ReactionType prevReaction = reactionsOfUsers.put(userProfile, reactionType);
        if (prevReaction != null) {
            reactionCounts.put(prevReaction, reactionCounts.get(prevReaction) - 1);
        }
        reactionCounts.put(reactionType, reactionCounts.getOrDefault(reactionType, 0) + 1);
        return prevReaction == null;
    }

    @Override
    public boolean removeReaction(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException();
        }
        ReactionType removedReaction = reactionsOfUsers.remove(userProfile);
        if (removedReaction != null) {
            reactionCounts.put(removedReaction, reactionCounts.get(removedReaction) - 1);
            return true;
        }
        return false;
    }

    @Override
    public Map<ReactionType, Set<UserProfile>> getAllReactions() {
        Map<ReactionType, Set<UserProfile>> reactionMap = new HashMap<>();
        for (Map.Entry<UserProfile, ReactionType> entry : reactionsOfUsers.entrySet()) {
            ReactionType reactionType = entry.getValue();
            UserProfile userProfile = entry.getKey();
            if (!reactionMap.containsKey(reactionType)) {
                reactionMap.put(reactionType, new HashSet<>());
            }
            reactionMap.get(reactionType).add(userProfile);
        }
        return Collections.unmodifiableMap(reactionMap);
    }

    @Override
    public int getReactionCount(ReactionType reactionType) {
        if (reactionType == null) {
            throw new IllegalArgumentException();
        }
        return reactionCounts.getOrDefault(reactionType, 0);
    }

    @Override
    public int totalReactionsCount() {
        return reactionsOfUsers.size();
    }
}