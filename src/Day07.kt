data class Card(val value: Int, val playWithJoker: Boolean = false): Comparable<Card> {
    constructor(cardString: String, playWithJoker: Boolean = false): this(getValueFromString(cardString, playWithJoker)){}

    
    companion object CardString{
        fun getValueFromString(cardString: String, playWithJoker: Boolean = false): Int {
            return when(cardString) {
                "T" -> 10
                "J" -> if(playWithJoker) 0 else 11
                "Q" -> 12
                "K" -> 13
                "A" -> 14
                else -> 0
            }
        }
    }

    override fun compareTo(other: Card): Int {
        return this.value.compareTo(other.value)
    }
}

enum class HandType(val rank: Int) {
    FIVE_OF_A_KIND(7),
    FOUR_OF_A_KIND(6),
    FULL_HOUSE(5),
    THREE_OF_A_KIND(4),
    TWO_PAIR(3),
    ONE_PAIR(2),
    HIGH_CARD(1),
    
}

fun main() {
    
    data class Hand(val cards: List<Card>, val bid: Int, val playWithJoker: Boolean = false) :Comparable<Hand> {
        var hand_type: HandType
        
        init {
            this.hand_type = getHandType(this.cards)
            if(this.playWithJoker) {
                this.hand_type = getJokerHandType(this.cards)
            }
        }
        
        fun getJokerHandType(cards: List<Card>): HandType {
            val jokerInstance = Card(0)
            if(cards.contains(jokerInstance)) {
                var mapOfCards: MutableMap<Card, Int> = mutableMapOf()
                cards.forEach {card ->mapOfCards[card] = mapOfCards.getOrDefault(card, 0) + 1}
                
                return when(this.hand_type) {
                    HandType.FIVE_OF_A_KIND -> HandType.FIVE_OF_A_KIND
                    HandType.FOUR_OF_A_KIND -> HandType.FIVE_OF_A_KIND // XJJJJ -> JJJJJ or JXXXX -> XXXXX
                    HandType.FULL_HOUSE -> HandType.FIVE_OF_A_KIND // XXJJJ or JJXXX
                    HandType.THREE_OF_A_KIND -> HandType.FOUR_OF_A_KIND//XXXJY or JJJXY -> XXXXY
                    HandType.TWO_PAIR -> { 
                        if(mapOfCards[jokerInstance] == 2) { // XXJJY -> XXXXY or XXYYY
                            HandType.FOUR_OF_A_KIND
                        } else { // JXXYY -> XXXYY or XXYYY
                            HandType.FULL_HOUSE
                        }
                    }
                    HandType.ONE_PAIR -> HandType.THREE_OF_A_KIND // JJXYZ OR XXJYZ -> XXXYZ
                    HandType.HIGH_CARD -> HandType.ONE_PAIR
                    else -> this.hand_type
                }
            }
            
            return this.hand_type
        }
        
        fun getHandType(cards: List<Card>): HandType {
            // Initialize Map
            var mapOfCards: MutableMap<Card, Int> = mutableMapOf()
            cards.forEach {card ->mapOfCards[card] = mapOfCards.getOrDefault(card, 0) + 1}
            
            // 5 Of A Kind
            if(mapOfCards.values.filter { it -> it == 5 }.isNotEmpty()) {
                return HandType.FIVE_OF_A_KIND
            }
            
            // 4 Of a Kind
            if(mapOfCards.values.filter {it == 4}.isNotEmpty()) {
                return HandType.FOUR_OF_A_KIND
            }
            
            // Full House or Three of a Kind
            if(mapOfCards.values.filter {it == 3}.isNotEmpty()) {
                // Full House
                if(mapOfCards.values.filter {it == 2}.isNotEmpty()) {
                    return HandType.FULL_HOUSE
                } else {
                    return HandType.THREE_OF_A_KIND
                }
            }
            
            // Two Pair or One Pair
            if(mapOfCards.values.filter {it == 2}.isNotEmpty()) {
                // Two Pairs
                if(mapOfCards.values.filter{it == 2}.size == 2) {
                    return HandType.TWO_PAIR
                } else {
                    return HandType.ONE_PAIR
                }
            }
            
            return HandType.HIGH_CARD
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other?.javaClass != javaClass) return false

            other as Hand
            return this.compareTo(other) == 0
        }
        
        override fun compareTo(other: Hand): Int {
            if(this.hand_type.rank > other.hand_type.rank) {
                return 1
            } else if(this.hand_type.rank < other.hand_type.rank) {
                return -1
            } else {
                // Same hand, go card by card
                if(this.cards.size == other.cards.size) {
                    for(index in 0 until this.cards.size) {
                        if(this.cards[index].value > other.cards[index].value) {
                            return 1
                        } else if(this.cards[index].value < other.cards[index].value) {
                            return -1
                        } else {
                            // Do nothing
                        }
                    }
                    return this.bid.compareTo(other.bid)
                } else { // Won't happen in this case
                    return 1
                }
            }
        }
        
    }
    
    fun getHand(inputString: String, playWithJoker: Boolean = false): Hand {
        val tokens = inputString.split(" ")
        val handString = tokens[0]
        val bid = tokens[1].toInt()

        val cards = handString.map {
            val cardValue = it.digitToIntOrNull()
            if(cardValue != null) {
                return@map Card(cardValue, playWithJoker)
            } else {
                return@map Card(it.toString(), playWithJoker)
            }
        }
        return Hand(cards, bid, playWithJoker)
    }
    
    fun part1(input: List<String>): Int {
        val hands = input.map{getHand(it)}
        
        val sortedHands = hands.sorted()
        
        
//        sortedHands.forEachIndexed {index, it ->
//            println("${index + 1}: Hand bid: ${it.bid}: ${it.hand_type} for hand ${it.cards.map{it.value}.joinToString(",")}")
//        }
        
        return sortedHands.mapIndexed{index, hand -> return@mapIndexed (index + 1) * hand.bid}.sum()
    }

    fun part2(input: List<String>): Int {
        val hands = input.map{getHand(it, true)}

        val sortedHands = hands.sorted()


//        sortedHands.forEachIndexed {index, it ->
//            println("${index + 1}: Hand bid: ${it.bid}: ${it.hand_type} for hand ${it.cards.map{it.value}.joinToString(",")}")
//        }

        return sortedHands.mapIndexed{index, hand -> return@mapIndexed (index + 1) * hand.bid}.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    
//    val hand1 = getHand("KK677 28")
//    val hand2 = getHand("KTJJT 220")
//    
//    println(hand1 > hand2)
//    println(hand2 > hand1)
    
    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
