package client.petmooby.com.br.petmooby.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
        tableName = "animal"
)
class AnimalEntity {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    @ColumnInfo(name = "animal_as_json")
    var animalAsJSON : String = ""
    @ColumnInfo(name = "id_firebase")
    var idFirebase : String = ""
    var time : Long = 0
}