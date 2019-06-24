package diet;

import animals.Animal;
import food.EFoodType;
import food.IEdible;

public class Herbivore implements IDiet {

	@Override
	public boolean canEat(IEdible food) {
		return (food.getFoodtype() == EFoodType.VEGETABLE);
	}

	public boolean canEat(EFoodType food_type) {
		return food_type == EFoodType.VEGETABLE;
	}
	
	@Override
	public boolean eat(Animal animal, IEdible food) {
		boolean isSuccess = canEat(food);
		if (isSuccess) {
			animal.setWeight(animal.getWeight() * 1.07);
		}
		return isSuccess;
	}

	@Override
	public String toString() {
		return "[" + this.getClass().getSimpleName() + "]";
	}
}
