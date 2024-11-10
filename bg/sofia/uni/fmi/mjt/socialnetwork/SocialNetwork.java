package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;

public interface SocialNetwork {
    void registerUser(UserProfile userProfile) throws UserRegistrationException;

    Set<UserProfile> getAllUsers();

    Post post(UserProfile userProfile, String content) throws UserRegistrationException;

    Collection<Post> getPosts();

    Set<UserProfile> getReachedUsers(Post post);

    Set<UserProfile> getMutualFriends(UserProfile userProfile1, UserProfile userProfile2)
            throws UserRegistrationException;

    SortedSet<UserProfile> getAllProfilesSortedByFriendsCount();

}