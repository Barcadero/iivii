package client.petmooby.com.br.petmooby.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
        tableName = "nearby_vets"
)
class NearbyVetsEntity {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    @ColumnInfo(name = "json")
    var json : String = ""
    var time : Long = 0
}