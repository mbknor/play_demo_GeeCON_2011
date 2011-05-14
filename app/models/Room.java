package models;

import play.libs.F;

public class Room {

    public F.ArchivedEventStream<Message> stream = new F.ArchivedEventStream<Message>(100);
    public static Room instance = new Room();


    public F.EventStream<Message> join() {
        return stream.eventStream();
    }

    public void send( String username, String msg ) {
        Message m = new Message(username, msg);
        stream.publish(m);
    }

    public static class Message {
        public String username;
        public String msg;

        public Message(String username, String msg) {
            this.username = username;
            this.msg = msg;
        }
    }
}
