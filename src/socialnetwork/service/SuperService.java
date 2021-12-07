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



    public SuperService(FriendshipService friendshipService, UserService userService){
        this.friendshipService = friendshipService;
        this.userService = userService;
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



    //david code
    public Iterable<Friendship> getAllFriendships(Long id){

        Iterable<Friendship> allFriendships = friendshipService.repo.findAll();
        Set<Friendship> friendships = new HashSet<>();
        for(Friendship friendship : allFriendships)
            if(friendship.getFriendshipStatus().equals("approved") && ( friendship.getFr1() == id || friendship.getFr2() == id  )  )
                friendships.add(friendship);

        return friendships;

    }

    public User findOneUser(Long messageTask){
        User user = userService.repo.findOne(messageTask);
        return user;
    }


    public void responseToFriendRequest(Long idFrom, Long idTo, String response) throws ServiceException {
        Long sender = idFrom;
        if(idFrom > idTo){
            Long swap = idFrom;
            idFrom = idTo;
            idTo = swap;
        }
        if(friendshipService.repo.findOne(new Tuple<Long, Long> (idFrom, idTo)) == null)
            throw new ServiceException("Invalid friend request!");
        Friendship newFriendship = new Friendship(response, idFrom, idTo, sender);
        friendshipService.repo.update(newFriendship);
    }


    public List<Friendship> pendingFriendships(Long id) {
        List<Friendship> pendingFriendships = new ArrayList<>();
        Iterable<Friendship> friendships = friendshipService.repo.findAll();
        for (Friendship friendship : friendships) {
            if( ((friendship.getId().getLeft().equals(id) && !friendship.getId().getLeft().equals(friendship.getSender()) )
                    || (friendship.getId().getRight().equals(id) && !friendship.getId().getRight().equals(friendship.getSender())))
                    && friendship.getFriendshipStatus().equals("pending") )
                pendingFriendships.add(friendship);
        }
        return pendingFriendships;
    }

    public void sendFriendRequest(Long idFrom, Long idTo) throws ServiceException {
        Long sender = idFrom;
        if(idFrom > idTo){
            Long swap = idFrom;
            idFrom = idTo;
            idTo = swap;
        }
        if(userService.findUserByID(idTo) == null)
            throw new ServiceException("invalid id!\n");
        if(friendshipService.repo.findOne(new Tuple<Long, Long> (idFrom, idTo)) != null)
            throw new ServiceException("There is already a friendship/friend request/rejected friend request between these users\n");
        if(idTo == idFrom)
            throw new ServiceException("Wtf cant send a friend request yourself\n");
        Friendship friendship = new Friendship("pending", idFrom, idTo, sender);
        friendshipService.repo.save(friendship);
    }

}
