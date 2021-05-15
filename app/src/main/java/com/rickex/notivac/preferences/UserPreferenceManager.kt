package com.tcp.rickexuser.preferences

import android.content.Context
import android.content.SharedPreferences


object UserPreferenceManager {
    private var mSharedPreferences: SharedPreferences? = null
    const val PREF_NAME = "SAMSRUSER"

    private const val USER_ID = "user_id"
    private const val PUSH_ID = "push_id"
    private const val FIRST_NAME = "first_name"
    private const val LAST_NAME = "last_name"
    private const val LOGIN_STATUS = "login_status"
    private const val AUTH_TOKEN = "auth_token"

    private const val SELECTED_DATE = "selected_date"
    private const val SELECTED_AGE = "selected_age"
    private const val SELECTED_VACCINE = "selected_vaccine"
    private const val SELECTED_STATE = "selected_state"
    private const val SELECTED_DISTRICT = "selected_district"
    private const val SELECTED_STATEID = "selected_stateid"
    private const val SELECTED_DISTRICTID = "selected_districtid"
    private const val SELECTED_FILTER_DISTRICT_PINCODE = "selected_districtpincode"
    private const val SELECTED_PINCODE = "selected_pincode"

    private fun init(mContext: Context) {
        mSharedPreferences = mContext.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
    }

    fun login(
        mContext: Context,
        userID: Int
    ) {
        if (mSharedPreferences == null) {
            init(mContext)
        }
        val mShEditor = mSharedPreferences!!.edit()
        mShEditor.putInt(USER_ID, userID)
        mShEditor.apply()
    }

    fun getUserId(mContext: Context): String? {
        var mUserId : String? = ""
        if (mSharedPreferences == null) {
            init(mContext)
        }
        mUserId = mSharedPreferences!!.getString(USER_ID, "")
        return mUserId
    }

    fun setUserId(mContext: Context, userId: String) {
        if (mSharedPreferences == null) {
            init(mContext)
        }
        val mShEditor = mSharedPreferences!!.edit()
        mShEditor.putString(USER_ID, userId)
        mShEditor.commit()
    }

    fun getPushNotiId(mContext: Context): String? {
        var mUserPushID: String? = ""
        if (mSharedPreferences == null) {
            init(mContext)
        }
        mUserPushID =
            mSharedPreferences!!.getString(PUSH_ID, "")
        return mUserPushID
    }

    fun setPushNotiId(
        mContext: Context,
        userNotificationID: String?
    ) {
        if (mSharedPreferences == null) {
            init(mContext)
        }
        val mShEditor = mSharedPreferences!!.edit()
        mShEditor.putString(PUSH_ID, userNotificationID)
        mShEditor.commit()
    }

    fun getFirstName(mContext: Context): String? {
        var mFirstName: String? = ""
        if (mSharedPreferences == null) {
            init(mContext)
        }
        mFirstName =
            mSharedPreferences!!.getString(FIRST_NAME, "")
        return mFirstName
    }

    fun setFirstName(
        mContext: Context,
        mFirstName: String?
    ) {
        if (mSharedPreferences == null) {
            init(mContext)
        }
        val mShEditor = mSharedPreferences!!.edit()
        mShEditor.putString(FIRST_NAME, mFirstName)
        mShEditor.commit()
    }

    fun getLastName(mContext: Context): String? {
        var mLastName: String? = ""
        if (mSharedPreferences == null) {
            init(mContext)
        }
        mLastName =
            mSharedPreferences!!.getString(LAST_NAME, "")
        return mLastName
    }

    fun setLastName(
        mContext: Context,
        mLastName: String?
    ) {
        if (mSharedPreferences == null) {
            init(mContext)
        }
        val mShEditor = mSharedPreferences!!.edit()
        mShEditor.putString(LAST_NAME, mLastName)
        mShEditor.commit()
    }

    fun getLoginStatus(mContext: Context): Boolean {
        var mLoginStatus: Boolean = false
        if (mSharedPreferences == null) {
            init(mContext)
        }
        mLoginStatus =
            mSharedPreferences!!.getBoolean(LOGIN_STATUS, false)
        return mLoginStatus
    }

    fun setLoginStatus(
        mContext: Context,
        mLoginStatus: Boolean
    ) {
        if (mSharedPreferences == null) {
            init(mContext)
        }
        val mShEditor = mSharedPreferences!!.edit()
        mShEditor.putBoolean(LOGIN_STATUS, mLoginStatus)
        mShEditor.commit()
    }

    fun getAuthToken(mContext: Context): String? {
        var mAuthToken: String? = ""
        if (mSharedPreferences == null) {
            init(mContext)
        }
        mAuthToken =
                mSharedPreferences!!.getString(AUTH_TOKEN, "")
        return mAuthToken
    }

    fun setAuthToken(
            mContext: Context,
            mAuthToken: String?
    ) {
        if (mSharedPreferences == null) {
            init(mContext)
        }
        val mShEditor = mSharedPreferences!!.edit()
        mShEditor.putString(AUTH_TOKEN, mAuthToken)
        mShEditor.commit()
    }

    fun getSelectedDate(mContext: Context): String? {
        var mSelectedDate: String? = ""
        if (mSharedPreferences == null) {
            init(mContext)
        }
        mSelectedDate =
            mSharedPreferences!!.getString(SELECTED_DATE, "")
        return mSelectedDate
    }

    fun setSelectedDate(
        mContext: Context,
        mSelectedDate: String?
    ) {
        if (mSharedPreferences == null) {
            init(mContext)
        }
        val mShEditor = mSharedPreferences!!.edit()
        mShEditor.putString(SELECTED_DATE, mSelectedDate)
        mShEditor.commit()
    }

    fun getSelectedAge(mContext: Context): String? {
        var mSelectedAge: String? = ""
        if (mSharedPreferences == null) {
            init(mContext)
        }
        mSelectedAge =
            mSharedPreferences!!.getString(SELECTED_AGE, "18 AND 45")
        return mSelectedAge
    }

    fun setSelectedAge(
        mContext: Context,
        mSelectedAge: String?
    ) {
        if (mSharedPreferences == null) {
            init(mContext)
        }
        val mShEditor = mSharedPreferences!!.edit()
        mShEditor.putString(SELECTED_AGE, mSelectedAge)
        mShEditor.commit()
    }

    fun getSelectedVaccine(mContext: Context): String? {
        var mSelectedVaccine: String? = ""
        if (mSharedPreferences == null) {
            init(mContext)
        }
        mSelectedVaccine =
            mSharedPreferences!!.getString(SELECTED_VACCINE, "COVAXIN AND COVISHIELD")
        return mSelectedVaccine
    }

    fun setSelectedVaccine(
        mContext: Context,
        mSelectedVaccine: String?
    ) {
        if (mSharedPreferences == null) {
            init(mContext)
        }
        val mShEditor = mSharedPreferences!!.edit()
        mShEditor.putString(SELECTED_VACCINE, mSelectedVaccine)
        mShEditor.commit()
    }

    fun getSelectedState(mContext: Context): String? {
        var mSelectedState: String? = ""
        if (mSharedPreferences == null) {
            init(mContext)
        }
        mSelectedState =
            mSharedPreferences!!.getString(SELECTED_STATE, "STATE")
        return mSelectedState
    }

    fun setSelectedState(
        mContext: Context,
        mSelectedState: String?
    ) {
        if (mSharedPreferences == null) {
            init(mContext)
        }
        val mShEditor = mSharedPreferences!!.edit()
        mShEditor.putString(SELECTED_STATE, mSelectedState)
        mShEditor.commit()
    }

    fun getSelectedStateID(mContext: Context): String? {
        var mSelectedStateID: String? = ""
        if (mSharedPreferences == null) {
            init(mContext)
        }
        mSelectedStateID =
            mSharedPreferences!!.getString(SELECTED_STATEID, "STATEID")
        return mSelectedStateID
    }

    fun setSelectedStateID(
        mContext: Context,
        mSelectedStateID: String?
    ) {
        if (mSharedPreferences == null) {
            init(mContext)
        }
        val mShEditor = mSharedPreferences!!.edit()
        mShEditor.putString(SELECTED_STATEID, mSelectedStateID)
        mShEditor.commit()
    }

    fun getSelectedDistrict(mContext: Context): String? {
        var mSelectedDistrict: String? = ""
        if (mSharedPreferences == null) {
            init(mContext)
        }
        mSelectedDistrict =
            mSharedPreferences!!.getString(SELECTED_DISTRICT, "DISTRICT")
        return mSelectedDistrict
    }

    fun setSelectedDistrict(
        mContext: Context,
        mSelectedDistrict: String?
    ) {
        if (mSharedPreferences == null) {
            init(mContext)
        }
        val mShEditor = mSharedPreferences!!.edit()
        mShEditor.putString(SELECTED_DISTRICT, mSelectedDistrict)
        mShEditor.commit()
    }

    fun getSelectedDistrictID(mContext: Context): String? {
        var mSelectedDistrictID: String? = ""
        if (mSharedPreferences == null) {
            init(mContext)
        }
        mSelectedDistrictID =
            mSharedPreferences!!.getString(SELECTED_DISTRICTID, "DISTRICTID")
        return mSelectedDistrictID
    }

    fun setSelectedDistrictID(
        mContext: Context,
        mSelectedDistrictID: String?
    ) {
        if (mSharedPreferences == null) {
            init(mContext)
        }
        val mShEditor = mSharedPreferences!!.edit()
        mShEditor.putString(SELECTED_DISTRICTID, mSelectedDistrictID)
        mShEditor.commit()
    }

    fun getSelectedDistrictOrPincode(mContext: Context): String? {
        var mSelectedDistrictorPincode: String? = ""
        if (mSharedPreferences == null) {
            init(mContext)
        }
        mSelectedDistrictorPincode =
            mSharedPreferences!!.getString(SELECTED_FILTER_DISTRICT_PINCODE, "DISTRICT")
        return mSelectedDistrictorPincode
    }

    fun setSelectedDistrictOrPincode(
        mContext: Context,
        mSelectedDistrictorPincode: String?
    ) {
        if (mSharedPreferences == null) {
            init(mContext)
        }
        val mShEditor = mSharedPreferences!!.edit()
        mShEditor.putString(SELECTED_FILTER_DISTRICT_PINCODE, mSelectedDistrictorPincode)
        mShEditor.commit()
    }

    fun getSelectedPincode(mContext: Context): String? {
        var mSelectedPincode: String? = ""
        if (mSharedPreferences == null) {
            init(mContext)
        }
        mSelectedPincode =
            mSharedPreferences!!.getString(SELECTED_PINCODE, "")
        return mSelectedPincode
    }

    fun setSelectedPincode(
        mContext: Context,
        mSelectedPincode: String?
    ) {
        if (mSharedPreferences == null) {
            init(mContext)
        }
        val mShEditor = mSharedPreferences!!.edit()
        mShEditor.putString(SELECTED_PINCODE, mSelectedPincode)
        mShEditor.commit()
    }

}
