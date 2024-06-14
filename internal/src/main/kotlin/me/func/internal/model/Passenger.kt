package me.func.internal.model

import jakarta.persistence.*
import me.func.internal.converter.PassengerCategoryConverter

@Entity
@Table(name = "passenger")
data class Passenger(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @Column(nullable = false)
    var fullName: String,

    @ElementCollection
    @CollectionTable(name = "contact_numbers", joinColumns = [JoinColumn(name = "passenger_id")])
    var contactNumbers: Set<ContactNumber> = emptySet(),

    @Column(nullable = false)
    var gender: String,

    @Column(name = "category")
    @Convert(converter = PassengerCategoryConverter::class)
    var category: PassengerCategory,

    @Column
    var additionalInfo: String? = null,

    @Column(nullable = false)
    var hasPacemaker: Boolean = false
)

@Embeddable
data class ContactNumber(
    @Column(nullable = false)
    val number: String,

    val description: String? = null
)