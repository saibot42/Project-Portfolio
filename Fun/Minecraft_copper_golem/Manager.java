import java.util.ArrayList;

public class Manager {
    private ArrayList<Golem> golems = new ArrayList<Golem>();
    
    public Manager() {
        //Setup
        Item testItem = new Item("Iron");
        CopperChest testCopperChest = new CopperChest();
        testCopperChest.addItem(testItem);
        Golem testGolem = testCopperChest.getGolem();
        golems.add(testGolem);
        WoodenChest[] chests = new WoodenChest[testGolem.getMaxAssignedChest()];
        for (int i = 0; i < chests.length; i++) {
            chests[i] = new WoodenChest(testGolem);
        }
        chests[3].addItem(testItem);

        //Test
        testGolem.StartCleaning(chests);

    }
}
