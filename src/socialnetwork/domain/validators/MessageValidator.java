package socialnetwork.domain.validators;

import socialnetwork.domain.Message;

public class MessageValidator implements Validator<Message>{

    @Override
    public void validate(Message message) throws ValidationException{
        String messageError = "";
        if(message.getId() == null)
            messageError += "ID error";
        if(message.getMesaj().isEmpty())
            messageError += "Empty message";
        if(messageError.length() > 0)
            throw new ValidationException(messageError);
    }
}
