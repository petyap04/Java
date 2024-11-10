package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.HashSet;

public class SocialNetworkImpl implements SocialNetwork {

    private final Set<UserProfile> users = new HashSet<>();
    private final List<Post> posts = new ArrayList<>();

    @Override
    public void registerUser(UserProfile userProfile) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException();
        }
        if (!users.add(userProfile)) {
            throw new UserRegistrationException();
        }
    }

    @Override
    public Set<UserProfile> getAllUsers() {
        return Collections.unmodifiableSet(users);
    }

    @Override
    public Post post(UserProfile userProfile, String content) throws UserRegistrationException {
        if (userProfile == null || content == null || content.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (!users.contains(userProfile)) {
            throw new UserRegistrationException();
        }
        Post newPost = new SocialFeedPost(userProfile, content);
        posts.add(newPost);
        return newPost;
    }

    @Override
    public Collection<Post> getPosts() {
        return Collections.unmodifiableCollection(posts);
    }

    private boolean hasCommonInterest(UserProfile user1, UserProfile user2) {
        Collection<Interest> interests1 = user1.getInterests();
        Collection<Interest> interests2 = user2.getInterests();
        for (Interest interest : interests1) {
            if (interests2.contains(interest)) {
                return true;
            }
        }
        return false;
    }

    private boolean areConnected(UserProfile user1, UserProfile user2) {
        if (user1.equals(user2)) {
            return true;
        }
        Set<UserProfile> visited = new HashSet<>();
        Queue<UserProfile> queue = new LinkedList<>();
        queue.add(user1);
        visited.add(user1);
        while (!queue.isEmpty()) {
            UserProfile currentUser = queue.poll();
            for (UserProfile friend : currentUser.getFriends()) {
                if (friend.equals(user2)) {
                    return true;
                }
                if (!visited.contains(friend)) {
                    visited.add(friend);
                    queue.add(friend);
                }
            }
        }
        return false;
    }

    @Override
    public Set<UserProfile> getReachedUsers(Post post) {
        if (post == null) {
            throw new IllegalArgumentException();
        }
        UserProfile author = post.getAuthor();
        Set<UserProfile> reachedUsers = new LinkedHashSet<>();
        for (UserProfile user : users) {
            if (user.equals(author)) {
                continue;
            }
            if (hasCommonInterest(user, author) && areConnected(user, author)) {
                reachedUsers.add(user);
            }
        }

        return reachedUsers;
    }

    @Override
    public Set<UserProfile> getMutualFriends(UserProfile userProfile1, UserProfile userProfile2)
            throws UserRegistrationException {
        if (userProfile1 == null || userProfile2 == null) {
            throw new IllegalArgumentException();
        }
        if (!users.contains(userProfile1) || !users.contains(userProfile2)) {
            throw new UserRegistrationException();
        }
        Set<UserProfile> mutualFriends = new LinkedHashSet<>(userProfile1.getFriends());
        mutualFriends.retainAll(userProfile2.getFriends());
        return mutualFriends;
    }

    @Override
    public SortedSet<UserProfile> getAllProfilesSortedByFriendsCount() {
        SortedSet<UserProfile> sortedSet = new TreeSet<>(users);
        return sortedSet;
    }
}