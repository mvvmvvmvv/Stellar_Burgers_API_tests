package org.example;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String ingredient1;
    private String ingredient2;

    public Order(String ingredient1, String ingredient2) {
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
    }

    public Order() {
    }

    public String getIngredient1() {
        return ingredient1;
    }

    public void setIngredient1(String ingredient1) {
        this.ingredient1 = ingredient1;
    }

    public String getIngredient2() {
        return ingredient2;
    }

    public void setIngredient2(String ingredient2) {
        this.ingredient2 = ingredient2;
    }

    public List<String> getIngredients(String ingredient1, String ingredient2) {
        List<String> ingredients = new ArrayList<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        return ingredients;
    }
}
