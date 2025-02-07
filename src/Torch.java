public class Torch implements Interactible {
    private boolean lit;
    public String description;
    public Torch(boolean isLit)
    {
        description = "flaming stick";
        lit = isLit;
    }
    public String getDescription()
    {
        return description;
    }
    public void action()
    {
        lit = !lit;
    }
}
