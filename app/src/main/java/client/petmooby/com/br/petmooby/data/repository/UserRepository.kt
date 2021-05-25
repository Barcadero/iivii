package client.petmooby.com.br.petmooby.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import client.petmooby.com.br.petmooby.data.datasource.UserDataSource
import client.petmooby.com.br.petmooby.data.enums.EnumUserStatus
import client.petmooby.com.br.petmooby.model.Resource
import client.petmooby.com.br.petmooby.model.User
import client.petmooby.com.br.petmooby.model.enums.TypeUserEnum
import client.petmooby.com.br.petmooby.util.LogUtil
import client.petmooby.com.br.petmooby.util.Preference
import com.facebook.AccessToken
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
        private val userDataSource: UserDataSource
) {

    fun getFacebookUser(userId : String) : LiveData<Resource<User?,EnumUserStatus>>{
        return userDataSource.getFacebookUser(userId)
    }

    fun saveMissingUserInformation(context: Context, documentId: String, user: User): LiveData<Resource<User?, EnumUserStatus>> {
        var userNeedsUpdate = false
        if (user.name == null) {
            userNeedsUpdate = true
            user.name = Preference.getUserName(context)
        }
        if (user.email == null || user.email?.isEmpty()!!) {
            userNeedsUpdate = true
            user.email = Preference.getUserEmail(context)
        }
        if (user.type == null) {
            userNeedsUpdate = true
            user.type = TypeUserEnum.values()[Preference.getUserType(context)]
        }
        return if (userNeedsUpdate) {
            userDataSource.updateUser(documentId, user)
        } else {
            val mLiveData = MutableLiveData<Resource<User?, EnumUserStatus>>()
            mLiveData.value = Resource(user, EnumUserStatus.UPDATED)
            mLiveData
        }
    }

    fun getUser(userId: String): LiveData<Resource<User?, EnumUserStatus>> {
       return userDataSource.getUser(userId)
    }

    fun saveCurrentUser(context: Context) : LiveData<Resource<User?, EnumUserStatus>>{
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        return if(isLoggedIn) {
            val user            = User()
            user.name           = Preference.get<String>(context, Preference.USER_NAME)
            user.tokenFacebook  = Preference.get(context, Preference.USER_TOKEN)
            user.userIdFB       = Preference.getUserId(context)
            user.type           = TypeUserEnum.values()[Preference.getUserType(context)]
            user.email          = Preference.getUserEmail(context)
            user.registerDate   = Date()
            LogUtil.logDebug("FACE user name: ${user.name}")
            LogUtil.logDebug("FACE user email: ${user.email}")
            userDataSource.saveNewUser(user)
        }else{
            val mLiveData = MutableLiveData<Resource<User?, EnumUserStatus>>()
            mLiveData.value = Resource(null, EnumUserStatus.FAIL)
            mLiveData
        }
    }

}