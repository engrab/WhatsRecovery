/**
 * Copyright (C) 2018 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.billingrepo.localdb.GasTank
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.viewmodels.BillingViewModel
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.ads.AdmobUtils
import kotlinx.android.synthetic.main.fragment_game.*


class GameFragment : androidx.fragment.app.Fragment() {
    private val LOG_TAG = "GameFragment"

    private var gasLevel: GasTank? = null
    private lateinit var billingViewModel: BillingViewModel
    private lateinit var preferencePurchase: PreferencePurchase

    override fun onCreateView(
            inflater: LayoutInflater,
            containter: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, containter, false)


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        btn_drive.setOnClickListener { onDrive() }
        btn_purchase.setOnClickListener {
            onPurchase(it)
        }
        start_with_ads.setOnClickListener {

            val interstitial = AdmobUtils.getInterstitial()
            if (interstitial != null) {
                interstitial.show(requireActivity())
                interstitial.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        activity?.finish()
                        AdmobUtils.loadInterstitial(activity)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        super.onAdFailedToShowFullScreenContent(adError)
                        AdmobUtils.loadInterstitial(activity)
                    }
                }
            } else {
                activity?.finish()
                AdmobUtils.loadInterstitial(activity)
            }

        }
        billingViewModel = ViewModelProvider(this).get(BillingViewModel::class.java)
        billingViewModel.gasTankLiveData.observe(this, Observer {
            gasLevel = it
            Log.d(LOG_TAG, "showGasLevel called from billingViewModel with level ${it?.getLevel()}")
            showGasLevel()
        })
        billingViewModel.premiumCarLiveData.observe(this, Observer {
            it?.apply { showPremiumCar(entitled) }
        })
        billingViewModel.goldStatusLiveData.observe(this, Observer {
            it?.apply { showGoldStatus(entitled) }
        })
    }

//    private fun onDrive() {
//        gasLevel?.apply {
//            if (!needGas()) {
//                billingViewModel.decrementAndSaveGas()
//                showGasLevel()
//                Toast.makeText(context, getString(R.string.alert_drove), Toast.LENGTH_LONG).show()
//            }
//        }
//        if (gasLevel?.needGas() != false) {
//            Toast.makeText(context, getString(R.string.alert_no_gas), Toast.LENGTH_LONG).show()
//        }
//    }

    private fun onPurchase(view: View) {
        view.findNavController().navigate(R.id.action_makePurchase)
    }

    private fun showGasLevel() {
        gasLevel?.apply {
            Log.d(LOG_TAG, "showGasLevel called with level ${getLevel()} ")
            val drawableName = "gas_level_${getLevel()}"
            val drawableId = resources.getIdentifier(
                    drawableName,
                    "drawable",
                    requireActivity().packageName
            )
//            gas_gauge.setImageResource(drawableId)
        }
        if (gasLevel == null) {
//            gas_gauge.setImageResource(R.drawable.gas_level_0)
        }
    }

    private fun showPremiumCar(entitled: Boolean) {
        preferencePurchase = PreferencePurchase(requireContext())
        if (entitled) {
//            free_or_premium_car.setImageResource(R.drawable.premium_car)
            preferencePurchase.setProductId("com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver")
            preferencePurchase.setItemDetails("com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver")

        } else {
//            free_or_premium_car.setImageResource(R.drawable.free_car)
            preferencePurchase.setProductId("")
            preferencePurchase.setItemDetails("")

        }
    }

    private fun showGoldStatus(entitled: Boolean) {
        preferencePurchase = PreferencePurchase(requireContext())
        if (entitled) {
            preferencePurchase.setProductId("com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver")
            preferencePurchase.setItemDetails("com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver")

        } else {
            preferencePurchase.setProductId("")
            preferencePurchase.setItemDetails("")
//            gold_status.setBackgroundResource(0)
        }
    }


}
