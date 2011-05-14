package controllers;

import play.*;
import play.libs.F;
import play.mvc.*;

import java.util.*;

import models.*;
import play.utils.HTML;

import static play.libs.F.Matcher.*;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void room( String username) {
        render(username);
    }

    public static void room2(String username) {
        room(username); // HTTP redirects
    }

    public static class OurWS extends WebSocketController {

        public static void join(String username) {

            final Room room = Room.instance;
            final F.EventStream<Room.Message> stream = room.join();

            while( inbound.isOpen()) {

                F.Either<Room.Message, Http.WebSocketEvent> e = await(F.Promise.waitEither( stream.nextEvent(), inbound.nextEvent()));


                for( Room.Message m : ClassOf(Room.Message.class).match(e._1) ) {
                    outbound.send( m.username + ": " + m.msg);
                }

                for( String m : Http.WebSocketEvent.TextFrame.match(e._2)){
                    m = HTML.htmlEscape(m); //escape it so we don't get hacked again :)
                    room.send(username, m);
                }


            }

        }

    }

}