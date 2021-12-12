package sku.challenge.atmanatodoapp.ui

interface ItemButtonListener {
    fun edit(position: Int)
    fun delete(position: Int)

    companion object {
        val NULL_ITEM_BUTTON_LISTENER = object : ItemButtonListener {
            override fun edit(position: Int) {
                // no op
            }

            override fun delete(position: Int) {
                // no op
            }

        }
    }
}
