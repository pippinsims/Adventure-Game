public class ViewablePicture implements Interactible{
    String txtFileName;
    String description;
    
    public ViewablePicture(String fileName)
    {
        description = "depiction";
        txtFileName = fileName;
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

    public String getFileName()
    {
        return txtFileName;
    }
}
