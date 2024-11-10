package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class DefaultUserProfile implements UserProfile {
    String username;
    Collection<Interest> interests;
    Collection<UserProfile> friends;

    public DefaultUserProfile(String username) {
        this.username = username;
        interests = new HashSet<>();
        friends = new HashSet<>();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<Interest> getInterests() {
        return Collections.unmodifiableCollection(interests);
    }

    @Override
    public boolean addInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException();
        }
        return interests.add(interest);
    }

    @Override
    public boolean removeInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException();
        }
        return interests.remove(interest);
    }

    @Override
    public Collection<UserProfile> getFriends() {
        return Collections.unmodifiableCollection(friends);
    }

    @Override
    public boolean addFriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException();
        }

        if (userProfile.equals(this)) {
            throw new IllegalArgumentException();
        }

        if (this.friends.contains(userProfile)) {
            return false;
        }

        this.friends.add(userProfile);
        userProfile.addFriend(this);

        return true;
    }

    @Override
    public boolean unfriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException();
        }

        if (userProfile.equals(this)) {
            throw new IllegalArgumentException();
        }

        if (!this.friends.contains(userProfile)) {
            return false;
        }

        this.friends.remove(userProfile);
        userProfile.unfriend(this);

        return true;
    }

    @Override
    public boolean isFriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("Invalid user profile to be checked for friendship!");
        }

        return this.friends.contains(userProfile);
    }

    @Override
    public int getFriendsCount() {
        return friends.size();
    }

    @Override
    public int compareTo(UserProfile o) {
        return Integer.compare(friends.size(), o.getFriendsCount());
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UserProfile)) return false;
        UserProfile other = (UserProfile) obj;
        return username.equals(other.getUsername());
    }
}