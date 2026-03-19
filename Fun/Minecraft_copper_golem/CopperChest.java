import java.util.ArrayList;

public class CopperChest implements IChest {
    private final Golem attachedGolem;
    private ArrayList<Item> items = new ArrayList<Item>();
    public CopperChest() {
        attachedGolem = new Golem(this);
    }

    public Golem getGolem() {
        return attachedGolem;
    }

    public Item getFirstItem() {
        Item firstItem = items.get(0);
        items.remove(0);
        return firstItem;
    }

    @Override
    public void addItem(Item item) {
        items.add(item);
    }

    @Override
    public ArrayList<Item> getItems() {
        return items;
    }
}
