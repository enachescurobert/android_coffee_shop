package com.robert.enachescurobert.robucoffeeshop.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.robert.enachescurobert.robucoffeeshop.R;

import java.text.NumberFormat;


public class MainActivity extends AppCompatActivity {

    enum COFFEE {
        SIMPLE,
        OREO,
        KITKAT,
        TOPPING
    }

    EditText customerName;
    EditText address;
    TextView mPriceTextView;
    TextView simpleTotal;
    TextView oreoTotal;
    TextView kitKatTotal;
    TextView toppingTotal;
    Button makeBillBtn;
    Button sendEmailBtn;
    Button incrementSimpleCoffeeBtn;
    Button incrementOreoCoffeeBtn;
    Button incrementKitKatCoffeeBtn;
    Button incrementToppingCoffeeBtn;
    Button decrementSimpleCoffeeBtn;
    Button decrementOreoCoffeeBtn;
    Button decrementKitKatCoffeeBtn;
    Button decrementToppingCoffeeBtn;

    private int numberOfSimpleCoffees = 0;
    private int numberOfOreoCoffees = 0;
    private int numberOfKitKatCoffees = 0;
    private int numberOfToppingCoffees = 0;

    View.OnClickListener amountOfCoffeesCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing views with activity_main.xml
        customerName = findViewById(R.id.customerName);
        address = findViewById(R.id.address);
        mPriceTextView = findViewById(R.id.price_text_view);
        makeBillBtn = findViewById(R.id.makeBillBtn);
        sendEmailBtn = findViewById(R.id.sendEmailBtn);
        simpleTotal = findViewById(R.id.simpleTotal);
        kitKatTotal = findViewById(R.id.kitKatTotal);
        oreoTotal = findViewById(R.id.oreoTotal);
        toppingTotal = findViewById(R.id.toppingTotal);
        incrementSimpleCoffeeBtn = findViewById(R.id.incrementSimpleCoffeeBtn);
        incrementOreoCoffeeBtn = findViewById(R.id.incrementOreoCoffeeBtn);
        incrementKitKatCoffeeBtn = findViewById(R.id.incrementKitKatCoffeeBtn);
        incrementToppingCoffeeBtn = findViewById(R.id.incrementToppingCoffeeBtn);
        decrementSimpleCoffeeBtn = findViewById(R.id.decrementSimpleCoffeeBtn);
        decrementOreoCoffeeBtn = findViewById(R.id.decrementOreoCoffeeBtn);
        decrementKitKatCoffeeBtn = findViewById(R.id.decrementKitKatCoffeeBtn);
        decrementToppingCoffeeBtn = findViewById(R.id.decrementToppingCoffeeBtn);

        amountOfCoffeesCallback = new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String nameOfResource = view.getResources().getResourceEntryName(view.getId());
                boolean shouldIncrement = nameOfResource.startsWith("increment");

                switch (nameOfResource) {
                    case "incrementSimpleCoffeeBtn" :
                    case "decrementSimpleCoffeeBtn" :
                        changeCoffeeAmount(shouldIncrement, COFFEE.SIMPLE);
                        break;

                    case "incrementOreoCoffeeBtn" :
                    case "decrementOreoCoffeeBtn" :
                        changeCoffeeAmount(shouldIncrement, COFFEE.OREO);
                        break;

                    case "incrementKitKatCoffeeBtn" :
                    case "decrementKitKatCoffeeBtn" :
                        changeCoffeeAmount(shouldIncrement, COFFEE.KITKAT);
                        break;

                    case "incrementToppingCoffeeBtn" :
                    case "decrementToppingCoffeeBtn" :
                        changeCoffeeAmount(shouldIncrement, COFFEE.TOPPING);
                        break;

                }
            }
        };

        incrementSimpleCoffeeBtn.setOnClickListener(amountOfCoffeesCallback);
        incrementOreoCoffeeBtn.setOnClickListener(amountOfCoffeesCallback);
        incrementKitKatCoffeeBtn.setOnClickListener(amountOfCoffeesCallback);
        incrementToppingCoffeeBtn.setOnClickListener(amountOfCoffeesCallback);
        decrementSimpleCoffeeBtn.setOnClickListener(amountOfCoffeesCallback);
        decrementOreoCoffeeBtn.setOnClickListener(amountOfCoffeesCallback);
        decrementKitKatCoffeeBtn.setOnClickListener(amountOfCoffeesCallback);
        decrementToppingCoffeeBtn.setOnClickListener(amountOfCoffeesCallback);

        makeBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (orderIsNotViable()){
                    return;
                }

                displayPrice();

                sendEmailBtn.setEnabled(true);
            }
        });

        sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (orderIsNotViable()){
                    sendEmailBtn.setEnabled(false);
                    mPriceTextView.setText("");
                    return;
                }

                displayPrice();

                String subject = customerName.getText().toString().equals("") ?
                        getResources().getString(R.string.order) :
                        getResources().getString(R.string.order_of) +
                                customerName.getText().toString().trim();
                String message = mPriceTextView.getText().toString().trim();

                sendEmail(subject, message);
            }
        });
    }

    private void sendEmail(String subject, String message) {
        Intent mEmailIntent = new Intent(Intent.ACTION_SEND);
//        mEmailIntent.setType("text/plain");
        mEmailIntent.setType("message/rfc822");
        mEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"enachescurobert@gmail.com"});
        mEmailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        mEmailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(mEmailIntent, this.getResources().getString(R.string.choose_mail)));
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private int incrementOrDecrement(int currentValue, boolean shouldIncrement) {

        if (shouldIncrement) {
            if (currentValue < 15) {
                return ++currentValue;
            } else {
                Toast.makeText(getApplicationContext(), R.string.over_15, Toast.LENGTH_SHORT).show();
            }
        } else {
            if (currentValue > 0) {
                return --currentValue;
            } else {
                Toast.makeText(getApplicationContext(), R.string.under_0, Toast.LENGTH_SHORT).show();
            }
        }
        return currentValue;
    }

    private void changeCoffeeAmount(boolean shouldIncrement, COFFEE coffeeType) {

        switch (coffeeType) {
            case SIMPLE:
                numberOfSimpleCoffees = incrementOrDecrement(numberOfSimpleCoffees, shouldIncrement);
                simpleTotal.setText(String.valueOf(numberOfSimpleCoffees));
                break;
            case OREO:
                numberOfOreoCoffees = incrementOrDecrement(numberOfOreoCoffees, shouldIncrement);
                oreoTotal.setText(String.valueOf(numberOfOreoCoffees));
                break;
            case KITKAT:
                numberOfKitKatCoffees = incrementOrDecrement(numberOfKitKatCoffees, shouldIncrement);
                kitKatTotal.setText(String.valueOf(numberOfKitKatCoffees));
                break;
            case TOPPING:
                numberOfToppingCoffees = incrementOrDecrement(numberOfToppingCoffees, shouldIncrement);
                toppingTotal.setText(String.valueOf(numberOfToppingCoffees));
                break;
        }
    }

    private void displayPrice() {

        int priceOfOneCoffee = 3;
        int priceOfOreoCoffee = 4;
        int priceOfKitKatCoffee = 4;
        int priceOfToppingCoffee = 5;

        int total = numberOfSimpleCoffees * priceOfOneCoffee + numberOfOreoCoffees * priceOfOreoCoffee + numberOfKitKatCoffees * priceOfKitKatCoffee + numberOfToppingCoffees * priceOfToppingCoffee;

        String addressOfTheCustomer = address.getText().toString();

        StringBuilder textToShow = new StringBuilder();
        textToShow.append("Selected Cofees:");
        if (numberOfSimpleCoffees > 0 ) {
            textToShow.append("\n-");
            textToShow.append(numberOfSimpleCoffees == 1 ?
                    numberOfSimpleCoffees + " " + this.getResources().getString(R.string.simple_coffee) :
                    numberOfSimpleCoffees + " " + this.getResources().getString(R.string.simple_coffees));
        }
        textToShow.append(numberOfOreoCoffees > 0 ? "\n-" + numberOfOreoCoffees + " " + this.getResources().getString(R.string.oreo_coffees) : "");
        textToShow.append(numberOfKitKatCoffees > 0 ? "\n-" + numberOfKitKatCoffees + " " + this.getResources().getString(R.string.kit_kat_coffees) : "");
        textToShow.append(numberOfToppingCoffees > 0 ? "\n-" + numberOfToppingCoffees + " " + this.getResources().getString(R.string.both_coffees) : "");
        textToShow.append("\n\n")
                .append(this.getResources().getString(R.string.how_much))
                .append("\n")
                .append(NumberFormat.getCurrencyInstance().format(total))
                .append("\n\n")
                .append(getString(R.string.deliver_at_address))
                .append("\n")
                .append(addressOfTheCustomer);

        mPriceTextView.setText(textToShow);
    }

    private boolean orderIsNotViable() {

        boolean viable = true;

        if (numberOfSimpleCoffees == 0 &&
                numberOfOreoCoffees == 0 &&
                numberOfKitKatCoffees == 0 &&
                numberOfToppingCoffees == 0) {
            Toast.makeText(getApplicationContext(),
                    "You must add at least one coffee to order.",
                    Toast.LENGTH_SHORT).show();
            viable = false;
        } else if (customerName.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "You must tell us your name to order.",
                    Toast.LENGTH_SHORT).show();
            viable =  false;
        } else if (address.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(),
                    "You must tell us your address to order.",
                    Toast.LENGTH_SHORT).show();
            viable = false;
        }
        return !viable;
    }

}
