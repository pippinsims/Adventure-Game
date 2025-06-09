public class Bananarang implements Item 
{
    String description = "A boomerang made of a potassium rich biotic material.";
    
    @Override
    public void action() 
    {
        Utils.slowPrint("You wail the bananarang at your enemy with KILLER INSTINCT!");
        Utils.slowPrintln(" It kills him.", 200);
    }
}
