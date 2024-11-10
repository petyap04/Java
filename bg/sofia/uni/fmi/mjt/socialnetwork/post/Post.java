package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public interface Post {

    String getUniqueId();

    UserProfile getAuthor();

    LocalDateTime getPublishedOn();

    String getContent();

    boolean addReaction(UserProfile userProfile, ReactionType reactionType);

    boolean removeReaction(UserProfile userProfile);

    Map<ReactionType, Set<UserProfile>> getAllReactions();

    int getReactionCount(ReactionType reactionType);

    int totalReactionsCount();

}