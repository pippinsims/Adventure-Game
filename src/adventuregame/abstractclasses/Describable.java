package adventuregame.abstractclasses;

public abstract class Describable
{
    public abstract String getPluralDescription();
    public abstract String getDescription();
    public abstract  String getName();

    @Override
    public int hashCode() 
    {
        return getDescription().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj) return true;

        if(obj == null || getClass() != obj.getClass()) return false;

        Describable d = (Describable) obj;
        return this.getDescription().equals(d.getDescription());
    }
}
