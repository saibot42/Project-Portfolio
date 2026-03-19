import java.util.ArrayList;

public class WoodenChest implements IChest {
    private ArrayList<Item> items = new ArrayList<Item>();
    private Golem assignedGolem;
    public WoodenChest(Golem assignedGolem) {
        this.assignedGolem = assignedGolem;
    }

    @Override
    public void addItem(Item item) {
        items.add(item);
    }

    @Override
    public ArrayList<Item> getItems() {
        return items;
    }

    public Golem getGolem() {
        return assignedGolem;
    }

    
}
