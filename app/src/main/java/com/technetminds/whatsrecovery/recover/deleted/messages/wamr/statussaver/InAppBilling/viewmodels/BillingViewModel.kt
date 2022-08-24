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
package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.viewmodels

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.billingrepo.BillingRepository
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.billingrepo.localdb.AugmentedSkuDetails
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.billingrepo.localdb.GasTank
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.billingrepo.localdb.GoldStatus
import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.InAppBilling.billingrepo.localdb.PremiumCar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Notice just how small and simple this BillingViewModel is!!
 *
 * This beautiful simplicity is the result of keeping all the hard work buried inside the
 * [BillingRepository] and only inside the [BillingRepository]. The rest of your app
 * is now free from [BillingClient] tentacles!! And this [BillingViewModel] is the one and only
 * object the rest of your Android team need to know about billing.
 *
 */
class BillingViewModel(application: Application) : AndroidViewModel(application) {

    val gasTankLiveData: LiveData<GasTank>
    val premiumCarLiveData: LiveData<PremiumCar>
    val goldStatusLiveData: LiveData<GoldStatus>
    val subsSkuDetailsListLiveData: LiveData<List<AugmentedSkuDetails>>
    val inappSkuDetailsListLiveData: LiveData<List<AugmentedSkuDetails>>

    private val LOG_TAG = "BillingViewModel"
    private val viewModelScope = CoroutineScope(Job() + Dispatchers.Main)
    private val repository: BillingRepository

    init {
        repository = BillingRepository.getInstance(application)
        repository.startDataSourceConnections()
        gasTankLiveData = repository.gasTankLiveData
        premiumCarLiveData = repository.premiumCarLiveData
        goldStatusLiveData = repository.goldStatusLiveData
        subsSkuDetailsListLiveData = repository.subsSkuDetailsListLiveData
        inappSkuDetailsListLiveData = repository.inappSkuDetailsListLiveData
    }


    fun queryPurchases() = repository.queryPurchasesAsync()

    override fun onCleared() {
        super.onCleared()
        Log.d(LOG_TAG, "onCleared")
        repository.endDataSourceConnections()
        viewModelScope.coroutineContext.cancel()
    }

    fun makePurchase(activity: Activity, augmentedSkuDetails: AugmentedSkuDetails) {
        repository.launchBillingFlow(activity, augmentedSkuDetails)
    }

    /**
     * It's important to save after decrementing since gas can be updated by both clients and
     * the data sources.
     *
     * Note that even the ViewModel does not need to worry about thread safety because the
     * repo has already taken care it. So definitely the clients also don't need to worry about
     * thread safety.
     */
    fun decrementAndSaveGas() {
        val gas = gasTankLiveData.value
        gas?.apply {
            decrement()
            viewModelScope.launch {
                repository.updateGasTank(this@apply)
            }
        }
    }

}