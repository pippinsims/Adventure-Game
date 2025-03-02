//FOR PLAYER, FOLLOWERS, NPCS, AND ENEMIES

public interface Animate 
{
    public boolean performAction(int i);
    public void setActions(Room curRoom);

    public boolean receiveDamage(int damage);
    public int getHealth();
    public Inventory getInventory();
    public int getAttackDamage();
    public int getWisdom();
}
