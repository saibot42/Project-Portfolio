package Object;

public interface IObject {

/**
 * Represents the interaction behavior of an object in the game.
 * <p>
 * Every class implementing this interface must define what happens 
 * when the object is interacted with (e.g., touched by the player).
 * This could involve actions like redrawing the object, removing 
 * collisions, playing animations, triggering events, or any 
 * other game-specific behavior.
 * </p>
 * 
 * <p>Examples of use:</p>
 * <ul>
 *   <li>A door object might toggle between open and closed states.</li>
 *   <li>A collectible item might disappear from the game world upon interaction.</li>
 *   <li>An enemy might change its behavior or trigger an attack.</li>
 * </ul>
 * 
 * <p>Note: This method is intended to be called whenever the object 
 * is "touched" or otherwise interacted with by the player or another 
 * game entity.</p>
 */
void objectInteraction();

}
