package Object;

public class customerDoor extends GameObject {
    
    public customerDoor() {
        name = "Door";
        getObjectImage("/Assets/Objects/door.png"); //Set image by using method in gameobject
        needsInteraction = true; //Player needs to interact first
    }

    @Override
    public void objectInteraction() {
        // TODO implement this method for customer door
        getObjectImage("/Assets/Objects/door2.png");
    }


}
