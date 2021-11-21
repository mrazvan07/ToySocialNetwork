package socialnetwork.service;

import socialnetwork.domain.User;
import socialnetwork.repository.Repository;

import java.util.ArrayList;
import java.util.List;

import static socialnetwork.Utils.constants.ValidatorConstants.TEMPORARY_USER_ID;

public class UserService {
    private Repository<Long, User> repo;

    public UserService(Repository<Long, User> repo) {
        this.repo = repo;
    }

    public void addUser(User user) {
        user.setId(TEMPORARY_USER_ID);
        repo.save(user);
    }

    public void removeUser(Long id){
        repo.delete(id);
    }

    public Iterable<User> findAll(){
        return repo.findAll();
    }

    public User findUserByID(Long id){
        return repo.findOneById(id);
    }

    /*public void addFriend(User user, User friend){
        user.getFriends().add(friend);
        friend.getFriends().add(user);
    }*/

    public User findUserByFirstAndLastName(String first_name, String last_name) {
        List<Object> args = new ArrayList<>();
        args.add(first_name);
        args.add(last_name);
        return repo.findOneByOtherAttributes(args);
    }
}
