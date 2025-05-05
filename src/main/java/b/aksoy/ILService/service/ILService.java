package b.aksoy.ILService.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import b.aksoy.ILService.entity.Il;
import b.aksoy.ILService.exception.ILNameValueCannotNullException;
import b.aksoy.ILService.exception.IlAlreadyExistsException;
import b.aksoy.ILService.exception.IlNotFoundException;
import b.aksoy.ILService.repository.ILRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ILService {
	private final ILRepository ilRepository;
	
	public List<Il> getIller(String name) {
	    if (name == null) {
	        return ilRepository.findAll();              // tüm illeri getir
	    } else {
	        return ilRepository.findAllByName(name);    // isme göre filtrele
	    }
	}

	public Il createIl(Il newIl) {
		// Aynı isimde il varsa hata fırlatır.
		Optional<Il> ilByName =  ilRepository.findByName(newIl.getName());
		if (ilByName.isPresent()) {  // Değer mevcutsa burası çalışır
			throw new IlAlreadyExistsException("Il already exists with name : " +newIl.getName());
		}
		if (newIl.getName() == null || newIl.getName().trim().isEmpty()) {
		    throw new ILNameValueCannotNullException("Il name cannot be null or empty");
		}
		return ilRepository.save(newIl);
	}

	public void deleteIl(String id) {
		ilRepository.deleteById(id);
	}
	
	// İstenilen ID’ye sahip il varsa döner, yoksa IlNotFoundException fırlatır.
	public Il getIlById(String id) {
		return ilRepository.findById(id)
				.orElseThrow(() -> new IlNotFoundException("Il not found with id  :" +id));
	}
	
	// Önce il’in var olup olmadığını kontrol eder.
	// Varsa ismini günceller ve kaydeder.
	public void updateIl(String id, Il newIl) {
		Il oldIl = getIlById(id);
		oldIl.setName(newIl.getName());
		
		ilRepository.save(oldIl);
	}
	
	
	
	
	
}
