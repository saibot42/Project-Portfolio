package Object;

import java.util.Timer;
import java.util.TimerTask;

public class door extends GameObject {

    public door() {
        name = "Door";
        getObjectImage("/Assets/Objects/door.png"); //Set image by using method in gameobject
    }

    @Override
    public void objectInteraction() {
        getObjectImage("/Assets/Objects/door2.png");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getObjectImage("/Assets/Objects/door.png");
                timer.cancel(); //Clean up the timer
            }
        }, 400); //Delay of 0,45 seconds (400 milliseconds)
    }
}
