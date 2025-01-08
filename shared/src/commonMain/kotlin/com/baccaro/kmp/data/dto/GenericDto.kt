import kotlinx.serialization.Serializable

@Serializable
sealed class GenericDto{
    @Serializable
    data class ListDto(val data: List<ItemDto>):GenericDto()
    @Serializable
    data class ItemDto(val id:Int, val title:String, val imageUrl:String, val description:String):GenericDto()
}