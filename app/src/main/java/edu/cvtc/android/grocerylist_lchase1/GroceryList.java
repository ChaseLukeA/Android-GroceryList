package edu.cvtc.android.grocerylist_lchase1;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class GroceryList extends AppCompatActivity {

    private ArrayList<GroceryItem> groceryItemArray = new ArrayList<>();

    private Button addGroceryItemButton;
    private EditText addGroceryItemEditText;
    private EditText addGroceryItemQuantityEditText;

    private Integer groceryItemTextSize;
    private Integer groceryItemTextPadding;

    private LinearLayout groceryItemListLayout;
    private boolean initGroceryItemArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groceryItemTextSize = Math.round(getResources().getDimensionPixelOffset(R.dimen.grocery_item_text_size) / getResources().getDisplayMetrics().density);
        groceryItemTextPadding = Math.round(getResources().getDimensionPixelOffset(R.dimen.grocery_item_text_padding) / getResources().getDisplayMetrics().density);


        initLayout();

        String[] groceryList = this.getResources().getStringArray(R.array.groceryList);

        // NOTES: initGroceryItemArray prevents showing "Updated existing item quantity"
        // Toast for the first init of groceryItemArray for duplicates
        initGroceryItemArray = true;
        for (String item : groceryList) {

            addGroceryItem(new GroceryItem(item));
        }
        initGroceryItemArray = false;

        initAddGroceryItemListeners();
    }


    private void initLayout() {

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setBackgroundColor(getResources().getColor(R.color.color_primary_dark));


        // --Add Grocery Item Init -----------------------------------------------------------------
        LinearLayout addGroceryItemLayout = new LinearLayout(this);
        addGroceryItemLayout.setOrientation(LinearLayout.HORIZONTAL);


        addGroceryItemQuantityEditText = new EditText(this);
        addGroceryItemQuantityEditText.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                20
        ));
        addGroceryItemQuantityEditText.setGravity(Gravity.CENTER_HORIZONTAL);
        addGroceryItemQuantityEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        addGroceryItemQuantityEditText.setTextColor(getResources().getColor(R.color.color_text));
        addGroceryItemQuantityEditText.setHintTextColor(getResources().getColor(R.color.color_hint));
        addGroceryItemQuantityEditText.setHint(getString(R.string.add_grocery_item_quantity_edit_text_hint));

        addGroceryItemLayout.addView(addGroceryItemQuantityEditText);


        addGroceryItemEditText = new EditText(this);
        addGroceryItemEditText.setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                80
        ));
        addGroceryItemEditText.setGravity(Gravity.LEFT);
        addGroceryItemEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        addGroceryItemEditText.setTextColor(getResources().getColor(R.color.color_text));
        addGroceryItemEditText.setHintTextColor(getResources().getColor(R.color.color_hint));
        addGroceryItemEditText.setHint(getString(R.string.add_grocery_item_edit_text_hint));
        addGroceryItemEditText.requestFocus();

        addGroceryItemLayout.addView(addGroceryItemEditText);


        addGroceryItemButton = new Button(this);
        addGroceryItemButton.setText(getString(R.string.add_grocery_item_button_text));

        addGroceryItemLayout.addView(addGroceryItemButton);


        // Grocery Item List Header Init -----------------------------------------------------------
        LinearLayout groceryItemListHeaderLayout = new LinearLayout(this);
        groceryItemListHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);


        TextView groceryItemQuantityHeader = new TextView(this);
        groceryItemQuantityHeader.setLayoutParams(new LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 20
        ));
        groceryItemQuantityHeader.setGravity(Gravity.CENTER_HORIZONTAL);
        groceryItemQuantityHeader.setPadding(groceryItemTextPadding, groceryItemTextPadding, groceryItemTextPadding, groceryItemTextPadding);
        groceryItemQuantityHeader.setTextColor(getResources().getColor(R.color.color_text));
        groceryItemQuantityHeader.setText(R.string.grocery_item_quantity);

        groceryItemListHeaderLayout.addView(groceryItemQuantityHeader);


        TextView groceryItemNameHeader = new TextView(this);
        groceryItemNameHeader.setLayoutParams(new LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 60
        ));
        groceryItemNameHeader.setGravity(Gravity.LEFT);
        groceryItemNameHeader.setPadding(groceryItemTextPadding, groceryItemTextPadding, groceryItemTextPadding, groceryItemTextPadding);
        groceryItemNameHeader.setTextColor(getResources().getColor(R.color.color_text));
        groceryItemNameHeader.setText(R.string.grocery_item_name);

        groceryItemListHeaderLayout.addView(groceryItemNameHeader);


        TextView groceryItemInCartHeader = new TextView(this);
        groceryItemInCartHeader.setLayoutParams(new LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 20
        ));
        groceryItemInCartHeader.setGravity(Gravity.CENTER_HORIZONTAL);
        groceryItemInCartHeader.setPadding(groceryItemTextPadding, groceryItemTextPadding, groceryItemTextPadding, groceryItemTextPadding);
        groceryItemInCartHeader.setTextColor(getResources().getColor(R.color.color_text));
        groceryItemInCartHeader.setText(R.string.grocery_item_in_cart);

        groceryItemListHeaderLayout.addView(groceryItemInCartHeader);


        // -----------------------------------------------------------------------------------------
        groceryItemListLayout = new LinearLayout(this);
        groceryItemListLayout.setOrientation(LinearLayout.VERTICAL);


        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(groceryItemListLayout);


        container.addView(addGroceryItemLayout);
        container.addView(groceryItemListHeaderLayout);
        container.addView(scrollView);


        setContentView(container);
    }


    private void addGroceryItem(GroceryItem item) {

        if (null != item.getItemName() && !item.getItemName().isEmpty()) {

            GroceryItem groceryItem = existingGroceryItem(item);

            if (null == groceryItem) {

                groceryItemArray.add(item);

                addGroceryItemDetailLayout(item);

                if (!initGroceryItemArray) {

                    Toast.makeText(this, R.string.add_grocery_item_success, Toast.LENGTH_SHORT).show();
                }
            }
            else {  // item exits - update quantity

                groceryItem.setQuantity(groceryItem.getQuantity() + item.getQuantity());

                refreshGroceryItemLayout();

                if (!initGroceryItemArray) {

                    Toast.makeText(this, R.string.add_grocery_item_quantity_updated, Toast.LENGTH_SHORT).show();
                }
            }

        }
        else {

            Toast.makeText(this, R.string.add_grocery_item_error, Toast.LENGTH_SHORT);
        }
    }


    private void initAddGroceryItemListeners() {

        addGroceryItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGroceryItemFromEditTexts();
            }
        });

        addGroceryItemButton.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_NUMPAD_ENTER:
                            addGroceryItemFromEditTexts();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }


    private void addGroceryItemDetailLayout(final GroceryItem item) {

        Integer textColor,
                backgroundColor;

        if (groceryItemListLayout.getChildCount() % 2  == 1) {
            textColor = getResources().getColor(R.color.grocery_item_text_color_light);
            backgroundColor = getResources().getColor(R.color.grocery_item_background_color_dark);
        }
        else {
            textColor = getResources().getColor(R.color.grocery_item_text_color_dark);
            backgroundColor = getResources().getColor(R.color.grocery_item_background_color_light);
        }


        LinearLayout groceryItemDetailLayout = new LinearLayout(this);
        groceryItemDetailLayout.setOrientation(LinearLayout.HORIZONTAL);


        TextView quantityTextView = new TextView(this);
        quantityTextView.setLayoutParams(new LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 20
        ));
        quantityTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        quantityTextView.setPadding(groceryItemTextPadding, groceryItemTextPadding, groceryItemTextPadding, groceryItemTextPadding);
        quantityTextView.setBackgroundColor(backgroundColor);
        quantityTextView.setTextColor(textColor);
        quantityTextView.setTextSize(groceryItemTextSize);
        quantityTextView.setText(item.getQuantity().toString());

        groceryItemDetailLayout.addView(quantityTextView);


        TextView nameTextView = new TextView(this);
        nameTextView.setLayoutParams(new LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 60
        ));
        nameTextView.setGravity(Gravity.LEFT);
        nameTextView.setBackgroundColor(backgroundColor);
        nameTextView.setPadding(groceryItemTextPadding, groceryItemTextPadding, groceryItemTextPadding, groceryItemTextPadding);
        nameTextView.setTextColor(textColor);
        nameTextView.setTextSize(groceryItemTextSize);
        nameTextView.setText(item.getItemName());

        groceryItemDetailLayout.addView(nameTextView);


        // NOTES: needed to use a layout to contain checkbox to center the
        // checkbox, it's gravity doesn't work correctly in linear layouts
        LinearLayout inCartCheckBoxLayout = new LinearLayout(this);
        inCartCheckBoxLayout.setLayoutParams(new LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 20
        ));
        inCartCheckBoxLayout.setGravity(Gravity.CENTER);
        inCartCheckBoxLayout.setBackgroundColor(backgroundColor);

        CheckBox inCartCheckBox = new CheckBox(this);
        inCartCheckBox.setButtonDrawable(R.drawable.checkbox_states);
        inCartCheckBox.setChecked(item.isItemInCart());
        inCartCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    item.setItemInCart(true);
                }
                else {
                    item.setItemInCart(false);
                }
            }
        });

        inCartCheckBoxLayout.addView(inCartCheckBox);
        groceryItemDetailLayout.addView(inCartCheckBoxLayout);


        groceryItemListLayout.addView(groceryItemDetailLayout);
    }


    private void refreshGroceryItemLayout() {

        groceryItemListLayout.removeAllViewsInLayout();

        for (GroceryItem item : groceryItemArray) {

            addGroceryItemDetailLayout(item);
        }
    }


    private void addGroceryItemFromEditTexts() {

        String itemName = addGroceryItemEditText.getText().toString().trim();
        String itemQuantity = addGroceryItemQuantityEditText.getText().toString().trim();


        if (null != itemName && !itemName.isEmpty()) {

            if (null != itemQuantity && !itemQuantity.isEmpty()) {

                // NOTES: used a try-catch here even though the android soft keyboards
                // tested won't allow selection of the alpha-numeric keys; used in case
                // a different used soft keyboard doesn't use numbers keyboard correctly
                try {

                    Integer quantity = Integer.parseInt(itemQuantity);
                    addGroceryItem(new GroceryItem(itemName, quantity));

                    resetItemName();
                    resetQuantity();
                    hideSoftKeyboard();
                }
                catch (NumberFormatException e) {

                    Toast.makeText(this, R.string.add_grocery_item_quantity_edit_text_error, Toast.LENGTH_SHORT).show();

                    resetQuantity();

                    addGroceryItemQuantityEditText.requestFocus();
                }
            }
            else {

                addGroceryItem(new GroceryItem(itemName));

                resetItemName();
                hideSoftKeyboard();
            }
        }
        else {

            Toast.makeText(this, R.string.add_grocery_item_edit_text_error, Toast.LENGTH_SHORT).show();

            addGroceryItemEditText.requestFocus();
        }
    }


    private GroceryItem existingGroceryItem(GroceryItem item) {

        for (GroceryItem existingItem : groceryItemArray) {

            if (existingItem.equals(item)) {

                return existingItem;
            }
        }

        return null;
    }


    private void resetItemName() {

        addGroceryItemEditText.setText("");
    }


    private void resetQuantity() {

        addGroceryItemQuantityEditText.setText("");
    }


    private void hideSoftKeyboard() {

        View currentView = this.getCurrentFocus();

        if (null != currentView) {

            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
        }
    }

}
