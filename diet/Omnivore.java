package diet;

import animals.Animal;
import food.EFoodType;
import food.IEdible;

/**
 * @author baroh
 *
 */
public class Omnivore implements IDiet {

	private IDiet canrivore;
	private IDiet herbivore;

	public Omnivore() {
		this.canrivore = new Carnivore();
		this.herbivore = new Herbivore();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see diet.IDiet#canEat(food.IFood)
	 */
	@Override
	public boolean canEat(IEdible food) {

		return this.canrivore.canEat(food) || this.herbivore.canEat(food);

	}

	public boolean canEat(EFoodType food_type) {
		return food_type != EFoodType.NOTFOOD;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see diet.IDiet#eat(food.IFood)
	 */
	@Override
	public boolean eat(Animal animal, IEdible food) {
		EFoodType ft = food.getFoodtype();

		if (ft == EFoodType.MEAT)
			return this.canrivore.eat(animal, food);
		if (ft == EFoodType.VEGETABLE)
			return this.canrivore.eat(animal, food);
		return false;

	}

	@Override
	public String toString() {
		return "[" + this.getClass().getSimpleName() + "]";
	}
}
