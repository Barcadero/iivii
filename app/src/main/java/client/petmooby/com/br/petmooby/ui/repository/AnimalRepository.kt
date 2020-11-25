package client.petmooby.com.br.petmooby.ui.repository


import client.petmooby.com.br.petmooby.model.Animal
import client.petmooby.com.br.petmooby.model.CollectionsName
import client.petmooby.com.br.petmooby.util.FireStoreReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class AnimalRepository {
    private var docRefVet = FirebaseFirestore.getInstance()

    fun getAnimals(callback: (animals : List<Animal>) -> Unit){
        docRefVet.collection(CollectionsName.ANIMAL)
                .whereEqualTo("user", FireStoreReference.docRefUser)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    callback(
                        snapshotToList(querySnapshot)
                    )
                }.addOnFailureListener { exception ->
                    exception.message!!
                }
    }

    private fun snapshotToList(querySnapshot: QuerySnapshot) : List<Animal> {
        val list = mutableListOf<Animal>()
        querySnapshot.documents.forEach {doc->
            val animal = doc.toObject(Animal::class.java)
            if (animal?.id == null || animal.id!!.isEmpty()) {
                animal?.id = doc.id
            }
            list.add(animal!!)
        }
        return list
    }
}