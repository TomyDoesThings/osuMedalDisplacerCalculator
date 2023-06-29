/***
 * Tomy's osu! Medal Displacer Calculator
 * Made for Java 8 and above.
 * No exception handling at the moment, besides, I have yet to learn how to make X on top right of JOptionPane window close program.
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JOptionPane;

public class Main
{
    public static void main(String[] args)
    {
        // Introduction to the Program
        JOptionPane.showMessageDialog(null, "Hello there! Welcome to osu! Medal Displacer Calculator made by Tomy.\n");
        JOptionPane.showMessageDialog(null, "This program allows you to figure out how many new doable medals need to be released in a row (without any impossible medals in the batches) to displace a certain amount of unobtained medals.");
        JOptionPane.showMessageDialog(null, "Medals should always be typed as integers, and medal percent should always be typed as integer or decimal;\notherwise, this program will crash.");

        // Guaranteed Variable of Decimal Precision Initialization
        short bigDecimalPrecision = Short.parseShort(JOptionPane.showInputDialog(null, "How many decimal point numbers of precision do you want (lowest being 0 and max being 32,767; 100 is what Tomy uses)?\nThis must coincide with the amount of decimal point numbers the medal percent you request is."));

        BigDecimal multiplicandToConvertBetweenDecimalAndPercent = new BigDecimal(100); // 10^2
        BigDecimal wantedMedalPercentInDecimal; // This variable changes its usage depending on the mode chosen
        BigDecimal requiredDoableMedalsInARowAdditions; // The end result

        // Absolute Medal Information Variable Initialization From User
        BigDecimal existingMedalCount = new BigDecimal(JOptionPane.showInputDialog(null, "Please enter how many medals exist in osu! right now:"));
        BigDecimal obtainedMedalCount = new BigDecimal(JOptionPane.showInputDialog(null, "Please enter how many medals have you obtained:"));

        // Method Chooser
        Object[] modeOptions = {"Percent", "Medal Literal"};
        int modeOfChoice = JOptionPane.showOptionDialog(null, "Please choose your method.",  "Choice", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, modeOptions, modeOptions[0]);

        // Corresponding Method's Value Initialization
        if (modeOfChoice == JOptionPane.YES_OPTION) // Percent Method - wantedMedalPercentInDecimal is initialized to wanted medal %
        {
            wantedMedalPercentInDecimal = new BigDecimal(JOptionPane.showInputDialog(null, "Please enter the medal percent (between current medal percent and 100%) you want to reach:"));
            wantedMedalPercentInDecimal = wantedMedalPercentInDecimal.divide(multiplicandToConvertBetweenDecimalAndPercent, bigDecimalPrecision, RoundingMode.UNNECESSARY); // Converts percent to decimal

            // Calculate
            requiredDoableMedalsInARowAdditions = calculateRequiredDoableMedalsInARowAdditions(obtainedMedalCount, existingMedalCount, wantedMedalPercentInDecimal);

            // Results Shown To User
            JOptionPane.showMessageDialog(null, "With you having " + obtainedMedalCount + " out of " + existingMedalCount + " medal(s) and wanting " + wantedMedalPercentInDecimal.multiply(multiplicandToConvertBetweenDecimalAndPercent) + "%,\nyou need " + requiredDoableMedalsInARowAdditions + " doable medals in a row to be added.\n\nDecimal Point Numbers of Precision (if humungous, consider Alt + F4): " + bigDecimalPrecision + "\nI hope this program was of use to you.");
        }
        else if (modeOfChoice == JOptionPane.NO_OPTION) // Medal Literal Method - wantedMedalPercentInDecimal is initialized to wished medals count in order to save wanted medal %
        {
            wantedMedalPercentInDecimal = new BigDecimal(JOptionPane.showInputDialog(null, "You have " + obtainedMedalCount + " medals.\nEnter the amount of medals you wish you had out of the " + existingMedalCount + " currently existing medals:"));
            wantedMedalPercentInDecimal = wantedMedalPercentInDecimal.divide(existingMedalCount, bigDecimalPrecision, RoundingMode.HALF_UP);

            // Calculate
            requiredDoableMedalsInARowAdditions = calculateRequiredDoableMedalsInARowAdditions(obtainedMedalCount, existingMedalCount, wantedMedalPercentInDecimal);

            // Results Shown To User
            JOptionPane.showMessageDialog(null, "With you having " + obtainedMedalCount + " out of " + existingMedalCount + " medal(s) meaning you wish to have " + wantedMedalPercentInDecimal.multiply(multiplicandToConvertBetweenDecimalAndPercent) + "%,\nyou need " + requiredDoableMedalsInARowAdditions + " doable medals in a row to be added.\n\nDecimal Point Numbers of Precision (if humungous, consider Alt + F4): " + bigDecimalPrecision + "\nI hope this program was of use to you.");
        }
    }

    // obtainedMedalCount is integer between 0 and existingMedalCount.
    // existingMedalCount is amount of medals that exist.
    // extraWantedDisplacedMedalValue is wanted medal percent in decimal form.
    private static BigDecimal calculateRequiredDoableMedalsInARowAdditions(BigDecimal obtainedMedalCount, BigDecimal existingMedalCount, BigDecimal extraWantedDisplacedMedalValue)
    {
        BigDecimal requiredDoableMedalsInARowAdditions;

        /* Algebra Time
         * -----------------------------------------------------------------------------------------------------------------------------------------------
         * Solve for requiredDoableMedalsInARowAdditions:
         * (obtainedMedalCount + requiredDoableMedalsInARowAdditions) / (existingMedalCount + requiredDoableMedalsInARowAdditions) = wantedMedalPercentage
         * therefore
         * requiredDoableMedalsInARowAdditions = (wantedMedalPercentage * existingMedalCount - obtainedMedalCount) / (1 - wantedMedalPercentage)
         */

        // Numerator Being Solved
        requiredDoableMedalsInARowAdditions = extraWantedDisplacedMedalValue.multiply(existingMedalCount);
        requiredDoableMedalsInARowAdditions = requiredDoableMedalsInARowAdditions.subtract(obtainedMedalCount);

        // Denominator Being Solved
        BigDecimal denominator = BigDecimal.ONE.subtract(extraWantedDisplacedMedalValue);

        // Denominator Being Used
        requiredDoableMedalsInARowAdditions = requiredDoableMedalsInARowAdditions.divide(denominator, 0, RoundingMode.UP); // Fortunately there's rounding only on this final step

        // Required Doable Medals In A Row Additions Being Returned
        return requiredDoableMedalsInARowAdditions;
    }
}
