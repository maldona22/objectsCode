import tester.*;
/*
A BagelRecipe can be represented as the amount of flour, water, yeast, salt, and
malt in the recipe. A perfect bagel results when the ratios of the weights are right:
    1. the weight of the flour should be equal to the weight of the water
    2. the weight of the yeast should be equal the weight of the malt
    3. the weight of the salt + yeast should be 1/20th the weight of the flour

Design the BagelRecipe class. The fields should be of type double and represent the
weight of the ingredients as ounces. Provide three constructors for this class:
    1. Your main constructor should take in all the fields and enforce all above
    constraints to ensure a perfect bagel recipe.
    2. Provide another constructor that only requires the weights of flour and yeast, and
    produces a perfect bagel recipe.
    3. Provide another constructor that takes in the flour, yeast and salt as volumes rather
    than weights, and tries to produce a perfect recipe. Here, too, a perfect recipe should
    be enforced.

Flour and water volumes are measured in cups, while yeast, salt, and malt volumes
are measured in teaspoons.

Helpful conversion factors:
    48 teaspoons = 1 cup
    1 cup of yeast = 5 ounces
    1 cup of salt = 10 ounces
    1 cup of malt = 11 ounces
    1 cup of water = 8 ounces
    1 cup of flour = 4 and 1⁄4 ounces

Once you’ve completed the above constructors, you should:
Remove as much duplicate code as possible from these constructors.

Implement the method sameRecipe(BagelRecipe other) which returns true if the same
ingredients have the same weights to within 0.001 ounces.
 */
class Utils {
    /* Template:
     * Fields:
     * n/a
     * Methods:
     * this.sameRecipeHelp(double i1, double 12, double difference) -- boolean
     * this.cupToOunces(double volume, double toOunces) -- double
     * this.tspToCup(double tsp) -- double
     * Methods of fields:
     * n/a
     */
    boolean sameRecipeHelp(double i1, double i2, double difference) {
        double dif = Math.abs(i1 - i2);
        return (dif < difference);
    }
    double cupToOunce(double volume, double toOunces) {
        return (volume * toOunces);
    }
    double tspToCup(double tsp) {
        return (tsp / 48.0);
    }

}


//A bagel recipe represents the amount of flour, water, yeast, salt, and malt in the recipe
public class BagelRecipesAbby {
    /* Template:
     * Fields:
     * this.weightFlour -- double
     * this.weightWater -- double
     * this.weightYeast -- double
     * this.weightSalt -- double
     * this.weightMalt -- double
     * Methods:
     * this.sameRecipe(BagelRecipe other) -- boolean
     * Methods of fields:
     * n/a
     */
    double weightFlour; //weight of flour in bagel in ounces
    double weightWater; //weight of water in bagel in ounces
    double weightYeast; //weight of yeast in bagel in ounces
    double weightSalt; //weight of salt in bagel in ounces
    double weightMalt; //weight of malt in bagel in ounces

    //Main Constructor
    BagelRecipesAbby(double weightFlour, double weightWater, double weightYeast, double weightSalt, double weightMalt) {
        if ((weightFlour == weightWater) && (weightYeast == weightMalt) && (((weightSalt + weightYeast) * 20.0) == weightFlour)) {
            this.weightFlour = weightFlour;
            this.weightWater = weightWater;
            this.weightYeast = weightYeast;
            this.weightSalt = weightSalt;
            this.weightMalt = weightMalt;
        } else {
            throw new IllegalArgumentException("Imperfect Bagel Recipe");
        }
    }

    //Constructor with just Flour and Yeast
    BagelRecipesAbby(double weightFlour, double weightYeast) {
        this.weightFlour = weightFlour;
        this.weightWater = weightFlour;
        this.weightYeast = weightYeast;
        this.weightSalt = ((weightFlour/20) - weightYeast);
        this.weightMalt = weightYeast;
    }
    //Constructor given Volume(in Cups) of weightFlour, weightYeast, and weightSalt
    BagelRecipesAbby(double weightFlour, double weightYeast, double weightSalt) {
        double cupsToOunceFlour = 4.25;
        double cupsToOunceWater = 8.0;
        double cupsToOunceYeast = 5.0;
        double cupsToOunceSalt = 10.0;
        double cupsToOunceMalt = 11.0;

        this.weightFlour = new Utils().cupToOunce(weightFlour, cupsToOunceFlour);
        this.weightWater = new Utils().cupToOunce(weightFlour,  cupsToOunceWater);
        this.weightYeast = new Utils().cupToOunce(new Utils().tspToCup(weightYeast), cupsToOunceYeast);
        this.weightSalt = new Utils().cupToOunce(new Utils().tspToCup(weightSalt), cupsToOunceSalt);
        this.weightMalt = new Utils().cupToOunce(new Utils().tspToCup(weightYeast), cupsToOunceMalt);
    }
    boolean sameRecipe(BagelRecipesAbby other) {
        double dif = 0.001; //max difference between this fields and other fields
        return  new Utils().sameRecipeHelp(this.weightFlour, other.weightFlour, dif) &&
                new Utils().sameRecipeHelp(this.weightWater, other.weightWater, dif) &&
                new Utils().sameRecipeHelp(this.weightYeast, other.weightYeast, dif) &&
                new Utils().sameRecipeHelp(this.weightSalt, other.weightSalt, dif) &&
                new Utils().sameRecipeHelp(this.weightMalt, other.weightMalt, dif);
    }
}


class ExamplesBagelRecipesAbby {
    BagelRecipesAbby bagel1 = new BagelRecipesAbby(10, 10, 0.25, 0.25, 0.25);
    BagelRecipesAbby bagel2 = new BagelRecipesAbby(10, 0.25);
    BagelRecipesAbby bagel3 = new BagelRecipesAbby(2.35,22, 13);
    BagelRecipesAbby bagel4 = new BagelRecipesAbby(25, 30, 45, 10, 4);

    boolean testSameRecipe (Tester t) {
        return  t.checkExpect(bagel2.sameRecipe(bagel1), true)&&
                t.checkExpect(bagel2.sameRecipe(bagel3), false);
    }
    boolean testImperfectBagelRecipe (Tester t) {
        return t.checkConstructorException(new IllegalArgumentException("Imperfect Bagel Recipe"), "BagelRecipesAbby", bagel4);
    }
}