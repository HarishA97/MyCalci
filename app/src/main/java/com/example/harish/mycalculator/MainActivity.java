package com.example.harish.mycalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button cancel,backspace,mod,division,seven,eight,nine,mult,four,five,six,minus,
            one1,two,three,plus,dot,zero,equal;
    TextView textView;
    String str;

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    public  boolean check(String c){
        switch(c){
            case "+":
            case "-":
            case "*":
            case "%":
            case "/":
            case ".":
                return false;
            default:
                return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    textView.setText("");
            }
        });

        backspace = (Button) findViewById(R.id.backspace);
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                if(str != null && !str.isEmpty()){
                    str = str.substring(0, str.length() - 1);
                    textView.setText(str);
                }
            }
        });

        mod = (Button) findViewById(R.id.mod);
        mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                if (check(str.substring(str.length() - 1))) {
                    str = str + "%";
                    textView.setText(str);
                }
            }
        });

        division = (Button) findViewById(R.id.division);
        division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                if (check(str.substring(str.length() - 1))) {
                    str = str + "/";
                    textView.setText(str);
                }
            }
        });

        mult = (Button) findViewById(R.id.multiply);
        mult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                if (check(str.substring(str.length() - 1))) {
                    str = str + "*";
                    textView.setText(str);
                }
            }
        });

        minus = (Button) findViewById(R.id.minus);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                if (str.isEmpty() || check(str.substring(str.length() - 1))) {
                    str = str + "-";
                    textView.setText(str);
                }
            }
        });

        plus = (Button) findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                if (str.isEmpty() || check(str.substring(str.length() - 1))) {
                    str = str + "+";
                    textView.setText(str);
                }
            }
        });

        dot = (Button) findViewById(R.id.point);
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                String[] parts = str.split("[^0-9\\.]");
                String s = parts[parts.length-1];
                if(s.indexOf('.')<0) {
                    str = str + ".";
                    textView.setText(str);
                }
            }
        });

        zero = (Button) findViewById(R.id.zero);
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                str = str + "0";
                textView.setText(str);
            }
        });

        one1 = (Button) findViewById(R.id.one);
        one1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                str = str + "1";
                textView.setText(str);
            }
        });

        two = (Button) findViewById(R.id.two);
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                str = str + "2";
                textView.setText(str);
            }
        });

        three = (Button) findViewById(R.id.three);
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                str = str + "3";
                textView.setText(str);
            }
        });

        four = (Button) findViewById(R.id.four);
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                str = str + "4";
                textView.setText(str);
            }
        });

        five = (Button) findViewById(R.id.five);
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                str = str + "5";
                textView.setText(str);
            }
        });

        six = (Button) findViewById(R.id.six);
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                str = str + "6";
                textView.setText(str);
            }
        });

        seven = (Button) findViewById(R.id.seven);
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                str = str + "7";
                textView.setText(str);
            }
        });

        eight = (Button) findViewById(R.id.eight);
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                str = str + "8";
                textView.setText(str);
            }
        });

        nine = (Button) findViewById(R.id.nine);
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                str = str + "9";
                textView.setText(str);
            }
        });

        equal = (Button) findViewById(R.id.equal);
        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = (String) textView.getText();
                try {
                    double ans = eval(str);
                    str = String.valueOf(ans);
                    textView.setText(str);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this,"Sorry! Unable to process that!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
