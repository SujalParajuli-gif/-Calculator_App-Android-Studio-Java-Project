package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// Main class for the calculator app
public class MainActivity extends AppCompatActivity {

    // Declare variables for display and student ID
    TextView display, studentId;

    // Store the first number entered, the operator used, and its numeric value
    String firstNumber = "";
    String operator = "";
    double firstValue = 0;

    // NEW: Variable to hold full expression like "12.5 + 7.5"
    String expression = "";

    // Called when the app starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link TextViews from XML layout
        display = findViewById(R.id.tvDisplay);
        studentId = findViewById(R.id.studentIdDisplay);

        // Set student ID to display
        studentId.setText("Student ID: 20027187");

        // Call methods to setup all buttons
        setupNumberButtons();
        setupOperatorButtons();
        setupEqualsButton();
        setupClearButton();
    }

    // This method handles all number and dot buttons
    private void setupNumberButtons() {
        // IDs of number and dot buttons
        int[] numberIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot
        };

        // When a number/dot is clicked, add it to the string and update the display
        View.OnClickListener listener = view -> {
            Button clicked = (Button) view;
            String text = clicked.getText().toString();
            firstNumber += text;             // Add to current input
            expression += text;              // NEW: Add to expression for full display
            display.setText(expression);     // Show full expression on screen
        };

        // Set the listener for each button
        for (int id : numberIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    // This method handles the operators: +, -, *, /, %
    private void setupOperatorButtons() {
        int[] ops = {
                R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply,
                R.id.btnDivide, R.id.btnPercent
        };

        // When an operator is clicked, save the first number and the operator
        View.OnClickListener opClick = view -> {
            if (!firstNumber.isEmpty()) {
                operator = ((Button) view).getText().toString();  // Get operator symbol
                expression += " " + operator + " ";               // NEW: Add operator to expression

                // Replace symbols with code-friendly values
                if (operator.equals("×")) operator = "*";
                if (operator.equals("÷")) operator = "/";

                // We added this block to handle single-number percentage conversion (like 25% = 0.25)
                if (operator.equals("%")) {
                    double value = Double.parseDouble(firstNumber);
                    value = value / 100;
                    display.setText(String.valueOf(value));
                    firstNumber = String.valueOf(value);  // Store the result for further use
                    operator = "";  // No further operation expected
                    expression = ""; // NEW: Reset expression for next input
                    return; // Stop here since we don't need to wait for another number
                }

                // Store first number for binary operations
                firstValue = Double.parseDouble(firstNumber);
                firstNumber = ""; // Clear for next input
                display.setText(expression);  // Show full expression so far
            }
        };

        // Set the listener for each operator button
        for (int id : ops) {
            findViewById(id).setOnClickListener(opClick);
        }
    }

    // This method calculates the result when "=" is pressed
    private void setupEqualsButton() {
        findViewById(R.id.btnEqual).setOnClickListener(view -> {
            if (!firstNumber.isEmpty()) {
                double secondValue = Double.parseDouble(firstNumber); // Get second number
                double result = 0;

                // Perform the operation based on selected operator
                switch (operator) {
                    case "+": result = firstValue + secondValue; break;
                    case "-": result = firstValue - secondValue; break;
                    case "*": result = firstValue * secondValue; break;
                    case "/":
                        if (secondValue != 0) {
                            result = firstValue / secondValue;
                        } else {
                            display.setText("Can't divide by 0");
                            return;
                        }
                        break;
                    case "%":
                        // We added this function to handle binary percentage (e.g., 200 % 10 = 20)
                        result = (firstValue * secondValue) / 100;
                        break;
                }

                // Show the result and reset values
                display.setText(String.valueOf(result));
                firstNumber = "";
                firstValue = 0;
                operator = "";
                expression = ""; // NEW: Clear full expression after showing result
            }
        });
    }

    // This method clears everything when "C" is pressed
    private void setupClearButton() {
        findViewById(R.id.btnClear).setOnClickListener(view -> {
            display.setText("0");   // Reset display
            firstNumber = "";       // Reset input
            firstValue = 0;         // Reset stored number
            operator = "";          // Reset operator
            expression = "";        // NEW: Reset expression
        });
    }
}
