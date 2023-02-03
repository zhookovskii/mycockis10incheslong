package hashslingingslasher.andoidhw.messenger

/*
    Class to convert Message objects to TableMessage objects
    for further insertion into application database
 */

class TableDataConverter(private val messages: List<TableMessage>) {

    constructor() : this(emptyList())

    fun toTable(message: Message): TableMessage {
        if (message.data.Text != null) {
            return TableMessage(
                message.id,
                message.from,
                message.to,
                message.data.Text.text,
                false,
                message.time)
        } else {
            return TableMessage(
                message.id,
                message.from,
                message.to,
                message.data.Image!!.link,
                true,
                message.time
            )
        }
    }

    fun convert() : MutableList<Message> {
        val res = mutableListOf<Message>()
        for (tableMessage in messages) {
            if (tableMessage.image) {
                res.add(Message(
                    tableMessage.id,
                    tableMessage.from,
                    tableMessage.to,
                    Data(null, Image(tableMessage.text, null)),
                    tableMessage.time
                ))
            } else {
                res.add(Message(
                    tableMessage.id,
                    tableMessage.from,
                    tableMessage.to,
                    Data(Text(tableMessage.text), null),
                    tableMessage.time
                ))
            }
        }
        return res
    }



}