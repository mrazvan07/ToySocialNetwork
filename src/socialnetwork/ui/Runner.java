package socialnetwork.ui;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.repoExceptions.RepoException;
import socialnetwork.service.SuperService;
import socialnetwork.service.ServiceException;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;

import java.util.*;

import static socialnetwork.Utils.constants.UiConstants.*;
import static socialnetwork.Utils.constants.RepoConstants.*;


public class Runner {
    private SuperService superService = null;

    public Runner(SuperService superService){
        this.superService = superService;
    }

    private void mainShowAllFriendships(){
        System.out.println("De implementat");
    }

    private void mainShowAllUsers(){
        System.out.println("De implementat");
    }

    private void UIAddUser(){
        Scanner console = new Scanner(System.in);
        System.out.print("First name: ");
        String first_name = console.next();
        String valid_first_name = first_name.strip();
        System.out.print("Last name: ");
        String last_name = console.next();
        String valid_last_name = last_name.strip();
        int rez = superService.addUser(valid_first_name,valid_last_name);
        if(rez == SUCCESFUL_OPERATION_RETURN_CODE)
            System.out.println("User added succesfully!");
        else
            System.out.println("User already exists!");
    }


    private void  UIDeleteFriend(User user){
        if(superService.getAllFriendsForGivenUser(user).size() == 0){
            System.out.println("This user has no friends! :(");
            return;
        }
        Scanner console = new Scanner(System.in);
        System.out.print("Friend name: ");
        String name = console.next();
        String stripped_name = name.strip();
        List<User> usersMatchingName = superService.findAllFriendsMathcingNameForGivenUser(user,stripped_name);
        if(usersMatchingName.size() == 0){
            System.out.println("No friends found mathcing this name!");
            return;
        }
        for(int i=0;i<usersMatchingName.size();i++)
            System.out.println(String.format("%d. ", i) + usersMatchingName.get(i));
        while(true) {
            try {
                System.out.println("Select the friend to be removed: ");
                System.out.print(">>> ");
                Integer selected_user_index = console.nextInt();
                if(selected_user_index<0 || selected_user_index >= usersMatchingName.size()){
                    System.out.println("Invalid friend selected!");
                    break;
                }
                superService.deleteFriendForUser(user,usersMatchingName.get(selected_user_index));
                return;

            } catch (InputMismatchException ex) {
                System.out.println("Invalid friend selected!");
                break;
            }
        }
    }

    private void  UIDeleteThisUser(User user){
       superService.removeUser(user);
    }

    private void  UIShowAllFriendsForThisUser(User user){
       Set<User> friends = superService.getAllFriendsForGivenUser(user);
       if(friends.size() == 0){
           System.out.println("No friends to show");
           return;
       }
       for(User friend: friends)
           System.out.println(friend);
    }

    private void  runSelectedUserMenu(User user){
        while(true){
            showSelectedUserMenu();
            Scanner console = new Scanner(System.in);
            String command = console.next();
            String stripped_command = command.strip();
            try{
                switch (stripped_command){
                    /*case ADD_FRIEND:
                        UIAddFriend(user);
                        break;*/
                    case DELETE_FRIEND:
                        UIDeleteFriend(user);
                        break;
                    case DELETE_THIS_USER:
                        UIDeleteThisUser(user);
                        return;
                    case SHOW_ALL_FRIENDS_FOR_THIS_USER:
                        printAllFriendships(user.getId());
                        break;
                    case SEND_FRIEND_REQUEST:
                        sendFriendRequest(user.getId());
                        break;
                    case RESPOND_FRIEND_REQUESTS:
                        responseFriendRequest(user.getId());
                        break;
                    case RETURN_TO_USER_OPERATIONS:
                        return;
                    default:
                        System.out.println("Invalid command!");
                }
            }
            catch (RepoException | ValidationException ex){
                System.out.println(ex.getMessage());
            }
        }
    }







    private void UISelectUser(){
        Scanner console = new Scanner(System.in);
        System.out.print("User name: ");
        String name = console.next();
        String stripped_name = name.strip();
        List<User> usersMatchingName = superService.findUsersByName(stripped_name);
        if(usersMatchingName.size() == 0){
            System.out.println("No users found mathcing this name!");
            return;
        }
        for(int i=0;i<usersMatchingName.size();i++)
            System.out.println(String.format("%d. ", i) + usersMatchingName.get(i));
       while(true) {
           try {
               System.out.println("Select a user: ");
               System.out.print(">>> ");
               Integer selected_user_index = console.nextInt();
               if(selected_user_index<0 || selected_user_index >= usersMatchingName.size()){
                   System.out.println("Invalid user selected!");
                   break;
               }
               runSelectedUserMenu(usersMatchingName.get(selected_user_index));
               return;

           } catch (InputMismatchException ex) {
               System.out.println("Invalid user selected!");
               break;
           }
       }
    }

    private void runUserOperationMenu(){
        while(true){
            showUserOperationsMenu();
            Scanner console = new Scanner(System.in);
            String command = console.next();
            String stripped_command = command.strip();
            try{
                switch (stripped_command){
                    case ADD_USER:
                        UIAddUser();
                        break;
                    case SELECT_USER:
                        UISelectUser();
                        break;
                    case EXIT_USER_OPERATIONS:
                        return;
                    default:
                        System.out.println("Invalid command!");
                }
            }
            catch (RepoException | ValidationException ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    private void runMainMenu(){
        while(true){
            showMainMenu();
            Scanner console = new Scanner(System.in);
            String command = console.next();
            String stripped_command = command.strip();
            try{
                switch (stripped_command){
                    case MAIN_SHOW_ALL_USERS:
                        mainShowAllUsers();
                        break;
                    case MAIN_SHOW_ALL_FRIENDSHIPS:
                        mainShowAllFriendships();
                        break;
                    case MAIN_USER_OPERATIONS:
                        runUserOperationMenu();
                        break;
                    case MAIN_EXIT_APP:
                        System.out.println("Sayonara");
                        return;
                    default:
                        System.out.println("Invalid command!");
                }
            }
            catch (RepoException | ValidationException ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    public void runApp(){
        runMainMenu();
    }


    private void showMainMenu(){
        System.out.println("==================== MENU ====================");
        System.out.println("1. Show all users");
        System.out.println("2. Show all friendships");
        System.out.println("3. graf");
        System.out.println("4. graf");
        System.out.println("5. User Operations");
        System.out.println("6. Exit app");
        System.out.print(">>> ");
    }

    private void showUserOperationsMenu(){
        System.out.println("==================== USER OPERATIONS MENU ====================");
        System.out.println("1. Add new User");
        System.out.println("2. Select User");
        System.out.println("3. Return to main Menu");
        System.out.print(">>> ");
    }

    private void showSelectedUserMenu(){
        System.out.println("==================== SELECTED USER MENU ====================");
        System.out.println("1. Add friend");
        System.out.println("2. Delete friend");
        System.out.println("3. Delete this user");
        System.out.println("4. Show all friendships for this user");
        System.out.println("5. Return to User Operations Menu");
        System.out.println("6. Send a friend request");
        System.out.println("7. Respond to friend requests");
        System.out.print(">>> ");
    }







    //david code

    public void printAllFriendships(Long id){
        Iterable<Friendship> friendships = superService.getAllFriendships(id);
        for(Friendship friendship : friendships)
            System.out.println(friendship.getId().toString());
    }

    public void sendFriendRequest(Long idFrom){
        Scanner input = new Scanner(System.in);
        System.out.println("Introduce the id of the user: ");
        String idTo = input.next();
        try{
            Long IDTo = Long.parseLong(idTo);
            superService.sendFriendRequest(idFrom, IDTo);

        }catch (NumberFormatException ex){
            System.out.println("ID should be a positive number!\n");
        }catch(ServiceException ex){
            System.out.println(ex.getMessage());
        }
    }


    public void responseFriendRequest(Long idFrom){
        List<Friendship> pendingFriendships = superService.pendingFriendships(idFrom);
        if(pendingFriendships.size() == 0){
            System.out.println("There are no friend requests\n");
            return;
        }
        System.out.println("List of friend requests:");
        for(Friendship friendship: pendingFriendships){
            if(idFrom.equals(friendship.getId().getLeft()))
                System.out.println("Id="+String.valueOf(friendship.getId().getRight()) + " " + superService.findOneUser(friendship.getId().getRight()).getLastName() + " " + superService.findOneUser(friendship.getId().getRight()).getFirstName());
            if(idFrom.equals(friendship.getId().getRight()))
                System.out.println("Id="+String.valueOf(friendship.getId().getLeft()) + " " + superService.findOneUser(friendship.getId().getLeft()).getLastName() + " " + superService.findOneUser(friendship.getId().getLeft()).getFirstName());
        }
        Scanner input = new Scanner(System.in);
        System.out.println("Introduce the id of the user you want to response:");
        String idUser = input.next();
        try {
            Long IDUser = Long.parseLong(idUser);
            Long id1 = idFrom;
            Long id2 = IDUser;

            System.out.println("Type 'approved' or 'rejected': ");
            String status = input.next();
            if(!status.equals("approved") && !status.equals("rejected")){
                System.out.println("Invalid response!\n");
                return;
            }

            superService.responseToFriendRequest( id1, id2, status);

        } catch (NumberFormatException ex) {
            System.out.println("IDs should be positive numbers\n");
        } catch(ServiceException ex){
            System.out.println(ex.getMessage());
        }

    }



}
