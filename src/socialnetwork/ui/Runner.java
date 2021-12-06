package socialnetwork.ui;

import socialnetwork.domain.User;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.repoExceptions.RepoException;
import socialnetwork.service.SuperService;

import java.time.LocalDateTime;
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

    private void  UIAddFriend(User user){
        Scanner console = new Scanner(System.in);
        System.out.println("First name: ");
        String name = console.next();
        String stripped_name = name.strip();
        List<User> usersMatchingName = superService.findUsersByName(stripped_name);
        if(usersMatchingName.size() == 0){
            System.out.println("No users found mathcing this name!");
            return;
        }
        for(int i=0;i<usersMatchingName.size();i++)
            System.out.println(String.format("%d. ", i) + usersMatchingName.get(i));
        try {
            System.out.println("Select user: ");
            System.out.print(">>> ");
            Integer selected_user_index = console.nextInt();
            if(selected_user_index<0 || selected_user_index >= usersMatchingName.size()){
                System.out.println("Invalid friend selected!");
                return;
            }
            int rez = superService.addFriendForGivenUser(user,usersMatchingName.get(selected_user_index));
            if(rez == 1)
                System.out.println("Friendship already exists!");
        } catch (InputMismatchException ex) {
            System.out.println("Invalid friend selected!");
        }
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

    private void  UIShowAllFriendshipsForThisUser(){
        Scanner console = new Scanner(System.in);
        System.out.print("First name: ");
        String first_name = console.next();
        String valid_first_name = first_name.strip();
        System.out.print("Last name: ");
        String last_name = console.next();
        String valid_last_name = last_name.strip();
        User user = new User (first_name, last_name);
        Set<String> friends = superService.getAllFriendshipsForGivenUser(user);
        if(friends.size() == 0){
            System.out.println("No friendships to show");
            return;
        }
        for(String friend: friends)
            System.out.println(friend);
    }

    /*private void  UIShowAllFriendshipsForThisUserandMonth(User user, LocalDateTime month){
        Set<User> friends = superService.getAllFriendsForGivenUser(user);
        if(friends.size() == 0){
            System.out.println("No friends to show");
            return;
        }
        for(User friend: friends)
            System.out.println(friend);
    }*/

    private void  runSelectedUserMenu(User user){
        while(true){
            showSelectedUserMenu();
            Scanner console = new Scanner(System.in);
            String command = console.next();
            String stripped_command = command.strip();
            try{
                switch (stripped_command){
                    case ADD_FRIEND:
                        UIAddFriend(user);
                        break;
                    case DELETE_FRIEND:
                        UIDeleteFriend(user);
                        break;
                    case DELETE_THIS_USER:
                        UIDeleteThisUser(user);
                        return;
                    case SHOW_ALL_FRIENDS_FOR_THIS_USER:
                        UIShowAllFriendsForThisUser(user);
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
                    case MAIN_SHOW_FRIENDSHIPS_USER:
                        System.out.println("Show Friendships");
                        UIShowAllFriendshipsForThisUser();
                        break;
                    case MAIN_SHOW_FRIENDSHIPS_MONTH:
                        System.out.println("Show Friendships from a Month");
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
        System.out.println("7. Show friendships of a user");
        System.out.println("8. Show friendships of a user that had been created in month");
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
        System.out.print(">>> ");
    }





}
