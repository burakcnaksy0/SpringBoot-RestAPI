package b.aksoy.ILService.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// Global Exception Handler
// Bu sınıf, global hata yakalayıcıdır. (Tüm Controller sınıflarındaki hataları merkezi şekilde yakalar.)
// @ControllerAdvice: Spring’e “tüm controller’larda oluşan istisnaları ben yöneteceğim” demektir.
// @ExceptionHandler(...): Belirli bir istisna türünü yakalamak için kullanılır.
@ControllerAdvice
public class ILControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(IlNotFoundException.class)
	protected ResponseEntity<Object> handleIlNotFound(IlNotFoundException exception , WebRequest request){
		return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
}
