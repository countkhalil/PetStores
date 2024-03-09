package pet.store.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import pet.store.entity.PetStore;

@SuppressWarnings("unused")
public interface PetStoreDao extends JpaRepository<PetStore, Long>{

	//Set<PetStore> findAllByPetStoreIn(Set<String> petStores);
}
