import java.util.HashMap;

public class Golem {
    private CopperChest originChest;
    private HashMap<Item, WoodenChest> knownItems = new HashMap<>();
    private final Integer maxAssignedChest = 9;

    public Golem(CopperChest originChest) {
        this.originChest = originChest;
    }

    public void StartCleaning(WoodenChest[] chests) {
        Item currentItem = originChest.getFirstItem();
        if (currentItem == null) {
            System.out.println("Twidling thumbs");
        }

        WoodenChest homeChest = knownItems.getOrDefault(currentItem, null);
        
        if(homeChest == null) {
            Integer count = 0;
            for (WoodenChest chest : chests) {
                if(chest.getItems().contains(currentItem)) {
                    chest.addItem(currentItem);
                    System.out.println(currentItem.getName() + " has been added to a fitting chest");
                    knownItems.put(currentItem, chest);
                }
                count++;
            }
            if (count > maxAssignedChest) {
                System.out.println(currentItem.getName() + " does not exist in any exist");
                originChest.addItem(currentItem);
                System.out.println(currentItem.getName() + " was added back to copperchest. \n Make sure a relevant item is already in chest");
            }   

        }
    }

    public Integer getMaxAssignedChest() {
        return maxAssignedChest;
    }
}