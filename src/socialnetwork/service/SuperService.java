package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static socialnetwork.Utils.constants.RepoConstants.*;

public class SuperService {
    private FriendshipService friendshipService = null;
    private UserService userService = null;
    private MessageService messageService = null;

    private Long genereateUserID(){
        long leftLimit = 100L;
        long rightLimit = 999L;
        long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        return generatedLong;
    }

    private void initializeFriendshipLists(){
        Iterable<Friendship> friendships = friendshipService.findAll();
        for(Friendship el : friendships){
            User user1 = userService.findUserByID(el.getId().getLeft());
            User user2 = userService.findUserByID(el.getId().getRight());
            user1.addFriend(user2);
            user2.addFriend(user1);

        }
    }



    public SuperService(FriendshipService friendshipService, UserService userService,MessageService messageService){
        this.friendshipService = friendshipService;
        this.userService = userService;
        this.messageService = messageService;

        //initializeFriendshipLists();
    }

    public int addUser(String firstName, String lastName){
        List<User> op = StreamSupport.stream(userService.findAll().spliterator(), false)
                .filter(x->x.getFirstName().matches(firstName) && x.getLastName().matches(lastName))
                .collect(Collectors.toList());
        if(!op.isEmpty())
            return UNSUCCESFUL_OPERATION_RETURN_CODE;
        User newUser = new User(firstName,lastName);
        userService.addUser(newUser);
        return SUCCESFUL_OPERATION_RETURN_CODE;
    }

    public void removeUser(User user){
       /* Long id = user.getId();
        List<User> friends = userService.findUserByID(id).getFriends();
        if(friends.size() == 0)
            userService.removeUser(id);
        else{
            for(User friend: friends){
                friendshipService.deleteFriendship(new Tuple<Long,Long>(id,friend.getId()));
                userService.findUserByID(friend.getId()).getFriends().remove(user);
            }
            userService.removeUser(id);
        }*/
        StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(x->x.getId().getLeft().equals(user.getId()) || x.getId().getRight().equals(user.getId()))
                .forEach(x->{
                    friendshipService.deleteFriendship(new Tuple<Long,Long>(x.getId().getLeft(),x.getId().getRight()));
                });
        userService.removeUser(user.getId());
    }

    public void deleteFriendForUser(User user,User friend){
        friendshipService.deleteFriendship(new Tuple<>(user.getId(),friend.getId()));
    }

    public Iterable<User> findAllUsers(){
        return userService.findAll();
    }

    public List<User> findAllFriendsMathcingNameForGivenUser(User user,String name){
        List<User> friends_found = new ArrayList<>();
        for(User friend: getAllFriendsForGivenUser(user))
            if(friend.getFirstName().matches(name))
                friends_found.add(friend);
        return friends_found;
    }

    public  List<User> findUsersByName(String name){
        List<User> users = StreamSupport.stream(userService.findAll().spliterator(), false)
                .filter(x->x.getFirstName().matches(name))
                .collect(Collectors.toList());
        return users;
    }

    public Set<User> getAllFriendsForGivenUser(User user){
        Set<User> users1 = StreamSupport.stream(friendshipService.findAll().spliterator(), false)
                .filter(friendship->friendship.getId().getLeft().equals(user.getId()) || friendship.getId().getRight().equals(user.getId()))
                .map(friendship -> {
                    if(friendship.getId().getLeft().equals(user.getId())) return userService.findUserByID(friendship.getId().getRight());
                     else return userService.findUserByID(friendship.getId().getLeft());
                })
                .collect(Collectors.toSet());
        return users1;
    }

    public int addFriendForGivenUser(User given_user, User friend_to_be_added) {
        for (Friendship friend : friendshipService.findAll())
            if (friend.getId().equals(new Tuple<Long,Long>(given_user.getId(),friend_to_be_added.getId())))
                return UNSUCCESFUL_OPERATION_RETURN_CODE;
        Friendship newFriendship = new Friendship();
        newFriendship.setId(new Tuple<>(given_user.getId(),friend_to_be_added.getId()));
        friendshipService.addFriendShip(newFriendship);
        return SUCCESFUL_OPERATION_RETURN_CODE;
    }

}
