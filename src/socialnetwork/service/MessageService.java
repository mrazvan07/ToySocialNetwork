package socialnetwork.service;

import socialnetwork.domain.Message;
import socialnetwork.repository.Repository;

public class MessageService {
    private Repository<Long, Message> repo;

    public MessageService(Repository<Long,Message> repo){
        this.repo = repo;
    }

    public void addMessage(Message message){
        repo.save(message);
    }

    public void deleteMessage(Long id){
        repo.delete(id);
    }

    public void undoMessageDelete(Message message){
        repo.update(message);
    }

    public Iterable<Message> findAll(){
        return repo.findAll();
    }

    public Message findMessageById(Long id){
        return repo.findOneById(id);
    }

}
