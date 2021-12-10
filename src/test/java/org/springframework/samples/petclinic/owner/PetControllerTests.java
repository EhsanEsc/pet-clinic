package org.springframework.samples.petclinic.owner;

import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.assertj.core.util.Lists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTests {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private PetRepository pets;
	@MockBean
	private OwnerRepository owners;

	private static final int OID = 12;
	private static final int PID = 22;
	private static final String PT_NAME = "PERSIAN";

	@BeforeEach
	void setup() {
		PetType catType = new PetType();
		catType.setId(1);
		catType.setName(PT_NAME);
		Pet pet = new Pet();
		pet.setId(PID);
		given(this.pets.findPetTypes()).willReturn(Lists.newArrayList(catType));
		given(this.owners.findById(OID)).willReturn(new Owner());
		given(this.pets.findById(PID)).willReturn(pet);
	}

	@Test
	void testInitCreationForm() throws Exception {
		ResultActions ra = mockMvc.perform(get("/owners/{ownerId}/pets/new", OID));
		ra.andExpect(status().isOk());
		ra.andExpect(model().attributeExists("pet"));
	}

	@Test
	void testProcessCreationFormSuccess() throws Exception {
		ResultActions ra = mockMvc.perform(post("/owners/{ownerId}/pets/new", OID)
				.param("name", "Roach")
				.param("type", PT_NAME)
				.param("birthDate", "1379-01-02"));
		ra.andExpect(status().is3xxRedirection());
		ra.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@Test
	void testInitUpdateForm() throws Exception {
		ResultActions ra = mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", OID, PID));
		ra.andExpect(status().isOk());
		ra.andExpect(model().attributeExists("pet"));
		ra.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		ResultActions ra = mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", OID, PID)
				.param("name", "AnotherRoach")
				.param("type", PT_NAME)
				.param("birthDate", "2222-02-22"));
		ra.andExpect(status().is3xxRedirection());
		ra.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
}
