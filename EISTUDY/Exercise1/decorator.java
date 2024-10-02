public class decorator {
    // Pizza.java
interface Pizza {
    String getDescription();
    double getCost();
}

// BasicPizza.java
class BasicPizza implements Pizza {
    @Override
    public String getDescription() {
        return "Basic pizza";
    }

    @Override
    public double getCost() {
        return 50.0;
    }
}

// PizzaDecorator.java
abstract class PizzaDecorator implements Pizza {
    protected Pizza pizza;

    public PizzaDecorator(Pizza pizza) {
        this.pizza = pizza;
    }

    @Override
    public String getDescription() {
        return pizza.getDescription();
    }

    @Override
    public double getCost() {
        return pizza.getCost();
    }
}

// CheeseDecorator.java
class CheeseDecorator extends PizzaDecorator {
    public CheeseDecorator(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Cheese";
    }

    @Override
    public double getCost() {
        return pizza.getCost() + 20.0;
    }
}

// Main.java
public class Main {
    public static void main(String[] args) {
        Pizza pizza = new BasicPizza();
        pizza = new CheeseDecorator(pizza);

        System.out.println(pizza.getDescription() + " | Cost: " + pizza.getCost());
    }
}

}
