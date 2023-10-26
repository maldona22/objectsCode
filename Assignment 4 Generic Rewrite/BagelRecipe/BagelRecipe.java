class UtilsBagel {
    static boolean doubleEquals(double a, double b, double epsilon) {
        return Math.abs(a - b) < epsilon;
    }
}

public class BagelRecipe {
    private double flourWeight;
    private double waterWeight;
    private double yeastWeight;
    private double maltWeight;
    private double saltWeight;

    void setFlourWeight(double weight) {
        flourWeight = weight;
    }

    double getFlourWeight() {
        return flourWeight;
    }

    void setWaterWeight(double weight) {
        waterWeight = weight;
    }

    double getWaterWeight() {
        return waterWeight;
    }

    void setYeastWeight(double weight) {
        yeastWeight = weight;
    }

    double getYeastWeight() {
        return yeastWeight;
    }

    void setMaltWeight(double weight) {
        maltWeight = weight;
    }

    double getMaltWeight() {
        return maltWeight;
    }

    void setSaltWeight(double weight) {
        saltWeight = weight;
    }

    double getSaltWeight() {
        return saltWeight;
    }

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
                    // TODO come up with better errors
                    throw new IllegalArgumentException("Invalid salt, yeast weight: Salt - " + saltWeight + " Yeast - " + yeastWeight);
                }
            } else {
                // TODO come up with better errors
                throw new IllegalArgumentException();
            }
        }
        else {
            // TODO come up with better errors
            throw new IllegalArgumentException();
        }
    }
    
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

    double convertFlourVolumeToWeight(double cups) {
        return cups * 4.25;
    }

    double convertWaterVolumeToWeight(double cups) {
        return cups * 8;
    }

    double convertYeastVolumeToWeight(double teaspoons) {
        return (teaspoons / 48.0) * 5.0;
    }

    double convertSaltVolumeToWeight(double teaspoons) {
        return (teaspoons / 48.0) * 10.0;
    }

    double convertMaltVolumeToWeight(double teaspoons) {
        return (teaspoons / 48.0) * 11.0;
    }

    // TODO figure out if this is returning volumes or weights???
    // fuck it I'll just assume weights for now
    BagelRecipe(double flourVolume, double yeastVolume, double saltVolume) {
        double tempFlourWeight = convertFlourVolumeToWeight(flourVolume);
        double tempYeastWeight = convertYeastVolumeToWeight(yeastVolume);
        double tempSaltWeight = convertSaltVolumeToWeight(saltVolume);

        if (UtilsBagel.doubleEquals(tempSaltWeight, (tempFlourWeight / 20.0) - tempYeastWeight, 0.001)) {
            this.flourWeight = tempFlourWeight;
            this.waterWeight = tempFlourWeight;
            this.yeastWeight = tempYeastWeight;
            this.maltWeight = tempYeastWeight;
            this.saltWeight = tempSaltWeight;
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    boolean equals(BagelRecipe other) {
        return UtilsBagel.doubleEquals(this.saltWeight, other.saltWeight, 0.001) &&
                UtilsBagel.doubleEquals(this.flourWeight, other.flourWeight, 0.001) &&
                UtilsBagel.doubleEquals(this.maltWeight, other.maltWeight, 0.001) &&
                UtilsBagel.doubleEquals(this.waterWeight, other.waterWeight, 0.001) &&
                UtilsBagel.doubleEquals(this.yeastWeight, other.yeastWeight, 0.001);
    }

    void printRecipe() {
        System.out.println("Flour weight: " + this.flourWeight);
        System.out.println("Water weight: " + this.waterWeight);
        System.out.println("Yeast weight: " + this.yeastWeight);
        System.out.println("Malt weight: " + this.maltWeight);
        System.out.println("Salt weight: " + this.saltWeight);
    }

    public static void main(String[] args) {
        BagelRecipe test1 = new BagelRecipe(20, 20, 0.5, 0.5, 0.5);
        System.out.println("Works fine?");
        try {
            BagelRecipe test2 = new BagelRecipe(21, 20, 0.5, 0.5, 0.5);
        } catch (Exception IllegalArgumentException) {
            System.out.println("Still working 1");
        }

        BagelRecipe test3 = new BagelRecipe(20, 0.5);

        System.out.println("test1 recipe");
        test1.printRecipe();
        System.out.println("test3 recipe");
        test3.printRecipe();

        if (test3.equals(test1)) {
            System.out.println("Still working 2");
        }

        try {
            BagelRecipe test4 = new BagelRecipe(20, 1, 1);
        } catch (Exception IllegalArgumentException) {
            System.out.println("Nothing wrong yet");
        }
        
    }
}
