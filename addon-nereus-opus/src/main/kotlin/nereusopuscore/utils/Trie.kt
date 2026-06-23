package nereusopuscore.utils

class Trie {
    private var root: Node = Node()

    constructor()

    constructor(list: List<String>) {
        addWords(list)
    }

    fun removeWord(word: String) {
        var cur = root
        val array = word.toCharArray()

        for (c in array) {
            val son = cur.getSonMap()
            val next = son[c] ?: return
            cur.removeWord(word)
            cur = next
        }
    }

    fun addWords(list: List<String>) {
        root = Node()
        for (word in list) {
            addWord(word)
        }
    }

    fun matchPrefix(prefix: String): List<String>? {
        var cur = root
        val array = prefix.toCharArray()

        for (c in array) {
            val son = cur.getSonMap()
            val next = son[c] ?: return null
            cur = next
        }

        return ArrayList(cur.words)
    }

    fun addWord(word: String) {
        var cur = root
        val array = word.toCharArray()

        for (c in array) {
            val son = cur.getSonMap()
            son.putIfAbsent(c, Node())
            cur.addWord(word)
            cur = son[c]!!
        }

        cur.setEnd()
    }

    fun getNext(node: Node?, c: Char): Node? {
        return (node ?: root).getSon(c)
    }

    fun getAllValues(): List<String> {
        return ArrayList(root.words)
    }
}
