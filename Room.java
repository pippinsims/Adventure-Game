public class Room {

    private Room[] doors = new Room[10];
    private Enemy[] enemies = new Enemy[10];
    private Interactible[] interactibles = new Interactible[10];

    Interactible getInteractible(int i)
    {
        return interactibles[i];
    }

    Room getRoom(int i)
    {
        return doors[i];
    }

    Enemy getEnemy(int i)
    {
        return enemies[i];
    }
}