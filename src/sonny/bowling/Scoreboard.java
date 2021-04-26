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
    private JTextField three_score_text;
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
    private JTextField[] scoreFields = {one_score_text, two_score_text, three_score_text, four_score_text,
            five_score_text, six_score_text, seven_score_text, eight_score_text, nine_score_text, ten_score_text};

    // Constant values
    final int FRAME_COUNT = 10, LAST_FRAME = FRAME_COUNT - 1, LAST_FRAME_INDEX = LAST_FRAME * 2;
    final int STRIKE_SPARE_SCORE = 10;

    // Non-digit character representation.
    final char STRIKE_CHAR = 'x', SPARE_CHAR = '/', WRONG_CHAR = '?', EMPTY_CHAR = ' ';

    public Scoreboard() {
        // Action listener for the reset button
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetBoard();
            }
        });

        // Document listener for each editable TextField. Listens to realtime text updating.
        DocumentListener realtimeUpdate = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onTextUpdate();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                onTextUpdate();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                onTextUpdate();
            }
        };

        // Add the document listener to each TextField in [inputFields].
        for (JTextField field : inputFields)
            field.getDocument().addDocumentListener(realtimeUpdate);
    }

    // Method called when text is updated.
    private void onTextUpdate() {
        String inputs = grabInputs();
        displayScores(calculateScores(inputs), inputs);
    }

    // Main method. Sets up Java SWING GUI.
    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new Scoreboard().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    // Parses the given input string and calculates score and bonuses based on each character. Returns an array with calculated scores.
    private int[] calculateScores(String inputs) {
        int[] scores = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        int result = 0, frameIndex;
        char input1, input2, input3;

        /*  Input is represented as designated characters in a string.
            Depending on which character it is, getPoints(char) will return a designated value for it.

            Example input string = "51x 5/34x x 73x x xx?" (always 21 characters long)
            Empty textFields = ' ', Wrong characters = '?', Digits = '0' - '9', Strike = 'x', Spare = '/'
        */

        for (int frame = 0; frame < FRAME_COUNT; frame++) {

            // Start by grabbing score of previous frame
            if (frame == 0) { frameIndex = 0; }
            else { frameIndex = scores[frame - 1]; }

            // Grab all needed values
            frameIndex = 2 * frame;
            input1 = inputs.charAt(frameIndex);
            input2= inputs.charAt(frameIndex + 1);
            input3 = inputs.charAt(frameIndex + 2);

            // Score += Value of both TextFields in frame.
            result += getPoints(input1) + getPoints(input2);

            if (input1 == STRIKE_CHAR) {
                if (frame == LAST_FRAME) {
                    // Score += Last TextField's value.
                    result += getPoints(input3);
                    // If last two shots are spare
                    if (isNum(input2) && (input3 == SPARE_CHAR || (isNum(input3) && getPoints(input2) + getPoints(input3) >= STRIKE_SPARE_SCORE)))
                        result += STRIKE_SPARE_SCORE - getPoints(input2) - getPoints(input3);
                }
                else // Not last frame
                    result += grabNextThrows(inputs, frame, true) - getPoints(input2);
            }
            else if (isNum(input1)) {
                // If first two shots are spare.
                if (input2 == SPARE_CHAR || (isNum(input2) && getPoints(input1) + getPoints(input2) >= STRIKE_SPARE_SCORE)) {
                    if (frame == LAST_FRAME)
                        result += STRIKE_SPARE_SCORE + getPoints(input3) - getPoints(input1) - getPoints(input2);
                    else // Not last frame
                        result += STRIKE_SPARE_SCORE + grabNextThrows(inputs, frame, false) - getPoints(input1) - getPoints(input2);
                }
            }
            scores[frame] = result;
        }
        return scores;
    }


    // Given a frame, and a boolean flag, calculates the sum of the next two shots and returns it.
    private int grabNextThrows(String inputs, int frame, boolean isStrike) {
        int nextFrameIndex, value = 0;;
        char input1, input2, input3;

        // Grab next frame's starting index and first shot.
        nextFrameIndex = 2 * (frame + 1);
        input1 = inputs.charAt(nextFrameIndex);

        // Do we grab next 1 or 2 shots?
        if (isStrike) {
            // Grab needed values (2nd shot)
            input2 = inputs.charAt(nextFrameIndex + 1);
            input3 = inputs.charAt(nextFrameIndex + 2);

            // If next shot was a strike
            if (input1 == STRIKE_CHAR) {
                value += getPoints(input1);
                // If next frame is the 10th
                if (nextFrameIndex < LAST_FRAME_INDEX && input3 != SPARE_CHAR)
                    value += getPoints(input3);
                else if (input2 != SPARE_CHAR)
                    value += getPoints(input2);
            }
            // If next shot was a number
            else if (isNum(input1)) {
                value += getPoints(input1);

                // If 2nd shot spared
                if (input2 == SPARE_CHAR || (isNum(input2) && getPoints(input1) + getPoints(input2) >= STRIKE_SPARE_SCORE))
                    value += STRIKE_SPARE_SCORE - getPoints(input1);
                else
                    value += getPoints(input2);
            }
        }
        // If just grabbing next shot
        else if (input1 != SPARE_CHAR)
            value += getPoints(input1);

        return value;
    }

    // Given a calculated score, displays each frame's score in its designated JTextField.
    private void displayScores(int[] scores, String inputs) {
        resetScores();
        for (int frame = 0; frame < checkFrames(inputs); frame++) {
            scoreFields[frame].setText(Integer.toString(scores[frame]));
        }
    }

    // Iterates through each frame, checks for correct syntax and returns the number of syntactically correct rounds.
    private int checkFrames(String inputs) {
        int rounds = 0, startIndex = 0;
        char v1, v2, v3;

        for (int frameIndex = 0; frameIndex <= LAST_FRAME_INDEX; frameIndex += 2) {
            // Grab each frame's needed values.
            v1 = inputs.charAt(frameIndex);
            v2 = inputs.charAt(frameIndex + 1);
            v3 = inputs.charAt(frameIndex + 2);

            /* For frames 1-9, check for the following bad conditions.
               - Box1 contains either a spare or no characters.                         [/][?]      [ ][?]
               - Box1 does not contain a strike, Box2 contains a strike or nothing.     [1][x]      [1][ ]
            */
            if (v1 == EMPTY_CHAR || v1 == SPARE_CHAR)
                break;
            else if (v1 != STRIKE_CHAR && (v2 == EMPTY_CHAR || v2 == STRIKE_CHAR))
                break;

            /* For frame 10, check for the following bad conditions.
               - Box1 contains a strike                                                 [x]
                    - Box2 or Box3 contains no characters OR Box2 contains a spare      [x][/][?]    [x][ ][ ]
                    - Box2 contains a strike, Box3 contains a spare                     [x][x][/]
                    - Box2 contains a digit, Box3 contains a strike                     [x][1][x]
               - Box1 contains a digit and Box2 is a strike.                            [1][x]
            */
            if (frameIndex == LAST_FRAME_INDEX) {
                if (v1 == STRIKE_CHAR) {
                    if (v2 == EMPTY_CHAR || v3 == EMPTY_CHAR || v2 == SPARE_CHAR)
                        break;
                    else if (v2 == STRIKE_CHAR && v3 == SPARE_CHAR)
                        break;
                    else if (isNum(v2) && v3 == STRIKE_CHAR)
                        break;
                }
                else if (isNum(v1) && v2 == STRIKE_CHAR)
                    break;
            }
            // Increments rounds up to the last syntactically correct frame.
            rounds++;
        }
        return rounds;
    }

    // Grabs all inputs, stores them into a character array (String) and returns it.
    private String grabInputs() {
        String inputs = "";
        // Iterate through each frame. (Frames contain two TextFields, tenth has three)
        for (int frameIndex = 0; frameIndex <= LAST_FRAME_INDEX; frameIndex += 2) {
            inputs = inputs.concat(Character.toString(getFirstChar(inputFields[frameIndex])));
            inputs = inputs.concat(Character.toString(getFirstChar(inputFields[frameIndex + 1])));
            if (frameIndex == LAST_FRAME_INDEX) { inputs = inputs.concat(Character.toString(getFirstChar(inputFields[frameIndex + 2]))); }
        }
        return inputs;
    }

    // Grabs, converts to lowercase, and returns the first character from a JTextField.
    private char getFirstChar(JTextField field) {
        char firstChar;

        // If TextField is empty
        if (field.getText().equals(""))
            firstChar = EMPTY_CHAR;
        else {
            firstChar = field.getText().toLowerCase().charAt(0);

            // If character is neither an 'x', '/', or digit
            if (firstChar != STRIKE_CHAR && firstChar != SPARE_CHAR && !isNum(firstChar))
                firstChar = WRONG_CHAR;
        }
        return firstChar;
    }

    // Given a character input, returns the designated score value of that character.
    private int getPoints(char input) {
        switch (input) {
            case STRIKE_CHAR:
            case SPARE_CHAR:
                return STRIKE_SPARE_SCORE;
            case WRONG_CHAR:
            case EMPTY_CHAR:
                return 0;
            default:
                return charToInt(input);
        }
    }

    // Calls resetInputs() and resetScores() to erase entire scoreboard.
    private void resetBoard() {
        resetInputs();
        resetScores();
    }

    // Iterates through all input-holding TextFields, and erases their content.
    private void resetInputs() {
        for (int i = 0; i < inputFields.length; i++)
            inputFields[i].setText("");
    }

    // Iterates through all score-holding TextFields, and erases their content.
    private void resetScores() {
        for (int i = 0; i < scoreFields.length; i++)
            scoreFields[i].setText("");
    }

    // Given a character input, returns a converted integer value.
    private int charToInt(char input) {
        return input - '0';
    }

    // Given a character input, returns whether the character can be treated as a number.
    private boolean isNum(char input) {
        return input >= '0' && input <= '9' || input == WRONG_CHAR;
    }
}