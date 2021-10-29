package github.alexzhirkevich.community.core.entities.imp

import android.os.Parcel
import android.os.Parcelable
import github.alexzhirkevich.community.core.entities.interfaces.Entity
import github.alexzhirkevich.community.core.entities.interfaces.IEvent

class EventImpl(
        id: String="",
        override var name: String="",
        override var imageUri: String="",
        override var creatorId: String="",
        override var time: Long=0,
        override var address: String="",
        override var location: Pair<Float, Float> = 0f to 0f,
        override var description: String="",
        override var isEnded: Boolean = false,
        override var isValid: Boolean = true
        ) : EntityImpl(id), IEvent, Parcelable {
    constructor(parcel: Parcel) : this(
            id = parcel.readString().orEmpty(),
            name =parcel.readString().orEmpty(),
            imageUri =parcel.readString().orEmpty(),
            creatorId = parcel.readString().orEmpty(),
            time = parcel.readLong(),
            address = parcel.readString().orEmpty(),
            location = parcel.readFloat() to parcel.readFloat(),
            description = parcel.readString().orEmpty(),
            isEnded = parcel.readInt() == 1,
            isValid = parcel.readInt() == 1)


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(imageUri)
        parcel.writeString(creatorId)
        parcel.writeLong(time)
        parcel.writeString(address)
        parcel.writeFloat(location.first)
        parcel.writeFloat(location.second)
        parcel.writeString(description)
        parcel.writeInt( if (isEnded) 0 else 1)
        parcel.writeInt( if (isValid) 0 else 1)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun compareTo(other: Entity): Int {
        if (other !is EventImpl){
            return 0
        }
        if ((isEnded || !isValid) && !other.isEnded && other.isValid) return 1
        if ((other.isEnded || !other.isValid) && !isEnded && isValid) return -1

        if ((!isValid || isEnded) && (other.isEnded || !other.isValid)) {
            if (time != other.time) return other.time.compareTo(time)
            if (name != other.name) return other.name.compareTo(name)
            if (address != other.address) return other.address.compareTo(address)
            if (description != other.description) return other.description.compareTo(description)
        } else{
            if (time != other.time) return time.compareTo(other.time)
            if (name != other.name) return name.compareTo(other.name)
            if (address != other.address) return address.compareTo(other.address)
            if (description != other.description) return description.compareTo(other.description)
        }
        return super.compareTo(other)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EventImpl) return false
        if (!super.equals(other)) return false

        if (name != other.name) return false
        if (imageUri != other.imageUri) return false
        if (creatorId != other.creatorId) return false
        if (time != other.time) return false
        if (address != other.address) return false
        if (location != other.location) return false
        if (description != other.description) return false
        if (isEnded != other.isEnded) return false
        if (isValid != other.isValid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + imageUri.hashCode()
        result = 31 * result + creatorId.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + location.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + isEnded.hashCode()
        result = 31 * result + isValid.hashCode()
        return result
    }

    override fun toString(): String {
        return "Event(name='$name', imageUri='$imageUri', creatorId='$creatorId', time=$time, address='$address', location=$location, description='$description', isEnded=$isEnded, isValid=$isValid)"
    }


    companion object CREATOR : Parcelable.Creator<EventImpl> {
        override fun createFromParcel(parcel: Parcel): EventImpl {
            return EventImpl(parcel)
        }

        override fun newArray(size: Int): Array<EventImpl?> {
            return arrayOfNulls(size)
        }
    }
}