package b.aksoy.ILService.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import b.aksoy.ILService.entity.Il;
import b.aksoy.ILService.exception.ILNameValueCannotNullException;
import b.aksoy.ILService.exception.IlAlreadyExistsException;
import b.aksoy.ILService.exception.IlNotFoundException;
import b.aksoy.ILService.service.ILService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@RestController
@RequestMapping("/iller")
public class ILController {
	
	private final ILService ilService;
	
	// Filtreli veya Tüm Listeyi Getir
	// @RequestParam(required = false): URL'deki ?name=Ankara gibi sorgu parametresini opsiyonel yapar.
	// Eğer name verilmişse: O isme sahip iller gelir.
	// Verilmemişse: Tüm iller gelir.
	@GetMapping
	public ResponseEntity<List<Il>> getAll(@RequestParam(required = false) String name){ 
		return new ResponseEntity<>(ilService.getIller(name) , HttpStatus.OK); 
	}
	
	// Tek Bir İl Getir
	// @PathVariable: URL yolundan id'yi alır.
	@GetMapping("/{id}")
	public ResponseEntity<Il> getIl(@PathVariable String id){
		return new ResponseEntity<>(getIlById(id),HttpStatus.OK);
	}
	
	// @RequestBody: JSON formatındaki istek gövdesini Il nesnesine dönüştürür.
	// Başarılıysa 201 (CREATED) döner.
	@PostMapping
	public ResponseEntity<Il> createIL(@RequestBody @Valid Il newIl){
		return new ResponseEntity<>(ilService.createIl(newIl), HttpStatus.CREATED);
		
	}
	
	// Yanıt olarak içerik döndürmez, sadece 200 OK gönderir.
	@PutMapping("/{id}")
	public ResponseEntity<String> updateIl(@PathVariable String id , @RequestBody Il newIl){
		ilService.updateIl(id , newIl);
		
		return new ResponseEntity<>( id +" plakası " +newIl.getName() + " ile güncellendi." ,HttpStatus.OK);
		
	}
	
	// Başarılıysa içerik döndürmez, sadece 200 OK döner.
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteIl(@PathVariable String id){
		ilService.deleteIl(id);
		return new ResponseEntity<>(id + " değeri silindi"  ,HttpStatus.OK);
	}
	
	private Il getIlById(String id) {
		return ilService.getIlById(id);
	}
	
	@ExceptionHandler(IlNotFoundException.class)
	public ResponseEntity<String> handleIlNotFoundExcepiton(IlNotFoundException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(IlAlreadyExistsException.class)
	public ResponseEntity<String> handleIlAlreadyExistsException(IlAlreadyExistsException exception){
		return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ILNameValueCannotNullException.class)
	public ResponseEntity<String> handleNameCannotNullException(ILNameValueCannotNullException exception){
		return new ResponseEntity<>(exception.getMessage() , HttpStatus.BAD_REQUEST);
		
	}
	
	
	
	
	
	
	
	
}
