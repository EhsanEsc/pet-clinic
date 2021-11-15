package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.priceCalculators.SimplePriceCalculator;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimplePriceCalculatorTests {
    Pet nonRarePet;
	Pet rarePet;
	PetType nonRarePetType;
	PetType rarePetType;

	UserType userType;
	double baseCharge = -1;
	double basePricePerPet = -1;
	List<Pet> pets;

	private static final double BASE_RARE_COEF = 1.2;

	SimplePriceCalculator simplePriceCalculator;

	@BeforeEach
	public void setup() {
		nonRarePet = mock(Pet.class);
		nonRarePetType = mock(PetType.class);
		lenient().when(nonRarePet.getType()).thenReturn(nonRarePetType);
		lenient().when(nonRarePetType.getRare()).thenReturn(false);

		rarePet = mock(Pet.class);
		rarePetType = mock(PetType.class);
		lenient().when(rarePet.getType()).thenReturn(rarePetType);
		lenient().when(rarePetType.getRare()).thenReturn(true);

		simplePriceCalculator = new SimplePriceCalculator();
	}

	// Path: {1, 2, 7, 8, 9}
	@Test
	public void emptyPetsForNewUser() {
		userType = UserType.NEW;
		pets = Collections.emptyList();
		baseCharge = 10;
		basePricePerPet = 1;
		double expectedPrice = baseCharge*UserType.NEW.discountRate;
		assertEquals(expectedPrice, simplePriceCalculator.calcPrice
			(pets, baseCharge, basePricePerPet, userType));
	}

	// Path: {1, 2, 7, 9}
	@Test
	public void emptyPetsForSilverUser() {
		userType = UserType.SILVER;
		pets = Collections.emptyList();
		baseCharge = 10;
		basePricePerPet = 1;
		double expectedPrice = baseCharge;
		assertEquals(expectedPrice, simplePriceCalculator.calcPrice
			(pets, baseCharge, basePricePerPet, userType));
	}

	// Path: {1, 2, 3, 4, 6, 10, 2, 7, 8, 9}
	@Test
	public void rarePetsForNewUser() {
		userType = UserType.NEW;
		pets = Collections.singletonList(rarePet);
		baseCharge = 10;
		basePricePerPet = 1;
		double expectedPrice =
			(basePricePerPet*BASE_RARE_COEF+baseCharge)*UserType.NEW.discountRate;
		assertEquals(expectedPrice, simplePriceCalculator.calcPrice
			(pets, baseCharge, basePricePerPet, userType));
	}

	// Path: {1, 2, 3, 5, 6, 10, 2, 7, 8, 9}
	@Test
	public void nonRarePetsForNewUser() {
		userType = UserType.NEW;
		pets = Collections.singletonList(nonRarePet);
		baseCharge = 10;
		basePricePerPet = 1;
		double expectedPrice =
			(basePricePerPet+baseCharge)*UserType.NEW.discountRate;
		assertEquals(expectedPrice, simplePriceCalculator.calcPrice
			(pets, baseCharge, basePricePerPet, userType));
	}

	// Path: {1, 2, 3, 4, 6, 10, 2, 7, 9}
	@Test
	public void rarePetsForSilverUser() {
		userType = UserType.SILVER;
		pets = Collections.singletonList(rarePet);
		baseCharge = 10;
		basePricePerPet = 1;
		double expectedPrice = (basePricePerPet*BASE_RARE_COEF+baseCharge);
		assertEquals(expectedPrice, simplePriceCalculator.calcPrice
			(pets, baseCharge, basePricePerPet, userType));
	}

	// Path: {1, 2, 3, 5, 6, 10, 2, 7, 9}
	@Test
	public void nonRarePetsForSilverUser() {
		userType = UserType.SILVER;
		pets = Collections.singletonList(nonRarePet);
		baseCharge = 10;
		basePricePerPet = 1;
		double expectedPrice = (basePricePerPet+baseCharge);
		assertEquals(expectedPrice, simplePriceCalculator.calcPrice
			(pets, baseCharge, basePricePerPet, userType));
	}
}
