import tester.Tester;

// Utility functions
class UtilsBagel {
    /* Template
     * 
     * Fields:
     * 
     * Methods:
     * UtilsBagel.doubleEquals(double a, double b, double epsilon) -- boolean
     * 
     * Methods of fields:
     * 
     */
    static boolean doubleEquals(double a, double b, double epsilon) {
        return Math.abs(a - b) < epsilon;
    }
}

// Creates a perfect recipe to bake a bagel with
public class BagelRecipe {
    /* Template:
     *
     * Fields:
     * this.flourWeight -- double
     * this.waterWeight -- double
     * this.yeastWeight -- double
     * this.maltWeight -- double
     * this.saltWeight -- double
     * 
     * Methods:
     * this.convertFlourVolumeToWeight(double cups) -- double
     * this.convertWaterVolumeToWeight(double cups) -- double
     * this.convertYeastVolumeToWeight(double teaspoons) -- double
     * this.convertSaltVolumeToWeight(double teaspoons) -- double
     * this.convertMaltVolumeToWeight(double teaspoons) --double
     * this.sameRecipe(BagelRecipe other) -- boolean
     * 
     * Methods of fields:
     *  
    */
    double flourWeight;
    double waterWeight;
    double yeastWeight;
    double maltWeight;
    double saltWeight;

    // Constructor given the weight of every ingredient within the BagelRecipe
    BagelRecipe(double flourWeight, double waterWeight, double yeastWeight, double maltWeight, double saltWeight) {
        if (flourWeight == waterWeight) {
            if (yeastWeight == maltWeight) {
                if (UtilsBagel.doubleEquals(saltWeight + yeastWeight, flourWeight / 20.0, 0.001)) {
                    this.flourWeight = flourWeight;
                    this.waterWeight = waterWeight;
                    this.yeastWeight = yeastWeight;
                    this.maltWeight = maltWeight;
                    this.saltWeight = saltWeight;
                } else {
                    throw new IllegalArgumentException("Imperfect Bagel Recipe");
                }
            } else {
                throw new IllegalArgumentException("Imperfect Bagel Recipe");
            }
        } else {
            throw new IllegalArgumentException("Imperfect Bagel Recipe");
        }
    }

    // Constructor given only the weight of flour and the weight of yeast
    BagelRecipe(double flourWeight, double yeastWeight) {
        double tempWaterWeight = flourWeight;
        double tempMaltWeight = yeastWeight;
        double tempSaltWeight = (flourWeight / 20.0) - yeastWeight;
        this.flourWeight = flourWeight;
        this.waterWeight = tempWaterWeight;
        this.yeastWeight = yeastWeight;
        this.maltWeight = tempMaltWeight;
        this.saltWeight = tempSaltWeight;
    }

    // Converts a volume of flour in cups to its weight in ounces
    double convertFlourVolumeToWeight(double cups) {
        return cups * 4.25;
    }

    // Converts a volume of water in cups to its weight in ounces
    double convertWaterVolumeToWeight(double cups) {
        return cups * 8;
    }

    // Converts a volume of yeast in teaspoons to its weight in ounces
    double convertYeastVolumeToWeight(double teaspoons) {
        return (teaspoons / 48.0) * 5.0;
    }

    double convertSaltVolumeToWeight(double teaspoons) {
        return (teaspoons / 48.0) * 10.0;
    }

    // Converts a volume of malt in teaspoons to its weight in ounces
    double convertMaltVolumeToWeight(double teaspoons) {
        return (teaspoons / 48.0) * 11.0;
    }

    // Constructor given volume of flour in cups, and yeast and salt in teaspoons
    BagelRecipe(double flourVolume, double yeastVolume, double saltVolume) {
        // Conversions to weight from volume:
        double tempFlourWeight = convertFlourVolumeToWeight(flourVolume);
        double tempYeastWeight = convertYeastVolumeToWeight(yeastVolume);
        double tempSaltWeight = convertSaltVolumeToWeight(saltVolume);

        // Verify that this is a valid BagelRecipe
        if (UtilsBagel.doubleEquals(tempSaltWeight, (tempFlourWeight / 20.0) - tempYeastWeight, 0.001)) {
            this.flourWeight = tempFlourWeight;
            this.waterWeight = tempFlourWeight;
            this.yeastWeight = tempYeastWeight;
            this.maltWeight = tempYeastWeight;
            this.saltWeight = tempSaltWeight;
        } else {
            throw new IllegalArgumentException("Imperfect Bagel Recipe");
        }
    }

    // Computes if the other BagelRecipes is the same as this BagelRecipe within a plus minus of 0.001
    boolean sameRecipe(BagelRecipe other) {
        return UtilsBagel.doubleEquals(this.saltWeight, other.saltWeight, 0.001) &&
                UtilsBagel.doubleEquals(this.flourWeight, other.flourWeight, 0.001) &&
                UtilsBagel.doubleEquals(this.maltWeight, other.maltWeight, 0.001) &&
                UtilsBagel.doubleEquals(this.waterWeight, other.waterWeight, 0.001) &&
                UtilsBagel.doubleEquals(this.yeastWeight, other.yeastWeight, 0.001);
    }

}

class ExamplesBagelRecipe {
    BagelRecipe bagel1 = new BagelRecipe(10, 10, 0.25, 0.25, 0.25);
    BagelRecipe bagel2 = new BagelRecipe(10, 0.25);
    BagelRecipe bagel3 = new BagelRecipe(20, 0.5);

    boolean testSameRecipe(Tester t) {
        return t.checkExpect(bagel2.sameRecipe(bagel1), true) &&
                t.checkExpect(bagel1.sameRecipe(bagel3), false);
    }

    boolean testImperfectBagelRecipe(Tester t) {
        return t.checkConstructorException(new IllegalArgumentException("Imperfect Bagel Recipe"), "BagelRecipe",
                new BagelRecipe(10, 20, 30)) &&
                t.checkConstructorException(new IllegalArgumentException("Imperfect Bagel Recipe"), "BagelRecipe",
                        new BagelRecipe(0.5, 10, 0.5, 10, 0.5))
                &&
                t.checkConstructorException(new IllegalArgumentException("Imperfect Bagel Recipe"), "BagelRecipe",
                        new BagelRecipe(30, 0.23));
    }

    boolean testDoubleEquals(Tester t) {
        return t.checkExpect(UtilsBagel.doubleEquals(1.0001, 1.0, 0.001), true);
    }

    boolean testConvertFlourVolumeToWeight(Tester t) {
        return t.checkInexact(bagel1.convertFlourVolumeToWeight(2), 8.25, 0.001);
    }

    boolean testConvertWaterVolumeToWeight(Tester t) {
        return t.checkInexact(bagel1.convertWaterVolumeToWeight(2), 16.0, 0.001);
    }

    boolean testConvertYeastVolumeToWeight(Tester t) {
        return t.checkInexact(bagel1.convertYeastVolumeToWeight(2), 0.208333, 0.001);
    }

    boolean testConvertSaltVolumeToWeight(Tester t) {
        return t.checkInexact(bagel1.convertSaltVolumeToWeight(2), 0.416667, 0.001);
    }

    boolean testConvertMaltVolumeToWeight(Tester t) {
        return t.checkInexact(bagel1.convertMaltVolumeToWeight(2), 0.458333, 0.001);
    }
}
