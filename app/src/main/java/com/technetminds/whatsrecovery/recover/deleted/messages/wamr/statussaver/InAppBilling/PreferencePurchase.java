package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;


public class PreferencePurchase
{
    private SharedPreferences m_preference;
    private Editor my_editor;
    public static final String App_productId = "wrdm_productid";
    private final String AppTransactionDetails = "wrdm_transaction_details";

    public PreferencePurchase(Context context)
    {
        m_preference = context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0);
        my_editor = m_preference.edit();
    }

    public String getProductId()
    {
        return m_preference.getString(App_productId, "");
    }

    public void setProductId(String id)
    {
        my_editor.putString(App_productId, id).commit();
    }

    public void setItemDetails(String details)
    {
        my_editor.putString(AppTransactionDetails, details).commit();
    }

    public String getItemDetail()
    {
        return m_preference.getString(AppTransactionDetails, "");

    }


}
