package adventuregame.interfaces;

import adventuregame.Room;

public interface Interactible extends Describable
{
    public void action(Unit u);
    public Room getRoom();
    public String getActionDescription();
    public String getExposition();
    public void inspectInteractible();
    public boolean isWallInteractible();
    public boolean isDoor();
}
