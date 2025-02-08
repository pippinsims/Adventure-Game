public class Torch implements Interactible {
    private boolean lit;
    public String description;
    
    public Torch(boolean isLit)
    {
        description = "flaming stick";
        lit = isLit;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public void action()
    {
        lit = !lit;
    }
}
