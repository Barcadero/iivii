package client.petmooby.com.br.petmooby.model.repository



import androidx.lifecycle.MutableLiveData
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.model.Resource
import client.petmooby.com.br.petmooby.model.User
import client.petmooby.com.br.petmooby.model.UserLogin
import client.petmooby.com.br.petmooby.model.enums.StatusLogin
import client.petmooby.com.br.petmooby.model.enums.TypeUserEnum
import client.petmooby.com.br.petmooby.util.EncryptUtil
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository  @Inject constructor(): BaseRepo(){
    private var docRefUser = fbReference.collection(CollectionsName.USER)
    suspend fun doLoginSystem(email : String, pwd : String) : Resource<UserLogin,StatusLogin>{
        return try{
                val query = docRefUser
                    .whereEqualTo(User.USER_EMAIL,email)
                    .whereEqualTo(User.USER_PASSWORD, EncryptUtil.encryptPWD(pwd))
                    .get()
                    .await()
                if(query.isEmpty){
                    Resource(null,StatusLogin.EMPTY)
                }else{
                    val user = UserLogin(
                               email = email,
                               docRef = query.documents[0].reference.path,
                               docId =  query.documents[0].reference.id,
                               type = TypeUserEnum.USER_SYSTEM
                        )
                    Resource(
                                user,
                                StatusLogin.SUCCESS
                        )
                }
        }catch (e : Exception){
            return Resource(null, StatusLogin.FAIL)
        }
    }
}