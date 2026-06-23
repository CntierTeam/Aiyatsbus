package nereusopuscore.utils

class Node(
    val words: MutableSet<String> = HashSet()
) {
    private var end = false
    private val son: MutableMap<Char, Node> = HashMap()

    fun setEnd() {
        end = true
    }

    fun removeWord(word: String) {
        words.remove(word)
    }

    fun getSon(key: Char): Node? {
        return son[key]
    }

    fun getSonMap(): MutableMap<Char, Node> {
        return son
    }

    fun getIsEnd(): Boolean {
        return end
    }

    fun addSon(key: Char, node: Node) {
        son[key] = node
    }

    fun addWord(word: String) {
        words.add(word)
    }
}
