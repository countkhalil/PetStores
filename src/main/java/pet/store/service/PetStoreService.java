package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@SuppressWarnings("unused")
@Service
public class PetStoreService {

	@Autowired
	private PetStoreDao petStoreDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	 private CustomerDao customerDao;

	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long petStoreId= petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);
		
		copyPetStoreFields(petStore,petStoreData);
		
		return new PetStoreData(petStoreDao.save(petStore));
	}

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
		
	}

	private PetStore findOrCreatePetStore(Long petStoreId) {
		PetStore petStore;
		
		if (Objects.isNull(petStoreId)) {
			 petStore = new PetStore();
		} else {
			petStore = findPetStoreById (petStoreId);
		}
		
		return petStore;
	
	}

	private PetStore findPetStoreById(Long petStoreId) {

		return petStoreDao.findById(petStoreId).orElseThrow( 
				() -> new NoSuchElementException("Pet store with ID=" + petStoreId + " was not found."));
	}

	
	
	
	@Transactional (readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee storeEmployee) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long employeeId = storeEmployee.getEmployeeId();
		Employee employee = findOrCreateEmployee(petStoreId, employeeId );
		
		copyEmployeeFields(employee, storeEmployee);
		
		employee.setPetStore(petStore);
		petStore.getEmployees().add(employee);
		
		Employee dbEmployee = employeeDao.save(employee);
		return new PetStoreEmployee(dbEmployee);
	}
	private void copyEmployeeFields (Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
	}
	
	private Employee findOrCreateEmployee (Long petStoreId, Long employeeId) {
		Employee employee;
		if (Objects.isNull(employeeId)) {
			employee = new Employee();
		}else {
			employee = findEmployeeById(petStoreId, employeeId);
		}
		
		return employee;
	}
	
	@Transactional (readOnly = true)
 	public Employee findEmployeeById (Long petStoreId, Long employeeId) {
		findPetStoreById(petStoreId);
		Employee employee = employeeDao.findById(employeeId).orElseThrow(
				() -> new NoSuchElementException("Employee with ID=" + employeeId + " does not exist"));
		
		if (employee.getPetStore().getPetStoreId() == petStoreId) {
			return  employee;
		} else {
			throw new IllegalStateException("Pet Store with ID=" + petStoreId + "does not have an employee with ID=" + employeeId);
		}
		
	}

	
	
	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer storeCustomer) {
		//PetStore petStore = findPetStoreById(petStoreId);
		Long customerId = storeCustomer.getCustomerId();
		Customer customer = findOrCreateCustomer(petStoreId, customerId);
	//	Set<PetStore> petStores = petStoreDao.findAllByPetStoreIn(storeCustomer);
		
		
		
		copyCustomerFields(customer, storeCustomer);
		
		for (PetStore  petStoreVari : customer.getPetStores()) {
			petStoreVari.getCustomers().add(customer);
			customer.getPetStores().add(petStoreVari);
		}
		
		Customer dbCustomer = customerDao.save(customer);
		return new PetStoreCustomer(dbCustomer);
		
		
	}
	
	private void copyCustomerFields (Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
		customer.setCustomerId(petStoreCustomer.getCustomerId());
	}

	private Customer findOrCreateCustomer (Long petStoreId, Long customerId) {
		Customer customer;
		if (Objects.isNull(customerId)) {
			customer = new Customer();
		}else {
			customer = findCustomerById(petStoreId, customerId);
		}
		return customer;
	}
	
	private Customer findCustomerById (Long petStoreId, Long customerId) {
		findPetStoreById(petStoreId);
		Customer customer = customerDao.findById(customerId).orElseThrow(
				() -> new NoSuchElementException("Customer with ID=" + customerId + " does not exist"));
		
		
		if (customer.getPetStores().contains(findPetStoreById(petStoreId))) {
			return customer;
		}else {
			throw new IllegalStateException("Pet Store with ID=" + petStoreId + "does not have a customer with ID=" + customerId);
		}
		
		
			
		}

	
	@Transactional (readOnly = true)
	public List<PetStoreData> retrieveAllPetStores() {
		List <PetStore> petStores = petStoreDao.findAll();
		List <PetStoreData> response = new LinkedList<>();
		
		for (PetStore petStore : petStores) {
			PetStoreData psd = new PetStoreData(petStore);
			
			psd.getCustomers().clear();
			psd.getEmployees().clear();
			response.add(psd);
		}
		return response;
	}


	@Transactional (readOnly = true)
	public PetStoreData retrievePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		return new PetStoreData(petStore);
	}

	public void deletePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		petStoreDao.delete(petStore);
		
	}
			
		
			
		
	
	
}
