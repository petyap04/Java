package bg.sofia.uni.fmi.mjt.todoist.server.database;

import bg.sofia.uni.fmi.mjt.todoist.server.user.User;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.userexceptions.UserExistsException;
import bg.sofia.uni.fmi.mjt.todoist.server.exception.userexceptions.UserNotFoundException;

import java.util.Collection;

public class UserDatabase extends FileDatabase<User> {
    private static final String USER_DATABASE_FILE_PATH = ".\\users.txt";

    public UserDatabase() {
        super(USER_DATABASE_FILE_PATH);
    }

    public User create(String username, String password) throws UserExistsException {
        if (this.objects.containsKey(username)) {
            throw new UserExistsException();
        }

        User user = new User(username, password);
        this.objects.put(username, user);
        return user;
    }

    public User get(String username) throws UserNotFoundException {
        if (!this.objects.containsKey(username)) {
            throw new UserNotFoundException();
        }

        return this.objects.get(username);
    }

    public void removeCollaborations(Collection<String> usernames, String collaboration) {
        for (String username : usernames) {
            if (this.objects.containsKey(username)) {
                this.objects.get(username).removeCollaboration(collaboration);
            }
        }
    }
}
