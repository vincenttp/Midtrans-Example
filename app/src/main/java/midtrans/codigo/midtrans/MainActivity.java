package midtrans.codigo.midtrans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TransactionRequest transactionRequest;
    Button tcash, corekit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tcash = (Button) findViewById(R.id.bca);
        corekit = (Button) findViewById(R.id.corekit);

        init();
        preparePayment();
        tcash.setOnClickListener(this);
        corekit.setOnClickListener(this);
    }

    private void init(){
        SdkUIFlowBuilder.init(getApplicationContext(), BuildConfig.CLIENT_KEY, BuildConfig.BASE_URL, new TransactionFinishedCallback() {
            @Override
            public void onTransactionFinished(TransactionResult result) {

            }
        })
                .enableLog(true)
                .buildSDK();
    }

    private void preparePayment(){
        int id = new Random().nextInt(99)+100;
        transactionRequest = new TransactionRequest(String.valueOf(id), 10000);

        CustomerDetails customer = new CustomerDetails();
        customer.setFirstName("Vincent Tolanda Parinding");
        customer.setEmail("tolanda.vincent@gmail.com");
        customer.setPhone("");
        transactionRequest.setCustomerDetails(customer);

        UserAddress userAddress = new UserAddress();
        userAddress.setAddress("");
        userAddress.setCity("");
        userAddress.setZipcode("");
        userAddress.setCountry("");

        ArrayList<UserAddress> userAddresses = new ArrayList<>();
        userAddresses.add(userAddress);

        UserDetail userDetail = new UserDetail();
        userDetail.setUserFullName("Vincent Tolanda Parinding");
        userDetail.setEmail("tolanda.vincent@gmail.com");
        userDetail.setPhoneNumber("");
        userDetail.setUserAddresses(userAddresses);
        LocalDataHandler.saveObject("user_details", userDetail);

        ItemDetails itemDetails = new ItemDetails("1", 10000, 1, "Beli Baju");

        // Add item details into item detail list.
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        transactionRequest.setItemDetails(itemDetailsArrayList);

        CreditCard creditCardOptions = new CreditCard();
        creditCardOptions.setSaveCard(false);
        creditCardOptions.setSecure(false);

        transactionRequest.setCreditCard(creditCardOptions);
        transactionRequest.setCardPaymentInfo(getString(R.string.card_click_type_none), true);

        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bca:
                MidtransSDK.getInstance().startPaymentUiFlow(this);
                break;
            case R.id.corekit:
                Intent intent = new Intent(this, UiKitCustom.class);
                startActivity(intent);
        }
    }
}
