package adventuregame.interfaces;

public interface Interactible extends Describable
{
    public void action();
    public String getExposition();
    public void inspectInteractible();
    public boolean isWallInteractible();
}
