//package github.alexzhirkevich.community.core.entities.converters
//
//import androidx.room.ProvidedTypeConverter
//import androidx.room.TypeConverter
//import github.alexzhirkevich.community.core.entities.imp.ChannelAdmin
//import github.alexzhirkevich.community.core.entities.MediaContent
//import com.google.common.reflect.TypeToken
//import com.google.gson.Gson
//
//@ProvidedTypeConverter
//class CATypeConverter {
//
//    private val gson = Gson()
//
//    @TypeConverter
//    fun fromJson(string: String): Map<String, ChannelAdmin> {
//        val mapType = object : TypeToken<Map<String, ChannelAdmin>>() {}.type
//        return gson.fromJson(string, mapType)
//    }
//
//    @TypeConverter
//    fun toJson(obj: Map<String, ChannelAdmin>): String {
//        return gson.toJson(obj)
//    }
//}
//
//@ProvidedTypeConverter
//class MCTypeConverter {
//
//    private val gson = Gson()
//
//    @TypeConverter
//    fun fromJson(string: String): List<MediaContent> {
//        val mapType = object : TypeToken<List<MediaContent>>() {}.type
//        return gson.fromJson(string, mapType)
//    }
//
//    @TypeConverter
//    fun toJson(obj: List<MediaContent>): String {
//        return gson.toJson(obj)
//    }
//}
