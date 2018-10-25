package com.example.tytanan.mycalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CalculatorHome extends AppCompatActivity {

    TextView inputBox, answerBox = null;
    char selectedFunction = '0';
    boolean allowDot = true;
    boolean lastButtonPressNotNumber = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_home);

        inputBox = findViewById(R.id.inputText);
        answerBox = findViewById(R.id.answer);

    }

    public void OnNumberButtonClick(View v) {

        /* add this button's number to the input string
         *  Note: a number is always a valid button press */
        Button thisButton = findViewById(v.getId());

        String tempText = inputBox.getText().toString();
        tempText += thisButton.getText().toString();
        inputBox.setText(tempText);

        lastButtonPressNotNumber = false;

    }

    public void OnDotButtonClick(View v) {

        /*  Add a dot only if there have been no dot presses since a function button press
         *  or an equals button press.
         *  Add a zero to the front of the dot if the preceding character is not a number to ensure
         *  safe handling when converting to doubles. */
        if (allowDot) {

            String tempText = inputBox.getText().toString();

            if (tempText.isEmpty() || lastButtonPressNotNumber) {
                tempText += "0.";
            } else {
                tempText += ".";
            }

            inputBox.setText(tempText);

            lastButtonPressNotNumber = false;
            allowDot = false;
        }

    }

    public void OnFunctionButtonClick(View v) {

        /* check if there is an input number, otherwise use the previous answer
           save the function associated with this button
           add the selected function to the displayed input text
           Note: do nothing if there is no valid input number, or a function was already selected
         */

        Button thisButton = findViewById(v.getId());
        String inputText = inputBox.getText().toString();

        /* Use the previous answer if the input is empty */
        if (inputText.isEmpty()) {
            inputText = "ans";
        }

        /* Only set a new function is there was no previous function selected */
        if (!inputText.isEmpty() && selectedFunction == '0') {

            selectedFunction = thisButton.getText().charAt(0);
            inputText += selectedFunction;

            inputBox.setText(inputText);

            allowDot = true;
            lastButtonPressNotNumber = true;

        }

    }

    public void OnEqualButtonClick(View v) {

        /* Perform operations based on which function was pressed
           Display answer in answer box, or display the input if no function was pressed
           Clear input box
         */

        String equation = inputBox.getText().toString();

        boolean valid = false;
        double ans = 0;

        /* If a function button was not pressed, display the input
         * If a function button was pressed, perform the selected operation
         * An equals button press is not valid if the input is empty or there has not been a number
         *    button pressed since a function button was pressed */
        if (!equation.isEmpty() && selectedFunction == '0') {

            valid = true;

            ans = Double.parseDouble(equation);

        } else if (!equation.isEmpty() && !lastButtonPressNotNumber) {

            valid = true;

            int idx = equation.indexOf(selectedFunction);

            /* x is the input preceding the function character or the previous answer
            *  y is the input following the function character */
            double x;
            if (equation.substring(0, idx).matches("ans"))
                x = Double.parseDouble(answerBox.getText().toString());
            else
                x = Double.parseDouble(equation.substring(0, idx));

            double y = Double.parseDouble(equation.substring(idx + 1));


            switch (selectedFunction) {
                case '+':
                    ans = x + y;
                    break;
                case '-':
                    ans = x - y;
                    break;
                case '*':
                    ans = x * y;
                    break;
                case '/':
                    ans = x / y;
                    break;
                default:
                    ans = x;
                    break;
            }

        }

        /* Update the answer box only if this was a valid equals button press */
        if (valid) {

            answerBox.setText(Double.toString(ans));
            inputBox.setText("");

            allowDot = true;
            lastButtonPressNotNumber = true;

            selectedFunction = '0';

        }

    }

}
