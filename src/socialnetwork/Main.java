package socialnetwork;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.UserValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.FriendshipsDbRepository;
import socialnetwork.repository.database.UserDbRepository;
import socialnetwork.repository.file.FriendshipFileRepository;
import socialnetwork.repository.file.UserFileRepository;
import socialnetwork.repository.repoExceptions.FileError;
import socialnetwork.repository.repoExceptions.RepoException;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.SuperService;
import socialnetwork.service.UserService;
import socialnetwork.ui.Runner;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        //String fileName= ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        String fileName="data/users.csv";
        String fileName2="data/friendships.csv";
        Repository<Long, User> userFileRepository = null;
        Repository<Tuple<Long,Long>, Friendship> friendshipFileRepository = null;
        Repository<Long, User> userDbRepository = null;
        Repository<Tuple<Long,Long>, Friendship> friendshipDbRepository = null;
        try {
            userFileRepository = new UserFileRepository(fileName
                    , new UserValidator());
            friendshipFileRepository = new FriendshipFileRepository(fileName2, new FriendshipValidator());
            userDbRepository = new UserDbRepository("jdbc:postgresql://localhost:5432/academic","postgres","22adc#cJf6", new UserValidator());
            friendshipDbRepository = new FriendshipsDbRepository("jdbc:postgresql://localhost:5432/academic","postgres","22adc#cJf6",new FriendshipValidator());
        }
        catch (FileError ex){
            System.out.println(ex.getMessage());
            return;
        }
        catch (RepoException ex){
            System.out.println(ex.getMessage());
            return;
        }
        //FILE
        /*UserService userService = new UserService(userFileRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipFileRepository);
        SuperService superService = new SuperService(friendshipService,userService);*/

        //DataBase
        UserService userService = new UserService(userDbRepository);
        FriendshipService friendshipService = new FriendshipService(friendshipDbRepository);
        SuperService superService = new SuperService(friendshipService,userService);
        Runner runner = new Runner(superService);
        runner.runApp();
        //repoDB.findAll().forEach(System.out::println);
     /*   String first = "hagi";
        String last = "gica";
        List<Object> str = new ArrayList<>();
        str.add(first);
        str.add(last);
        User rez = userDbRepository.findOneByOtherAttributes(str);
        System.out.println(rez.getFriends());*/
        //friendshipDbRepository.findAll().forEach(System.out::println);

        //////

        /////
        ///
    }
}



