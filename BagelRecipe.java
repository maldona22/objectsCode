class Utils {
    static boolean doubleEquals(double a, double b, double epsilon) {
        return Math.abs(a - b) < epsilon;
    }
}

public class BagelRecipe {
    private double flourWeight = 0.0;
    private double waterWeight = 0.0;
    private double yeastWeight = 0.0;
    private double maltWeight = 0.0;
    private double saltWeight = 0.0;

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
                if (Utils.doubleEquals(saltWeight + yeastWeight, flourWeight / 20.0, 0.001)) {
                    flourWeight = this.flourWeight;
                    waterWeight = this.waterWeight;
                    yeastWeight = this.yeastWeight;
                    maltWeight = this.maltWeight;
                    saltWeight = this.saltWeight;
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
        double tempSaltWeight = (flourWeight / 20.0) + yeastWeight;
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
    BagelRecipe(double flourVolume, double yeastVolume, double saltVolume) {
        
    }
}
