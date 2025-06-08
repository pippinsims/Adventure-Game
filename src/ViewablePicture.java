public class ViewablePicture implements Interactible{
    String txtFileName;
    String description;
    private int wall; //1X is floor, 2X is wall, X1 is south, X2 is west, X3 is north, X4 is east
    
    public ViewablePicture(String fileName, int w)
    {
        description = "depiction";
        txtFileName = fileName;
        wall = w;
    }

    @Override
    public void action() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public String getExposition()
    {
        return getDescription() + " on the " + getWall() + " wall";
    }

    @Override
    public void inspectInteractible()
    {
        InteractionUtil.slowPrintln("You take a closer look at the depiction:\n");
        String s = InteractionUtil.readFile(getFileName());
        System.out.println(s);
        System.out.println("Press enter to continue");
        InteractionUtil.scanner.nextLine();
    }

    private String getWall() 
    {
        switch (wall) {
            case 1: //Why is south 1 this is so weird
                return "south";
            case 2:
                return "west";
            case 3:
                return "north";
            case 4:
                return "east";
            default:
                return "error";
        }    
    }

    public String getFileName()
    {
        return txtFileName;
    }

    @Override
    public boolean isWallInteractible() 
    {
        return true;
    }
}
