package pet.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.service.PetStoreService;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {

	@Autowired
	private PetStoreService petStoreService;
	
	@PostMapping("/pet_store")
	@ResponseStatus (code = HttpStatus.CREATED)
	public PetStoreData insertPetStore (@RequestBody PetStoreData petStoreData) {
		
		log.info("Creating new pet store {}" , petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}
	
	@PutMapping ("/pet_store/{petStoreId}")
	public PetStoreData updatePetStore (@PathVariable Long petStoreId, @RequestBody PetStoreData petStoreData) {
		petStoreData.setPetStoreId(petStoreId);
		log.info("Updating pet store {}", petStoreId);
		return petStoreService.savePetStore(petStoreData);
	}
	
	@PostMapping ("/pet_store/{petStoreId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee insertStoreEmployee (@PathVariable Long petStoreId, @RequestBody PetStoreEmployee storeEmployee ){
		log.info("Creating new store employee {}", storeEmployee);
		
		return petStoreService.saveEmployee(petStoreId, storeEmployee);
		
	}
	
	@PostMapping ("/pet_store/{petStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer insertStoreCustomer (@PathVariable Long petStoreId, @RequestBody PetStoreCustomer storeCustomer ){
		log.info("Creating new store customer {}", storeCustomer);
		
		return petStoreService.saveCustomer(petStoreId, storeCustomer);
	}
	
	@GetMapping ("/pet_store")
	public List<PetStoreData> retrieveAllPetStores(){
		log.info("All pet stores have been called.");
		return petStoreService.retrieveAllPetStores();
	}

	@GetMapping ("/pet_store/{petStoreId}")
	public PetStoreData retrievePetStoreById(@PathVariable Long petStoreId){
		log.info("Calling all pet stores with ID={}", petStoreId);
		return petStoreService.retrievePetStoreById(petStoreId);
	}

@DeleteMapping ("/pet_store/{petStoreId}")
public Map <String, String> deletePetStoreById (@PathVariable Long petStoreId){
	log.info("Deleting pet store with ID={}", petStoreId);
	petStoreService.deletePetStoreById(petStoreId);
	
	return Map.of("message", "Deletion of pet store with ID=" + petStoreId + " was a success.");
	
}


}
