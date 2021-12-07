package socialnetwork.service;

import socialnetwork.domain.Message;
import socialnetwork.repository.Repository;

public class MessageService {
    Repository<Long, Message> repo;

    public MessageService(Repository<Long,Message> repo){
        this.repo = repo;
    }

    public void addMessage(Message message){
        repo.save(message);
    }

    public void deleteMessage(Long id){
        repo.delete(id);
    }

    public Iterable<Message> findAll(){
        return repo.findAll();
    }
}
