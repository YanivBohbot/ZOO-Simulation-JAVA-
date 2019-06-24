package graphics;

public interface IAnimalBehavior {
	 abstract public String getName();
	 abstract public void setSuspend();
	 abstract public void setResume();
	 abstract public int getSize();
	 abstract public void eatInc();
	 abstract public int getEatCount();
	 abstract public boolean getChanges();
	 abstract public void setChanges(boolean state);
}

