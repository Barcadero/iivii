package client.petmooby.com.br.petmooby.model.enums


import androidx.annotation.StringRes
import client.petmooby.com.br.petmooby.R
import client.petmooby.com.br.petmooby.application.Application

/**
 * Created by Rafael Rocha on 25/07/2019.
 */
enum class EnumBreedsForCats(@StringRes resId:Int, value:String) : EnumBreedBase {

    ABYSSINIAN          (R.string.Abyssinian             ,"Abyssinian"),
    TURKISH_ANGORA      (R.string.Turkish_Angora         ,"Turkish Angora"),
    BALINESE            (R.string.Balinese               ,"Balinese"),
    BENGAL              (R.string.Bengal                 ,"Bengal"),
    AMERICAN_BOBTAIL    (R.string.American_Bobtail       ,"American Bobtail"),
    JAPANESE_BOBTAIL    (R.string.Japanese_Bobtail       ,"Japanese Bobtail"),
    BOMBAY              (R.string.Bombay                 ,"Bombay"),
    BURMESE             (R.string.Burmese                ,"Burmese"),
    CHARTREUX           (R.string.Chartreux              ,"Chartreux"),
    COLORPOINT_SHORTHAIR(R.string.Colorpoint_Shorthair   ,"Colorpoint Shorthair"),
    CORNISH_REX         (R.string.Cornish_Rex            ,"Cornish Rex"),
    AMERICAN_CURL       (R.string.American_Curl          ,"American Curl"),
    DEVON_REX           (R.string.Devon_Rex              ,"Devon Rex"),
    HIMALAYAN           (R.string.Himalayan              ,"Himalayan"),
    JAVANESE            (R.string.Javanese               ,"Javanese"),
    KORAT               (R.string.Korat                  ,"Korat"),
    LAPERM              (R.string.LaPerm                 ,"LaPerm"),
    MAINE_COON          (R.string.Maine_Coon             ,"Maine Coon"),
    MANX                (R.string.Manx                   ,"Manx "),
    EGYPTIAN_MAU        (R.string.Egyptian_Mau           ,"Egyptian Mau"),
    AUSTRALIAN_MIST     (R.string.Australian_Mist        ,"Australian Mist"),
    MUNCHKIN            (R.string.Munchkin               ,"Munchkin"),
    NORWEGIAN_FOREST    (R.string.Norwegian_Forest       ,"Norwegian Forest"),
    AMERICAN_SHORTHAIR  (R.string.American_ShortHair     ,"American ShortHair"),
    BRAZILIAN_SHORTHAIR (R.string.Brazilian_Shorthair    ,"Brazilian Shorthair"),
    EUROPEAN_SHORTHAIR  (R.string.European_shorthair     ,"European shorthair"),
    BRITISH_SHORTHAIR   (R.string.British_Shorthair      ,"British Shorthair"),
    PERSIAN             (R.string.Persian                ,"Persian"),
    PIXIE_BOB           (R.string.Pixie_bob              ,"Pixie-bob"),
    RAGDOLL             (R.string.Ragdoll                ,"Ragdoll"),
    OCICAT              (R.string.Ocicat                 ,"Ocicat"),
    RUSSIAN_BLUE        (R.string.Russian_Blue           ,"Russian Blue"),
    BIRMAN              (R.string.Birman                 ,"Birman"),
    SAVANNAH            (R.string.Savannah               ,"Savannah"),
    SCOTTISH_FOLD       (R.string.Scottish_Fold          ,"Scottish Fold"),
    SELKIRK_REX         (R.string.Selkirk_Rex            ,"Selkirk Rex"),
    SIAMESE             (R.string.Siamese                ,"Siamese"),
    SIBERIAN            (R.string.Siberian               ,"Siberian"),
    SINGAPURA           (R.string.Singapura              ,"Singapura"),
    SOMALI              (R.string.Somali                 ,"Somali"),
    SPHYNX              (R.string.Sphynx                 ,"Sphynx"),
    THAI                (R.string.Thai                   ,"Thai"),
    TONKINESE           (R.string.Tonkinese              ,"Tonkinese"),
    TOYGER              (R.string.Toyger                 ,"Toyger"),
    USSURI              (R.string.Ussuri                 ,"Ussuri"),
    OTHER               (R.string.Other                 ,"Other" );
    val label = Application.getString(resId)
    val value = value

    override fun toString(): String {
        return label
    }

    fun getByValue(value:String): EnumBreedsForCats{
        return EnumBreedsForCats.values()
                .firstOrNull()
                ?.let { if(it.value == value) it else EnumBreedsForCats.OTHER }
                ?: EnumBreedsForCats.OTHER
    }

    override fun getValue(position:Int): String {
        return EnumBreedsForCats.values()[position].value
    }

    override fun getIndex(enumBreedBase: EnumBreedBase): Int {
        return (enumBreedBase as EnumBreedsForCats).ordinal
    }

}