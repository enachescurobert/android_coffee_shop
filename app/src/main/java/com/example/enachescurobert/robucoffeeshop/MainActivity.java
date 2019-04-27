package com.example.enachescurobert.robucoffeeshop;
/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    public int numberOfCoffees = 0;
    public int priceOfOneCoffe = 3;

    public int numberOfOreoCoffees = 0;
    public int priceOfOreoCoffe = 4;

    public int numberOfKitKatCoffees = 0;
    public int priceOfKitKatCoffe = 4;

    public int numberOfToppingCoffees = 0;
    public int priceOfToppingCoffe = 5;

    EditText mCustomerName;
    TextView mPriceTextView;
    Button mSendEmailBtn;


    //Button sendEmailBtn;

    /*
    public boolean oreoAdded;
    public boolean kitKatAdded;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing views with activity_main.xml
        mCustomerName = findViewById(R.id.customerName);
        mPriceTextView = findViewById(R.id.price_text_view);
        mSendEmailBtn = findViewById(R.id.sendEmailBtn);

        //button click to get input and call sendEmail method
        mSendEmailBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //get input from EditTexts and TextViews and save in variables
                String recipient = "enachescurobert@gmail.com";
                String subject = mCustomerName.getText().toString().trim();
                String message = mPriceTextView.getText().toString().trim();

                //method call for email intent with these inputs as parameters
                sendEmail(recipient, subject, message);

            }
        });
    }

    private void sendEmail(String recipient, String subject, String message) {
        //Action_SEND action to launch an email client installed on our Android device
        Intent mEmailIntent = new Intent(Intent.ACTION_SEND);
        //To send an email, we need to specify milto: as URI using setData() method
        //and data type will be to text/plain using setType() method
        mEmailIntent.setData(Uri.parse("mailto:"));
        mEmailIntent.setType("text/plain");
        //put recipient email in intent
        /* recipient is put as array because we may want to send email to multiple emails so
        by using commas(,) separated emails, it will be stored in array
        */
        mEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
        //we will put the subject of the email
        mEmailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        //and now, the message
        mEmailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            //no error, so start intent
            startActivity(Intent.createChooser(mEmailIntent, this.getResources().getString(R.string.choose_mail)));
        }
        catch (Exception e){
            //if anything goes wrong e.g no internet or email client
            //get and show exception
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        displayPrice(numberOfCoffees * priceOfOneCoffe + numberOfOreoCoffees * priceOfOreoCoffe + numberOfKitKatCoffees * priceOfKitKatCoffe + numberOfToppingCoffees * priceOfToppingCoffe);
    }



    public void sendOurOrderByMail(View view) {

    }

    public void increment(View view) {
        if (numberOfCoffees < 15) {
            display(++numberOfCoffees);
            //displayPrice(numberOfCoffees * priceOfOneCoffe);
            ;
        } else {
            Toast.makeText(this, R.string.over_15, Toast.LENGTH_SHORT).show();
        }
    }

    public void increment_oreo_button(View view) {
        if (numberOfOreoCoffees < 15) {
            displayOreo(++numberOfOreoCoffees);
            //displayPrice(numberOfOreoCoffees * priceOfOreoCoffe);
            ;
        } else {
            Toast.makeText(this, R.string.over_15, Toast.LENGTH_SHORT).show();
        }
    }

    public void increment_kit_kat_button(View view) {
        if (numberOfKitKatCoffees < 15) {
            displayKitKat(++numberOfKitKatCoffees);
            //displayPrice(numberOfKitKatCoffees * priceOfKitKatCoffe);
            ;
        } else {
            Toast.makeText(this, R.string.over_15, Toast.LENGTH_SHORT).show();
        }
    }

    public void increment_both_button(View view) {
        if (numberOfToppingCoffees < 15) {
            displayTopping(++numberOfToppingCoffees);
            //displayPrice(numberOfToppingCoffees * priceOfToppingCoffe);
            ;
        } else {
            Toast.makeText(this, R.string.over_15, Toast.LENGTH_SHORT).show();
        }
    }

    public void decrement(View view) {
        if (numberOfCoffees > 0) {
            display(--numberOfCoffees);
            //displayPrice(numberOfCoffees * priceOfOneCoffe);
            ;
        } else {
            Toast.makeText(this, R.string.under_0, Toast.LENGTH_SHORT).show();
        }
    }

    public void decrement_oreo_button(View view) {
        if (numberOfOreoCoffees > 0) {
            displayOreo(--numberOfOreoCoffees);
            //displayPrice(numberOfOreoCoffees * priceOfOreoCoffe);
            ;
        } else {
            Toast.makeText(this, R.string.under_0, Toast.LENGTH_SHORT).show();
        }
    }

    public void decrement_kit_kat_button(View view) {
        if (numberOfKitKatCoffees > 0) {
            displayKitKat(--numberOfKitKatCoffees);
            //displayPrice(numberOfKitKatCoffees * priceOfKitKatCoffe);
            ;
        } else {
            Toast.makeText(this, R.string.under_0, Toast.LENGTH_SHORT).show();
        }
    }

    public void decrement_both_button(View view) {
        if (numberOfToppingCoffees > 0) {
            displayTopping(--numberOfToppingCoffees);
            //displayPrice(numberOfToppingCoffees * numberOfToppingCoffees);
            ;
        } else {
            Toast.makeText(this, R.string.under_0, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    private void displayKitKat(int numberKitKat) {
        TextView kitKatTextView = (TextView) findViewById(R.id.kit_kat_total);
        kitKatTextView.setText("" + numberKitKat);
    }

    private void displayOreo(int numberOreo) {
        TextView oreoTextView = (TextView) findViewById(R.id.oreo_total);
        oreoTextView.setText("" + numberOreo);
    }

    private void displayTopping(int numberTopping) {
        TextView toppingTextView = (TextView) findViewById(R.id.both_total);
        toppingTextView.setText("" + numberTopping);
    }

    private void displayPrice(int number) {

        EditText editText = (EditText) findViewById(R.id.customerName);
        String nameOfTheCustomer = editText.getText().toString();

        String text1 = String.valueOf(R.string.order_of);

        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(this.getResources().getString(R.string.order_of) + nameOfTheCustomer + ":\n" + numberOfCoffees + this.getResources().getString(R.string.simple_coffees) + numberOfOreoCoffees+ this.getResources().getString(R.string.oreo_coffees) + numberOfKitKatCoffees + this.getResources().getString(R.string.kit_kat_coffees) + numberOfToppingCoffees + this.getResources().getString(R.string.both_coffees) +"\n"+ this.getResources().getString(R.string.how_much) + NumberFormat.getCurrencyInstance().format(number));

    }





}