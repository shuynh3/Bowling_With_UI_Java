package sonny.bowling;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Scoreboard {

    // Java GUI variables (SWING)

    private JPanel mainPanel;
    private JPanel inputPanel;
    private JPanel one_panel;
    private JPanel two_panel;
    private JPanel three_panel;
    private JPanel four_panel;
    private JPanel five_panel;
    private JPanel six_panel;
    private JPanel seven_panel;
    private JPanel eight_panel;
    private JPanel nine_panel;
    private JPanel ten_panel;

    private JLabel one_label;
    private JTextField one_score_text;
    private JTextField one_throw1_text;
    private JTextField one_throw2_text;
    private JLabel two_label;
    private JTextField two_score_text;
    private JTextField two_throw1_text;
    private JTextField two_throw2_text;
    private JLabel three_label;
    private JTextField three_score_label;
    private JTextField three_throw1_text;
    private JTextField three_throw2_text;
    private JLabel four_label;
    private JTextField four_score_text;
    private JTextField four_throw1_text;
    private JTextField four_throw2_text;
    private JLabel five_label;
    private JTextField five_score_text;
    private JTextField five_throw1_text;
    private JTextField five_throw2_text;
    private JLabel six_label;
    private JTextField six_score_text;
    private JTextField six_throw1_text;
    private JTextField six_throw2_text;
    private JLabel seven_label;
    private JTextField seven_score_text;
    private JTextField seven_throw1_text;
    private JTextField seven_throw2_text;
    private JLabel eight_label;
    private JTextField eight_score_text;
    private JTextField eight_throw1_text;
    private JTextField eight_throw2_text;
    private JLabel nine_label;
    private JTextField nine_score_text;
    private JTextField nine_throw1_text;
    private JTextField nine_throw2_text;
    private JLabel ten_label;
    private JTextField ten_score_text;
    private JTextField ten_throw1_text;
    private JTextField ten_throw2_text;
    private JTextField ten_throw3_text;

    private JButton resetButton;

    /* Array of TextField references. 2 TextFields per frame except for the tenth frame which has 3.
       TextField Index : [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 ]
       Frame           : [ 1  1  2  2  3  3  4  4  5  5   6   6   7   7   8   8   9   9  10  10  10 ]
    */
    private JTextField[] inputFields = {one_throw1_text, one_throw2_text, two_throw1_text, two_throw2_text,
            three_throw1_text, three_throw2_text, four_throw1_text, four_throw2_text, five_throw1_text,
            five_throw2_text, six_throw1_text, six_throw2_text, seven_throw1_text, seven_throw2_text,
            eight_throw1_text, eight_throw2_text, nine_throw1_text, nine_throw2_text, ten_throw1_text,
            ten_throw2_text, ten_throw3_text};

    /* Array of score TextField references. 1 TextField per frame.
       TextField Index : [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 ]
    */
    private JTextField[] scoreFields = {one_score_text, two_score_text, three_score_label, four_score_text,
            five_score_text, six_score_text, seven_score_text, eight_score_text, nine_score_text, ten_score_text};

    // String which acts as a character array. Holds all inputted values from GUI.
    private String inputs = "";

    // Int array which holds calculated scores.
    private int[] scores = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    // Constant values
    final int FRAME_COUNT = 10;
    final int LAST_FRAME = FRAME_COUNT - 1;
    final int LAST_FRAME_INDEX = 18;

    final int STRIKE_SPARE_SCORE = 10;

    final char STRIKE_CHAR = 'x';
    final char SPARE_CHAR = '/';
    final char EMPTY_CHAR = ' ';

    public Scoreboard() {
        // Action listener for the reset button
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { resetBoard(); }
        });

        // Document listener for each editable TextField. Listens to realtime text updating.
        DocumentListener realtimeUpdate = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { onTextUpdate(); }
            @Override
            public void removeUpdate(DocumentEvent e) { onTextUpdate(); }
            @Override
            public void changedUpdate(DocumentEvent e) { onTextUpdate(); }
        };

        // Add the document listener to each TextField in [inputFields].
        for (JTextField i : inputFields) { i.getDocument().addDocumentListener(realtimeUpdate); }
    }

    /* Method called when text is updated.
    */
    private void onTextUpdate() {
        grabInputs();
        calculateScores();
        displayScores();
    }

    /* Main method. Sets up Java SWING GUI.
    */
    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new Scoreboard().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /* Fills the global level array, [scores] with the calculated scores from each bowling frame.
    */
    private void calculateScores() {
        int frameScore = 0, startIndex = 0;
        char value1 = '0', value2 = '0', value3 = '0';

        // Calculate scores for frames 0 - 9.
        for (int i = 0; i < FRAME_COUNT; i++) {

            // Start by grabbing total score.
            if (i == 0) { frameScore = 0; }
            else { frameScore = getScore(i - 1); }

            // This is the index of textField1's value in the string.
            startIndex = 2 * i;
            value1 = getInput(startIndex);
            value2 = getInput(startIndex + 1);

            if (i < LAST_FRAME) {       // If current frame is 0-9.
                if (value1 == STRIKE_CHAR) { frameScore += getPoints(value1) + grabNextThrows(i, true); }       // (Strike) Score += Strike(10) + next two shots.
                else if (value2 == SPARE_CHAR) { frameScore += getPoints(value2) + grabNextThrows(i, false); }      // (Digit)(Spare) Score += Spare(10) + next shot.
                else if (isNum(value1) && isNum(value2)) {      // (Digit)(Digit)
                    if (getPoints(value1) + getPoints(value2) >= STRIKE_SPARE_SCORE) { frameScore += STRIKE_SPARE_SCORE + grabNextThrows(i, false); }       // (Digit)(Digit) [Spare] Score += Spare(10) + next shot.
                    else { frameScore += getPoints(value1) + getPoints(value2); }       // (Digit)(Digit) Score += Sum of two shots.
                }
            }
            else {      // If current frame is 10.
                value3 = getInput(startIndex + 2);      // Grab tenth frame's third value.

                frameScore += getPoints(value1) + getPoints(value2);        // Score += first and second value.

                if (value1 == STRIKE_CHAR) { frameScore += getPoints(value3); }     // (Strike)(Strike/Digit)(Strike/Digit) Score += third value.
                else if (value2 == SPARE_CHAR) { frameScore += getPoints(value3) - getPoints(value1); }     // (Digit)(Spare)(Digit/Strike) Score += third value - first value (the spare character returns 10 score)
                else if (getPoints(value1) + getPoints(value2) >= STRIKE_SPARE_SCORE) { frameScore += STRIKE_SPARE_SCORE + getPoints(value3) - getPoints(value1) - getPoints(value2); }     // (Digit)(Digit)(Digit/Strike) [Spare] Score += Spare(10) + third - first - second
            }
            setScore(i, frameScore);
        }
    }

    /* Returns the score of the two next shots given the current frame and the type of shot.
       @params: frame = reference to the current frame.
       @params: strike = boolean flag which determines how method functions.
       @return: Integer value of the scores of the two next shots.
    */
    private int grabNextThrows(int frame, boolean strike) {
        int startIndex, value = 0;;
        char input1, input2, input3;

        // Grab next frame's starting index and first value.
        startIndex = 2 * (frame + 1);
        input1 = getInput(startIndex);

        if (frame < LAST_FRAME - 1) {       // If next frame is 8 or below. (Frames 9 & 10 handle values differently)
            if (strike) {       // If we should grab next two shots [Strike]
                // Grab next frame's 2nd value and next frame's first value. (Needed for strikes)
                input2 = getInput(startIndex + 1);
                input3 = getInput(startIndex + 2);

                if (input1 == STRIKE_CHAR) {        // If-else structure is based on all possible permutations, then simplified.
                    value += getPoints(input1);                                 // (Strike) -> ...              Value += first
                    if (input3 != SPARE_CHAR) { value += getPoints(input3); }   // (Strike) -> (Strike/Digit)   Value += third
                }
                else if (input2 == SPARE_CHAR) { value += getPoints(input2); }  // (Digit) -> (Spare)           Value += second
                else if (isNum(input1)) {                      // (Digit) -> (Digit)           Value += first
                    value += getPoints(input1);
                    if (getPoints(input1) + getPoints(input2) >= STRIKE_SPARE_SCORE) { value += STRIKE_SPARE_SCORE - getPoints(input1); } // (Digit) -> (Digit) [ Spare ]   Value += Spare(10) - first
                    else if (isNum(input2)) { value += getPoints(input2); } // (Digit) -> (Digit) Value += second
                }
            }
            else if (input1 != SPARE_CHAR) { value += getPoints(input1); }      // If we should just grab next shot. [Spare] Value += first
        }
        else {      // If current frame is 9. (10 is handled directly in calculateScores().
            if (strike) {       // If we should grab next two shots [Strike]
                input2 = getInput(startIndex + 1);

                if (input1 == STRIKE_CHAR) {                                    // (Strike) -> ...              Value += first
                    value += getPoints(input1);
                    if (input2 != SPARE_CHAR) { value += getPoints(input2); }   // (Strike) -> (Digit / Strike) Value += second
                }
                else if (isNum(input1)) {                                       // (Digit) -> ...               Value += first
                    value += getPoints(input1);
                    if (input2 == SPARE_CHAR) { value += getPoints(input2) - getPoints(input1); } // (Digit) -> (Spare)     Value += second(10) - first
                    else if (isNum(input2)) {                                   // (Digit) -> (Digit)           Value += second
                        value += getPoints(input2);
                        if (getPoints(input1) + getPoints(input2) >= STRIKE_SPARE_SCORE) { value += STRIKE_SPARE_SCORE - getPoints(input1) - getPoints(input2); }   // (Digit) -> (Digit) [Spare] Value += Spare(10) - first - second
                    }
                }
            }
            else if (input1 != SPARE_CHAR) { value += getPoints(input1); }      // If we should just grab next shot. [Spare] Value += first
        }
        return value;
    }

    /* Iterates through all score-holding TextFields and replaces the text with values in the global level array, [scores]
    */
    private void displayScores() {
        resetScores();
        for (int i = 0; i < checkFrames(); i++) {
            scoreFields[i].setText(Integer.toString(scores[i]));
        }
    }

    /* Returns how many frames are completed and have correct formatting. Iterates through each frame
       and checks whether the inputs follow correct formatting using an if-else decision tree.
       @return: integer value for how many round are syntactically correct.
    */
    private int checkFrames() {
        int rounds = 0, startIndex = 0;
        char v1, v2, v3;

        // Iterate through each frame.
        for (int i = 0; i < FRAME_COUNT; i++) {
            startIndex = i * 2;

            // Grab each frame's values.
            v1 = getInput(startIndex + 0);
            v2 = getInput(startIndex + 1);
            v3 = getInput(startIndex + 2);

            /* For frames 1-9, check for the following bad conditions.
               - Box1 contains either a spare or no characters.
               - Box1 does not contain a strike, however Box2 contains either a strike or no characters.
            */
            if (v1 == EMPTY_CHAR || v1 == SPARE_CHAR || (v1 != STRIKE_CHAR && (v2 == EMPTY_CHAR || v2 == STRIKE_CHAR))) { break; }

            /* For frame 10, check for the following bad conditions.
               - Box1 contains a strike
                    - Box2 contains a strike, Box3 is either a spare or no character.
                    - Box2 contains a digit, Box3 is a strike or empty.
                    - Box2 contains a spare or is empty or Box3 is empty.
               - Box1 contains a digit and Box2 is a strike.
            */
            if (i == LAST_FRAME) {
                if (v1 == STRIKE_CHAR) {
                    if (v2 == STRIKE_CHAR && (v3 == SPARE_CHAR || v3 == EMPTY_CHAR)) { break; }
                    else if (isNum(v2) && (v3 == STRIKE_CHAR || v3 == EMPTY_CHAR)) { break; }
                    else if (v2 == SPARE_CHAR || v2 == EMPTY_CHAR || v3 == EMPTY_CHAR) { break; }
                }
                else if (isNum(v1) && v2 == STRIKE_CHAR) { break; }
            }

            // Increments rounds up the frame that the for loop is broken out of.
            rounds++;
        }
        return rounds;
    }

    /* Grabs and stores all inputs in the global string variable, [inputs].
    */
    private void grabInputs() {
        inputs = "";
        // Iterate through each frame. (Frames contain two TextFields, tenth has three)
        for (int i = 0; i <= LAST_FRAME_INDEX; i += 2) {
            // Concatenate the characters to the global string.
            this.inputs = inputs.concat(Character.toString(getFirstChar(inputFields[i])));
            this.inputs = inputs.concat(Character.toString(getFirstChar(inputFields[i + 1])));
            if (i == LAST_FRAME_INDEX) { this.inputs = inputs.concat(Character.toString(getFirstChar(inputFields[i + 2]))); }
        }
    }

    /* Grabs, converts to lowercase, and returns first character in a JTextField.
       @params: field = JTextField which contains shot result.
       @return: character value in the set, [ ' ', 'x', '/', '0-9' ]
    */
    private char getFirstChar(JTextField field) {
        char firstChar;

        // If TextField is left empty, denote character as a space.
        if (field.getText().equals("")) {
            firstChar = EMPTY_CHAR;
        }
        // Else grab and process the character.
        else {
            firstChar = field.getText().toLowerCase().charAt(0);
            // If character is not either a 'x', '/', '0-9', denote as '0'.
            if (firstChar != STRIKE_CHAR && firstChar != SPARE_CHAR && !isNum(firstChar)) { firstChar = '0'; }
        }
        return firstChar;
    }

    /* Returns a score value given a character.
       @params: input = character.
       @return: integer value <= 10
    */
    private int getPoints(char input) {
        switch (input) {
            case STRIKE_CHAR:
            case SPARE_CHAR:
                return STRIKE_SPARE_SCORE;
            case EMPTY_CHAR:
                return 0;
            default:
                return charToInt(input);
        }
    }

    /* Calls resetInputs() and resetScores() to erase entire scoreboard.
     */
    private void resetBoard() {
        resetInputs();
        resetScores();
    }

    /* Iterates through all input-holding TextFields, and erases their content.
     */
    private void resetInputs() {
        for (int i = 0; i < inputFields.length; i++) {
            inputFields[i].setText("");
        }
    }

    /* Iterates through all score-holding TextFields, and erases their content.
    */
    private void resetScores() {
        for (int i = 0; i < scoreFields.length; i++) {
            scoreFields[i].setText("");
        }
    }

    /* Converts a character digit, to an int.
       @params: input = character in the form of a digit.
       @return: Integer value.
    */
    private int charToInt(char input) {
        return input - '0';
    }

    /* Checks whether the character is a digit between 0 and 9.
       @params: input = character.
       @return: boolean value (true if character is a digit, else false).
    */
    private boolean isNum(char input) {
        return input >= '0' && input <= '9';
    }

    /* Mutates specified index in the global level variable, [scores]. Mutator method to encapsulate data.
       @params: frame = index to set in [scores]
       @params: value = value to set in [scores]
    */
    private void setScore(int frame, int value) {
        this.scores[frame] = value;
    }

    /* Grabs specified index from the global level variable, [scores]. Accessor method to encapsulate data.
       @params: frame = index to grab from [scores]
       @return: int value from [scores]
    */
    private int getScore(int frame) {
        return this.scores[frame];
    }

    /* Grabs specified index from the global level variable, [inputs].Accessor method to encapsulate data.
       @params: index = index to grab from input
       @return: character value from [inputs]
    */
    private char getInput(int index) {
        return inputs.charAt(index);
    }
}
