package com.robert.enachescurobert.robucoffeeshop.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.robert.enachescurobert.robucoffeeshop.R;
import com.robert.enachescurobert.robucoffeeshop.models.Order;

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

    private static final String TAG = "MainActivity";
    public static final String FIREBASE_ORDERS_REFERENCE = "orders";
    
    private int numberOfSimpleCoffees = 0;
    private int numberOfOreoCoffees = 0;
    private int numberOfKitKatCoffees = 0;
    private int numberOfToppingCoffees = 0;

    View.OnClickListener amountOfCoffeesCallback;

    // Write a message to the database
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

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

                showOrderToBeSentAlert();
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

    private void writeNewOrderToFirebase() {
        Order order = new Order(customerName.getText().toString(),
                address.getText().toString(),
                numberOfSimpleCoffees,
                numberOfOreoCoffees,
                numberOfKitKatCoffees,
                numberOfToppingCoffees
        );

        String key = mDatabase.getDatabase().getReference(FIREBASE_ORDERS_REFERENCE).push().getKey();

        if (key != null) {
            mDatabase.child(FIREBASE_ORDERS_REFERENCE).child(key).setValue(order)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: It worked!");
                            showSendEmailAlert("Request done.", "Do you want to send us an email too?");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onSuccess: It failed! Error: " + e.getLocalizedMessage());
                            showSendEmailAlert("Request failed.", "Do you want to send us your order by email instead?");
                        }
                    });
        }
    }

    public void showOrderToBeSentAlert(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("One more step: ");
        alertDialog.setMessage("Complete the order?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        writeNewOrderToFirebase();
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }

    public void showSendEmailAlert(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String subject = customerName.getText().toString().equals("") ?
                                getResources().getString(R.string.order) :
                                getResources().getString(R.string.order_of) +
                                        customerName.getText().toString().trim();
                        String message = mPriceTextView.getText().toString().trim();

                        sendEmail(subject, message);

                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }

}
