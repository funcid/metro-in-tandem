package me.func.internal.model

import jakarta.persistence.*
import me.func.internal.converter.PassengerCategoryConverter

@Entity
@Table(name = "passenger")
data class Passenger(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false)
    val fullName: String,

    @ElementCollection
    @CollectionTable(name = "contact_numbers", joinColumns = [JoinColumn(name = "passenger_id")])
    val contactNumbers: Set<ContactNumber> = emptySet(),

    @Column(nullable = false)
    val gender: String,

    @Column(name = "category")
    @Convert(converter = PassengerCategoryConverter::class)
    val category: PassengerCategory,

    @Column
    val additionalInfo: String? = null,

    @Column(nullable = false)
    val hasPacemaker: Boolean = false
)

@Embeddable
data class ContactNumber(
    @Column(nullable = false)
    val number: String,

    val description: String? = null
)